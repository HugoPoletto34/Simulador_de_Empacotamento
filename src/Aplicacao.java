import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Aplicacao {
    public static void main(String[] args) {
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        MyTimer timer = new MyTimer();
        arq.abrirArquivo("DadosEmpacotadeira.txt");
        int tam = Integer.parseInt(arq.ler());
        String[] ent;
        Pedidos[] listaPedidos = new Pedidos[tam];
        int qtdePedidos = 0;
        //timer.initTimer();

        for (int i = 0; i < tam; i++) {
            ent = arq.ler().split(";");
            listaPedidos[i] = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]));
            qtdePedidos++;
            listaPedidos[i].organizaPedidos(qtdePedidos, listaPedidos, i);
        }


        arq.fecharArquivo();
    }
}
