package invernadero;

public class Fase1_Secuencial {
    public static void main(String[] args) {
        Sensor temperatura = new Temperatura();
        Sensor humedad = new Humedad();
        Sensor luz = new Luz();
        ProgramadorRiego riego = new ProgramadorRiego();

        System.out.println("=== FASE 1 · VERSIÓN SECUENCIAL ===");
        long inicio = System.currentTimeMillis();

        // Llamamos a run() a propósito: se ejecuta en el hilo main, uno detrás de otro
        temperatura.run();
        humedad.run();
        luz.run();
        riego.run();

        long fin = System.currentTimeMillis();

        System.out.println("\n=== INFORME ===");
        System.out.println(temperatura.informe());
        System.out.println(humedad.informe());
        System.out.println(luz.informe());
        System.out.println(riego.informe());
        System.out.printf("Tiempo total: %d ms%n", fin - inicio);
    }
}
