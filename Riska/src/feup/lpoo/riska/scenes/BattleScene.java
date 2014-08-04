package feup.lpoo.riska.scenes;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import feup.lpoo.riska.utilities.Utils;

public class BattleScene extends BaseScene {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final Color COLOR_LOSE = Color.RED;
	private final Color COLOR_WIN = Color.GREEN;

	// ======================================================
	// FIELDS
	// ======================================================
	BattleGenerator battleGenerator;

	Sprite window;
	Text vsText;
	Text typePlayer1, typePlayer2;
	Text regionName1, regionName2;
	Text points1, points2;
	Text remaining1, remaining2;
	Sprite result1, result2;

	boolean attackerWon;

	// ======================================================
	// ======================================================

	@Override
	public void createScene()
	{		
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
	private void createDisplay()
	{
		setBackgroundEnabled(false);

		window = new Sprite(
				camera.getCenterX(),
				camera.getCenterY(),
				camera.getWidth(),
				camera.getHeight(),
				resources.windowRegion,
				vbom);

		window.setScale(0.9f);
		window.setAlpha(1f);

		vsText = new Text(
				0.5f * Utils.getBoundsX(window),
				0.5f * Utils.getBoundsY(window),
				resources.mGameFont, "X", vbom);

		vsText.setColor(Color.BLACK);
		vsText.setScale(1.4f);

		typePlayer1 = new Text(0,0,resources.mGameFont,"",10,resources.vbom);
		typePlayer2 = new Text(0,0,resources.mGameFont,"",10,resources.vbom);

		regionName1 = new Text(0,0,resources.mGameFont,"",1000,resources.vbom);
		regionName2 = new Text(0,0,resources.mGameFont,"",1000,resources.vbom);
		
		points1 = new Text(0,0,resources.mGameFont,"",10,resources.vbom);
		points1.setColor(Color.BLACK);
		points2 = new Text(0,0,resources.mGameFont,"",10,resources.vbom);
		points2.setColor(Color.BLACK);
		
		remaining1 = new Text(0,0,resources.mGameFont,"",100,resources.vbom);
		remaining2 = new Text(0,0,resources.mGameFont,"",100,resources.vbom);

		result1 = new Sprite(0,0,resources.regionBtnRegion, resources.vbom);
		result2 = new Sprite(0,0,resources.regionBtnRegion, resources.vbom);

		result1.setPosition(0.25f * Utils.getBoundsX(window), 0.3f * Utils.getBoundsY(window));
		result2.setPosition(0.75f * Utils.getBoundsX(window), 0.3f * Utils.getBoundsY(window));
		
		attachChild(window);
		window.attachChild(result1);
		window.attachChild(result2);
		window.attachChild(typePlayer1);
		window.attachChild(typePlayer2);
		window.attachChild(regionName1);
		window.attachChild(regionName2);
		window.attachChild(points1);
		window.attachChild(points2);
		window.attachChild(remaining1);
		window.attachChild(remaining2);
		window.attachChild(vsText);
	}

	// ======================================================
	// UPDATE DATA
	// ======================================================
	public void update(Region pRegion1, Region pRegion2, BattleGenerator battleGenerator)
	{
		float width = Utils.getBoundsX(window);
		float height = Utils.getBoundsY(window);
		
		boolean result = battleGenerator.result;

		float x1, x2;
		float y1, y2;

		Player Player1 = pRegion1.getOwner();
		Player Player2 = pRegion2.getOwner();
		
		points1.setText("" + battleGenerator.attackerPoints);
		points2.setText("" + battleGenerator.defenderPoints);

		x1 = 0.25f * width;
		x2 = 0.75f * width;

		if(Player1.isCPU && !Player2.isCPU)
		{
			x1 = 0.75f * width;
			x2 = 0.25f * width;

			result = !result;
			
			points1.setText("" + battleGenerator.defenderPoints);
			points2.setText("" + battleGenerator.attackerPoints);
		}
		else
		{
			points1.setText("" + battleGenerator.attackerPoints);
			points2.setText("" + battleGenerator.defenderPoints);
		}

		y1 = 0.8f * height;
		y2 = 0.8f * height;

		typePlayer1.setText(Player1.getName());
		typePlayer1.setPosition(x1, y1);
		typePlayer2.setText(Player2.getName());
		typePlayer2.setPosition(x2, y2);

		y1 = 0.7f * height;
		y2 = 0.7f * height;

		regionName1.setText(pRegion1.getName());
		regionName1.setPosition(x1, y1);
		regionName2.setText(pRegion2.getName());
		regionName2.setPosition(x2, y2);
		
		x1 = 0.4f * width;
		x2 = 0.6f * width;
		y1 = 0.5f * height;
		y2 = 0.5f * height;
		
		points1.setScale(2f);
		points1.setPosition(x1, y1);
		points2.setScale(2f);
		points2.setPosition(x2, y2);

		if(result)	// player 1 won
		{
			result1.setColor(COLOR_WIN);
			result2.setColor(COLOR_LOSE);
		}
		else		// player 2 won
		{
			result1.setColor(COLOR_LOSE);
			result2.setColor(COLOR_WIN);
		}
	}

}
