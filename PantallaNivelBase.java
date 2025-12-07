package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import elementos.Jugador;
import utiles.*;

public abstract class PantallaNivelBase implements Screen {

    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;

    // HUD
    protected BitmapFont font;
    protected GlyphLayout layout;
    protected Texture estrellaTexture;

    // Imagen de pausa
    protected Texture pausaTexture;

    // Timer
    protected Timer timer;

    // Estrellas recolectables
    protected Array<Rectangle> estrellas;

    // Para evitar que la pausa se dispare muchas veces al mantener apretado
    private boolean pausePressedLastFrame = false;

    protected int numeroNivel;


    public PantallaNivelBase() {
        batch = Render.batch;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();

        estrellaTexture = new Texture(Recursos.ESTRELLA);
        pausaTexture = new Texture("juego en pausa (1).png"); // tu imagen

        timer = new Timer(60f);
        estrellas = new Array<>();
    }

    @Override
    public void render(float delta) {

        // === TOGGLE DE PAUSA MEJORADO ===
        boolean pausePressed = InputManager.isPausePressed();
        if (pausePressed && !pausePressedLastFrame) {
            timer.alternarPausa();
        }
        pausePressedLastFrame = pausePressed;

        // Actualizar timer
        timer.actualizar(delta);

        // Tiempo agotado → Pantalla derrota
        if (timer.estaTerminado()) {
            Render.app.setScreen(new PantallaDerrota());
            return;
        }

        // Si NO está en pausa, actualizar lógica del nivel
        if (!timer.estaPausado()) {
            updateNivel(delta);
        }

        // Si está en pausa, dibujar imagen de pausa
        if (timer.estaPausado()) {
            drawPausa();
        }

        // HUD siempre visible
        drawHud();
    }

    // ===== MÉTODOS ABSTRACTOS =====
    protected abstract void updateNivel(float delta);
    protected abstract OrthographicCamera getCamera();
    protected abstract Jugador getJugadorLuna();
    protected abstract Jugador getJugadorSol();

    protected int getEstrellasRecolectadas() {
        return getJugadorLuna().getEstrellasRecolectadas() +
            getJugadorSol().getEstrellasRecolectadas();
    }

    // ===== MECANICAS COMUNES =====

    protected void manejarRecoleccionEstrellas(Jugador jugadorLuna, Jugador jugadorSol) {
        for (int i = estrellas.size - 1; i >= 0; i--) {
            Rectangle rect = estrellas.get(i);

            if (jugadorLuna.getHitbox().overlaps(rect)) {
                jugadorLuna.sumarEstrella();
                estrellas.removeIndex(i);
            } else if (jugadorSol.getHitbox().overlaps(rect)) {
                jugadorSol.sumarEstrella();
                estrellas.removeIndex(i);
            }
        }
    }

    protected boolean verificarPeligros(Jugador jugadorLuna, Jugador jugadorSol,
                                        Array<Rectangle> peligroLava, Array<Rectangle> peligroAgua) {

        if (peligroLava != null) {
            for (Rectangle rect : peligroLava) {
                if (jugadorLuna.getHitbox().overlaps(rect) && jugadorLuna.estaVivo()) {
                    jugadorLuna.morir();
                    Render.app.setScreen(new PantallaDerrota());
                    return true;
                }
            }
        }

        if (peligroAgua != null) {
            for (Rectangle rect : peligroAgua) {
                if (jugadorSol.getHitbox().overlaps(rect) && jugadorSol.estaVivo()) {
                    jugadorSol.morir();
                    Render.app.setScreen(new PantallaDerrota());
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean verificarPeligrosComunes(Jugador jugadorLuna, Jugador jugadorSol,
                                               Array<Rectangle> peligros) {
        if (peligros == null) return false;

        for (Rectangle rect : peligros) {
            if (jugadorLuna.getHitbox().overlaps(rect) && jugadorLuna.estaVivo()) {
                jugadorLuna.morir();
                Render.app.setScreen(new PantallaDerrota());
                return true;
            }
            if (jugadorSol.getHitbox().overlaps(rect) && jugadorSol.estaVivo()) {
                jugadorSol.morir();
                Render.app.setScreen(new PantallaDerrota());
                return true;
            }
        }

        return false;
    }

    protected boolean verificarVictoria(Jugador luna, Jugador sol,
                                        Array<Rectangle> puertasLuna, Array<Rectangle> puertasSol) {

        boolean lunaOK = false;
        boolean solOK = false;

        if (puertasLuna != null) {
            for (Rectangle r : puertasLuna) {
                if (luna.getHitbox().overlaps(r)) { lunaOK = true; break; }
            }
        }

        if (puertasSol != null) {
            for (Rectangle r : puertasSol) {
                if (sol.getHitbox().overlaps(r)) { solOK = true; break; }
            }
        }

        if (lunaOK && solOK) {
            manejarVictoria();
            return true;
        }

        return false;
    }

    /**
     * Lógica que se ejecuta al ganar un nivel:
     * 1. Desbloquea el siguiente nivel usando ProgresoJuego.
     * 2. Muestra la pantalla de victoria.
     */
    protected void manejarVictoria() {
        // El siguiente nivel es el actual (numeroNivel) + 1.
        int siguienteNivel = numeroNivel + 1;

        // Desbloquea el siguiente nivel usando tu clase utilidad
        ProgresoJuego.desbloquearNivel(siguienteNivel);

        // Cambiar a la pantalla de victoria
        Render.app.setScreen(new PantallaVictoria());
    }

    protected void dibujarEstrellas() {
        for (Rectangle rect : estrellas) {
            batch.draw(estrellaTexture, rect.x, rect.y);
        }
    }

    protected OrthographicCamera configurarCamara(TiledMap mapa) {
        int mapWidth = mapa.getProperties().get("width", Integer.class)
            * mapa.getProperties().get("tilewidth", Integer.class);
        int mapHeight = mapa.getProperties().get("height", Integer.class)
            * mapa.getProperties().get("tileheight", Integer.class);

        OrthographicCamera cam = new OrthographicCamera();

        float screenRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        float mapRatio = (float) mapWidth / mapHeight;

        if (mapRatio > screenRatio) {
            cam.viewportWidth = mapWidth;
            cam.viewportHeight = mapWidth / screenRatio;
        } else {
            cam.viewportHeight = mapHeight;
            cam.viewportWidth = mapHeight * screenRatio;
        }

        cam.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        cam.update();

        return cam;
    }

    // ===== HUD =====

    protected void drawHud() {
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();

        float hudX = getCamera().position.x - getCamera().viewportWidth / 2 + 20;
        float hudY = getCamera().position.y + getCamera().viewportHeight / 2 - 20;

        float iconSize = 64;
        font.getData().setScale(2f);

        batch.draw(estrellaTexture, hudX, hudY - iconSize, iconSize, iconSize);

        String estrellasTexto = "x " + getEstrellasRecolectadas();
        layout.setText(font, estrellasTexto);
        font.draw(batch, estrellasTexto,
            hudX + iconSize + 15,
            hudY - iconSize / 2 + layout.height / 2);

        String timerTexto = "Tiempo: " + (int) timer.getTiempoRestante();
        layout.setText(font, timerTexto);
        font.draw(batch, timerTexto,
            getCamera().position.x + getCamera().viewportWidth / 2 - layout.width - 20,
            hudY - iconSize / 2 + layout.height / 2);

        batch.end();
    }

    // ===== IMAGEN DE PAUSA =====

    protected void drawPausa() {
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();

        float w = getCamera().viewportWidth;
        float h = getCamera().viewportHeight;

        batch.draw(
            pausaTexture,
            getCamera().position.x - w / 2,
            getCamera().position.y - h / 2,
            w, h
        );

        batch.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        estrellaTexture.dispose();
        pausaTexture.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() { timer.pausar(); }
    @Override public void resume() { timer.despausar(); }
    @Override public void hide() {}
}
