package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import elementos.Imagen;
import utiles.Recursos;
import utiles.Render;

public class PantallaDerrota implements Screen {
    Imagen fondo, boton;
    SpriteBatch b;
    OrthographicCamera camera;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDODERROTA);
        fondo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Crear cámara ajustada a la pantalla
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        b = Render.batch;

        boton = new Imagen(Recursos.BOTONJUGAR, Recursos.BOTONJUGARHOVER);
        boton.setSize(220,80);
        boton.setPosition(
            (Gdx.graphics.getWidth() - boton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - boton.getHeight()) / 2 - 150
        );
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Render.limpiarPantalla(0, 0, 0);

        // Ajustar proyección del SpriteBatch a la cámara de pantalla completa
        b.setProjectionMatrix(camera.combined);

        b.begin();
        fondo.dibujar();
        boton.dibujar();

        if (boton.isClicked()) {
            Render.app.setScreen(new PantallaNivelSeleccion());
            System.out.println("Pantalla cambiada");
        }

        b.end();
    }

    @Override
    public void resize(int width, int height) {
        // Si cambia el tamaño, actualizar cámara y fondo
        camera.setToOrtho(false, width, height);
        fondo.setSize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
