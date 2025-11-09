<?php

define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'OpticaDB');

define('APP_NAME', "Sistema de Registro de Recetas Ópticas D'Gabriel");
define('APP_VERSION', '1.0.0');

header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

class Database {
    private $conn = null;

    public function getConnection() {
        if ($this->conn === null) {
            try {
                $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
                if ($this->conn->connect_error) {
                    throw new Exception("Error de conexión a la base de datos: " . $this->conn->connect_error);
                }
                $this->conn->set_charset("utf8");
            } catch (Exception $e) {
                $this->sendError(500, 'Error de conexión', $e->getMessage());
            }
        }
        return $this->conn;
    }

    public function query($sql, $params = [], $types = '') {
        $conn = $this->getConnection();
        $stmt = $conn->prepare($sql);
        if (!$stmt) {
            throw new Exception("Error en la consulta: " . $conn->error);
        }
        if (!empty($params)) {
            $stmt->bind_param($types, ...$params);
        }
        $stmt->execute();
        return $stmt;
    }

    public function callProcedure($procName, $params = [], $types = '') {
        $placeholders = str_repeat('?,', count($params));
        $placeholders = rtrim($placeholders, ',');
        $sql = "CALL $procName($placeholders)";
        return $this->query($sql, $params, $types);
    }

    public function fetchAll($stmt) {
        $result = $stmt->get_result();
        $data = [];
        if ($result) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function fetchOne($stmt) {
        $result = $stmt->get_result();
        return $result ? $result->fetch_assoc() : null;
    }

    public function close() {
        if ($this->conn !== null) {
            $this->conn->close();
            $this->conn = null;
        }
    }

    public function sendSuccess($data = [], $message = 'Operación exitosa', $code = 200) {
        http_response_code($code);
        echo json_encode([
            'success' => true,
            'message' => $message,
            'data' => $data
        ]);
        exit();
    }

    public function sendError($code = 400, $message = 'Error de servidor', $details = null) {
        http_response_code($code);
        $response = [
            'success' => false,
            'error' => $message
        ];
        if ($details !== null) {
            $response['details'] = $details;
        }
        echo json_encode($response);
        exit();
    }

    public function getRequestData() {
        $data = json_decode(file_get_contents('php://input'), true);
        return $data ? $data : [];
    }

    public function validarDNI($dni) {
        return preg_match('/^\d{8}$/', $dni);
    }

    public function limpiarString($str) {
        return trim(htmlspecialchars($str, ENT_QUOTES, 'UTF-8'));
    }

    public function validarCamposRequeridos($data, $campos) {
        $faltantes = [];
        foreach ($campos as $campo) {
            if (!isset($data[$campo]) || empty($data[$campo])) {
                $faltantes[] = $campo;
            }
        }
        return $faltantes;
    }

    public function hashPassword($password) {
        return hash('sha256', $password);
    }

    public function verificarAutenticacion() {
        $headers = getallheaders();
        if (!isset($headers['Authorization'])) {
            $this->sendError(401, 'No autorizado - Token requerido');
        }
    }

    public function registrarLog($mensaje, $tipo = 'INFO') {
        $fecha = date('Y-m-d H:i:s');
        $log = "[$fecha] [$tipo] $mensaje" . PHP_EOL;
        // file_put_contents(__DIR__ . '/../logs/app.log', $log, FILE_APPEND);
    }
}

set_error_handler(function($errno, $errstr, $errfile, $errline) {
    $db = new Database();
    $db->sendError(500, 'Error interno del servidor', [
        'error' => $errstr,
        'file' => $errfile,
        'line' => $errline
    ]);
});

set_exception_handler(function($exception) {
    $db = new Database();
    $db->sendError(500, 'Excepción no capturada: ' . $exception->getMessage());
});
?>
