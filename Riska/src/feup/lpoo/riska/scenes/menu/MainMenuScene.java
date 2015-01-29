package feup.lpoo.riska.scenes.menu;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.RiskaMenuIcon;
import feup.lpoo.riska.gameInterface.RiskaMenuItem;
import feup.lpoo.riska.gameInterface.RiskaSprite;
import feup.lpoo.riska.utilities.Utils;

public class MainMenuScene extends RiskaMenuScene {

	RiskaMenuItem newGame;
	//RiskaMenuItem loadGame;
	RiskaMenuItem options;
	
	// ------------------
	//  Methods
	public MainMenuScene(Camera pCamera)
	{
		super(pCamera);
		
		createDisplay();
	}

	@Override
	public void createDisplay()
	{
//		newGame = new RiskaMenuIcon(
//				camera.getCenterX(), 0.67f * camera.getHeight(),
//				0.5f * camera.getWidth(), 0.25f * camera.getHeight(),
//				new RiskaSprite(resources.buttonGenericSmall, resources.vbom), new RiskaSprite(resources.solidNoBorder, resources.vbom));
//		
//		options = new RiskaMenuIcon(
//				camera.getCenterX(), 0.33f * camera.getHeight(),
//				newGame.getWidth(), newGame.getHeight(),
//				new RiskaSprite(resources.buttonGenericSmall, resources.vbom), new RiskaSprite(resources.solidNoBorder, resources.vbom));
//		
//		
//		Sprite background = new Sprite(
//				camera.getCenterX(), 
//				camera.getCenterY(),
//				camera.getWidth() + 1,
//				camera.getHeight() + 1,
//				resources.menuBackground, 
//				resources.vbom);
//
//		Sprite alphaCover = new Sprite(
//				camera.getCenterX(),
//				camera.getCenterY(),
//				0.9f * camera.getWidth(),
//				0.9f * camera.getHeight(),
//				resources.solidNoBorder,
//				resources.vbom);
//
//		alphaCover.setColor(Color.BLACK);
//		alphaCover.setAlpha(0.8f);
//		
//		attachChild(background);
//		attachChild(alphaCover);
//		
//		attachChild(newGame);
//		attachChild(options);
		
		RiskaMenuIcon collisionSprite = new RiskaMenuIcon(
				camera.getCenterX(), 0.67f * camera.getHeight(),
				resources.buttonGenericMedium.getWidth(), resources.buttonGenericMedium.getHeight(),
				//0.5f * camera.getWidth(), 0.25f * camera.getHeight(),
				new RiskaSprite(resources.buttonGenericMedium, resources.vbom), new RiskaSprite(resources.buttonGenericMedium, resources.vbom)) {
			
			@Override
            public boolean onAreaTouched(TouchEvent pEvent, float pX, float pY)
            {			
                switch(pEvent.getMotionEvent().getActionMasked()){
                case MotionEvent.ACTION_DOWN:                  
                        break;
                case MotionEvent.ACTION_MOVE:
                        break;
                case MotionEvent.ACTION_OUTSIDE:
                		break;
                case MotionEvent.ACTION_UP:    
                    final boolean isTransparent = (getTextureRegion(0).getBitmapPixelTransparency((int)pX, (int)pY) == 0);

                    if(!isTransparent){
                        Debug.d("Riska","-> Collision Test Detected");
                        Debug.d("Riska","------> " + pX + ", " + pY);
                        Debug.d("Riska","------> " + (int)pX + ", " + (int)pY);
                    }   
                }
               
                return true;
            }
		};
		
		attachChild(collisionSprite);
		registerTouchArea(collisionSprite);
	}
	
}
