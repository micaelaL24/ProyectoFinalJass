package elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import utiles.GestorMusica;
import utiles.InputManager;

public class BarraVolumen {
    private float x, y;
    private float ancho, alto;
    private Rectangle hitboxBarra;
    private Rectangle hitboxSlider;
    private boolean arrastrando = false;
    private float volumen;

    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    // Colores
    private Color colorFondo = new Color(0.3f, 0.3f, 0.3f, 0.8f);
    private Color colorBarra = new Color(0.2f, 0.7f, 1f, 1f);
    private Color colorSlider = new Color(1f, 1f, 1f, 1f);

    public BarraVolumen(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

        this.hitboxBarra = new Rectangle(x, y, ancho, alto);
        this.hitboxSlider = new Rectangle(x, y, 20, alto);

        this.volumen = GestorMusica.getVolumen();

        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();

        actualizarPosicionSlider();
    }

    public void update() {
        int mouseX = InputManager.getMouseX();
        int mouseY = InputManager.getMouseY();
        boolean mousePresionado = InputManager.isMouseClicked() || Gdx.input.isTouched();

        // Verificar si se hace clic en el slider o en la barra
        if (mousePresionado && !arrastrando) {
            if (hitboxSlider.contains(mouseX, mouseY) || hitboxBarra.contains(mouseX, mouseY)) {
                arrastrando = true;
            }
        }

        // Arrastrar el slider
        if (arrastrando && mousePresionado) {
            float nuevaPosX = mouseX - hitboxSlider.width / 2;
            nuevaPosX = Math.max(x, Math.min(x + ancho - hitboxSlider.width, nuevaPosX));

            hitboxSlider.x = nuevaPosX;

            // Calcular volumen basado en la posición
            float progreso = (hitboxSlider.x - x) / (ancho - hitboxSlider.width);
            volumen = progreso;

            // Actualizar volumen en el gestor
            GestorMusica.setVolumen(volumen);
        }

        // Soltar el slider
        if (!mousePresionado) {
            arrastrando = false;
        }
    }

    private void actualizarPosicionSlider() {
        float posicionX = x + (ancho - hitboxSlider.width) * volumen;
        hitboxSlider.x = posicionX;
    }

    public void dibujar(SpriteBatch batch) {
        batch.end(); // Terminar el batch para usar ShapeRenderer

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Dibujar fondo de la barra
        shapeRenderer.setColor(colorFondo);
        shapeRenderer.rect(x - 5, y - 5, ancho + 10, alto + 10);

        // Dibujar barra de progreso
        shapeRenderer.setColor(colorBarra);
        shapeRenderer.rect(x, y, ancho, alto);

        // Dibujar barra llena hasta la posición del slider
        shapeRenderer.setColor(0.1f, 0.5f, 0.9f, 1f);
        float anchoLleno = hitboxSlider.x - x + hitboxSlider.width / 2;
        shapeRenderer.rect(x, y, anchoLleno, alto);

        // Dibujar slider
        shapeRenderer.setColor(colorSlider);
        if (arrastrando) {
            shapeRenderer.setColor(0.9f, 0.9f, 0.9f, 1f); // Color más claro cuando se arrastra
        }
        shapeRenderer.rect(hitboxSlider.x, hitboxSlider.y, hitboxSlider.width, hitboxSlider.height);

        shapeRenderer.end();

        // Continuar con el batch para dibujar texto
        batch.begin();

        // Dibujar etiqueta y porcentaje
        font.draw(batch, "Volumen:", x, y + alto + 25);
        String porcentaje = String.format("%.0f%%", volumen * 100);
        font.draw(batch, porcentaje, x + ancho - 30, y + alto + 25);
    }

    public void setPosition(float x, float y) {
        float deltaX = x - this.x;
        float deltaY = y - this.y;

        this.x = x;
        this.y = y;

        hitboxBarra.setPosition(x, y);
        hitboxSlider.setPosition(hitboxSlider.x + deltaX, y);
    }

    public float getVolumen() {
        return volumen;
    }

    public void setVolumen(float volumen) {
        this.volumen = Math.max(0f, Math.min(1f, volumen));
        actualizarPosicionSlider();
        GestorMusica.setVolumen(this.volumen);
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
