package feup.lpoo.riska.sound;

import android.content.Context;

import org.andengine.audio.music.*;

import java.io.IOException;

public class RiskaMusic {

    public String name;
    Music music;

    public RiskaMusic(String pName, String pPath, MusicManager pManager, Context pActivity) {
        this.name = pName;

        try {
            this.music =  MusicFactory.createMusicFromAsset(pManager, pActivity, pPath);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
