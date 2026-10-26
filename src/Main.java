/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhaycaminhas
 */
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame janela = new JFrame("Controle Financeiro Pessoal");

            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setContentPane(new ControleFinanceiro());

            janela.pack();
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}