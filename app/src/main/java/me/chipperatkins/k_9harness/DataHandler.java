package me.chipperatkins.k_9harness;

/**
 * Created by patrickatkins on 10/24/17.
 */

class DataHandler {

    /* Convert ADC value to degrees Fahrenheit */
    static double convertTemp(double rawValue) {
        double temp = 1023.0 / rawValue - 1.0;
        temp = 10000.0 / temp;
        double steinhart = temp / 10000.0;
        steinhart = Math.log(steinhart);
        steinhart = steinhart / 3950.0;
        steinhart = steinhart + 1.0 / (25.0 + 273.15);
        steinhart = 1.0 / steinhart;
        steinhart = steinhart * (9.0 / 5.0) - 459.67;
        return steinhart;
    }

    /* Calculate core tempurature from the abdominal, ambient, and chest temperatures */
    static int calculateCoreTemp(double abdominal, double ambient, double chest, double avgAmbient) {
        double C = 1;
        double Ka = (0.01203) * avgAmbient + -0.77285;
        double Kb = (0.05661) * avgAmbient + -3.60028;
        double Kc = (-0.07096) * avgAmbient + 5.64545;

        double coreTemp = (Ka * ambient) + (Kb * chest) + (Kc * abdominal) + C;
        return (int) coreTemp;
    }
}
