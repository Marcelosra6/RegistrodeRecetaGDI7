import Sidebar from "../components/Sidebar";

export default function Dashboard() {
  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 p-6">
        <h1 className="text-2xl font-semibold mb-4">Óptica D'Gabriel</h1>
        <p>Bienvenido al sistema de gestión de pacientes y recetas ópticas de la óptica D'gabriel.</p>
      </main>
    </div>
  );
}
