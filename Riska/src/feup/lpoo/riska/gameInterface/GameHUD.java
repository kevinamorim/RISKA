package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.utilities.Utils;
import android.view.MotionEvent;

public class GameHUD extends HUD implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================

	public enum BUTTON { ATTACK, MOVE, DETAILS, AUTO_DEPLOY, NEXT_TURN, ARROW_LEFT, ARROW_RIGHT };

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
	private ButtonSprite moveButton;
	private ButtonSprite detailsButton;
	private ButtonSprite autoDeployButton;
	private ButtonSprite nextTurnButton;
	private ButtonSprite arrowLeft;
	private ButtonSprite arrowRight;
	private InfoTab infoTab;

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
			if(logic.getState() == GAME_STATE.PLAY) {
				
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
			} else if(logic.getState() == GAME_STATE.MOVE) {
				hide(BUTTON.AUTO_DEPLOY);
				hide(BUTTON.DETAILS);
				hide(BUTTON.ATTACK);
				hide(BUTTON.MOVE);
				if(logic.selectedRegion != null) {
					if(logic.targetedRegion != null) {
						show(BUTTON.MOVE);
					} 
				}
				
			}
		}
		else
		{
			hide(BUTTON.DETAILS);
			hide(BUTTON.ATTACK);
		}
		
		infoTab.setText(logic);
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
		createNextTurnButton();
		createArrows();
		createMoveButton();

		registerTouchArea(attackButton);
		registerTouchArea(detailsButton);
		registerTouchArea(autoDeployButton);
		registerTouchArea(nextTurnButton);
		registerTouchArea(arrowLeft);
		registerTouchArea(arrowRight);
		registerTouchArea(moveButton);

		attachChild(attackButton);
		attachChild(infoTab.getSprite());
		attachChild(detailsButton);
		attachChild(autoDeployButton);
		attachChild(nextTurnButton);
		attachChild(arrowLeft);
		attachChild(arrowRight);
		attachChild(moveButton);
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
		
		float pWidth = 1f * camera.getWidth();
		float pHeight = 0.1f * camera.getHeight();
		float pX = 0.5f * camera.getWidth();
		float pY =  camera.getHeight() - 0.5f * resources.infoTabRegion.getHeight();
		float pAlpha = 0.8f;
		
		infoTab = new InfoTab(resources.infoTabRegion, resources.mInfoTabFont, 
				pWidth, pHeight, pX, pY, pAlpha, resources.vbom);
		
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
	
	private void createMoveButton() {
		moveButton = new ButtonSprite(0, 0, resources.moveBtnRegion, resources.vbom) 
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.MOVE);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.MOVE);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.MOVE);
					break;
					
				default:
					break;
					
				}
				return true;
			}
		};

		float scale = Utils.getWrapScale(moveButton, 1f * camera.getWidth(), 0.3f * camera.getHeight(), 1f);
		moveButton.setScale(-scale, scale);
		moveButton.setPosition(camera.getWidth() - Utils.getScaledCenterX(moveButton), 0.5f * camera.getHeight());

		moveButton.setVisible(false);
	}

	private void createNextTurnButton() {
		nextTurnButton = new ButtonSprite(0, 0, resources.nextTurnBtnRegion, resources.vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getAction())
				{
				
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.NEXT_TURN);
					break;
					
				case MotionEvent.ACTION_UP:
					touched(BUTTON.NEXT_TURN);
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.NEXT_TURN);
					break;
					
				default:
					break;
					
				}
				return true;
			}
		};
		float scale = Utils.getWrapScale(nextTurnButton, 1f * camera.getWidth(), 0.3f * camera.getHeight(), 1f);
		nextTurnButton.setScale(-scale, scale);
		nextTurnButton.setPosition(camera.getWidth() - Utils.getScaledCenterX(nextTurnButton), 0.5f * camera.getHeight());
		nextTurnButton.setVisible(false);
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
			
		case MOVE:
			moveButton.setCurrentTileIndex(1);
			break;

		case DETAILS:
			// Do something
			break;

		case AUTO_DEPLOY:
			autoDeployButton.setCurrentTileIndex(1);
			break;
			
		case NEXT_TURN:
			nextTurnButton.setCurrentTileIndex(1);
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
		
		released(x);
		
		switch(x)
		{

		case ATTACK:
			gameScene.onAttackButtonTouched();
			break;
			
		case MOVE:
			gameScene.onMoveButtonTouched();
			break;

		case DETAILS:
			gameScene.onDetailButtonTouched();
			break;

		case AUTO_DEPLOY:
			gameScene.onAutoDeployButtonTouched();
			break;
			
		case NEXT_TURN:
			gameScene.onNextTurnButtonTouched();
			break;

		case ARROW_LEFT:
			gameScene.onLeftArrowTouched();
			break;

		case ARROW_RIGHT:
			gameScene.onRightArrowTouched();
			break;

		default:
			break;
		}
	}

	private void released(BUTTON x)
	{
		switch(x) {

		case ATTACK:
			attackButton.setCurrentTileIndex(0);
			break;
			
		case MOVE:
			moveButton.setCurrentTileIndex(0);
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

	public ButtonSprite get(BUTTON x)
	{
		switch(x)
		{
		case ATTACK:
			return attackButton;
		case DETAILS:
			return detailsButton;
		case MOVE:
			return moveButton;
		case AUTO_DEPLOY:
			return autoDeployButton;
		case NEXT_TURN:
			return nextTurnButton;
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
			return infoTab.getSprite();
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

}
