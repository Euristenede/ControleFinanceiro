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
public class ModeloParcela {
    private int     codigo;
    private String  descricao;
    private float   valor;
    private int     parcela;
    private Date    vencimento;
    private int     situacao;
    private int     fkCCusto;
    private int     fkBanco;

    public int getFkBanco() {
        return fkBanco;
    }

    public void setFkBanco(int fkBanco) {
        this.fkBanco = fkBanco;
    }
    
    

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getParcela() {
        return parcela;
    }

    public void setParcela(int parcela) {
        this.parcela = parcela;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public int getFkCCusto() {
        return fkCCusto;
    }

    public void setFkCCusto(int fkCCusto) {
        this.fkCCusto = fkCCusto;
    }
    
}
