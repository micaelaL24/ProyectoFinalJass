package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import online.ClientNetworkListener;
import online.ClientThread;
import utiles.Render;

public class PantallaClienteConectando implements Screen, ClientNetworkListener {

    private int nivelDestino;

    public PantallaClienteConectando(int nivel) {
        this.nivelDestino = nivel;
    }


    private Texture imagenConectando;
    private ClientThread client;

    private boolean startGame = false;
    private int myPlayerNum = -1;

    @Override
    public void show() {

        imagenConectando = new Texture("conectando.png");

        client = new ClientThread(this);
        client.start();

        System.out.println("[CLIENT] Cliente inicializado. Conectando al servidor...");
    }

    @Override
    public void render(float delta) {

        if (startGame && myPlayerNum != -1) {

            Screen nuevaPantalla;

            switch (nivelDestino) {
                case 1:
                    nuevaPantalla = new PantallaNivel1Cliente(client, myPlayerNum);
                    break;

                case 2:
                    nuevaPantalla = new PantallaNivel2Cliente(client, myPlayerNum);
                    break;

                case 3:
                    nuevaPantalla = new PantallaNivel3Cliente(client, myPlayerNum);
                    break;

                case 4:
                    nuevaPantalla = new PantallaNivel4Cliente(client, myPlayerNum);
                    break;

                case 5:
                    nuevaPantalla = new PantallaNivel5Cliente(client, myPlayerNum);
                    break;

                case 6:
                    nuevaPantalla = new PantallaNivel6Cliente(client, myPlayerNum);
                    break;

                case 7:
                    nuevaPantalla = new PantallaNivel7Cliente(client, myPlayerNum);
                    break;

                case 8:
                    nuevaPantalla = new PantallaNivel8Cliente(client, myPlayerNum);
                    break;

                case 9:
                    nuevaPantalla = new PantallaNivel9Cliente(client, myPlayerNum);
                    break;

                case 10:
                    nuevaPantalla = new PantallaNivel10Cliente(client, myPlayerNum);
                    break;

                default:
                    throw new RuntimeException("Nivel no implementado: " + nivelDestino);
            }

            ((ClientNetworkListener)nuevaPantalla).onStart();
            Render.app.setScreen(nuevaPantalla);
        }



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Render.batch.begin();
        Render.batch.draw(
            imagenConectando,
            0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        Render.batch.end();
    }

    // ========================= NETWORK EVENTS ==========================

    @Override
    public void onConnected(int playerNum) {
        System.out.println("[CLIENT] Asignado playerNum=" + playerNum);
        myPlayerNum = playerNum;
    }

    @Override
    public void onStart() {
        System.out.println("[CLIENT] RECIBIDO START");
        startGame = true;
    }

    @Override
    public void onFull() {
        System.out.println("[CLIENT] Servidor lleno (FULL)");
    }

    @Override
    public void onUpdatePos(int playerNum, float x, float y, boolean mirandoDerecha) {
        // NO hacemos nada aquí.
        // Esto se gestiona en PantallaNivel1Cliente.
    }


    @Override
    public void onDisconnect() {
        System.out.println("[CLIENT] Desconectado del servidor");
    }

    @Override
    public void onPauseGame() {

    }

    @Override
    public void onResumeGame() {

    }

    // ==================================================================

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        if (imagenConectando != null) imagenConectando.dispose();
    }
}
