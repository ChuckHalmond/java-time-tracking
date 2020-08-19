package swing;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * A simple placeholded text field.  
 *
 * @author Charles MECHERIKI
 *
 */
public class PlaceholdedTextField extends JTextField {
	private static final long serialVersionUID = 1L;

	private String placeholder;
	
	public PlaceholdedTextField(String _placeholder) {
		setPlaceholder(_placeholder);
	}
	
	public void reset() {
		setText(" " + placeholder);
		setForeground(Color.GRAY);
	}
	
	private boolean isEmpty() {
		return super.getText().equals(" " + placeholder) || super.getText().equals("");
	}
	
	public void setPlaceholder(String _placeholder) {
		placeholder = _placeholder;
		setText(" " + _placeholder);
		setForeground(Color.GRAY);
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent _event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent _event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent _event) {
        		if (isEmpty()) {
        			setText("");
        			setForeground(Color.BLACK);
        		}
			}
		});

        addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent _caretEvent) {
        		if (isEmpty()) {
        			if (getCaretPosition() !=  0) {
        				setCaretPosition(0);
        			}
        		}
			}
        });
        
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent _caretEvent) {
        		if (isEmpty()) {
        			setText("");
        			setForeground(Color.BLACK);
        		}
            }

            @Override
            public void focusLost(FocusEvent _caretEvent) {
        		if (getText().length() == 0) {
        			reset();
        		}
            }
        });
	}

	@Override
	public String getText() {
		return (isEmpty()) ? "" : super.getText();
	}
	
	@Override
	public void setText(String _text) {
		if (_text.equals(null)) {
			super.setText(" " + placeholder);
			setForeground(Color.GRAY);
		}
		else {
			super.setText(_text);
			setForeground(Color.BLACK);
		}
	}
}
