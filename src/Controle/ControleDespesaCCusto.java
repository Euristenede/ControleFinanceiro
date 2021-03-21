/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloDespesaCCusto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleDespesaCCusto {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_despesaccusto(ModeloDespesaCCusto mod){
        conexaoBanco.conecta();        
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO DESPESACCUSTO (DS_DESPCCUSTO, FK_CCUSTO, FK_FUNCIONARIO, FK_CARRO) VALUES(?, ?, ?, ?)");
            pst.setString(1, mod.getDescricao());
            pst.setInt(2, mod.getFkCCusto());
            pst.setInt(3, mod.getFkFuncionario());
            pst.setInt(4, mod.getFkVeiculo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados da Despesa por Centro de Custo. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_despesaccusto(ModeloDespesaCCusto mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE DESPESACCUSTO SET DS_DESPCCUSTO = ?, FK_CCUSTO = ?, FK_FUNCIONARIO = ?, FK_CARRO = ? WHERE CD_DESPCCUSTO = ?;");
            pst.setString(1, mod.getDescricao());
            pst.setInt(2, mod.getFkCCusto());
            pst.setInt(3, mod.getFkFuncionario());
            pst.setInt(4, mod.getFkVeiculo());
            pst.setInt(5, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados da Despesa por Centro de Custo.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_despesaccusto(ModeloDespesaCCusto mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM DESPESACCUSTO WHERE CD_DESPCCUSTO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados da Despesa por Centro de Custo.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
