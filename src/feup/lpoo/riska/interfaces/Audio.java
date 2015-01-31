package feup.lpoo.riska.interfaces;

public interface Audio {

    public void play();

    public void pause();

    public void resume();

    public void stop();

    public void restart();

    public void setLooping(boolean val);

    public void release();

    public void setVolume(final float pLeft, final float pRight);

}
