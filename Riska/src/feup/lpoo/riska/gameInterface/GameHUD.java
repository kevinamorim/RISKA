package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.utilities.Utils;

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

	private UIElement barLeft, barBottom, barRight, barTop;
	private UIElement cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight;

	private UIElement attackButton, summonButton, deployButton;
	private TiledSprite attackSprite, summonSprite, deploySprite;

	private UIElement pool1, pool2, pool3;
	private BarVertical bar1, bar2, bar3;

	private UIElement actionButton, endTurnButton;

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

		createFrame();
		createModeButtons();
		createPools();
		createActionButtons();

		registerTouchArea(summonButton);
		registerTouchArea(deployButton);
		registerTouchArea(attackButton);

		setTouchAreaBindingOnActionDownEnabled(true);
	}

	private void createFrame()
	{
		float width = 0.18f * camera.getHeight();
		float height = 0.18f * camera.getHeight();

		cornerBottomLeft = new UIElement(width, height, resources.bottomLeft, vbom);
		cornerBottomLeft.setPosition(Utils.halfX(cornerBottomLeft), Utils.halfY(cornerBottomLeft));

		cornerBottomRight = new UIElement(cornerBottomLeft.getWidth(), cornerBottomLeft.getHeight(), resources.bottomRight, vbom);
		cornerBottomRight.setPosition(camera.getWidth() - Utils.halfX(cornerBottomRight), cornerBottomLeft.getY());

		cornerTopLeft = new UIElement(cornerBottomLeft.getWidth(), cornerBottomLeft.getHeight(), resources.topLeft, vbom);
		cornerTopLeft.setPosition(cornerBottomLeft.getX(), camera.getHeight() - Utils.halfY(cornerTopLeft));

		cornerTopRight = new UIElement(cornerBottomLeft.getWidth(), cornerBottomLeft.getHeight(), resources.topRight, vbom);
		cornerTopRight.setPosition(cornerBottomRight.getX(), cornerTopLeft.getY());

		width = Utils.leftGlobal(cornerTopRight) - Utils.rightGlobal(cornerTopLeft);
		height = 0.5f * cornerBottomRight.getHeight();

		barTop = new UIElement(width, height, resources.topCenterNoBorder, vbom);
		barTop.setPosition(camera.getCenterX(), camera.getHeight() - Utils.halfY(barTop));

		barBottom = new UIElement(width, height, resources.bottomCenterNoBorder, vbom);
		barBottom.setPosition(camera.getCenterX(), Utils.halfY(barBottom));

		width = 0.8f * cornerBottomLeft.getWidth();
		height = Utils.bottomGlobal(cornerTopLeft) - Utils.topGlobal(cornerBottomLeft);

		barLeft = new UIElement(width, height, resources.midLeftNoBorder, vbom);
		barLeft.setPosition(Utils.halfX(barLeft), camera.getCenterY());

		barRight = new UIElement(width, height, resources.midRightNoBorder, vbom);
		barRight.setPosition(camera.getWidth() - Utils.halfX(barRight), camera.getCenterY());

		attachChild(barTop);
		attachChild(barBottom);
		attachChild(barLeft);
		attachChild(barRight);

		attachChild(cornerBottomLeft);
		attachChild(cornerBottomRight);
		attachChild(cornerTopLeft);
		attachChild(cornerTopRight);

		barTop.setSpriteColor(Utils.OtherColors.GREY);
		barBottom.setSpriteColor(Utils.OtherColors.GREY);
		barLeft.setSpriteColor(Utils.OtherColors.GREY);
		barRight.setSpriteColor(Utils.OtherColors.GREY);

		float pAlpha = 0.7f;

		barTop.setSpriteAlpha(pAlpha);
		barBottom.setSpriteAlpha(pAlpha);

		pAlpha = 0.9f;

		barLeft.setSpriteAlpha(pAlpha);
		barRight.setSpriteAlpha(pAlpha);


		cornerBottomLeft.setSpriteColor(Utils.OtherColors.BLACK);
		cornerBottomRight.setSpriteColor(Utils.OtherColors.BLACK);
		cornerTopLeft.setSpriteColor(Utils.OtherColors.BLACK);
		cornerTopRight.setSpriteColor(Utils.OtherColors.BLACK);

		pAlpha = 1f;

		cornerBottomLeft.setSpriteAlpha(pAlpha);
		cornerBottomRight.setSpriteAlpha(pAlpha);
		cornerTopLeft.setSpriteAlpha(pAlpha);
		cornerTopRight.setSpriteAlpha(pAlpha);


		/*
		width = 0.6f;
		height = 0.12f;

		lowerBarCenter = new UIElement(width * camera.getWidth(), height * camera.getHeight(), resources.bottom, resources.vbom);
		lowerBarCenter.setPosition(0.5f * camera.getWidth(), Utils.halfY(lowerBarCenter));
		lowerBarCenter.setSpriteColor(Utils.OtherColors.LIGHT_GREY);

		width = 0.15f;
		height = 0.15f;

		lowerBarLeft = new UIElement(width * camera.getHeight(), height * camera.getHeight(), resources.bottomLeft, resources.vbom);
		lowerBarLeft.setPosition(Utils.halfX(lowerBarLeft), Utils.halfY(lowerBarLeft));
		lowerBarLeft.setSpriteColor(Utils.OtherColors.LIGHT_GREY);

		lowerBarRight = new UIElement(width * camera.getHeight(), height * camera.getHeight(), resources.bottomRight, resources.vbom);
		lowerBarRight.setPosition(camera.getWidth() - Utils.halfX(lowerBarRight), Utils.halfY(lowerBarRight));
		lowerBarRight.setSpriteColor(Utils.OtherColors.LIGHT_GREY);

		poolButton = new RiskaTextButtonSprite(resources.regionButtonRegion, vbom, "", resources.mGameFont, Utils.maxNumericChars);
		poolButton.setTextColor(Color.BLACK);
		poolButton.setText("" + logic.getCurrentPlayer().moves);
		poolButton.setTextColor(Color.WHITE);

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
		movesButton.setTextColor(Color.WHITE);

		poolButton.setColor(Utils.OtherColors.LIGHT_GREY);
		attackButton.setColor(Utils.OtherColors.LIGHT_GREY);
		deployButton.setColor(Utils.OtherColors.LIGHT_GREY);
		summonButton.setColor(Utils.OtherColors.LIGHT_GREY);
		movesButton.setColor(Utils.OtherColors.LIGHT_GREY);

		float factor = 0.8f;

		Utils.wrap(poolButton, lowerBarCenter, factor);
		Utils.wrap(summonButton, lowerBarCenter, factor);
		Utils.wrap(deployButton, lowerBarCenter, factor);
		Utils.wrap(attackButton, lowerBarCenter, factor);
		Utils.wrap(movesButton, lowerBarCenter, factor);

		height = 0.45f;

		//poolButton.setPosition(1/8f * lowerBarCenter.getWidth(), height * lowerBarCenter.getHeight());
		//movesButton.setPosition(7/8f * lowerBarCenter.getWidth(), height * lowerBarCenter.getHeight());

		summonButton.setPosition(3/8f * lowerBarCenter.getWidth(), height * lowerBarCenter.getHeight());
		deployButton.setPosition(4/8f * lowerBarCenter.getWidth(), height * lowerBarCenter.getHeight());
		attackButton.setPosition(5/8f * lowerBarCenter.getWidth(),height * lowerBarCenter.getHeight());


		//lowerBarCenter.attachChild(poolButton);
		//lowerBarCenter.attachChild(movesButton);
		lowerBarCenter.attachChild(summonButton);
		lowerBarCenter.attachChild(deployButton);
		lowerBarCenter.attachChild(attackButton);

		//attachChild(lowerBarLeft);
		//attachChild(lowerBarRight);
		//attachChild(lowerBarCenter);
		 */
	}

	private void createModeButtons()
	{
		float width = 0.75f * barRight.getWidth();
		float height = 0.3f * barRight.getHeight();

		float pX = barRight.getX() + 1;
		float pY = Utils.posY(1/6f, barRight);

		attackButton = new UIElement(width, height, resources.midCenter, vbom);
		attackButton.setPosition(pX, pY);

		pY = Utils.posY(3/6f, barRight);

		deployButton = new UIElement(width, height, resources.midCenter, vbom);
		deployButton.setPosition(pX, pY);

		pY = Utils.posY(5/6f, barRight);

		summonButton = new UIElement(width, height, resources.midCenter, vbom);
		summonButton.setPosition(pX, pY);

		width = 0.85f * attackButton.getWidth();
		height = width;
		//height = 0.85f * attackButton.getWidth();

		pX = 0.5f * attackButton.getWidth();
		pY = 0.5f * attackButton.getHeight();

		attackSprite = new TiledSprite(pX, pY, width, height, resources.spriteAttack, vbom);
		attackSprite.setColor(Utils.OtherColors.LIGHT_GREY);
		attackButton.attachChild(attackSprite);

		deploySprite = new TiledSprite(pX, pY, width, height, resources.spriteDeploy, vbom);
		deploySprite.setColor(Utils.OtherColors.LIGHT_GREY);
		deployButton.attachChild(deploySprite);

		summonSprite = new TiledSprite(pX, pY, width, height, resources.spriteSummon, vbom);
		summonSprite.setColor(Utils.OtherColors.LIGHT_GREY);
		summonButton.attachChild(summonSprite);

		attackButton.setSpriteColor(Utils.OtherColors.BLACK);
		deployButton.setSpriteColor(Utils.OtherColors.BLACK);
		summonButton.setSpriteColor(Utils.OtherColors.BLACK);

//		float pAlpha = 0.7f;

//		attackButton.setSpriteAlpha(pAlpha);
//		deployButton.setSpriteAlpha(pAlpha);
//		summonButton.setSpriteAlpha(pAlpha);

		attachChild(attackButton);
		attachChild(deployButton);
		attachChild(summonButton);
	}

	private void createPools()
	{
		// ----------------------------------------
		// CREATE BARS/POOLS
		// ----------------------------------------
		float width = 0.6f * barLeft.getWidth();
		float height = 0.46f * barLeft.getHeight();

		pool1 = new UIElement(width, height, resources.midCenter, vbom);
		pool2 = new UIElement(width, height, resources.midCenter, vbom);

		height = 0.96f * barLeft.getHeight();	
		pool3 = new UIElement(width, height, resources.midCenter, vbom);

		width = 0.9f * pool1.getWidth();
		height = 0.95f * pool1.getHeight();

		bar1 = new BarVertical(width, height, GameInfo.maxGarrison, GameInfo.minGarrison, resources.midCenterSmall, vbom);
		bar1.setColors(Utils.OtherColors.DARK_GREY, Utils.OtherColors.BLUE_GREEN);

		bar2 = new BarVertical(width, height, GameInfo.maxGarrison, GameInfo.minGarrison, resources.midCenterSmall, vbom);
		bar2.setColors(Utils.OtherColors.DARK_GREY, Utils.OtherColors.BLUE_GREEN);

		width = 0.9f * pool3.getWidth();
		height = 0.95f * pool3.getHeight();
		bar3 = new BarVertical(width, height, GameInfo.maxSummonPool, GameInfo.minSummonPool, resources.midCenterSmall, vbom);
		bar3.setColors(Utils.OtherColors.DARK_GREY, Utils.OtherColors.BLUE_GREEN);

		// ----------------------------------------
		// SET POSITION OF BARS/POOLS
		// ----------------------------------------
		float pX = barLeft.getX() - 1;

		pool1.setPosition(pX, Utils.posY(0.75f, barLeft));
		pool2.setPosition(pX, Utils.posY(0.25f, barLeft));
		pool3.setPosition(pX, Utils.posY(0.5f, barLeft));

		bar1.setPosition(Utils.halfX(pool1), Utils.halfY(pool1));
		bar2.setPosition(Utils.halfX(pool2), Utils.halfY(pool2));
		bar3.setPosition(Utils.halfX(pool3), Utils.halfY(pool3));
		
		pool1.attachChild(bar1);
		pool2.attachChild(bar2);
		pool3.attachChild(bar3);

		// ----------------------------------------
		// ----------------------------------------
		pool1.setSpriteColor(Utils.OtherColors.BLACK);
		pool2.setSpriteColor(Utils.OtherColors.BLACK);
		pool3.setSpriteColor(Utils.OtherColors.BLACK);

		attachChild(pool1);
		attachChild(pool2);
		//attachChild(pool3);
	}

	private void createActionButtons()
	{
		float width = 0.49f * barBottom.getWidth();
		float height = 0.90f * barBottom.getHeight();
		
		actionButton = new UIElement(width, height, resources.midCenter, vbom);
		actionButton.setSpriteColor(Color.BLACK);
		
		actionButton.setTextBoundingFactor(0.9f, 0.6f);
		actionButton.setTextColor(Utils.OtherColors.LIGHT_GREY);
		actionButton.setText("ACTION", resources.mGameFont);
		
		actionButton.setPosition(Utils.posX(0.25f, barBottom), barBottom.getY() - 1);
		
		endTurnButton = new UIElement(width, height, resources.midCenter, vbom);
		endTurnButton.setSpriteColor(Color.BLACK);
		
		endTurnButton.setTextBoundingFactor(0.9f, 0.6f);
		endTurnButton.setTextColor(Utils.OtherColors.LIGHT_GREY);
		endTurnButton.setText("END TURN", resources.mGameFont);
		
		endTurnButton.setPosition(Utils.posX(0.75f, barBottom), barBottom.getY() - 1);
		
		attachChild(actionButton);
		attachChild(endTurnButton);
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
			//attackButton.setCurrentTileIndex(1);
			break;

		case SUMMON:
			//summonButton.setCurrentTileIndex(1);
			break;

		case DEPLOY:
			//deployButton.setCurrentTileIndex(1);
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
			//attackButton.setColor(Utils.OtherColors.LIGHT_GREY, attackButton.getAlpha());
			break;

		case DEPLOY:
			//deployButton.setColor(Utils.OtherColors.LIGHT_GREY, deployButton.getAlpha());
			break;

		case SUMMON:
			//summonButton.setColor(Utils.OtherColors.LIGHT_GREY, summonButton.getAlpha());
			break;

		default:
			break;
		}

		currentButton = mode;

		switch(mode)
		{
		case ATTACK:
			//attackButton.setColor(Utils.OtherColors.RED, attackButton.getAlpha());
			break;

		case DEPLOY:
			//deployButton.setColor(Utils.OtherColors.CYAN, deployButton.getAlpha());
			break;

		case SUMMON:
			//summonButton.setColor(Utils.OtherColors.GREEN, summonButton.getAlpha());
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
			//attackButton.setCurrentTileIndex(0);
			break;

		case SUMMON:
			//summonButton.setCurrentTileIndex(0);
			break;

		case DEPLOY:
			//deployButton.setCurrentTileIndex(0);
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
		Sprite x = (Sprite)get(toHide);

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
		Sprite x = (Sprite)get(toShow);

		if(x == null)
		{
			return;
		}

		if(!x.isVisible())
		{
			x.setVisible(true);
		}
	}

	public IEntity get(BUTTON x)
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
		UIElement buttonCanvas = new UIElement(100f, 100f, resources.midCenter, vbom);

		buttonCanvas.setSprite(resources.midCenter);
		buttonCanvas.setSpriteColor(Color.RED);
		buttonCanvas.setText("Test Text", resources.mMenuFont);
		buttonCanvas.setTextColor(Color.BLACK);

		buttonCanvas.setPosition(camera.getCenterX(), camera.getCenterY());
		buttonCanvas.setSize(0.5f * camera.getWidth(), 0.5f * camera.getHeight());

		attachChild(buttonCanvas);
	}

	
	// THE FOLLOWING ARE ONLY TO THE GAME'S MAP !!
	
	public float mapWidth()
	{
		return Utils.leftGlobal(barRight) - Utils.rightGlobal(barLeft);
	}
	
	public float mapHeight()
	{
		return Utils.bottomGlobal(barTop) - Utils.topGlobal(barBottom);
	}
	
}
