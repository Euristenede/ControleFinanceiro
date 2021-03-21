/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloFuncionario;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleFuncionario {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_funcionario(ModeloFuncionario mod){
        conexaoBanco.conecta();        
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO FUNCIONARIO (NM_FUNCIONARIO, NR_CPF, NR_RG, DT_NASCIMENTO, VL_SALARIO, DT_ADMISSAO, NR_TELEFONE, DS_CARGO) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, mod.getNome());
            pst.setLong(2, mod.getCpf());
            pst.setLong(3, mod.getRg());
            pst.setDate(4, mod.getDataNascimento());
            pst.setFloat(5, mod.getSalario());
            pst.setDate(6,mod.getDataAdimissao());
            pst.setLong(7, mod.getTelefone());
            pst.setString(8, mod.getCargo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados do Funcionario. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_funcionario(ModeloFuncionario mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE FUNCIONARIO SET NM_FUNCIONARIO = ?, NR_CPF = ?, NR_RG = ?, DT_NASCIMENTO = ?, VL_SALARIO = ?, DT_ADMISSAO = ?, NR_TELEFONE = ?, DS_CARGO = ? WHERE CD_FUNCIONARIO = ?;");
            pst.setString(1, mod.getNome());
            pst.setLong(2, mod.getCpf());
            pst.setLong(3, mod.getRg());
            pst.setDate(4, new java.sql.Date(mod.getDataNascimento().getTime()));
            pst.setFloat(5, mod.getSalario());
            pst.setDate(6, new java.sql.Date(mod.getDataAdimissao().getTime()));
            pst.setLong(7, mod.getTelefone());
            pst.setString(8, mod.getCargo());
            pst.setInt(9, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados do Funcionario.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_funcionario(ModeloFuncionario mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM FUNCIONARIO WHERE CD_FUNCIONARIO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados do Funcionario.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
