package src;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Aplicacao {
    public static void main(String[] args) throws Exception {
        Caminhao caminhao = new Caminhao();
        Semaphore listLock = new Semaphore(1);
        String nomeArquivo = "DadosEmpacotadeira_2.txt";
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        SyncList listas = new SyncList();
        ArrayList<Pedidos> listaPedidosVipsFinalizados = new ArrayList<Pedidos>();
        ArrayList<Pedidos> listaPedidosCommonsFinalizados = new ArrayList<Pedidos>();
        arq.abrirArquivo(nomeArquivo);
        int qtdPedidos = Integer.parseInt(arq.ler());

        Relogio relogio = new Relogio(8, 0);
        Conjunto c1 = new Conjunto(0, listLock, nomeArquivo, arq, listas, listaPedidosVipsFinalizados, listaPedidosCommonsFinalizados, qtdPedidos, relogio, caminhao);
        Conjunto c2 = new Conjunto(1, listLock, nomeArquivo, arq, listas, listaPedidosVipsFinalizados, listaPedidosCommonsFinalizados, qtdPedidos, relogio, caminhao);

        try {
            c1.start();
            c2.start();            
            c1.join();
            c2.join();
        }catch(Exception ie){};
        
        relatorioGeral(listaPedidosVipsFinalizados, listaPedidosCommonsFinalizados, relogio);
    }

    public static void relatorioGeral(ArrayList<Pedidos> listaPedidosVipsFinalizados, ArrayList<Pedidos> listaPedidosCommonsFinalizados, Relogio relogio) {
        ArquivoTextoEscrita arq = new ArquivoTextoEscrita();
        arq.abrirArquivo("relatorio_geral.md");
        arq.escrever("# Relatório Geral");
        arq.escrever(" - Quantidade Pedidos vips: " + listaPedidosVipsFinalizados.size());
        arq.escrever(" - Quantidade Pedidos comuns: " + listaPedidosCommonsFinalizados.size());
        arq.escrever(" - Quantidade total de Pedidos analizados: " + (listaPedidosVipsFinalizados.size() + listaPedidosCommonsFinalizados.size()));
        arq.escrever(" - Expediente total: 8h0m -> " + relogio.relogio.hora + "h" + relogio.relogio.minuto + "s");
        arq.fecharArquivo();
    }
}
