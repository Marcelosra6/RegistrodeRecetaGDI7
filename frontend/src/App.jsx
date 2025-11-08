import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'


import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Pacientes from "./pages/Pacientes";
import Recetas from "./pages/Recetas";
import NuevaReceta from "./pages/NuevaReceta";
import Reportes from "./pages/Reportes";

export default function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen">
          <Navbar />
          <Routes>
            <Route path="/" element={<Login />} />
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/pacientes"
              element={
                <ProtectedRoute>
                  <Pacientes />
                </ProtectedRoute>
              }
            />
            <Route
              path="/recetas"
              element={
                <ProtectedRoute>
                  <Recetas />
                </ProtectedRoute>
              }
            />
            <Route
              path="/recetas/new"
              element={
                <ProtectedRoute>
                  <NuevaReceta />
                </ProtectedRoute>
              }
            />
            <Route
              path="/recetas/edit/:id"
              element={
                <ProtectedRoute>
                  <NuevaReceta />
                </ProtectedRoute>
              }
            />
            <Route
              path="/reportes"
              element={
                <ProtectedRoute>
                  <Reportes />
                </ProtectedRoute>
              }
            />
            {/* puedes agregar rutas adicionales para usuarios/admin */}
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}











/*
function App() {
  return (
    <div className="h-screen flex items-center justify-center bg-blue-600 text-white text-4xl font-bold">
      Tailwind funciona correctamente 🚀
    </div>
  )
}

export default App;
*/

