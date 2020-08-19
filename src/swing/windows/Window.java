package swing.windows;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A simple window class, extending a JFrame.
 *
 * @author Charles MECHERIKI
 *
 */
public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Dimension size; 
	private String title;
	
	public Window(String _title, Dimension _size) {
		title = _title;
		size = _size;
	}
	
	public void setWindowPreExitAction(Runnable _runnable) {
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		    	_runnable.run();
		    	System.exit(0);
		    }
		});
	}
	
	public void display() {
		setSize(size);
		setTitle(title);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
