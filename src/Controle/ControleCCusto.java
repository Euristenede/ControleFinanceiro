/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloCCusto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleCCusto {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_ccusto(ModeloCCusto mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO CENTROCUSTO (DS_CCUSTO) VALUES(?)");
            pst.setString(1, mod.getDescricao());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados do Centro de Custo. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_ccusto(ModeloCCusto mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE CENTROCUSTO SET DS_CCUSTO = ? WHERE CD_CCUSTO = ?;");
            pst.setString(1, mod.getDescricao());
            pst.setFloat(2, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados do Centro de Custo.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_ccusto(ModeloCCusto mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM CENTROCUSTO WHERE CD_CCUSTO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados do Centro de Custo.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
