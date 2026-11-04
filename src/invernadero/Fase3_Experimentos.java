package invernadero;

public class Fase3_Experimentos {
    public static void main(String[] args) throws InterruptedException {
        experimentoA();
        experimentoB();
        experimentoC();
    }

    /** A: informe sin esperar y después de esperar. */
    private static void experimentoA() throws InterruptedException {
        System.out.println("\n=== EXPERIMENTO A · informe sin join() ===");
        Sensor temperatura = new Temperatura();
        Thread hilo = new Thread(temperatura, "sensor-Temperatura");
        hilo.start();

        System.out.println("Sin esperar   → " + temperatura.informe());

        hilo.join();
        System.out.println("Tras join()   → " + temperatura.informe());
    }

    /** B: run() en lugar de start(). */
    private static void experimentoB() {
        System.out.println("\n=== EXPERIMENTO B · riego.run() en vez de start() ===");
        ProgramadorRiego riego = new ProgramadorRiego(3);

        long inicio = System.currentTimeMillis();
        riego.run(); // NO crea hilo nuevo: lo ejecuta main
        long fin = System.currentTimeMillis();

        System.out.printf("Tiempo: %d ms%n", fin - inicio);
        System.out.println("Estado de riego: " + riego.getState());
    }

    /** C: llamar dos veces a start() sobre el mismo hilo. */
    private static void experimentoC() throws InterruptedException {
        System.out.println("\n=== EXPERIMENTO C · doble start() ===");
        ProgramadorRiego riego = new ProgramadorRiego(2);
        riego.start();
        riego.join();
        System.out.println("Estado tras el primer start() y join(): " + riego.getState());

        try {
            riego.start();
        } catch (IllegalThreadStateException e) {
            System.out.println("Excepción capturada: " + e);
        }
    }
}
