/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilitarios;

import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author euris
 */
public class Utilitarios {
    public void inserirIcone(JFrame frm){
        try{
            frm.setIconImage(Toolkit.getDefaultToolkit().getImage("src/Imagen/icone.png"));
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
}
