package feup.lpoo.riska.sound;

import android.content.Context;

import org.andengine.audio.sound.*;

import java.io.IOException;

public class RiskaSound {

    public String name;
    Sound sound;

    public RiskaSound(String pName, String pPath, SoundManager pManager, Context pActivity) {
        this.name = pName;

        try {
            this.sound =  SoundFactory.createSoundFromAsset(pManager, pActivity, pPath);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
