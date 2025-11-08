const key = "recetas";

export const getRecetas = () => JSON.parse(localStorage.getItem(key)) || [];
export const saveReceta = (receta) => {
  const data = getRecetas();
  receta.id = Date.now();
  localStorage.setItem(key, JSON.stringify([...data, receta]));
};
export const updateReceta = (id, updated) => {
  const data = getRecetas().map(r => r.id === id ? updated : r);
  localStorage.setItem(key, JSON.stringify(data));
};
export const deleteReceta = (id) => {
  const data = getRecetas().filter(r => r.id !== id);
  localStorage.setItem(key, JSON.stringify(data));
};
