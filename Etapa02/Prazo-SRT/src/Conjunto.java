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
    MyTimer timer;
    SyncList lists;
    ArrayList<Pedidos> listaPedidosFinalizados;
    Relatorio relatorio;
    Caminhao caminhao;
    int qtdPedidos;

    public Conjunto(int id, Semaphore listLock, String nomeArquivo, ArquivoTextoLeitura arq, MyTimer timer, SyncList lists, ArrayList<Pedidos> listaPedidosFinalizados, Caminhao caminhao) {
        this.id = id;
        this.listLock = listLock;
        this.nomeArquivo = nomeArquivo;
        this.arq = arq;
        this.esteira = new Esteira();
        this.bracoRobotico = new BracoRobotico();
        this.timer = timer;
        this.lists = lists;
        this.listaPedidosFinalizados = listaPedidosFinalizados;
        this.relatorio = new Relatorio();
        this.caminhao = caminhao;
    }

    @Override
    public void run() {
        arq.abrirArquivo(nomeArquivo);
        qtdPedidos = Integer.parseInt(arq.ler());
        relatorio.qtdePedidos = qtdPedidos;
        int horaInicio;
        int minutoInicio;

        horaInicio = timer.hora;
        minutoInicio = timer.minuto;
        try {
            startEmpacotamento(bracoRobotico, caminhao, esteira);
        } catch (Exception e) {
            e.printStackTrace();
        }

        relatorio.setTempoTotalExecucao(horaInicio, minutoInicio, timer.hora, timer.minuto);
        relatorio.criarRelatorio(nomeArquivo);
    }

    public Pedidos getMyPedido() throws InterruptedException {
        Pedidos result = new Pedidos();
        if (lists.getSizeListVip() == 0 && this.getName().equals("Thread-0")) {
            result = getPedido();
        } else if (lists.getSizeListVip() >= 0 && this.getName().equals("Thread-0")){
            result = lists.removeListVip(0);
        }
        else if (lists.getSizeListCommon() == 0 && this.getName().equals("Thread-1")) {
            result = getPedido();
        }
        else if (lists.getSizeListCommon() >= 0 && this.getName().equals("Thread-1")){
            result = lists.removeListCommon(0);
        }
        return result;
    }

    public void startEmpacotamento(BracoRobotico bracoRobotico, Caminhao caminhao, Esteira esteira) throws Exception {
        for (int i = 0; i < qtdPedidos; i++) {
            listLock.acquire();
                Pedidos pedido = getMyPedido();
                    listLock.release();
            if (pedido != null) {
                double initTime = timer.hora * 3600 + timer.minuto  * 60 + timer.segundo;
                esteira.rodaProdutos(20);
                while (!pedido.pedidoCompleto()) {
                    Pacotes pacote = new Pacotes(pedido);
                    pedido.pacotes.add(pacote);
                    bracoRobotico.inserirProdutos(pedido, pacote, esteira, timer);
                    transicaoPacoteEsteira(bracoRobotico, pacote, esteira, caminhao);
                }
                System.out.println(id + " " + i + " " + pedido.prazo + " " + pedido.nome + " " + pedido.qtdeProdutosPedido);
                listaPedidosFinalizados.add(pedido);

                double finalTime = (timer.hora * 3600 + timer.minuto  * 60 + timer.segundo);
                pedido.minutoFinalizado = pedido.prazo == 0 ? true : (((finalTime -initTime) / 60) < pedido.prazo);
                relatorio.makeRelatorioPedido(pedido, caminhao, timer);
            }
        }

        /*for (int i = 0; i < listaPedidosFinalizados.size(); i++) {
            Pedidos aux = listaPedidosFinalizados.get(i);
            System.out.println(i + " " + aux.prazo + " " + aux.nome + " " + aux.qtdeProdutosPedido);
        }*/
    }

    public Pedidos getPedido() throws NullPointerException {
        String linhaArq = arq.ler();
        try {
            String[] ent = {"", "", "", ""};
            if (linhaArq != null)
                ent = linhaArq.split(";");
            int minutoAtual = (timer.hora - 8) * 60 + (timer.minuto);
            Pedidos pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]), Integer.parseInt(ent[3]));
            int minutoChegadaPedido = pd.horaChegada;
            if (pd.prazo == 20)
                lists.addToListVip(pd);
            else
                lists.addToListCommon(pd);

            while (minutoAtual >= minutoChegadaPedido /*&& listaPrincipal.getSize() < qtdPedidos*/) {
                linhaArq = arq.ler();
                if (linhaArq != null) {
                    ent = linhaArq.split(";");
                    pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]), Integer.parseInt(ent[3]));
                    minutoChegadaPedido = pd.horaChegada;
                    if (pd.prazo == 20)
                        lists.addToListVip(pd);
                    else
                        lists.addToListCommon(pd);
                }
            }
            lists.organizaPedidos();
            if (this.getName().equals("Thread-0"))
                return lists.removeListVip(0);
            else
                return lists.removeListCommon(0);
        } catch (Exception e) {

        }
        return null;
    }

    private void transicaoPacoteEsteira(BracoRobotico bracoRobotico, Pacotes pacote, Esteira esteira, Caminhao caminhao) throws IllegalAccessException {
        double tempoTransicacao = 0.5;
        bracoRobotico.colocarPacoteNaEsteira(pacote, caminhao, timer);
        esteira.rodaProdutos(20);
        timer.incrementaSegundo(tempoTransicacao);
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
