/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloBanco;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleBanco {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_banco(ModeloBanco mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO BANCO (DS_BANCO, NR_AGENCIA, NR_CONTA, VL_SALDO) VALUES(?, ?, ?, ?)");
            pst.setString(1, mod.getBanco());
            pst.setInt(2, mod.getAgencia());
            pst.setInt(3, mod.getConta());
            pst.setFloat(4, mod.getSaldo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados do banco. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_banco(ModeloBanco mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE BANCO SET DS_BANCO = ?, NR_AGENCIA = ?, NR_CONTA = ?, VL_SALDO = ? WHERE CD_BANCO = ?;");
            pst.setString(1, mod.getBanco());
            pst.setInt(2, mod.getAgencia());
            pst.setInt(3, mod.getConta());
            pst.setFloat(4, mod.getSaldo());
            pst.setFloat(5, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados do Banco.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_banco(ModeloBanco mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM BANCO WHERE CD_BANCO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados do Banco.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
}
