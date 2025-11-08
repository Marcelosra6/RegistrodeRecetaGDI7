const key = "pacientes";

export const getPacientes = () => JSON.parse(localStorage.getItem(key)) || [];
export const savePaciente = (paciente) => {
  const data = getPacientes();
  paciente.id = Date.now();
  localStorage.setItem(key, JSON.stringify([...data, paciente]));
};
export const updatePaciente = (id, updated) => {
  const data = getPacientes().map(p => p.id === id ? updated : p);
  localStorage.setItem(key, JSON.stringify(data));
};
export const deletePaciente = (id) => {
  const data = getPacientes().filter(p => p.id !== id);
  localStorage.setItem(key, JSON.stringify(data));
};
