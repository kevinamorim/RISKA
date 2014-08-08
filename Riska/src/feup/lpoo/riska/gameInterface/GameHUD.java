package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.R;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.utilities.Utils;
import android.view.MotionEvent;

public class GameHUD extends HUD implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================

	public enum BUTTON { ATTACK, DETAILS, AUTO_DEPLOY, ARROW_LEFT, ARROW_RIGHT };

	public enum SPRITE { INFO_TAB };

	// ======================================================
	// SINGLETONS
	// ======================================================
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private CameraManager camera;
	private ButtonSprite attackButton;
	private ButtonSprite detailsButton;
	private ButtonSprite autoDeployButton;
	private ButtonSprite arrowLeft;
	private ButtonSprite arrowRight;
	private Sprite infoTab;
	private Text infoTabText;

	private GameScene gameScene;

	// ======================================================
	// ======================================================

	public GameHUD(GameScene scene)
	{
		gameScene = scene;

		resources = ResourceCache.getSharedInstance();
		
		camera = resources.camera;

		createDisplay();
	}

	public void draw(GameLogic logic)
	{
		if(!logic.getCurrentPlayer().isCPU)
		{
			if(logic.selectedRegion != null)
			{
				show(BUTTON.DETAILS);
				if(logic.targetedRegion != null)
				{
					show(BUTTON.ATTACK);
				}
				else
				{
					hide(BUTTON.ATTACK);
				}
			}
			else
			{
				hide(BUTTON.DETAILS);
			}
		}
		else
		{
			hide(BUTTON.DETAILS);
			hide(BUTTON.ATTACK);
		}
	}

	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	public void createDisplay()
	{	
		createAttackButton();
		createInfoTab();
		createDetailsButton();
		createAutoDeployButton();
		createArrows();

		registerTouchArea(attackButton);
		//registerTouchArea(infoTab);
		registerTouchArea(detailsButton);
		registerTouchArea(autoDeployButton);
		registerTouchArea(arrowLeft);
		registerTouchArea(arrowRight);

		attachChild(attackButton);
		attachChild(infoTab);
		attachChild(detailsButton);
		attachChild(autoDeployButton);
		attachChild(arrowLeft);
		attachChild(arrowRight);
	}

	private void createAttackButton()
	{
		attackButton = new ButtonSprite(0, 0, resources.attackBtnRegion, resources.vbom) 
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.ATTACK);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.ATTACK);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.ATTACK);
					break;
					
				default:
					break;
					
				}
				return true;
			}
		};

		float scale = Utils.getWrapScale(attackButton, 1f * camera.getWidth(), 0.3f * camera.getHeight(), 1f);
		attackButton.setScale(-scale, scale);
		attackButton.setPosition(camera.getWidth() - Utils.getScaledCenterX(attackButton), 0.5f * camera.getHeight());

		attackButton.setVisible(false);
	}

	private void createInfoTab()
	{
		infoTab = new Sprite(0, 0, resources.infoTabRegion, resources.vbom);

		//Utils.expand(infoTab, 1f * camera.getWidth(), 0.1f * camera.getHeight(), 0.9f);
		infoTab.setSize(1f * camera.getWidth(), 0.1f * camera.getHeight());
		infoTab.setPosition(0.5f * camera.getWidth(), camera.getHeight() - 0.5f * infoTab.getHeight());
		infoTab.setAlpha(0.8f);
		
		infoTabText = new Text(0, 0, resources.mInfoTabFont, "", 1000, resources.vbom);

		Utils.wrap(infoTabText, infoTab, 0.9f);
		infoTabText.setPosition(0.5f * infoTab.getWidth(), 0.5f * infoTab.getHeight());
		infoTabText.setColor(Color.BLACK);

		infoTab.attachChild(infoTabText);
		infoTab.setVisible(false);
	}

	private void createDetailsButton()
	{
		detailsButton = new ButtonSprite(0, 0, resources.detailsBtnRegion, resources.vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.DETAILS);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.DETAILS);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.DETAILS);
					break;
					
				default:
					break;
					
				}
				return true;
			}
		};

		Utils.wrap(detailsButton, 1f * camera.getWidth(),  0.3f * camera.getHeight(), 1f);
		detailsButton.setPosition(Utils.getScaledCenterX(detailsButton) , 0.5f * camera.getHeight());

		detailsButton.setVisible(false);
	}

	private void createAutoDeployButton()
	{
		autoDeployButton = new ButtonSprite(0, 0, resources.autoDeployBtnRegion, resources.vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.AUTO_DEPLOY);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.AUTO_DEPLOY);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.AUTO_DEPLOY);
					break;
					
				default:
					break;
				}
				return true;
			}
		};
		
		float scale = Utils.getWrapScale(autoDeployButton, 1f * camera.getWidth(), 0.3f * camera.getHeight(), 1f);

		autoDeployButton.setScale(-scale, scale);
		autoDeployButton.setPosition(camera.getWidth() - Utils.getScaledCenterX(autoDeployButton), 0.5f * camera.getHeight());

		autoDeployButton.setVisible(false);
	}

	private void createArrows()
	{
		arrowLeft = new ButtonSprite(0, 0, resources.arrowRightRegion, resources.vbom) 
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.ARROW_LEFT);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.ARROW_LEFT);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.ARROW_LEFT);
					break;
					
				default:
					break;
				}
				return true;
			}	
		};

		arrowLeft.setScale(-Utils.getWrapScale(arrowLeft, 0.1f * camera.getWidth(), 0.25f * camera.getHeight(), 1f));
		arrowLeft.setPosition(0.25f * camera.getWidth(), 0.5f * camera.getHeight());
		

		arrowRight = new ButtonSprite(0, 0, resources.arrowRightRegion, resources.vbom) 
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.ARROW_RIGHT);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.ARROW_RIGHT);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.ARROW_RIGHT);
					break;
					
				default:
					break;
				}
				return true;
			}	
		};

		arrowRight.setScale(Utils.getWrapScale(arrowRight, 0.1f * camera.getWidth(), 0.25f * camera.getHeight(), 1f));
		arrowRight.setPosition(0.75f * camera.getWidth(), 0.5f * camera.getHeight());

		arrowLeft.setVisible(false);
		arrowRight.setVisible(false);
	}

	// ======================================================
	// LOCK / UNLOCK
	// ======================================================
	public void Lock()
	{

		detailsButton.setEnabled(false);
		attackButton.setEnabled(false);

	}

	public void Unlock()
	{

		detailsButton.setEnabled(true);
		attackButton.setEnabled(true);

	}

	// ======================================================
	// BUTTONS ACTIONS
	// ======================================================
	private void pressed(BUTTON x)
	{
		switch(x)
		{

		case ATTACK:
			attackButton.setCurrentTileIndex(1);
			break;

		case DETAILS:
			// Do something
			break;

		case AUTO_DEPLOY:
			autoDeployButton.setCurrentTileIndex(1);
			break;

		case ARROW_LEFT:
			arrowLeft.setCurrentTileIndex(1);
			break;

		case ARROW_RIGHT:
			arrowRight.setCurrentTileIndex(1);
			break;

		default:
			// Do nothing
			break;
		}
	}

	private void touched(BUTTON x)
	{
		switch(x)
		{

		case ATTACK:
			released(x);
			gameScene.onAttackButtonTouched();
			break;

		case DETAILS:
			released(x);
			gameScene.onDetailButtonTouched();
			break;

		case AUTO_DEPLOY:
			released(x);
			gameScene.onAutoDeployButtonTouched();
			break;

		case ARROW_LEFT:
			released(x);
			gameScene.onLeftArrowTouched();
			break;

		case ARROW_RIGHT:
			released(x);
			gameScene.onRightArrowTouched();
			break;

		default:
			// Do nothing
			break;
		}
	}

	private void released(BUTTON x)
	{
		switch(x) {

		case ATTACK:
			attackButton.setCurrentTileIndex(0);
			break;

		case DETAILS:
			// Do something
			break;

		case AUTO_DEPLOY:
			autoDeployButton.setCurrentTileIndex(0);
			break;

		case ARROW_LEFT:
			arrowLeft.setCurrentTileIndex(0);
			break;

		case ARROW_RIGHT:
			arrowRight.setCurrentTileIndex(0);
			break;

		default:
			// Do nothing
			break;
		}
	}

	public void hide(BUTTON toHide)
	{
		ButtonSprite x = get(toHide);

		if(x == null)
		{
			return;
		}

		if(x.isVisible())
		{
			x.setVisible(false);
		}
	}

	public void show(BUTTON toShow)
	{
		ButtonSprite x = get(toShow);

		if(x == null)
		{
			return;
		}

		if(!x.isVisible())
		{
			x.setVisible(true);
		}
	}

	public void hide(SPRITE toHide)
	{
		Sprite x = get(toHide);

		if(x == null)
		{
			return;
		}

		if(x.isVisible())
		{
			x.setVisible(false);
		}
	}

	public void show(SPRITE toShow)
	{
		Sprite x = get(toShow);

		if(x == null)
		{
			return;
		}

		if(!x.isVisible())
		{
			x.setVisible(true);
		}
	}

	private ButtonSprite get(BUTTON x)
	{
		switch(x)
		{
		case ATTACK:
			return attackButton;
		case DETAILS:
			return detailsButton;
		case AUTO_DEPLOY:
			return autoDeployButton;
		case ARROW_LEFT:
			return arrowLeft;
		case ARROW_RIGHT:
			return arrowRight;
		default:
			return null;
		}
	}

	private Sprite get(SPRITE x)
	{
		switch(x)
		{
		case INFO_TAB:
			return infoTab;
		default:
			return null;
		}
	}

	// ======================================================
	// DETAIL BUTTON
	// ======================================================
	public void setDetailButtonToQuestion()
	{
		detailsButton.setCurrentTileIndex(0);
	}

	public void setDetailButtonToExit()
	{
		detailsButton.setCurrentTileIndex(1);
	}

	// ======================================================
	// INFO TAB
	// ======================================================
	public void setInfoTabText(String info)
	{
		infoTabText.setText(info);
		Utils.wrap(infoTabText, infoTab, 0.9f);
	}

	public void setInfoTabText(GameLogic logic)
	{
		if(logic.getCurrentPlayer().isCPU)
		{
			setInfoTabText(Utils.getString(R.string.game_info_wait_for_CPU));
		}
		else
		{
			if(logic.selectedRegion != null && logic.targetedRegion != null)
			{
				setInfoTabText(Utils.getString(R.string.game_info_attack));
			}
			else if(logic.selectedRegion != null)
			{
				setInfoTabText(Utils.getString(R.string.game_info_tap_enemy_region));
			}
			else
			{
				setInfoTabText(Utils.getString(R.string.game_info_tap_allied_region));
			}
		}
	}
}
