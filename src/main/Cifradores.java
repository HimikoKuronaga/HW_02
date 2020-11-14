/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;

/**
 *
 * @author isaia
 */
public class Cifradores {
    
    //Constructor
    public Cifradores(){};
    
    /**
     * Ejemplo de modificacion de pixeles de imagen
     * @param pixeles
     * @return 
     */
    public ArrayList<Pixel> ejemploModificarColor( ArrayList<Pixel> pixeles ){
        
        for( Pixel pixel : pixeles ){
            if(pixel.getRed() == 255 && pixel.getGreen()==0 && pixel.getBlue()== 0){
                pixel.setRGB(191, 140, 225);
            }
        }
        
        return pixeles;
    }
}
