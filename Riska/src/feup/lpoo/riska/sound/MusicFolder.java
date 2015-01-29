package feup.lpoo.riska.sound;

import org.andengine.util.debug.Debug;

import java.util.ArrayList;

import feup.lpoo.riska.interfaces.Debuggable;

public class MusicFolder implements Debuggable {

    public String name;
    private ArrayList<RiskaMusic> musics;

    public MusicFolder(String pName) {
        this.name = pName;

        this.musics = new ArrayList<RiskaMusic>();
    }

    public void addMusic(RiskaMusic music) {

        if(getMusic(music.name) != null) {
            musics.add(music);
        }
        else {
            print("addMusic() : music with name '" + music.name + "' already exists");
        }
    }

    private RiskaMusic getMusic(String name) {

        for(RiskaMusic music : musics) {
            if(music.name == name) {
                return music;
            }
        }

        return null;
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Sounds", "[Class=MusicFolder]");
        Debug.d("Sounds", "    " + debugInfo);
    }
}
