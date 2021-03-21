/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloCarro;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleCarro {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_carro(ModeloCarro mod){
        conexaoBanco.conecta();        
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO CARRO (NR_PLACA, DS_MARCA, DS_MODELO, NR_ANO, TP_COMBUSTIVEL, NR_DOCUMENTO, DT_VECDOCUMENTO) VALUES(?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, mod.getPlaca());
            pst.setString(2, mod.getMarca());
            pst.setString(3, mod.getModelo());
            pst.setInt(4, mod.getAno());
            pst.setInt(5, mod.getCombustivel());
            pst.setLong(6,mod.getDocumento());
            pst.setDate(7, (Date) mod.getVencimento());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados do Carro. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_carro(ModeloCarro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE CARRO SET NR_PLACA = ?, DS_MARCA = ?, DS_MODELO = ?, NR_ANO = ?, TP_COMBUSTIVEL = ?, NR_DOCUMENTO = ?, DT_VECDOCUMENTO = ? WHERE CD_CARRO = ?;");
            pst.setString(1, mod.getPlaca());
            pst.setString(2, mod.getMarca());
            pst.setString(3, mod.getModelo());
            pst.setInt(4, mod.getAno());
            pst.setInt(5, mod.getCombustivel());
            pst.setLong(6,mod.getDocumento());
            pst.setDate(7, (Date) mod.getVencimento());
            pst.setInt(8, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados do Carro.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_carro(ModeloCarro mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM CARRO WHERE CD_CARRO = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados do Carro.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
