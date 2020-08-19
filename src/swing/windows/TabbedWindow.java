package swing.windows;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

import views.View;

/**
 * A tabbed window class, where one tab is equivalent to one view.
 *
 * @author Charles MECHERIKI
 *
 */
public class TabbedWindow extends Window {
	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	
	public TabbedWindow(String _title, Dimension _size) {
		super(_title, _size);
		tabbedPane = new JTabbedPane();
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	public void addTabChangeListener(ChangeListener _changeListener) {
		tabbedPane.addChangeListener(_changeListener);
	}
	
	public void integrateViews(ArrayList<View> _views) {
		for (View view : _views) {
			tabbedPane.addTab(view.getName(), view);
		}
	    tabbedPane.setTabPlacement(JTabbedPane.TOP);
	    this.getContentPane().add(tabbedPane);
	}
}
