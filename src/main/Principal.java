/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author isaia
 */
public class Principal {
    
    public static void main(String [] args ){
        Helper helper = new Helper();
        Cifradores cifradores = new Cifradores();

        String path;
        File archivo = new File("");
        path = archivo.getAbsolutePath();
        
        archivo = new File(path+"//Archivos//thundercats.bmp");
        
        ArrayList<Pixel> pixeles = helper.cargarImg(archivo);
        
        if( helper.guardarImagen(cifradores.ejemploModificarColor(pixeles), archivo, "prueba")){
            System.out.println("Ok");
        }else{
            System.out.println("erro");
        }
            
        
    }
    
}
