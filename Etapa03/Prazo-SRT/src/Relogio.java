public class Relogio {
    MyTimer timerConj01;
    MyTimer timerConj02;
    MyTimer relogio;
    int horaInicio = 8;

    public Relogio(int hora, int minuto) {
        timerConj01 = new MyTimer(0,0, 0);
        timerConj02 = new MyTimer(0,0, 0);
        relogio = new MyTimer(hora, minuto, 0);
    }

    public void atualizarRelogio() {
        double segundosConj01 = (timerConj01.hora * 3600) + (timerConj01.minuto * 60) + timerConj01.segundo;
        double segundosConj02 = (timerConj02.hora * 3600) + (timerConj02.minuto * 60) + timerConj02.segundo;

        if (segundosConj01 > segundosConj02)
            relogio = new MyTimer(timerConj01.hora + horaInicio, timerConj01.minuto, timerConj01.segundo);
        else
            relogio = new MyTimer(timerConj02.hora + horaInicio, timerConj02.minuto, timerConj02.segundo);
    }

    public int getTempoMinutosSemHorasIniciais() {
        return (relogio.hora - horaInicio) * 60 + relogio.minuto;
    }
}
