import java.io.*;
import java.util.ArrayList;

public class Aplicacao {
    static MyTimer timer = new MyTimer(8, 0);
    static String nomeArquivo = "DadosEmpacotadeira.txt";
    static int qtdePedidos;
    static String tempoListaCOMPrioridade;
    static String tempoListaSEMPrioridade;
    static String tempoTotalExecucao;
    static int qtdePacotesFeitos;
    static int qtdePedidosFinalizados;

    public static void main(String[] args) {
        ArrayList<Pedidos> listaPedidos = new ArrayList();
        ArrayList<Pedidos> listaMenorPrioridade = new ArrayList();
        BracoRobotico bracoRobotico = new BracoRobotico();
        Caminhao caminhao = new Caminhao();
        Esteira esteira = new Esteira();
        int hora;
        int minuto;
        long initTime = System.currentTimeMillis();

        criaPedidos(listaPedidos, listaMenorPrioridade);
        timer.incrementaSegundo((System.currentTimeMillis() - initTime) / 1000);

        hora = timer.hora;
        minuto = timer.minuto;
        startEmpacotamento(listaPedidos, bracoRobotico, caminhao, esteira);
        tempoListaCOMPrioridade = (timer.hora - hora) + "h" + (timer.minuto - minuto) + "m" + " -- Finalizado: " + timer.hora + "h" + timer.minuto + "m";

        hora = timer.hora;
        minuto = timer.minuto;
        startEmpacotamento(listaMenorPrioridade, bracoRobotico, caminhao, esteira);
        tempoListaSEMPrioridade = (timer.hora - hora) + "h" + (timer.minuto - minuto)  + "m";

        tempoTotalExecucao = timer.hora + "h" + timer.minuto  + "m";

        fazerRelatorio();
    }

    public static void fazerRelatorio() {
        ArquivoTextoEscrita arqEscrita = new ArquivoTextoEscrita();
        arqEscrita.abrirArquivo("relatório.md");
        arqEscrita.escrever("# Relatório");
        arqEscrita.escrever(" - Nome do Arquivo de pedidos: " + nomeArquivo);
        arqEscrita.escrever(" - Quantidade de pedidos: " + qtdePedidos);
        arqEscrita.escrever("## Tempo de Execução:");
        arqEscrita.escrever(" - Tempo lista COM prioridade: " + tempoListaCOMPrioridade);
        arqEscrita.escrever(" - Tempo lista SEM prioridade: " + tempoListaSEMPrioridade);
        arqEscrita.escrever(" - Tempo total: " + tempoTotalExecucao);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos às 12h:");
        arqEscrita.escrever(" - Quantidade de pacotes feitos: " + qtdePacotesFeitos);
        arqEscrita.escrever(" - Quantidade de pedidos feitos: " + qtdePedidosFinalizados);
        arqEscrita.fecharArquivo();
    }

    public static void startEmpacotamento(ArrayList<Pedidos> listaPedidos, BracoRobotico bracoRobotico, Caminhao caminhao, Esteira esteira) {
        boolean jaPassouMeioDia = false;
        for (int i = 0; i < listaPedidos.size(); i++) {
            Pedidos pedido = listaPedidos.get(i);
            double initTime = timer.minuto  * 60 + timer.segundo;
            esteira.rodaProdutos(20);
            while (!pedido.pedidoCompleto()) {
                Pacotes pacote = new Pacotes(pedido);
                pedido.pacotes.add(pacote);
                bracoRobotico.inserirProdutos(pedido, pacote, esteira, timer);
                transicaoPacoteEsteira(bracoRobotico, pacote, esteira, caminhao);
            }
            double finalTime = (timer.minuto  * 60 + timer.segundo);
            pedido.minutoFinalizado = (finalTime -initTime) / 60;

            if (!jaPassouMeioDia && timer.hora >= 12) {
                qtdePacotesFeitos = caminhao.pacotesCaminhao.size();
                jaPassouMeioDia = true;
            }
            else if (!jaPassouMeioDia)
                qtdePedidosFinalizados++;
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
        arq.abrirArquivo(nomeArquivo);
        qtdePedidos = Integer.parseInt(arq.ler());
        String[] ent;

        for (int i = 0; i < qtdePedidos; i++) {
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
