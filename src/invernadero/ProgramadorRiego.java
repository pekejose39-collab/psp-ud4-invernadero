package invernadero;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * El riego ES un hilo (extends Thread): se arranca directamente con start().
 */
public class ProgramadorRiego extends Thread {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final double LITROS_POR_APERTURA = 2.5;
    private static final long PERIODO_MS = 700;

    private final int ciclos;
    private int ciclosHechos = 0;
    private double litros = 0;

    public ProgramadorRiego(int ciclos) {
        super("riego"); // nombre del hilo, como en el constructor del Cohete
        this.ciclos = ciclos;
    }

    public ProgramadorRiego() {
        this(5);
    }

    @Override
    public void run() {
        for (int i = 1; i <= ciclos; i++) {
            boolean abierta = (i % 2 == 1); // abre, cierra, abre...
            if (abierta) {
                litros += LITROS_POR_APERTURA;
            }
            ciclosHechos++;

            // Ojo: uso Thread.currentThread() y no getName(). Si alguien llama
            // a run() directamente, el log mostrará el hilo que REALMENTE ejecuta.
            System.out.printf("%s [%-18s] Riego        ciclo %d: válvula %s (total %.1f L)%n",
                    LocalTime.now().format(HORA), Thread.currentThread().getName(), i,
                    abierta ? "ABIERTA" : "cerrada", litros);

            try {
                Thread.sleep(PERIODO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public String informe() {
        return String.format("%-12s → ciclos: %d | litros regados: %.1f L", "Riego", ciclosHechos, litros);
    }
}
