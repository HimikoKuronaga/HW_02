import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class UiAlicia extends JFrame{

	private JLabel lblTitulo;
	private JLabel lblNombreArchivo;
	private JButton btnOperacion;
	private JButton btnSeleccionar;
	private JCheckBox cbxCifrar;
	private JCheckBox cbxDecifrar;

	private boolean seleccionado;
	private boolean opCifrar;
	private boolean opDecifrar;
	private JFileChooser fc;
	private File archivo;

	public UiAlicia(){
		setTitle("Alicia");	
		setSize(520, 240);	
		setVisible(true);
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

		cbxCifrar = new JCheckBox("Cifrar");
		cbxCifrar.addItemListener(new ItemListener(){
				public void itemStateChanged( ItemEvent e){
					if( e.getStateChange() == 1){
						opCifrar = true;
						opDecifrar = false;
						cbxDecifrar.setSelected(false);
					}
				}
		});

		cbxDecifrar = new JCheckBox("Decifrar");
		cbxDecifrar.addItemListener(new ItemListener(){
				public void itemStateChanged( ItemEvent e){
					if( e.getStateChange() == 1){
						opCifrar = false;
						opDecifrar = true;
						cbxCifrar.setSelected(false);
					}
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
					.addComponent( cbxCifrar )
					.addComponent( btnOperacion )	
				)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addComponent( cbxDecifrar )
				)	
		);	

		gl.setVerticalGroup(
				gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addGap(5)
					.addComponent( cbxCifrar )
					.addComponent( cbxDecifrar )	
				)
				.addGap(5)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent( btnOperacion )
				)	
		);	

	//	panelControles.add(cbxCifrar);
	//	panelControles.add(cbxDecifrar);
	//	panelControles.add(btnCifrar);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add( panelSuperior, BorderLayout.NORTH );
		getContentPane().add( panelArchivo, BorderLayout.CENTER );
		getContentPane().add( panelControles, BorderLayout.LINE_END );
	}

	public void accionSalir(){
		System.exit(0);
	}

	public void accionCifrar(){
	
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
