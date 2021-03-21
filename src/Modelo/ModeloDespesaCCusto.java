/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author euris
 */
public class ModeloDespesaCCusto {
    private int codigo;
    private String descricao;
    private Date data;
    private int fkCCusto;
    private int fkFuncionario;
    private int fkVeiculo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getFkCCusto() {
        return fkCCusto;
    }

    public void setFkCCusto(int fkCCusto) {
        this.fkCCusto = fkCCusto;
    }

    public int getFkFuncionario() {
        return fkFuncionario;
    }

    public void setFkFuncionario(int fkFuncionario) {
        this.fkFuncionario = fkFuncionario;
    }

    public int getFkVeiculo() {
        return fkVeiculo;
    }

    public void setFkVeiculo(int fkVeiculo) {
        this.fkVeiculo = fkVeiculo;
    }
    
    
}
