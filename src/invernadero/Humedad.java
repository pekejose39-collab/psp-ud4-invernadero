package invernadero;

import java.util.concurrent.ThreadLocalRandom;

public class Humedad extends Sensor {
    public Humedad() {
        super("Humedad", "%", 600, 5, 50, 80);
    }

    @Override
    public double medir() {
        return ThreadLocalRandom.current().nextDouble(40.0, 90.0);
    }
}
