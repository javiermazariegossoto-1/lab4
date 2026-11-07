import java.util.ArrayList;
import java.util.Collections;

public class Oficina {
    private String codigoOficina;
    private String nombreInquilino;
    private ArrayList<Dispositivo> listaDispositivos;
    private ArrayList<String> listaFacturas;
    private double consumoValle;
    private double consumoNormal;
    private double consumoPunta;
    private double creditosDia;
    private double totalValle;
    private double totalNormal;
    private double totalPunta;
    private double totalCreditos;
    private double totalFacturado;

    public Oficina(String codigoOficina, String nombreInquilino) {
        if (codigoOficina == null || codigoOficina.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de oficina no puede estar vacío.");
        }
        if (nombreInquilino == null || nombreInquilino.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del inquilino no puede estar vacío.");
        }
        this.codigoOficina = codigoOficina.trim();
        this.nombreInquilino = nombreInquilino.trim();
        this.listaDispositivos = new ArrayList<Dispositivo>();
        this.listaFacturas = new ArrayList<String>();
        this.consumoValle = 0;
        this.consumoNormal = 0;
        this.consumoPunta = 0;
        this.creditosDia = 0;
        this.totalValle = 0;
        this.totalNormal = 0;
        this.totalPunta = 0;
        this.totalCreditos = 0;
        this.totalFacturado = 0;
    }

    public String getCodigoOficina() {
        return this.codigoOficina;
    }

    public double getConsumoValle() {
        return this.consumoValle;
    }

    public double getConsumoNormal() {
        return this.consumoNormal;
    }

    public double getConsumoPunta() {
        return this.consumoPunta;
    }

    public double getCreditosDia() {
        return this.creditosDia;
    }

    public double getTotalValle() {
        return this.totalValle;
    }

    public double getTotalNormal() {
        return this.totalNormal;
    }

    public double getTotalPunta() {
        return this.totalPunta;
    }

    public double getTotalCreditos() {
        return this.totalCreditos;
    }

    public double getTotalFacturado() {
        return this.totalFacturado;
    }

    public int getCantidadDispositivos() {
        return this.listaDispositivos.size();
    }

    public int getCantidadFacturas() {
        return this.listaFacturas.size();
    }

    public Dispositivo buscarDispositivo(String codigo) {
        for (Dispositivo dispositivo : this.listaDispositivos) {
            if (dispositivo.getCodigo().equals(codigo)) {
                return dispositivo;
            }
        }
        return null; 
    }

    void agregarDispositivo(Dispositivo dispositivo) {
        this.listaDispositivos.add(dispositivo);
    }

    void acumularConsumo(int horaDelDia) {
        double consumoHoraOficina = 0;
        for (Dispositivo dispositivo : this.listaDispositivos) {
            double consumo = dispositivo.consumoHora();
            dispositivo.acumularConsumo(consumo);
            consumoHoraOficina = consumoHoraOficina + consumo;
        }

        if (horaDelDia >= 22 || horaDelDia < 6) {
            this.consumoValle = this.consumoValle + consumoHoraOficina;
        } else if (horaDelDia < 18) {
            this.consumoNormal = this.consumoNormal + consumoHoraOficina;
        } else {
            this.consumoPunta = this.consumoPunta + consumoHoraOficina;
        }
    }

    void agregarCredito(double credito) {
        this.creditosDia = this.creditosDia + credito;
    }

    String cerrarDiaYFacturar(int dia) {
        double montoValle = this.consumoValle * 0.85;
        double montoNormal = this.consumoNormal * 1.40;
        double montoPunta = this.consumoPunta * 2.10;
        double subtotal = montoValle + montoNormal + montoPunta;

        double creditoAplicado = this.creditosDia;
        if (creditoAplicado > subtotal) {
            creditoAplicado = subtotal;
        }
        double total = subtotal - creditoAplicado;
        total = Math.round(total * 100.0) / 100.0;

        String factura = "Factura de oficina " + this.codigoOficina + " | Día " + dia + "\n";
        factura = factura + "Valle: " + String.format("%.4f", this.consumoValle);
        factura = factura + " kWh x Q0.85 = Q" + String.format("%.2f", montoValle) + "\n";
        factura = factura + "Normal: " + String.format("%.4f", this.consumoNormal);
        factura = factura + " kWh x Q1.40 = Q" + String.format("%.2f", montoNormal) + "\n";
        factura = factura + "Punta: " + String.format("%.4f", this.consumoPunta);
        factura = factura + " kWh x Q2.10 = Q" + String.format("%.2f", montoPunta) + "\n";
        factura = factura + "Subtotal: Q" + String.format("%.2f", subtotal) + "\n";
        factura = factura + "Créditos generados: Q" + String.format("%.2f", this.creditosDia) + "\n";
        factura = factura + "Créditos aplicados: Q" + String.format("%.2f", creditoAplicado) + "\n";
        factura = factura + "Total a pagar: Q" + String.format("%.2f", total) + "\n";

        // Guardamos el texto de la factura y sus montos históricos.
        this.listaFacturas.add(factura);
        this.totalValle = this.totalValle + montoValle;
        this.totalNormal = this.totalNormal + montoNormal;
        this.totalPunta = this.totalPunta + montoPunta;
        this.totalCreditos = this.totalCreditos + creditoAplicado;
        this.totalFacturado = this.totalFacturado + total;

        this.consumoValle = 0;
        this.consumoNormal = 0;
        this.consumoPunta = 0;
        this.creditosDia = 0;
        for (Dispositivo dispositivo : this.listaDispositivos) {
            dispositivo.reiniciarConsumo();
        }
        return factura;
    }

    public String mostrarDispositivos() {

        ArrayList<Dispositivo> copia = new ArrayList<Dispositivo>(this.listaDispositivos);
        Collections.sort(copia);
        String cadena = "";
        for (Dispositivo dispositivo : copia) {
            cadena = cadena + dispositivo.toString() + "\n";
        }
        if (cadena.equals("")) {
            cadena = "No hay dispositivos.\n";
        }
        cadena = cadena + "Consumo de hoy por franja:\n";
        cadena = cadena + "Valle: " + String.format("%.4f", this.consumoValle) + " kWh\n";
        cadena = cadena + "Normal: " + String.format("%.4f", this.consumoNormal) + " kWh\n";
        cadena = cadena + "Punta: " + String.format("%.4f", this.consumoPunta) + " kWh\n";
        cadena = cadena + "Créditos de hoy: Q" + String.format("%.2f", this.creditosDia) + "\n";
        return cadena;
    }

    public String mostrarFacturas() {
        String cadena = "";
        for (String factura : this.listaFacturas) {
            cadena = cadena + factura + "\n";
        }
        if (cadena.equals("")) {
            cadena = "Todavía no hay facturas para " + this.codigoOficina + ".\n";
        }
        return cadena;
    }

    @Override
    public String toString() {
        String cadena = this.codigoOficina + " | " + this.nombreInquilino;
        cadena = cadena + " | Dispositivos: " + this.listaDispositivos.size();
        return cadena;
    }
}
