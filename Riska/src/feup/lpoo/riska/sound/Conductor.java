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
    private static ArrayList<MusicFolder> musicFolders = new ArrayList<MusicFolder>();
    private static ArrayList<SoundFolder> soundFolders = new ArrayList<SoundFolder>();

    private MusicFolder activeMusicFolder;
    private SoundFolder activeSoundFolder;

    // ==================================================
    // SINGLETON
    // ==================================================
    public static Conductor instance = new Conductor();

    // ==================================================
    // METHODS
    // ==================================================
	private Conductor() {

        this.activeMusicFolder = null;
        this.activeSoundFolder = null;
    }

    /**
     * Should be set active whenever a context changes (generally, a "top" scene,
     *      eg: menu->game or vice-versa).
     *
     * @param pContext
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
        MusicFolder fdMusic = getMusicFolder(name);
        SoundFolder fdSound = getSoundFolder(name);

        if(fdMusic != null && fdSound != null) {
            activeMusicFolder = fdMusic;
            activeSoundFolder = fdSound;
        }
        else {
            if(fdMusic == null) {
                print("setActiveFolder() : music folder '" + name + "' does not exist");
            }
            if(fdSound == null) {
                print("setActiveFolder() : sound folder '" + name + "' does not exist");
            }
        }
    }

    public void createMusicFolder(String name) {
        if(getMusicFolder(name) != null) {
            musicFolders.add(new MusicFolder(name));
        }
        else {
            print("createMusicFolder() : music folder with name '" + name + "' already exists");
        }
    }

    public void createSoundFolder(String name) {
        if(getSoundFolder(name) != null) {
            soundFolders.add(new SoundFolder(name));
        }
        else {
            print("createSoundFolder() : sound folder with name '" + name + "' already exists");
        }
    }

    public void addMusicToFolder(String folderName, RiskaMusic music) {
        MusicFolder fd = getMusicFolder(folderName);

        if(fd != null) {
            fd.addMusic(music);
        }
        else {
            print("addMusicToFolder() : music folder '" + folderName + "' does not exist");
        }
    }

    public void addSoundToFolder(String folderName, RiskaSound sound) {
        SoundFolder fd = getSoundFolder(folderName);

        if(fd != null) {
            fd.addSound(sound);
        }
        else {
            print("addSoundToFolder() : sound folder '" + folderName + "' does not exist");
        }
    }

    private MusicFolder getMusicFolder(String name) {

        for(MusicFolder folder : musicFolders) {
            if(folder.name == name) {
                return folder;
            }
        }

        return null;
    }

    private SoundFolder getSoundFolder(String name) {

        for(SoundFolder folder : soundFolders) {
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
