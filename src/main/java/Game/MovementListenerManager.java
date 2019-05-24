package Game;

import player.HumanPlayer.HumanPlayer;

public interface MovementListenerManager {
    void addListenerForPlayer(HumanPlayer player);
    void removeListenerForPlayer(HumanPlayer player);
}
