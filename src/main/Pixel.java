/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author isaia
 */
public class Pixel {
    
    private int []cod = new int[2];
    private int []rgb = new int[3];
    
    //Contructor
    public Pixel(int x, int y,int r, int g, int b){
        this.cod[0] = x;
        this.cod[1] = y;
        this.rgb[0] = r;
        this.rgb[1] = b;
        this.rgb[2] = b;
    }
    
    //Establecer x
    public void setXcod(int x){
        cod[0] = x;
    }
    
    //obtener x
    public int getXcod(){
        return cod[0];
    }
    
    //Establecer y
    public void setYcod(int y){
        cod[1] = y;
    }
    
    //obtener y
    public int getYcod(){
        return cod[1];
    }
    
    //Establecer rojo
    public void setRed(int r){
        rgb[0] = r;
    }
    
    //Obtener rojo
    public int getRed(){
        return rgb[0];
    }
    
    //Establecer verde
    public void setGreen(int g){
        rgb[1] = g;
    }
    
    //Obtener verde
    public int getGreen(){
        return rgb[1];
    }
    
    //Establecer rojo
    public void setBlue(int b){
        rgb[2] = b;
    }
    
    //Obtener azul
    public int getBlue(){
        return rgb[2];
    }
    
    //Establecer rgb
    public void setRGB( int r, int g, int b ){
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }
    
    //Obtener rgb
    public int []getRGB(){
        return rgb;
    }
    
    @Override
    public String toString(){
        return "cod["+cod[0]+","+cod[1]+"] - rgb["+rgb[0]+","+rgb[1]+","+rgb[2]+"]";
    }
}
