
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.crypto.SecretKey;

public class UiAlicia extends JFrame{

	private static final int CIFRAR = 0;
	private static final int DECIFRAR = 1;
	private static final String[] MODOS = new String[]{"Modo de operacion","CBC", "CFB", "CTR", "CTS", "ECB", "OFB", "PCBC"};
	
	private JLabel lblTitulo;
	private JLabel lblNombreArchivo;
	private JButton btnSeleccionar;

	private JButton btnSeleccionarLlave;
	
	private JLabel lblLlave;
	private JLabel lblNomLlave;
	private JTextField txtLlave;
	private JCheckBox cbxCifrar;
	private JCheckBox cbxDecifrar;
	private JComboBox cmbModos;
	private JButton btnOperacion;
	
	private boolean seleccionado;
	private boolean seleccionadoLlave;
	private JFileChooser fc;
	private File archivo;
	private File archivoLlave;

	private Cifrador cd;
	private String modo;
	private int operacion;
	
	public UiAlicia(){
		setTitle("Alicia");	
		setSize(620, 240);	
		setVisible(true);
		operacion = -1;
		seleccionado = false;
		cd = new Cifrador();
		/*Cuando se cierra la ventana*/
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				accionSalir();
			}
		});
		init();
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
		
		btnOperacion = new JButton("Cifrar/Decifrar");
		btnOperacion.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				accionCifrar();
			}	
		});
		
		lblLlave = new JLabel("Seleccionar llave: ");
		lblNomLlave = new JLabel("");
		//txtLlave = new JTextField(10);

		btnSeleccionarLlave = new JButton("Selecionar Llave ");
		btnSeleccionarLlave.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				accionSeleccionarLlave();
			}	
		});
		
		cbxCifrar = new JCheckBox("Cifrar");
		cbxCifrar.addItemListener(new ItemListener(){
		
			public void itemStateChanged( ItemEvent e){
				if( e.getStateChange() == 1){
					operacion = CIFRAR;
					cbxDecifrar.setSelected(false);
				}
			}
		});

		cbxDecifrar = new JCheckBox("Decifrar");
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
		
		
		
		panelSuperior.add( lblTitulo);
		panelArchivo.add( lblNombreArchivo );
		panelArchivo.add( btnSeleccionar );
		panelArchivo.add(btnSeleccionarLlave);
	
		GroupLayout gl = new GroupLayout(panelControles);
		panelControles.setLayout( gl );
		gl.setHorizontalGroup(
			gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent( lblLlave )
					.addComponent( cbxCifrar )
					.addComponent( cmbModos )
					.addComponent( btnOperacion )	
				)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addComponent( lblNomLlave )
					.addComponent( cbxDecifrar )
				)	
		);	

		gl.setVerticalGroup(
				gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( lblLlave )
					.addComponent( lblNomLlave )	
				)
				.addGap(3)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( cbxCifrar )
					.addComponent( cbxDecifrar )	
				)
				.addGap(3)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( cmbModos )
				)
				.addGap(10)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( btnOperacion )
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
		
		if( !seleccionadoLlave ){
			JOptionPane.showMessageDialog(this,"Debes ingresar una llave", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		SecretKey llave = cd.loadKey( archivoLlave );
		if( !cd.cifrarDescifrarImg( llave, modo, operacion, archivo) ){
			JOptionPane.showMessageDialog(this,"Error al cifrar/descifrar imagen", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this,"Imagen procesada", "Ok", JOptionPane.INFORMATION_MESSAGE);
			return;
	}

	public void accionSeleccionar(){
		fc = new JFileChooser("./Archivos");
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			archivo = fc.getSelectedFile();
			seleccionado = true;
			lblNombreArchivo.setText(archivo.getName());
		}else{	
			seleccionado = false;
			lblNombreArchivo.setText("Selecciona un archivo");
		}
	}

	public void accionSeleccionarLlave(){
		fc = new JFileChooser("./");
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			archivoLlave = fc.getSelectedFile();
			seleccionadoLlave = true;
			lblNomLlave.setText(archivoLlave.getName());
		}else{	
			seleccionadoLlave = false;
			lblNomLlave.setText("Selecciona un archivo");
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
