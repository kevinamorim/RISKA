package feup.lpoo.riska;

import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

public class SplashScene extends Scene {
	
	MainActivity activity;
	
	Text title, description;
	
	public SplashScene() {
		
		setBackground(new Background(0.3333f, 0.4196f, 0.1843f));
		activity = MainActivity.getSharedInstance();
		
//		title = new Text(0, 0, activity.mFont, activity.getString(R.string.title), 
//				activity.getVertexBufferObjectManager());
//		description = new Text(0, 0, activity.mFont, activity.getString(R.string.description),
//				activity.getVertexBufferObjectManager());
//		
//		setText();
		
	}
	
	public void setText() {
		title.setPosition(-title.getWidth(), activity.mCamera.getHeight()/2);
		description.setPosition(-description.getWidth(), 
				activity.mCamera.getHeight()/2 - title.getHeight()*2);
		
		attachChild(title);
		attachChild(description);
		
		title.registerEntityModifier(new MoveXModifier(1, title.getX(), activity.mCamera.getWidth()/2));
		description.registerEntityModifier(new MoveXModifier(1, description.getX(), activity.mCamera.getWidth()/2));
	}

}
