import { useEffect, useState } from "react";
import { getRecetas } from "../services/recetasService";
import { enfermedades as catalogo } from "../services/enfermedadesService";

function exportCSV(filename, rows) {
  if (!rows.length) return;
  const header = Object.keys(rows[0]).join(",");
  const csv = [header].concat(rows.map(r => Object.values(r).map(v => `"${String(v).replace(/\"/g,'""')}"`).join(","))).join("\n");
  const blob = new Blob([csv], { type: "text/csv" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = filename;
  link.click();
}

export default function Reportes() {
  const [recetas, setRecetas] = useState([]);
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [result, setResult] = useState([]);

  useEffect(()=> setRecetas(getRecetas()), []);

  function run() {
    let list = recetas.slice();
    if (from) list = list.filter(r => new Date(r.createdAt) >= new Date(from));
    if (to) list = list.filter(r => new Date(r.createdAt) <= new Date(to + "T23:59:59"));
    setResult(list);
  }

  function exportResults() {
    const rows = result.map(r => ({
      id: r.id,
      paciente: r.patientNombre,
      dni: r.patientDNI,
      fecha: r.createdAt,
      enfermedades: (r.enfermedades || []).join(";"),
    }));
    exportCSV("report_recetas.csv", rows);
  }

  const counts = {};
  recetas.forEach(r => (r.enfermedades || []).forEach(e => counts[e] = (counts[e] || 0) + 1));

  return (
    <div className="flex">
      <aside className="w-60 bg-gray-100 p-4 hidden md:block">
        <h4 className="font-semibold">Reportes</h4>
        <p className="text-sm text-gray-600">Filtrar por fecha y exportar.</p>
      </aside>

      <main className="flex-1 p-6">
        <h1 className="text-2xl font-semibold mb-4">Reportes</h1>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-3 max-w-xl">
          <label>Desde<input type="date" className="border p-2 rounded w-full" value={from} onChange={(e)=>setFrom(e.target.value)} /></label>
          <label>Hasta<input type="date" className="border p-2 rounded w-full" value={to} onChange={(e)=>setTo(e.target.value)} /></label>
          <div className="flex items-end gap-2">
            <button onClick={run} className="bg-blue-600 text-white px-4 py-2 rounded">Generar</button>
            <button onClick={exportResults} className="px-4 py-2 border rounded">Exportar CSV</button>
          </div>
        </div>

        <div className="mt-6">
          <h3 className="font-semibold">Resultados ({result.length})</h3>
          <table className="w-full table-auto mt-2 bg-white rounded shadow">
            <thead className="bg-gray-50">
              <tr><th className="p-2 border">ID</th><th className="p-2 border">Paciente</th><th className="p-2 border">Fecha</th></tr>
            </thead>
            <tbody>
              {result.map(r => (<tr key={r.id}><td className="p-2 border">{r.id}</td><td className="p-2 border">{r.patientNombre}</td><td className="p-2 border">{new Date(r.createdAt).toLocaleString()}</td></tr>))}
              {result.length===0 && <tr><td colSpan="3" className="p-4 text-center text-gray-500">Sin resultados</td></tr>}
            </tbody>
          </table>
        </div>

        <div className="mt-6">
          <h3 className="font-semibold">Diagnósticos más frecuentes</h3>
          <ul className="list-disc ml-6 mt-2">
            {Object.entries(counts).map(([en, c]) => <li key={en}>{en} — {c}</li>)}
            {Object.keys(counts).length===0 && <li className="text-gray-500">No hay diagnósticos registrados</li>}
          </ul>
        </div>
      </main>
    </div>
  );
}
