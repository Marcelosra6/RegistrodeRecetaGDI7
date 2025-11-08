import { useEffect, useState } from "react";
import { saveReceta, getRecetas, updateReceta } from "../services/recetasService";
import { getPacientes } from "../services/pacientesService";
import { enfermedades } from "../services/enfermedadesService";
import { useNavigate, useParams } from "react-router-dom";

export default function NuevaReceta() {
  const params = useParams();
  const isEdit = Boolean(params.id);
  const navigate = useNavigate();

  const [patients, setPatients] = useState([]);
  const [errors, setErrors] = useState({}); // 🔴 Para guardar errores de validación

  const [form, setForm] = useState({
    patientDNI: "",
    patientNombre: "",
    oftalDNI: "20123456",
    oftalNombre: "Ana Gomez Diaz",
    fechaExamen: new Date().toISOString(),
    tipo_lente: "monofocal",
    vision: "lejos",
    esfera_izq: "0.00",
    cilindro_izq: "0.00",
    eje_izq: "0",
    esfera_der: "0.00",
    cilindro_der: "0.00",
    eje_der: "0",
    adicion: "0.00",
    dip: "62",
    observaciones: "",
    enfermedades: [],
    createdAt: new Date().toISOString(),
  });

  useEffect(() => {
    setPatients(getPacientes());
    if (isEdit) {
      const r = getRecetas().find(x => String(x.id) === String(params.id));
      if (r) setForm(r);
    }
  }, []);

  // 🩺 Validaciones personalizadas
  const validateForm = () => {
    const newErrors = {};

    // DNI del paciente
    if (!/^\d{8}$/.test(form.patientDNI)) {
      newErrors.patientDNI = "El DNI debe tener exactamente 8 dígitos numéricos.";
    }

    // Campos numéricos: permiten decimales y negativos
    const numberFields = [
      "esfera_izq", "cilindro_izq", "eje_izq",
      "esfera_der", "cilindro_der", "eje_der",
      "adicion", "dip"
    ];

    numberFields.forEach(field => {
      if (form[field] === "" || isNaN(form[field])) {
        newErrors[field] = "Ingrese un valor numérico válido.";
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0; // true si no hay errores
  };

  function handleSave(e) {
    e.preventDefault();
    if (!validateForm()) {
      window.scrollTo({ top: 0, behavior: "smooth" });
      return;
    }

    if (isEdit) {
      updateReceta(form.id, form);
    } else {
      saveReceta(form);
    }
    navigate("/recetas");
  }

  function handleChange(field, value) {
    // Limpiar errores al escribir
    setErrors(prev => ({ ...prev, [field]: "" }));

    // Evitar letras en campos numéricos
    const numericFields = [
      "esfera_izq", "cilindro_izq", "eje_izq",
      "esfera_der", "cilindro_der", "eje_der",
      "adicion", "dip"
    ];

    if (field === "patientDNI") {
      // Solo permitir números y máximo 8 dígitos
      if (/^\d{0,8}$/.test(value)) {
        setForm(f => ({ ...f, [field]: value }));
      }
      return;
    }

    if (numericFields.includes(field)) {
      // Solo acepta números, decimales y signo negativo
      if (/^-?\d*\.?\d*$/.test(value)) {
        setForm(f => ({ ...f, [field]: value }));
      }
      return;
    }

    // Otros campos normales
    setForm(f => ({ ...f, [field]: value }));
  }

  function toggleEnf(id) {
    setForm(f => ({
      ...f,
      enfermedades: f.enfermedades.includes(id)
        ? f.enfermedades.filter(x => x !== id)
        : [...f.enfermedades, id],
    }));
  }

  function handlePatientSelect(e) {
    const dni = e.target.value;
    const p = patients.find(x => x.dni === dni);
    setForm(f => ({
      ...f,
      patientDNI: dni,
      patientNombre: p
        ? `${p.nombre} ${p.apellido_p} ${p.apellido_m}`
        : "",
    }));
    setErrors(prev => ({ ...prev, patientDNI: "" }));
  }

  // 🔔 Componente para mostrar errores
  const ErrorMsg = ({ message }) =>
    message ? (
      <p className="text-red-600 text-sm mt-1">{message}</p>
    ) : null;

  return (
    <div className="flex">
      <aside className="w-60 bg-gray-100 p-4 hidden md:block">
        <h3 className="font-semibold">Receta</h3>
        <p className="text-sm text-gray-600">
          Complete todos los campos y guarde.
        </p>
      </aside>

      <main className="flex-1 p-6">
        <h1 className="text-2xl font-semibold mb-4">
          {isEdit ? "Editar receta" : "Nueva receta"}
        </h1>

        <form
          onSubmit={handleSave}
          className="bg-white p-4 rounded shadow max-w-3xl"
        >
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <label>
              DNI paciente
              <input
                value={form.patientDNI}
                onChange={e => handleChange("patientDNI", e.target.value)}
                className={`border p-2 rounded w-full ${
                  errors.patientDNI ? "border-red-500" : ""
                }`}
                maxLength={8}
              />
              <ErrorMsg message={errors.patientDNI} />
            </label>

            <label>
              Seleccionar paciente
              <select
                onChange={handlePatientSelect}
                className="border p-2 rounded w-full"
                value={form.patientDNI || ""}
              >
                <option value="">-- seleccionar --</option>
                {patients.map(p => (
                  <option key={p.id} value={p.dni}>
                    {p.dni} - {p.nombre} {p.apellido_p}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Nombre paciente
              <input
                value={form.patientNombre}
                onChange={e => handleChange("patientNombre", e.target.value)}
                className="border p-2 rounded w-full"
              />
            </label>

            <label>
              Fecha examen
              <input
                type="datetime-local"
                value={form.fechaExamen.slice(0, 16)}
                onChange={e =>
                  handleChange("fechaExamen", new Date(e.target.value).toISOString())
                }
                className="border p-2 rounded w-full"
              />
            </label>
          </div>

          <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-3">
            <label>
              Tipo de lente
              <select
                value={form.tipo_lente}
                onChange={e => handleChange("tipo_lente", e.target.value)}
                className="border p-2 rounded w-full"
              >
                <option>monofocal</option>
                <option>bifocal</option>
                <option>progresivo</option>
              </select>
            </label>
            <label>
              Visión corregida
              <select
                value={form.vision}
                onChange={e => handleChange("vision", e.target.value)}
                className="border p-2 rounded w-full"
              >
                <option>lejos</option>
                <option>cerca</option>
                <option>ambos</option>
              </select>
            </label>
          </div>

          <h3 className="mt-4 font-semibold">Mediciones</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <label>
                Esfera OI
                <input
                  value={form.esfera_izq}
                  onChange={e => handleChange("esfera_izq", e.target.value)}
                  className={`border p-2 w-full rounded ${
                    errors.esfera_izq ? "border-red-500" : ""
                  }`}
                />
                <ErrorMsg message={errors.esfera_izq} />
              </label>

              <label>
                Cilindro OI
                <input
                  value={form.cilindro_izq}
                  onChange={e => handleChange("cilindro_izq", e.target.value)}
                  className={`border p-2 w-full rounded mt-2 ${
                    errors.cilindro_izq ? "border-red-500" : ""
                  }`}
                />
                <ErrorMsg message={errors.cilindro_izq} />
              </label>

              <label>
                Eje OI
                <input
                  value={form.eje_izq}
                  onChange={e => handleChange("eje_izq", e.target.value)}
                  className={`border p-2 w-full rounded mt-2 ${
                    errors.eje_izq ? "border-red-500" : ""
                  }`}
                />
                <ErrorMsg message={errors.eje_izq} />
              </label>
            </div>

            <div>
              <label>
                Esfera OD
                <input
                  value={form.esfera_der}
                  onChange={e => handleChange("esfera_der", e.target.value)}
                  className={`border p-2 w-full rounded ${
                    errors.esfera_der ? "border-red-500" : ""
                  }`}
                />
                <ErrorMsg message={errors.esfera_der} />
              </label>

              <label>
                Cilindro OD
                <input
                  value={form.cilindro_der}
                  onChange={e => handleChange("cilindro_der", e.target.value)}
                  className={`border p-2 w-full rounded mt-2 ${
                    errors.cilindro_der ? "border-red-500" : ""
                  }`}
                />
                <ErrorMsg message={errors.cilindro_der} />
              </label>

              <label>
                Eje OD
                <input
                  value={form.eje_der}
                  onChange={e => handleChange("eje_der", e.target.value)}
                  className={`border p-2 w-full rounded mt-2 ${
                    errors.eje_der ? "border-red-500" : ""
                  }`}
                />
                <ErrorMsg message={errors.eje_der} />
              </label>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mt-3">
            <label>
              Adición
              <input
                value={form.adicion}
                onChange={e => handleChange("adicion", e.target.value)}
                className={`border p-2 rounded w-full ${
                  errors.adicion ? "border-red-500" : ""
                }`}
              />
              <ErrorMsg message={errors.adicion} />
            </label>

            <label>
              DIP (mm)
              <input
                value={form.dip}
                onChange={e => handleChange("dip", e.target.value)}
                className={`border p-2 rounded w-full ${
                  errors.dip ? "border-red-500" : ""
                }`}
              />
              <ErrorMsg message={errors.dip} />
            </label>
          </div>

          <label className="mt-3">
            Observaciones
            <textarea
              value={form.observaciones}
              onChange={e => handleChange("observaciones", e.target.value)}
              className="border p-2 rounded w-full"
            />
          </label>

          <div className="mt-3">
            <h4 className="font-semibold">Diagnósticos</h4>
            <div className="flex gap-2 flex-wrap mt-2">
              {enfermedades.map((en, i) => (
                <label key={i} className="border p-2 rounded">
                  <input
                    type="checkbox"
                    checked={form.enfermedades.includes(en)}
                    onChange={() => toggleEnf(en)}
                  />{" "}
                  {en}
                </label>
              ))}
            </div>
          </div>

          <div className="mt-4 flex gap-2">
            <button
              className="bg-green-600 text-white px-4 py-2 rounded"
              type="submit"
            >
              Guardar
            </button>
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="px-4 py-2 border rounded"
            >
              Cancelar
            </button>
          </div>
        </form>
      </main>
    </div>
  );
}
