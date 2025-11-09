<?php
/**
 * API de Oftalmólogos
 * CRUD completo usando procedimientos almacenados
 */

require_once 'config.php';

$db = new Database();
$method = $_SERVER['REQUEST_METHOD'];

try {
    switch ($method) {
        case 'GET':
            obtenerOftalmologos($db);
            break;
            
        case 'POST':
            crearOftalmologo($db);
            break;
            
        case 'PUT':
            actualizarOftalmologo($db);
            break;
            
        case 'DELETE':
            eliminarOftalmologo($db);
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
 * Obtener lista de oftalmólogos activos
 */
function obtenerOftalmologos($db) {
    $sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno, especialidad
            FROM oftalmologo
            WHERE activo = TRUE
            ORDER BY apellido_paterno, apellido_materno";
    
    $stmt = $db->query($sql);
    $oftalmologos = $db->fetchAll($stmt);
    
    $db->sendSuccess($oftalmologos, 'Oftalmólogos obtenidos exitosamente');
}

/**
 * Crear nuevo oftalmólogo
 */
function crearOftalmologo($db) {
    $data = getRequestData();
    
    $camposFaltantes = validarCamposRequeridos($data, [
        'dni', 'nombre', 'apellido_paterno', 'apellido_materno', 'especialidad'
    ]);
    
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }
    
    if (!validarDNI($data['dni'])) {
        $db->sendError(400, 'DNI debe tener 8 dígitos');
    }
    
    $dni = limpiarString($data['dni']);
    $nombre = limpiarString($data['nombre']);
    $apellido_paterno = limpiarString($data['apellido_paterno']);
    $apellido_materno = limpiarString($data['apellido_materno']);
    $especialidad = limpiarString($data['especialidad']);
    
    if (strlen($nombre) > 15 || strlen($apellido_paterno) > 15 || strlen($apellido_materno) > 15) {
        $db->sendError(400, 'Los nombres y apellidos no pueden exceder 15 caracteres');
    }
    
    try {
        $stmt = $db->callProcedure(
            'sp_insertar_oftalmologo',
            [$dni, $nombre, $apellido_paterno, $apellido_materno, $especialidad],
            'sssss'
        );
        
        $db->sendSuccess([
            'dni' => $dni,
            'nombre_completo' => "$apellido_paterno $apellido_materno, $nombre",
            'especialidad' => $especialidad
        ], 'Oftalmólogo creado exitosamente', 201);
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Duplicate entry') !== false) {
            $db->sendError(409, 'El DNI ya está registrado');
        }
        throw $e;
    }
}

/**
 * Actualizar oftalmólogo
 */
function actualizarOftalmologo($db) {
    $data = getRequestData();
    
    $camposFaltantes = validarCamposRequeridos($data, [
        'dni', 'nombre', 'apellido_paterno', 'apellido_materno', 'especialidad'
    ]);
    
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }
    
    if (!validarDNI($data['dni'])) {
        $db->sendError(400, 'DNI debe tener 8 dígitos');
    }
    
    $dni = limpiarString($data['dni']);
    $nombre = limpiarString($data['nombre']);
    $apellido_paterno = limpiarString($data['apellido_paterno']);
    $apellido_materno = limpiarString($data['apellido_materno']);
    $especialidad = limpiarString($data['especialidad']);
    
    try {
        $stmt = $db->callProcedure(
            'sp_actualizar_oftalmologo',
            [$dni, $nombre, $apellido_paterno, $apellido_materno, $especialidad],
            'sssss'
        );
        
        $db->sendSuccess([
            'dni' => $dni,
            'nombre_completo' => "$apellido_paterno $apellido_materno, $nombre"
        ], 'Oftalmólogo actualizado exitosamente');
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Oftalmólogo no encontrado') !== false) {
            $db->sendError(404, 'Oftalmólogo no encontrado');
        }
        throw $e;
    }
}

/**
 * Eliminar oftalmólogo (soft delete)
 */
function eliminarOftalmologo($db) {
    if (!isset($_GET['dni'])) {
        $db->sendError(400, 'DNI requerido');
    }
    
    $dni = limpiarString($_GET['dni']);
    
    if (!validarDNI($dni)) {
        $db->sendError(400, 'DNI inválido');
    }
    
    try {
        $stmt = $db->callProcedure('sp_eliminar_oftalmologo', [$dni], 's');
        
        $db->sendSuccess(['dni' => $dni], 'Oftalmólogo eliminado exitosamente');
        
    } catch (Exception $e) {
        if (strpos($e->getMessage(), 'Oftalmólogo no encontrado') !== false) {
            $db->sendError(404, 'Oftalmólogo no encontrado');
        }
        if (strpos($e->getMessage(), 'foreign key constraint') !== false) {
            $db->sendError(409, 'No se puede eliminar: el oftalmólogo tiene recetas asociadas');
        }
        throw $e;
    }
}
?>