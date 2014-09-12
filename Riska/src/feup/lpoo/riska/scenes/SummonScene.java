package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.gameInterface.RiskaCanvas;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.utilities.Utils;

public class SummonScene extends BaseScene implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================
	final int MIN_SOLDIERS_PER_ATTACK = 1;

	enum ARROW { LEFT, RIGHT };

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	
	RiskaCanvas windowCanvas;
	
	Sprite window;
	Sprite soldierNumBox;
	Text soldiersToSend;
	Text soldierNum;
	Text successNum;

	private int soldierMin;
	private int soldierMax;
	private int currentSoldiers = 1;
	private int defendingSoldiers = 1;
	
	// ======================================================
	// ======================================================

	public SummonScene(GameLogic logic)
	{
		super();
		this.logic = logic;
	}

	@Override
	public void createScene()
	{
		createDisplay();
	}

	@Override
	public void onBackKeyPressed()
	{
		((GameScene)getParent()).onBackKeyPressed();
	}

	@Override
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.PRE_BATTLE;
	}

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
		window.attachChild(successNum);
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
				0.5f * Utils.getRightBoundsX(window),
				0.5f * Utils.getUpperBoundsY(window),
				resources.windowRegionGenericInverted,
				vbom);

		soldierNumBox.setScaleY(0.5f);
	}
	
	private void createText()
	{
		soldiersToSend = new Text(
				0.5f * Utils.getRightBoundsX(window),
				0.8f * Utils.getUpperBoundsY(window),
				resources.mGameFont, "SOLDIERS TO SEND ON ATTACK", 100, vbom);

		soldiersToSend.setColor(Color.WHITE);
		soldiersToSend.setScale(1.2f);

		soldierNum = new Text(
				0.5f * Utils.getRightBoundsX(window),
				0.5f * Utils.getUpperBoundsY(window),
				resources.mGameFont, "X", 500, vbom);

		soldierNum.setColor(Color.BLACK);
		soldierNum.setScale(1.4f);
		
		successNum = new Text(
				0.5f * Utils.getRightBoundsX(window),
				0.20f * Utils.getUpperBoundsY(window),
				resources.mGameFont, "INSERT", 500, vbom);

		successNum.setColor(Color.BLACK);
		successNum.setScale(1f);
	}

	// ======================================================
	// UPDATE DATA
	// ======================================================
	public void increaseSoldiers()
	{
		this.currentSoldiers++;
		this.currentSoldiers = Math.min(soldierMax, currentSoldiers);

		updateText();
	}
	
	public void decreaseSoldiers()
	{
		this.currentSoldiers--;
		this.currentSoldiers = Math.max(soldierMin, currentSoldiers);

		updateText();
	}

	public void update(int numAttackers, int numDefenders)
	{
		this.soldierMin = MIN_SOLDIERS_PER_ATTACK;
		this.soldierMax = numAttackers;
		
		this.currentSoldiers = numAttackers;
		this.defendingSoldiers = numDefenders;

		updateText();
	}

	private void updateText()
	{
		//int successChance = (int)(Utils.calculateChanceOfSuccess(currentSoldiers, defendingSoldiers) * 100);
		
		soldierNum.setText(currentSoldiers + " (" + soldierMax + " max)");
		successNum.setText("Chance of success: " + 0 + " %");
	}

	public int getAttackingSoldiers()
	{
		return currentSoldiers;
	}

	@Override
	public void setAlpha(float pAlpha)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);
			
			e.setAlpha(pAlpha);
		}
		
		super.setAlpha(pAlpha);
	}
	
	public void show()
	{
		// TODO
	}
	
}
