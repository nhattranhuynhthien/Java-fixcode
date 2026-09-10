package org.example;

import org.example.login.DangNhap;

import javax.swing.*;

public class _Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
    }
}
