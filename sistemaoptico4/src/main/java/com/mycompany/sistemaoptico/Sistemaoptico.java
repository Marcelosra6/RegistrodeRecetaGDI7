/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemaoptico;

import vista.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal del Sistema de Recetas Médicas
 * Punto de entrada de la aplicación
 */
public class Sistemaoptico {
    
    public static void main(String[] args) {
        
        System.out.println("==============================================");
        System.out.println("  SISTEMA DE RECETAS MÉDICAS - ÓPTICA");
        System.out.println("  Iniciando aplicación...");
        System.out.println("==============================================\n");
        
        // Establecer Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("✓ Interfaz gráfica configurada");
        } catch (Exception e) {
            System.err.println("✗ No se pudo configurar la interfaz gráfica");
            e.printStackTrace();
        }
        
        // Ejecutar la ventana de login en el Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    System.out.println("✓ Ventana de login mostrada\n");
                    System.out.println("Credenciales por defecto:");
                    System.out.println("  Usuario: admin");
                    System.out.println("  Contraseña: admin123");
                    System.out.println("==============================================\n");
                } catch (Exception e) {
                    System.err.println("✗ Error al iniciar la ventana de login");
                    e.printStackTrace();
                }
            }
        });
    }
}