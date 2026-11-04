package invernadero;

public class Fase4_Vigilante {
    public static void main(String[] args) throws InterruptedException {
        Thread hTemp = new Thread(new Temperatura(), "sensor-Temperatura");
        Thread hHum = new Thread(new Humedad(), "sensor-Humedad");
        Thread hLuz = new Thread(new Luz(), "sensor-Luz");
        ProgramadorRiego riego = new ProgramadorRiego();

        Thread[] invernadero = {hTemp, hHum, hLuz, riego};

        Thread vigilante = new Thread(() -> {
            while (true) {
                mostrarEstados("VIGILANTE", invernadero);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "vigilante");
        // Demonio: la JVM no espera por él. Hay que llamarlo ANTES de start().
        vigilante.setDaemon(true);

        mostrarEstados("ANTES DE ARRANCAR", invernadero);

        hTemp.start();
        hHum.start();
        hLuz.start();
        riego.start();
        vigilante.start();

        for (Thread t : invernadero) {
            t.join();
        }

        mostrarEstados("DESPUÉS DE ESPERAR", invernadero);
        System.out.println("main termina. El vigilante es demonio, así que la JVM se cierra.");
    }

    private static void mostrarEstados(String momento, Thread[] hilos) {
        StringBuilder sb = new StringBuilder(String.format(">> %-18s ", momento));
        for (Thread t : hilos) {
            sb.append(String.format("| %s=%s ", t.getName(), t.getState()));
        }
        System.out.println(sb);
    }
}
