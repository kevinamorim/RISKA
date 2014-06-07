package feup.lpoo.riska.elements;

public class Unit extends Element {
	
	protected int attack;
	protected int defense;
	
	public Unit(int x, int y, int att, int def, String name) {
		super(x,y, name);
		this.attack = att;
		this.defense = def;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
