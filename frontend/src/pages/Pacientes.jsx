import { useEffect, useState } from "react";
import {
  getPacientes,
  savePaciente,
  updatePaciente,
  deletePaciente,
} from "../services/pacientesService";

export default function Pacientes() {
  const [list, setList] = useState([]);
  const [query, setQuery] = useState("");
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ dni: "", nombre: "", apellido_p: "", apellido_m: "" });
  const [error, setError] = useState("");


  useEffect(() => {
    setList(getPacientes());
  }, []);

  function refresh() {
    setList(getPacientes());
  }

  function handleSave(e) {
    e.preventDefault();

    if (!/^\d{8}$/.test(form.dni)) {
      setError("El DNI debe contener exactamente 8 dígitos numéricos.");
      return;
    }

    if (!form.nombre.trim()) {
      setError("El nombre es obligatorio.");
      return;
    }

    setError(""); // limpiar errores si todo está correcto

    if (editing) {
      updatePaciente(editing.id, { ...form, id: editing.id });
    } else {
      savePaciente(form);
    }

    setForm({ dni: "", nombre: "", apellido_p: "", apellido_m: "" });
    setEditing(null);
    refresh();
  }



  function handleEdit(p) {
    setEditing(p);
    setForm({ dni: p.dni, nombre: p.nombre, apellido_p: p.apellido_p, apellido_m: p.apellido_m });
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  function handleDelete(id) {
    if (!confirm("Eliminar paciente?")) return;
    deletePaciente(id);
    refresh();
  }

  const filtered = list.filter(
    (p) =>
      p.nombre.toLowerCase().includes(query.toLowerCase()) ||
      p.dni.includes(query) ||
      `${p.apellido_p} ${p.apellido_m}`.toLowerCase().includes(query.toLowerCase())
  );

  return (
    <div className="flex">
      <aside className="w-60 bg-gray-100 p-4 hidden md:block">
        <h3 className="font-semibold mb-2">Menú</h3>
        <p className="text-sm text-gray-600">Acciones: crear, editar, eliminar pacientes</p>
      </aside>

      <main className="flex-1 p-6">
        <h1 className="text-2xl font-semibold mb-4">Pacientes</h1>

        <form onSubmit={handleSave} className="bg-white p-4 rounded shadow mb-6 max-w-2xl">
          <h2 className="font-medium mb-2">{editing ? "Editar paciente" : "Nuevo paciente"}</h2>
            {error && (
              <div className="bg-red-100 text-red-700 p-2 rounded mb-3 border border-red-300 text-sm">
                {error}
              </div>
            )}

          <div className="grid grid-cols-1 md:grid-cols-4 gap-3">
            

            <input
              className={`border p-2 rounded ${form.dni && !/^\d{8}$/.test(form.dni) ? "border-red-500" : ""}`}
              placeholder="DNI"
              maxLength={8}
              value={form.dni}
              onChange={(e) => {
                const onlyNums = e.target.value.replace(/\D/g, "");
                setForm({ ...form, dni: onlyNums });
              }}
            />
            <input className="border p-2 rounded md:col-span-1" placeholder="Nombre" value={form.nombre} onChange={(e)=>setForm({...form,nombre:e.target.value})} />
            <input className="border p-2 rounded" placeholder="Apellido paterno" value={form.apellido_p} onChange={(e)=>setForm({...form,apellido_p:e.target.value})} />
            <input className="border p-2 rounded" placeholder="Apellido materno" value={form.apellido_m} onChange={(e)=>setForm({...form,apellido_m:e.target.value})} />
          </div>
          <div className="mt-3 flex gap-2">
            <button className="bg-green-600 text-white px-4 py-2 rounded" type="submit">{editing ? "Actualizar" : "Guardar"}</button>
            <button type="button" className="px-4 py-2 border rounded" onClick={()=>{ setEditing(null); setForm({dni:'',nombre:'',apellido_p:'',apellido_m:''}) }}>Cancelar</button>
          </div>
        </form>

        <div className="mb-4 flex gap-2">
          <input placeholder="Buscar por nombre o DNI" value={query} onChange={(e)=>setQuery(e.target.value)} className="border p-2 rounded flex-1" />
        </div>

        <div className="bg-white rounded shadow">
          <table className="w-full table-auto">
            <thead className="bg-gray-50">
              <tr>
                <th className="p-2 border">DNI</th>
                <th className="p-2 border">Nombre</th>
                <th className="p-2 border">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((p) => (
                <tr key={p.id}>
                  <td className="p-2 border">{p.dni}</td>
                  <td className="p-2 border">{p.nombre} {p.apellido_p} {p.apellido_m}</td>
                  <td className="p-2 border">
                    <div className="flex gap-2">
                      <button onClick={()=>handleEdit(p)} className="px-2 py-1 border rounded">Editar</button>
                      <button onClick={()=>handleDelete(p.id)} className="px-2 py-1 border rounded">Eliminar</button>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length===0 && (
                <tr><td colSpan="3" className="p-4 text-center text-gray-500">No hay pacientes</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </main>
    </div>
  );
}
