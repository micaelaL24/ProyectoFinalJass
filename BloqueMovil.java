package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import utiles.Recursos;

public class BloqueMovil {
    private Rectangle hitbox;
    private float velocidadEmpuje = 80f;
    private float velX = 0;
    private float velY = 0;
    private final float gravedad = -500f;
    private boolean enSuelo = false;

    private Texture textura;

    public BloqueMovil(float x, float y, float ancho, float alto, String rutaImagen) {
        this.hitbox = new Rectangle(x, y, ancho, alto);
        this.textura = new Texture(rutaImagen);
    }

    public BloqueMovil(float x, float y, float ancho, float alto) {
        this(x, y, ancho, alto, Recursos.BLOQUEMOVIL);
    }

    public void update(float delta, Array<Rectangle> colisionesEstaticas, Jugador jugadorAgua, Jugador jugadorFuego) {
        // Aplicar gravedad
        velY += gravedad * delta;

        // Verificar si algún jugador está empujando el bloque
        velX = 0;

        // Jugador Agua empujando
        if (jugadorAgua != null && jugadorAgua.estaVivo()) {
            if (empujandoPorDerecha(jugadorAgua)) {
                velX = velocidadEmpuje * delta;
            } else if (empujandoPorIzquierda(jugadorAgua)) {
                velX = -velocidadEmpuje * delta;
            }
        }

        // Jugador Fuego empujando
        if (jugadorFuego != null && jugadorFuego.estaVivo()) {
            if (empujandoPorDerecha(jugadorFuego)) {
                velX = velocidadEmpuje * delta;
            } else if (empujandoPorIzquierda(jugadorFuego)) {
                velX = -velocidadEmpuje * delta;
            }
        }

        // Mover horizontalmente
        if (velX != 0) {
            moverEnX(velX, colisionesEstaticas);
        }

        // Mover verticalmente (gravedad)
        moverEnY(velY * delta, colisionesEstaticas);
    }

    private boolean empujandoPorDerecha(Jugador jugador) {
        Rectangle hitboxJugador = jugador.getHitbox();

        // Verificar si el jugador está a la izquierda del bloque y tocándolo
        return hitboxJugador.x + hitboxJugador.width >= hitbox.x - 5 &&
            hitboxJugador.x + hitboxJugador.width <= hitbox.x + 10 &&
            hitboxJugador.y < hitbox.y + hitbox.height &&
            hitboxJugador.y + hitboxJugador.height > hitbox.y &&
            jugador.getX() < hitbox.x;
    }

    private boolean empujandoPorIzquierda(Jugador jugador) {
        Rectangle hitboxJugador = jugador.getHitbox();

        // Verificar si el jugador está a la derecha del bloque y tocándolo
        return hitboxJugador.x <= hitbox.x + hitbox.width + 5 &&
            hitboxJugador.x >= hitbox.x + hitbox.width - 10 &&
            hitboxJugador.y < hitbox.y + hitbox.height &&
            hitboxJugador.y + hitboxJugador.height > hitbox.y &&
            jugador.getX() > hitbox.x + hitbox.width;
    }

    private void moverEnX(float dx, Array<Rectangle> colisiones) {
        hitbox.x += dx;

        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                hitbox.x -= dx;
                velX = 0;
                break;
            }
        }
    }

    private void moverEnY(float dy, Array<Rectangle> colisiones) {
        hitbox.y += dy;

        boolean colisionVertical = false;
        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                colisionVertical = true;
                break;
            }
        }

        if (colisionVertical) {
            if (dy < 0) {
                enSuelo = true;
                velY = 0;
                hitbox.y -= dy;
            } else {
                velY = 0;
                hitbox.y -= dy;
            }
        } else {
            enSuelo = false;
        }
    }

    public void dibujar(SpriteBatch batch) {
        // Dibujar la textura del bloque
        batch.draw(textura, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean estaEnSuelo() {
        return enSuelo;
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
