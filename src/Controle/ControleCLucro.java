/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;


import Modelo.ModeloCLucro;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author euris
 */
public class ControleCLucro {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_clucro(ModeloCLucro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO CENTROLUCRO (DS_CLUCRO) VALUES(?)");
            pst.setString(1, mod.getDescricao());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados do Centro de Lucro. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_clucro(ModeloCLucro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE CENTROLUCRO SET DS_CLUCRO = ? WHERE CD_CLUCRO = ?;");
            pst.setString(1, mod.getDescricao());
            pst.setFloat(2, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados do Centro de Lucro.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_clucro(ModeloCLucro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM CENTROLUCRO WHERE CD_CLUCRO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados do Centro de Lucro.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }

}
