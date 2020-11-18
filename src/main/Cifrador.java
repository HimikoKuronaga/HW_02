
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
import javax.crypto.spec.IvParameterSpec;
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
    public boolean cifrarDescifrarImg( SecretKey llave, String modo, int op, int tamBloque, File archivo ){
        
        try {
            //Ya que los modos CFB y OFB necesaitan el vector de inicializacion se usa el siguiente vector
            byte[] iv = new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF }; 
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

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
            
            fis.close();
            bos.close();
            //Imagen en bytes
            byte[] bytes = bos.toByteArray();
           
            //Separar la cabecera y la parte de datos
            byte[] cabecera = new byte[54];
            byte[] data = new byte[bytes.length-54];
            
            //Cabecera
            System.arraycopy(bytes, 0, cabecera, 0, cabecera.length);
            //Datos
            System.arraycopy(bytes, cabecera.length, data, 0, data.length);
       
            //Cifrar o Descifrar archivo
            Cipher cifrador = Cipher.getInstance("AES/"+modo+"/NoPadding");
            
            if( op == 0 )
                if( modo.equals("CBC") || modo.equals("CFB") || modo.equals("OFB") )
                    cifrador.init(Cipher.ENCRYPT_MODE, llave, ivSpec);
                else
                    cifrador.init(Cipher.ENCRYPT_MODE, llave );
            else{
				if( modo.equals("CBC") || modo.equals("CFB") || modo.equals("OFB") )
	                cifrador.init(Cipher.DECRYPT_MODE, llave, ivSpec);
				else
					cifrador.init(Cipher.DECRYPT_MODE, llave);
            }
            
            bos = new ByteArrayOutputStream();
            int noBloques = data.length/tamBloque;
            
            int i=0;
            int aux=0;

            //Inicia el cifrado por bloques

            for(i=0; i<noBloques; i++){
                //Bloque con los datos a cifrar
                byte []bloque = new byte[tamBloque];

                System.arraycopy(data, aux, bloque, 0, bloque.length);
                //Bloque con los datos cifrados
                byte []bloquec = cifrador.doFinal(bloque);

                bos.write(bloquec, 0, bloquec.length);
                aux = aux+tamBloque;
            }

            //Se obtienen los datos cifrados o descifrados
            byte []dataC = bos.toByteArray();
                    
            //Se recontruye la imagen bmp con la cabecera y la parte de datos
            byte res[] = new byte[bytes.length];
            System.arraycopy(cabecera, 0, res, 0, cabecera.length);
            System.arraycopy(dataC, 0, res, cabecera.length, dataC.length);
            
            //Se guarda la imagen
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(res)); 
           // ImageIO.write(image, "BMP", new File(op+archivo.getName().substring(0, archivo.getName().length()-4)+modo+".bmp"));     
            if( op == 0 )
            {
                String nomImg = archivo.getName().substring(0,archivo.getName().length()-4);
                ImageIO.write(image, "BMP", new File("./Archivos/"+nomImg+"_"+modo+".bmp")); 
            }
                
            else
            {

                ImageIO.write(image, "BMP", new File("./Archivos/"+archivo.getName()+"-descifrado.bmp")); 
            }
            
          
			return true;
		}catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param tamBloque tamaño del bloque
     * @param data datos para ser cifrados
     * @param key  llave para cifrar los datos
     */
    public byte[] ecDatos( int tamBloque, byte [] data , SecretKey key , String modo){
        
        byte datac[] = null;

        try {
            Cipher cifrador = Cipher.getInstance("AES/CBC/NoPadding");
            cifrador.init(Cipher.ENCRYPT_MODE, key);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int noBloques = data.length/tamBloque;
            int i=0;
            int aux=0;

            for(i=0; i<noBloques; i++){
                //Bloque con los datos a cifrar
                byte []bloque = new byte[tamBloque];

                System.arraycopy(data, aux, bloque, 0, bloque.length);
                //Bloque con los datos cifrados
                byte []bloquec = cifrador.doFinal(bloque);

                bos.write(bloquec, 0, bloquec.length);
                aux = aux+tamBloque;
            }

            datac = bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        

        return datac;
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
    public SecretKey loadKey(File keyfile){
        SecretKeySpec keyspec = null;
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(keyfile));
            byte[] rawkey = new byte[ (int) keyfile.length()];

            input.readFully(rawkey);
            input.close();
            keyspec = new SecretKeySpec(rawkey, "AES");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return keyspec;
     }
}
