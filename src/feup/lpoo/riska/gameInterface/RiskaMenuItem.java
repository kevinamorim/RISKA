package feup.lpoo.riska.gameInterface;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.interfaces.Animated;

public abstract class RiskaMenuItem extends Entity implements Animated, IMenuItem {
	
	protected boolean selected = false;
	
	public RiskaMenuItem(float pX, float pY)
	{
		super(pX, pY, 0f, 0f);
	}
	
	public RiskaMenuItem(float pX, float pY, float pWidth, float pHeight)
	{
		super(pX, pY, pWidth, pHeight);
	}

	@Override
	public abstract void fadeOut(float deltaTime);
	
	@Override
	public abstract void fadeIn(float deltaTime);

	@Override
	public abstract void rotate(float deltaTime);

	@Override
	public abstract void rotate(float deltaTime, float startingAngle, float endingAngle);

	@Override
	public abstract void stopRotation();

	@Override
	public abstract void fadeOutAndStopRotation(float deltaTime);

	@Override
	public boolean isBlendingEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBlendingEnabled(boolean pBlendingEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBlendFunctionSource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlendFunctionDestination() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBlendFunctionSource(int pBlendFunctionSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlendFunctionDestination(int pBlendFunctionDestination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlendFunction(int pBlendFunctionSource,
			int pBlendFunctionDestination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVertexBufferObject getVertexBufferObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShaderProgram getShaderProgram() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShaderProgram(ShaderProgram pShaderProgram) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onSelected() {
		selected = true;
	}

	@Override
	public void onUnselected() {
		if(selected)
			selected = false;
	}

}
