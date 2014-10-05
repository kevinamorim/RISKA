package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Animated;
import feup.lpoo.riska.utilities.Utils;

public class RiskaButtonSprite extends ButtonSprite implements Animated {

	private Color spriteColor = Utils.OtherColors.GREY;
	private static final float animationTime = 0.2f;
	
	public RiskaButtonSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		super(0f, 0f, pTexture, vbom);
	}
	
	public RiskaButtonSprite(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		super(0f, 0f, pTexture, vbom);
		
		setSize(pWidth, pHeight);
	}

	
	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(0f, 0f, pTexture, vbom);
	}

	@Override
	public void setColor(Color pColor)
	{
		spriteColor = Utils.getColorWithAlpha(pColor, getAlpha());
		
		super.setColor(spriteColor);
	}
	
	public void fadeOut(float deltaTime)
	{
		if(deltaTime == 0f)
		{
			setAlpha(0f);
		}
		else
		{
			AlphaModifier alphaOut = new AlphaModifier(deltaTime, 1f, 0f);
			registerEntityModifier(alphaOut);
		}
	}

	public void fadeOut()
	{
		fadeOut(animationTime);
	}

	public void fadeIn(float deltaTime)
	{
		if(deltaTime == 0f)
		{
			setAlpha(1f);
		}
		else
		{
			AlphaModifier alphaIn = new AlphaModifier(deltaTime, 0f, 1f);
			registerEntityModifier(alphaIn);
		}	
	}

	public void fadeIn()
	{
		fadeIn(animationTime);
	}

	public void animate()
	{
		final float newAnimationTime = 0.5f * animationTime;

		DelayModifier waitForAnim = new DelayModifier(newAnimationTime)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				fadeIn(newAnimationTime);
			}

		};
		registerEntityModifier(waitForAnim);

		fadeOut(newAnimationTime);
	}

	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(float pSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(float pSpeed, float pStartingAngle, float pEndingAngle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopRotation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fadeOutAndStopRotation(float deltaTime) {
		// TODO Auto-generated method stub
		
	}


}
