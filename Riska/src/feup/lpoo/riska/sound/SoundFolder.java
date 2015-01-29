package feup.lpoo.riska.sound;


import org.andengine.util.debug.Debug;

import java.util.ArrayList;

import feup.lpoo.riska.interfaces.Debuggable;

public class SoundFolder implements Debuggable {

    public String name;
    private ArrayList<RiskaSound> sounds;

    public SoundFolder(String pName) {
        this.name = pName;

        this.sounds = new ArrayList<RiskaSound>();
    }

    public void addSound(RiskaSound sound) {

        if(getSound(sound.name) != null) {
            sounds.add(sound);
        }
        else {
            print("addMusic() : music with name '" + sound.name + "' already exists");
        }
    }

    private RiskaSound getSound(String name) {

        for(RiskaSound sound : sounds) {
            if(sound.name == name) {
                return sound;
            }
        }

        return null;
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Sounds", "[Class=SoundFolder]");
        Debug.d("Sounds", "    " + debugInfo);
    }
}
