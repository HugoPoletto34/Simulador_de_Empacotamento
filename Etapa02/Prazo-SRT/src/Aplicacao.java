import java.io.*;
import java.util.ArrayList;

public class Aplicacao {
    static MyTimer timer = new MyTimer(8, 0);
    static Relatorio relatorio = new Relatorio();
    static String nomeArquivo = "DadosEmpacotadeira_2.txt";
    static ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
    static ArrayList<Pedidos> listaPrincipal = new ArrayList();
    static ArrayList<Pedidos> listaPedidosFinalizados = new ArrayList();
    static int qtdPedidos;


    public static void main(String[] args) throws Exception {
        arq.abrirArquivo(nomeArquivo);
        qtdPedidos = Integer.parseInt(arq.ler());
        relatorio.qtdePedidos = qtdPedidos;

        BracoRobotico bracoRobotico = new BracoRobotico();
        Caminhao caminhao = new Caminhao();
        Esteira esteira = new Esteira();
        int horaInicio;
        int minutoInicio;

        horaInicio = timer.hora;
        minutoInicio = timer.minuto;
        startEmpacotamento(bracoRobotico, caminhao, esteira);

        relatorio.setTempoTotalExecucao(horaInicio, minutoInicio, timer.hora, timer.minuto);
        relatorio.criarRelatorio(nomeArquivo);
    }

    public static void verificarListaPedidos() throws NullPointerException {
        String linhaArq = arq.ler();
        try {
            String[] ent = {"", "", "", ""};
            if (linhaArq != null)
                ent = linhaArq.split(";");
            int minutoAtual = (timer.hora - 8) * 60 + (timer.minuto);
            Pedidos pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]), Integer.parseInt(ent[3]));
            int minutoChegadaPedido = pd.horaChegada;
            listaPrincipal.add(pd);

            while (minutoAtual >= minutoChegadaPedido && listaPrincipal.size() < qtdPedidos) {
                linhaArq = arq.ler();
                if (linhaArq != null) {
                    ent = linhaArq.split(";");
                    pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]), Integer.parseInt(ent[3]));
                    minutoChegadaPedido = pd.horaChegada;
                    listaPrincipal.add(pd);
                }
            }
            pd.organizaPedidos(listaPrincipal);
        } catch (Exception e) {

        }

    };

    public static void startEmpacotamento(BracoRobotico bracoRobotico, Caminhao caminhao, Esteira esteira) throws Exception {
        for (int i = 0; i < qtdPedidos; i++) {
            verificarListaPedidos();
            Pedidos pedido = listaPrincipal.remove(0);

            double initTime = timer.hora * 3600 + timer.minuto  * 60 + timer.segundo;
            esteira.rodaProdutos(20);
            while (!pedido.pedidoCompleto()) {
                Pacotes pacote = new Pacotes(pedido);
                pedido.pacotes.add(pacote);
                bracoRobotico.inserirProdutos(pedido, pacote, esteira, timer);
                transicaoPacoteEsteira(bracoRobotico, pacote, esteira, caminhao);
            }
            listaPedidosFinalizados.add(pedido);

            double finalTime = (timer.hora * 3600 + timer.minuto  * 60 + timer.segundo);
            pedido.minutoFinalizado = pedido.prazo == 0 ? true : (((finalTime -initTime) / 60) < pedido.prazo);
            relatorio.makeRelatorioPedido(pedido, caminhao, timer);
        }

        for (int i = 0; i < listaPedidosFinalizados.size(); i++) {
            Pedidos aux = listaPedidosFinalizados.get(i);
            System.out.println(i + " " + aux.prazo + " " + aux.nome + " " + aux.qtdeProdutosPedido);
        }
    }

    private static void transicaoPacoteEsteira(BracoRobotico bracoRobotico, Pacotes pacote, Esteira esteira, Caminhao caminhao) throws IllegalAccessException {
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
