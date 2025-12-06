package online;


public interface GameControllerOnline {

    void playerConnected(int playerNum);

    void playerDisconnected(int playerNum);

    void movePlayer(int playerNum, int dirX);

    void jumpPlayer(int playerNum);

    void startGame();

    /**
     * Posición que llega desde la red de un jugador remoto.
     * En nuestro caso el cliente manda la posición de FUEGO (player 2).
     */
    void updateRemotePosition(int playerNum, float x, float y);
}
