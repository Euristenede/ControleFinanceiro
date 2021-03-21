/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloDespesa;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleDespesa {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_despesa(ModeloDespesa mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO DESPESA (DT_DESPESA, VL_DESPESA, ST_DESPESA, FK_CCUSTO, FK_DESPCCUSTO, FK_BANCO) VALUES(?, ?, ?, ?, ?, ?)");
            pst.setDate(1, (Date) mod.getData());
            pst.setFloat(2, mod.getValor());
            pst.setInt(3, mod.getSituacao());
            pst.setInt(4, mod.getFkCCusto());
            pst.setInt(5, mod.getFkDespCCusto());
            pst.setInt(6, mod.getFkBanco());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados da Despesa. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_despesa(ModeloDespesa mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE DESPESA SET DT_DESPESA = ?, VL_DESPESA = ?, ST_DESPESA = ?, FK_CCUSTO = ?, FK_DESPCCUSTO = ?, FK_BANCO = ? WHERE CD_DESPESA = ?;");
            pst.setDate(1, (Date) mod.getData());
            pst.setFloat(2, mod.getValor());
            pst.setInt(3, mod.getSituacao());
            pst.setInt(4, mod.getFkCCusto());
            pst.setInt(5, mod.getFkDespCCusto());
            pst.setInt(6, mod.getFkBanco());
            pst.setFloat(7, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados da Despesa.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_despesa(ModeloDespesa mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM DESPESA WHERE CD_DESPESA = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados da Despesa.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
