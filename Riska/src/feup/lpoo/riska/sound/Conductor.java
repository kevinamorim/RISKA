package feup.lpoo.riska.sound;

import java.util.ArrayList;

import org.andengine.util.debug.Debug;

import feup.lpoo.riska.interfaces.Debuggable;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.utilities.Utils;

public class Conductor implements Debuggable {

    // ==================================================
    // FIELDS
    // ==================================================
    private static ArrayList<AudioFolder> audioFolders = new ArrayList<AudioFolder>();

    private AudioFolder activeAudioFolder;

    // ==================================================
    // SINGLETON
    // ==================================================
    public static Conductor instance = new Conductor();

    // ==================================================
    // METHODS
    // ==================================================
	private Conductor() {

        this.activeAudioFolder = null;
    }

    /**
     * Should be set active whenever a context changes (generally, a "top" scene,
     *      eg: menu->game or vice-versa).
     *
     * @param pContext : Context of the conductor (menu, game, battle, etc...)
     */
    public void setContext(String pContext) {
        if(GameInfo.isValidSoundContext(pContext)) {
            setActiveFolder(pContext);
        }
        else {
            print("setContext() : context '" + pContext + "' is not valid");
        }
    }

    private void setActiveFolder(String name) {
        AudioFolder fdAudio = getAudioFolder(name);

        if(fdAudio != null) {
            activeAudioFolder = fdAudio;
        }
        else {
            print("setActiveFolder() : audio folder '" + name + "' does not exist");
        }
    }

    public void createAudioFolder(String name) {
        if(getAudioFolder(name) != null) {
            audioFolders.add(new AudioFolder(name));
        }
        else {
            print("createAudioFolder() : audio folder with name '" + name + "' already exists");
        }
    }

    public void addMusicToFolder(String folderName, RiskaMusic music) {
        AudioFolder fd = getAudioFolder(folderName);

        if(fd != null) {
            fd.addMusic(music);
        }
        else {
            print("addMusicToFolder() : music folder '" + folderName + "' does not exist");
        }
    }

    public void addSoundToFolder(String folderName, RiskaSound sound) {
        AudioFolder fd = getAudioFolder(folderName);

        if(fd != null) {
            fd.addSound(sound);
        }
        else {
            print("addSoundToFolder() : sound folder '" + folderName + "' does not exist");
        }
    }

    private AudioFolder getAudioFolder(String name) {

        for(AudioFolder folder : audioFolders) {
            if(folder.name == name) {
                return folder;
            }
        }

        return null;
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Sounds", "[Class=Conductor]");
        Debug.d("Sounds", "    " + debugInfo);
    }
}
