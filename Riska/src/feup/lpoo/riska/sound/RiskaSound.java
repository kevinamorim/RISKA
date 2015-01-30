package feup.lpoo.riska.sound;

import android.content.Context;

import org.andengine.audio.sound.*;
import org.andengine.util.debug.Debug;

import java.io.IOException;

import feup.lpoo.riska.interfaces.Audio;
import feup.lpoo.riska.interfaces.Debuggable;
import feup.lpoo.riska.logic.MainActivity;

public class RiskaSound implements Audio,Debuggable {

    public String name;
    Sound sound;

    public RiskaSound(String pName, String pPath) {
        this.name = pName;

        try {
            this.sound =  SoundFactory.createSoundFromAsset(MainActivity.instance.getSoundManager(), MainActivity.instance, pPath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        sound.play();
    }

    @Override
    public void pause() {
        sound.pause();
    }

    @Override
    public void resume() {
        sound.resume();
    }

    @Override
    public void stop() {
        sound.stop();
    }

    @Override
    public void restart() {
        sound.stop();
        sound.play();
    }

    @Override
    public void setLooping(boolean val) {
        sound.setLooping(val);
    }

    @Override
    public void release() {
        sound.release();
    }

    @Override
    public void setVolume(final float pLeft, final float pRight) {
        sound.setVolume(pLeft, pRight);
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Sounds", "[Class=RiskaSound]");
        Debug.d("Sounds", "    " + debugInfo);
    }
}
