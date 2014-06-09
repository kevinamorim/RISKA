package feup.lpoo.riska.music;

import java.util.ArrayList;

import org.andengine.audio.music.Music;

/**
 * Class that handles all sound related features.
 */
public class Conductor {

	private Music backgroundMusic;
	private Music currentMusic;
	
	private ArrayList<Music> playlist;
	
	static Conductor instance;
	
	public Conductor() {
		instance = this;	
		
		this.currentMusic = null;
		
		playlist = new ArrayList<Music>();
	}
	
	public static Conductor getSharedInstance() {
		return instance;
	}
	
	/**
	 * Changes the background music.
	 * 
	 * @param music : new background music.
	 */
	public void setBackgroundMusic(Music music) {
		this.backgroundMusic = music;
	}
	
	/**
	 * Sets the current music to play.
	 * 
	 * @param music : new music to play
	 */
	public void setCurrentMusic(Music music) {
		
		if(currentMusic != null) {
			if(currentMusic.isPlaying()) {
				currentMusic.stop();
			}
		}
		
		this.currentMusic = music;
	}
	
	/**
	 * Plays the current music.
	 */
	public void play() {
		if(!currentMusic.isPlaying()) {
			currentMusic.play();
		}
	}
	
	/**
	 * Pauses the current music.
	 */
	public void pause() {
		if(currentMusic.isPlaying()) {
			currentMusic.pause();
		}
	}
	
	/**
	 * Plays the background music;
	 */
	public void playBackgroundMusic() {
		setCurrentMusic(backgroundMusic);
		setLooping(true);
		play();
	}
	
	/**
	 * Sets the loop parameter for the current music.
	 * 
	 * @param value : loop value to set
	 */
	public void setLooping(boolean value) {
		if(currentMusic != null) {
			currentMusic.setLooping(value);
		}
	}
	
	public boolean isMusicPlaying() {
		return currentMusic.isPlaying();
	}
}
