public class MyTimer {
    int hora;
    int minuto;
    double segundo;

    public MyTimer(int hora, int minuto, double segundo) {
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
    }

    public void incrementaMinuto(int minuto) {
        this.minuto += minuto;
        if (this.minuto >= 60) {
            this.hora++;
            this.minuto -= 60;
        }
    }

    public void incrementaSegundo(double segundo) {
        this.segundo += segundo;
        if (this.segundo >= 60) {
            this.minuto++;
            this.segundo -= 60;
        }
        if (this.minuto >= 60)
            incrementaMinuto(0);
    }

    public double tempoEmMinutos() {
        return this.hora * 60 + this.minuto + this.segundo;
    }
}