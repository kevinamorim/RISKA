package feup.lpoo.riska.sound;

import org.andengine.audio.music.*;
import org.andengine.util.debug.Debug;

import java.io.IOException;

import feup.lpoo.riska.interfaces.Audio;
import feup.lpoo.riska.interfaces.Debuggable;
import feup.lpoo.riska.logic.MainActivity;

public class RiskaMusic implements Audio,Debuggable {

    public String name;
    Music music;

    public RiskaMusic(String pName, String pPath) {
        this.name = pName;

        try {
            this.music =  MusicFactory.createMusicFromAsset(MainActivity.instance.getMusicManager(), MainActivity.instance, pPath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {

        if(!music.isPlaying()) {
            music.play();
        } else {
            print("play() : music is already playing");
        }
    }

    @Override
    public void pause() {

        if(music.isPlaying()) {
            music.pause();
        } else {
            print("pause() : music is not playing");
        }
    }

    @Override
    public void resume() {

        if(!music.isPlaying()) {
            music.resume();
        } else {
            print("resume() : music is playing");
        }
    }

    @Override
    public void stop() {

        if(music.isPlaying()) {
            music.stop();
        } else {
            print("stop() : music is not playing");
        }
    }

    @Override
    public void restart() {
        music.stop();
        music.play();
    }

    public boolean isPlaying() {
        return music.isPlaying();
    }

    public void seekTo(int mili) {
        music.seekTo(mili);
    }

    @Override
    public void setLooping(boolean val) {
        music.setLooping(val);
    }

    @Override
    public void release() {
        music.release();
    }

    @Override
    public void setVolume(final float pLeft, final float pRight) {
        music.setVolume(pLeft, pRight);
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Sounds", "[Class=RiskaMusic]");
        Debug.d("Sounds", "    " + debugInfo);
    }
}
