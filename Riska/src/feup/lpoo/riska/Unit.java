package feup.lpoo.riska;

public class Unit extends Element {
	
	protected float attack;
	protected float defense;
	protected String name;

	public Unit(float x, float y) {
		super(x, y);
		this.attack = 0;
		this.defense = 0;
		this.name = null;
	}
	
	public Unit(float x, float y, String TAG) {
		super(x, y, TAG);
		this.attack = 0;
		this.defense = 0;
		this.name = null;
	}
	
	public Unit(float x, float y, float att, float def, String name) {
		super(x,y);
		this.attack = att;
		this.defense = def;
		this.name = name;
	}
	
	public Unit(float x, float y, float att, float def, String name, String TAG) {
		super(x,y, TAG);
		this.attack = att;
		this.defense = def;
		this.name = name;
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
