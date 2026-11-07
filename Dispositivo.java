public abstract class Dispositivo implements Comparable<Dispositivo> {
    protected String codigo;
    protected String oficina;


    protected double potenciaNom;
    protected boolean estado;
    protected double consumoDia;

    public Dispositivo(String codigo, String oficina, double potenciaNom) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del dispositivo no puede estar vacío.");
        }
        if (oficina == null || oficina.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de oficina no puede estar vacío.");
        }
        if (potenciaNom <= 0 || Double.isNaN(potenciaNom) || Double.isInfinite(potenciaNom)) {
            throw new IllegalArgumentException("La potencia debe ser un número positivo.");
        }
        this.codigo = codigo.trim();
        this.oficina = oficina.trim();
        this.potenciaNom = potenciaNom;
        this.estado = false;
        this.consumoDia = 0;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getOficina() {
        return this.oficina;
    }

    public double getPotenciaNom() {
        return this.potenciaNom;
    }

    public boolean getEstado() {
        return this.estado;
    }

    public double getConsumoDia() {
        return this.consumoDia;
    }

    public void encender() {
        if (this.estado) {
            throw new IllegalArgumentException("El dispositivo ya está encendido.");
        }
        this.estado = true;
    }

    // método abstracto para cada uno de los consumos en kWh
    public abstract double consumoHora();

    //se rechazan operaciones que no se pueden realizar
    // Se reemplaza el método cuando sí se puede realizar
    public void apagar() {
        throw new IllegalArgumentException("Este dispositivo no se puede apagar.");
    }

    public void ajustarNivel(int nuevoNivel) {
        throw new IllegalArgumentException("Este dispositivo no tiene nivel ajustable.");
    }

    public void registrarLectura(double lectura) {
        throw new IllegalArgumentException("Este dispositivo no mide.");
    }

    public void registrarMovimiento(int hora) {
        throw new IllegalArgumentException("Este dispositivo no es una cámara.");
    }

    public String alertar() {
        return ""; //no hay alerta
    }

    public String generarReporte() {
        return "";
    }

    // Se llaman estos métodos cuando se avanza la hora o el día
    void acumularConsumo(double consumo) {
        this.consumoDia = this.consumoDia + consumo;
    }

    void reiniciarConsumo() {
        this.consumoDia = 0;
    }

    @Override
    public int compareTo(Dispositivo otro) {
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public String toString() {
        String textoEstado = "Apagado";
        if (this.estado) {
            textoEstado = "Encendido";
        }

        String cadena = "Código: " + this.codigo;
        cadena = cadena + " | Oficina: " + this.oficina;
        cadena = cadena + " | " + textoEstado;
        cadena = cadena + " | Potencia: " + this.potenciaNom + " W";
        cadena = cadena + " | Consumo de hoy: " + String.format("%.4f", this.consumoDia) + " kWh";
        return cadena;
    }
}
