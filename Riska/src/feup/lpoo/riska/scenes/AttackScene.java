package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.gameInterface.RiskaCanvas;
import feup.lpoo.riska.gameInterface.RiskaSprite;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;

public class AttackScene extends BaseScene {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final Color COLOR_LOSE = Color.RED;
	private final Color COLOR_WIN = Color.GREEN;

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	
	private RiskaCanvas windowCanvas;
	
	private RiskaCanvas attackerCanvas;
	private RiskaSprite attackerPlayerColor;
	private Text attackerRegionName;
	private RiskaCanvas attackerRegionCanvas1;
	private RiskaCanvas attackerRegionCanvas2;
	private RiskaSprite[] attackerBox1;
	private RiskaSprite[] attackerBox2;
	
	private RiskaCanvas defenderCanvas;
	private RiskaSprite defenderPlayerColor;
	private Text defenderRegionName;
	private RiskaCanvas defenderRegionCanvas1;
	private RiskaCanvas defenderRegionCanvas2;

	boolean attackerWon;
	
	private Region attacker, defender;

	// ======================================================
	// ======================================================

	public AttackScene(GameLogic logic)
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
		return SCENE_TYPE.BATTLE;
	}

	@Override
	public void disposeScene()
	{
		dispose();
	}

	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	private void createDisplay()
	{
		setBackgroundEnabled(false);
		
		windowCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.95f * camera.getWidth(), 0.9f * camera.getHeight());
		windowCanvas.setCanvasSprite(new RiskaSprite(resources.windowRegion, vbom));
		
		createAttackerCanvas();
		createDefenderCanvas();
		
		attachChild(windowCanvas);
	}

	private void createAttackerCanvas()
	{
		attackerBox1 = new RiskaSprite[GameInfo.maxGarrison];
		attackerBox2 = new RiskaSprite[GameInfo.maxGarrison];
		
		attackerCanvas = new RiskaCanvas(0f, 0f, windowCanvas.getWidth(), windowCanvas.getHeight());
		//attackerCanvas.setCanvasSprite(new RiskaSprite(resources.barFillRegion, vbom));
		
		windowCanvas.addGraphic(attackerCanvas, 0.25f, 0.5f, 0.5f, 1f);
		
		attackerPlayerColor = new RiskaSprite(resources.buttonRegion, vbom, "PLAYER", resources.mGameFont);
		attackerPlayerColor.setColor(Color.RED);
		
		attackerRegionName = new Text(0f, 0f, resources.mGameFont, "REGION NAME", 100, vbom);
		attackerRegionName.setColor(Color.RED);
		
		attackerCanvas.addGraphicWrap(attackerPlayerColor, 0.2f, 0.8f, 0.2f, 0.2f);
		attackerCanvas.addText(attackerRegionName, 0.6f, 0.8f, 0.5f, 0.2f);
		
		attackerRegionCanvas1 = new RiskaCanvas(0f, 0f, 0.6f * attackerCanvas.getWidth(), 0.1f * attackerCanvas.getHeight());
		attackerRegionCanvas2 = new RiskaCanvas(0f, 0f, 0.6f * attackerCanvas.getWidth(), 0.1f * attackerCanvas.getHeight());
		
		//attackerRegionCanvas1.setCanvasSprite(new RiskaSprite(resources.barFillRegion, vbom));
		//attackerRegionCanvas2.setCanvasSprite(new RiskaSprite(resources.barFillRegion, vbom));
		
		float factor = 1f / (GameInfo.maxGarrison + 1);
		for(int i = 0; i < GameInfo.maxGarrison; i++)
		{
			float offsetX = (i+1) * factor;
			
			attackerBox1[i] = new RiskaSprite(resources.fillSquareRegion, vbom);
			attackerRegionCanvas1.addGraphic(attackerBox1[i], offsetX, 0.5f, 0.9f * factor, 0.9f);
			
			attackerBox2[i] = new RiskaSprite(resources.fillSquareRegion, vbom);
			attackerRegionCanvas2.addGraphic(attackerBox2[i], offsetX, 0.5f, 0.9f * factor, 0.9f);
		}
		
		attackerCanvas.addGraphicWrap(attackerRegionCanvas1, 0.5f, 5/8f, 0.9f, 0.125f);
		attackerCanvas.addGraphicWrap(attackerRegionCanvas2, 0.5f, 3/8f, 0.9f, 0.125f);
	}

	private void createDefenderCanvas()
	{
		// TODO Auto-generated method stub
		
	}

	// ======================================================
	// UPDATE DATA
	// ======================================================
	public void update(Region attacker, Region defender)
	{
		
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
