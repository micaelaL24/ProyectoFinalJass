package utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class GestorMusica {

    private static Music musicaMenu;
    private static Music musicaNivel;
    private static Music musicaActual;
    private static float volumenGlobal = 0.5f;

    // Tipos de música
    public enum TipoMusica {
        MENU,
        NIVEL
    }

    public static void cargarMusicas() {
        if (musicaMenu == null) {
            musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("dueto_video_face.mp3"));
            musicaNivel = Gdx.audio.newMusic(Gdx.files.internal("musicanivel1.mp3"));

            musicaMenu.setLooping(true);
            musicaNivel.setLooping(true);
            musicaMenu.setVolume(0.5f);
            musicaNivel.setVolume(0.6f);
        }
    }

    public static boolean estanCargadas() {
        return musicaMenu != null;
    }

    public static void reproducir(TipoMusica tipo) {
        Music nuevaMusica = null;

        switch (tipo) {
            case MENU:
                nuevaMusica = musicaMenu;
                break;
            case NIVEL:
                nuevaMusica = musicaNivel;
                break;
        }

        // Solo cambiar si es una música diferente
        if (nuevaMusica != null && nuevaMusica != musicaActual) {
            if (musicaActual != null && musicaActual.isPlaying()) {
                musicaActual.stop();
            }
            musicaActual = nuevaMusica;
            musicaActual.play();
        } else if (nuevaMusica != null && !nuevaMusica.isPlaying()) {
            // Si es la misma música pero no está reproduciéndose, reanudarla
            nuevaMusica.play();
        }
    }

    public static void pausar() {
        if (musicaActual != null && musicaActual.isPlaying()) {
            musicaActual.pause();
        }
    }

    public static void reanudar() {
        if (musicaActual != null && !musicaActual.isPlaying()) {
            musicaActual.play();
        }
    }

    public static void parar() {
        if (musicaActual != null && musicaActual.isPlaying()) {
            musicaActual.stop();
        }
    }

    public static void dispose() {
        if (musicaMenu != null) {
            musicaMenu.dispose();
        }
        if (musicaNivel != null) {
            musicaNivel.dispose();
        }
    }

    public static void setVolumen(float volumen) {
        volumenGlobal = Math.max(0f, Math.min(1f, volumen)); // Clamp entre 0 y 1
        if (musicaActual != null) {
            musicaActual.setVolume(volumenGlobal);
        }
    }

    public static float getVolumen() {
        return volumenGlobal;
    }
}
