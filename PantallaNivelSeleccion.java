package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Preferences;
import elementos.Imagen;
import utiles.GestorMusica;
import utiles.ProgresoJuego;
import utiles.Recursos;
import utiles.Render;

import java.util.ArrayList;
import java.util.List;

public class PantallaNivelSeleccion implements Screen {

    private Imagen fondo;
    private List<Imagen> botonesNivel;
    private SpriteBatch b;

    // Constantes
    private static final float ANCHO_BOTON = 80;
    private static final float ALTO_BOTON = 86;
    private static final float SEPARACION_HORIZONTAL = 6f;
    private static final float OFFSET_FILA_SUPERIOR = 70;
    private static final float OFFSET_FILA_INFERIOR = 200;
    private static final int NIVELES_POR_FILA = 5;
    private static final int TOTAL_NIVELES = 10;

    // Progreso guardado
    private int nivelMax;

    @Override
    public void show() {
        System.out.println("NS: show() inicio");

        // Fondo
        System.out.println("NS: creando fondo con " + Recursos.FONDOSELECNIVEL);
        fondo = new Imagen(Recursos.FONDOSELECNIVEL);
        fondo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        System.out.println("NS: fondo OK");

        b = Render.batch;
        botonesNivel = new ArrayList<>(TOTAL_NIVELES);

        // ---------- PROGRESO ----------
        System.out.println("NS: pidiendo nivelMax");
        nivelMax = ProgresoJuego.getNivelMaxDesbloqueado();
        System.out.println("NS: nivelMax = " + nivelMax);

        float centroY = (Gdx.graphics.getHeight() - ALTO_BOTON) / 2;

        // Crear botones
        for (int i = 0; i < TOTAL_NIVELES; i++) {
            int nivel = i + 1;
            System.out.println("NS: creando boton nivel " + nivel);
            Imagen boton = crearBotonNivel(nivel);

            int columna = (i % NIVELES_POR_FILA) + 1;
            int fila = i / NIVELES_POR_FILA;

            float offsetX = (Gdx.graphics.getWidth() - ANCHO_BOTON) / SEPARACION_HORIZONTAL * columna;
            float offsetY = centroY - (fila == 0 ? OFFSET_FILA_SUPERIOR : OFFSET_FILA_INFERIOR);

            boton.setSize(ANCHO_BOTON, ALTO_BOTON);
            boton.setPosition(offsetX, offsetY);

            if (nivel > nivelMax) {
                boton.setBloqueado(true);
                boton.setTransperencia(0.4f);
            }

            botonesNivel.add(boton);
            System.out.println("NS: boton nivel " + nivel + " OK");
        }

        // Música
        System.out.println("NS: reproducir música MENU");
        GestorMusica.reproducir(GestorMusica.TipoMusica.MENU);
        System.out.println("NS: show() FIN");
    }

    private Imagen crearBotonNivel(int nivel) {
        switch (nivel) {
            case 1: return new Imagen(Recursos.BOTONNIVEL1, Recursos.BOTONNIVELHOVER1);
            case 2: return new Imagen(Recursos.BOTONNIVEL2, Recursos.BOTONNIVELHOVER2);
            case 3: return new Imagen(Recursos.BOTONNIVEL3, Recursos.BOTONNIVELHOVER3);
            case 4: return new Imagen(Recursos.BOTONNIVEL4, Recursos.BOTONNIVELHOVER4);
            case 5: return new Imagen(Recursos.BOTONNIVEL5, Recursos.BOTONNIVELHOVER5);
            case 6: return new Imagen(Recursos.BOTONNIVEL6, Recursos.BOTONNIVELHOVER6);
            case 7: return new Imagen(Recursos.BOTONNIVEL7, Recursos.BOTONNIVELHOVER7);
            case 8: return new Imagen(Recursos.BOTONNIVEL8, Recursos.BOTONNIVELHOVER8);
            case 9: return new Imagen(Recursos.BOTONNIVEL9, Recursos.BOTONNIVELHOVER9);
            case 10: return new Imagen(Recursos.BOTONNIVEL10, Recursos.BOTONNIVELHOVER10);
        }
        return null;
    }

    private Screen crearPantallaNivel(int nivel) {
        switch (nivel) {
            case 1: return new PantallaNivel1();
            //case 2: return new PantallaNivel2();
            //case 3: return new PantallaNivel3();
            //case 4: return new PantallaNivel4();
            //case 5: return new PantallaNivel5();
            //case 6: return new PantallaNivel6();
            //case 7: return new PantallaNivel7();
            //case 8: return new PantallaNivel8();
            //case 9: return new PantallaNivel9();
            //case 10: return new PantallaNivel10();
        }
        return null;
    }

    @Override
    public void render(float delta) {
        b.begin();
        fondo.dibujar();

        for (int i = 0; i < botonesNivel.size(); i++) {
            Imagen boton = botonesNivel.get(i);
            int nivel = i + 1;

            boton.dibujar();

            if (!boton.isBloqueado() && boton.isClicked()) {
                GestorMusica.reproducir(GestorMusica.TipoMusica.NIVEL);

                switch (nivel) {
                    case 1: Render.app.setScreen(new PantallaClienteConectando(1));
                        break;
                    case 2: Render.app.setScreen(new PantallaClienteConectando(2)); break;
                    case 3: Render.app.setScreen(new PantallaClienteConectando(3)); break;
                    case 4: Render.app.setScreen(new PantallaClienteConectando(4)); break;
                    case 5: Render.app.setScreen(new PantallaClienteConectando(5)); break;
                    case 6: Render.app.setScreen(new PantallaClienteConectando(6)); break;
                    case 7: Render.app.setScreen(new PantallaClienteConectando(7)); break;
                    case 8: Render.app.setScreen(new PantallaClienteConectando(8)); break;
                    case 9: Render.app.setScreen(new PantallaClienteConectando(9)); break;
                    case 10: Render.app.setScreen(new PantallaClienteConectando(10)); break;
                }

                b.end();
                return;
            }

        }

        // cerrar el batch si no se clickeó nada
        b.end();
    }


    // ================= Métodos del Screen =================

    @Override public void resize(int width, int height) {}

    @Override public void pause() { GestorMusica.pausar(); }

    @Override public void resume() { GestorMusica.reanudar(); }

    @Override public void hide() {}

    @Override public void dispose() {}
}
