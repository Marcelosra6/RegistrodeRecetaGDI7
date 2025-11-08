import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleBack = () => {
    navigate("/dashboard"); // Cambia la ruta si tu dashboard tiene otro path
  };

  return (
    <nav className="bg-blue-600 text-white px-6 py-3 flex justify-between items-center shadow-md">
      
      {/* IZQUIERDA: Volver + Título */}
      <div className="flex items-center gap-3">
        <button
          onClick={handleBack}
          className="flex items-center gap-1 bg-blue-500 hover:bg-blue-700 px-3 py-1 rounded-lg text-sm transition"
        >
          <span className="font-bold text-lg"></span>
          Volver
        </button>

        <h1 className="text-xl font-bold tracking-wide">
          Óptica D'Gabriel
        </h1>
      </div>

      {/* DERECHA: Usuario + Logout */}
      {user && (
        <div className="flex items-center gap-4">
          <span className="bg-blue-500 px-3 py-1 rounded-lg text-sm shadow-sm">
            {user.username} ({user.role})
          </span>

          <button
            onClick={logout}
            className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded-lg text-sm transition shadow-sm"
          >
            Cerrar sesión
          </button>
        </div>
      )}
    </nav>
  );
}
