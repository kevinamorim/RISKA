package feup.lpoo.riska.scenes;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import feup.lpoo.riska.utilities.Utilities;

public class PreBattleScene extends BaseScene implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================
	final int MIN_SOLDIERS_PER_ATTACK = 1;

	enum ARROW { LEFT, RIGHT };

	// ======================================================
	// FIELDS
	// ======================================================
	Sprite window;
	Sprite soldierNumBox;
	Text soldiersToSend;
	Text soldierNum;

	int soldierMin;
	int soldierMax;
	int currentSoldiers;

	// ======================================================
	// ======================================================

	@Override
	public void createScene() {
		createDisplay();
	}

	@Override
	public void onBackKeyPressed() { }

	@Override
	public SceneType getSceneType() { return null; }

	@Override
	public void disposeScene() { }

	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	@Override
	public void createDisplay()
	{
		setBackgroundEnabled(false);
		
		createBackground();	
		createText();

		attachChild(window);
		window.attachChild(soldiersToSend);
		window.attachChild(soldierNumBox);
		window.attachChild(soldierNum);
	}

	private void createBackground()
	{
		window = new Sprite(
				camera.getCenterX(),
				camera.getCenterY(),
				camera.getWidth(),
				camera.getHeight(),
				resources.windowRegionGeneric,
				vbom);

		window.setScale(0.9f);
		//window.setAlpha(1f);
		
		
		soldierNumBox = new Sprite(
				0.5f * Utilities.getBoundsX(window),
				0.5f * Utilities.getBoundsY(window),
				resources.windowButtonGenericInverted,
				vbom);

		soldierNumBox.setScaleX(1.1f);
	}
	
	private void createText()
	{
		soldiersToSend = new Text(
				0.5f * Utilities.getBoundsX(window),
				0.8f * Utilities.getBoundsY(window),
				resources.mGameFont, "SOLDIERS TO SEND ON ATTACK", 100, vbom);

		soldiersToSend.setColor(Color.WHITE);
		soldiersToSend.setScale(1.2f);

		soldierNum = new Text(
				0.5f * Utilities.getBoundsX(window),
				0.5f * Utilities.getBoundsY(window),
				resources.mGameFont, "X", 500, vbom);

		soldierNum.setColor(Color.BLACK);
		soldierNum.setScale(1.4f);
	}

	// ======================================================
	// UPDATE DATA
	// ======================================================
	public void increaseSoldiers() {
		this.currentSoldiers++;
		this.currentSoldiers = Math.min(soldierMax, currentSoldiers);

		updateText();
	}

	public void decreaseSoldiers() {
		this.currentSoldiers--;
		this.currentSoldiers = Math.max(soldierMin, currentSoldiers);

		updateText();
	}

	public void update(int max)
	{
		this.soldierMin = MIN_SOLDIERS_PER_ATTACK;
		this.soldierMax = max;
		this.currentSoldiers = max;

		updateText();
	}

	private void updateText()
	{
		soldierNum.setText(currentSoldiers + " (" + soldierMax + " max)");
	}

	public int getAttackingSoldiers()
	{
		return currentSoldiers;
	}
}
