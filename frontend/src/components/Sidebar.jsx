import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Sidebar() {
  const { user } = useAuth();
  return (
    <aside className="w-60 bg-gray-100 h-screen p-4">
      <nav className="flex flex-col gap-3">
        <Link to="/dashboard" className="hover:text-blue-600 font-medium">Inicio</Link>
        <Link to="/pacientes" className="hover:text-blue-600 font-medium">Pacientes</Link>
        <Link to="/recetas" className="hover:text-blue-600 font-medium">Recetas</Link>
        <Link to="/reportes" className="hover:text-blue-600 font-medium">Reportes</Link>
        {user?.role === "admin" && (
          <Link to="/usuarios" className="hover:text-blue-600 font-medium">Usuarios</Link>
        )}
      </nav>
    </aside>
  );
}
