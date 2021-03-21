/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Euristenede
 */
public class ConectaBanco {
    public Statement stm;//responsavél por preparar e realizar pesquisas no banco de dados
    public ResultSet rs;//responsavél por armazenar o resultado de uma pesquisa passada para o statement
    public Statement stmSubConsulta;//responsavél por preparar e realizar pesquisas no banco de dados
    public ResultSet rsSubConsulta;//responsavél por armazenar o resultado de uma pesquisa passada para o statement
    private String driver = "org.postgresql.Driver";//Responsável por identificar o serviço de banco de dados
    private String caminho = "jdbc:postgresql://localhost:5432/FINANCEIRO_PESSOAL";// responsavél por setar o local do banco de dados
    private String usuario = "postgres";
    private String senha = "tenede98";
    public Connection conn;//responsavél por realizar a conexão com o banco de dados
    
    public void conecta(){//metodo responsavél por realizar a conexão com o banco
        try {//tentativa inicial
            System.setProperty("jdbc.Drivers", driver);// seta a propriedade do driver de conexão
            conn = DriverManager.getConnection(caminho, usuario, senha);// realiza a conexão com o banco de dados
            //JOptionPane.showMessageDialog(null, "Conectado com sucesso!");// imprime uma caixa de mensagen
        } catch (SQLException ex) {//excessão
            JOptionPane.showMessageDialog(null, "Erro de conexão!\n Erro"+ ex.getMessage());
        }
    }
    
    public void executaSQL(String sql){
        try {
            stm = conn.createStatement(rs.TYPE_SCROLL_INSENSITIVE,rs.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de executaSQL\n Erro"+ ex.getMessage());
        }
    }
    
    public void executaSqlSubConsulta(String sql){
        try {
            stmSubConsulta = conn.createStatement(rsSubConsulta.TYPE_SCROLL_INSENSITIVE,rsSubConsulta.CONCUR_READ_ONLY);
            rsSubConsulta = stmSubConsulta.executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de executaSQL\n Erro"+ ex.getMessage());
        }
    }
    
    public void executaSqlUpdate(String sql){
        try {
            stm = conn.createStatement(rs.TYPE_SCROLL_INSENSITIVE,rs.CONCUR_READ_ONLY);
            stm.executeQuery(sql);
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao executar o Update.\n Erro"+ ex.getMessage());
        }
    }
    
    public void desconecta(){// metodo para fechar a conexão com o banco de dados
        try {
            conn.close();// fecha a conexão
            //JOptionPane.showMessageDialog(null, "Desconectado com sucesso!");// imprime uma caixa de mensagen
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão! \n Erro"+ ex.getMessage());
        }
    }
    
}
