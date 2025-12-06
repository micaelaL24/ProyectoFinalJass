package utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProgresoJuego {
    private static final String PREFS_NAME = "FuegoAguaProgreso";
    private static final String KEY_NIVEL_MAX = "nivelMaxDesbloqueado";

    private static Preferences prefs;

    /**
     * Inicializa el sistema de guardado
     */
    public static void inicializar() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * Obtiene el nivel máximo desbloqueado
     * @return número del nivel máximo (1 al inicio)
     */
    public static int getNivelMaxDesbloqueado() {
        if (prefs == null) inicializar();
        return prefs.getInteger(KEY_NIVEL_MAX, 5); // Por defecto nivel 1
    }

    /**
     * Desbloquea un nuevo nivel si es mayor al actual
     * @param nivel número del nivel a desbloquear
     */
    public static void desbloquearNivel(int nivel) {
        if (prefs == null) inicializar();

        int nivelActual = getNivelMaxDesbloqueado();
        if (nivel > nivelActual) {
            prefs.putInteger(KEY_NIVEL_MAX, nivel);
            prefs.flush(); // Guardar en disco
            System.out.println("¡Nivel " + nivel + " desbloqueado!");
        }
    }

    /**
     * Reinicia el progreso (útil para testing)
     */
    public static void reiniciarProgreso() {
        if (prefs == null) inicializar();
        prefs.putInteger(KEY_NIVEL_MAX, 1);
        prefs.flush();
        System.out.println("Progreso reiniciado. Solo nivel 1 disponible.");
    }

    /**
     * Desbloquea TODOS los niveles (útil para testing)
     */
    public static void desbloquearTodos(int cantidadNiveles) {
        if (prefs == null) inicializar();
        prefs.putInteger(KEY_NIVEL_MAX, cantidadNiveles);
        prefs.flush();
        System.out.println("¡Todos los " + cantidadNiveles + " niveles desbloqueados!");
    }
}
