package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;

import elementos.Jugador;
import utiles.MapaHelper;
import utiles.Recursos;
import utiles.Render;

public class PantallaNivel1 extends PantallaNivelBase {

    protected TiledMap mapa;
    protected OrthogonalTiledMapRenderer mapRenderer;

    protected OrthographicCamera camera;

    protected Jugador jugadorLuna;
    protected Jugador jugadorSol;

    protected Music musicaNivel;

    protected Array<Rectangle> colisiones;
    protected Array<Rectangle> peligroLava;
    protected Array<Rectangle> peligroAgua;
    protected Array<Rectangle> puertaLuna;
    protected Array<Rectangle> puertaSol;

    @Override
    public void show() {

        numeroNivel = 1;

        mapa = new TmxMapLoader().load(Recursos.MENUNIVEL1);
        mapRenderer = new OrthogonalTiledMapRenderer(mapa);

        // ESTE método sí existe en PantallaNivelBase
        camera = configurarCamara(mapa);

        jugadorLuna = new Jugador(100, 400, Jugador.TipoJugador.AGUA);
        jugadorSol = new Jugador(100, 100, Jugador.TipoJugador.FUEGO);

        colisiones = MapaHelper.cargarRectangulos(mapa, "Colisiones");
        peligroLava = MapaHelper.cargarRectangulos(mapa, "PeligroLava");
        peligroAgua = MapaHelper.cargarRectangulos(mapa, "PeligroAgua");
        estrellas = MapaHelper.cargarRectangulos(mapa, "Estrellas");
        puertaLuna = MapaHelper.cargarRectangulos(mapa, "PuertaLuna");
        puertaSol = MapaHelper.cargarRectangulos(mapa, "PuertaSol");

        musicaNivel = Gdx.audio.newMusic(Gdx.files.internal("musicanivel1.mp3"));
        musicaNivel.setLooping(true);
        musicaNivel.setVolume(0.6f);
        musicaNivel.play();
    }

    @Override
    public void render(float delta) {

        // dibujar siempre el nivel
        camera.update();
        Render.limpiarPantalla(0,0,0);

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        dibujarEstrellas();
        jugadorLuna.dibujar(batch);
        jugadorSol.dibujar(batch);
        batch.end();

        super.render(delta); // HUD + timer
    }


    @Override
    protected void updateNivel(float delta) {

        jugadorLuna.update(delta, colisiones);
        jugadorSol.update(delta, colisiones);

        manejarRecoleccionEstrellas(jugadorLuna, jugadorSol);

        // ESTOS 2 métodos SÍ existen en PantallaNivelBase
        if (verificarPeligros(jugadorLuna, jugadorSol, peligroLava, peligroAgua)) return;
        if (verificarVictoria(jugadorLuna, jugadorSol, puertaLuna, puertaSol)) return;
    }


    // GETTERS solicitados por PantallaNivel1Cliente
    public Array<Rectangle> getColisiones() { return colisiones; }

    @Override
    protected OrthographicCamera getCamera() { return camera; }

    @Override
    protected Jugador getJugadorLuna() { return jugadorLuna; }

    @Override
    protected Jugador getJugadorSol() { return jugadorSol; }

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
        if (musicaNivel != null) musicaNivel.dispose();
    }
}
