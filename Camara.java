public class Camara extends Dispositivo implements Apagable, Alarma {
    private int horaMovimiento;

    public Camara(String codigo, String oficina, double potenciaNom) {
        super(codigo, oficina, potenciaNom);
        this.horaMovimiento = -1; //Esto significa que todavía no se registran movimientos
    }

    @Override
    public void apagar() {
        if (!this.estado) {
            throw new IllegalArgumentException("La cámara ya está apagada");
        }
        this.estado = false;
    }

    @Override
    public double consumoHora() {
        if (!this.estado) {
            return 0;
        }
        return this.potenciaNom / 1000.0;
    }

    @Override
    public void registrarMovimiento(int hora) {
        if (!this.estado) {
            throw new IllegalArgumentException("No se puede registrar movimiento con la cámara apagada");
        }
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23");
        }
        this.horaMovimiento = hora;
    }

    @Override
    public String alertar() {
        if (this.horaMovimiento == -1) {
            return "";
        }
        if (this.horaMovimiento < 7 || this.horaMovimiento >= 19) {
            return "Alta";
        }
        return "";
    }

    @Override
    public String generarReporte() {
        return "Movimiento fuera de horario a las " + this.horaMovimiento + ":00 en " + this.codigo;
    }

    @Override
    public String toString() {
        return "Cámara | " + super.toString();
    }
}
