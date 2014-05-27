package feup.lpoo.riska;

public class Unit extends Element {
	
	protected float attack;
	protected float defense;
	
	public Unit(float x, float y, float att, float def, String name) {
		super(x,y, name);
		this.attack = att;
		this.defense = def;
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public float getDefense() {
		return defense;
	}

	public void setDefense(float defense) {
		this.defense = defense;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
