package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import elementos.BarraVolumen;
import elementos.Imagen;
import utiles.GestorMusica;
import utiles.Recursos;
import utiles.Render;

public class PantallaMenu implements Screen {

    Imagen fondo, btn1, btn2;
    SpriteBatch b;
    BarraVolumen barraVolumen;

    // --- NUEVO ---
    private boolean mostrandoInstrucciones = false;
    private Texture instruccionesTexture;
    private boolean ignorarPrimerClick = false;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO_MENU);
        fondo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        b = Render.batch;

        btn1 = new Imagen(Recursos.BOTONJUGAR, Recursos.BOTONJUGARHOVER);
        btn1.setSize(220, 80);
        btn1.setPosition(
            (Gdx.graphics.getWidth() - btn1.getWidth()) / 2,
            (Gdx.graphics.getHeight() - btn1.getHeight()) / 2 - 70
        );

        btn2 = new Imagen(Recursos.BOTONINSTRUC, Recursos.BOTONINSTRUCHOVER);
        btn2.setSize(280, 80);
        btn2.setPosition(
            (Gdx.graphics.getWidth() - btn2.getWidth()) / 2,
            (Gdx.graphics.getHeight() - btn2.getHeight()) / 2 - 180
        );

        // Imagen de instrucciones
        instruccionesTexture = new Texture("instrucciones.png");

        if (!GestorMusica.estanCargadas()) {
            GestorMusica.cargarMusicas();
        }

        GestorMusica.reproducir(GestorMusica.TipoMusica.MENU);

        float barraAncho = 200;
        float barraAlto = 20;
        float barraX = Gdx.graphics.getWidth() - barraAncho - 30;
        float barraY = Gdx.graphics.getHeight() - 60;
        barraVolumen = new BarraVolumen(barraX, barraY, barraAncho, barraAlto);
    }

    @Override
    public void render(float delta) {

        // Si está mostrando instrucciones
        if (mostrandoInstrucciones) {

            // Dibujar menú debajo
            b.begin();
            fondo.dibujar();
            btn1.dibujar();
            btn2.dibujar();
            barraVolumen.dibujar(b);
            b.end();

            // --- DIBUJAR INSTRUCCIONES MÁS CHICA Y CENTRADA ---
            float ancho = Gdx.graphics.getWidth() * 0.8f;
            float alto = Gdx.graphics.getHeight() * 0.8f;

            float x = (Gdx.graphics.getWidth() - ancho) / 2;
            float y = (Gdx.graphics.getHeight() - alto) / 2;

            b.begin();
            b.draw(instruccionesTexture, x, y, ancho, alto);
            b.end();

            // Ignorar el clic que abrió la imagen
            if (ignorarPrimerClick) {
                if (!Gdx.input.isTouched()) {
                    ignorarPrimerClick = false;
                }
                return;
            }

            // Cerrar instrucciones con clic o ESC
            if (Gdx.input.justTouched()
                || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
                mostrandoInstrucciones = false;
            }

            return;
        }

        // Mostrar menú normal
        barraVolumen.update();

        b.begin();
        fondo.dibujar();
        btn1.dibujar();
        btn2.dibujar();
        barraVolumen.dibujar(b);
        b.end();

        if (btn1.isClicked()) {
            Render.app.setScreen(new PantallaNivelSeleccion());
        }



        if (btn2.isClicked()) {
            mostrandoInstrucciones = true;
            ignorarPrimerClick = true; // <- evita que desaparezca al instante
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() { GestorMusica.pausar(); }
    @Override public void resume() { GestorMusica.reanudar(); }
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (barraVolumen != null) barraVolumen.dispose();
        if (instruccionesTexture != null) instruccionesTexture.dispose();
    }
}
