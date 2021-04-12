import java.io.*;
import java.util.ArrayList;

public class Aplicacao {
    static MyTimer timer = new MyTimer(8, 0);

    public static void main(String[] args) {
        ArrayList<Pedidos> listaPedidos = new ArrayList();
        ArrayList<Pedidos> listaMenorPrioridade = new ArrayList();
        BracoRobotico bracoRobotico = new BracoRobotico();
        Caminhao caminhao = new Caminhao();
        Esteira esteira = new Esteira();

        long initTime = System.currentTimeMillis();

        criaPedidos(listaPedidos, listaMenorPrioridade);
        timer.incrementaSegundo((System.currentTimeMillis() - initTime) / 1000);
        startEmpacotamento(listaPedidos, bracoRobotico, caminhao, esteira);
        startEmpacotamento(listaMenorPrioridade, bracoRobotico, caminhao, esteira);
    }

    public static void startEmpacotamento(ArrayList<Pedidos> listaPedidos, BracoRobotico bracoRobotico, Caminhao caminhao, Esteira esteira) {
        for (int i = 0; i < listaPedidos.size(); i++) {
            Pedidos pedido = listaPedidos.get(i);
            Pacotes pacote = new Pacotes(pedido);

            pedido.pacotes.add(pacote);
            esteira.rodaProdutos(20);
            bracoRobotico.inserirProdutos(pedido, pacote, esteira, timer);
            transicaoPacoteEsteira(bracoRobotico, pacote, esteira, caminhao);

            while (!pedido.pedidoCompleto()) {
                Pacotes novoPacote = new Pacotes(pedido);
                pedido.pacotes.add(novoPacote);
                bracoRobotico.inserirProdutos(pedido, novoPacote, esteira, timer);
                transicaoPacoteEsteira(bracoRobotico, pacote, esteira, caminhao);
            }
        }
    }

    private static void transicaoPacoteEsteira(BracoRobotico bracoRobotico, Pacotes pacote, Esteira esteira, Caminhao caminhao) {
        double tempoTransicacao = 0.5;
        bracoRobotico.colocarPacoteNaEsteira(pacote, caminhao, timer);
        esteira.rodaProdutos(20);
        timer.incrementaSegundo(tempoTransicacao);
    }

    public static void criaPedidos(ArrayList<Pedidos> listaPedidos,  ArrayList<Pedidos> listaMenorPrioridade) {
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        arq.abrirArquivo("DadosEmpacotadeira.txt");
        int tam = Integer.parseInt(arq.ler());
        String[] ent;

        for (int i = 0; i < tam; i++) {
            ent = arq.ler().split(";");
            Pedidos pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]));
            if (pd.prazo != 0) {
                listaPedidos.add(pd);
                pd.organizaPedidos(listaPedidos);
            }
            else {
                listaMenorPrioridade.add(pd);
                pd.organizaPedidos(listaMenorPrioridade);
            }
        }
        arq.fecharArquivo();
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