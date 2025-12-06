package utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputManager {

    // ===== JUGADOR 1 (Fuego) - Flechas =====
    public static boolean isMovingLeft() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    public static boolean isMovingRight() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public static boolean isJumping() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }

    // ===== JUGADOR 2 (Agua) - WASD  =====
    public static boolean isMovingLeftP2() {
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }

    public static boolean isMovingRightP2() {
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }

    public static boolean isJumpingP2() {
        return Gdx.input.isKeyJustPressed(Input.Keys.W);
    }

    // ===== CONTROLES DEL JUEGO =====
    public static boolean isPausePressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.P);
    }

    public static boolean isEscapePressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    // ===== MOUSE (para botones e interfaces) =====
    public static boolean isMouseClicked() {
        return Gdx.input.justTouched();
    }

    public static int getMouseX() {
        return Gdx.input.getX();
    }

    public static int getMouseY() {
        return Gdx.graphics.getHeight() - Gdx.input.getY();
    }

    public static boolean isMousePressed() {
        return Gdx.input.isTouched();
    }
}
