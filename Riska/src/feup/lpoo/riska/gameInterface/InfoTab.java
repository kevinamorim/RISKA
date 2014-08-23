package feup.lpoo.riska.gameInterface;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.R;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.utilities.Utils;

public class InfoTab {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final float BOUNDING_FACTOR = 0.9f;

	// ======================================================
	// FIELDS
	// ======================================================
	private Sprite sprite;
	private Text text;


	// ======================================================
	// CONSTRUCTOR
	// ======================================================
	public InfoTab(ITiledTextureRegion pSprite, Font pFont, 
			float pWidth, float pHeight, float pX, float pY, float pAlpha, VertexBufferObjectManager vbom) {

		createSprite(pSprite, pWidth, pHeight, pX, pY, pAlpha, vbom);
		createText(pFont, vbom);

	}

	// ======================================================
	// GETTERS & SETTERS 
	// ======================================================
	public Sprite getSprite() {
		return sprite;
	}

	public void setText(String pString) {
		text.setText(pString);
		Utils.wrap(text, sprite, BOUNDING_FACTOR);
	}

	public void setText(GameLogic logic) {
		switch(logic.getState()) {
		case SETUP:
			setupText(logic);
			break;
		case ATTACK:
			attackText(logic);
			break;
		case MOVE:
			moveText(logic);
			break;
		case DEPLOYMENT:
			deploymentText(logic);
			break;
		case ENDTURN:
			endTurnText(logic);
			break;
		default:
			defaultText();
			break;
		}
	}

	// ======================================================
	// LOGIC
	// ======================================================
	private void createSprite(ITiledTextureRegion pSprite, 
			float pWidth, float pHeight, float pX, float pY, float pAlpha, VertexBufferObjectManager vbom) {

		sprite = new Sprite(0, 0, pSprite, vbom);

		sprite.setSize(pWidth, pHeight);
		sprite.setPosition(pX, pY);
		sprite.setAlpha(pAlpha);

	}

	private void createText(Font pFont, VertexBufferObjectManager vbom) {

		text = new Text(0, 0, pFont, "", 1000, vbom);

		Utils.wrap(text, sprite, BOUNDING_FACTOR);
		text.setPosition(0.5f * sprite.getWidth(), 0.5f * sprite.getHeight());
		text.setColor(Color.BLACK);
		sprite.attachChild(text);

	}
	
	private void setupText(GameLogic logic) {
		deploymentText(logic);
	}
	
	private void attackText(GameLogic logic) {
		
		if(logic.getCurrentPlayer().isCPU) {
			setText(Utils.getString(R.string.game_info_cpu_attack));
			return;
		}
		
		if(logic.selectedRegion != null && logic.targetedRegion != null)
		{
			setText(Utils.getString(R.string.game_info_attack));
		}
		else if(logic.selectedRegion != null)
		{
			setText(Utils.getString(R.string.game_info_tap_enemy_region));
		}
		else
		{
			setText(Utils.getString(R.string.game_info_tap_allied_region));
		}
	}
	
	private void moveText(GameLogic logic) {
		
		if(logic.getCurrentPlayer().isCPU) {
			setText(Utils.getString(R.string.game_info_cpu_move));
			return;
		}
		
		if(logic.selectedRegion != null && logic.targetedRegion != null)
		{
			setText(Utils.getString(R.string.game_info_confirm_moving));
		}
		else if(logic.selectedRegion != null)
		{
			setText(Utils.getString(R.string.game_info_choose_dest_move_troops));
		}
		else
		{
			setText(Utils.getString(R.string.game_info_choose_src_move_troops));
		}
	}
	
	private void deploymentText(GameLogic logic) {
		setText(logic.getCurrentPlayer().soldiersToDeploy + Utils.getString(R.string.game_info_left_to_deploy));
	}
	
	private void endTurnText(GameLogic logic) {
		setText(Utils.getString(R.string.game_info_end_turn));
	}
	
	private void defaultText() {
		setText(Utils.getString(R.string.game_info_default));
	}





}
