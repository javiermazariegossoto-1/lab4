public class Alerta implements Comparable<Alerta> {
    private int codigoSecuencial;
    private Dispositivo dispositivoOrigen;
    private int horaGeneracion;
    private String severidad;
    private String mensaje;
    private int horaAtencion;

    public Alerta(int codigo, Dispositivo origen, int hora, String severidad, String mensaje) {
        if (!severidad.equals("Baja") && !severidad.equals("Alta") && !severidad.equals("Crítica")) {
            throw new IllegalArgumentException("Severidad desconocida.");
        }
        this.codigoSecuencial = codigo;
        this.dispositivoOrigen = origen;
        this.horaGeneracion = hora;
        this.severidad = severidad;
        this.mensaje = mensaje;
        this.horaAtencion = -1;
    }

    public int getCodigoSecuencial() {
        return this.codigoSecuencial;
    }

    public Dispositivo getDispositivoOrigen() {
        return this.dispositivoOrigen;
    }

    public String getSeveridad() {
        return this.severidad;
    }

    public int getHoraGeneracion() {
        return this.horaGeneracion;
    }

    public int getHoraAtencion() {
        return this.horaAtencion;
    }

    public String getEstado() {
        if (this.horaAtencion == -1) {
            return "Pendiente";
        }
        return "Atendida";
    }

    // las reglas de severidad están juntas en estos tres métodos
    public int getPrioridad() {
        if (this.severidad.equals("Crítica")) {
            return 3;
        }
        if (this.severidad.equals("Alta")) {
            return 2;
        }
        return 1;
    }

    public int getPlazo() {
        if (this.severidad.equals("Crítica")) {
            return 1;
        }
        if (this.severidad.equals("Alta")) {
            return 4;
        }
        return 24;
    }

    public double getCreditoPorHora() {
        if (this.severidad.equals("Crítica")) {
            return 5;
        }
        if (this.severidad.equals("Alta")) {
            return 2;
        }
        return 0.50;
    }

    public int getHorasRetraso() {
        if (this.horaAtencion == -1) {
            return 0;
        }
        int horasTranscurridas = this.horaAtencion - this.horaGeneracion;
        int retraso = horasTranscurridas - getPlazo();
        if (retraso < 0) {
            retraso = 0;
        }
        return retraso;
    }

    public double getCredito() {
        return getHorasRetraso() * getCreditoPorHora();
    }

    @Override
    public int compareTo(Alerta otra) {
        //aquí se ordena la lista para decidir si va antes o después
        if (getPrioridad() > otra.getPrioridad()) {
            return -1;
        }
        if (getPrioridad() < otra.getPrioridad()) {
            return 1;
        }
        if (this.horaGeneracion < otra.horaGeneracion) {
            return -1;
        }
        if (this.horaGeneracion > otra.horaGeneracion) {
            return 1;
        }
        if (this.codigoSecuencial < otra.codigoSecuencial) {
            return -1;
        }
        if (this.codigoSecuencial > otra.codigoSecuencial) {
            return 1;
        }
        return 0;
    }

    public void atender(int horaActual) {
    if (this.horaAtencion != -1 || horaActual < this.horaGeneracion) {
        throw new IllegalArgumentException("No se puede atender esta alerta.");
    }

    this.horaAtencion = horaActual;
}
    @Override
    public String toString() {
        String cadena = "Alerta " + this.codigoSecuencial + " | " + this.severidad;
        cadena = cadena + " | " + getEstado() + " | Hora de generación: " + this.horaGeneracion;
        cadena = cadena + " | " + this.mensaje;
        if (this.horaAtencion != -1) {cadena = cadena + "  Hora de atención: " + this.horaAtencion;
            cadena = cadena + "  Retraso: " + getHorasRetraso() + " h";
            cadena = cadena + "  Crédito: Q" + String.format("%.2f", getCredito());
        }
        return cadena;
    }
}
