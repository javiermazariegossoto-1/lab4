
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {

		Scanner teclado = new Scanner(System.in);
        TorreGT torre = new TorreGT();
        torre.cargarDatosIniciales();
        int opcion = -1;

        while (opcion != 0) {
            int horaDelDia = torre.getHoraSimulacion() % 24;
            int dia = torre.getHoraSimulacion() / 24 + 1;
            System.out.println("\nTORRE GT | Hora simulada: " + torre.getHoraSimulacion());
            System.out.println("Día " + dia + " | Hora del día: " + horaDelDia + ":00");
            System.out.println("1. Registrar oficina");
            System.out.println("2. Registrar dispositivo");
            System.out.println("3. Encender dispositivo");
            System.out.println("4. Apagar dispositivo");
            System.out.println("5. Ajustar nivel");
            System.out.println("6. Registrar lectura");
            System.out.println("7. Registrar movimiento");
            System.out.println("8. Avanzar una hora");
            System.out.println("9. Atender la alerta más urgente");
            System.out.println("10. Mostrar oficinas");
            System.out.println("11. Mostrar dispositivos de una oficina");
            System.out.println("12. Mostrar alertas");
            System.out.println("13. Mostrar facturas y totales");
            System.out.println("0. Salir");


            try {
			opcion = teclado.nextInt();
            teclado.nextLine();
                if (opcion == 1) {
                    String codigo = leerTexto(teclado, "Código de oficina: ");
                    String nombre = leerTexto(teclado, "Nombre del inquilino: ");
                    torre.registrarOficina(codigo, nombre);
                    System.out.println("Oficina registrada.");
                } else if (opcion == 2) {
                    System.out.println("1. Luminaria | 2. Climatizador | 3. Inverter | 4. Sensor | 5. Cámara");
                    int tipo = leerEntero(teclado, "Tipo: ");
                    String codigo = leerTexto(teclado, "Código del dispositivo: ");
                    String oficina = leerTexto(teclado, "Código de oficina: ");
                    double potencia = leerDecimal(teclado, "Potencia nominal en watts: ");
                    torre.agregarDispositivo(tipo, codigo, oficina, potencia);
                    System.out.println("Dispositivo registrado.");
                } else if (opcion == 3) {
                    String codigo = leerTexto(teclado, "Código del dispositivo: ");
                    torre.encender(codigo);
                    System.out.println("Dispositivo encendido.");
                } else if (opcion == 4) {
                    String codigo = leerTexto(teclado, "Código del dispositivo: ");
                    torre.apagar(codigo);
                    System.out.println("Dispositivo apagado.");
                } else if (opcion == 5) {
                    String codigo = leerTexto(teclado, "Código del dispositivo: ");
                    int nivel = leerEntero(teclado, "Nivel: ");
                    torre.ajustarNivel(codigo, nivel);
                    System.out.println("Nivel ajustado.");
                } else if (opcion == 6) {
                    String codigo = leerTexto(teclado, "Código del dispositivo: ");
                    double lectura = leerDecimal(teclado, "Lectura (°C o ppm, según el dispositivo): ");
                    System.out.println(torre.registrarLectura(codigo, lectura));
                } else if (opcion == 7) {
                    String codigo = leerTexto(teclado, "Código de la cámara: ");
                    System.out.println(torre.registrarMovimiento(codigo));
                } else if (opcion == 8) {
                    System.out.println(torre.avanzarHora());
                } else if (opcion == 9) {
                    System.out.println(torre.atenderAlerta());
                } else if (opcion == 10) {
                    System.out.println(torre.mostrarOficinas());
                } else if (opcion == 11) {
                    String codigo = leerTexto(teclado, "Código de oficina: ");
                    System.out.println(torre.mostrarDispositivos(codigo));
                } else if (opcion == 12) {
                    System.out.println(torre.mostrarAlertas());
                } else if (opcion == 13) {
                    System.out.println(torre.mostrarFacturas());
                } else if (opcion != 0) {
                    System.out.println("Esa opción no existe.");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("Operación rechazada: " + error.getMessage());
            } catch (java.util.NoSuchElementException error) {
                opcion = 0; 
            }
        }
        System.out.println("Programa finalizado.");
        teclado.close();
    }

    public static String leerTexto(Scanner teclado, String mensaje) {
        System.out.print(mensaje);
        String texto = teclado.nextLine();
        texto = texto.trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede estar vacía.");
        }
        return texto;
    }

    public static int leerEntero(Scanner teclado, String mensaje) {
        String texto = leerTexto(teclado, mensaje);
        try {
            int numero = Integer.parseInt(texto);
            return numero;
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Escribe un número entero.");
        }
    }

    public static double leerDecimal(Scanner teclado, String mensaje) {
        String texto = leerTexto(teclado, mensaje);
        texto = texto.replace(',', '.');
        try {
            double numero = Double.parseDouble(texto);
            if (Double.isNaN(numero) || Double.isInfinite(numero)) {
                throw new NumberFormatException();
            }
            return numero;
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Escribe un número decimal.");
        }
    }
}
