package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.interfaces.Animated;

public class RiskaTiledSprite extends TiledSprite implements Animated {

	private final float fadeAnimationTime = 0.2f;
	private final float rotateAnimationTime = 3f;
	private final float rotateStartingAngle = 0f;
	private final float rotateEndingAngle = -360f;
	
	private boolean rotating = false;
	
	private AlphaModifier alphaModifier;
	private RotationModifier rotationModifier;
	
	public RiskaTiledSprite(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		super(0f, 0f, pTexture, vbom);
		
		setSize(pWidth, pHeight);
	}
	
	@Override
	public void fadeOut(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(deltaTime == 0f)
		{
			setAlpha(0f);
		}
		else
		{
			alphaModifier = new AlphaModifier(deltaTime, 1f, 0f);
			registerEntityModifier(alphaModifier);
		}
	}

	@Override
	public void fadeOut()
	{
		fadeOut(fadeAnimationTime);
	}

	@Override
	public void fadeIn(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(deltaTime == 0f)
		{
			setAlpha(1f);
		}
		else
		{
			alphaModifier = new AlphaModifier(deltaTime, 0f, 1f);
			registerEntityModifier(alphaModifier);
		}	
	}

	@Override
	public void fadeIn()
	{
		fadeIn(fadeAnimationTime);
	}

	@Override
	public void rotate()
	{
		rotate(rotateAnimationTime, rotateStartingAngle, rotateEndingAngle);
	}
	
	@Override
	public void rotate(float pSpeed)
	{
		rotate(pSpeed, rotateStartingAngle, rotateEndingAngle);
	}
	
	@Override
	public void rotate(float pSpeed, float pStartingAngle, float pEndingAngle)
	{
		if(!rotating)
		{
			rotationModifier = new RotationModifier(pSpeed, pStartingAngle, pEndingAngle);

			registerEntityModifier(new LoopEntityModifier(rotationModifier));
		}
	}
	
	@Override
	public void stopRotation()
	{
		unregisterEntityModifier(rotationModifier);
		
		rotating = false;
	}
	
	@Override
	public void fadeOutAndStopRotation(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(deltaTime == 0f)
		{
			setAlpha(0f);
			stopRotation();
		}
		else
		{
			alphaModifier = new AlphaModifier(deltaTime, 1f, 0f)
			{
				@Override
				protected void onModifierFinished(IEntity pItem)
				{
					stopRotation();
					super.onModifierFinished(pItem);
				}
				
			};
			registerEntityModifier(alphaModifier);
		}
	}
	
}
