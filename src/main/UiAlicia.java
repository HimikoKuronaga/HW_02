
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.crypto.SecretKey;

public class UiAlicia extends JFrame{

	private static final int CIFRAR = 0;
	private static final int DECIFRAR = 1;
	private static final String[] MODOS = new String[]{"Modo de operacion","ECB", "CBC", "CFB", "OFB"};
	private static final int DEF_TAM_BLOQUE = 16;

	private JLabel lblTitulo;
	private JLabel lblNombreArchivo;
	private JButton btnSeleccionar;
	private JButton btnSeleccionarLlave;
	
	private JLabel lblLlave;
	private JLabel lblTamBloque;
	private JTextField txtLlave;
	private JTextField txtTamBloque;
	private JCheckBox cbxCifrar;
	private JCheckBox cbxDecifrar;
	private JComboBox cmbModos;
	private JButton btnGenerar;
	private JButton btnElegirLlave;
	private JButton btnOperacion;
	
	private boolean seleccionado;
	private boolean llaveSeleccionada;
	private boolean seleccionadoLlave;
	private JFileChooser fc;
	private File archivo;
	private File archivoLlave;

	private Cifrador cd;
	private String modo;
	private int operacion;
	private int tamBloque;

	public UiAlicia(){
		setTitle("Practica 3, Criptografia.");	
		setSize(620, 240);	
		setLocationRelativeTo(null);
		operacion = -1;
		seleccionado = false;
		llaveSeleccionada = false;
		cd = new Cifrador();
		tamBloque = DEF_TAM_BLOQUE;
		/*Cuando se cierra la ventana*/
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				accionSalir();
			}
		});
		init();
		setVisible(true);
	}

	void init(){

		JPanel panelSuperior = new JPanel();	
		JPanel panelArchivo = new JPanel();
		JPanel panelControles = new JPanel();
		
		lblTitulo = new JLabel("Modos de operacion");
		lblNombreArchivo = new JLabel("Selecciona un archivo", SwingConstants.CENTER);
		
		btnSeleccionar = new JButton("Seleccionar");
		btnSeleccionar.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				accionSeleccionar();
			}	
		});
		
		lblLlave = new JLabel("Nombre de llave:");
		txtLlave = new JTextField( 10 );

		btnOperacion = new JButton("Cifrar/Decifrar");
		btnOperacion.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				accionCifrar();
			}	
		});

		lblTamBloque = new JLabel("Tam de bloque:");
		txtTamBloque = new JTextField(10);

		cbxCifrar = new JCheckBox("Cifrar");
		cbxCifrar.addItemListener(new ItemListener(){
		
			public void itemStateChanged( ItemEvent e){
				if( e.getStateChange() == 1){
					operacion = CIFRAR;
					cbxDecifrar.setSelected(false);
				}
			}
		});

		cbxDecifrar = new JCheckBox("Descifrar");
		cbxDecifrar.addItemListener(new ItemListener(){
			public void itemStateChanged( ItemEvent e){
				if( e.getStateChange() == 1){
					operacion = DECIFRAR;
					cbxCifrar.setSelected(false);
				}
			}
		});
		
		cmbModos = new JComboBox( MODOS );
		cmbModos.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				modo = ( String )cmbModos.getItemAt(cmbModos.getSelectedIndex());
			}
		});

		btnGenerar = new JButton("Generar llave");
		btnGenerar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					accionGenerar();
				}
		});
		
		btnElegirLlave = new JButton("Elegir llave");
		btnElegirLlave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				accionElegirLlave();
			}
		});

		panelSuperior.add( lblTitulo);
		panelArchivo.add( lblNombreArchivo );
		panelArchivo.add( btnSeleccionar );
	
		GroupLayout gl = new GroupLayout(panelControles);
		panelControles.setLayout( gl );
		gl.setHorizontalGroup(
			gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent( lblLlave ).addGap(2)
					.addComponent( lblTamBloque ).addGap(2)
					.addComponent( cbxCifrar ).addGap(2)
					.addComponent( cmbModos ).addGap(2)
					.addComponent( btnOperacion ).addGap(2)	
				)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent( txtLlave )
					.addComponent( txtTamBloque )
					.addComponent( cbxDecifrar )
					.addComponent( btnElegirLlave )
					.addComponent( btnGenerar )
				)
		);	

		gl.setVerticalGroup(
				gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( lblLlave )
					.addComponent( txtLlave )
				)
				.addGap(5)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent( lblTamBloque )
						.addComponent( txtTamBloque )	
				)
				.addGap(5)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( cbxCifrar )
					.addGap(5)
					.addComponent( cbxDecifrar )	
				)
				.addGap(5)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( cmbModos )
					.addComponent( btnElegirLlave )
				)
				.addGap(15)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( btnOperacion )
					.addComponent( btnGenerar ) 	
				)	
		);	

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add( panelSuperior, BorderLayout.NORTH );
		getContentPane().add( panelArchivo, BorderLayout.CENTER );
		getContentPane().add( panelControles, BorderLayout.LINE_END );
	}

	public void accionSalir(){
		System.exit(0);
	}

	public void accionSeleccionar(){
		fc = new JFileChooser("./Archivos");
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			archivo = fc.getSelectedFile();
			seleccionado = true;
			lblNombreArchivo.setText(archivo.getName());
		}else{	
			seleccionado = false;
			lblNombreArchivo.setText("Selecciona un archivo");
		}
	}
	
	public void accionElegirLlave(){
		fc = new JFileChooser(".");
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			archivoLlave = fc.getSelectedFile();
			llaveSeleccionada = true;
		}else{	
			llaveSeleccionada = false;
		}
	}
	
	public void accionGenerar(){
		if( txtLlave.getText().length() == 0 ){
			JOptionPane.showMessageDialog(null, "Debes escribir el nombre de la llave", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if( cd.generarLlave( txtLlave.getText() )){
			JOptionPane.showMessageDialog(null, "Llave generada correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, "No se pudo generar la llave", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void accionCifrar(){
		if(!seleccionado){
			JOptionPane.showMessageDialog(this, "Primero debes seleccionar un archivo", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if( operacion == -1){
			JOptionPane.showMessageDialog(this, "Debes seleccionar una operacion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if( cmbModos.getSelectedIndex() == 0){
			JOptionPane.showMessageDialog(this,"Debes seleccionar un modo de operacion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if( txtTamBloque.getText().length() > 0){
			try{
				tamBloque = Integer.parseInt(txtTamBloque.getText());
				if( tamBloque <= 0 || (tamBloque % 16) != 0){
					tamBloque = DEF_TAM_BLOQUE;
				}
			}catch(NumberFormatException e) {
				System.err.println("El tamanio de bloque que ingresaste no es un numero.");
				tamBloque = DEF_TAM_BLOQUE;
			}
		}
		
		if( !llaveSeleccionada ){
			JOptionPane.showMessageDialog(null,"Primero selecciona la llave", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		SecretKey llave = cd.loadKey( archivoLlave );
		//System.out.println("K" + llave + " M " + modo + " Op " + operacion + " - " + archivo.getName());
		if( cd.cifrarDescifrarImg( llave, modo, operacion, tamBloque, archivo) ){
			JOptionPane.showMessageDialog(null,"Imagen procesada", "Ok", JOptionPane.INFORMATION_MESSAGE);
			return;
		}else{
			JOptionPane.showMessageDialog(null,"Error al procesar imagen", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public static void main(String[] args){	
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				UiAlicia alicia = new UiAlicia();
			}	
		});
	}

}
