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
	
	private static Conductor instance = new Conductor();
	
	public Conductor()
	{
		instance = this;
		
		playlist = new ArrayList<Music>();
		playlistNames = new ArrayList<String>();
	}
	
	public static Conductor getSharedInstance()
	{
		return instance;
	}

	public void addMusic(Music music, String name)
	{
		if(music != null)
		{
			playlist.add(music);
			playlistNames.add(name);
		}
	}

	public void playMusic(String musicName)
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

	public void playAll()
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

	public void pauseMusic(String musicName)
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

	public void pauseAll()
	{
		for(Music m : playlist)
		{
			m.pause();
		}
	}

	public void setLoopingMusic(String musicName, boolean value)
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
		else
		{
			Log.e("Music", "Music requested for looping not in playlist. <" + musicName + ">");
		}
	}

	public void setLoopingAll(boolean value)
	{
		for(Music m : playlist)
		{
			m.setLooping(value);
		}
	}

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
