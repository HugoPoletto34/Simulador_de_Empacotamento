import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Conjunto extends Thread{
    int id;
    Semaphore listLock;
    String nomeArquivo;
    ArquivoTextoLeitura arq;
    Esteira esteira;
    BracoRobotico bracoRobotico;
    ControleContainers controleContainers;
    Relogio relogio;
    MyTimer timer;
    SyncList lists;
    ArrayList<Pedidos> listaPedidosVipsFinalizados;
    ArrayList<Pedidos> listaPedidosCommonsFinalizados;
    Relatorio relatorio;
    Caminhao caminhao;
    int qtdPedidos;

    public Conjunto(int id, Semaphore listLock, String nomeArquivo, ArquivoTextoLeitura arq, ControleContainers controleContainers, SyncList lists, ArrayList<Pedidos> listaPedidosVipsFinalizados, ArrayList<Pedidos> listaPedidosCommonsFinalizados, int qtdPedidos, Relogio relogio, Caminhao caminhao) {
        this.id = id;
        this.listLock = listLock;
        this.nomeArquivo = nomeArquivo;
        this.arq = arq;
        this.esteira = new Esteira();
        this.bracoRobotico = new BracoRobotico();
        this.relogio = relogio;
        this.controleContainers = controleContainers;
        if (getName().equals("Thread-0"))
            this.timer = relogio.timerConj01;
        else
            this.timer = relogio.timerConj02;
        this.lists = lists;
        this.listaPedidosVipsFinalizados = listaPedidosVipsFinalizados;
        this.listaPedidosCommonsFinalizados = listaPedidosCommonsFinalizados;
        this.qtdPedidos = qtdPedidos;
        this.relatorio = new Relatorio();
        this.caminhao = caminhao;
    }

    @Override
    public void run() {
        try {
            startEmpacotamento(bracoRobotico, caminhao, esteira);
        } catch (Exception e) {
            e.printStackTrace();
        }

        relatorio.setTempoTotalExecucao(timer.hora, timer.minuto);
        relatorio.criarRelatorio(id, nomeArquivo);
        System.out.println("---------------Processo do Conjunto-0" + (id + 1) + " Finalizado---------------");
    }

    public void startEmpacotamento(BracoRobotico bracoRobotico, Caminhao caminhao, Esteira esteira) throws Exception {
        while ((listaPedidosCommonsFinalizados.size() + listaPedidosVipsFinalizados.size()) < qtdPedidos) {
            listLock.acquire();
                Pedidos pedido = getMyPedido();
                    listLock.release();
            if (pedido != null) {
                Pacotes pacote = new Pacotes(pedido);
                double initTime = timer.hora * 3600 + timer.minuto  * 60 + timer.segundo;
                int qtdProdutosPorPacote = pacote.quantosProdutosCabeNoPacote(pedido.produto.volume);
                Container c = controleContainers.getContainerEmUso(pedido.produto);

                if (c != null) {
                    esteira.rodaProdutos(c.retirarProdutos(qtdProdutosPorPacote), controleContainers);
                    while (!pedido.pedidoCompleto()) {
                        pedido.pacotes.add(pacote);
                        bracoRobotico.inserirProdutos(pedido, pacote, esteira, timer, relogio);
                        transicaoPacoteEsteira(qtdProdutosPorPacote, c, bracoRobotico, pacote, esteira, caminhao);
                        pacote = new Pacotes(pedido);
                    }
                    //System.out.println("Thread-" + id + " - " + i + " " + pedido.prazo + " " + pedido.nome + " " + pedido.qtdeProdutosPedido + " " + pedido.horaChegada);
                    if (pedido.prazo <= 50 && pedido.prazo != 0)
                        listaPedidosVipsFinalizados.add(pedido);
                    else
                        listaPedidosCommonsFinalizados.add(pedido);

                    double finalTime = (timer.hora * 3600 + timer.minuto * 60 + timer.segundo);
                    pedido.minutoFinalizado = pedido.prazo == 0 || (((finalTime - initTime) / 60) < pedido.prazo);
                    relatorio.makeRelatorioPedido(pedido, caminhao, timer, relogio);
                }
            }
        }
    }

    public Pedidos getMyPedido() {
        Pedidos result = null;
        if (lists.getSizeListVip() == 0 && this.getName().equals("Thread-0")) {
            result = getPedido();
        } else if (lists.getSizeListVip() >= 0 && this.getName().equals("Thread-0")){
            result = controleContainers.buscarPedido(this.getName(), lists, timer);
        }
        else if (lists.getSizeListCommon() == 0 && this.getName().equals("Thread-1")) {
            result = getPedido();
        }
        else if (lists.getSizeListCommon() >= 0 && this.getName().equals("Thread-1")){
            result = controleContainers.buscarPedido(this.getName(), lists, timer);
        }
        return result;
    }

    private void incluirPedido(Pedidos pd) {
        if (pd.prazo <= 50 && pd.prazo != 0)
            lists.addToListVip(pd);
        else
            lists.addToListCommon(pd);
    }

    public Pedidos getPedido() throws NullPointerException {
        String linhaArq = arq.ler();
        try {
            String[] ent = {"", "", "", "", ""};
            if (linhaArq != null)
                ent = linhaArq.split(";");
            int minutoAtual = (relogio.relogio.hora - 8) * 60 + (relogio.relogio.minuto);
            Pedidos pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]), Integer.parseInt(ent[3]), controleContainers.getProduto(Integer.parseInt(ent[4])));
            int minutoChegadaPedido = pd.minutoEntrado;
            incluirPedido(pd);

            while (minutoAtual >= minutoChegadaPedido && linhaArq != null) {
                linhaArq = arq.ler();
                if (linhaArq != null) {
                    ent = linhaArq.split(";");
                    pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]), Integer.parseInt(ent[3]), controleContainers.getProduto(Integer.parseInt(ent[4])));
                    minutoChegadaPedido = pd.minutoEntrado;
                    incluirPedido(pd);
                }
            }
            lists.organizaPedidos();
            if (controleContainers.vazio())
                controleContainers.encher(lists);

            return controleContainers.buscarPedido(this.getName(), lists, timer);

        } catch (Exception ignored) {

        }
        return null;
    }

    public void transicaoPacoteEsteira(int qtdProdutosPorPacote, Container c, BracoRobotico bracoRobotico, Pacotes pacote, Esteira esteira, Caminhao caminhao) {
        double tempoTransicacao = Esteira.getTempoMinutoTransicao();
        bracoRobotico.colocarPacoteNaEsteira(pacote, caminhao);
        esteira.rodaProdutos(c.retirarProdutos(qtdProdutosPorPacote), controleContainers);
        timer.incrementaSegundo(tempoTransicacao);
        relogio.atualizarRelogio();
    }
}

class ArquivoTextoLeitura {

    private BufferedReader entrada;

    public void abrirArquivo(String nomeArquivo){

        try {
            entrada = new BufferedReader(new FileReader(nomeArquivo));
        }
        catch (FileNotFoundException excecao) {
            System.out.println("Arquivo não encontrado");
        }
    }

    public void fecharArquivo() {

        try {
            entrada.close();
        }
        catch (IOException excecao) {
            System.out.println("Erro no fechamento do arquivo de leitura: " + excecao);
        }
    }

    public String ler() {

        String textoEntrada;

        try {
            textoEntrada = entrada.readLine();
        }
        catch (EOFException excecao) { //Exceção de final de arquivo.
            return null;
        }
        catch (IOException excecao) {
            System.out.println("Erro de leitura: " + excecao);
            return null;
        }
        return textoEntrada;
    }
}

class ArquivoTextoEscrita {

    private BufferedWriter saida;

    public void abrirArquivo(String nomeArquivo){

        try {
            saida = new BufferedWriter(new FileWriter(nomeArquivo));
        }
        catch (FileNotFoundException excecao) {
            System.out.println("Arquivo não encontrado");
        }
        catch (IOException excecao) {
            System.out.println("Erro na abertura do arquivo de escrita: " + excecao);
        }
    }

    public void fecharArquivo() {

        try {
            saida.close();
        }
        catch (IOException excecao) {
            System.out.println("Erro no fechamento do arquivo de escrita: " + excecao);
        }
    }

    public void escrever(String textoEntrada) {

        try {
            saida.write(textoEntrada);
            saida.newLine();
        }
        catch (IOException excecao){
            System.out.println("Erro de entrada/saída " + excecao);
        }
    }
}
