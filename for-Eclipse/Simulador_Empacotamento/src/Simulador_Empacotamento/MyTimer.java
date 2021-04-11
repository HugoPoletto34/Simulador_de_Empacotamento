package Simulador_Empacotamento;
import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
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
}