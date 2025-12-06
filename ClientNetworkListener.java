package online;

public interface ClientNetworkListener {

    void onConnected(int playerNum);
    void onFull();
    void onStart();

    void onUpdatePos(int playerNum, float x, float y, boolean mirandoDerecha);

    void onDisconnect();

    void onPauseGame();
    void onResumeGame();
}
