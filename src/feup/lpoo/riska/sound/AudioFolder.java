package feup.lpoo.riska.sound;

import org.andengine.util.debug.Debug;

import java.util.ArrayList;

import feup.lpoo.riska.interfaces.Debuggable;

public class AudioFolder implements Debuggable {

    public String name;
    private ArrayList<RiskaMusic> musics;
    private ArrayList<RiskaSound> sounds;

    public AudioFolder(String pName) {
        this.name = pName;

        this.musics = new ArrayList<RiskaMusic>();
        this.sounds = new ArrayList<RiskaSound>();
    }

    public void addMusic(RiskaMusic music) {

        if(getMusic(music.name) != null) {
            musics.add(music);
        }
        else {
            print("addMusic() : music with name '" + music.name + "' already exists");
        }
    }

    public void addSound(RiskaSound sound) {

        if(getSound(sound.name) != null) {
            sounds.add(sound);
        }
        else {
            print("addSound() : sound with name '" + sound.name + "' already exists");
        }
    }

    public RiskaMusic getMusic(String name) {

        for(RiskaMusic music : musics) {
            if(music.name == name) {
                return music;
            }
        }

        return null;
    }

    public RiskaSound getSound(String name) {

        for(RiskaSound sound : sounds) {
            if(sound.name == name) {
                return sound;
            }
        }

        return null;
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Sounds", "[Class=AudioFolder]");
        Debug.d("Sounds", "    " + debugInfo);
    }
}
