import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
    int hora;
    int minuto;
    double segundo;

    public MyTimer(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = 0;
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
}

/*public class MyTimer {
    public void initTimer () {
        Timer timer = new Timer();
        final int[] hora = {8};
        final int[] minuto = {0};
        final long intervalo = (1000 * 60);

        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                System.out.println((hora[0] < 10 ? "0" + hora[0] : hora[0]) + ":" + (minuto[0] < 10 ? "0" + minuto[0] : minuto[0]));
                if (intervalo % 60 == 0)
                    minuto[0]++;
                if (minuto[0] == 60) {
                    minuto[0] = 0;
                    hora[0]++;
                }

            }
        };

        timer.scheduleAtFixedRate(tarefa, 0, intervalo);
    }
}*/