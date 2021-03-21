/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloReceitaCLucro;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleReceitaCLucro {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_receitaclucro(ModeloReceitaCLucro mod){
        conexaoBanco.conecta();        
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO RECEITACLUCRO (DS_RECCLUCRO, FK_CLUCRO) VALUES(?, ?)");
            pst.setString(1, mod.getDescricao());
            pst.setInt(2, mod.getFkCLucro());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados da Receita por Centro de Lucro. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_receitaclucro(ModeloReceitaCLucro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE RECEITACLUCRO SET DS_RECCLUCRO = ?, FK_CLUCRO = ? WHERE CD_RECCLUCRO = ?;");
            pst.setString(1, mod.getDescricao());
            pst.setInt(2, mod.getFkCLucro());
            pst.setInt(3, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados da Receita por Centro de Lucro.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_receitaclucro(ModeloReceitaCLucro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM RECEITACLUCRO WHERE CD_RECCLUCRO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados da Receita por Centro de Lucro.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
