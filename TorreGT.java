import java.util.ArrayList;


public class TorreGT {
    private ArrayList<Oficina> listaOficinas;
    private BandejaAlertas bandeja;
    private ArrayList<Alerta> alertasAtendidas;
    private int horaSimulacion;
    private int siguienteCodigo;

    public TorreGT() {
        this.listaOficinas = new ArrayList<Oficina>();
        this.bandeja = new BandejaAlertas();
        this.alertasAtendidas = new ArrayList<Alerta>();
        this.horaSimulacion = 0;
        this.siguienteCodigo = 1;
    }

    public int getHoraSimulacion() {
        return this.horaSimulacion;
    }

    public int getCantidadPendientes() {
        return this.bandeja.cantidad();
    }

    public Iterable<Alerta> getAlertasPendientes() {
        return this.bandeja;
    }


    public Oficina buscarOficina(String codigo) {
        for (Oficina oficina : this.listaOficinas) {
            if (oficina.getCodigoOficina().equals(codigo)) {
                return oficina;
            }
        }
        return null;
    }

    public Dispositivo buscarDispositivo(String codigo) {
        for (Oficina oficina : this.listaOficinas) {
            Dispositivo dispositivo = oficina.buscarDispositivo(codigo);
            if (dispositivo != null) {
                return dispositivo;
            }
        }
        return null;
    }

    public void registrarOficina(String codigo, String inquilino) {
        Oficina nueva = new Oficina(codigo, inquilino);
        if (buscarOficina(nueva.getCodigoOficina()) != null) {
            throw new IllegalArgumentException("Ese código de oficina ya existe.");
        }
        this.listaOficinas.add(nueva);
    }


    public void agregarDispositivo(int tipo, String codigo, String oficina, double potencia) {
        if (codigo == null || oficina == null) {
            throw new IllegalArgumentException("Escribe los códigos del dispositivo y de la oficina.");
        }
        codigo = codigo.trim();
        oficina = oficina.trim();
        Oficina destino = buscarOficina(oficina);
        if (destino == null) {
            throw new IllegalArgumentException("No existe la oficina indicada.");
        }
        if (buscarDispositivo(codigo) != null) {
            throw new IllegalArgumentException("Ese código de dispositivo ya existe.");
        }

        Dispositivo nuevo;
        if (tipo == 1) {
            nuevo = new Luminaria(codigo, oficina, potencia);
        } else if (tipo == 2) {
            nuevo = new Climatizador(codigo, oficina, potencia);
        } else if (tipo == 3) {
            nuevo = new Inverter(codigo, oficina, potencia);
        } else if (tipo == 4) {
            nuevo = new Sensor(codigo, oficina, potencia);
        } else if (tipo == 5) {
            nuevo = new Camara(codigo, oficina, potencia);
        } else {
            throw new IllegalArgumentException("El tipo debe estar entre 1 y 5.");
        }
        destino.agregarDispositivo(nuevo);
    }


    private Dispositivo pedirDispositivo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Escribe un código de dispositivo.");
        }
        Dispositivo dispositivo = buscarDispositivo(codigo.trim());
        if (dispositivo == null) {
            throw new IllegalArgumentException("No existe ese dispositivo.");
        }
        return dispositivo;
    }

    public void encender(String codigo) {
        Dispositivo dispositivo = pedirDispositivo(codigo);
        dispositivo.encender();
    }

    public void apagar(String codigo) {
        Dispositivo dispositivo = pedirDispositivo(codigo);
        dispositivo.apagar();
    }

    public void ajustarNivel(String codigo, int nivel) {
        Dispositivo dispositivo = pedirDispositivo(codigo);
        dispositivo.ajustarNivel(nivel);
    }

    public String registrarLectura(String codigo, double lectura) {
        Dispositivo dispositivo = pedirDispositivo(codigo);
        dispositivo.registrarLectura(lectura);
        return crearAlerta(dispositivo);
    }

    public String registrarMovimiento(String codigo) {
        Dispositivo dispositivo = pedirDispositivo(codigo);
        int horaDelDia = this.horaSimulacion % 24;
        dispositivo.registrarMovimiento(horaDelDia);
        return crearAlerta(dispositivo);
    }

    private String crearAlerta(Dispositivo dispositivo) {
        String severidad = dispositivo.alertar();
        if (severidad.equals("")) {
            return "Registro aceptado. No se generó una alerta.";
        }
        String mensaje = dispositivo.generarReporte();
        Alerta alerta = new Alerta(this.siguienteCodigo, dispositivo,
                this.horaSimulacion, severidad, mensaje);
        this.bandeja.agregar(alerta);
        this.siguienteCodigo = this.siguienteCodigo + 1;
        return "Alerta creada: " + alerta.toString();
    }

    public String avanzarHora() {
        int horaDelDia = this.horaSimulacion % 24;
        for (Oficina oficina : this.listaOficinas) {
            oficina.acumularConsumo(horaDelDia);
        }
        this.horaSimulacion = this.horaSimulacion + 1;
        String cadena = "Hora actual: " + this.horaSimulacion + "\n";

        if (this.horaSimulacion % 24 == 0) {
            int diaCerrado = this.horaSimulacion / 24;
            for (Oficina oficina : this.listaOficinas) {
                cadena = cadena + oficina.cerrarDiaYFacturar(diaCerrado) + "\n";
            }
        }
        return cadena;
    }

    public Alerta atenderAlerta() {
        Alerta alerta = this.bandeja.retirarPrimera();
        alerta.atender(this.horaSimulacion);
        String codigoOficina = alerta.getDispositivoOrigen().getOficina();
        Oficina oficina = buscarOficina(codigoOficina);
        oficina.agregarCredito(alerta.getCredito());
        this.alertasAtendidas.add(alerta);
        return alerta;
    }

    public String mostrarOficinas() {
        String cadena = "";
        for (Oficina oficina : this.listaOficinas) {
            cadena = cadena + oficina.toString() + "\n";
        }
        if (cadena.equals("")) {
            cadena = "No hay oficinas.\n";
        }
        return cadena;
    }

    public String mostrarDispositivos(String codigoOficina) {
        Oficina oficina = buscarOficina(codigoOficina.trim());
        if (oficina == null) {
            throw new IllegalArgumentException("No existe esa oficina.");
        }
        return oficina.toString() + "\n" + oficina.mostrarDispositivos();
    }

    public String mostrarAlertas() {
        String cadena = "PENDIENTES, DE MAYOR A MENOR URGENCIA:\n";
        if (this.bandeja.cantidad() == 0) {
            cadena = cadena + "Ninguna.\n";
        }
        for (Alerta alerta : this.bandeja) {
            cadena = cadena + alerta.toString() + "\n";
        }
        cadena = cadena + "ATENDIDAS:\n";
        if (this.alertasAtendidas.isEmpty()) {
            cadena = cadena + "Ninguna.\n";
        }
        for (Alerta alerta : this.alertasAtendidas) {
            cadena = cadena + alerta.toString() + "\n";
        }
        cadena = cadena + "ATENDIDAS CON RETRASO:\n";
        boolean hayRetraso = false;
        for (Alerta alerta : this.alertasAtendidas) {
            if (alerta.getHorasRetraso() > 0) {
                cadena = cadena + alerta.toString() + "\n";
                hayRetraso = true;
            }
        }
        if (!hayRetraso) {
            cadena = cadena + "Ninguna.\n";
        }
        return cadena;
    }

    public String mostrarFacturas() {
        String cadena = "";
        double valle = 0;
        double normal = 0;
        double punta = 0;
        double creditos = 0;
        double total = 0;
        for (Oficina oficina : this.listaOficinas) {
            cadena = cadena + oficina.mostrarFacturas() + "\n";
            valle = valle + oficina.getTotalValle();
            normal = normal + oficina.getTotalNormal();
            punta = punta + oficina.getTotalPunta();
            creditos = creditos + oficina.getTotalCreditos();
            total = total + oficina.getTotalFacturado();
        }
        cadena = cadena + "TOTALES DE TODAS LAS FACTURAS:\n";
        cadena = cadena + "Montos por franja antes de créditos:\n";
        cadena = cadena + "Valle: Q" + String.format("%.2f", valle) + "\n";
        cadena = cadena + "Normal: Q" + String.format("%.2f", normal) + "\n";
        cadena = cadena + "Punta: Q" + String.format("%.2f", punta) + "\n";
        cadena = cadena + "Créditos aplicados: Q" + String.format("%.2f", creditos) + "\n";
        cadena = cadena + "Total facturado: Q" + String.format("%.2f", total) + "\n";
        return cadena;
    }

    public void cargarDatosIniciales() { //Carga inicial 
        registrarOficina("O101", "Estudio Aurora");
        registrarOficina("O102", "Soluciones Maya");
        registrarOficina("O103", "Consultores del Valle");
        agregarDispositivo(1, "L01", "O101", 100);
        agregarDispositivo(1, "L02", "O102", 80);
        agregarDispositivo(2, "CL01", "O101", 1500);
        agregarDispositivo(2, "CL02", "O103", 1200);
        agregarDispositivo(3, "I01", "O102", 1500);
        agregarDispositivo(3, "I02", "O103", 1000);
        agregarDispositivo(4, "S01", "O101", 5);
        agregarDispositivo(4, "S02", "O102", 5);
        agregarDispositivo(5, "CA01", "O102", 20);
        agregarDispositivo(5, "CA02", "O103", 25);
    }
}
