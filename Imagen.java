package elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import utiles.InputManager;
import utiles.Render;

public class Imagen {
    private Texture normal;
    private Texture hover;
    private Sprite s;

    // ----------- NUEVO -----------
    private boolean bloqueado = false;
    // ------------------------------

    // Constructor con solo una imagen (sin hover)
    public Imagen(String rutaNormal) {
        this(rutaNormal, null);
    }

    // Constructor con hover opcional
    public Imagen(String rutaNormal, String rutaHover) {
        normal = new Texture(rutaNormal);
        hover = (rutaHover != null) ? new Texture(rutaHover) : null;
        s = new Sprite(normal);
    }

    // Detectar si el mouse está encima
    private boolean isMouseOver() {
        float mouseX = InputManager.getMouseX();
        float mouseY = InputManager.getMouseY();
        return mouseX > s.getX() && mouseX < s.getX() + s.getWidth()
            && mouseY > s.getY() && mouseY < s.getY() + s.getHeight();
    }

    // Dibujar el botón
    public void dibujar() {
        // ----------- NUEVO: si está bloqueado no usa hover -----------
        if (!bloqueado && hover != null) {
            s.setTexture(isMouseOver() ? hover : normal);
        } else {
            s.setTexture(normal);
        }
        // -------------------------------------------------------------

        s.draw(Render.batch);
    }

    // Detectar click (deshabilitado si está bloqueado)
    public boolean isClicked() {
        return !bloqueado && isMouseOver() && InputManager.isMouseClicked();
    }

    // ----------- NUEVO: bloquear/desbloquear botón -----------
    public void setBloqueado(boolean b) {
        this.bloqueado = b;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }
    // -----------------------------------------------------------

    // Manejo de opacidad
    public void setTransperencia(float a) {
        s.setAlpha(a);
    }

    public void setSize(float ancho, float alto) {
        s.setSize(ancho, alto);
    }

    public void setPosition(float x, float y){
        s.setPosition(x, y);
    }

    public float getWidth() {
        return s.getWidth();
    }

    public float getHeight() {
        return s.getHeight();
    }
}
