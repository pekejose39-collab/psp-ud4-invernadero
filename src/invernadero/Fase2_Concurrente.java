package invernadero;

public class Fase2_Concurrente {
    public static void main(String[] args) throws InterruptedException {
        Sensor temperatura = new Temperatura();
        Sensor humedad = new Humedad();
        Sensor luz = new Luz();
        ProgramadorRiego riego = new ProgramadorRiego();

        // Los sensores son tareas: hay que meterlos en un Thread
        Thread hTemp = new Thread(temperatura, "sensor-Temperatura");
        Thread hHum = new Thread(humedad, "sensor-Humedad");
        Thread hLuz = new Thread(luz, "sensor-Luz");
        // El riego ya es un hilo: no necesita new Thread(...)

        System.out.println("=== FASE 2 · VERSIÓN CONCURRENTE ===");
        long inicio = System.currentTimeMillis();

        hTemp.start();
        hHum.start();
        hLuz.start();
        riego.start();

        // main espera a que terminen los cuatro antes de seguir
        hTemp.join();
        hHum.join();
        hLuz.join();
        riego.join();

        long fin = System.currentTimeMillis();

        System.out.println("\n=== INFORME ===");
        System.out.println(temperatura.informe());
        System.out.println(humedad.informe());
        System.out.println(luz.informe());
        System.out.println(riego.informe());
        System.out.printf("Tiempo total: %d ms%n", fin - inicio);
    }
}
