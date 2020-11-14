/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author isaia
 */
public class Helper {
    
    private ArrayList<Pixel> pixeles= new ArrayList();
    
    //Contructor de clase
    public Helper(){
        
    }
    
    /**
     * Carga la imagen y obtiene los pixeles guardando los datos de r g b
     * @param archivo
     * @return 
     */
    public ArrayList<Pixel> cargarImg( File archivo ){
        BufferedImage imgBuf = null;
        
        try {
            //Cargar la imagen en el mapa de bits para operar los pixeles
            imgBuf = ImageIO.read(archivo);
            
            //Recorre imagen en alto y largo obteniendo los pixeles
            for( int x=0; x < imgBuf.getWidth(); x++ ){
                for( int y=0; y < imgBuf.getHeight(); y++ ){
                    int rgb = imgBuf.getRGB(x, y);
                    Color color = new Color(rgb, true);
                    
                    pixeles.add(new Pixel( x , y ,color.getRed(), color.getGreen(), color.getBlue()));
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return pixeles;
    }
    
    /**
     * Escribe la imagen usando el mismo nombre y agregandoles el cifrado
     * @param pixeles
     * @param archivo
     * @param cifrado
     * @return 
     */
    public boolean guardarImagen( ArrayList<Pixel> pixeles , File archivo, String cifrado){
        //Buffer para escribir imagen
        BufferedImage imgBuf = null;
        String path;
        File auxArchivo = new File("");
        path = auxArchivo.getAbsolutePath()+"//Archivos//";
               
        try {
            //Cargar la imagen en el mapa de bits 
            imgBuf = ImageIO.read(archivo);
            
            //Se recorre el arreglo con los nuevos pixeles
            for( Pixel pixel : pixeles ){
                Color color = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                
                //se modifica la imagen
                imgBuf.setRGB(pixel.getXcod(), pixel.getYcod(), color.getRGB());
                
            }
            
            //Se escribe la imagen modificada
            auxArchivo = new File(path+archivo.getName().substring(0, archivo.getName().length()-4)+cifrado+".bmp");
            ImageIO.write(imgBuf, "BMP", auxArchivo);
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
      
        return true;
    }
}
