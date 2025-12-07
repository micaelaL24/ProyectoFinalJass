package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Boton {
    private Rectangle hitbox;
    private boolean presionado = false;
    private ShapeRenderer shapeRenderer;

    // Para usar imágenes
    private Texture texturaNormal;
    private Texture texturaPresionada;
    private boolean usarImagenes = false;


    // Constructor con imágenes (nuevo)
    public Boton(float x, float y, float ancho, float alto, String rutaImagenNormal, String rutaImagenPresionada) {
        this.hitbox = new Rectangle(x, y, ancho, alto);
        this.texturaNormal = new Texture(rutaImagenNormal);
        this.texturaPresionada = new Texture(rutaImagenPresionada);
        this.usarImagenes = true;
    }

    public void update(Jugador jugadorLuna, Jugador jugadorSol) {
        // Verificar si algún jugador está pisando el botón
        presionado = false;

        if (jugadorLuna != null && jugadorLuna.estaVivo()) {
            if (estaPisando(jugadorLuna)) {
                presionado = true;
            }
        }

        if (jugadorSol != null && jugadorSol.estaVivo()) {
            if (estaPisando(jugadorSol)) {
                presionado = true;
            }
        }
    }

    private boolean estaPisando(Jugador jugador) {
        Rectangle hitboxJugador = jugador.getHitbox();

        // Verificar si el jugador está sobre el botón
        // El pie del jugador debe estar tocando la parte superior del botón
        float pieJugador = hitboxJugador.y;
        float topeBoton = hitbox.y + hitbox.height;

        return hitboxJugador.x + hitboxJugador.width > hitbox.x + 5 &&
            hitboxJugador.x < hitbox.x + hitbox.width - 5 &&
            pieJugador >= hitbox.y - 10 &&
            pieJugador <= topeBoton + 20;
    }

    public void dibujar(SpriteBatch batch) {
        if (usarImagenes) {
            // Dibujar con imágenes
            Texture texturaActual = presionado ? texturaPresionada : texturaNormal;
            batch.draw(texturaActual, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
    }

    public boolean estaPresionado() {
        return presionado;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (texturaNormal != null) {
            texturaNormal.dispose();
        }
        if (texturaPresionada != null) {
            texturaPresionada.dispose();
        }
    }
}
