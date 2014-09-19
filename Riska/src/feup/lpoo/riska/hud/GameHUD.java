package feup.lpoo.riska.hud;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.gameInterface.RiskaButtonSprite;
import feup.lpoo.riska.gameInterface.RiskaTextButtonSprite;
import feup.lpoo.riska.gameInterface.UIElement;
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

	public enum BUTTON { SUMMON, DEPLOY, ATTACK, PLAYER, AUTO_DEPLOY, NONE };

	private static float animationTime = 0.5f;

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

	private RiskaButtonSprite[] poolLeft, poolRight;
	private RiskaTextButtonSprite soldiersLeft, soldiersRight;
	private Text soldiersLeftNum, soldiersRightNum;

	private UIElement actionButton, endTurnButton;

	private RiskaTextButtonSprite movesLeft;

	private RiskaTextButtonSprite playerPoolSprite;

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
		createActionButtons();
		createInfoElements();
		createPools();

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

		//		attachChild(barTop);
		//		attachChild(barBottom);
		//		attachChild(barLeft);
		//		attachChild(barRight);

		//attachChild(cornerBottomLeft);
		//attachChild(cornerBottomRight);
		//		attachChild(cornerTopLeft);
		//		attachChild(cornerTopRight);

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


		cornerBottomLeft.setSpriteColor(Utils.OtherColors.GREY);
		cornerBottomRight.setSpriteColor(Utils.OtherColors.GREY);
		cornerTopLeft.setSpriteColor(Utils.OtherColors.GREY);
		cornerTopRight.setSpriteColor(Utils.OtherColors.GREY);

		pAlpha = 1f;

		cornerBottomLeft.setSpriteAlpha(pAlpha);
		cornerBottomRight.setSpriteAlpha(pAlpha);
		cornerTopLeft.setSpriteAlpha(pAlpha);
		cornerTopRight.setSpriteAlpha(pAlpha);

		// TEMPORARY !!!!!!!!!!!!
		//		cornerBottomLeft.rotate();
		//		cornerBottomRight.rotate();
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

		//		attachChild(attackButton);
		//		attachChild(deployButton);
		//		attachChild(summonButton);
	}

	private void createPools()
	{
		float pWidth = 0.35f * camera.getHeight();
		float pHeight = pWidth;
		
		float posOffset = 0.2f;

		// ----------------------------------------
		// CREATE POOL CIRCLES
		// ----------------------------------------
		soldiersLeft = new RiskaTextButtonSprite(resources.buttonRegionBig, vbom, "", resources.mGameNumbersFont);
		soldiersLeft.setColor(Utils.OtherColors.CYAN);
		soldiersLeft.setSize(pWidth, pHeight);
		soldiersLeft.setTextColor(Utils.OtherColors.BLACK);
		soldiersLeft.setText("");
		soldiersLeft.setTextBoundingFactor(0.4f);
		soldiersLeft.setAlpha(0.9f);
		soldiersLeft.setPosition(posOffset * Utils.halfX(soldiersLeft), posOffset * Utils.halfY(soldiersLeft));
		
		soldiersRight = new RiskaTextButtonSprite(resources.buttonRegionBig, vbom, "", resources.mGameNumbersFont);
		soldiersRight.setColor(Utils.OtherColors.DARK_RED);
		soldiersRight.setSize(pWidth, pHeight);
		soldiersRight.setTextColor(Utils.OtherColors.BLACK);
		soldiersRight.setText("");
		soldiersRight.setTextBoundingFactor(0.4f);
		soldiersRight.setAlpha(0.9f);
		
		soldiersRight.setPosition(camera.getWidth() - posOffset * Utils.halfX(soldiersRight), posOffset * Utils.halfY(soldiersRight));

		soldiersLeftNum = new Text(0f, 0f, resources.mGameNumbersFont, "2", Utils.maxNumericChars, vbom);
		Utils.wrap(soldiersLeftNum, soldiersLeft, 0.5f);
		soldiersLeftNum.setColor(Utils.OtherColors.WHITE);
		soldiersLeftNum.setPosition(0.4f * Utils.halfX(soldiersLeft), 0.4f * Utils.halfY(soldiersLeft));
		
		soldiersRightNum = new Text(0f, 0f, resources.mGameNumbersFont, "2", Utils.maxNumericChars, vbom);
		Utils.wrap(soldiersRightNum, soldiersRight, 0.5f);
		soldiersRightNum.setColor(Utils.OtherColors.DARK_YELLOW);
		soldiersRightNum.setPosition(camera.getWidth() - 0.4f * Utils.halfX(soldiersRight), 0.4f * Utils.halfY(soldiersRight));
		
		//registerTouchArea(soldiersLeft);
		//registerTouchArea(soldiersRight);

		// ----------------------------------------
		// CREATE POOLS
		// ----------------------------------------
		int soldiersPerPool = GameInfo.maxGarrison;

		poolLeft = new RiskaButtonSprite[soldiersPerPool];
		poolRight = new RiskaButtonSprite[soldiersPerPool];
		
		double arch = Math.PI / 2;
		arch *= Utils.halfX(soldiersLeft);

		pWidth = (float) ((0.8f / soldiersPerPool) * arch);
		pHeight = pWidth;

		double angleInc = Math.PI / (2 * (soldiersPerPool - 1));

		for(int i = 0; i < soldiersPerPool; i++)
		{
			poolLeft[i] = new RiskaButtonSprite(pWidth, pHeight, resources.buttonRegion, vbom);
			poolRight[i] = new RiskaButtonSprite(pWidth, pHeight, resources.buttonRegion, vbom);
		}
		
		for(int i = 0; i < soldiersPerPool; i++)
		{
			float radiusX = Utils.halfX(soldiersLeft) - Utils.halfX(poolLeft[i]);
			float radiusY = Utils.halfY(soldiersLeft) - Utils.halfY(poolLeft[i]);
			
			// Set Position
			float pX = soldiersLeft.getX() + (float) (radiusX * Math.cos((double)i * angleInc));
			float pY = soldiersLeft.getY() + (float) (radiusY * Math.sin((double)i * angleInc));
			poolLeft[i].setPosition(pX, pY);
			
			radiusX = Utils.halfX(soldiersRight) - Utils.halfX(poolRight[i]);
			radiusY = Utils.halfY(soldiersRight) - Utils.halfY(poolRight[i]);

			pX = soldiersRight.getX() - (float) (radiusX * Math.cos((double)i * angleInc));
			pY = soldiersRight.getY() + (float) (radiusY * Math.sin((double)i * angleInc));
			poolRight[i].setPosition(pX, pY);
		}
		
		attachChild(soldiersLeft);
		attachChild(soldiersRight);
		
		attachChild(soldiersLeftNum);
		attachChild(soldiersRightNum);

		for(int i = 0; i < soldiersPerPool; i++)
		{
			// Attach to HUD
			attachChild(poolRight[i]);
			attachChild(poolLeft[i]);
		}

		updatePoolsColors();
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

		//		attachChild(actionButton);
		//		attachChild(endTurnButton);

		playerPoolSprite = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.AUTO_DEPLOY);
					break;

				case MotionEvent.ACTION_UP:
					if(Utils.contains(this, pX, pY))
					{
						onButtonSelected(BUTTON.AUTO_DEPLOY);
					}
					else
					{
						released(BUTTON.AUTO_DEPLOY);
					}
					break;

				default:
					break;
				}

				return true;
			}
		};

		playerPoolSprite.setSize(0.15f * camera.getHeight(), 0.15f * camera.getHeight());
		playerPoolSprite.setPosition(Utils.halfX(playerPoolSprite), Utils.halfY(playerPoolSprite));
		playerPoolSprite.setColor(Utils.OtherColors.GREY);
		playerPoolSprite.setTextBoundingFactor(0.7f);

		updateInfo();
		//registerTouchArea(playerPoolSprite);

		//attachChild(playerPoolSprite);
	}

	private void createInfoElements()
	{
		float pWidth = 0.35f * camera.getHeight();
		float pHeight = pWidth;

		movesLeft = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameFont);
		movesLeft.setColor(Utils.OtherColors.GREY);
		movesLeft.setSize(pWidth, pHeight);

		movesLeft.setTextColor(Utils.OtherColors.BLACK);
		movesLeft.setText("" + logic.getCurrentPlayer().moves);

		movesLeft.setPosition(Utils.posX(0.5f, cornerBottomRight), Utils.posY(0.5f, cornerBottomRight));
	}

	public void updateInfo()
	{
		switch(logic.getState())
		{
		case SETUP:
			if( logic.getCurrentPlayer().soldiersPool > 0)
			{
				playerPoolSprite.setText("" + logic.getCurrentPlayer().soldiersPool);
			}
			break;

		case PLAY:
			updatePoolsColors();
			break;

		default:
			if(playerPoolSprite.isVisible())
			{
				playerPoolSprite.fadeOut(animationTime);
			}	
			break;
		}
	}

	public void updatePoolsColors()
	{
		for(int i = 0; i < poolLeft.length; i++)
		{
			// Set Color
			poolLeft[i].setColor(logic.Selected() ? logic.selectedRegion.priColor : Utils.OtherColors.WHITE);
			poolRight[i].setColor(logic.Targeted() ? logic.targetedRegion.priColor : Utils.OtherColors.DARK_YELLOW);
		}	
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

		case AUTO_DEPLOY:
			playerPoolSprite.setCurrentTileIndex(1);
			break;

		default:
			// Do nothing
			break;
		}
	}

	private void onButtonSelected(BUTTON mode)
	{
		switch(mode)
		{

		//		case ATTACK:
		//			attackButton.setSpriteColor(Utils.OtherColors.BLACK);
		//			attackSprite.setColor(Utils.OtherColors.LIGHT_GREY);
		//			break;
		//
		//		case DEPLOY:
		//			deployButton.setSpriteColor(Utils.OtherColors.BLACK);
		//			deployButton.setColor(Utils.OtherColors.LIGHT_GREY);
		//			break;
		//
		//		case SUMMON:
		//			summonButton.setSpriteColor(Utils.OtherColors.BLACK);
		//			summonButton.setColor(Utils.OtherColors.LIGHT_GREY);
		//			break;

		case AUTO_DEPLOY:
			gameScene.onAutoDeployButtonTouched();
			updateInfo();
			break;

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

		case AUTO_DEPLOY:
			playerPoolSprite.setCurrentTileIndex(0);
			break;

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
