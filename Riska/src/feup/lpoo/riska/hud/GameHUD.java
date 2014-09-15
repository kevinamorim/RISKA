package feup.lpoo.riska.hud;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.BarVertical;
import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.gameInterface.RiskaTextButtonSprite;
import feup.lpoo.riska.gameInterface.UIElement;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.MODE;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.utilities.Utils;

public class GameHUD extends HUD implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================

	public enum BUTTON { SUMMON, DEPLOY, ATTACK, PLAYER, NONE };

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

	private RiskaTextButtonSprite movesLeft;
	private RiskaTextButtonSprite soldiers1, soldiers2, soldiers3;

	private RiskaTextButtonSprite currentPlayerButton;

	private GameScene gameScene;

	// ======================================================
	// ======================================================

	public GameHUD(GameScene scene, GameLogic logic)
	{
		this.gameScene = scene;
		this.logic = logic;

		resources = ResourceCache.instance;
		vbom = resources.vbom;

		camera = resources.camera;

		currentButton = BUTTON.NONE;

		createDisplay();
	}

	public void draw()
	{
		switch(logic.Mode())
		{
		
		case NONE:
			break;
			
		case SUMMON:
			break;
			
		case DEPLOY:
			break;
			
		case ATTACK:
			break;
			
		default:
			break;
		}
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
		createInfoElements();

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
		height = 0.6f * cornerBottomRight.getHeight();

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
	}

	private void createModeButtons()
	{
		float width = 0.75f * barRight.getWidth();
		float height = 0.3f * barRight.getHeight();

		float pX = barRight.getX() + 1;
		float pY = Utils.posY(1/6f, barRight);

		attackButton = new UIElement(width, height, resources.midCenter, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				
				case MotionEvent.ACTION_DOWN:
					//onRegionPressed(this, getTag());
					break;
					
				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.ATTACK);
					}
					else
					{
						//onRegionReleased(this, getTag());
					}
					break;

				default:
					break;
				}

				return true;
			}
		};
		attackButton.setPosition(pX, pY);

		pY = Utils.posY(3/6f, barRight);

		deployButton = new UIElement(width, height, resources.midCenter, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				
				case MotionEvent.ACTION_DOWN:
					//onRegionPressed(this, getTag());
					break;
					
				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.DEPLOY);
					}
					else
					{
						//onRegionReleased(this, getTag());
					}
					break;

				default:
					break;
				}

				return true;
			}
		};
		deployButton.setPosition(pX, pY);

		pY = Utils.posY(5/6f, barRight);

		summonButton = new UIElement(width, height, resources.midCenter, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				
				case MotionEvent.ACTION_DOWN:
					//onRegionPressed(this, getTag());
					break;
					
				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.SUMMON);
					}
					else
					{
						//onRegionReleased(this, getTag());
					}
					break;

				default:
					break;
				}

				return true;
			}
		};
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
		bar1.invertOrientation(true);

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

		bar1.setAlpha(0f);
		pool1.setSpriteAlpha(0f);

		bar2.setAlpha(0f);
		pool2.setSpriteAlpha(0f);

		bar3.setAlpha(0f);
		pool3.setSpriteAlpha(0f);

		attachChild(pool1);
		attachChild(pool2);
		attachChild(pool3);
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

	private void createInfoElements()
	{
		float pWidth = 0.8f * cornerBottomRight.getWidth();
		float pHeight = pWidth;

		movesLeft = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont);
		movesLeft.setColor(Utils.OtherColors.GREY);
		movesLeft.setSize(pWidth, pHeight);

		movesLeft.setTextColor(Utils.OtherColors.BLACK);
		movesLeft.setText("" + logic.getCurrentPlayer().moves);

		movesLeft.setPosition(Utils.posX(0.5f, cornerBottomRight), Utils.posY(0.5f, cornerBottomRight));


		soldiers1 = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont);
		soldiers1.setColor(Utils.OtherColors.GREY);
		soldiers1.setSize(pWidth, pHeight);

		soldiers1.setTextColor(Utils.OtherColors.BLACK);
		soldiers1.setText("");

		soldiers1.setPosition(Utils.posX(0.5f, cornerTopLeft), Utils.posY(0.5f, cornerTopLeft));

		soldiers1 = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont);
		soldiers1.setColor(Utils.OtherColors.GREY);
		soldiers1.setSize(pWidth, pHeight);

		soldiers1.setTextColor(Utils.OtherColors.BLACK);
		soldiers1.setText("");

		soldiers1.setPosition(Utils.posX(0.5f, cornerTopLeft), Utils.posY(0.5f, cornerTopLeft));

		soldiers2 = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont);
		soldiers2.setColor(Utils.OtherColors.GREY);
		soldiers2.setSize(pWidth, pHeight);

		soldiers2.setTextColor(Utils.OtherColors.BLACK);
		soldiers2.setText("");

		soldiers2.setPosition(Utils.posX(0.5f, cornerBottomLeft), Utils.posY(0.5f, cornerBottomLeft));

		soldiers3 = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont);
		soldiers3.setColor(Utils.OtherColors.GREY);
		soldiers3.setSize(pWidth, pHeight);

		soldiers3.setTextColor(Utils.OtherColors.BLACK);
		soldiers3.setText("");

		soldiers3.setPosition(Utils.posX(0.5f, cornerBottomLeft), Utils.posY(0.5f, cornerBottomLeft));

		attachChild(movesLeft);
		attachChild(soldiers1);
		attachChild(soldiers2);
		attachChild(soldiers3);
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
			attackButton.setSpriteColor(Utils.OtherColors.BLACK);
			attackSprite.setColor(Utils.OtherColors.LIGHT_GREY);
			break;

		case DEPLOY:
			deployButton.setSpriteColor(Utils.OtherColors.BLACK);
			deployButton.setColor(Utils.OtherColors.LIGHT_GREY);
			break;

		case SUMMON:
			summonButton.setSpriteColor(Utils.OtherColors.BLACK);
			summonButton.setColor(Utils.OtherColors.LIGHT_GREY);
			break;

		default:
			break;
		}

		if(currentButton != mode)
		{
			currentButton = mode;

			switch(mode)
			{
			
			case ATTACK:
				attackButton.setSpriteColor(Utils.OtherColors.DARK_GREY);
				attackSprite.setColor(Utils.OtherColors.WHITE);
				break;

			case DEPLOY:
				deployButton.setSpriteColor(Utils.OtherColors.DARK_GREY);
				deploySprite.setColor(Utils.OtherColors.WHITE);
				break;

			case SUMMON:
				summonButton.setSpriteColor(Utils.OtherColors.DARK_GREY);
				summonSprite.setColor(Utils.OtherColors.WHITE);
				break;
				
			default:
				break;
			}
		}
		else
		{
			currentButton = BUTTON.NONE;
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
