public class Climatizador extends Dispositivo implements Apagable, Alarma {
    protected double temperatura;
    protected int nivel;

    public Climatizador(String codigo, String oficina, double potenciaNom) {
        super(codigo, oficina, potenciaNom);
        this.temperatura = 20;
        this.nivel = 50;
    }

    public int getNivel() {
        return this.nivel;
    }

    public double getTemperatura() {
        return this.temperatura;
    }

    @Override
    public void apagar() {
        if (!this.estado) {
            throw new IllegalArgumentException("El climatizador ya está apagado.");
        }
        this.estado = false;
    }

    public int nivelMinimo() {
        return 20;
    }

    @Override
    public void ajustarNivel(int nuevoNivel) {
        if (!this.estado) {
            throw new IllegalArgumentException("Primero debes encender el climatizador.");
        }
        if (nuevoNivel < nivelMinimo() || nuevoNivel > 100) {
            throw new IllegalArgumentException("El nivel debe estar entre " + nivelMinimo() + " y 100.");
        }
        this.nivel = nuevoNivel;
    }

    @Override
    public double consumoHora() {
        if (!this.estado) {
            return 0;
        }
        double factor = 0.30 + 0.70 * this.nivel / 100.0;
        double watts = this.potenciaNom * factor;
        double kWh = watts / 1000.0;
        return kWh;
    }

    @Override
    public void registrarLectura(double lectura) {
        if (!this.estado) {
            throw new IllegalArgumentException("No se puede leer un climatizador apagado.");
        }
        if (Double.isNaN(lectura) || lectura < -10 || lectura > 60) {
            throw new IllegalArgumentException("La temperatura debe estar entre -10 y 60 °C.");
        }
        this.temperatura = lectura;
    }

    @Override
    public String alertar() {
        if (this.temperatura >= 40) {
            return "Crítica";
        }
        if (this.temperatura >= 32) {
            return "Alta";
        }
        if (this.temperatura >= 28) {
            return "Baja";
        }
        return "";
    }

    @Override
    public String generarReporte() {
        return "Temperatura de " + this.temperatura + " °C en " + this.codigo;
    }

    @Override
    public String toString() {
        String cadena = super.toString(); cadena = cadena + " | Nivel: " + this.nivel;
        cadena = cadena + " | Temperatura: " + this.temperatura + " °C";
        return cadena;
    }
}
