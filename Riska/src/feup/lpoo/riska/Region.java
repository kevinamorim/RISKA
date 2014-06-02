package feup.lpoo.riska;

import android.graphics.Point;

public class Region {

	protected Point stratCenter;

	protected String name;
	protected String continent;

	protected boolean selected;

	protected boolean owned;
	
	public Region(String name, Point stratCenter, String continent) {
		
		this.name = name;
		this.stratCenter = stratCenter;
		this.continent = continent;
		
	}
	
	
	
	/* ======================================================================
	 * ======================================================================    
	 *                          GETTERS & SETTERS 
	 * ======================================================================    
	 * ======================================================================    
	 */


	/**
	 * @return the stratCenter
	 */
	public Point getStratCenter() {
		return stratCenter;
	}

	/**
	 * @param stratCenter the stratCenter to set
	 */
	public void setStratCenter(Point stratCenter) {
		this.stratCenter = stratCenter;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the owned
	 */
	public boolean isOwned() {
		return owned;
	}

	/**
	 * @param owned the owned to set
	 */
	public void setOwned(boolean owned) {
		this.owned = owned;
	}



}
