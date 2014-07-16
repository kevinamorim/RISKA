package feup.lpoo.riska.music;

import java.util.ArrayList;

import org.andengine.audio.music.Music;

import android.util.Log;

/**
 * Class that handles all sound related features.
 */
public class Conductor {

	private ArrayList<Music> playlist;
	private ArrayList<String> playlistNames;
	
	private static Conductor instance;
	
	public Conductor() {
		instance = this;
		
		playlist = new ArrayList<Music>();
		playlistNames = new ArrayList<String>();
	}
	
	public static Conductor getSharedInstance() {
		return instance;
	}
	
	/**
	 * Changes the background music.
	 * 
	 * @param music : new background music.
	 */
	public void addMusic(Music music, String name) {
		playlist.add(music);
		playlistNames.add(name);
	}
	
	/**
	 * Plays the current music.
	 */
	public void play(String musicName)
	{
		if(playlistNames.contains(musicName))
		{
			for(int i = 0; i < playlistNames.size(); i++)
			{
				if(playlistNames.get(i).equals(musicName))
				{
					Music music = playlist.get(i);
					
					if(!music.isPlaying())
					{
						music.play();
					}
					break;
				}
			}
		}
		else {
			Log.e("Music", "Music requested for playing not in playlist. <" + musicName + ">");
		}
	}
	
	/**
	 * Plays all musics (not recomended with more than one music).
	 */
	public void play()
	{
		for(Music music : playlist)
		{
			if(!music.isPlaying())
			{
				music.play();
			}
			//break;
		}
	}

	/**
	 * Pauses a music.
	 */
	public void pause(String musicName)
	{
		if(playlistNames.contains(musicName))
		{
			for(int i = 0; i < playlistNames.size(); i++)
			{
				if(playlistNames.get(i).equals(musicName))
				{
					Music music = playlist.get(i);
					
					if(music.isPlaying())
					{
						music.pause();
					}
					break;
				}
			}
		}
		else {
			Log.e("Music", "Music requested for pausing not in playlist. <" + musicName + ">");
		}
	}

	/**
	 * Pauses all music.
	 */
	public void pause()
	{
		for(Music m : playlist)
		{
			m.pause();
		}
	}

	/**
	 * Sets the loop parameter for a music.
	 * 
	 * @param music : music to change
	 * @param value : loop value to set
	 */
	public void setLooping(String musicName, boolean value)
	{
		if(playlistNames.contains(musicName))
		{
			for(int i = 0; i < playlistNames.size(); i++)
			{
				if(playlistNames.get(i).equals(musicName))
				{
					Music music = playlist.get(i);
					
					music.setLooping(value);
					break;
				}
			}
		}
		else {
			Log.e("Music", "Music requested for looping not in playlist. <" + musicName + ">");
		}
	}
	
	/**
	 * Sets the loop parameter for the current music.
	 * 
	 * @param value : loop value to set
	 */
	public void setLooping(boolean value)
	{
		for(Music m : playlist)
		{
			m.setLooping(value);
		}
	}
	
	/**
	 * Sets the loop parameter for the current music.
	 * 
	 * @param value : loop value to set
	 */
	public boolean isMusicPlaying()
	{
		for(Music m : playlist)
		{
			if(m.isPlaying()) {
				return true;
			}
		}
		
		return false;
	}
}
