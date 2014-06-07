package feup.lpoo.riska.music;

import org.andengine.audio.music.Music;

public class Conductor {

	private Music backgroundMusic;
	
	private Music currentMusic;
	
	static Conductor instance;
	
	public Conductor() {
		instance = this;	
		
		this.currentMusic = null;
	}
	
	public static Conductor getSharedInstance() {
		return instance;
	}
	
	
	public void setBackgroundMusic(Music music) {
		this.backgroundMusic = music;
	}
	
	public void setCurrentMusic(Music music) {
		
		if(currentMusic != null) {
			if(currentMusic.isPlaying()) {
				currentMusic.stop();
			}
		}
		
		this.currentMusic = music;
	}
	
	public void play() {
		if(!currentMusic.isPlaying()) {
			currentMusic.play();
		}
	}
	
	public void pause() {
		if(currentMusic.isPlaying()) {
			currentMusic.pause();
		}
	}
	
	public void playBackgroundMusic() {
		setCurrentMusic(backgroundMusic);
		setLooping(true);
		play();
	}
	
	public void setLooping(boolean value) {
		if(currentMusic != null) {
			currentMusic.setLooping(value);
		}
	}
}
