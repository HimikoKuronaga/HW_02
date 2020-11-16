
//package main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

public class Cifrador {
    
    //Constructor
    public Cifrador(){};
      
    /**
     * Cifra la imagen ingresada con el modo ingresado
     * @param llave : Llave de cifrado
     * @param modo  : CBC , CFB , CTR , CTS , ECB , OFB , PCBC
     * @param op    : 0 cifrar 1 descifrar
     * @param archivo : La imagen a cifrar
     * @return true si el archivo se cifro con exito false si no
     */
    public boolean cifrarDescifrarImg( SecretKey llave, String modo, int op,  File archivo ){
        
        try {
            //Flujo de datos de entrada
            FileInputStream fis = new FileInputStream(archivo);
            //Flujo de datos para almacenar los bytes
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            long tamImg = archivo.length();
            int l=0;
            long leido=0; 
            
            //Se lee la imagen
            while( leido != tamImg ){
                byte [] b = new byte[65535];
                l = fis.read(b);
                bos.write(b, 0, l);
                leido = leido + l;
            }
                  
            //Imagen en bytes
            byte[] bytes = bos.toByteArray();
           
            //byte[] bytes = new byte[tamImg];
            //fis.read( bytes );
            
            //Separar la cabecera y la parte de datos
            byte[] cabecera = new byte[54];
            byte[] data = new byte[bytes.length-54];
            
            System.arraycopy(bytes, 0, cabecera, 0, cabecera.length);
            System.arraycopy(bytes, cabecera.length, data, 0, data.length);
       
            //Cifrar o Descifrar archivo
            Cipher cifrador = Cipher.getInstance("AES/"+modo+"/NoPadding");
            
            if( op == 0 )
                cifrador.init(Cipher.ENCRYPT_MODE, llave);
            else{
                cifrador.init(Cipher.DECRYPT_MODE, llave);
			}

            
            //Se obtienen los datos cifrados o descifrados
            byte []dataC = cifrador.doFinal(data);
           // System.out.println(Arrays.toString(dataC));
            
            //Se recontruye la imagen bmp con la cabecera y la parte de datos
            byte res[] = new byte[bytes.length];
            System.arraycopy(cabecera, 0, res, 0, cabecera.length);
            System.arraycopy(dataC, 0, res, cabecera.length, dataC.length);
            
            //Se guarda la imagen
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(res)); 
            ImageIO.write(image, "BMP", new File(op+archivo.getName().substring(0, archivo.getName().length()-4)+modo+".bmp"));     
            
			return true;
		} catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /*
     * Genera una llave AES de 128 bits
     * @param nombreLlave : Nombre para el archivo de la llave .key
     * @return true cuando la llave se guarda con exito false cuando no
     */
     
    public boolean generarLlave(String nombreLlave){
        //Generar una llave de 128 bits
            try {
                //Generador para llave AES
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(128);
                SecretKey key = keyGenerator.generateKey();
                saveKey(key, nombreLlave+".key");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
    }
    
    /**
     * Se guarda la llave 
     * @param key
     * @param fileName
     * @return
     * @throws Exception 
     */
    public boolean saveKey(SecretKey key, String fileName) throws Exception {
        byte[] secretKeyBytes = key.getEncoded();
        SecretKeySpec skey = new SecretKeySpec(secretKeyBytes, "AES");
        byte[] encoded = skey.getEncoded();
        try {
            File keyfile = new File(fileName);
            FileOutputStream fos = new FileOutputStream(keyfile);
            fos.write(encoded);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
     }
    
    /**
     * Se carga la llave
     * @param fileName
     * @return 
     */
    public SecretKey loadKey( /*String filename*/ File f){
        SecretKeySpec keyspec = null;       
        try {
            //File keyfile = new File(fileName+".key");
            DataInputStream input = new DataInputStream(new FileInputStream( f ));
            byte[] rawkey = new byte[ (int) /*keyfile*/f.length()];
            input.readFully(rawkey);
            input.close();
            keyspec = new SecretKeySpec(rawkey, "AES");
        } catch (Exception e) {
        	e.printStackTrace();
		}
        return keyspec;
     }
}
