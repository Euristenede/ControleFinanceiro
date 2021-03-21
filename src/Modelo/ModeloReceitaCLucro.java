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
public class ModeloReceitaCLucro {
    private int codigo;
    private String descricao;
    private Date data;
    private int fkCLucro;

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

    public int getFkCLucro() {
        return fkCLucro;
    }

    public void setFkCLucro(int fkCLucro) {
        this.fkCLucro = fkCLucro;
    }
    
    
}
