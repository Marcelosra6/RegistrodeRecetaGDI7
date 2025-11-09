<?php

require_once 'config.php';

$db = new Database();
$conn = $db->getConnection();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    $db->sendError(405, 'Método no permitido');
}

try {
    $data = getRequestData();

    $camposFaltantes = validarCamposRequeridos($data, ['usuario', 'password']);
    if (!empty($camposFaltantes)) {
        $db->sendError(400, 'Campos requeridos faltantes', $camposFaltantes);
    }

    $usuario = limpiarString($data['usuario']);
    $password = $data['password'];
    $passwordHash = hashPassword($password);

    $sql = "SELECT u.id, u.usuario, u.rol, u.dni_oftalmologo,
                       o.nombre, o.apellido_paterno
            FROM usuarios u
            LEFT JOIN oftalmologo o ON u.dni_oftalmologo = o.DNI
            WHERE u.usuario = ? AND u.password_hash = ? AND u.activo = TRUE";
    $stmt = $db->query($sql, [$usuario, $passwordHash], 'ss');
    $user = $db->fetchOne($stmt);
    if (!$user) {
        $db->sendError(401, 'Credenciales inválidas');
    }
    $token = bin2hex(random_bytes(32));
    session_start();
    $_SESSION['user_id'] = $user['id'];
    $_SESSION['usuario'] = $user['usuario'];
    $_SESSION['rol'] = $user['rol'];
    $_SESSION['token'] = $token;

    $db->sendSuccess([
        'token' => $token,
        'user' => [
            'id' => $user['id'],
            'usuario' => $user['usuario'],
            'rol' => $user['nombre'],
            'nombre' => $user['nombre'],
            'apellido' => $user['apellido_paterno']
        ]
        ], 'Login exitoso');
} catch (Exception $e) {
    $db->sendError(500, 'Error en el proceso de autenticación', $e->getMessage());
} finally {
    $db->close();
}

?>