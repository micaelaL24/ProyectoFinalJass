package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import online.ClientNetworkListener;
import online.ClientThread;
import utiles.InputManager;
import utiles.Render;

public class PantallaNivel7Cliente extends PantallaNivel7 implements ClientNetworkListener {

    private ClientThread clientThread;
    private int myPlayerNum;

    private Texture imagenConectando;
    private boolean gameStarted = false;

    public PantallaNivel7Cliente(ClientThread client, int playerNum) {
        super();
        this.clientThread = client;
        this.myPlayerNum = playerNum;
    }

    @Override
    public void show() {
        super.show();
        imagenConectando = new Texture("conectando.png");
        clientThread.setListener(this);
    }

    @Override
    public void render(float delta) {

        if (!gameStarted) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            Render.batch.begin();
            Render.batch.draw(imagenConectando,
                0,0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
            Render.batch.end();
            return;
        }

        // ===== PAUSA SINCRONIZADA =====
        if (InputManager.isPausePressed()) {
            if (!timer.estaPausado()) {
                timer.pausar();
                clientThread.sendMessage("Pause");
            } else {
                timer.despausar();
                clientThread.sendMessage("Resume");
            }
        }

        // ===== Movimiento SOLO del jugador local =====
        if (!timer.estaPausado()) {
            if (myPlayerNum == 1)
                jugadorLuna.update(delta, getColisiones());
            else
                jugadorSol.update(delta, getColisiones());
        }

        // ===== dibuja todo con la lógica del padre =====
        super.render(delta);

        // ===== Enviar posición + orientación =====
        if (!timer.estaPausado()) {
            if (myPlayerNum == 1)
                clientThread.sendPosition(
                    jugadorLuna.getX(),
                    jugadorLuna.getY(),
                    jugadorLuna.isMirandoDerecha()
                );
            else
                clientThread.sendPosition(
                    jugadorSol.getX(),
                    jugadorSol.getY(),
                    jugadorSol.isMirandoDerecha()
                );
        }
    }

    // ============================================================
    //  RESPUESTAS DE RED
    // ============================================================

    @Override
    public void onStart() {
        gameStarted = true;
        timer.despausar();
    }

    @Override
    public void onUpdatePos(int playerNum, float x, float y, boolean mirandoDerecha) {

        if (playerNum == myPlayerNum)
            return;

        if (playerNum == 1) {
            jugadorLuna.setPosition(x, y);
            jugadorLuna.setMirandoDerecha(mirandoDerecha);
        } else {
            jugadorSol.setPosition(x, y);
            jugadorSol.setMirandoDerecha(mirandoDerecha);
        }
    }

    @Override
    public void onPauseGame() {
        timer.pausar();
    }

    @Override
    public void onResumeGame() {
        timer.despausar();
    }

    @Override
    public void hide() {
        System.out.println("[CLIENT] Cerrando PantallaNivel7Cliente → enviando Disconnect");

        if (clientThread != null) {
            clientThread.terminate();
        }
    }

    @Override public void onConnected(int n) {}
    @Override public void onFull() {}
    @Override public void onDisconnect() {}
}
