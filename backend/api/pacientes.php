<?php
/**
 * API de Pacientes
 * CRUD completo usando procedimientos almacenados
 */

require_once 'config.php';

$db = new Database();
$method = $_SERVER['REQUEST_METHOD'];

try {
    switch ($method) {
        case 'GET':
            // Obtener todos los pacientes o uno específico
            if (isset($_GET['dni'])) {
                obtenerPaciente($db, $_GET['dni']);
            } else {
                obtenerPacientes($db);
            }
            break;
            
        case 'POST':
            // Crear nuevo paciente
            crearPaciente($db);
            break;
            
        case 'PUT':
            // Actualizar paciente
            actualizarPaciente($db);
            break;
            
        case 'DELETE':
            // Eliminar paciente (soft delete)
            eliminarPaciente($db);
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
 * Obtener lista de pacientes activos
 */
function obtenerPacientes($db) {
    $sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno, 
                   DATE_FORMAT(fecha_registro, '%d/%m/%Y %H:%i') as fecha_registro
            FROM paciente
            WHERE activo = TRUE
            ORDER BY apellido_paterno, apellido_materno, nombre";
    
    $stmt = $db->query($sql);
    $pacientes = $db->fetchAll($stmt);
    
    $db->sendSuccess($pacientes, 'Pacientes obtenidos exitosamente');
}

/**
 * Obtener un paciente específico
 */
function obtenerPaciente($db, $dni) {
    if (!validarDNI($dni)) {
        $db->sendError(400, 'DNI inválido');
    }
    
    $sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno,
                   DATE_FORMAT(fecha_registro, '%d/%m/%Y %H:%i') as fecha_registro,
                   activo
            FROM paciente
            WHERE DNI = ?";
    
    $stmt = $db->query($sql, [$dni], 's');
    $paciente = $db->fetchOne($stmt);
    
    if (!$paciente) {
        $db->sendError(404, 'Paciente no encontrado');
    }
    
    $db->sendSuccess($paciente, 'Paciente encontrado');
}

/**
 * Crear nuevo paciente usando procedimiento almacenado
 */
function crearPaciente($db) {
    $data = getRequestData();
    
    // Validar campos requeridos
    $camposFaltantes = validarCamposRequeridos($data, [
        'dni', 'nombre', 'apellido_paterno', 'apellido_materno'
    ]);
    
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }
    
    // Validar DNI
    if (!validarDNI($data['dni'])) {
        $db->sendError(400, 'DNI debe tener 8 dígitos');
    }
    
    // Limpiar datos
    $dni = limpiarString($data['dni']);
    $nombre = limpiarString($data['nombre']);
    $apellido_paterno = limpiarString($data['apellido_paterno']);
    $apellido_materno = limpiarString($data['apellido_materno']);
    
    // Validar longitud
    if (strlen($nombre) > 15 || strlen($apellido_paterno) > 15 || strlen($apellido_materno) > 15) {
        $db->sendError(400, 'Los nombres y apellidos no pueden exceder 15 caracteres');
    }
    
    // Llamar al procedimiento almacenado
    try {
        $stmt = $db->callProcedure(
            'sp_insertar_paciente',
            [$dni, $nombre, $apellido_paterno, $apellido_materno],
            'ssss'
        );
        
        $db->sendSuccess([
            'dni' => $dni,
            'nombre_completo' => "$apellido_paterno $apellido_materno, $nombre"
        ], 'Paciente creado exitosamente', 201);
        
    } catch (Exception $e) {
        // Verificar si es error de duplicado
        if (strpos($e->getMessage(), 'Duplicate entry') !== false) {
            $db->sendError(409, 'El DNI ya está registrado');
        }
        throw $e;
    }
}

/**
 * Actualizar paciente usando procedimiento almacenado
 */
function actualizarPaciente($db) {
    $data = getRequestData();
    
    // Validar campos requeridos
    $camposFaltantes = validarCamposRequeridos($data, [
        'dni', 'nombre', 'apellido_paterno', 'apellido_materno'
    ]);
    
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }
    
    // Validar DNI
    if (!validarDNI($data['dni'])) {
        $db->sendError(400, 'DNI debe tener 8 dígitos');
    }
    
    // Limpiar datos
    $dni = limpiarString($data['dni']);
    $nombre = limpiarString($data['nombre']);
    $apellido_paterno = limpiarString($data['apellido_paterno']);
    $apellido_materno = limpiarString($data['apellido_materno']);
    
    // Validar longitud
    if (strlen($nombre) > 15 || strlen($apellido_paterno) > 15 || strlen($apellido_materno) > 15) {
        $db->sendError(400, 'Los nombres y apellidos no pueden exceder 15 caracteres');
    }
    
    // Llamar al procedimiento almacenado
    try {
        $stmt = $db->callProcedure(
            'sp_actualizar_paciente',
            [$dni, $nombre, $apellido_paterno, $apellido_materno],
            'ssss'
        );
        
        $db->sendSuccess([
            'dni' => $dni,
            'nombre_completo' => "$apellido_paterno $apellido_materno, $nombre"
        ], 'Paciente actualizado exitosamente');
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Paciente no encontrado') !== false) {
            $db->sendError(404, 'Paciente no encontrado');
        }
        throw $e;
    }
}

/**
 * Eliminar paciente (soft delete) usando procedimiento almacenado
 */
function eliminarPaciente($db) {
    if (!isset($_GET['dni'])) {
        $db->sendError(400, 'DNI requerido');
    }
    
    $dni = limpiarString($_GET['dni']);
    
    if (!validarDNI($dni)) {
        $db->sendError(400, 'DNI inválido');
    }
    
    try {
        $stmt = $db->callProcedure('sp_eliminar_paciente', [$dni], 's');
        
        $db->sendSuccess(['dni' => $dni], 'Paciente eliminado exitosamente');
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Paciente no encontrado') !== false) {
            $db->sendError(404, 'Paciente no encontrado');
        }
        if (strpos($e->getMessage(), 'foreign key constraint') !== false) {
            $db->sendError(409, 'No se puede eliminar: el paciente tiene recetas asociadas');
        }
        throw $e;
    }
}
?>