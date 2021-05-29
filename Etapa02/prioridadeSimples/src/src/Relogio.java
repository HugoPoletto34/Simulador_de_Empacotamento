package src;

public class Relogio {
    MyTimer timerConj01;
    MyTimer timerConj02;
    MyTimer relogio;

    public Relogio(int hora, int minuto) {
        timerConj01 = new MyTimer(0,0, 0);
        timerConj02 = new MyTimer(0,0, 0);
        relogio = new MyTimer(hora, minuto, 0);
    }

    public void atualizarRelogio() {
        double segundosConj01 = (timerConj01.hora * 3600) + (timerConj01.minuto * 60) + timerConj01.segundo;
        double segundosConj02 = (timerConj02.hora * 3600) + (timerConj02.minuto * 60) + timerConj02.segundo;

        if (segundosConj01 > segundosConj02)
            relogio = new MyTimer(timerConj01.hora + 8, timerConj01.hora, timerConj01.segundo);
        else
            relogio = new MyTimer(timerConj02.hora + 8, timerConj02.hora, timerConj02.segundo);
    }
}
