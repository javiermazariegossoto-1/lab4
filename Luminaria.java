public class Luminaria extends Dispositivo implements Apagable {
    private int nivel;

    public Luminaria(String codigo, String oficina, double potenciaNom) {
        super(codigo, oficina, potenciaNom);
        this.nivel = 100;
    }

    public int getNivel() {
        return this.nivel;
    }

    @Override
    public void apagar() {
        if (!this.estado) {
            throw new IllegalArgumentException("La luminaria ya está apagada.");
        }
        this.estado = false;
    }

    @Override
    public void ajustarNivel(int nuevoNivel) {
        if (!this.estado) {
            throw new IllegalArgumentException("Primero debes encender la luminaria.");
        }
        if (nuevoNivel < 10 || nuevoNivel > 100) {
            throw new IllegalArgumentException("El nivel de la luminaria debe estar entre 10 y 100.");
        }
        this.nivel = nuevoNivel;
    }

    @Override
    public double consumoHora() {
        if (!this.estado) {
            return 0;
        }
        double watts = this.potenciaNom * this.nivel / 100.0;
        double kWh = watts / 1000.0;
        return kWh;
    }

    @Override
    public String toString() {
        String cadena = "Luminaria | " + super.toString();
        cadena = cadena + " | Nivel: " + this.nivel;
        return cadena;
    }
}
