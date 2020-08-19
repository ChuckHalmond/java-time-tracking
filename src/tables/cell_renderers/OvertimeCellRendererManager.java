package tables.cell_renderers;

/**
 * A static class used to update the incident threshold value of the overtime cell renderer from anywhere.
 *
 * @author Charles MECHERIKI
 *
 */
public class OvertimeCellRendererManager {
	private static OvertimeCellRenderer renderer = new OvertimeCellRenderer();
	
	public static OvertimeCellRenderer getRenderer() {
		return renderer;
	}
	
	public static void updateIncidentThreshold(int _incidentThreshold) {
		renderer.setIncidentThreshold(_incidentThreshold);
	}
}
