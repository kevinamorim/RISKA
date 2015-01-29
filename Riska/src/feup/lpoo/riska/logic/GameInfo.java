package feup.lpoo.riska.logic;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.resources.ResourceManager;

public class GameInfo
{
    public static boolean isValidSoundContext(String x) {
        return (x == "menu" || x == "game" || x == "battle");
    }
}
