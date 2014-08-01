package feup.lpoo.riska.scenes;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import android.view.MotionEvent;
import feup.lpoo.riska.HUD.GameHUD.BUTTON;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class PreBattleScene extends BaseScene implements Displayable {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	final int MIN_SOLDIERS_PER_ATTACK = 2;
	
	enum ARROW { LEFT, RIGHT };
	
	// ======================================================
	// FIELDS
	// ======================================================
	Sprite window;
	Sprite soldierNumBox;
	Text soldiers;
	Text soldierNum;
	ButtonSprite arrowLeft, arrowRight;
	
	int soldierMin, soldierMax, currentSoldiers;

	@Override
	public void createScene() {
		createDisplay();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

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
				resources.mGameFont, "SOLDIERS", vbom);
		
		soldiers.setColor(Color.WHITE);
		soldiers.setScale(1.2f);
		
		soldierNum = new Text(0.5f * MainActivity.CAMERA_WIDTH, 0.5f * MainActivity.CAMERA_HEIGHT,
				resources.mGameFont, "X", vbom);
		
		soldierNum.setColor(Color.BLACK);
		soldierNum.setScale(2.5f);
		
		soldierNumBox = new Sprite(
				MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2, resources.windowButtonGeneric, vbom);
		
		soldierNumBox.setScaleX(0.3f);
		//soldierNumBox.setAlpha(1f);
		
		createArrows();
		
		attachChild(window);
		attachChild(soldiers);
		attachChild(soldierNumBox);
		attachChild(soldierNum);
		attachChild(arrowLeft);
		attachChild(arrowRight);
	}
	
	public void createArrows()
	{
		arrowLeft = new ButtonSprite(0, 0,
				resources.arrowRightRegion,
				resources.vbom) 
		{

			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					pressed(ARROW.LEFT);
					break;
				case MotionEvent.ACTION_UP:
					touched(ARROW.LEFT);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(ARROW.LEFT);
					break;
				default:
					break;
				}
				return true;
			}	
		};
		
		arrowLeft.setScaleX(-1f);
		arrowLeft.setScale(0.6f);	
		arrowLeft.setPosition(0.25f * MainActivity.CAMERA_WIDTH, 0.5f * MainActivity.CAMERA_HEIGHT);
		
		
		arrowRight = new ButtonSprite(0, 0,
				resources.arrowRightRegion,
				resources.vbom) 
		{

			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					pressed(ARROW.RIGHT);
					break;
				case MotionEvent.ACTION_UP:
					touched(ARROW.RIGHT);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(ARROW.RIGHT);
					break;
				default:
					break;
				}
				return true;
			}	
		};
		
		arrowRight.setScale(0.6f);
		arrowRight.setPosition(0.75f * MainActivity.CAMERA_WIDTH, 0.5f * MainActivity.CAMERA_HEIGHT);
		
		//registerTouchArea(arrowLeft);
		//registerTouchArea(arrowRight);
	}

	private void pressed(ARROW x)
	{
		switch(x)
		{
		case LEFT:
			arrowLeft.setCurrentTileIndex(1);
			break;
		case RIGHT:
			arrowRight.setCurrentTileIndex(1);
			break;
		default:
			break;
		}
	}
	
	private void touched(ARROW x)
	{
		Log.d("Riska", "Touched an arrow");
		switch(x)
		{
		case LEFT:
			released(x);
			decreaseSoldiers();
			break;
		case RIGHT:
			released(x);
			increaseSoldiers();
			break;
		default:
			break;
		}
	}

	private void released(ARROW x)
	{
		switch(x)
		{
		case LEFT:
			arrowLeft.setCurrentTileIndex(0);
			break;
		case RIGHT:
			arrowRight.setCurrentTileIndex(0);
			break;
		default:
			break;
		}
	}
	
	private void increaseSoldiers() {
		this.currentSoldiers++;
		this.currentSoldiers = Math.min(soldierMax, currentSoldiers);
		
		soldierNum.setText("" + currentSoldiers);
	}
	
	private void decreaseSoldiers() {
		this.currentSoldiers--;
		this.currentSoldiers = Math.max(soldierMin, currentSoldiers);
		
		soldierNum.setText("" + currentSoldiers);
	}
	
	public void update(int max)
	{
		this.soldierMin = MIN_SOLDIERS_PER_ATTACK;
		this.soldierMax = max;
		this.currentSoldiers = max;
		
		soldierNum.setText("" + currentSoldiers);
	}
	
	public int getAttackingSoldiers()
	{
		return currentSoldiers;
	}

}
