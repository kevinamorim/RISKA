package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.utilities.Utils;

public class DeployScene extends BaseScene implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================
	final int MIN_SOLDIERS_TO_MOVE = 1;

	enum ARROW { LEFT, RIGHT };

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	
	Sprite window;
	Sprite soldierNumBox;
	Text soldiersToSend;
	Text soldierNum;

	private int soldierMin;
	private int soldierMax;
	private int currentSoldiers;

	// ======================================================
	// ======================================================

	public DeployScene(GameLogic logic)
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
		return SCENE_TYPE.PRE_MOVE;
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
				resources.mGameFont, "SOLDIERS TO MOVE", 100, vbom);

		soldiersToSend.setColor(Color.WHITE);
		soldiersToSend.setScale(1.2f);

		soldierNum = new Text(
				0.5f * Utils.getRightBoundsX(window),
				0.5f * Utils.getUpperBoundsY(window),
				resources.mGameFont, "X", 500, vbom);

		soldierNum.setColor(Color.BLACK);
		soldierNum.setScale(1.4f);
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

	public void update(int numSoldiers)
	{
		this.soldierMin = MIN_SOLDIERS_TO_MOVE;
		this.soldierMax = numSoldiers;
		
		this.currentSoldiers = MIN_SOLDIERS_TO_MOVE;

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

	@Override
	public void onMenuKeyPressed() {
		// TODO Auto-generated method stub
		
	}
	
}
