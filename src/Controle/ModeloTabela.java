/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Euristenede
 */

// A classe abaixo é responsável por montar a tabela
public class ModeloTabela extends AbstractTableModel{
    private ArrayList linhas = null; // Guarda a quantidade de dados do retorno
    private String[] colunas = null;//  Armazenar o nome das colunas
    
    // O metodo abaixo recebe um Array e uma String preenchida e vai setar pro setLinhas e pro setColunas 
    public ModeloTabela(ArrayList lin, String[] col){
        setLinhas(lin);
        setColunas(col);
    }
    
    public ArrayList getLinhas(){
        return linhas;
    }
    
    public void setLinhas(ArrayList dados){
        linhas = dados; // seta a quantidade de linhas no ModeloTabela
    }
    
    public String[] getColunas(){
        return colunas;
    }
    
    public void setColunas(String[] nomes){
        colunas = nomes; // seta a quantidade de colunas no ModeloTabela
    }
    
    @Override
    public int getColumnCount(){
        return colunas.length; // Conta a quantidade de colunas
    }
    
    @Override
    public int getRowCount(){
        return linhas.size();// Conta a quantidade de linhas
    }
    
    @Override
    public String getColumnName(int numCol){
        return colunas[numCol];// Conta a quantidade de nomes passado pra essa classe, pra saber quantas colunas terá
    }
    
    //No metodo abaixo-> Pega os valores e monta o Object linha
    // e retorna pra tabela a quantidade de linhas de acordo com a quantidade de colunas
    @Override
    public Object getValueAt(int numLin, int numCol){
        Object[] linha = (Object[])getLinhas().get(numLin);
        return linha[numCol];
    }
}
