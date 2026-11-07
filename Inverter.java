public class Inverter extends Climatizador {
    public Inverter(String codigo, String oficina, double potenciaNom) {
        super(codigo, oficina, potenciaNom);
    }

    @Override
    public int nivelMinimo() {
        return 10;
    }

    @Override
    public double consumoHora() {
        double consumoNormal = super.consumoHora();
        double consumoInverter = consumoNormal * 0.80;
        return consumoInverter;
    }

    @Override
    public String toString() {
        return "Inverter | " + super.toString();
    }
}
