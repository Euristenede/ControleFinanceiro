/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Controle.ConectaBanco;
import Controle.ControleBanco;
import Controle.ControleCCusto;
import Controle.ControleCLucro;
import Controle.ControleDespesa;
import Controle.ControleDespesaCCusto;
import Controle.ControleParcela;
import Controle.ControleReceita;
import Controle.ControleReceitaCLucro;
import Controle.ModeloTabela;
import Modelo.ModeloBanco;
import Modelo.ModeloCCusto;
import Modelo.ModeloCLucro;
import Modelo.ModeloDespesa;
import Modelo.ModeloDespesaCCusto;
import Modelo.ModeloParcela;
import Modelo.ModeloReceita;
import Modelo.ModeloReceitaCLucro;
import java.awt.Color;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;


/**
 *
 * @author EuristenedeSantos
 */
public class SISTEMA extends javax.swing.JInternalFrame {
    ConectaBanco conexao = new ConectaBanco();
    java.util.Date dataSistema = new Date();
    
    //---VARIÁVEIS PARA IMPORTAR AS TELAS DE CONSULTAS---//
    ConsultaCCusto consultaCCusto;
    ConsultaCLucro consultaCLucro;
    ConsultaBanco consultaBanco;
    ConsultaDespCCusto consultaDespCCusto;
    ConsultaRecCLucro consultaRecCLucro;
    //---------------------------------------------------//
    
    //------VARIÁVEIS COM AS CONSULTAS DAS TABELAS-------//
    String SQL_tabelaBanco = "SELECT * FROM BANCO";
    String SQL_tabelaCCusto = "SELECT * FROM CENTROCUSTO";
    String SQL_tabelaCLucro = "SELECT * FROM CENTROLUCRO";
    String SQL_tabelaDespCCusto = "SELECT * FROM DESPESACCUSTO";
    String SQL_tabelaRecCLucro = "SELECT * FROM RECEITACLUCRO";
    String SQL_tabelaReceita = "SELECT * FROM RECEITA";
    String SQL_tabelaDespesa = "SELECT * FROM DESPESA";
    String SQL_tabelaParcela = "SELECT * FROM PARCELA";
    String SubSQL_tabelaDespCCustoDsCusto = null;
    String SubSQL_tabelaCLucroDsLucro = null;
    String SubSQL_tabelaReceitaDsBanco = null;
    String SubSQL_tabelaRecCLucroDsRecLucro = null;
    String SQL_UpdateSoma = null;
    String SQL_UpdateAltera_Receita = null;
    String SQL_UpdateSubtrai = null;
    String SubSQL_tabelaDespesaDsDespCCusto = null;
    String SQL_UpdateAltera_Despesa = null;
    private void montaSqlDsCCusto(int codigo){
        SubSQL_tabelaDespCCustoDsCusto = "SELECT DS_CCUSTO FROM CENTROCUSTO WHERE CD_CCUSTO = "+codigo+";";
    }
    private void montaSqlDsCLucro(int codigo){
        SubSQL_tabelaCLucroDsLucro = "SELECT DS_CLUCRO FROM CENTROLUCRO WHERE CD_CLUCRO = "+codigo+";";
    }
    private void montaSqlDsRecCLucro(int codigo){
        SubSQL_tabelaRecCLucroDsRecLucro = "SELECT DS_RECCLUCRO FROM RECEITACLUCRO WHERE CD_RECCLUCRO = "+codigo+";";
    }
    private void montaSqlDsBanco(int codigo){
        SubSQL_tabelaReceitaDsBanco = "SELECT DS_BANCO FROM BANCO WHERE CD_BANCO = "+codigo+";";
    }
    private void montaSqlUpdateSoma(int codigoBanco, float valor){
        SQL_UpdateSoma = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO + "+valor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlUpdateSubtrai(int codigoBanco, float valor){
        SQL_UpdateSubtrai = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO - "+valor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlUpdateAltera_Receita(int codigoBanco, float novoValor){
        SQL_UpdateAltera_Receita = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO - "+valorOriginal+" + "+novoValor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlUpdateAltera_Despesa(int codigoBanco, float novoValor){
        SQL_UpdateAltera_Despesa = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO + "+valorOriginal+" - "+novoValor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlDsDespesa(int codigo){
        SubSQL_tabelaDespesaDsDespCCusto = "SELECT DS_DESPCCUSTO FROM DESPESACCUSTO WHERE CD_DESPCCUSTO = "+codigo+";";
    }
    //---------------------------------------------------//
    
    //-------------VARIÁVEIS GLOBAIS---------------------//
    //Essas variáveis guarda o código das tabelas que tem nas abas, esse código
    //é usado pra fazer update ou delete;
    int codigoBanco;
    int codigoCCusto;
    int codigoCLucro;
    int codigoDespCCusto;
    int codigoRecCLucro;
    int codigoReceita;
    int codigoDespesa;
    int codigoParcela;
    int fkCCusto;//Usado no parâmetro da tela de consulta de despesas por centro de custos.
    int fkCLucro;//Usado no parâmetro da tela de consulta de receita por centro de lucros.
    boolean aPagar;//Variável guardo o valor list box a pagar da despesa.
    //---------------------------------------------------//
    
    //-------------VARIÁVEIS PARA UPDATE-----------------//
    //Essa variável, guarda o valor da receita ou despesa,
    //quando clicado na tabela, serve para atualizar o saldo
    //do banco, quando a operação for de alteração ou exclusão
    //da Receita ou Despesa.
    String valorOriginal;
    int codBancoOrig;
    //---------------------------------------------------//
    
    //-------------MASCARAS PARA CAMPOS------------------//
    String telefoneMask = "(##)# #### ####";
    String cpfMask = "###.###.###-##";
    String dataMask = "##/##/####";
    //---------------------------------------------------//
    /**
     * Creates new form SISTEMA
     * @throws java.text.ParseException
     */
    //--VARIÁVEIS QUE RECEBE AS DESCRIÇÕES DA CONSULTA---//
    String jtDS_CCusto = null;
    String jtDS_Banco = null;
    String jtDS_DespCCusto = null;
    String jtDS_CLucro = null;
    String jtDS_RecCLucro = null;
    //---------------------------------------------------//
    
    public SISTEMA() throws ParseException {
        conexao.conecta();
        consultaCCusto = new ConsultaCCusto(new javax.swing.JFrame(), true);
        consultaCLucro = new ConsultaCLucro(new javax.swing.JFrame(), true);
        consultaBanco = new ConsultaBanco(new javax.swing.JFrame(), true);
        initComponents();
        preencherTabelaBanco(SQL_tabelaBanco);
        preencherTabelaCCusto(SQL_tabelaCCusto);
        preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
        preencherTabelaCLucro(SQL_tabelaCLucro);
        preencherTabelaReceiraCLucro(SQL_tabelaRecCLucro);
        preencherTabelaReceita(SQL_tabelaReceita);
        preencherTabelaDespesa(SQL_tabelaDespesa);
        preencherTabelaParcela(SQL_tabelaParcela);
        jFormattedTextFieldDataCCusto.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDataDespCCusto.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDataCLucro.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDataDespCLucro.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDtReceita.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDtDespesa.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
    }

    //---------------------Aba Banco--------------------------//
    
    public void preencherTabelaBanco(String SQL){
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Banco","Agência","Conta","Saldo"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
                dados.add(new Object[]{conexao.rs.getString("CD_BANCO"),conexao.rs.getString("DS_BANCO"), conexao.rs.getInt("NR_AGENCIA"),conexao.rs.getString("NR_CONTA"),conexao.rs.getFloat("VL_SALDO")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Banco. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableBANCO.setModel(modelo);
        jTableBANCO.getColumnModel().getColumn(0).setPreferredWidth(50);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(0).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(1).setPreferredWidth(310);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(1).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(2).setPreferredWidth(150);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(2).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(3).setPreferredWidth(200);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(3).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(4).setPreferredWidth(200);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(4).setResizable(false);
        jTableBANCO.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableBANCO.setAutoResizeMode(jTableBANCO.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableBANCO.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposBanco(){
        jFormattedTextFieldDS_BANCO.setText("");
        jTextFieldNR_AGENCIA.setText("");
        jTextFieldNR_CONTA.setText("");
        jTextFieldVL_SALDO.setText("");
    }
    
    public boolean verifica_campos_vazio_banco(){
        if(jFormattedTextFieldDS_BANCO.getText().isEmpty() || jTextFieldNR_AGENCIA.getText().isEmpty() ||
                jTextFieldNR_CONTA.getText().isEmpty() || jTextFieldVL_SALDO.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_banco(boolean valor){
        jButtonAlterarBanco.setEnabled(valor);
        jButtonExcluiBanco.setEnabled(valor);
    }
    
    //---------------------Fim Aba Banco--------------------------//
    
    //---------------------Aba Centro Custo--------------------------//
    
    public void preencherTabelaCCusto(String SQL){
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Descição","Data"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
                dados.add(new Object[]{conexao.rs.getString("CD_CCUSTO"),conexao.rs.getString("DS_CCUSTO"), conexao.rs.getDate("DT_RECORD")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Centro de Custo. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableCentroCusto.setModel(modelo);
        jTableCentroCusto.getColumnModel().getColumn(0).setPreferredWidth(120);//Largura da coluna
        jTableCentroCusto.getColumnModel().getColumn(0).setResizable(false);
        jTableCentroCusto.getColumnModel().getColumn(1).setPreferredWidth(650);//Largura da coluna
        jTableCentroCusto.getColumnModel().getColumn(1).setResizable(false);
        jTableCentroCusto.getColumnModel().getColumn(2).setPreferredWidth(200);//Largura da coluna
        jTableCentroCusto.getColumnModel().getColumn(2).setResizable(false);
        jTableCentroCusto.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableCentroCusto.setAutoResizeMode(jTableCentroCusto.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableCentroCusto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposCCusto(){
        jTextFieldDS_CentroCusto.setText("");
    }
    
    public boolean verifica_campos_vazio_ccusto(){
        if(jFormattedTextFieldDataCCusto.getText().isEmpty() || jTextFieldDS_CentroCusto.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_ccusto(boolean valor){
        jButtonAlterarCCusto.setEnabled(valor);
        jButtonExcluirCCusto.setEnabled(valor);
    }
    
    //---------------------Fim Aba Centro Custo--------------------------//
    
    
    
//---------------Aba Despesa por Centro de Custo------------------//
    
    public void preencherTabelaDespesaccusto(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsCCusto = null;
        
        String [] Colunas = new String[]{"Código","Descrição","Centro de Custo"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                montaSqlDsCCusto(conexao.rs.getInt("FK_CCUSTO"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoDsCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsCCusto = conexao.rsSubConsulta.getString("DS_CCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Custo. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_DESPCCUSTO"),
                                       conexao.rs.getString("DS_DESPCCUSTO"), 
                                       dsCCusto});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Despesa por Centro de Custo. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableDespesaCCusto.setModel(modelo);
        jTableDespesaCCusto.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(0).setResizable(false);
        jTableDespesaCCusto.getColumnModel().getColumn(1).setPreferredWidth(500);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(1).setResizable(false);
        jTableDespesaCCusto.getColumnModel().getColumn(2).setPreferredWidth(400);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(2).setResizable(false);
        jTableDespesaCCusto.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableDespesaCCusto.setAutoResizeMode(jTableDespesaCCusto.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableDespesaCCusto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposdespesaccusto(){
        jTextFieldCdCCusto.setText("");
        jTextFieldDsCCusto.setText("");
        jTextFieldDsDespCCusto.setText("");
    }
    
    public boolean verifica_campos_vazio_despesaccusto(){
        if(jTextFieldCdCCusto.getText().isEmpty() || jTextFieldDsDespCCusto.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_despesaccusto(boolean valor){
        jButtonAlterarDespCCusto.setEnabled(valor);
        jButtonExcluirDespCCusto.setEnabled(valor);
    }
    
    public void retornaFks(int codigo){
        int FK_CCUSTO = 0;
        conexao.executaSQL("SELECT * FROM DESPESACCUSTO WHERE CD_DESPCCUSTO = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CCUSTO = conexao.rs.getInt("FK_CCUSTO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs das Despesas por centro de custo. \nErro: "+ ex);
        }
        jTextFieldCdCCusto.setText(FK_CCUSTO+"");

    }
    //------------Fim Aba Despesa por Centro de Custo------------------//
    
    //---------------------Aba Centro Lucro--------------------------//
    
    public void preencherTabelaCLucro(String SQL){
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Descição","Data"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
                dados.add(new Object[]{conexao.rs.getString("CD_CLUCRO"),conexao.rs.getString("DS_CLUCRO"), conexao.rs.getDate("DT_RECORD")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Centro de Lucro. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableCentroLucro.setModel(modelo);
        jTableCentroLucro.getColumnModel().getColumn(0).setPreferredWidth(120);//Largura da coluna
        jTableCentroLucro.getColumnModel().getColumn(0).setResizable(false);
        jTableCentroLucro.getColumnModel().getColumn(1).setPreferredWidth(650);//Largura da coluna
        jTableCentroLucro.getColumnModel().getColumn(1).setResizable(false);
        jTableCentroLucro.getColumnModel().getColumn(2).setPreferredWidth(200);//Largura da coluna
        jTableCentroLucro.getColumnModel().getColumn(2).setResizable(false);
        jTableCentroLucro.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableCentroLucro.setAutoResizeMode(jTableCentroLucro.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableCentroLucro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposCLucro(){
        jTextFieldDS_CentroLucro.setText("");
    }
    
    public boolean verifica_campos_vazio_clucro(){
        if(jFormattedTextFieldDataCLucro.getText().isEmpty() || jTextFieldDS_CentroLucro.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_clucro(boolean valor){
        jButtonAlterarCLucro.setEnabled(valor);
        jButtonExcluirCLucro.setEnabled(valor);
    }
    
    //---------------------Fim Aba Centro Lucro--------------------------//
    
    
    
//---------------Aba Receita por Centro de Lucro------------------//
    
    public void preencherTabelaReceiraCLucro(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsCLucro = null;
        
        String [] Colunas = new String[]{"Código","Descrição","Centro de Lucro"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                montaSqlDsCLucro(conexao.rs.getInt("FK_CLUCRO"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaCLucroDsLucro);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsCLucro = conexao.rsSubConsulta.getString("DS_CLUCRO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Lucro. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_RECCLUCRO"),
                                       conexao.rs.getString("DS_RECCLUCRO"), 
                                       dsCLucro});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Despesa por Centro de Lucro. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableReceitaCLucro.setModel(modelo);
        jTableReceitaCLucro.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableReceitaCLucro.getColumnModel().getColumn(0).setResizable(false);
        jTableReceitaCLucro.getColumnModel().getColumn(1).setPreferredWidth(500);//Largura da coluna
        jTableReceitaCLucro.getColumnModel().getColumn(1).setResizable(false);
        jTableReceitaCLucro.getColumnModel().getColumn(2).setPreferredWidth(400);//Largura da coluna
        jTableReceitaCLucro.getColumnModel().getColumn(2).setResizable(false);
        jTableReceitaCLucro.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableReceitaCLucro.setAutoResizeMode(jTableReceitaCLucro.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableReceitaCLucro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposreceitaclucro(){
        jTextFieldCdCLucro.setText("");
        jTextFieldDsCLucro.setText("");
        jTextFieldDsRecCLucro.setText("");
    }
    
    public boolean verifica_campos_vazio_receitaclucro(){
        if(jTextFieldCdCLucro.getText().isEmpty() || jTextFieldDsRecCLucro.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_receitaclucro(boolean valor){
        jButtonAlterarRecCLucro.setEnabled(valor);
        jButtonExcluirRecCLucro.setEnabled(valor);
    }
    
    public void retornaFkCLucro(int codigo){
        int FK_CLUCRO = 0;
        conexao.executaSQL("SELECT * FROM RECEITACLUCRO WHERE CD_RECCLUCRO = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CLUCRO = conexao.rs.getInt("FK_CLUCRO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs das Receitas por centro de lucro. \nErro: "+ ex);
        }
        jTextFieldCdCLucro.setText(FK_CLUCRO+"");

    }
    //------------Fim Aba Receita por Centro de Lucro------------------//
    
    //--------------------------Aba Receita--------------------------//
    
    public void preencherTabelaReceita(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsBanco;
        String dsClucro;
        String dsRecClucro;
        String [] Colunas = new String[]{"Código","Valor","Centro de Lucro","Descrição da Receita","Data","Banco"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                dsBanco = null;
                montaSqlDsBanco(conexao.rs.getInt("FK_BANCO"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaReceitaDsBanco);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsBanco = conexao.rsSubConsulta.getString("DS_BANCO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Banco. \nErro: "+ ex);
                }
                
                dsClucro = null;
                montaSqlDsCLucro(conexao.rs.getInt("FK_CLUCRO"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaCLucroDsLucro);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsClucro = conexao.rsSubConsulta.getString("DS_CLUCRO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Lucro. \nErro: "+ ex);
                }
                
                dsRecClucro = null;
                montaSqlDsRecCLucro(conexao.rs.getInt("FK_RECCLUCRO"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaRecCLucroDsRecLucro);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsRecClucro = conexao.rsSubConsulta.getString("DS_RECCLUCRO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Lucro por Centro de Lucro. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_RECEITA"),
                                       conexao.rs.getString("VL_RECEITA"), 
                                       dsClucro,
                                       dsRecClucro,
                                       conexao.rs.getDate("DT_RECEITA"),
                                       dsBanco});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Receita. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableReceita.setModel(modelo);
        jTableReceita.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(0).setResizable(false);
        jTableReceita.getColumnModel().getColumn(1).setPreferredWidth(70);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(1).setResizable(false);
        jTableReceita.getColumnModel().getColumn(2).setPreferredWidth(300);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(2).setResizable(false);
        jTableReceita.getColumnModel().getColumn(3).setPreferredWidth(300);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(3).setResizable(false);
        jTableReceita.getColumnModel().getColumn(4).setPreferredWidth(80);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(4).setResizable(false);
        jTableReceita.getColumnModel().getColumn(5).setPreferredWidth(150);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(5).setResizable(false);
        jTableReceita.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableReceita.setAutoResizeMode(jTableReceita.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableReceita.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposReceita(){
        //jFormattedTextFieldDtReceita.setText("");
        jTextFieldCodCLucroRec.setText("");
        jTextFieldDsCLucroRec.setText("");
        jTextFieldCodRecCLucroRec.setText("");
        jTextFieldDsRecCLucroRec.setText("");
        jTextFieldCdBancoRec.setText("");
        jTextFieldDsBancoRec.setText("");
        jTextFieldVlReceita.setText("");
    }
    
    public boolean verifica_campos_vazio_Receita(){
        if(jFormattedTextFieldDtReceita.getText().isEmpty() || jTextFieldCodCLucroRec.getText().isEmpty()||
           jTextFieldCodRecCLucroRec.getText().isEmpty() || jTextFieldCdBancoRec.getText().isEmpty()||
           jTextFieldVlReceita.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_Receita(boolean valor){
        jButtonAlterarReceita.setEnabled(valor);
        jButtonExcluirReceita.setEnabled(valor);
    }
    
    public void retornaFksReceita(int codigo){
        int FK_CLUCRO = 0;
        int FK_RECCLUCRO = 0;
        int FK_BANCO = 0;
        conexao.executaSQL("SELECT * FROM RECEITA WHERE CD_RECEITA = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CLUCRO = conexao.rs.getInt("FK_CLUCRO");
                FK_RECCLUCRO = conexao.rs.getInt("FK_RECCLUCRO");
                FK_BANCO = conexao.rs.getInt("FK_BANCO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs da Receita. \nErro: "+ ex);
        }
        jTextFieldCodCLucroRec.setText(FK_CLUCRO+"");
        jTextFieldCodRecCLucroRec.setText(FK_RECCLUCRO+"");
        jTextFieldCdBancoRec.setText(FK_BANCO+"");
        codBancoOrig = FK_BANCO;
    }
    
    //---------------------Fim Aba Receita--------------------------//
    
    //------------------------Aba Despesa---------------------------//
    
    public void preencherTabelaDespesa(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsCCusto;
        String dsDespesa;
        String dsBanco;
        String situacao;
        
        String [] Colunas = new String[]{"Código","Valor","Descrição","Centro de Custo","Data","Situação","Banco"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                dsCCusto = null;
                dsDespesa = null;
                dsBanco = null;
                situacao = null;
                montaSqlDsCCusto(conexao.rs.getInt("FK_CCUSTO"));
                montaSqlDsDespesa(conexao.rs.getInt("FK_DESPCCUSTO"));
                montaSqlDsBanco(conexao.rs.getInt("FK_BANCO"));
                
                if(conexao.rs.getInt("ST_DESPESA") == 0){
                    situacao = "Pago";
                }else if(conexao.rs.getInt("ST_DESPESA") == 1){
                    situacao = "A Pagar";
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoDsCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsCCusto = conexao.rsSubConsulta.getString("DS_CCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Custo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespesaDsDespCCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsDespesa = conexao.rsSubConsulta.getString("DS_DESPCCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta da Despesa por Centro de Custo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaReceitaDsBanco);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsBanco = conexao.rsSubConsulta.getString("DS_BANCO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Banco. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_DESPESA"),
                                       conexao.rs.getString("VL_DESPESA"), 
                                       dsDespesa,
                                       dsCCusto,
                                       conexao.rs.getString("DT_DESPESA"),
                                       situacao,
                                       dsBanco});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Despesa. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableDespesa.setModel(modelo);
        jTableDespesa.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(0).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(1).setPreferredWidth(80);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(1).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(2).setPreferredWidth(250);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(2).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(3).setPreferredWidth(200);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(3).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(4).setPreferredWidth(90);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(4).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(5).setPreferredWidth(70);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(5).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(6).setPreferredWidth(200);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(6).setResizable(false);
        jTableDespesa.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableDespesa.setAutoResizeMode(jTableDespesa.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableDespesa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposdespesa(){
        //jFormattedTextFieldDtDespesa.setText("");
        jTextFieldCdCCustoDesp.setText("");
        jTextFieldDsCCuscoDesp.setText("");
        jTextFieldCdDespCCusto.setText("");
        jTextFieldDsDespCCustoDesp.setText("");
        jTextFieldVlDesp.setText("");
        jComboBoxStDesp.setSelectedIndex(0);
        jTextFieldCdBancoDesp.setText("");
        jTextFieldDsBancoDesp.setText("");
    }
    
    public boolean verifica_campos_vazio_despesa(){
        if(jComboBoxStDesp.getSelectedIndex() == 0){
            if(jFormattedTextFieldDtDespesa.getText().isEmpty() || jTextFieldCdCCustoDesp.getText().isEmpty() ||
                jTextFieldCdDespCCusto.getText().isEmpty() || jTextFieldVlDesp.getText().isEmpty() || jTextFieldCdBancoDesp.getText().isEmpty()){
                 return true;
             }else{
                 return false;
             }
        }else{
            if(jFormattedTextFieldDtDespesa.getText().isEmpty() || jTextFieldCdCCustoDesp.getText().isEmpty() ||
                jTextFieldCdDespCCusto.getText().isEmpty() || jTextFieldVlDesp.getText().isEmpty()){
                 return true;
             }else{
                 return false;
             }
        }
        
    }
    
    public void habilitar_campo_despesa(boolean valor){
        jButtonAlterarDesp.setEnabled(valor);
        jButtonExcluirDesp.setEnabled(valor);
    }
    
    public void retornaFksDespesa(int codigo){
        int FK_CCUSTO = 0;
        int FK_DESPCCUSTO = 0;
        int FK_BANCO = 0;
        conexao.executaSQL("SELECT * FROM DESPESA WHERE CD_DESPESA = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CCUSTO = conexao.rs.getInt("FK_CCUSTO");
                FK_DESPCCUSTO = conexao.rs.getInt("FK_DESPCCUSTO");
                FK_BANCO = conexao.rs.getInt("FK_BANCO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs das Despesas. \nErro: "+ ex);
        }
        jTextFieldCdCCustoDesp.setText(FK_CCUSTO+"");
        jTextFieldCdDespCCusto.setText(FK_DESPCCUSTO+"");
        if(FK_BANCO != 0){
            jTextFieldCdBancoDesp.setText(FK_BANCO+"");
        }
    }
    //----------------------Fim Aba Despesa ---------------------------//
    
    //------------------------ Aba Parcela ----------------------------//
    public void preencherTabelaParcela(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsCCusto;
        String dsBanco;
        String situacao;
        
        String [] Colunas = new String[]{"Código","Valor","Descrição","Centro de Custo","Data","Parcela","Situação","Banco"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                dsCCusto = null;
                dsBanco = null;
                situacao = null;
                montaSqlDsCCusto(conexao.rs.getInt("FK_CCUSTO"));
                montaSqlDsBanco(conexao.rs.getInt("FK_BANCO"));
                
                if(conexao.rs.getInt("ST_PARCELA") == 0){
                    situacao = "Pago";
                }else if(conexao.rs.getInt("ST_PARCELA") == 1){
                    situacao = "A Pagar";
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoDsCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsCCusto = conexao.rsSubConsulta.getString("DS_CCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Custo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaReceitaDsBanco);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsBanco = conexao.rsSubConsulta.getString("DS_BANCO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Banco. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_PARCELA"),
                                       conexao.rs.getString("VL_PARCELA"), 
                                       conexao.rs.getString("DS_PARCELA"),
                                       dsCCusto,
                                       conexao.rs.getString("DT_PARCELA"),
                                       conexao.rs.getInt("NR_PARCELA"),
                                       situacao,
                                       dsBanco});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Parcela. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableContasParceladas.setModel(modelo);
        jTableContasParceladas.getColumnModel().getColumn(0).setPreferredWidth(50);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(0).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(1).setPreferredWidth(80);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(1).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(2).setPreferredWidth(235);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(2).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(3).setPreferredWidth(180);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(3).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(4).setPreferredWidth(90);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(4).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(5).setPreferredWidth(60);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(5).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(6).setPreferredWidth(70);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(6).setResizable(false);
        jTableContasParceladas.getColumnModel().getColumn(7).setPreferredWidth(160);//Largura da coluna
        jTableContasParceladas.getColumnModel().getColumn(7).setResizable(false);
        jTableContasParceladas.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableContasParceladas.setAutoResizeMode(jTableContasParceladas.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableContasParceladas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposparcela(){
        jTextFieldCdCCustoParcela.setText("");
        jTextFieldDsCCustoParcela.setText("");
        jTextFieldDsDespCCustoParcela.setText("");
        jFormattedTextFieldNrParcela.setText("");
        jFormattedTextFieldValorParcela.setText("");
        jFormattedTextFieldDataVencParcela.setText("");
        jComboBoxStParcela.setSelectedIndex(0);
        jTextFieldCdBancoParcela.setText("");
        jTextFieldDsBancoParcela.setText("");
    }
    
    public boolean verifica_campos_vazio_parcela(){
        if(jComboBoxStParcela.getSelectedIndex() == 0){
            if(jTextFieldCdCCustoParcela.getText().isEmpty() || jTextFieldDsDespCCustoParcela.getText().isEmpty() ||
                jFormattedTextFieldNrParcela.getText().isEmpty() || jFormattedTextFieldValorParcela.getText().isEmpty() || 
                jFormattedTextFieldDataVencParcela.getText().isEmpty() || jTextFieldCdBancoParcela.getText().isEmpty()){
                 return true;
             }else{
                 return false;
             }
        }else{
            if(jTextFieldCdCCustoParcela.getText().isEmpty() || jTextFieldDsDespCCustoParcela.getText().isEmpty() ||
                jFormattedTextFieldNrParcela.getText().isEmpty() || jFormattedTextFieldValorParcela.getText().isEmpty() || 
                jFormattedTextFieldDataVencParcela.getText().isEmpty()){
                 return true;
             }else{
                 return false;
             }
        }
        
    }
    
    public void retornaFksParcela(int codigo){
        int FK_CCUSTO = 0;
        int FK_BANCO = 0;
        conexao.executaSQL("SELECT * FROM PARCELA WHERE CD_PARCELA = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CCUSTO = conexao.rs.getInt("FK_CCUSTO");
                FK_BANCO = conexao.rs.getInt("FK_BANCO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs das Parcelas. \nErro: "+ ex);
        }
        jTextFieldCdCCustoParcela.setText(FK_CCUSTO+"");
        if(FK_BANCO != 0){
            jTextFieldCdBancoParcela.setText(FK_BANCO+"");
        }
    }
    
    public void habilitar_campo_parcela(boolean valor){
        jButtonAlterarParcela.setEnabled(valor);
        jButtonExcluirParcela.setEnabled(valor);
    }
    
    //----------------------Fim Aba Parcela ---------------------------//
    
    public boolean verificaSaldo(int codigoBanco, float valor){
        String SQL = "SELECT * FROM BANCO WHERE CD_BANCO = "+codigoBanco+";";
        Float saldo = null;
        conexao.executaSqlSubConsulta(SQL);
        try {
            conexao.rsSubConsulta.first();
            do{  
                saldo = conexao.rsSubConsulta.getFloat("VL_SALDO");
            }while(conexao.rsSubConsulta.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar o saldo do Banco. \nErro: "+ ex);
        }
        if((saldo - valor) < 0){
            JOptionPane.showMessageDialog(null, "Não é possível pagar essa despesa, pois o saldo da conta é de: R$ "+saldo+". Por favor, tente pagar usando outra conta bancária.");
            return false;
        }else{
            return true;
        }
    }
    
    public boolean verificaSaldoAlteracao(int codigoBanco, float vlOriginal, float valor){
        String SQL = "SELECT * FROM BANCO WHERE CD_BANCO = "+codigoBanco+";";
        Float saldo = null;
        conexao.executaSqlSubConsulta(SQL);
        try {
            conexao.rsSubConsulta.first();
            do{  
                saldo = conexao.rsSubConsulta.getFloat("VL_SALDO");
            }while(conexao.rsSubConsulta.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar o saldo do Banco. \nErro: "+ ex);
        }
        if(((saldo + vlOriginal)- valor) < 0){
            JOptionPane.showMessageDialog(null, "Não é possível fazer essa alteração na despesa, pois o saldo da conta é de: R$ "+saldo+". Por favor, tente pagar usando outra conta bancária.");
            return false;
        }else{
            return true;
        }
    }
    
    public void estornaSaldo(int codigoBanco, float valor){
        montaSqlUpdateSoma(codigoBanco, valor);
        conexao.executaSqlUpdate(SQL_UpdateSoma);
        preencherTabelaBanco(SQL_tabelaBanco);
    }
    
    public boolean verificaData(Date dataVerificar){
        String dia, dia2;
        String mes, mes2;
        String ano, ano2;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	Date date = new Date(); 
        String dataAtual = dateFormat.format(date);
        String dataAVerificar = dateFormat.format(dataVerificar);
        dia = dataAtual.substring(0, 2);
        mes = dataAtual.substring(3, 5);
        ano = dataAtual.substring(6);
        dia2 = dataAVerificar.substring(0, 2);
        mes2 = dataAVerificar.substring(3, 5);
        ano2 = dataAVerificar.substring(6);

        if(Integer.parseInt(ano2) >= Integer.parseInt(ano)){
            if(Integer.parseInt(ano2) > Integer.parseInt(ano)){
                return true;
            }
            if(Integer.parseInt(mes2) >= Integer.parseInt(mes)){
                if((Integer.parseInt(ano2) <= Integer.parseInt(ano)) && (Integer.parseInt(mes2) > Integer.parseInt(mes))){
                    return true;
                }
                if(Integer.parseInt(dia2) > Integer.parseInt(dia)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
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

        MenuLogo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Receita = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jFormattedTextFieldDtReceita = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonExcluirReceita = new javax.swing.JButton();
        jButtonAlterarReceita = new javax.swing.JButton();
        jTextFieldVlReceita = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldCodCLucroRec = new javax.swing.JTextField();
        jTextFieldDsCLucroRec = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextFieldCodRecCLucroRec = new javax.swing.JTextField();
        jTextFieldDsRecCLucroRec = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextFieldCdBancoRec = new javax.swing.JTextField();
        jTextFieldDsBancoRec = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableReceita = new javax.swing.JTable();
        jButtonSalvarReceita = new javax.swing.JButton();
        Despesa = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jFormattedTextFieldDtDespesa = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jButtonExcluirDesp = new javax.swing.JButton();
        jButtonAlterarDesp = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxStDesp = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCdCCustoDesp = new javax.swing.JTextField();
        jTextFieldDsCCuscoDesp = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTextFieldDsDespCCustoDesp = new javax.swing.JTextField();
        jTextFieldCdDespCCusto = new javax.swing.JTextField();
        jTextFieldCdBancoDesp = new javax.swing.JTextField();
        jTextFieldDsBancoDesp = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextFieldVlDesp = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDespesa = new javax.swing.JTable();
        jButtonSalvarDespesa = new javax.swing.JButton();
        CentroCusto = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jFormattedTextFieldDataCCusto = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldDS_CentroCusto = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonAlterarCCusto = new javax.swing.JButton();
        jButtonExcluirCCusto = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableCentroCusto = new javax.swing.JTable();
        jButtonSalvarCCusto = new javax.swing.JButton();
        DespesaCCusto = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jFormattedTextFieldDataDespCCusto = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldDsDespCCusto = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButtonAlterarDespCCusto = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jButtonExcluirDespCCusto = new javax.swing.JButton();
        jTextFieldCdCCusto = new javax.swing.JTextField();
        jTextFieldDsCCusto = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableDespesaCCusto = new javax.swing.JTable();
        jButtonSalvarDespCCusto = new javax.swing.JButton();
        CentroLucro = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jFormattedTextFieldDataCLucro = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldDS_CentroLucro = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButtonAlterarCLucro = new javax.swing.JButton();
        jButtonExcluirCLucro = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableCentroLucro = new javax.swing.JTable();
        jButtonSalvarCLucro = new javax.swing.JButton();
        LucroCLucro = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jFormattedTextFieldDataDespCLucro = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldDsRecCLucro = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jButtonAlterarRecCLucro = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jButtonExcluirRecCLucro = new javax.swing.JButton();
        jTextFieldCdCLucro = new javax.swing.JTextField();
        jTextFieldDsCLucro = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableReceitaCLucro = new javax.swing.JTable();
        jButtonSalvarRecCLucro = new javax.swing.JButton();
        DespesasParceladas = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldDsDespCCustoParcela = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jButtonAlterarParcela = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jButtonExcluirParcela = new javax.swing.JButton();
        jTextFieldCdCCustoParcela = new javax.swing.JTextField();
        jTextFieldDsCCustoParcela = new javax.swing.JTextField();
        jFormattedTextFieldNrParcela = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        jFormattedTextFieldValorParcela = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        jFormattedTextFieldDataVencParcela = new javax.swing.JFormattedTextField();
        jComboBoxStParcela = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCdBancoParcela = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTextFieldDsBancoParcela = new javax.swing.JTextField();
        jPanel38 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableContasParceladas = new javax.swing.JTable();
        jButtonSalvarParcela = new javax.swing.JButton();
        Banco = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jFormattedTextFieldDS_BANCO = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextFieldNR_AGENCIA = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jButtonAlterarBanco = new javax.swing.JButton();
        jTextFieldNR_CONTA = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextFieldVL_SALDO = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jButtonExcluiBanco = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableBANCO = new javax.swing.JTable();
        jButtonSalvarBanco = new javax.swing.JButton();
        jButtonTransferenciaBancaria = new javax.swing.JButton();
        Relatorios = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jFormattedDataIni1 = new javax.swing.JFormattedTextField();
        jFormattedFim1 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButtonGerarRel1 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jFormattedDataIni2 = new javax.swing.JFormattedTextField();
        jFormattedDataFim2 = new javax.swing.JFormattedTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jTextFieldCdCLucroRel2 = new javax.swing.JTextField();
        jTextFieldDsCLucroRel2 = new javax.swing.JTextField();
        jButtonGerarRel2 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jFormattedDataIni3 = new javax.swing.JFormattedTextField();
        jFormattedDataFim3 = new javax.swing.JFormattedTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextFieldCdCCustoRel3 = new javax.swing.JTextField();
        jTextFieldDsCCustoRel2 = new javax.swing.JTextField();
        jButtonGerarRel3 = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Controle Financeiro");
        setToolTipText("");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setVisible(true);

        MenuLogo.setBackground(new java.awt.Color(47, 79, 79));
        MenuLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        MenuLogo.setToolTipText("");
        MenuLogo.setPreferredSize(new java.awt.Dimension(2, 82));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Financeiro Pessoal");

        javax.swing.GroupLayout MenuLogoLayout = new javax.swing.GroupLayout(MenuLogo);
        MenuLogo.setLayout(MenuLogoLayout);
        MenuLogoLayout.setHorizontalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLogoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(380, 380, 380))
        );
        MenuLogoLayout.setVerticalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(58, 65, 84));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.setToolTipText("");

        jButton1.setText("Gravar o que foi Gasto");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cadastrar um Centro de Custo");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Gravar o que Foi Vendido");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cadastrar um Centro de Lucro");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Cadastrar uma descrição do Centro de Lucro");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Contas Parceladas");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton6MouseExited(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Verificar Saldos");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton7MouseExited(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Relatórios");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Cadastrar uma descrição do Centro de Custo");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton9MouseExited(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Receita.setPreferredSize(new java.awt.Dimension(944, 490));

        jPanel12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir Receita", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedTextFieldDtReceita.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel1.setText("Data da Receita:");

        jButtonExcluirReceita.setText("Excluir");
        jButtonExcluirReceita.setEnabled(false);
        jButtonExcluirReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirReceitaActionPerformed(evt);
            }
        });

        jButtonAlterarReceita.setText("Alterar");
        jButtonAlterarReceita.setEnabled(false);
        jButtonAlterarReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarReceitaActionPerformed(evt);
            }
        });

        jLabel7.setText("Valor:");

        jTextFieldCodCLucroRec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodCLucroRecKeyPressed(evt);
            }
        });

        jTextFieldDsCLucroRec.setEditable(false);
        jTextFieldDsCLucroRec.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCLucroRecFocusGained(evt);
            }
        });

        jLabel38.setText("Codigo:     Centro de Lucro:");

        jTextFieldCodRecCLucroRec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodRecCLucroRecKeyPressed(evt);
            }
        });

        jTextFieldDsRecCLucroRec.setEditable(false);
        jTextFieldDsRecCLucroRec.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsRecCLucroRecFocusGained(evt);
            }
        });

        jLabel39.setText("Codigo:     Lucro por Centro de Lucro:");

        jTextFieldCdBancoRec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdBancoRecKeyPressed(evt);
            }
        });

        jTextFieldDsBancoRec.setEditable(false);
        jTextFieldDsBancoRec.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsBancoRecFocusGained(evt);
            }
        });

        jLabel46.setText("Codigo:    Banco:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jFormattedTextFieldDtReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCodCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCdBancoRec, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsBancoRec, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldVlReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCodRecCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(260, 260, 260))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jButtonExcluirReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jTextFieldDsRecCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextFieldDtReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsRecCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodRecCLucroRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonAlterarReceita)
                        .addComponent(jButtonExcluirReceita))
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel46)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldDsBancoRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldCdBancoRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldVlReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableReceita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableReceita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableReceitaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableReceita);

        jButtonSalvarReceita.setText("Salvar");
        jButtonSalvarReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarReceitaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarReceita)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ReceitaLayout = new javax.swing.GroupLayout(Receita);
        Receita.setLayout(ReceitaLayout);
        ReceitaLayout.setHorizontalGroup(
            ReceitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ReceitaLayout.setVerticalGroup(
            ReceitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReceitaLayout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Lucro", Receita);

        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir Despesa", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedTextFieldDtDespesa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel8.setText("Data da Despesa:");

        jButtonExcluirDesp.setText("Excluir");
        jButtonExcluirDesp.setEnabled(false);
        jButtonExcluirDesp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirDespActionPerformed(evt);
            }
        });

        jButtonAlterarDesp.setText("Alterar");
        jButtonAlterarDesp.setEnabled(false);
        jButtonAlterarDesp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarDespActionPerformed(evt);
            }
        });

        jLabel11.setText("Valor:");

        jComboBoxStDesp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pago", "A Pagar" }));

        jLabel4.setText("Situação:");

        jTextFieldCdCCustoDesp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoDespKeyPressed(evt);
            }
        });

        jTextFieldDsCCuscoDesp.setEditable(false);
        jTextFieldDsCCuscoDesp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCuscoDespFocusGained(evt);
            }
        });

        jLabel32.setText("Codigo:     Centro de Custo:");

        jLabel37.setText("Codigo:    Despesa:");

        jTextFieldDsDespCCustoDesp.setEditable(false);
        jTextFieldDsDespCCustoDesp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsDespCCustoDespFocusGained(evt);
            }
        });

        jTextFieldCdDespCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdDespCCustoKeyPressed(evt);
            }
        });

        jTextFieldCdBancoDesp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdBancoDespKeyPressed(evt);
            }
        });

        jTextFieldDsBancoDesp.setEditable(false);
        jTextFieldDsBancoDesp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsBancoDespFocusGained(evt);
            }
        });

        jLabel40.setText("Codigo:     Banco:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8)
                        .addComponent(jFormattedTextFieldDtDespesa, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addComponent(jComboBoxStDesp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCdCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCdBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsCCuscoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDsBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExcluirDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCdDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsDespCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(105, 105, 105))
                            .addComponent(jTextFieldVlDesp)))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsCCuscoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCdCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldDsDespCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldVlDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldCdDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextFieldDtDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldDsBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonExcluirDesp)
                        .addComponent(jButtonAlterarDesp))
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCdBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxStDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel37))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableDespesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableDespesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDespesaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDespesa);

        jButtonSalvarDespesa.setText("Salvar");
        jButtonSalvarDespesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarDespesaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalvarDespesa)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DespesaLayout = new javax.swing.GroupLayout(Despesa);
        Despesa.setLayout(DespesaLayout);
        DespesaLayout.setHorizontalGroup(
            DespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DespesaLayout.setVerticalGroup(
            DespesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Despesa", Despesa);

        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir um Centro de Custo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jFormattedTextFieldDataCCusto.setEditable(false);

        jLabel12.setText("Data:");

        jLabel13.setText("Descrição:");

        jButtonAlterarCCusto.setText("Alterar");
        jButtonAlterarCCusto.setEnabled(false);
        jButtonAlterarCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarCCustoActionPerformed(evt);
            }
        });

        jButtonExcluirCCusto.setText("Excluir");
        jButtonExcluirCCusto.setEnabled(false);
        jButtonExcluirCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jFormattedTextFieldDataCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jTextFieldDS_CentroCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExcluirCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDataCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDS_CentroCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAlterarCCusto)
                    .addComponent(jButtonExcluirCCusto))
                .addContainerGap())
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Centro de Custo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableCentroCusto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableCentroCusto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCentroCustoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableCentroCusto);

        jButtonSalvarCCusto.setText("Salvar");
        jButtonSalvarCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarCCusto))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CentroCustoLayout = new javax.swing.GroupLayout(CentroCusto);
        CentroCusto.setLayout(CentroCustoLayout);
        CentroCustoLayout.setHorizontalGroup(
            CentroCustoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CentroCustoLayout.setVerticalGroup(
            CentroCustoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Centro de Custo", CentroCusto);

        jPanel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir uma Despesa por Centro de Custo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jFormattedTextFieldDataDespCCusto.setEditable(false);

        jLabel14.setText("Data:");

        jLabel15.setText("Descrição:");

        jButtonAlterarDespCCusto.setText("Alterar");
        jButtonAlterarDespCCusto.setEnabled(false);
        jButtonAlterarDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarDespCCustoActionPerformed(evt);
            }
        });

        jLabel16.setText("Codigo:     Centro de Custo:");

        jButtonExcluirDespCCusto.setText("Excluir");
        jButtonExcluirDespCCusto.setEnabled(false);
        jButtonExcluirDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirDespCCustoActionPerformed(evt);
            }
        });

        jTextFieldCdCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoKeyPressed(evt);
            }
        });

        jTextFieldDsCCusto.setEditable(false);
        jTextFieldDsCCusto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCustoFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jFormattedTextFieldDataDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50)
                        .addComponent(jTextFieldDsDespCCusto))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(95, 95, 95)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(594, 594, 594)
                        .addComponent(jButtonExcluirDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jButtonAlterarDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(6, 6, 6)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextFieldDataDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDsCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDsDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonExcluirDespCCusto)
                    .addComponent(jButtonAlterarDespCCusto)))
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas por Centro de Custos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableDespesaCCusto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableDespesaCCusto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDespesaCCustoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableDespesaCCusto);

        jButtonSalvarDespCCusto.setText("Salvar");
        jButtonSalvarDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarDespCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarDespCCusto)
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout DespesaCCustoLayout = new javax.swing.GroupLayout(DespesaCCusto);
        DespesaCCusto.setLayout(DespesaCCustoLayout);
        DespesaCCustoLayout.setHorizontalGroup(
            DespesaCCustoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DespesaCCustoLayout.setVerticalGroup(
            DespesaCCustoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DespesaCCustoLayout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Descrição do C.Custo", DespesaCCusto);

        jPanel24.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir um Centro de Lucro", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jFormattedTextFieldDataCLucro.setEditable(false);

        jLabel17.setText("Data:");

        jLabel18.setText("Descrição:");

        jButtonAlterarCLucro.setText("Alterar");
        jButtonAlterarCLucro.setEnabled(false);
        jButtonAlterarCLucro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarCLucroActionPerformed(evt);
            }
        });

        jButtonExcluirCLucro.setText("Excluir");
        jButtonExcluirCLucro.setEnabled(false);
        jButtonExcluirCLucro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirCLucroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jFormattedTextFieldDataCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jTextFieldDS_CentroLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExcluirCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDataCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDS_CentroLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAlterarCLucro)
                    .addComponent(jButtonExcluirCLucro))
                .addContainerGap())
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Centro de Lucro", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableCentroLucro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableCentroLucro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCentroLucroMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableCentroLucro);

        jButtonSalvarCLucro.setText("Salvar");
        jButtonSalvarCLucro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarCLucroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarCLucro))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CentroLucroLayout = new javax.swing.GroupLayout(CentroLucro);
        CentroLucro.setLayout(CentroLucroLayout);
        CentroLucroLayout.setHorizontalGroup(
            CentroLucroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CentroLucroLayout.setVerticalGroup(
            CentroLucroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Centro de Lucro", CentroLucro);

        jPanel27.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir uma Receita por Centro de Lucro", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jFormattedTextFieldDataDespCLucro.setEditable(false);

        jLabel19.setText("Data:");

        jLabel20.setText("Descrição:");

        jButtonAlterarRecCLucro.setText("Alterar");
        jButtonAlterarRecCLucro.setEnabled(false);
        jButtonAlterarRecCLucro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarRecCLucroActionPerformed(evt);
            }
        });

        jLabel21.setText("Codigo:     Centro de Lucro:");

        jButtonExcluirRecCLucro.setText("Excluir");
        jButtonExcluirRecCLucro.setEnabled(false);
        jButtonExcluirRecCLucro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirRecCLucroActionPerformed(evt);
            }
        });

        jTextFieldCdCLucro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCLucroKeyPressed(evt);
            }
        });

        jTextFieldDsCLucro.setEditable(false);
        jTextFieldDsCLucro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCLucroFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jFormattedTextFieldDataDespCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50)
                        .addComponent(jTextFieldDsRecCLucro))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(95, 95, 95)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(594, 594, 594)
                        .addComponent(jButtonExcluirRecCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jButtonAlterarRecCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20))
                .addGap(6, 6, 6)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextFieldDataDespCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCdCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDsCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDsRecCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonExcluirRecCLucro)
                    .addComponent(jButtonAlterarRecCLucro)))
        );

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas por Centro de Lucro", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableReceitaCLucro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableReceitaCLucro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableReceitaCLucroMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTableReceitaCLucro);

        jButtonSalvarRecCLucro.setText("Salvar");
        jButtonSalvarRecCLucro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarRecCLucroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarRecCLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarRecCLucro)
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout LucroCLucroLayout = new javax.swing.GroupLayout(LucroCLucro);
        LucroCLucro.setLayout(LucroCLucroLayout);
        LucroCLucroLayout.setHorizontalGroup(
            LucroCLucroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LucroCLucroLayout.setVerticalGroup(
            LucroCLucroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LucroCLucroLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Descrição C.Lucro", LucroCLucro);

        jPanel34.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir uma Conta Parcelada", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel22.setText("Vencimento:");

        jLabel23.setText("Descrição:");

        jButtonAlterarParcela.setText("Alterar");
        jButtonAlterarParcela.setEnabled(false);
        jButtonAlterarParcela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarParcelaActionPerformed(evt);
            }
        });

        jLabel24.setText("Codigo:     Descrição:");

        jButtonExcluirParcela.setText("Excluir");
        jButtonExcluirParcela.setEnabled(false);
        jButtonExcluirParcela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirParcelaActionPerformed(evt);
            }
        });

        jTextFieldCdCCustoParcela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoParcelaKeyPressed(evt);
            }
        });

        jTextFieldDsCCustoParcela.setEditable(false);
        jTextFieldDsCCustoParcela.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCustoParcelaFocusGained(evt);
            }
        });

        jLabel25.setText("Nº. da parcela:");

        jLabel26.setText("Valor:");

        try {
            jFormattedTextFieldDataVencParcela.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jComboBoxStParcela.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pago", "A Pagar" }));

        jLabel5.setText("Situação:");

        jTextFieldCdBancoParcela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdBancoParcelaKeyPressed(evt);
            }
        });

        jLabel41.setText("Codigo:     Banco:");

        jTextFieldDsBancoParcela.setEditable(false);
        jTextFieldDsBancoParcela.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsBancoParcelaFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxStParcela, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdBancoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsBancoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(153, 153, 153)
                        .addComponent(jButtonExcluirParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jButtonAlterarParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCCustoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCCustoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDsDespCCustoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextFieldNrParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldValorParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldDataVencParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel37Layout.createSequentialGroup()
                            .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel24)
                                .addComponent(jLabel23))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldCdCCustoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldDsCCustoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldDsDespCCustoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel37Layout.createSequentialGroup()
                            .addComponent(jLabel22)
                            .addGap(26, 26, 26))
                        .addGroup(jPanel37Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addGap(6, 6, 6)
                            .addComponent(jFormattedTextFieldNrParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldValorParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldDataVencParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonExcluirParcela)
                            .addComponent(jButtonAlterarParcela)))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsBancoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldCdBancoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxStParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );

        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parcelas", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableContasParceladas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableContasParceladas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableContasParceladasMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jTableContasParceladas);

        jButtonSalvarParcela.setText("Salvar");
        jButtonSalvarParcela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarParcelaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalvarParcela)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DespesasParceladasLayout = new javax.swing.GroupLayout(DespesasParceladas);
        DespesasParceladas.setLayout(DespesasParceladasLayout);
        DespesasParceladasLayout.setHorizontalGroup(
            DespesasParceladasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DespesasParceladasLayout.setVerticalGroup(
            DespesasParceladasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Contas Parceladas", DespesasParceladas);

        jPanel31.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir uma conta Bancária", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel33.setText("Banco:");

        jLabel34.setText("Agência:");

        jButtonAlterarBanco.setText("Alterar");
        jButtonAlterarBanco.setEnabled(false);
        jButtonAlterarBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarBancoActionPerformed(evt);
            }
        });

        jLabel35.setText("Número da Conta:");

        jLabel36.setText("Saldo:");

        jButtonExcluiBanco.setText("Excluir");
        jButtonExcluiBanco.setEnabled(false);
        jButtonExcluiBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluiBancoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExcluiBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jLabel34)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addComponent(jFormattedTextFieldDS_BANCO, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jTextFieldNR_AGENCIA, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNR_CONTA, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(jTextFieldVL_SALDO, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDS_BANCO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNR_AGENCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNR_CONTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVL_SALDO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAlterarBanco)
                    .addComponent(jButtonExcluiBanco))
                .addGap(0, 0, 0))
        );

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bancos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableBANCO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Banco", "Agência", "Número da Conta", "Saldo"
            }
        ));
        jTableBANCO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBANCOMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTableBANCO);

        jButtonSalvarBanco.setText("Salvar");
        jButtonSalvarBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarBancoActionPerformed(evt);
            }
        });

        jButtonTransferenciaBancaria.setText("Transferência");
        jButtonTransferenciaBancaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTransferenciaBancariaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonTransferenciaBancaria, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSalvarBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSalvarBanco)
                    .addComponent(jButtonTransferenciaBancaria)))
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout BancoLayout = new javax.swing.GroupLayout(Banco);
        Banco.setLayout(BancoLayout);
        BancoLayout.setHorizontalGroup(
            BancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
            .addGroup(BancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BancoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        BancoLayout.setVerticalGroup(
            BancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(BancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BancoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Banco", Banco);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas/Despesas/Totalizadores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedDataIni1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedFim1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel9.setText("Data de início :");

        jLabel10.setText("Até :");

        jButtonGerarRel1.setText("Gerar Relatório");
        jButtonGerarRel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataIni1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonGerarRel1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(jFormattedFim1))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jFormattedDataIni1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jFormattedFim1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonGerarRel1)
                .addContainerGap())
        );

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas Por Centro de Lucro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedDataIni2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedDataFim2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel43.setText("Data de início :");

        jLabel44.setText("Até :");

        jLabel45.setText("Codigo:     Centro de Lucro:");

        jTextFieldCdCLucroRel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCLucroRel2KeyPressed(evt);
            }
        });

        jTextFieldDsCLucroRel2.setEditable(false);
        jTextFieldDsCLucroRel2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCLucroRel2FocusGained(evt);
            }
        });

        jButtonGerarRel2.setText("Gerar Relatório");
        jButtonGerarRel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataIni2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataFim2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCLucroRel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCLucroRel2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGerarRel2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedDataFim2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedDataIni2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel43)
                        .addComponent(jLabel44))
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldDsCLucroRel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonGerarRel2))
                    .addComponent(jTextFieldCdCLucroRel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas Por Centro de Custo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedDataIni3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedDataFim3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel47.setText("Data de início :");

        jLabel48.setText("Até :");

        jLabel49.setText("Codigo:     Centro de Custo:");

        jTextFieldCdCCustoRel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoRel3KeyPressed(evt);
            }
        });

        jTextFieldDsCCustoRel2.setEditable(false);
        jTextFieldDsCCustoRel2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCustoRel2FocusGained(evt);
            }
        });

        jButtonGerarRel3.setText("Gerar Relatório");
        jButtonGerarRel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataIni3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataFim3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCCustoRel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel36Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCCustoRel2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGerarRel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedDataFim3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedDataIni3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel47)
                        .addComponent(jLabel48))
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldDsCCustoRel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonGerarRel3))
                    .addComponent(jTextFieldCdCCustoRel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout RelatoriosLayout = new javax.swing.GroupLayout(Relatorios);
        Relatorios.setLayout(RelatoriosLayout);
        RelatoriosLayout.setHorizontalGroup(
            RelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RelatoriosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RelatoriosLayout.createSequentialGroup()
                        .addGroup(RelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(RelatoriosLayout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(447, 447, 447)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        RelatoriosLayout.setVerticalGroup(
            RelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RelatoriosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(194, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Relatórios", Relatorios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
            .addComponent(MenuLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 1224, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Lucros");
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.jTabbedPane1.setSelectedIndex(6);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        this.jTabbedPane1.setSelectedIndex(8);
    }//GEN-LAST:event_jButton8ActionPerformed
/********************************************************************************
***************************Aba Bancos********************************************  
*********************************************************************************/  
    private void jButtonSalvarBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarBancoActionPerformed
        if(verifica_campos_vazio_banco() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloBanco mod = new ModeloBanco();
            ControleBanco controleBanco = new ControleBanco();
            mod.setBanco(jFormattedTextFieldDS_BANCO.getText());
            mod.setAgencia(Integer.parseInt(jTextFieldNR_AGENCIA.getText()));
            mod.setConta(Integer.parseInt(jTextFieldNR_CONTA.getText()));
            mod.setSaldo(Float.parseFloat(jTextFieldVL_SALDO.getText().replaceAll(",", ".")));
            controleBanco.grava_banco(mod);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposBanco();
        }
    }//GEN-LAST:event_jButtonSalvarBancoActionPerformed

    private void jTableBANCOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBANCOMouseClicked
        String codigo = ""+jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 0);
        codigoBanco = Integer.parseInt(codigo);
        String banco = ""+jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 2);
        String saldo = ""+ jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 4);
        jFormattedTextFieldDS_BANCO.setText((String) jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 1));
        jTextFieldNR_AGENCIA.setText(banco);
        jTextFieldNR_CONTA.setText((String) jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 3));
        jTextFieldVL_SALDO.setText(saldo);
        habilitar_campo_banco(true);
    }//GEN-LAST:event_jTableBANCOMouseClicked

    private void jButtonAlterarBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarBancoActionPerformed
        if(verifica_campos_vazio_banco()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloBanco mod = new ModeloBanco();
            ControleBanco controleBanco = new ControleBanco();
            mod.setBanco(jFormattedTextFieldDS_BANCO.getText());
            mod.setAgencia(Integer.parseInt(jTextFieldNR_AGENCIA.getText()));
            mod.setConta(Integer.parseInt(jTextFieldNR_CONTA.getText()));
            mod.setSaldo(Float.parseFloat(jTextFieldVL_SALDO.getText().replaceAll(",", ".")));
            mod.setCodigo(codigoBanco);
            controleBanco.altera_banco(mod);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposBanco();
            habilitar_campo_banco(false);
        }
    }//GEN-LAST:event_jButtonAlterarBancoActionPerformed
/********************************************************************************
***************************Fim Aba Bancos****************************************
*********************************************************************************/
    
/********************************************************************************
***************************Aba Centro de Custo***********************************
*********************************************************************************/
    private void jButtonSalvarCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarCCustoActionPerformed
        if(verifica_campos_vazio_ccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCCusto mod = new ModeloCCusto();
            ControleCCusto controleCCusto = new ControleCCusto();
            mod.setDescricao(jTextFieldDS_CentroCusto.getText());
            controleCCusto.grava_ccusto(mod);
            preencherTabelaCCusto(SQL_tabelaCCusto);
            limpar_camposCCusto();
        }
    }//GEN-LAST:event_jButtonSalvarCCustoActionPerformed

    private void jTableCentroCustoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCentroCustoMouseClicked
        String codigo = ""+jTableCentroCusto.getValueAt(jTableCentroCusto.getSelectedRow(), 0);
        codigoCCusto = Integer.parseInt(codigo);
        jTextFieldDS_CentroCusto.setText((String) jTableCentroCusto.getValueAt(jTableCentroCusto.getSelectedRow(), 1));
        habilitar_campo_ccusto(true);
    }//GEN-LAST:event_jTableCentroCustoMouseClicked

    private void jButtonAlterarCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarCCustoActionPerformed
        if(verifica_campos_vazio_ccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCCusto mod = new ModeloCCusto();
            ControleCCusto controleCCusto = new ControleCCusto();
            mod.setDescricao(jTextFieldDS_CentroCusto.getText());
            mod.setCodigo(codigoCCusto);
            controleCCusto.altera_ccusto(mod);
            preencherTabelaCCusto(SQL_tabelaCCusto);
            limpar_camposCCusto();
            habilitar_campo_ccusto(false);
        }
    }//GEN-LAST:event_jButtonAlterarCCustoActionPerformed

    private void jButtonExcluirCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirCCustoActionPerformed
        ModeloCCusto mod = new ModeloCCusto();
        ControleCCusto controleCCusto = new ControleCCusto();
        mod.setCodigo(codigoCCusto);
        controleCCusto.exclui_ccusto(mod);
        preencherTabelaCCusto(SQL_tabelaCCusto);
        limpar_camposCCusto();
        habilitar_campo_ccusto(false);
    }//GEN-LAST:event_jButtonExcluirCCustoActionPerformed
/********************************************************************************
***************************Fim Aba Centro de Custo*******************************
*********************************************************************************/
    
/********************************************************************************
********************** Aba Despesa por Centro de Custo***************************
*********************************************************************************/
    private void jButtonSalvarDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarDespCCustoActionPerformed
        if(verifica_campos_vazio_despesaccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloDespesaCCusto mod = new ModeloDespesaCCusto();
            ControleDespesaCCusto controleDespesaCCusto = new ControleDespesaCCusto();
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCusto.getText()));
            mod.setDescricao(jTextFieldDsDespCCusto.getText());
           
            controleDespesaCCusto.grava_despesaccusto(mod);
            try {
                preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposdespesaccusto();
        }
    }//GEN-LAST:event_jButtonSalvarDespCCustoActionPerformed

    private void jTableDespesaCCustoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDespesaCCustoMouseClicked
        limpar_camposdespesaccusto();
        String codigo = ""+jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 0);
        codigoDespCCusto = Integer.parseInt(codigo);
        jTextFieldDsDespCCusto.setText((String) jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 1));
        jTextFieldDsCCusto.setText((String) jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 2));
        retornaFks(codigoDespCCusto);
        habilitar_campo_despesaccusto(true);
    }//GEN-LAST:event_jTableDespesaCCustoMouseClicked

    private void jButtonAlterarDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarDespCCustoActionPerformed
        if(verifica_campos_vazio_despesaccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloDespesaCCusto mod = new ModeloDespesaCCusto();
            ControleDespesaCCusto controleDespesaCCusto = new ControleDespesaCCusto();
            mod.setCodigo(codigoDespCCusto);
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCusto.getText()));
            mod.setDescricao(jTextFieldDsDespCCusto.getText());
            
            controleDespesaCCusto.altera_despesaccusto(mod);
            try {
                preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposdespesaccusto();
            habilitar_campo_despesaccusto(false);
        }
    }//GEN-LAST:event_jButtonAlterarDespCCustoActionPerformed

    private void jButtonExcluirDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirDespCCustoActionPerformed
        ModeloDespesaCCusto mod = new ModeloDespesaCCusto();
        ControleDespesaCCusto controleDespesaCCusto = new ControleDespesaCCusto();
        mod.setCodigo(codigoDespCCusto);
        controleDespesaCCusto.exclui_despesaccusto(mod);
        try {
            preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpar_camposdespesaccusto();
        habilitar_campo_despesaccusto(false);
    }//GEN-LAST:event_jButtonExcluirDespCCustoActionPerformed
    
    private void jTextFieldCdCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCusto.setText(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoKeyPressed

    private void jTextFieldDsCCustoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCustoFocusGained
        jTextFieldDsCCusto.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCustoFocusGained

/********************************************************************************
********************Fim da Exclusão dos dados da Aba Carro***********************
*********************************************************************************/
    
/********************************************************************************
**************************Exclui dos dados da Aba Banco**************************
*********************************************************************************/
    private void jButtonExcluiBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluiBancoActionPerformed
        ModeloBanco mod = new ModeloBanco();
        ControleBanco controleBanco = new ControleBanco();
        mod.setCodigo(codigoBanco);
        controleBanco.exclui_banco(mod);
        preencherTabelaBanco(SQL_tabelaBanco);
        limpar_camposBanco();
        habilitar_campo_banco(false);
    }//GEN-LAST:event_jButtonExcluiBancoActionPerformed
/********************************************************************************
********************Fim da Exclusão dos dados da Aba Banco***********************
*********************************************************************************/
    
/********************************************************************************
******************************Aba Receitas*********************************
*********************************************************************************/
    private void jButtonSalvarReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarReceitaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtReceita.getText().substring(0, 2);
        mes = jFormattedTextFieldDtReceita.getText().substring(3, 5);
        ano = jFormattedTextFieldDtReceita.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtReceita = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_Receita() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtReceita) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloReceita mod = new ModeloReceita();
            ControleReceita controleReceita = new ControleReceita();
            
            mod.setData(dtReceita);
            
            mod.setFkCLucro(Integer.parseInt(jTextFieldCodCLucroRec.getText()));
            mod.setFkRecCLucro(Integer.parseInt(jTextFieldCodRecCLucroRec.getText()));
            mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoRec.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            controleReceita.grava_receita(mod);
            try {
                preencherTabelaReceita(SQL_tabelaReceita);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            montaSqlUpdateSoma(Integer.parseInt(jTextFieldCdBancoRec.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            conexao.executaSqlUpdate(SQL_UpdateSoma);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposReceita();
        }
    }//GEN-LAST:event_jButtonSalvarReceitaActionPerformed

    private void jTableReceitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableReceitaMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
        limpar_camposReceita();
        String codigo = ""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 0);
        codigoReceita = Integer.parseInt(codigo);
  
        ano = (""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 4)).substring(0, 4);
        mes = (""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 4)).substring(5, 7);
        dia = (""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 4)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDtReceita.setText(data);
        
        jTextFieldDsCLucroRec.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 2));
        jTextFieldDsRecCLucroRec.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 3));
        jTextFieldVlReceita.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 1));
        jTextFieldDsBancoRec.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 5));
        retornaFksReceita(codigoReceita);
        habilitar_campo_Receita(true);
        jButtonSalvarReceita.setEnabled(false);
        valorOriginal = (String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 1);
    }//GEN-LAST:event_jTableReceitaMouseClicked

    private void jTextFieldCodCLucroRecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodCLucroRecKeyPressed
       if(evt.getKeyCode() == evt.VK_F9){
            consultaCLucro.setVisible(true);
            jTextFieldCodCLucroRec.setText(this.consultaCLucro.getCodigo());
            fkCLucro = Integer.parseInt(this.consultaCLucro.getCodigo());
            jtDS_CLucro = this.consultaCLucro.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCodCLucroRecKeyPressed

    private void jTextFieldCodRecCLucroRecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodRecCLucroRecKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            String consulta = "SELECT CD_RECCLUCRO, DS_RECCLUCRO FROM RECEITACLUCRO WHERE FK_CLUCRO = "+fkCLucro+"";
            consultaRecCLucro = new ConsultaRecCLucro(new javax.swing.JFrame(), true, consulta);
            consultaRecCLucro.setVisible(true);
            jTextFieldCodRecCLucroRec.setText(this.consultaRecCLucro.getCodigo());
            jtDS_RecCLucro = this.consultaRecCLucro.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCodRecCLucroRecKeyPressed

    private void jButtonAlterarReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarReceitaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtReceita.getText().substring(0, 2);
        mes = jFormattedTextFieldDtReceita.getText().substring(3, 5);
        ano = jFormattedTextFieldDtReceita.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtReceita = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_Receita() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtReceita) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloReceita mod = new ModeloReceita();
            ControleReceita controleReceita = new ControleReceita();
            mod.setCodigo(codigoReceita);
            
            mod.setData(dtReceita);
            
            mod.setFkCLucro(Integer.parseInt(jTextFieldCodCLucroRec.getText()));
            mod.setFkRecCLucro(Integer.parseInt(jTextFieldCodRecCLucroRec.getText()));
            mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoRec.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            
            controleReceita.altera_receita(mod);
            try {
                preencherTabelaReceita(SQL_tabelaReceita);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(Integer.parseInt(jTextFieldCdBancoRec.getText()) == codBancoOrig){
                montaSqlUpdateAltera_Receita(Integer.parseInt(jTextFieldCdBancoRec.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
                conexao.executaSqlUpdate(SQL_UpdateAltera_Receita);
            }else{
                montaSqlUpdateSubtrai(codBancoOrig, Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
                conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                montaSqlUpdateSoma(Integer.parseInt(jTextFieldCdBancoRec.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
                conexao.executaSqlUpdate(SQL_UpdateSoma);
            }
            
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposReceita();
            habilitar_campo_Receita(false);
            jButtonSalvarReceita.setEnabled(true);
        }
    }//GEN-LAST:event_jButtonAlterarReceitaActionPerformed

    private void jButtonExcluirReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirReceitaActionPerformed
        ModeloReceita mod = new ModeloReceita();
        ControleReceita controleReceita = new ControleReceita();
        mod.setCodigo(codigoReceita);
        controleReceita.exclui_receita(mod);
        try {
            preencherTabelaReceita(SQL_tabelaReceita);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoRec.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
        preencherTabelaBanco(SQL_tabelaBanco);

        limpar_camposReceita();
        habilitar_campo_Receita(false);
        jButtonSalvarParcela.setEnabled(true);
    }//GEN-LAST:event_jButtonExcluirReceitaActionPerformed

    private void jTextFieldDsCLucroRecFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCLucroRecFocusGained
        jTextFieldDsCLucroRec.setText(jtDS_CLucro);
    }//GEN-LAST:event_jTextFieldDsCLucroRecFocusGained

    private void jTextFieldDsRecCLucroRecFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsRecCLucroRecFocusGained
        jTextFieldDsRecCLucroRec.setText(jtDS_RecCLucro);
    }//GEN-LAST:event_jTextFieldDsRecCLucroRecFocusGained
/********************************************************************************
******************************Fim da Aba Receita*********************************
*********************************************************************************/
    
/********************************************************************************
*************************************Aba Despesa*********************************
*********************************************************************************/
    private void jButtonSalvarDespesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarDespesaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtDespesa.getText().substring(0, 2);
        mes = jFormattedTextFieldDtDespesa.getText().substring(3, 5);
        ano = jFormattedTextFieldDtDespesa.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtDespesa = java.sql.Date.valueOf(data);

        if(verifica_campos_vazio_despesa()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtDespesa) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloDespesa mod = new ModeloDespesa();
            ControleDespesa controleDespesa = new ControleDespesa();
            
            mod.setData(dtDespesa);
            
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCustoDesp.getText()));
            mod.setFkDespCCusto(Integer.parseInt(jTextFieldCdDespCCusto.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
            mod.setSituacao(jComboBoxStDesp.getSelectedIndex());
            if(jComboBoxStDesp.getSelectedIndex() == 0){
                mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoDesp.getText()));
                
                if(verificaSaldo(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")))){
                    controleDespesa.grava_despesa(mod);
                    montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
                    conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                    preencherTabelaBanco(SQL_tabelaBanco);
                    limpar_camposdespesa();
                }
            }else{
                controleDespesa.grava_despesa(mod);
                limpar_camposdespesa();
            }
            
            try {
                preencherTabelaDespesa(SQL_tabelaDespesa);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonSalvarDespesaActionPerformed

    private void jButtonAlterarDespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarDespActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtDespesa.getText().substring(0, 2);
        mes = jFormattedTextFieldDtDespesa.getText().substring(3, 5);
        ano = jFormattedTextFieldDtDespesa.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtDespesa = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_despesa()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtDespesa) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloDespesa mod = new ModeloDespesa();
            ControleDespesa controleDespesa = new ControleDespesa();
            mod.setCodigo(codigoDespesa);
            
            mod.setData(dtDespesa);
            
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCustoDesp.getText()));
            mod.setFkDespCCusto(Integer.parseInt(jTextFieldCdDespCCusto.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
            mod.setSituacao(jComboBoxStDesp.getSelectedIndex());
            if(jComboBoxStDesp.getSelectedIndex() == 0){
                mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoDesp.getText()));
                if(verificaSaldoAlteracao(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(valorOriginal), Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")))){
                    controleDespesa.altera_despesa(mod);
                    float result = (Float.parseFloat(valorOriginal)) - (Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
                    //soma caso o valor da despesa é menor que a anterior
                    if(result > 0){
                        montaSqlUpdateSoma(Integer.parseInt(jTextFieldCdBancoDesp.getText()), result);
                        conexao.executaSqlUpdate(SQL_UpdateSoma);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    //esse else abaixo, subtrai o saldo do banco, quando estou alterando a despesa de a pagar para pago.
                    }else if(aPagar == true && result == 0){
                        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoDesp.getText()), (Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", "."))));
                        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    }
                    //esse else subtrai o saldo do banco, quando o valor da despesa a ser alterado é maior do que o valor anterior
                    else{
                        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoDesp.getText()), (result*(-1)));
                        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    }
                    limpar_camposdespesa();
                }
            }else{
                controleDespesa.altera_despesa(mod);
                limpar_camposdespesa();
            }
            
            
            try {
                preencherTabelaDespesa(SQL_tabelaDespesa);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            habilitar_campo_despesa(false);
            jButtonSalvarDespesa.setEnabled(true);
        }
    }//GEN-LAST:event_jButtonAlterarDespActionPerformed

    private void jTextFieldCdCCustoDespKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoDespKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCustoDesp.setText(this.consultaCCusto.getCodigo());
            fkCCusto = Integer.parseInt(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoDespKeyPressed

    private void jTextFieldCdDespCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdDespCCustoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            String consulta = "SELECT CD_DESPCCUSTO, DS_DESPCCUSTO FROM DESPESACCUSTO WHERE FK_CCUSTO = "+fkCCusto+"";
            consultaDespCCusto = new ConsultaDespCCusto(new javax.swing.JFrame(), true, consulta);
            consultaDespCCusto.setVisible(true);
            jTextFieldCdDespCCusto.setText(this.consultaDespCCusto.getCodigo());
            jtDS_DespCCusto = this.consultaDespCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdDespCCustoKeyPressed

    private void jTextFieldCdBancoDespKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdBancoDespKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaBanco.setVisible(true);
            jTextFieldCdBancoDesp.setText(this.consultaBanco.getCodigo());
            jtDS_Banco = this.consultaBanco.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdBancoDespKeyPressed

    private void jTextFieldDsBancoDespFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsBancoDespFocusGained
        jTextFieldDsBancoDesp.setText(jtDS_Banco);
    }//GEN-LAST:event_jTextFieldDsBancoDespFocusGained

    private void jTextFieldDsCCuscoDespFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCuscoDespFocusGained
        jTextFieldDsCCuscoDesp.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCuscoDespFocusGained

    private void jTextFieldDsDespCCustoDespFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsDespCCustoDespFocusGained
        jTextFieldDsDespCCustoDesp.setText(jtDS_DespCCusto);
    }//GEN-LAST:event_jTextFieldDsDespCCustoDespFocusGained

    private void jTableDespesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDespesaMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
        limpar_camposdespesa();
        String codigo = ""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 0);
        codigoDespesa = Integer.parseInt(codigo);
  
        ano = (""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 4)).substring(0, 4);
        mes = (""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 4)).substring(5, 7);
        dia = (""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 4)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDtDespesa.setText(data);
        
        jTextFieldDsCCuscoDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 3));
        jTextFieldDsDespCCustoDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 2));
        jTextFieldVlDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 1));
        jTextFieldDsBancoDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 6));
        if((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 5) == "Pago"){
            jComboBoxStDesp.setSelectedIndex(0);
        }else if((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 5) == "A Pagar"){
            aPagar = true;
            jComboBoxStDesp.setSelectedIndex(1);
        }
        retornaFksDespesa(codigoDespesa);
        habilitar_campo_despesa(true);
        jButtonSalvarDespesa.setEnabled(false);
        valorOriginal = (String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 1);
    }//GEN-LAST:event_jTableDespesaMouseClicked

    private void jButtonExcluirDespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirDespActionPerformed
        ModeloDespesa mod = new ModeloDespesa();
        ControleDespesa controleDespesa = new ControleDespesa();
        mod.setCodigo(codigoDespesa);
        if(jComboBoxStDesp.getSelectedIndex() == 0){
            estornaSaldo(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(valorOriginal));
            controleDespesa.exclui_despesa(mod);
            limpar_camposdespesa();
        }else{
            controleDespesa.exclui_despesa(mod);
            limpar_camposdespesa();
        }
        try {
            preencherTabelaDespesa(SQL_tabelaDespesa);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*montaSqlUpdateExclui_Receita(Integer.parseInt(jTextFieldCodBanco.getText()), Float.parseFloat(jTextFieldVlReceita.getText()));
        conexao.executaSqlUpdate(SQL_UpdateExclui_Receita);
        preencherTabelaBanco(SQL_tabelaBanco);*/

        limpar_camposdespesa();
        habilitar_campo_despesa(false);
        jButtonSalvarParcela.setEnabled(true);
    }//GEN-LAST:event_jButtonExcluirDespActionPerformed

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        jButton3.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        jButton3.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
       jButton1.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        jButton1.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton1MouseExited

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        jButton2.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        jButton2.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        jButton9.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        jButton9.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        jButton4.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        jButton5.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        jButton5.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton5MouseExited

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        jButton6.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        jButton6.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        jButton7.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        jButton7.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        jButton8.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        jButton8.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton8MouseExited

    private void jButtonTransferenciaBancariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTransferenciaBancariaActionPerformed
        TransferenciaBancaria transferencia = new TransferenciaBancaria(new javax.swing.JFrame(), true);
        transferencia.setVisible(true);
        preencherTabelaBanco(SQL_tabelaBanco);
    }//GEN-LAST:event_jButtonTransferenciaBancariaActionPerformed
    
    public boolean verificaCampoRel1(){
        if(jFormattedDataIni1.getText().isEmpty() || jFormattedFim1.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    private void jButtonGerarRel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel1ActionPerformed
        if(verificaCampoRel1() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel1() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni1.getText().substring(0, 2);
            mes = jFormattedDataIni1.getText().substring(3, 5);
            ano = jFormattedDataIni1.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedFim1.getText().substring(0, 2);
            mes = jFormattedFim1.getText().substring(3, 5);
            ano = jFormattedFim1.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);

            String SQLReceita = "SELECT DT_RECEITA, FK_CLUCRO, VL_RECEITA FROM RECEITA WHERE DT_RECEITA BETWEEN '"+dtInicio+"' AND '"+dtFim+"' ORDER BY DT_RECEITA DESC;";
            String SQLDespesa = "SELECT DT_DESPESA, FK_CCUSTO, VL_DESPESA FROM DESPESA WHERE DT_DESPESA BETWEEN '"+dtInicio+"' AND '"+dtFim+"' ORDER BY DT_DESPESA DESC;";
            Relatorio1 rel1 = new Relatorio1(SQLReceita, SQLDespesa);
            rel1.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel1ActionPerformed
    
            
    public boolean verificaCampoRel2(){
        if(jFormattedDataIni2.getText().isEmpty() || jFormattedDataFim2.getText().isEmpty() || jTextFieldCdCLucroRel2.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    private void jButtonGerarRel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel2ActionPerformed
        if(verificaCampoRel2() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel2() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni2.getText().substring(0, 2);
            mes = jFormattedDataIni2.getText().substring(3, 5);
            ano = jFormattedDataIni2.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedDataFim2.getText().substring(0, 2);
            mes = jFormattedDataFim2.getText().substring(3, 5);
            ano = jFormattedDataFim2.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);
            
            int codCLucro = Integer.parseInt(jTextFieldCdCLucroRel2.getText());
            
            Relatorio2 rel2 = new Relatorio2(dtInicio, dtFim, codCLucro);
            rel2.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel2ActionPerformed

    private void jTextFieldCdCLucroRel2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCLucroRel2KeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCLucro.setVisible(true);
            jTextFieldCdCLucroRel2.setText(this.consultaCLucro.getCodigo());
            jtDS_CLucro = this.consultaCLucro.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCLucroRel2KeyPressed

    private void jTextFieldDsCLucroRel2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCLucroRel2FocusGained
        jTextFieldDsCLucroRel2.setText(jtDS_CLucro);
    }//GEN-LAST:event_jTextFieldDsCLucroRel2FocusGained
    /********************************************************************************
    ***************************Aba Centro de Custo***********************************
    *********************************************************************************/
    private void jButtonAlterarCLucroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarCLucroActionPerformed
        if(verifica_campos_vazio_clucro()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCLucro mod = new ModeloCLucro();
            ControleCLucro controleCLucro = new ControleCLucro();
            mod.setDescricao(jTextFieldDS_CentroLucro.getText());
            mod.setCodigo(codigoCLucro);
            controleCLucro.altera_clucro(mod);
            preencherTabelaCLucro(SQL_tabelaCLucro);
            limpar_camposCLucro();
            habilitar_campo_clucro(false);
        }
    }//GEN-LAST:event_jButtonAlterarCLucroActionPerformed

    private void jButtonExcluirCLucroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirCLucroActionPerformed
        ModeloCLucro mod = new ModeloCLucro();
        ControleCLucro controleCLucro = new ControleCLucro();
        mod.setCodigo(codigoCLucro);
        controleCLucro.exclui_clucro(mod);
        preencherTabelaCLucro(SQL_tabelaCLucro);
        limpar_camposCLucro();
        habilitar_campo_clucro(false);
    }//GEN-LAST:event_jButtonExcluirCLucroActionPerformed

    private void jTableCentroLucroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCentroLucroMouseClicked
        String codigo = ""+jTableCentroLucro.getValueAt(jTableCentroLucro.getSelectedRow(), 0);
        codigoCLucro = Integer.parseInt(codigo);
        jTextFieldDS_CentroLucro.setText((String) jTableCentroLucro.getValueAt(jTableCentroLucro.getSelectedRow(), 1));
        habilitar_campo_clucro(true);
    }//GEN-LAST:event_jTableCentroLucroMouseClicked

    private void jButtonSalvarCLucroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarCLucroActionPerformed
        if(verifica_campos_vazio_clucro() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCLucro mod = new ModeloCLucro();
            ControleCLucro controleCLucro = new ControleCLucro();
            mod.setDescricao(jTextFieldDS_CentroLucro.getText());
            controleCLucro.grava_clucro(mod);
            preencherTabelaCLucro(SQL_tabelaCLucro);
            limpar_camposCLucro();
        }

    }//GEN-LAST:event_jButtonSalvarCLucroActionPerformed
    /********************************************************************************
    ***********************Fim Aba Centro de Custo***********************************
    *********************************************************************************/  
    
    /********************************************************************************
    ***************************Aba Receita Centro de Lucro***************************
    *********************************************************************************/  
    private void jButtonAlterarRecCLucroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarRecCLucroActionPerformed
        if(verifica_campos_vazio_receitaclucro()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloReceitaCLucro mod = new ModeloReceitaCLucro();
            ControleReceitaCLucro controleReceitaCLucro = new ControleReceitaCLucro();
            mod.setCodigo(codigoRecCLucro);
            mod.setFkCLucro(Integer.parseInt(jTextFieldCdCLucro.getText()));
            mod.setDescricao(jTextFieldDsRecCLucro.getText());
            
            controleReceitaCLucro.altera_receitaclucro(mod);
            try {
                preencherTabelaReceiraCLucro(SQL_tabelaRecCLucro);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposreceitaclucro();
            habilitar_campo_receitaclucro(false);
        }
    }//GEN-LAST:event_jButtonAlterarRecCLucroActionPerformed

    private void jButtonExcluirRecCLucroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirRecCLucroActionPerformed
        ModeloReceitaCLucro mod = new ModeloReceitaCLucro();
        ControleReceitaCLucro controleReceitaCLucro = new ControleReceitaCLucro();
        mod.setCodigo(codigoRecCLucro);
        controleReceitaCLucro.exclui_receitaclucro(mod);
        try {
            preencherTabelaReceiraCLucro(SQL_tabelaRecCLucro);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpar_camposreceitaclucro();
        habilitar_campo_receitaclucro(false);
    }//GEN-LAST:event_jButtonExcluirRecCLucroActionPerformed

    private void jTextFieldCdCLucroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCLucroKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCLucro.setVisible(true);
            jTextFieldCdCLucro.setText(this.consultaCLucro.getCodigo());
            jtDS_CLucro = this.consultaCLucro.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCLucroKeyPressed

    private void jTextFieldDsCLucroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCLucroFocusGained
        jTextFieldDsCLucro.setText(jtDS_CLucro);
    }//GEN-LAST:event_jTextFieldDsCLucroFocusGained

    private void jTableReceitaCLucroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableReceitaCLucroMouseClicked
        limpar_camposreceitaclucro();
        String codigo = ""+jTableReceitaCLucro.getValueAt(jTableReceitaCLucro.getSelectedRow(), 0);
        codigoRecCLucro = Integer.parseInt(codigo);
        jTextFieldDsRecCLucro.setText((String) jTableReceitaCLucro.getValueAt(jTableReceitaCLucro.getSelectedRow(), 1));
        jTextFieldDsCLucro.setText((String) jTableReceitaCLucro.getValueAt(jTableReceitaCLucro.getSelectedRow(), 2));
        retornaFkCLucro(codigoRecCLucro);
        habilitar_campo_receitaclucro(true);
    }//GEN-LAST:event_jTableReceitaCLucroMouseClicked

    private void jButtonSalvarRecCLucroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarRecCLucroActionPerformed
        if(verifica_campos_vazio_receitaclucro() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloReceitaCLucro mod = new ModeloReceitaCLucro();
            ControleReceitaCLucro controleReceitaCLucro = new ControleReceitaCLucro();
            mod.setFkCLucro(Integer.parseInt(jTextFieldCdCLucro.getText()));
            mod.setDescricao(jTextFieldDsRecCLucro.getText());
           
            controleReceitaCLucro.grava_receitaclucro(mod);
            try {
                preencherTabelaReceiraCLucro(SQL_tabelaRecCLucro);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposreceitaclucro();
        }
    }//GEN-LAST:event_jButtonSalvarRecCLucroActionPerformed
    /********************************************************************************
    ***********************Fim Aba Receita por Centro de Lucro***********************
    *********************************************************************************/
    private void jTextFieldCdBancoRecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdBancoRecKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaBanco.setVisible(true);
            jTextFieldCdBancoRec.setText(this.consultaBanco.getCodigo());
            jtDS_Banco = this.consultaBanco.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdBancoRecKeyPressed

    private void jTextFieldDsBancoRecFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsBancoRecFocusGained
        jTextFieldDsBancoRec.setText(jtDS_Banco);
    }//GEN-LAST:event_jTextFieldDsBancoRecFocusGained

    private void jTextFieldCdCCustoRel3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoRel3KeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCustoRel3.setText(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoRel3KeyPressed

    private void jTextFieldDsCCustoRel2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCustoRel2FocusGained
        jTextFieldDsCCustoRel2.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCustoRel2FocusGained

    public boolean verificaCampoRel3(){
        if(jFormattedDataIni3.getText().isEmpty() || jFormattedDataFim3.getText().isEmpty() || jTextFieldCdCCustoRel3.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    private void jButtonGerarRel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel3ActionPerformed
        if(verificaCampoRel3() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel3() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni3.getText().substring(0, 2);
            mes = jFormattedDataIni3.getText().substring(3, 5);
            ano = jFormattedDataIni3.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedDataFim3.getText().substring(0, 2);
            mes = jFormattedDataFim3.getText().substring(3, 5);
            ano = jFormattedDataFim3.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);
            
            int codCCusto = Integer.parseInt(jTextFieldCdCCustoRel3.getText());
            
            Relatorio3 rel3 = new Relatorio3(dtInicio, dtFim, codCCusto);
            rel3.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel3ActionPerformed
    /********************************************************************************
    ***********************Início da aba de contas parceladas************************
    *********************************************************************************/
    private void jTextFieldCdCCustoParcelaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoParcelaKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCustoParcela.setText(this.consultaCCusto.getCodigo());
            fkCCusto = Integer.parseInt(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoParcelaKeyPressed

    private void jTextFieldDsCCustoParcelaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCustoParcelaFocusGained
        jTextFieldDsCCustoParcela.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCustoParcelaFocusGained

    private void jTextFieldCdBancoParcelaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdBancoParcelaKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaBanco.setVisible(true);
            jTextFieldCdBancoParcela.setText(this.consultaBanco.getCodigo());
            jtDS_Banco = this.consultaBanco.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdBancoParcelaKeyPressed

    private void jTextFieldDsBancoParcelaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsBancoParcelaFocusGained
        jTextFieldDsBancoParcela.setText(jtDS_Banco);
    }//GEN-LAST:event_jTextFieldDsBancoParcelaFocusGained

    private void jButtonSalvarParcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarParcelaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDataVencParcela.getText().substring(0, 2);
        mes = jFormattedTextFieldDataVencParcela.getText().substring(3, 5);
        ano = jFormattedTextFieldDataVencParcela.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtVencimento = java.sql.Date.valueOf(data);

        if(verifica_campos_vazio_parcela()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloParcela mod = new ModeloParcela();
            ControleParcela controleParcela = new ControleParcela();
            
            mod.setVencimento(dtVencimento);
            
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCustoParcela.getText()));
            mod.setDescricao(jTextFieldDsDespCCustoParcela.getText());
            mod.setParcela(Integer.parseInt(jFormattedTextFieldNrParcela.getText()));
            mod.setValor(Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", ".")));
            mod.setSituacao(jComboBoxStParcela.getSelectedIndex());
            if(jComboBoxStParcela.getSelectedIndex() == 0){
                mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoParcela.getText()));
                
                if(verificaSaldo(Integer.parseInt(jTextFieldCdBancoParcela.getText()), Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", ".")))){
                    controleParcela.grava_parcela(mod);
                    montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoParcela.getText()), Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", ".")));
                    conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                    preencherTabelaBanco(SQL_tabelaBanco);
                    limpar_camposparcela();
                }
            }else{
                controleParcela.grava_parcela(mod);
                limpar_camposparcela();
            }
            
            try {
                preencherTabelaParcela(SQL_tabelaParcela);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonSalvarParcelaActionPerformed

    private void jTableContasParceladasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableContasParceladasMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
        limpar_camposparcela();
        String codigo = ""+jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 0);
        codigoParcela = Integer.parseInt(codigo);
  
        ano = (""+jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 4)).substring(0, 4);
        mes = (""+jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 4)).substring(5, 7);
        dia = (""+jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 4)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDataVencParcela.setText(data);
        
        jTextFieldDsCCustoParcela.setText((String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 3));
        jTextFieldDsDespCCustoParcela.setText((String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 2));
        jFormattedTextFieldValorParcela.setText((String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 1));
        jTextFieldDsBancoParcela.setText((String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 7));
        String nrParcela = ""+jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 5);
        jFormattedTextFieldNrParcela.setText(nrParcela);
        if((String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 6) == "Pago"){
            jComboBoxStParcela.setSelectedIndex(0);
        }else if((String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 6) == "A Pagar"){
            aPagar = true;
            jComboBoxStParcela.setSelectedIndex(1);
        }
        retornaFksParcela(codigoParcela);
        habilitar_campo_parcela(true);
        jButtonSalvarParcela.setEnabled(false);
        valorOriginal = (String) jTableContasParceladas.getValueAt(jTableContasParceladas.getSelectedRow(), 1);
    }//GEN-LAST:event_jTableContasParceladasMouseClicked

    private void jButtonAlterarParcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarParcelaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDataVencParcela.getText().substring(0, 2);
        mes = jFormattedTextFieldDataVencParcela.getText().substring(3, 5);
        ano = jFormattedTextFieldDataVencParcela.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtParcela = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_parcela() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloParcela mod = new ModeloParcela();
            ControleParcela controleParcela = new ControleParcela();
            mod.setCodigo(codigoParcela);
            
            mod.setVencimento(dtParcela);
            
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCustoParcela.getText()));
            mod.setDescricao(jTextFieldDsDespCCustoParcela.getText());
            mod.setParcela(Integer.parseInt(jFormattedTextFieldNrParcela.getText()));
            mod.setValor(Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", ".")));
            mod.setSituacao(jComboBoxStParcela.getSelectedIndex());
            if(jComboBoxStParcela.getSelectedIndex() == 0){
                mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoParcela.getText()));
                if(verificaSaldoAlteracao(Integer.parseInt(jTextFieldCdBancoParcela.getText()), Float.parseFloat(valorOriginal), Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", ".")))){
                    controleParcela.altera_parcela(mod);
                    float result = (Float.parseFloat(valorOriginal)) - (Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", ".")));
                    //soma caso o valor da despesa é menor que a anterior
                    if(result > 0){
                        montaSqlUpdateSoma(Integer.parseInt(jTextFieldCdBancoParcela.getText()), result);
                        conexao.executaSqlUpdate(SQL_UpdateSoma);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    //esse else abaixo, subtrai o saldo do banco, quando estou alterando a despesa de a pagar para pago.
                    }else if(aPagar == true && result == 0){
                        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoParcela.getText()), (Float.parseFloat(jFormattedTextFieldValorParcela.getText().replaceAll(",", "."))));
                        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    }
                    //esse else subtrai o saldo do banco, quando o valor da despesa a ser alterado é maior do que o valor anterior
                    else{
                        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoParcela.getText()), (result*(-1)));
                        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    }
                    limpar_camposparcela();
                }
            }else{
                controleParcela.altera_parcela(mod);
                limpar_camposparcela();
            }
            
            
            try {
                preencherTabelaParcela(SQL_tabelaParcela);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            habilitar_campo_parcela(false);
            jButtonSalvarParcela.setEnabled(true);
        }
    }//GEN-LAST:event_jButtonAlterarParcelaActionPerformed

    private void jButtonExcluirParcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirParcelaActionPerformed
        ModeloParcela mod = new ModeloParcela();
        ControleParcela controleParcela = new ControleParcela();
        mod.setCodigo(codigoParcela);
        if(jComboBoxStParcela.getSelectedIndex() == 0){
            estornaSaldo(Integer.parseInt(jTextFieldCdBancoParcela.getText()), Float.parseFloat(valorOriginal));
            controleParcela.exclui_parcela(mod);
            limpar_camposparcela();
        }else{
            controleParcela.exclui_parcela(mod);
            limpar_camposparcela();
        }
        try {
            preencherTabelaParcela(SQL_tabelaParcela);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*montaSqlUpdateExclui_Receita(Integer.parseInt(jTextFieldCodBanco.getText()), Float.parseFloat(jTextFieldVlReceita.getText()));
        conexao.executaSqlUpdate(SQL_UpdateExclui_Receita);
        preencherTabelaBanco(SQL_tabelaBanco);*/

        limpar_camposparcela();
        habilitar_campo_parcela(false);
        jButtonSalvarParcela.setEnabled(true);
    }//GEN-LAST:event_jButtonExcluirParcelaActionPerformed
      
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Banco;
    private javax.swing.JPanel CentroCusto;
    private javax.swing.JPanel CentroLucro;
    private javax.swing.JPanel Despesa;
    private javax.swing.JPanel DespesaCCusto;
    private javax.swing.JPanel DespesasParceladas;
    private javax.swing.JPanel LucroCLucro;
    private javax.swing.JPanel MenuLogo;
    private javax.swing.JPanel Receita;
    private javax.swing.JPanel Relatorios;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAlterarBanco;
    private javax.swing.JButton jButtonAlterarCCusto;
    private javax.swing.JButton jButtonAlterarCLucro;
    private javax.swing.JButton jButtonAlterarDesp;
    private javax.swing.JButton jButtonAlterarDespCCusto;
    private javax.swing.JButton jButtonAlterarParcela;
    private javax.swing.JButton jButtonAlterarRecCLucro;
    private javax.swing.JButton jButtonAlterarReceita;
    private javax.swing.JButton jButtonExcluiBanco;
    private javax.swing.JButton jButtonExcluirCCusto;
    private javax.swing.JButton jButtonExcluirCLucro;
    private javax.swing.JButton jButtonExcluirDesp;
    private javax.swing.JButton jButtonExcluirDespCCusto;
    private javax.swing.JButton jButtonExcluirParcela;
    private javax.swing.JButton jButtonExcluirRecCLucro;
    private javax.swing.JButton jButtonExcluirReceita;
    private javax.swing.JButton jButtonGerarRel1;
    private javax.swing.JButton jButtonGerarRel2;
    private javax.swing.JButton jButtonGerarRel3;
    private javax.swing.JButton jButtonSalvarBanco;
    private javax.swing.JButton jButtonSalvarCCusto;
    private javax.swing.JButton jButtonSalvarCLucro;
    private javax.swing.JButton jButtonSalvarDespCCusto;
    private javax.swing.JButton jButtonSalvarDespesa;
    private javax.swing.JButton jButtonSalvarParcela;
    private javax.swing.JButton jButtonSalvarRecCLucro;
    private javax.swing.JButton jButtonSalvarReceita;
    private javax.swing.JButton jButtonTransferenciaBancaria;
    private javax.swing.JComboBox jComboBoxStDesp;
    private javax.swing.JComboBox jComboBoxStParcela;
    private javax.swing.JFormattedTextField jFormattedDataFim2;
    private javax.swing.JFormattedTextField jFormattedDataFim3;
    private javax.swing.JFormattedTextField jFormattedDataIni1;
    private javax.swing.JFormattedTextField jFormattedDataIni2;
    private javax.swing.JFormattedTextField jFormattedDataIni3;
    private javax.swing.JFormattedTextField jFormattedFim1;
    private javax.swing.JFormattedTextField jFormattedTextFieldDS_BANCO;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCCusto;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCLucro;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataDespCCusto;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataDespCLucro;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataVencParcela;
    private javax.swing.JFormattedTextField jFormattedTextFieldDtDespesa;
    private javax.swing.JFormattedTextField jFormattedTextFieldDtReceita;
    private javax.swing.JFormattedTextField jFormattedTextFieldNrParcela;
    private javax.swing.JFormattedTextField jFormattedTextFieldValorParcela;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableBANCO;
    private javax.swing.JTable jTableCentroCusto;
    private javax.swing.JTable jTableCentroLucro;
    private javax.swing.JTable jTableContasParceladas;
    private javax.swing.JTable jTableDespesa;
    private javax.swing.JTable jTableDespesaCCusto;
    private javax.swing.JTable jTableReceita;
    private javax.swing.JTable jTableReceitaCLucro;
    private javax.swing.JTextField jTextFieldCdBancoDesp;
    private javax.swing.JTextField jTextFieldCdBancoParcela;
    private javax.swing.JTextField jTextFieldCdBancoRec;
    private javax.swing.JTextField jTextFieldCdCCusto;
    private javax.swing.JTextField jTextFieldCdCCustoDesp;
    private javax.swing.JTextField jTextFieldCdCCustoParcela;
    private javax.swing.JTextField jTextFieldCdCCustoRel3;
    private javax.swing.JTextField jTextFieldCdCLucro;
    private javax.swing.JTextField jTextFieldCdCLucroRel2;
    private javax.swing.JTextField jTextFieldCdDespCCusto;
    private javax.swing.JTextField jTextFieldCodCLucroRec;
    private javax.swing.JTextField jTextFieldCodRecCLucroRec;
    private javax.swing.JTextField jTextFieldDS_CentroCusto;
    private javax.swing.JTextField jTextFieldDS_CentroLucro;
    private javax.swing.JTextField jTextFieldDsBancoDesp;
    private javax.swing.JTextField jTextFieldDsBancoParcela;
    private javax.swing.JTextField jTextFieldDsBancoRec;
    private javax.swing.JTextField jTextFieldDsCCuscoDesp;
    private javax.swing.JTextField jTextFieldDsCCusto;
    private javax.swing.JTextField jTextFieldDsCCustoParcela;
    private javax.swing.JTextField jTextFieldDsCCustoRel2;
    private javax.swing.JTextField jTextFieldDsCLucro;
    private javax.swing.JTextField jTextFieldDsCLucroRec;
    private javax.swing.JTextField jTextFieldDsCLucroRel2;
    private javax.swing.JTextField jTextFieldDsDespCCusto;
    private javax.swing.JTextField jTextFieldDsDespCCustoDesp;
    private javax.swing.JTextField jTextFieldDsDespCCustoParcela;
    private javax.swing.JTextField jTextFieldDsRecCLucro;
    private javax.swing.JTextField jTextFieldDsRecCLucroRec;
    private javax.swing.JTextField jTextFieldNR_AGENCIA;
    private javax.swing.JTextField jTextFieldNR_CONTA;
    private javax.swing.JTextField jTextFieldVL_SALDO;
    private javax.swing.JTextField jTextFieldVlDesp;
    private javax.swing.JTextField jTextFieldVlReceita;
    // End of variables declaration//GEN-END:variables
}
