import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Aplicacao {
    public static void main(String[] args) {
        String nomeArquivo = "../DadosEmpacotadeira_3.txt";
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        Semaphore listLock = new Semaphore(1);
        Semaphore containersLock = new Semaphore(1);
        Caminhao caminhao = new Caminhao();
        SyncList listas = new SyncList();
        ControleContainers controleContainers = new ControleContainers();
        ArrayList<Pedidos> listaPedidosVipsFinalizados = new ArrayList<>();
        ArrayList<Pedidos> listaPedidosCommonsFinalizados = new ArrayList<>();
        controleContainers.preencherListaProdutos();

        arq.abrirArquivo(nomeArquivo);
        int qtdPedidos = Integer.parseInt(arq.ler());

        Relogio relogio = new Relogio(8, 0);
        Conjunto c1 = new Conjunto(0, listLock, containersLock, nomeArquivo, arq, controleContainers, listas, listaPedidosVipsFinalizados, listaPedidosCommonsFinalizados, qtdPedidos, relogio, caminhao);
        Conjunto c2 = new Conjunto(1, listLock, containersLock, nomeArquivo, arq, controleContainers, listas, listaPedidosVipsFinalizados, listaPedidosCommonsFinalizados, qtdPedidos, relogio, caminhao);

        try {
            c1.start();
            c2.start();
            c1.join();
            c2.join();
        }catch(Exception ignored){}
        relatorioGeral(listaPedidosVipsFinalizados, listaPedidosCommonsFinalizados, relogio, listas, c1.relatorio);
    }

    public static void relatorioGeral(ArrayList<Pedidos> listaPedidosVipsFinalizados, ArrayList<Pedidos> listaPedidosCommonsFinalizados, Relogio relogio, SyncList listas,Relatorio relatorio) {
        ArquivoTextoEscrita arq = new ArquivoTextoEscrita();
        arq.abrirArquivo("relatorio_geral.md");
        arq.escrever("# Relatório Geral");
        arq.escrever(" - Quantidade Pedidos vips: " + listaPedidosVipsFinalizados.size());
        arq.escrever(" - Quantidade Pedidos comuns: " + listaPedidosCommonsFinalizados.size());
        arq.escrever(" - Quantidade de Trocas de containers: " + relatorio.qtdTrocasContainers);
        arq.escrever(" - Quantidade total de Pedidos analizados: " + (listaPedidosVipsFinalizados.size() + listaPedidosCommonsFinalizados.size()));
        arq.escrever(" - Quantidade total de Pedidos pendentes: " + (319 - (listaPedidosVipsFinalizados.size() + listaPedidosCommonsFinalizados.size())));
        arq.escrever("   - Num. pedidos vips: " + relatorio.quantosVipsEstaoPendentes(listas.listVip()));
        arq.escrever("   - Num. pedidos Comuns: " + relatorio.quantosComunsEstaoPendentes(listas.listCommon()));
        arq.escrever("   - Num. pedidos sem prazo: " + relatorio.quantosSemPrazoEstaoPendentes(listas.listCommon()));
        arq.escrever(" - Expediente total: 8h0m -> " + relogio.relogio.hora + "h" + relogio.relogio.minuto + "m");
        arq.fecharArquivo();
    }
}
