package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import elementos.BloqueMovil;
import elementos.Jugador;
import elementos.Jugador.TipoJugador;
import utiles.Recursos;
import utiles.Render;
import utiles.MapaHelper;

public class PantallaNivel3 extends PantallaNivelBase implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    public Jugador jugadorLuna;
    public Jugador jugadorSol;
    private Music musicaNivel;

    private Array<Rectangle> colisiones;
    private Array<Rectangle> peligroLava;
    private Array<Rectangle> peligroAgua;
    private Array<Rectangle> puertaLuna;
    private Array<Rectangle> puertaSol;

    private Array<BloqueMovil> bloquesMoviles;

    @Override
    public void show() {
        // Cargar mapa
        mapa = new TmxMapLoader().load(Recursos.MENUNIVEL3);
        mapRenderer = new OrthogonalTiledMapRenderer(mapa);

        // --- ASIGNAR EL NÚMERO DE NIVEL ACTUAL ---
        numeroNivel = 3;

        // Configurar cámara
        camera = configurarCamara(mapa);

        // Crear jugadores
        jugadorLuna = new Jugador(150, 100, TipoJugador.AGUA);
        jugadorSol = new Jugador(250, 100, TipoJugador.FUEGO);

        // Cargar objetos del mapa
        colisiones = MapaHelper.cargarRectangulos(mapa, "Colisiones");
        peligroLava = MapaHelper.cargarRectangulos(mapa, "PeligroLava");
        peligroAgua = MapaHelper.cargarRectangulos(mapa, "PeligroAgua");
        estrellas = MapaHelper.cargarRectangulos(mapa, "Estrellas");
        puertaLuna = MapaHelper.cargarRectangulos(mapa, "PuertaLuna");
        puertaSol = MapaHelper.cargarRectangulos(mapa, "PuertaSol");

        // Cargar bloques móviles
        bloquesMoviles = new Array<>();
        Array<Rectangle> bloquesData = MapaHelper.cargarRectangulos(mapa, "BloquesMoviles");
        for (Rectangle rect : bloquesData) {
            bloquesMoviles.add(new BloqueMovil(rect.x, rect.y, rect.width, rect.height));
        }

        System.out.println("Nivel 3 cargado:");
        System.out.println("- Estrellas: " + estrellas.size);
        System.out.println("- Bloques móviles: " + bloquesMoviles.size);
        System.out.println("- Peligros lava: " + peligroLava.size);
        System.out.println("- Peligros agua: " + peligroAgua.size);

        // Timer (75 segundos para nivel 3)
        timer.setTiempoRestante(75.0f);

        // Música
        musicaNivel = Gdx.audio.newMusic(Gdx.files.internal("musicanivel1.mp3"));
        musicaNivel.setLooping(true);
        musicaNivel.setVolume(0.6f);
        musicaNivel.play();
    }

    @Override
    protected void updateNivel(float delta) {
        // Crear array combinado de colisiones (estáticas + bloques móviles)
        Array<Rectangle> todasLasColisiones = new Array<>();
        todasLasColisiones.addAll(colisiones);
        for (BloqueMovil bloque : bloquesMoviles) {
            todasLasColisiones.add(bloque.getHitbox());
        }

        // Actualizar jugadores
        jugadorLuna.update(delta, todasLasColisiones);
        jugadorSol.update(delta, todasLasColisiones);

        // Actualizar bloques móviles
        for (BloqueMovil bloque : bloquesMoviles) {
            bloque.update(delta, colisiones, jugadorLuna, jugadorSol);
        }

        // ===== USAR MÉTODOS DE LA CLASE BASE =====
        manejarRecoleccionEstrellas(jugadorLuna, jugadorSol);
        if (verificarPeligros(jugadorLuna, jugadorSol, peligroLava, peligroAgua)) return;
        if (verificarVictoria(jugadorLuna, jugadorSol, puertaLuna, puertaSol)) return;

        // Dibujar mapa y objetos
        Render.limpiarPantalla(0, 0, 0);
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dibujar estrellas (método de la clase base)
        dibujarEstrellas();

        // Dibujar bloques móviles
        for (BloqueMovil bloque : bloquesMoviles) {
            bloque.dibujar(batch);
        }

        // Dibujar jugadores
        jugadorLuna.dibujar(batch);
        jugadorSol.dibujar(batch);

        batch.end();
    }

    @Override
    protected OrthographicCamera getCamera() {
        return camera;
    }

    // AGREGAR estos métodos en su lugar:
    @Override
    protected Jugador getJugadorLuna() {
        return jugadorLuna;
    }

    @Override
    protected Jugador getJugadorSol() {
        return jugadorSol;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        super.pause();
        if (musicaNivel != null) musicaNivel.pause();
    }

    @Override
    public void resume() {
        super.resume();
        if (musicaNivel != null) musicaNivel.play();
    }

    @Override
    public void hide() {
        if (musicaNivel != null) musicaNivel.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        mapa.dispose();
        mapRenderer.dispose();
        jugadorLuna.dispose();
        jugadorSol.dispose();

        for (BloqueMovil bloque : bloquesMoviles) {
            bloque.dispose();
        }

        if (musicaNivel != null) musicaNivel.dispose();
    }
}
