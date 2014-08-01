package feup.lpoo.riska.scenes;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

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
	Text soldiers;
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
	public void createDisplay() {

		setBackgroundEnabled(false);

		window = new Sprite(
				MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2,
				MainActivity.CAMERA_WIDTH,
				MainActivity.CAMERA_HEIGHT,
				resources.windowRegionGeneric,
				vbom);

		window.setScale(0.9f);
		window.setAlpha(1f);

		soldiers = new Text(0.5f * MainActivity.CAMERA_WIDTH, 0.8f * MainActivity.CAMERA_HEIGHT,
				resources.mGameFont, "SOLDIERS", 100, vbom);

		soldiers.setColor(Color.WHITE);
		soldiers.setScale(1.2f);

		soldierNum = new Text(0.5f * MainActivity.CAMERA_WIDTH, 0.5f * MainActivity.CAMERA_HEIGHT,
				resources.mGameFont, "X", 500, vbom);

		soldierNum.setColor(Color.BLACK);
		soldierNum.setScale(1.4f);

		soldierNumBox = new Sprite(
				MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2, resources.windowButtonGenericInverted, vbom);

		soldierNumBox.setScaleX(1.1f);
		//soldierNumBox.setAlpha(1f);

		attachChild(window);
		attachChild(soldiers);
		attachChild(soldierNumBox);
		attachChild(soldierNum);
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
