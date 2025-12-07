package elementos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Barrera {
    private Rectangle hitbox;
    private float posicionInicial;
    private float posicionFinal;
    private float alturaTotal;
    private boolean activa = true;
    private float velocidadMovimiento = 300f;

    private ShapeRenderer shapeRenderer;
    private Color color;

    private Array<Boton> botonesAsociados; // Múltiples botones

    /**
     * Constructor que acepta múltiples botones.
     * La barrera se abre si CUALQUIER botón está presionado (lógica OR).
     */
    public Barrera(float x, float y, float ancho, float alto, Array<Boton> botones) {
        this.alturaTotal = alto;
        this.posicionInicial = y;
        this.posicionFinal = y - (alto * 0.95f);
        this.hitbox = new Rectangle(x, posicionInicial, ancho, alturaTotal);
        this.botonesAsociados = botones;
        this.shapeRenderer = new ShapeRenderer();
        this.color = new Color(0.8f, 0.2f, 0.2f, 0.9f);
    }

    public void update(float delta) {
        if (botonesAsociados == null || botonesAsociados.size == 0) return;

        // Verificar si CUALQUIER botón está presionado (lógica OR)
        boolean algunBotonPresionado = false;
        for (Boton boton : botonesAsociados) {
            if (boton.estaPresionado()) {
                algunBotonPresionado = true;
                break; // Con uno es suficiente
            }
        }

        // Si algún botón está presionado, bajar la barrera
        if (algunBotonPresionado) {
            if (hitbox.y > posicionFinal) {
                hitbox.y -= velocidadMovimiento * delta;
                if (hitbox.y <= posicionFinal) {
                    hitbox.y = posicionFinal;
                }
            }
        } else {
            // Si ningún botón está presionado, subir la barrera
            if (hitbox.y < posicionInicial) {
                hitbox.y += velocidadMovimiento * delta;
                if (hitbox.y >= posicionInicial) {
                    hitbox.y = posicionInicial;
                }
            }
        }

        // La barrera solo es activa (colisionable) si está arriba
        activa = (hitbox.y >= posicionInicial - 10);
    }

    public void dibujar(SpriteBatch batch) {
        // Calcular qué parte de la barrera está sobre el suelo (visible)
        float yMinimo = posicionInicial;

        if (hitbox.y + alturaTotal < yMinimo) return;

        float yDibujo = Math.max(hitbox.y, yMinimo);
        float alturaVisible = (hitbox.y + alturaTotal) - yDibujo;

        if (alturaVisible <= 0) return;

        // Calcular transparencia según la posición (fade out al bajar)
        float porcentajeVisible = (hitbox.y - posicionFinal) / (posicionInicial - posicionFinal);
        float alpha = Math.max(0.3f, Math.min(1.0f, porcentajeVisible));

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Dibujar solo la parte visible de la barrera con transparencia
        shapeRenderer.setColor(color.r, color.g, color.b, alpha);
        shapeRenderer.rect(hitbox.x, yDibujo, hitbox.width, alturaVisible);

        // Rayas horizontales para efecto visual
        shapeRenderer.setColor(0.6f, 0.1f, 0.1f, alpha * 0.8f);
        float espacioRayas = 20f;
        for (float y = yDibujo; y < yDibujo + alturaVisible; y += espacioRayas) {
            shapeRenderer.rectLine(hitbox.x, y, hitbox.x + hitbox.width, y, 3);
        }

        // Borde
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(hitbox.x, yDibujo, hitbox.width, alturaVisible);

        shapeRenderer.end();
        batch.begin();
    }

    public boolean estaActiva() {
        return activa;
    }

    public Rectangle getHitbox() {
        if (estaActiva()) {
            return new Rectangle(hitbox.x, posicionInicial, hitbox.width, alturaTotal);
        }
        return new Rectangle(0, 0, 0, 0);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
