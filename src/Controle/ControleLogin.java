/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloLogin;
import java.sql.SQLException;

/**
 *
 * @author euris
 */
public class ControleLogin {
    ConectaBanco conexaoLogin = new ConectaBanco();
    boolean logado = false;
    public boolean logar(ModeloLogin mod) throws SQLException{
        conexaoLogin.conecta();
        conexaoLogin.executaSQL("SELECT * FROM USUARIO WHERE DS_USUARIO = '"+mod.getUsuario()+"' AND DS_SENHA = '"+mod.getSenha()+"'");
        while(conexaoLogin.rs.next()) {
            if(conexaoLogin.rs.getString("DS_USUARIO") != null && conexaoLogin.rs.getString("DS_SENHA") != null){
                logado = true;
            }else{
                logado = false;
            }
        }
        return logado;
    }
}
