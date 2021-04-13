package app;

import java.util.ArrayList;
import java.util.Collections;

public class Aplicacao2 {
    static MyTimer timer = new MyTimer(8, 0);
    
    static String nomeArquivo = "DadosEmpacotadeira.txt";
    static int qtdePedidos;
    static String tempoLista;
    static String tempoTotalExecucao;
    static int qtdePacotesFeitosMeioDia;
    static int qtdePedidosFinalizadosMeioDia;
    static int qtdePacotesFeitos;
    static int qtdePedidosFinalizados;
    static double tempoGastoPedido;

    public static void main(String[] args) {
        ArrayList<Pedidos> listaPedidos = new ArrayList<Pedidos>();
        BracoRobotico bracoRobotico = new BracoRobotico();
        Caminhao caminhao = new Caminhao();
        Esteira esteira = new Esteira();
        int hora;
        int minuto;
        long initTime = System.currentTimeMillis();

        criaPedidos(listaPedidos);
        timer.incrementaSegundo((System.currentTimeMillis() - initTime) / 1000);
        hora = timer.hora;
        minuto = timer.minuto;
        
        startEmpacotamento(listaPedidos, bracoRobotico, caminhao, esteira);
    
        tempoLista = (timer.hora - hora) + "h" + (timer.minuto - minuto)  + "m";

        tempoTotalExecucao = timer.hora + "h" + timer.minuto  + "m";

        fazerRelatorio();
        
        for (int i = 0; i < listaPedidos.size(); i++) {
        	System.out.println(listaPedidos.get(i).prioridade);
        }
    
    }
    
    public static void fazerRelatorio() {
        ArquivoTextoEscrita arqEscrita = new ArquivoTextoEscrita();
        arqEscrita.abrirArquivo("relat�rio2.md");
        arqEscrita.escrever("# Relatório");
        arqEscrita.escrever(" - Nome do Arquivo de pedidos: " + nomeArquivo);
        arqEscrita.escrever(" - Quantidade de pedidos: " + qtdePedidos);
        arqEscrita.escrever(" - Tempo médio gasto de pedidos: " + tempoGastoPedido / qtdePedidos);
        arqEscrita.escrever("## Tempo de Execução:");
        arqEscrita.escrever(" - Tempo lista: " + tempoLista);
        arqEscrita.escrever(" - Tempo total: " + tempoTotalExecucao);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos às 12h:");
        arqEscrita.escrever(" - Quantidade de pacotes feitos: " + qtdePacotesFeitosMeioDia);
        arqEscrita.escrever(" - Quantidade de pedidos feitos: " + qtdePedidosFinalizadosMeioDia);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos no caminhão no fim do expediente (17h):");
        arqEscrita.escrever(" - Quantidade de pacotes: " + qtdePacotesFeitos);
        arqEscrita.escrever(" - Quantidade de pedidos finalizados: " + qtdePedidosFinalizados);
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
                qtdePacotesFeitosMeioDia = caminhao.pacotesCaminhao.size();
                jaPassouMeioDia = true;
            }
            else if (!jaPassouMeioDia)
                qtdePedidosFinalizadosMeioDia++;
            tempoGastoPedido += (timer.hora * 3600 + timer.minuto  * 60 + timer.segundo) / 60;
            
            if (timer.hora <= 17) {
                qtdePacotesFeitos = caminhao.pacotesCaminhao.size();
                qtdePedidosFinalizados++;
            }
        }
    }

    private static void transicaoPacoteEsteira(BracoRobotico bracoRobotico, Pacotes pacote, Esteira esteira, Caminhao caminhao) {
        double tempoTransicacao = 0.5;
        bracoRobotico.colocarPacoteNaEsteira(pacote, caminhao, timer);
        esteira.rodaProdutos(20);
        timer.incrementaSegundo(tempoTransicacao);
    }

    public static void criaPedidos(ArrayList<Pedidos> listaPedidos) {
    	ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        arq.abrirArquivo(nomeArquivo);
        qtdePedidos = Integer.parseInt(arq.ler());
        String[] ent;

        for (int i = 0; i < qtdePedidos; i++) {
            ent = arq.ler().split(";");
            Pedidos pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]));
            listaPedidos.add(pd);
        }
        organizaPedidosPrioridade(listaPedidos);
        arq.fecharArquivo();
    }
    
    public static void organizaPedidosPrioridade(ArrayList<Pedidos> listaPedidos) {
    	Collections.sort(listaPedidos);
	}
}
