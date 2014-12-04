package feup.lpoo.riska.scenes;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;

import android.util.Log;
import feup.lpoo.riska.gameInterface.RiskaMenuItem;
import feup.lpoo.riska.utilities.Utils;

public class RiskaMenuScene extends MenuScene {
	
	protected float radius;
	
	public enum MODE { CIRCULAR, DROPDOWN };
	
	// ------------------
	//  General
	protected ArrayList<RiskaMenuItem> items;
	protected int numIcons = 0;
	
	protected MODE mode = MODE.CIRCULAR;

	// ------------------
	//  Methods
	public RiskaMenuScene(Camera pCamera)
	{
		super(pCamera);
		this.setBackgroundEnabled(false);
		//this.setPosition(pCamera.getCenterX(), pCamera.getCenterY());
		
		//Log.d("Riska","> Camera Size    | " + pCamera.getWidth() + " x " + pCamera.getHeight());
		//Log.d("Riska","> Scene Size     | " + this.getWidth() + " x " + this.getHeight());
		//Log.d("Riska","> Scene Position | " + this.getX() + " x " + this.getY());
		
		
		this.radius = 0.25f * this.getHeight();
		//Log.d("Riska","Radius: " + radius);
		
		this.items = new ArrayList<RiskaMenuItem>();
	}

	public void addItem(RiskaMenuItem item)
	{
		items.add(item);
		numIcons = items.size();
		
		if(item.hasParent())
			item.detachSelf();
		
		attachChild(item);
		
		//item.setPosition(Utils.halfX(this), Utils.halfY(this));
		//Log.d("Riska","> Pos | " + item.getX() + " | " + item.getY());
		
		updateDisplay();
	}
	
	public void setDisplayMode(MODE x)
	{
		this.mode = x;
		
		updateDisplay();
	}

	public void updateDisplay()
	{
		switch(mode)
		{
		
		case CIRCULAR:
			double angle = 2.0 * Math.PI / numIcons;
			double offset = 3.0 * Math.PI / 2.0;
			//double offset = 0;
			
			for(int i = 0; i < numIcons; i++)
			{
				float pX = (float)(Utils.halfX(this) + radius * Math.cos(i * angle + offset));
				float pY = (float)(Utils.halfY(this) + radius * Math.sin(i * angle + offset));
				items.get(i).setPosition(pX, pY);
				
				Log.d("Riska","> Pos | " + items.get(i).getX() + " | " + items.get(i).getY());
			}
			break;
			
		case DROPDOWN:
			// TODO
			break;
		}
	}
	
}
