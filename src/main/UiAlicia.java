import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class UiAlicia extends JFrame{

	private JLabel nombreArchivo;
	private JButton btnCifrar;
	private JButton btnSeleccionar;

	private boolean seleccionado;
	private JFileChooser fc;

	private File archivo;

	public UiAlicia(){
		setTitle("Alicia");	
		setSize(480, 240);	
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

		JPanel panel = new JPanel();	
		
		nombreArchivo = new JLabel("No has seleccionado nada");
		
		btnCifrar = new JButton("Cifrar");
		btnCifrar.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				accionCifrar();
			}	
		});

		btnSeleccionar = new JButton("Seleccionar");
		btnSeleccionar.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				accionSeleccionar();
			}	
		});

		panel.add( nombreArchivo );
		panel.add( btnCifrar );
		panel.add( btnSeleccionar );

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add( panel, BorderLayout.CENTER );
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
			nombreArchivo.setText(archivo.getName());
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
