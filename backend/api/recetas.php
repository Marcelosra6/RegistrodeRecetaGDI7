<?php
/**
 * API de Recetas
 * CRUD completo con detalles y enfermedades
 */

require_once 'config.php';

$db = new Database();
$method = $_SERVER['REQUEST_METHOD'];

try {
    // Determinar acción específica
    $action = isset($_GET['action']) ? $_GET['action'] : null;
    
    switch ($method) {
        case 'GET':
            if ($action === 'detalle' && isset($_GET['id'])) {
                obtenerRecetaDetalle($db, $_GET['id']);
            } else {
                obtenerRecetas($db);
            }
            break;
            
        case 'POST':
            crearReceta($db);
            break;
            
        case 'PUT':
            actualizarReceta($db);
            break;
            
        case 'DELETE':
            eliminarReceta($db);
            break;
            
        default:
            $db->sendError(405, 'Método no permitido');
    }
    
} catch (Exception $e) {
    $db->sendError(500, 'Error en la operación', $e->getMessage());
} finally {
    $db->close();
}

/**
 * Obtener todas las recetas con información básica
 */
function obtenerRecetas($db) {
    $sql = "SELECT 
                r.id_receta,
                r.DNI_paciente,
                CONCAT(p.apellido_paterno, ' ', p.apellido_materno, ', ', p.nombre) AS paciente,
                r.DNI_oftalmologo,
                CONCAT(o.apellido_paterno, ' ', o.nombre) AS oftalmologo,
                DATE_FORMAT(r.fecha_examen, '%d/%m/%Y %H:%i') AS fecha_examen,
                r.diagnostico_general
            FROM receta r
            INNER JOIN paciente p ON r.DNI_paciente = p.DNI
            INNER JOIN oftalmologo o ON r.DNI_oftalmologo = o.DNI
            ORDER BY r.fecha_examen DESC";
    
    $stmt = $db->query($sql);
    $recetas = $db->fetchAll($stmt);
    
    $db->sendSuccess($recetas, 'Recetas obtenidas exitosamente');
}

/**
 * Obtener detalle completo de una receta
 */
function obtenerRecetaDetalle($db, $id) {
    // Obtener receta principal
    $sql = "SELECT 
                r.*,
                CONCAT(p.nombre, ' ', p.apellido_paterno, ' ', p.apellido_materno) AS paciente_completo,
                CONCAT(o.nombre, ' ', o.apellido_paterno) AS oftalmologo_completo,
                o.especialidad,
                DATE_FORMAT(r.fecha_examen, '%d/%m/%Y %H:%i') AS fecha_examen_formateada
            FROM receta r
            INNER JOIN paciente p ON r.DNI_paciente = p.DNI
            INNER JOIN oftalmologo o ON r.DNI_oftalmologo = o.DNI
            WHERE r.id_receta = ?";
    
    $stmt = $db->query($sql, [$id], 'i');
    $receta = $db->fetchOne($stmt);
    
    if (!$receta) {
        $db->sendError(404, 'Receta no encontrada');
    }
    
    // Obtener detalles de la receta
    $sql = "SELECT * FROM detalle_receta WHERE id_receta = ?";
    $stmt = $db->query($sql, [$id], 'i');
    $detalles = $db->fetchAll($stmt);
    
    // Obtener enfermedades
    $sql = "SELECT enfermedad FROM enfermedad WHERE id_receta = ?";
    $stmt = $db->query($sql, [$id], 'i');
    $enfermedades = $db->fetchAll($stmt);
    
    $receta['detalles'] = $detalles;
    $receta['enfermedades'] = array_column($enfermedades, 'enfermedad');
    
    $db->sendSuccess($receta, 'Detalle de receta obtenido');
}

/**
 * Crear nueva receta con detalles y enfermedades
 */
function crearReceta($db) {
    $data = getRequestData();
    
    // Validar campos requeridos de la receta
    $camposFaltantes = validarCamposRequeridos($data, [
        'dni_paciente', 'dni_oftalmologo', 'fecha_examen'
    ]);
    
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }
    
    // Validar DNIs
    if (!validarDNI($data['dni_paciente']) || !validarDNI($data['dni_oftalmologo'])) {
        $db->sendError(400, 'DNI inválido');
    }
    
    $conn = $db->getConnection();
    
    // Iniciar transacción
    $conn->begin_transaction();
    
    try {
        // Insertar receta principal
        $dni_paciente = limpiarString($data['dni_paciente']);
        $dni_oftalmologo = limpiarString($data['dni_oftalmologo']);
        $fecha_examen = $data['fecha_examen'];
        $diagnostico = isset($data['diagnostico']) ? limpiarString($data['diagnostico']) : '';
        
        // Llamar procedimiento para insertar receta
        $sql = "CALL sp_insertar_receta(?, ?, ?, ?, @id_receta)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param('ssss', $dni_paciente, $dni_oftalmologo, $fecha_examen, $diagnostico);
        $stmt->execute();
        $stmt->close();
        
        // Obtener el ID de la receta creada
        $result = $conn->query("SELECT @id_receta as id_receta");
        $row = $result->fetch_assoc();
        $id_receta = $row['id_receta'];
        
        // Insertar detalle si existe
        if (isset($data['detalle']) && !empty($data['detalle'])) {
            $det = $data['detalle'];
            
            $stmt = $db->callProcedure(
                'sp_insertar_detalle_receta',
                [
                    $id_receta,
                    $det['tipo_lente'],
                    $det['vision_corregida'],
                    isset($det['observaciones']) ? $det['observaciones'] : '',
                    $det['esf_ojo_izquierdo'],
                    $det['cilindro_ojo_izquierdo'],
                    $det['eje_ojo_izquierdo'],
                    $det['esf_ojo_derecho'],
                    $det['cilindro_ojo_derecho'],
                    $det['eje_ojo_derecho'],
                    $det['adicional'],
                    $det['distancia_interpupilar']
                ],
                'isssddddddi'
            );
        }
        
        // Insertar enfermedades si existen
        if (isset($data['enfermedades']) && is_array($data['enfermedades'])) {
            $sql = "INSERT INTO enfermedad (id_receta, enfermedad) VALUES (?, ?)";
            $stmt = $conn->prepare($sql);
            
            foreach ($data['enfermedades'] as $enfermedad) {
                $enfermedad = limpiarString($enfermedad);
                $stmt->bind_param('is', $id_receta, $enfermedad);
                $stmt->execute();
            }
            $stmt->close();
        }
        
        // Confirmar transacción
        $conn->commit();
        
        $db->sendSuccess([
            'id_receta' => $id_receta
        ], 'Receta creada exitosamente', 201);
        
    } catch (Exception $e) {
        $conn->rollback();
        throw $e;
    }
}

/**
 * Actualizar receta existente
 */
function actualizarReceta($db) {
    $data = getRequestData();
    
    $camposFaltantes = validarCamposRequeridos($data, [
        'id_receta', 'dni_paciente', 'dni_oftalmologo', 'fecha_examen'
    ]);
    
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }
    
    if (!validarDNI($data['dni_paciente']) || !validarDNI($data['dni_oftalmologo'])) {
        $db->sendError(400, 'DNI inválido');
    }
    
    try {
        $stmt = $db->callProcedure(
            'sp_actualizar_receta',
            [
                $data['id_receta'],
                $data['dni_paciente'],
                $data['dni_oftalmologo'],
                $data['fecha_examen'],
                isset($data['diagnostico']) ? $data['diagnostico'] : ''
            ],
            'issss'
        );
        
        $db->sendSuccess([
            'id_receta' => $data['id_receta']
        ], 'Receta actualizada exitosamente');
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Receta no encontrada') !== false) {
            $db->sendError(404, 'Receta no encontrada');
        }
        throw $e;
    }
}

/**
 * Eliminar receta
 */
function eliminarReceta($db) {
    if (!isset($_GET['id'])) {
        $db->sendError(400, 'ID de receta requerido');
    }
    
    $id = intval($_GET['id']);
    
    try {
        $stmt = $db->callProcedure('sp_eliminar_receta', [$id], 'i');
        
        $db->sendSuccess(['id_receta' => $id], 'Receta eliminada exitosamente');
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Receta no encontrada') !== false) {
            $db->sendError(404, 'Receta no encontrada');
        }
        throw $e;
    }
}
?>