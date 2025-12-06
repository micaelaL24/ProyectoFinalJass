package utiles;

public class Timer {
    private float tiempoRestante;
    private boolean pausado;

    public Timer(float tiempoInicial) {
        this.tiempoRestante = tiempoInicial;
        this.pausado = false;
    }

    public void actualizar(float delta) {
        if (!pausado && tiempoRestante > 0) {
            tiempoRestante -= delta;
            if (tiempoRestante < 0) tiempoRestante = 0;
        }
    }

    public void setTiempoRestante(float tiempo) {
        this.tiempoRestante = tiempo;
    }

    public void pausar() {
        pausado = true;
    }

    public void despausar() {
        pausado = false;
    }

    public void alternarPausa() {
        pausado = !pausado;
    }

    public boolean estaPausado() {
        return pausado;
    }

    public float getTiempoRestante() {
        return tiempoRestante;
    }

    public boolean estaTerminado() {
        return tiempoRestante <= 0;
    }
}
