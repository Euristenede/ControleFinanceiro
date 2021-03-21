/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloParcela;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleParcela {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_parcela(ModeloParcela mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO PARCELA (DS_PARCELA, VL_PARCELA, NR_PARCELA, ST_PARCELA, DT_PARCELA, FK_CCUSTO, FK_BANCO) VALUES(?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, mod.getDescricao());
            pst.setFloat(2, mod.getValor());
            pst.setInt(3, mod.getParcela());
            pst.setInt(4, mod.getSituacao());
            pst.setDate(5, (Date) mod.getVencimento());
            pst.setInt(6, mod.getFkCCusto());
            pst.setInt(7, mod.getFkBanco());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados da Parcela. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_parcela(ModeloParcela mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE PARCELA SET DS_PARCELA = ?, VL_PARCELA = ?, NR_PARCELA = ?, ST_PARCELA = ?, DT_PARCELA = ?, FK_CCUSTO = ?, FK_BANCO = ? WHERE CD_PARCELA = ?;");
            pst.setString(1, mod.getDescricao());
            pst.setFloat(2, mod.getValor());
            pst.setInt(3, mod.getParcela());
            pst.setInt(4, mod.getSituacao());
            pst.setDate(5, (Date) mod.getVencimento());
            pst.setInt(6, mod.getFkCCusto());
            pst.setInt(7, mod.getFkBanco());
            pst.setInt(8, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados da Parcela.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_parcela(ModeloParcela mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM PARCELA WHERE CD_PARCELA = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados da Parcela.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
