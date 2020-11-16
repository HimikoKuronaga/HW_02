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
	
	private JLabel lblLlave;
	private JTextField txtLlave;
	private JCheckBox cbxCifrar;
	private JCheckBox cbxDecifrar;
	private JComboBox cmbModos;
	private JButton btnOperacion;
	
	private boolean seleccionado;
	private JFileChooser fc;
	private File archivo;

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
		
		lblLlave = new JLabel("Llave: ");
		txtLlave = new JTextField(10);
		
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
					.addComponent( txtLlave )
					.addComponent( cbxDecifrar )
				)	
		);	

		gl.setVerticalGroup(
				gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( lblLlave )
					.addComponent( txtLlave )	
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
			JOptionPane.showMessageDialog(null, "Primero debes seleccionar un archivo", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if( operacion == -1){
			JOptionPane.showMessageDialog(null, "Debes seleccionar una operacion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if( cmbModos.getSelectedIndex() == 0){
			JOptionPane.showMessageDialog(null,"Debes seleccionar un modo de operacion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if( txtLlave.getText().length() == 0){
			JOptionPane.showMessageDialog(null,"Debes ingresar una llave", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if( !cd.generarLlave( txtLlave.getText() ) ){
			JOptionPane.showMessageDialog(null,"Error al generar llave", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		SecretKey llave = cd.loadKey( txtLlave.getText() );
		if( !cd.cifrarDescifrarImg( llave, modo, operacion, archivo) ){
			JOptionPane.showMessageDialog(null,"Error al cifrar/descifrar imagen", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(null,"Imagen procesada", "Ok", JOptionPane.OK_OPTION);
			return;
	}

	public void accionSeleccionar(){
		fc = new JFileChooser("./../../Archivos");
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			archivo = fc.getSelectedFile();
			seleccionado = true;
			lblNombreArchivo.setText(archivo.getName());
		}else{	
			seleccionado = false;
			lblNombreArchivo.setText("Selecciona un archivo");
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
