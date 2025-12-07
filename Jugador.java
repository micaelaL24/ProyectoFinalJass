package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import utiles.InputManager;

public class Jugador {
    private Animation<TextureRegion> animacionCaminar;
    private Animation<TextureRegion> animacionIdle;
    private Animation<TextureRegion> animacionSaltar;

    private TextureRegion frameActual;
    private float tiempoAnimacion = 0f;

    private float x, y;
    private float ancho, alto;
    private float velocidad = 150f;
    private float velY = 0;
    private final float gravedad = -500f;
    private boolean enSuelo = false;
    private Rectangle hitbox;
    private boolean vivo = true;

    // Estados de animación
    private EstadoAnimacion estadoActual = EstadoAnimacion.IDLE;
    private boolean mirandoDerecha = true;

    private int estrellasRecolectadas = 0;

    // Tipo de jugador
    private TipoJugador tipo;

    private enum EstadoAnimacion {
        IDLE, CAMINANDO, SALTANDO
    }

    public enum TipoJugador {
        AGUA, FUEGO
    }

    // Constructor con tipo de jugador
    public Jugador(float x, float y, TipoJugador tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;

        cargarAnimaciones();

        frameActual = animacionIdle.getKeyFrame(0);
        this.ancho = frameActual.getRegionWidth();
        this.alto = frameActual.getRegionHeight();
        this.hitbox = new Rectangle(x, y, ancho, alto);
    }

    // Constructor con tamaño personalizado
    public Jugador(float x, float y, float ancho, float alto, TipoJugador tipo) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = tipo;

        cargarAnimaciones();

        frameActual = animacionIdle.getKeyFrame(0);
        this.hitbox = new Rectangle(x, y, ancho, alto);
    }

    private void cargarAnimaciones() {
        // Cargar sprites según el tipo de jugador
        TextureRegion frame1, frame2, frame3, frame4;

        if (tipo == TipoJugador.AGUA) {
            frame1 = new TextureRegion(new Texture("quieto.png"));
            frame2 = new TextureRegion(new Texture("caminando.png"));
            frame3 = new TextureRegion(new Texture("corriendo 1.png"));
            frame4 = new TextureRegion(new Texture("corriendo2.png"));
        } else { // FUEGO
            frame1 = new TextureRegion(new Texture("quietoFuego.png"));
            frame2 = new TextureRegion(new Texture("caminandoFuego.png"));
            frame3 = new TextureRegion(new Texture("corriendo1Fuego.png"));
            frame4 = new TextureRegion(new Texture("corriendo2Fuego.png"));
        }

        // Animación de caminar
        TextureRegion[] framesCaminar = new TextureRegion[2];
        framesCaminar[0] = frame3;
        framesCaminar[1] = frame4;
        animacionCaminar = new Animation<>(0.3f, framesCaminar);
        animacionCaminar.setPlayMode(Animation.PlayMode.LOOP);

        // Idle
        TextureRegion[] framesIdle = new TextureRegion[1];
        framesIdle[0] = frame1;
        animacionIdle = new Animation<>(0.5f, framesIdle);

        // Saltar
        TextureRegion[] framesSaltar = new TextureRegion[1];
        framesSaltar[0] = frame2;
        animacionSaltar = new Animation<>(0.1f, framesSaltar);
    }

    public void update(float delta, Array<Rectangle> colisiones) {
        if (!vivo) return;

        tiempoAnimacion += delta;
        float dx = 0;
        boolean moviendose = false;

        // Movimiento según el tipo de jugador
        if (tipo == TipoJugador.AGUA) {
            // Jugador 1 (Luna de tipo agua) - Flechas
            if (InputManager.isMovingLeft()) {
                dx = -velocidad * delta;
                mirandoDerecha = false;
                moviendose = true;
            }
            if (InputManager.isMovingRight()) {
                dx = velocidad * delta;
                mirandoDerecha = true;
                moviendose = true;
            }
            if (InputManager.isJumping() && enSuelo) {
                velY = 450f;
                enSuelo = false;
            }
        } else {
            // Jugador 2 (Sol de tipo Juego) - WASD
            if (InputManager.isMovingLeftP2()) {
                dx = -velocidad * delta;
                mirandoDerecha = false;
                moviendose = true;
            }
            if (InputManager.isMovingRightP2()) {
                dx = velocidad * delta;
                mirandoDerecha = true;
                moviendose = true;
            }
            if (InputManager.isJumpingP2() && enSuelo) {
                velY = 450f;
                enSuelo = false;
            }
        }

        // Determinar estado de animación
        if (!enSuelo) {
            estadoActual = EstadoAnimacion.SALTANDO;
        } else if (moviendose) {
            estadoActual = EstadoAnimacion.CAMINANDO;
        } else {
            estadoActual = EstadoAnimacion.IDLE;
        }

        // Aplicar gravedad
        velY += gravedad * delta;

        // Mover y colisionar
        moverEnX(dx, colisiones);
        moverEnY(velY * delta, colisiones);

        // Actualizar frame actual
        actualizarAnimacion();
    }

    private void actualizarAnimacion() {
        switch (estadoActual) {
            case IDLE:
                frameActual = animacionIdle.getKeyFrame(tiempoAnimacion);
                break;
            case CAMINANDO:
                frameActual = animacionCaminar.getKeyFrame(tiempoAnimacion);
                break;
            case SALTANDO:
                frameActual = animacionSaltar.getKeyFrame(tiempoAnimacion);
                break;
        }
    }

    private void moverEnX(float dx, Array<Rectangle> colisiones) {
        x += dx;
        hitbox.setPosition(x, y);

        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                x -= dx;
                hitbox.setPosition(x, y);
                break;
            }
        }
    }

    private void moverEnY(float dy, Array<Rectangle> colisiones) {
        y += dy;
        hitbox.setPosition(x, y);

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
                y -= dy;
                hitbox.setPosition(x, y);
            } else {
                velY = 0;
                y -= dy;
                hitbox.setPosition(x, y);
            }
        } else {
            enSuelo = false;
        }
    }

    public void morir() {
        vivo = false;
        System.out.println("¡Jugador " + tipo + " muerto!");
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void sumarEstrella() {
        estrellasRecolectadas++;
        System.out.println("Jugador " + tipo + " - Estrellas: " + estrellasRecolectadas);
    }

    public int getEstrellasRecolectadas() {
        return estrellasRecolectadas;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        hitbox.setPosition(x, y);
    }

    public void dibujar(SpriteBatch batch) {
        if (mirandoDerecha) {
            batch.draw(frameActual, x, y);
        } else {
            batch.draw(frameActual, x + frameActual.getRegionWidth(), y,
                -frameActual.getRegionWidth(), frameActual.getRegionHeight());
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public TipoJugador getTipo() {
        return tipo;
    }


    public boolean isMirandoDerecha() {
        return mirandoDerecha;
    }

    public void setMirandoDerecha(boolean mirandoDerecha) {
        this.mirandoDerecha = mirandoDerecha;
    }


    public void dispose() {
        // Las texturas se disponen cuando se cierra el juego
    }
}
