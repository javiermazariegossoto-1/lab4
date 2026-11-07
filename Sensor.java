public class Sensor extends Dispositivo implements Alarma {
    private double concentracionHumo;

    public Sensor(String codigo, String oficina, double potenciaNom) {
        super(codigo, oficina, potenciaNom);
        this.estado = true; // Es el único tipo que inicia encendido
        this.concentracionHumo = 0;
    }

    public double getConcentracionHumo() {
        return this.concentracionHumo;
    }

    @Override
    public double consumoHora() {
        return this.potenciaNom / 1000.0;
    }

    @Override
    public void registrarLectura(double lectura) {
        if (Double.isNaN(lectura) || lectura < 0 || lectura > 1000) {
            throw new IllegalArgumentException("El humo debe estar entre 0 y 1000 ppm.");
        }
        this.concentracionHumo = lectura;
    }

    @Override
    public String alertar() {
        if (this.concentracionHumo >= 300) {
            return "Crítica";
        }
        if (this.concentracionHumo >= 150) {
            return "Alta";
        }
        return "";
    }

    @Override
    public String generarReporte() {
        return "Humo de " + this.concentracionHumo + " ppm en " + this.codigo;
    }

    @Override
    public String toString() {
        return "Sensor | " + super.toString();
    }
}
