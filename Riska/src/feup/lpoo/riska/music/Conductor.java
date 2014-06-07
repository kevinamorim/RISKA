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
	
	public void setBackgroundMusic(Music music) {
		this.backgroundMusic = music;
	}
	
	public void setCurrentMusic(Music music) {
		
		if(!(currentMusic == null)) {
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
		play();
	}
	
	public static Conductor getSharedInstance() {
		return instance;
	}
	
}
