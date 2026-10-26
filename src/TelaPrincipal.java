/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author dhaycaminhas
 */

import javax.swing.JFrame;

public class TelaPrincipal extends javax.swing.JPanel {

    /**
     * Creates new form TelaPrincipal
     */
    
    public TelaPrincipal() {
    initComponents();

    carregarDashboard();
    carregarDespesasMensais();
    carregarEntradasDashboard();
    carregarPlanejamentoDashboard();
    carregarMetasDashboard();
}
    
    private void carregarDashboard() {

    String sql =
            "SELECT " +
            "COALESCE(SUM(CASE WHEN tipo = 'Entrada' THEN valor ELSE 0 END), 0) AS entradas, " +
            "COALESCE(SUM(CASE WHEN tipo = 'Despesa' THEN valor ELSE 0 END), 0) AS despesas " +
            "FROM Transacao " +
            "WHERE usuario_id = 1";

    try (
        java.sql.Connection conexao = Conexao.conectar();
        java.sql.PreparedStatement stmt =
                conexao.prepareStatement(sql);
        java.sql.ResultSet resultado =
                stmt.executeQuery()
    ) {

        if (resultado.next()) {

            double entradas =
                    resultado.getDouble("entradas");

            double despesas =
                    resultado.getDouble("despesas");

            double saldo = entradas - despesas;

            lblValorEntradaMes.setText(
                    String.format("R$ %.2f", entradas)
            );

            lblValorDespesasMes.setText(
                    String.format("R$ %.2f", despesas)
            );

            lblValorSaldoAtual.setText(
                    String.format("R$ %.2f", saldo)
            );

            lblValorSaldoPrevisto.setText(
                    String.format("R$ %.2f", saldo)
            );
        }

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar os dados do dashboard:\n"
                + e.getMessage(),
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    private void carregarDespesasMensais() {

    String sql =
            "SELECT c.nome, COALESCE(SUM(t.valor), 0) AS total " +
            "FROM Transacao t " +
            "INNER JOIN Categoria c ON t.categoria_id = c.id " +
            "WHERE t.tipo = 'Despesa' " +
            "AND t.usuario_id = 1 " +
            "GROUP BY c.nome";

    try (
        java.sql.Connection conexao = Conexao.conectar();
        java.sql.PreparedStatement stmt =
                conexao.prepareStatement(sql);
        java.sql.ResultSet resultado =
                stmt.executeQuery()
    ) {

        // Começa todos os valores em zero
        lblValorAluguel.setText("R$ 0,00");
        lblValorAguaLuzNet.setText("R$ 0,00");
        lblValorTransporte.setText("R$ 0,00");
        lblValorPlanoSaude.setText("R$ 0,00");
        lblValorAlimentacao.setText("R$ 0,00");
        lblValorLazer.setText("R$ 0,00");

        double totalDespesas = 0;

        while (resultado.next()) {

            String categoria =
                    resultado.getString("nome");

            double valor =
                    resultado.getDouble("total");

            totalDespesas += valor;

            String valorFormatado =
                    String.format("R$ %.2f", valor);

            if (categoria.equals("Moradia")) {

                lblValorAluguel.setText(valorFormatado);

            } else if (categoria.equals("Alimentação")) {

                lblValorAlimentacao.setText(valorFormatado);

            } else if (categoria.equals("Transporte")) {

                lblValorTransporte.setText(valorFormatado);

            } else if (categoria.equals("Saúde")) {

                lblValorPlanoSaude.setText(valorFormatado);

            } else if (categoria.equals("Lazer")) {

                lblValorLazer.setText(valorFormatado);
            }
        }

        lblValorTotalDespesas.setText(
                String.format("Total R$ %.2f", totalDespesas)
        );

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar as despesas do dashboard:\n"
                + e.getMessage(),
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    private void carregarEntradasDashboard() {

    String sql =
            "SELECT c.nome, COALESCE(SUM(t.valor), 0) AS total " +
            "FROM Transacao t " +
            "INNER JOIN Categoria c ON t.categoria_id = c.id " +
            "WHERE t.tipo = 'Entrada' " +
            "AND t.usuario_id = 1 " +
            "GROUP BY c.nome";

    try (
        java.sql.Connection conexao = Conexao.conectar();
        java.sql.PreparedStatement stmt =
                conexao.prepareStatement(sql);
        java.sql.ResultSet resultado =
                stmt.executeQuery()
    ) {

        double totalEntradas = 0;

        lblValorSalario.setText("R$ 0,00");
        lblTotalEntradas.setText("R$ 0,00");
        lblValorTotalEntradas.setText("R$ 0,00");

        while (resultado.next()) {

            String categoria =
                    resultado.getString("nome");

            double valor =
                    resultado.getDouble("total");

            totalEntradas += valor;

            String valorFormatado =
                    String.format("R$ %.2f", valor);

            if (categoria.equals("Salário")) {

                lblValorSalario.setText(valorFormatado);

            } else if (categoria.equals("Trabalho extra")) {

                lblTotalEntradas.setText(valorFormatado);
            }
        }

        lblValorTotalEntradas.setText(
                String.format("R$ %.2f", totalEntradas)
        );

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar as entradas do dashboard:\n"
                + e.getMessage(),
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    private void carregarPlanejamentoDashboard() {

    String sql =
            "SELECT necessidades, desejos, poupanca " +
            "FROM Planejamento " +
            "WHERE usuario_id = 1 " +
            "ORDER BY id DESC " +
            "LIMIT 1";

    try (
        java.sql.Connection conexao = Conexao.conectar();
        java.sql.PreparedStatement stmt =
                conexao.prepareStatement(sql);
        java.sql.ResultSet resultado =
                stmt.executeQuery()
    ) {

        if (resultado.next()) {

            double necessidades =
                    resultado.getDouble("necessidades");

            double desejos =
                    resultado.getDouble("desejos");

            double poupanca =
                    resultado.getDouble("poupanca");

            lblReceitasPlanejamento.setText(
                    "Necessidades: R$ "
                    + String.format("%.2f", necessidades)
            );

            lblDespesasPlanejamento.setText(
                    "Desejos: R$ "
                    + String.format("%.2f", desejos)
            );

            lblSaldoPlanejamento.setText(
                    "Poupança: R$ "
                    + String.format("%.2f", poupanca)
            );

        } else {

            lblReceitasPlanejamento.setText(
                    "Necessidades: R$ 0,00"
            );

            lblDespesasPlanejamento.setText(
                    "Desejos: R$ 0,00"
            );

            lblSaldoPlanejamento.setText(
                    "Poupança: R$ 0,00"
            );
        }

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar o planejamento no dashboard:\n"
                + e.getMessage(),
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    private void carregarMetasDashboard() {

    String sql =
            "SELECT nome, valor_objetivo, valor_atual " +
            "FROM Meta " +
            "WHERE usuario_id = 1 " +
            "ORDER BY id " +
            "LIMIT 3";

    try (
        java.sql.Connection conexao = Conexao.conectar();
        java.sql.PreparedStatement stmt =
                conexao.prepareStatement(sql);
        java.sql.ResultSet resultado =
                stmt.executeQuery()
    ) {

        // Valores padrão
        lblMetaViagem.setText("Viagem: R$ 0,00");
        lblMetaReserva.setText("Reserva: R$ 0,00");
        lblMetaCarro.setText("Trocar de carro: R$ 0,00");

        int contador = 0;

        while (resultado.next()) {

            String nome = resultado.getString("nome");
            double valorObjetivo =
                    resultado.getDouble("valor_objetivo");
            double valorAtual =
                    resultado.getDouble("valor_atual");

            String texto =
                    nome + ": R$ "
                    + String.format("%.2f", valorAtual)
                    + " / R$ "
                    + String.format("%.2f", valorObjetivo);

            if (contador == 0) {

                lblMetaViagem.setText(texto);

            } else if (contador == 1) {

                lblMetaReserva.setText(texto);

            } else if (contador == 2) {

                lblMetaCarro.setText(texto);
            }

            contador++;
        }

    } catch (java.sql.SQLException e) {

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar as metas do dashboard:\n"
                + e.getMessage(),
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblAnotacoes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAnotacoes = new javax.swing.JTextArea();
        btnSalvarAnotacoes = new javax.swing.JButton();
        pnlPlanejamento = new javax.swing.JPanel();
        lblPlanejamento = new javax.swing.JLabel();
        lblReceitasPlanejamento = new javax.swing.JLabel();
        lblDespesasPlanejamento = new javax.swing.JLabel();
        lblSaldoPlanejamento = new javax.swing.JLabel();
        pnlMetas = new javax.swing.JPanel();
        lblMetas = new javax.swing.JLabel();
        lblMetaViagem = new javax.swing.JLabel();
        lblMetaReserva = new javax.swing.JLabel();
        lblMetaCarro = new javax.swing.JLabel();
        pnlEntradas = new javax.swing.JPanel();
        lblEntradas = new javax.swing.JLabel();
        lblSalario = new javax.swing.JLabel();
        lblDinheiroExtra = new javax.swing.JLabel();
        lblOutrasEntradas = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblValorSalario = new javax.swing.JLabel();
        lblTotalEntradas = new javax.swing.JLabel();
        lblValorTotalEntradas = new javax.swing.JLabel();
        pnlDespesasMensais = new javax.swing.JPanel();
        lblDespesasMensais = new javax.swing.JLabel();
        lblAluguel = new javax.swing.JLabel();
        lblAguaLuzNet = new javax.swing.JLabel();
        lblValorAluguel = new javax.swing.JLabel();
        lblValorAguaLuzNet = new javax.swing.JLabel();
        lblTransporte = new javax.swing.JLabel();
        lblValorTransporte = new javax.swing.JLabel();
        lblPlanoSaude = new javax.swing.JLabel();
        lblValorPlanoSaude = new javax.swing.JLabel();
        lblAlimentacao = new javax.swing.JLabel();
        lblValorAlimentacao = new javax.swing.JLabel();
        lblValorTotalDespesas = new javax.swing.JLabel();
        lblLazer = new javax.swing.JLabel();
        lblValorLazer = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblSaldoAtual = new javax.swing.JLabel();
        lblValorSaldoAtual = new javax.swing.JLabel();
        lblDespesasMes = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblValorDespesasMes = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblEntradaMes = new javax.swing.JLabel();
        lblValorEntradaMes = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblSaldoPrevisto = new javax.swing.JLabel();
        lblValorSaldoPrevisto = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JButton();
        btnEntradas = new javax.swing.JButton();
        btnDespesas = new javax.swing.JButton();
        btnPlanejamento = new javax.swing.JButton();
        btnMetas = new javax.swing.JButton();
        btnRelatorios = new javax.swing.JButton();
        btnConfigurações = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Finanças pessoais");

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 0, 10)); // NOI18N
        jLabel9.setText("Olá Dhayanne, bem vinda ao seu projeto financeiro pessoal");

        lblAnotacoes.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblAnotacoes.setText("Anotações");

        txtAnotacoes.setColumns(20);
        txtAnotacoes.setRows(5);
        jScrollPane1.setViewportView(txtAnotacoes);

        btnSalvarAnotacoes.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        btnSalvarAnotacoes.setText("Salvar");
        btnSalvarAnotacoes.addActionListener(this::btnSalvarAnotacoesActionPerformed);

        pnlPlanejamento.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblPlanejamento.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        lblPlanejamento.setText("Planejamento Financeiro");

        lblReceitasPlanejamento.setText("Necessidades: ");

        lblDespesasPlanejamento.setText("Desejos: ");

        lblSaldoPlanejamento.setText("Poupança:");

        javax.swing.GroupLayout pnlPlanejamentoLayout = new javax.swing.GroupLayout(pnlPlanejamento);
        pnlPlanejamento.setLayout(pnlPlanejamentoLayout);
        pnlPlanejamentoLayout.setHorizontalGroup(
            pnlPlanejamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlanejamentoLayout.createSequentialGroup()
                .addGroup(pnlPlanejamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPlanejamentoLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblPlanejamento))
                    .addGroup(pnlPlanejamentoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblReceitasPlanejamento))
                    .addGroup(pnlPlanejamentoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblSaldoPlanejamento))
                    .addGroup(pnlPlanejamentoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblDespesasPlanejamento)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPlanejamentoLayout.setVerticalGroup(
            pnlPlanejamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlanejamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPlanejamento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblReceitasPlanejamento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDespesasPlanejamento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSaldoPlanejamento)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pnlMetas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblMetas.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        lblMetas.setText("Metas");

        lblMetaViagem.setText("Viagem:");

        lblMetaReserva.setText("Reserva:");

        lblMetaCarro.setText("Trocar de carro:");

        javax.swing.GroupLayout pnlMetasLayout = new javax.swing.GroupLayout(pnlMetas);
        pnlMetas.setLayout(pnlMetasLayout);
        pnlMetasLayout.setHorizontalGroup(
            pnlMetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMetasLayout.createSequentialGroup()
                .addGroup(pnlMetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMetasLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lblMetas))
                    .addGroup(pnlMetasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblMetaViagem))
                    .addGroup(pnlMetasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblMetaReserva))
                    .addGroup(pnlMetasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblMetaCarro)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        pnlMetasLayout.setVerticalGroup(
            pnlMetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMetasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMetas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMetaViagem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMetaReserva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMetaCarro)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pnlEntradas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblEntradas.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblEntradas.setText("Entradas");

        lblSalario.setText("Salário");

        lblDinheiroExtra.setText("Dinheiro extra");

        lblOutrasEntradas.setText("Outras entradas");

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel8.setText("Total R$:");

        lblValorSalario.setText("R$:");

        lblTotalEntradas.setText("R$:");

        lblValorTotalEntradas.setText("R$:");

        javax.swing.GroupLayout pnlEntradasLayout = new javax.swing.GroupLayout(pnlEntradas);
        pnlEntradas.setLayout(pnlEntradasLayout);
        pnlEntradasLayout.setHorizontalGroup(
            pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntradasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEntradasLayout.createSequentialGroup()
                        .addComponent(lblSalario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorSalario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlEntradasLayout.createSequentialGroup()
                        .addComponent(lblDinheiroExtra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalEntradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlEntradasLayout.createSequentialGroup()
                        .addComponent(lblOutrasEntradas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorTotalEntradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlEntradasLayout.createSequentialGroup()
                        .addGroup(pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEntradas)
                            .addGroup(pnlEntradasLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlEntradasLayout.setVerticalGroup(
            pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEntradas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalario)
                    .addComponent(lblValorSalario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDinheiroExtra)
                    .addComponent(lblTotalEntradas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOutrasEntradas)
                    .addComponent(lblValorTotalEntradas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8))
        );

        pnlDespesasMensais.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDespesasMensais.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblDespesasMensais.setText("Despesas mensais");

        lblAluguel.setText("Aluguel ");

        lblAguaLuzNet.setText("Água, Luz e net");

        lblValorAluguel.setText("R$:");

        lblValorAguaLuzNet.setText("R$:");

        lblTransporte.setText("Transporte");

        lblValorTransporte.setText("R$:");

        lblPlanoSaude.setText("Plano de saúde");

        lblValorPlanoSaude.setText("R$:");

        lblAlimentacao.setText("Alimentação");

        lblValorAlimentacao.setText("R$:");

        lblValorTotalDespesas.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblValorTotalDespesas.setText("Total R$:");

        lblLazer.setText("Lazer");

        lblValorLazer.setText("R$:");

        javax.swing.GroupLayout pnlDespesasMensaisLayout = new javax.swing.GroupLayout(pnlDespesasMensais);
        pnlDespesasMensais.setLayout(pnlDespesasMensaisLayout);
        pnlDespesasMensaisLayout.setHorizontalGroup(
            pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                        .addComponent(lblAguaLuzNet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorAguaLuzNet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDespesasMensaisLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                                .addComponent(lblDespesasMensais)
                                .addGap(53, 53, 53))
                            .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                                .addComponent(lblTransporte)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblValorTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9))))
                    .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                        .addComponent(lblPlanoSaude)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorPlanoSaude, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                        .addComponent(lblAlimentacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorAlimentacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                        .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                                .addComponent(lblAluguel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblValorAluguel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                                .addComponent(lblLazer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblValorLazer, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblValorTotalDespesas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDespesasMensaisLayout.setVerticalGroup(
            pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDespesasMensaisLayout.createSequentialGroup()
                .addComponent(lblDespesasMensais)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAluguel)
                    .addComponent(lblValorAluguel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAguaLuzNet)
                    .addComponent(lblValorAguaLuzNet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransporte)
                    .addComponent(lblValorTransporte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlanoSaude)
                    .addComponent(lblValorPlanoSaude))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlimentacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblValorAlimentacao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDespesasMensaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLazer)
                    .addComponent(lblValorLazer))
                .addGap(2, 2, 2)
                .addComponent(lblValorTotalDespesas)
                .addGap(7, 7, 7))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSaldoAtual.setText("Saldo atual");

        lblValorSaldoAtual.setText("R$ 0,00");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblValorSaldoAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblSaldoAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSaldoAtual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorSaldoAtual)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblDespesasMes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Despesas no mês");

        lblValorDespesasMes.setText("R$ 0,00");

        javax.swing.GroupLayout lblDespesasMesLayout = new javax.swing.GroupLayout(lblDespesasMes);
        lblDespesasMes.setLayout(lblDespesasMesLayout);
        lblDespesasMesLayout.setHorizontalGroup(
            lblDespesasMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblDespesasMesLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(lblDespesasMesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValorDespesasMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        lblDespesasMesLayout.setVerticalGroup(
            lblDespesasMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblDespesasMesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblValorDespesasMes)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblEntradaMes.setText("Entrada no mês");

        lblValorEntradaMes.setText("R$ 0,00");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblEntradaMes))
                    .addComponent(lblValorEntradaMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEntradaMes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorEntradaMes, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSaldoPrevisto.setText("Saldo previsto");

        lblValorSaldoPrevisto.setText("R$ 0,00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lblSaldoPrevisto)
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addComponent(lblValorSaldoPrevisto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSaldoPrevisto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorSaldoPrevisto)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnDashboard.setText("Dashboard");
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);

        btnEntradas.setText("Entradas");
        btnEntradas.addActionListener(this::btnEntradasActionPerformed);

        btnDespesas.setText("Despesas");
        btnDespesas.addActionListener(this::btnDespesasActionPerformed);

        btnPlanejamento.setText("Planejamento");
        btnPlanejamento.addActionListener(this::btnPlanejamentoActionPerformed);

        btnMetas.setText("Metas");
        btnMetas.addActionListener(this::btnMetasActionPerformed);

        btnRelatorios.setText("Relatorios");
        btnRelatorios.addActionListener(this::btnRelatoriosActionPerformed);

        btnConfigurações.setText("Configurações");
        btnConfigurações.addActionListener(this::btnConfiguraçõesActionPerformed);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDashboard)
                    .addComponent(btnEntradas)
                    .addComponent(btnDespesas)
                    .addComponent(btnPlanejamento)
                    .addComponent(btnMetas)
                    .addComponent(btnRelatorios)
                    .addComponent(btnConfigurações))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnDashboard)
                .addGap(12, 12, 12)
                .addComponent(btnEntradas)
                .addGap(12, 12, 12)
                .addComponent(btnDespesas)
                .addGap(12, 12, 12)
                .addComponent(btnPlanejamento)
                .addGap(12, 12, 12)
                .addComponent(btnMetas)
                .addGap(12, 12, 12)
                .addComponent(btnRelatorios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfigurações)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(pnlEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pnlDespesasMensais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(13, 13, 13))
                                    .addComponent(pnlPlanejamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(pnlMetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(lblDespesasMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(btnSalvarAnotacoes)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 13, Short.MAX_VALUE)
                                .addComponent(lblAnotacoes)
                                .addGap(307, 307, 307))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, Short.MAX_VALUE)
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblDespesasMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnotacoes)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pnlEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSalvarAnotacoes))
                            .addComponent(pnlDespesasMensais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPlanejamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlMetas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarAnotacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarAnotacoesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalvarAnotacoesActionPerformed

    private void btnDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespesasActionPerformed
                                                                                      
    javax.swing.JFrame janela =
            (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

    janela.setContentPane(new TelaDespesas());
    janela.pack();
    janela.setLocationRelativeTo(null);
    janela.revalidate();
    janela.repaint();
    
    }//GEN-LAST:event_btnDespesasActionPerformed

    private void btnMetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMetasActionPerformed
                                     
    JFrame janela = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

    janela.setContentPane(new TelaMetas());
    janela.pack();
    janela.setLocationRelativeTo(null);
    janela.setVisible(true);

    }//GEN-LAST:event_btnMetasActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
                                                                                        
    javax.swing.JFrame janela =
            (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

    janela.setContentPane(new TelaPrincipal());
    janela.pack();
    janela.setLocationRelativeTo(null);

    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        javax.swing.JFrame janela =
        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

janela.setContentPane(new TelaRelatorios());
janela.pack();
janela.setLocationRelativeTo(null);
janela.revalidate();
janela.repaint();
    }//GEN-LAST:event_btnRelatoriosActionPerformed

    private void btnConfiguraçõesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfiguraçõesActionPerformed
                                                    
    javax.swing.JFrame janela =
            (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

    janela.setContentPane(new TelaConfiguracoes());
    janela.pack();
    janela.setLocationRelativeTo(null);
    janela.revalidate();
    janela.repaint();
    
    }//GEN-LAST:event_btnConfiguraçõesActionPerformed

    private void btnEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradasActionPerformed
                                               
    javax.swing.JFrame janela =
            (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

    janela.setContentPane(new TelaEntradas());
    janela.pack();
    janela.setLocationRelativeTo(null);
    janela.revalidate();
    janela.repaint();

    }//GEN-LAST:event_btnEntradasActionPerformed

    private void btnPlanejamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlanejamentoActionPerformed
                                                     
    JFrame janela = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

    janela.setContentPane(new TelaPlanejamento());
    janela.pack();
    janela.setLocationRelativeTo(null);
    janela.revalidate();
    janela.repaint();
    }//GEN-LAST:event_btnPlanejamentoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfigurações;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDespesas;
    private javax.swing.JButton btnEntradas;
    private javax.swing.JButton btnMetas;
    private javax.swing.JButton btnPlanejamento;
    private javax.swing.JButton btnRelatorios;
    private javax.swing.JButton btnSalvarAnotacoes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAguaLuzNet;
    private javax.swing.JLabel lblAlimentacao;
    private javax.swing.JLabel lblAluguel;
    private javax.swing.JLabel lblAnotacoes;
    private javax.swing.JLabel lblDespesasMensais;
    private javax.swing.JPanel lblDespesasMes;
    private javax.swing.JLabel lblDespesasPlanejamento;
    private javax.swing.JLabel lblDinheiroExtra;
    private javax.swing.JLabel lblEntradaMes;
    private javax.swing.JLabel lblEntradas;
    private javax.swing.JLabel lblLazer;
    private javax.swing.JLabel lblMetaCarro;
    private javax.swing.JLabel lblMetaReserva;
    private javax.swing.JLabel lblMetaViagem;
    private javax.swing.JLabel lblMetas;
    private javax.swing.JLabel lblOutrasEntradas;
    private javax.swing.JLabel lblPlanejamento;
    private javax.swing.JLabel lblPlanoSaude;
    private javax.swing.JLabel lblReceitasPlanejamento;
    private javax.swing.JLabel lblSalario;
    private javax.swing.JLabel lblSaldoAtual;
    private javax.swing.JLabel lblSaldoPlanejamento;
    private javax.swing.JLabel lblSaldoPrevisto;
    private javax.swing.JLabel lblTotalEntradas;
    private javax.swing.JLabel lblTransporte;
    private javax.swing.JLabel lblValorAguaLuzNet;
    private javax.swing.JLabel lblValorAlimentacao;
    private javax.swing.JLabel lblValorAluguel;
    private javax.swing.JLabel lblValorDespesasMes;
    private javax.swing.JLabel lblValorEntradaMes;
    private javax.swing.JLabel lblValorLazer;
    private javax.swing.JLabel lblValorPlanoSaude;
    private javax.swing.JLabel lblValorSalario;
    private javax.swing.JLabel lblValorSaldoAtual;
    private javax.swing.JLabel lblValorSaldoPrevisto;
    private javax.swing.JLabel lblValorTotalDespesas;
    private javax.swing.JLabel lblValorTotalEntradas;
    private javax.swing.JLabel lblValorTransporte;
    private javax.swing.JPanel pnlDespesasMensais;
    private javax.swing.JPanel pnlEntradas;
    private javax.swing.JPanel pnlMetas;
    private javax.swing.JPanel pnlPlanejamento;
    private javax.swing.JTextArea txtAnotacoes;
    // End of variables declaration//GEN-END:variables
}
