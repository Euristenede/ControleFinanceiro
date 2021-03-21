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
public class ModeloReceita {
    private int codigo;
    private Date data;
    private float valor;
    private int fkCLucro;
    private int fkRecCLucro;
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

    public int getFkCLucro() {
        return fkCLucro;
    }

    public void setFkCLucro(int fkCLucro) {
        this.fkCLucro = fkCLucro;
    }

    public int getFkRecCLucro() {
        return fkRecCLucro;
    }

    public void setFkRecCLucro(int fkRecCLucro) {
        this.fkRecCLucro = fkRecCLucro;
    }

    public int getFkBanco() {
        return fkBanco;
    }

    public void setFkBanco(int fkBanco) {
        this.fkBanco = fkBanco;
    }

    
    
}
