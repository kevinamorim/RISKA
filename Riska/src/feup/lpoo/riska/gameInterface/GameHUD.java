package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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

	private RiskaCanvas actionCanvas;
	private RiskaCanvas summonPoolCanvas;
	private RiskaCanvas movesPoolCanvas;
	
	private RiskaButtonSprite attackButton;
	private RiskaButtonSprite summonButton;
	private RiskaButtonSprite deployButton;
	
	private RiskaCanvas movesPoolBarCanvas;
	private Text movesPoolText;
	private RiskaSprite movesPoolBar;
	
	private RiskaCanvas summonPoolBarCanvas;
	private Text summonPoolText;
	private RiskaSprite summonPoolBar;
	
	private RiskaTextButtonSprite currentPlayerButton;

	private GameScene gameScene;

	// ======================================================
	// ======================================================

	public GameHUD(GameScene scene)
	{
		gameScene = scene;

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

		createActionCanvas();
		createSummonPoolCanvas();
		createMovesPoolCanvas();

		actionCanvas.setAlpha(0.8f);
		summonPoolCanvas.setAlpha(0.8f);
		movesPoolCanvas.setAlpha(0.8f);

		registerTouchArea(summonButton);
		registerTouchArea(deployButton);
		registerTouchArea(attackButton);

		registerTouchArea(summonPoolBarCanvas);
		registerTouchArea(movesPoolBarCanvas);
		
		setTouchAreaBindingOnActionDownEnabled(true);
	}

	private void createActionCanvas()
	{
		actionCanvas = new RiskaCanvas(0f, 0f, 0.5f * camera.getHeight(), 0.1f * camera.getHeight());

		actionCanvas.setCanvasSprite(new RiskaSprite(resources.barLowRegion, vbom));

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
		actionCanvas.addGraphicWrap(attackButton, 0.75f, 0.5f, 0.2f, 0.9f);

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
		actionCanvas.addGraphicWrap(deployButton, 0.5f, 0.5f, 0.2f, 0.9f);

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
		actionCanvas.addGraphicWrap(summonButton, 0.25f, 0.5f, 0.2f, 0.9f);

		actionCanvas.setPosition(0.5f * camera.getWidth(), Utils.halfY(actionCanvas));

		attackButton.setColor(Utils.OtherColors.LIGHT_GREY);
		deployButton.setColor(Utils.OtherColors.LIGHT_GREY);
		summonButton.setColor(Utils.OtherColors.LIGHT_GREY);

		attachChild(actionCanvas);
	}

	private void createSummonPoolCanvas()
	{
		summonPoolCanvas = new RiskaCanvas(0f, 0f, 0.15f * camera.getHeight(), 0.4f * camera.getHeight());

		summonPoolCanvas.setCanvasSprite(new RiskaSprite(resources.barLeftRegion, vbom));
		
		summonPoolBarCanvas = new RiskaCanvas(0f, 0f, 0.55f * summonPoolCanvas.getWidth(), 0.64f * summonPoolCanvas.getWidth())
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{

				switch(ev.getMotionEvent().getActionMasked())
				{

				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.SUMMON_POOL);
					break;

				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.SUMMON_POOL);
					}
					else
					{
						released(BUTTON.SUMMON_POOL);
					}
					break;

				default:
					break;

				}
				return true;
			}
		};
		
		summonPoolBar = new RiskaSprite(resources.fillSquareRegion, vbom);
		summonPoolBar.setAnchorCenterY(0f);
		summonPoolBarCanvas.addGraphic(summonPoolBar, 0.5f, 0f, 0.99f, 0.75f);
		summonPoolBarCanvas.setCanvasSprite(new RiskaSprite(resources.barFillRegion, vbom));
		
		summonPoolText = new Text(0f, 0f, resources.mGameFont, "12", Utils.maxNumericChars, vbom);
		
		summonPoolBarCanvas.setColor(Utils.OtherColors.WHITE);
		summonPoolBar.setColor(Utils.OtherColors.DARK_YELLOW);

		summonPoolCanvas.addGraphic(summonPoolBarCanvas, 0.4f, 0.55f, 0.55f, 0.64f);
		summonPoolCanvas.addText(summonPoolText, 0.4f, 0.1f, 1f, 0.2f);
		
		summonPoolCanvas.setPosition(Utils.halfX(summonPoolCanvas), Utils.halfY(summonPoolCanvas));

		attachChild(summonPoolCanvas);
	}
	
	private void createMovesPoolCanvas()
	{
		movesPoolCanvas = new RiskaCanvas(0f, 0f, 0.15f * camera.getHeight(), 0.4f * camera.getHeight());

		movesPoolCanvas.setCanvasSprite(new RiskaSprite(resources.barRightRegion, vbom));
		
		movesPoolBarCanvas = new RiskaCanvas(0f, 0f, 0.55f * movesPoolCanvas.getWidth(), 0.64f * movesPoolCanvas.getWidth())
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{

				switch(ev.getMotionEvent().getActionMasked())
				{

				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.MOVES_POOL);
					break;

				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.MOVES_POOL);
					}
					else
					{
						released(BUTTON.MOVES_POOL);
					}
					break;

				default:
					break;

				}
				return true;
			}
		};

		movesPoolBar = new RiskaSprite(resources.fillSquareRegion, vbom);
		movesPoolBar.setAnchorCenterY(0f);
		movesPoolBarCanvas.addGraphic(movesPoolBar, 0.5f, 0f, 0.99f, 0.75f);
		movesPoolBarCanvas.setCanvasSprite(new RiskaSprite(resources.barFillRegion, vbom));
		
		movesPoolText = new Text(0f, 0f, resources.mGameFont, "2", Utils.maxNumericChars, vbom);
		
		movesPoolBarCanvas.setColor(Utils.OtherColors.WHITE);
		movesPoolBar.setColor(Utils.OtherColors.LIGHT_GREY);

		movesPoolCanvas.addGraphic(movesPoolBarCanvas, 0.6f, 0.55f, 0.55f, 0.64f);
		movesPoolCanvas.addText(movesPoolText, 0.6f, 0.1f, 1f, 0.2f);
		
		movesPoolCanvas.setPosition(camera.getWidth() - Utils.halfX(movesPoolCanvas), Utils.halfY(movesPoolCanvas));

		attachChild(movesPoolCanvas);
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
			
		case SUMMON_POOL:
			return summonPoolBar;
			
		case MOVES_POOL:
			return movesPoolBar;
			
		case PLAYER:
			return currentPlayerButton;
			
		default:
			return null;
		}
	}

}
