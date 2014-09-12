package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

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

	public enum BUTTON { SUMMON, DEPLOY, ATTACK, PLAYER, SUMMON_POOL, MOVES_POOL, NONE };
	
	public enum SPRITE { BUTTON_TAB };

	private BUTTON currentButton;
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	ResourceCache resources;
	VertexBufferObjectManager vbom;

	// ======================================================
	// FIELDS
	// ======================================================
	private CameraManager camera;
	private GameLogic logic;
	
	private UIButton lowerBar;
	
	private RiskaButtonSprite attackButton;
	private RiskaButtonSprite summonButton;
	private RiskaButtonSprite deployButton;
	private RiskaTextButtonSprite movesButton;
	private RiskaTextButtonSprite poolButton;
	
	private RiskaTextButtonSprite currentPlayerButton;

	private GameScene gameScene;

	// ======================================================
	// ======================================================

	public GameHUD(GameScene scene, GameLogic logic)
	{
		this.gameScene = scene;
		this.logic = logic;

		resources = ResourceCache.getSharedInstance();
		vbom = resources.vbom;

		camera = resources.camera;

		currentButton = BUTTON.NONE;

		createDisplay();
	}

	public void draw(GameLogic logic)
	{
		//		if(!logic.getCurrentPlayer().isCPU)
		//		{
		//			if(logic.getState() == GAME_STATE.PLAY) {
		//
		//				if(logic.selectedRegion != null)
		//				{
		//					show(BUTTON.DETAILS);
		//					if(logic.targetedRegion != null)
		//					{
		//						show(BUTTON.ATTACK);
		//					}
		//					else
		//					{
		//						hide(BUTTON.ATTACK);
		//					}
		//				}
		//				else
		//				{
		//					hide(BUTTON.DETAILS);
		//				}
		//			} else if(logic.getState() == GAME_STATE.MOVE) {
		//				hide(BUTTON.AUTO_DEPLOY);
		//				hide(BUTTON.DETAILS);
		//				hide(BUTTON.ATTACK);
		//				hide(BUTTON.MOVE);
		//				if(logic.selectedRegion != null) {
		//					if(logic.targetedRegion != null) {
		//						show(BUTTON.MOVE);
		//					} 
		//				}
		//
		//			}
		//		}
		//		else
		//		{
		//			hide(BUTTON.DETAILS);
		//			hide(BUTTON.ATTACK);
		//		}
		//
		//		infoTab.setText(logic);
	}

	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	public void createDisplay()
	{

		createMainCanvas();

		registerTouchArea(summonButton);
		registerTouchArea(deployButton);
		registerTouchArea(attackButton);
		
		setTouchAreaBindingOnActionDownEnabled(true);
	}

	private void createMainCanvas()
	{
		lowerBar = new UIButton(0.7f * camera.getHeight(), 0.1f * camera.getHeight(), resources.bottom, resources.vbom);
		lowerBar.setPosition(0.5f * camera.getWidth(), Utils.halfY(lowerBar));
		lowerBar.setSpriteColor(Utils.OtherColors.DARK_GREY);
		
		poolButton = new RiskaTextButtonSprite(resources.regionButtonRegion, vbom, "", resources.mGameFont, Utils.maxNumericChars);
		poolButton.setTextColor(Color.BLACK);
		poolButton.setText("" + logic.getCurrentPlayer().moves);
		
		attackButton = new RiskaButtonSprite(resources.attackButtonRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked())
				{

				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.ATTACK);
					break;

				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.ATTACK);
					}
					else
					{
						released(BUTTON.ATTACK);
					}
					break;

				default:
					break;

				}
				return true;
			}
		};

		deployButton = new RiskaButtonSprite(resources.deployButtonRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{		
				switch(ev.getMotionEvent().getActionMasked())
				{

				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.DEPLOY);
					break;

				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.DEPLOY);
					}
					else
					{
						released(BUTTON.DEPLOY);
					}
					break;

				default:
					break;

				}
				return true;
			}
		};

		summonButton = new RiskaButtonSprite(resources.summonButtonRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{

				switch(ev.getMotionEvent().getActionMasked())
				{

				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.SUMMON);
					break;

				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.SUMMON);
					}
					else
					{
						released(BUTTON.SUMMON);
					}
					break;

				default:
					break;

				}
				return true;
			}
		};

		movesButton = new RiskaTextButtonSprite(resources.regionButtonRegion, vbom, "", resources.mGameFont, Utils.maxNumericChars);
		movesButton.setTextColor(Color.BLACK);
		movesButton.setText("" + logic.getCurrentPlayer().soldiersPool);

		attackButton.setColor(Utils.OtherColors.LIGHT_GREY);
		deployButton.setColor(Utils.OtherColors.LIGHT_GREY);
		summonButton.setColor(Utils.OtherColors.LIGHT_GREY);
		
		Utils.wrap(poolButton, lowerBar, 0.8f);
		Utils.wrap(summonButton, lowerBar, 0.8f);
		Utils.wrap(deployButton, lowerBar, 0.8f);
		Utils.wrap(attackButton, lowerBar, 0.8f);
		Utils.wrap(movesButton, lowerBar, 0.8f);
		
		poolButton.setPosition(1/6f * lowerBar.getWidth(), 0.45f * lowerBar.getHeight());
		summonButton.setPosition(2/6f * lowerBar.getWidth(), 0.45f * lowerBar.getHeight());
		deployButton.setPosition(3/6f * lowerBar.getWidth(), 0.45f * lowerBar.getHeight());
		attackButton.setPosition(4/6f * lowerBar.getWidth(), 0.45f * lowerBar.getHeight());
		movesButton.setPosition(5/6f * lowerBar.getWidth(), 0.45f * lowerBar.getHeight());
		
		lowerBar.attachChild(poolButton);
		lowerBar.attachChild(summonButton);
		lowerBar.attachChild(deployButton);
		lowerBar.attachChild(attackButton);
		lowerBar.attachChild(movesButton);
		
		attachChild(lowerBar);
	}

	// ======================================================
	// LOCK / UNLOCK
	// ======================================================
	public void Lock()
	{
		// TODO
	}

	public void Unlock()
	{
		// TODO
	}

	// ======================================================
	// BUTTONS ACTIONS
	// ======================================================
	private void pressed(BUTTON mode)
	{
		switch(mode)
		{

		case ATTACK:
			attackButton.setCurrentTileIndex(1);
			break;

		case SUMMON:
			summonButton.setCurrentTileIndex(1);
			break;

		case DEPLOY:
			deployButton.setCurrentTileIndex(1);
			break;

		default:
			// Do nothing
			break;
		}
	}

	private void onButtonSelected(BUTTON mode)
	{
		released(mode);

		switch(currentButton)
		{
		case ATTACK:
			attackButton.setColor(Utils.OtherColors.LIGHT_GREY, attackButton.getAlpha());
			break;

		case DEPLOY:
			deployButton.setColor(Utils.OtherColors.LIGHT_GREY, deployButton.getAlpha());
			break;

		case SUMMON:
			summonButton.setColor(Utils.OtherColors.LIGHT_GREY, summonButton.getAlpha());
			break;

		default:
			break;
		}

		currentButton = mode;

		switch(mode)
		{
		case ATTACK:
			attackButton.setColor(Utils.OtherColors.RED, attackButton.getAlpha());
			break;

		case DEPLOY:
			deployButton.setColor(Utils.OtherColors.CYAN, deployButton.getAlpha());
			break;

		case SUMMON:
			summonButton.setColor(Utils.OtherColors.GREEN, summonButton.getAlpha());
			break;

			/*
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
			 */
		default:
			break;
		}
	}

	private void released(BUTTON mode)
	{
		switch(mode)
		{

		case ATTACK:
			attackButton.setCurrentTileIndex(0);
			break;

		case SUMMON:
			summonButton.setCurrentTileIndex(0);
			break;

		case DEPLOY:
			deployButton.setCurrentTileIndex(0);
			break;

			/*
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
			 */
		default:
			// Do nothing
			break;
		}
	}

	public void hide(BUTTON toHide)
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

	public void show(BUTTON toShow)
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

	public Sprite get(BUTTON x)
	{
		switch(x)
		{
		case ATTACK:
			return attackButton;
			
		case SUMMON:
			return summonButton;
			
		case DEPLOY:
			return deployButton;
			
		case PLAYER:
			return currentPlayerButton;
			
		default:
			return null;
		}
	}

	public void test()
	{
		UIButton buttonCanvas = new UIButton(100f, 100f, resources.center, vbom);
		
		buttonCanvas.setSprite(resources.center);
		buttonCanvas.setSpriteColor(Color.RED);
		buttonCanvas.setText("Test Text", resources.mMenuFont);
		buttonCanvas.setTextColor(Color.BLACK);
		
		buttonCanvas.setPosition(camera.getCenterX(), camera.getCenterY());
		buttonCanvas.setSize(0.5f * camera.getWidth(), 0.5f * camera.getHeight());
		
		attachChild(buttonCanvas);
	}
}
