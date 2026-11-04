package invernadero;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Sensor genérico. Es una TAREA (implements Runnable), no un hilo:
 * necesita que alguien le dé un Thread para ejecutarse en paralelo.
 */
public abstract class Sensor implements Runnable {

    protected static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final String nombre;
    private final String unidad;
    private final long periodoMs;
    private final int numLecturas;
    private final double umbralMin;
    private final double umbralMax;

    // Estadísticas propias de cada sensor (no compartidas)
    private int lecturas = 0;
    private double suma = 0;
    private double minimo = Double.MAX_VALUE;
    private double maximo = -Double.MAX_VALUE;
    private int alertas = 0;

    protected Sensor(String nombre, String unidad, long periodoMs, int numLecturas,
                     double umbralMin, double umbralMax) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.periodoMs = periodoMs;
        this.numLecturas = numLecturas;
        this.umbralMin = umbralMin;
        this.umbralMax = umbralMax;
    }

    /** Cada sensor concreto decide cómo genera su valor. */
    public abstract double medir();

    @Override
    public void run() {
        for (int i = 1; i <= numLecturas; i++) {
            double valor = medir();
            registrar(valor);

            boolean fueraDeRango = valor < umbralMin || valor > umbralMax;
            // Como Sensor no hereda de Thread, no tiene getName():
            // preguntamos qué hilo nos está ejecutando.
            String hilo = Thread.currentThread().getName();

            System.out.printf("%s [%-18s] %-12s lectura %d: %7.1f %s%s%n",
                    LocalTime.now().format(HORA), hilo, nombre, i, valor, unidad,
                    fueraDeRango ? "  ⚠ ALERTA" : "");

            try {
                Thread.sleep(periodoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void registrar(double valor) {
        lecturas++;
        suma += valor;
        minimo = Math.min(minimo, valor);
        maximo = Math.max(maximo, valor);
        if (valor < umbralMin || valor > umbralMax) {
            alertas++;
        }
    }

    public String informe() {
        if (lecturas == 0) {
            return String.format("%-12s → sin lecturas todavía", nombre);
        }
        return String.format("%-12s → lecturas: %d | media: %.1f %s | mín: %.1f | máx: %.1f | alertas: %d",
                nombre, lecturas, suma / lecturas, unidad, minimo, maximo, alertas);
    }

    public String getNombre() {
        return nombre;
    }
}
