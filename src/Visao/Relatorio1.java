/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Controle.ConectaBanco;
import Controle.ModeloTabela;
import Utilitarios.Utilitarios;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author euris
 */
public class Relatorio1 extends javax.swing.JFrame {
    ConectaBanco conexao = new ConectaBanco();
    String SQLReceita;
    String SQLDespesa;
    
    float totalReceita;
    float totalDespesa;
    /**
     * Creates new form Relatorio1
     */
    public Relatorio1(String sqlReceita, String sqlDespesa) {
        this.SQLReceita = sqlReceita;
        this.SQLDespesa = sqlDespesa;
        conexao.conecta();
        Utilitarios u = new Utilitarios();
        u.inserirIcone(this);
        initComponents();
        preencherTabelaReceita();
        preencherTabelaDespesa();
        totalizadores();
    }

    private Relatorio1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void preencherTabelaReceita(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Descrição","Valor"};
    
        conexao.executaSQL(SQLReceita);
        try {
            conexao.rs.first();
            do{  totalReceita = totalReceita + conexao.rs.getFloat("VL_RECEITA");
                String dsDespesa = null;
                conexao.executaSqlSubConsulta("SELECT DS_CLUCRO FROM CENTROLUCRO WHERE CD_CLUCRO = "+conexao.rs.getInt("FK_CLUCRO")+";");
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsDespesa = conexao.rsSubConsulta.getString("DS_CLUCRO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar a sub consulta da descrição da receita. \nErro: "+ ex);
                }
            
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       dsDespesa, 
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Receita. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableRescRel.setModel(modelo);
        jTableRescRel.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableRescRel.getColumnModel().getColumn(0).setResizable(false);
        jTableRescRel.getColumnModel().getColumn(1).setPreferredWidth(250);//Largura da coluna
        jTableRescRel.getColumnModel().getColumn(1).setResizable(false);
        jTableRescRel.getColumnModel().getColumn(2).setPreferredWidth(100);//Largura da coluna
        jTableRescRel.getColumnModel().getColumn(2).setResizable(false);
        jTableRescRel.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableRescRel.setAutoResizeMode(jTableRescRel.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableRescRel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaDespesa(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Descrição","Valor"};
    
        conexao.executaSQL(SQLDespesa);
        try {
            conexao.rs.first();
            do{  totalDespesa = totalDespesa + conexao.rs.getFloat("VL_DESPESA");
                String dsDespesa = null;
                conexao.executaSqlSubConsulta("SELECT DS_CCUSTO FROM CENTROCUSTO WHERE CD_CCUSTO = "+conexao.rs.getInt("FK_CCUSTO")+";");
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsDespesa = conexao.rsSubConsulta.getString("DS_CCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar a sub consulta da descrição da Despesa. \nErro: "+ ex);
                }
            
                dados.add(new Object[]{conexao.rs.getDate("DT_DESPESA"),
                                       dsDespesa, 
                                       conexao.rs.getFloat("VL_DESPESA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Despesa. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableDespRel.setModel(modelo);
        jTableDespRel.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableDespRel.getColumnModel().getColumn(0).setResizable(false);
        jTableDespRel.getColumnModel().getColumn(1).setPreferredWidth(250);//Largura da coluna
        jTableDespRel.getColumnModel().getColumn(1).setResizable(false);
        jTableDespRel.getColumnModel().getColumn(2).setPreferredWidth(100);//Largura da coluna
        jTableDespRel.getColumnModel().getColumn(2).setResizable(false);
        jTableDespRel.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableDespRel.setAutoResizeMode(jTableDespRel.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableDespRel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void totalizadores(){
        jTextFieldTotalReceita.setText(totalReceita+"");
        jTextFieldTotalDespesa.setText(totalDespesa+"");
        jTextFieldLucro.setText((totalReceita - totalDespesa)+"");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuLogo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableRescRel = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDespRel = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldTotalReceita = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldTotalDespesa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldLucro = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        MenuLogo.setBackground(new java.awt.Color(47, 79, 79));
        MenuLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        MenuLogo.setToolTipText("");
        MenuLogo.setPreferredSize(new java.awt.Dimension(2, 82));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Relatório das Receitas, Despesas e Totais");

        javax.swing.GroupLayout MenuLogoLayout = new javax.swing.GroupLayout(MenuLogo);
        MenuLogo.setLayout(MenuLogoLayout);
        MenuLogoLayout.setHorizontalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLogoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(196, 196, 196))
        );
        MenuLogoLayout.setVerticalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas                                                                                                                                          Despesas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jTableRescRel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTableRescRel);

        jPanel2.add(jScrollPane2);

        jTableDespRel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableDespRel);

        jPanel2.add(jScrollPane1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Resumo das Reitas/Despesas/Lucros"));

        jLabel4.setText("Valor total da Receita : ");

        jLabel5.setText("Valor total da Despesa : ");

        jLabel6.setText("Lucros :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldTotalReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldTotalDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Relatorio1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Relatorio1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Relatorio1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Relatorio1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Relatorio1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuLogo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDespRel;
    private javax.swing.JTable jTableRescRel;
    private javax.swing.JTextField jTextFieldLucro;
    private javax.swing.JTextField jTextFieldTotalDespesa;
    private javax.swing.JTextField jTextFieldTotalReceita;
    // End of variables declaration//GEN-END:variables
}
