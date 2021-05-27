import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Aplicacao {
    static MyTimer timer = new MyTimer(8, 0);
    static String nomeArquivo = "DadosEmpacotadeira_2.txt";
    static ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
    static SyncList listas = new SyncList();
    static ArrayList<Pedidos> listaPedidosFinalizados = new ArrayList();

    public static void main(String[] args) throws Exception {
        Caminhao caminhao = new Caminhao();
        Semaphore listLock = new Semaphore(1);
        arq.abrirArquivo(nomeArquivo);
        int qtdPedidos = Integer.parseInt(arq.ler());
        Conjunto c1 = new Conjunto(0, listLock, nomeArquivo, arq, timer, listas, listaPedidosFinalizados, qtdPedidos, caminhao);
        Conjunto c2 = new Conjunto(1, listLock, nomeArquivo, arq, timer, listas, listaPedidosFinalizados, qtdPedidos, caminhao);

        try {
            c1.start();
            c2.start();
            c1.join();
            c2.join();
        }catch(Exception ie){};

        for (int i = 0; i < listaPedidosFinalizados.size(); i++) {
            Pedidos aux = listaPedidosFinalizados.get(i);
            System.out.println(i + " " + aux.prazo + " " + aux.nome + " " + aux.qtdeProdutosPedido + " " + aux.horaChegada);
        }

    }
}
