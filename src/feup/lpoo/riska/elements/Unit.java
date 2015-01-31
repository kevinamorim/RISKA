package feup.lpoo.riska.elements;

/**
 * Represents any unit in the game.
 */
public class Unit /*extends Element */{
	
	protected int attack;
	protected int defense;
	
	protected int experience;
	
	/**
	 * Constructor for a unit.
	 * <p>
	 * A unit has and attack and defense parameters.
	 * Each time an unit attacks or defends, the strike can vary between [0, value].
	 * 
	 * @param att : max attack for the unit
	 * @param def : max defense for the unit
	 */
	public Unit(int att, int def) {
		//super(0, 0, "");
		this.attack = att;
		this.defense = def;
	}

	/**
	 * @return The attack points of this unit affected by its combat experience
	 */
	public int getAttack() {
		return attack + experience;
	}

	/**
	 * @return The defense points of this unit affected by its combat experience
	 */
	public int getDefense() {
		return defense + experience;
	}

	/**
	 * Sets the attack points for a unit.
	 * 
	 * @param pValue : value to set
	 */
	public void setAttack(int pValue) {
		attack = pValue;
	}

	/**
	 * Sets the defense points for a unit.
	 * 
	 * @param pValue : value to set
	 */
	public void setDefense(int pValue) {
		defense = pValue;
	}
	
	/**
	 * Increases the experience of this unit by the given value.
	 * 
	 * @param pValue : value to add
	 */
	public void increaseExperience(int pValue) {
		experience += pValue;
	}
	
	/**
	 * Sets the value of this unit's experience.
	 * 
	 * @param pValue : value to set
	 */
	public void setExperience(int pValue) {
		experience = pValue;
	}

}
