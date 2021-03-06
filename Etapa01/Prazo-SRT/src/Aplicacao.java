import java.io.*;
import java.util.ArrayList;

public class Aplicacao {
    static int horaInicio = 8;
    static MyTimer timer = new MyTimer(horaInicio, 0);
    static String nomeArquivo = "DadosEmpacotadeira.txt";
    static int qtdePedidos;
    static String tempoListaCOMPrioridade;
    static String tempoListaSEMPrioridade;
    static String tempoTotalExecucao;
    static int qtdePedidosFinalizadosAntesDoPrazo;
    static int qtdePacotesFeitosMeioDia;
    static int qtdePedidosFinalizadosMeioDia;
    static int qtdePacotesFeitos;
    static int qtdePedidosFinalizados;
    static double tempoGastoPedido;

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
        criarRelatorio();
    }

    public static void startEmpacotamento(ArrayList<Pedidos> listaPedidos, BracoRobotico bracoRobotico, Caminhao caminhao, Esteira esteira) {
        boolean jaPassouMeioDia = false;

        for (int i = 0; i < listaPedidos.size(); i++) {
            Pedidos pedido = listaPedidos.get(i);
            double initTime = timer.hora * 3600 + timer.minuto  * 60 + timer.segundo;

            esteira.rodaProdutos(20);
            while (!pedido.pedidoCompleto()) {
                Pacotes pacote = new Pacotes(pedido);
                pedido.pacotes.add(pacote);
                bracoRobotico.inserirProdutos(pedido, pacote, esteira, timer);
                transicaoPacoteEsteira(bracoRobotico, pacote, esteira, caminhao);
            }
            double finalTime = (timer.hora * 3600 + timer.minuto  * 60 + timer.segundo);

            pedido.minutoFinalizado = ((finalTime -initTime) / 60) < pedido.prazo;

            // Para relat??rio -- INICIO
            if (pedido.prazo != 0)
                qtdePedidosFinalizadosAntesDoPrazo += (pedido.minutoFinalizado ? 1 : 0);
            else
                qtdePedidosFinalizadosAntesDoPrazo++;

            if (!jaPassouMeioDia && timer.hora >= 12) {
                qtdePacotesFeitosMeioDia = caminhao.pacotesCaminhao.size();
                jaPassouMeioDia = true;
            }
            else if (!jaPassouMeioDia)
                qtdePedidosFinalizadosMeioDia++;
                
            tempoGastoPedido += ((timer.hora - horaInicio) * 3600 + timer.minuto  * 60 + timer.segundo);

            if (timer.hora <= 17) {
                qtdePacotesFeitos = caminhao.pacotesCaminhao.size();
                qtdePedidosFinalizados++;
            }
            // Para relat??rio -- FIM
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

    public static void criarRelatorio() {
        ArquivoTextoEscrita arqEscrita = new ArquivoTextoEscrita();
        arqEscrita.abrirArquivo("relat??rio.md");
        arqEscrita.escrever("# Relat??rio - Ordena????o pelo prazo + SRT");
        arqEscrita.escrever(" - Nome do Arquivo de pedidos: " + nomeArquivo);
        arqEscrita.escrever(" - Quantidade de pedidos: " + qtdePedidos);
        arqEscrita.escrever(" - Tempo m??dio gasto de pedidos em hora: " + (tempoGastoPedido / 3600) / qtdePedidos);
        arqEscrita.escrever(" - Quantidade de Pedidos finalizados antes do prazo estipulado: " + qtdePedidosFinalizadosAntesDoPrazo );
        arqEscrita.escrever("## Tempo de Execu????o:");
        arqEscrita.escrever(" - Expediente: 8h ?? 17h");
        arqEscrita.escrever(" - Tempo lista COM prioridade: " + tempoListaCOMPrioridade);
        arqEscrita.escrever(" - Tempo lista SEM prioridade: " + tempoListaSEMPrioridade);
        arqEscrita.escrever(" - Expediente total de empacotamento : " + tempoTotalExecucao);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos ??s 12h:");
        arqEscrita.escrever(" - Quantidade de pacotes feitos: " + qtdePacotesFeitosMeioDia);
        arqEscrita.escrever(" - Quantidade de pedidos feitos: " + qtdePedidosFinalizadosMeioDia);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos no caminh??o no fim do expediente (17h):");
        arqEscrita.escrever(" - Quantidade de pacotes: " + qtdePacotesFeitos);
        arqEscrita.escrever(" - Quantidade de pedidos finalizados: " + qtdePedidosFinalizados);

        arqEscrita.fecharArquivo();
    }
}

class ArquivoTextoLeitura {

    private BufferedReader entrada;

    public void abrirArquivo(String nomeArquivo){

        try {
            entrada = new BufferedReader(new FileReader(nomeArquivo));
        }
        catch (FileNotFoundException excecao) {
            System.out.println("Arquivo n??o encontrado");
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
        catch (EOFException excecao) { //Exce????o de final de arquivo.
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
            System.out.println("Arquivo n??o encontrado");
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
            System.out.println("Erro de entrada/sa??da " + excecao);
        }
    }
}
