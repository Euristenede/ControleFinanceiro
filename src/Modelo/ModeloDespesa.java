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
public class ModeloDespesa {
    private int codigo;
    private Date data;
    private float valor;
    private int situacao;
    private int fkCCusto;
    private int fkDespCCusto;
    private int fkBanco;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public int getFkCCusto() {
        return fkCCusto;
    }

    public void setFkCCusto(int fkCCusto) {
        this.fkCCusto = fkCCusto;
    }

    public int getFkDespCCusto() {
        return fkDespCCusto;
    }

    public void setFkDespCCusto(int fkDespCCusto) {
        this.fkDespCCusto = fkDespCCusto;
    }

    public int getFkBanco() {
        return fkBanco;
    }

    public void setFkBanco(int fkBanco) {
        this.fkBanco = fkBanco;
    }
    
    
}
