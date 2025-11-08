export async function loginUser(username, password) {
  try {
    const response = await fetch("http://localhost/optica_backend/endpoints/login.php", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    const data = await response.json();
    if (data.success) return data.user;
    return null;
  } catch (err) {
    console.error("Error al iniciar sesión:", err);
    return null;
  }
}






/*const users = [
  { username: "admin", password: "1234", role: "admin" },
  { username: "secretaria", password: "1234", role: "user" },
];

export function loginUser(username, password) {
  return users.find(u => u.username === username && u.password === password) || null;
}*/
