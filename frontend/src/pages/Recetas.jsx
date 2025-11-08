import { useEffect, useState } from "react";
import { getRecetas, deleteReceta } from "../services/recetasService";
import { useNavigate } from "react-router-dom";

function formatDate(s) {
  if (!s) return "";
  return new Date(s).toLocaleString();
}

export default function Recetas() {
  const [list, setList] = useState([]);
  const [q, setQ] = useState("");
  const navigate = useNavigate();

  useEffect(()=> setList(getRecetas()), []);

  function refresh() { setList(getRecetas()); }

  function handleDelete(id) {
    if (!confirm("Eliminar receta?")) return;
    deleteReceta(id);
    refresh();
  }

  const filtered = list.filter(r => 
    (r.patientNombre || "").toLowerCase().includes(q.toLowerCase()) ||
    (r.patientDNI || "").includes(q) ||
    formatDate(r.createdAt).includes(q)
  );

  return (
    <div className="flex">
      <aside className="w-60 bg-gray-100 p-4 hidden md:block">
        <h3 className="font-semibold">Acciones</h3>
        <button onClick={()=>navigate('/recetas/new')} className="mt-2 bg-blue-600 text-white px-3 py-2 rounded">Nueva receta</button>
      </aside>

      <main className="flex-1 p-6">
        <h1 className="text-2xl font-semibold mb-4">Recetas</h1>

        <div className="mb-4 flex gap-2">
          <input placeholder="Buscar por paciente, DNI o fecha" value={q} onChange={(e)=>setQ(e.target.value)} className="border p-2 rounded flex-1" />
          <button onClick={()=>navigate('/recetas/new')} className="bg-green-600 text-white px-3 py-2 rounded">Nueva</button>
        </div>

        <div className="bg-white rounded shadow">
          <table className="w-full table-auto">
            <thead className="bg-gray-50">
              <tr>
                <th className="p-2 border">ID</th>
                <th className="p-2 border">Paciente</th>
                <th className="p-2 border">Fecha</th>
                <th className="p-2 border">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(r => (
                <tr key={r.id}>
                  <td className="p-2 border">{r.id}</td>
                  <td className="p-2 border">{r.patientNombre} ({r.patientDNI})</td>
                  <td className="p-2 border">{formatDate(r.createdAt)}</td>
                  <td className="p-2 border">
                    <div className="flex gap-2">
                      <button onClick={()=>navigate(`/recetas/edit/${r.id}`)} className="px-2 py-1 border rounded">Editar</button>
                      <button onClick={()=>{ navigate(`/recetas/edit/${r.id}`); }} className="px-2 py-1 border rounded">Ver</button>
                      <button onClick={()=>handleDelete(r.id)} className="px-2 py-1 border rounded">Eliminar</button>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length===0 && <tr><td colSpan="4" className="p-4 text-center text-gray-500">No hay recetas</td></tr>}
            </tbody>
          </table>
        </div>
      </main>
    </div>
  );
}
