public class Relatorio {
    public int qtdePedidos;
    //public String tempoListaCOMPrioridade;
    //public String tempoListaSEMPrioridade;
    public String tempoTotalExecucao;
    public int qtdePedidosFinalizadosAntesDoPrazo;
    public int qtdePacotesFeitosMeioDia;
    public int qtdePedidosFinalizadosMeioDia;
    public int qtdePacotesFeitos;
    public int qtdePedidosFinalizados;
    public double tempoGastoPedido;
    public boolean jaPassouMeioDia = false;

    public Relatorio() {

    }

    public void setTempoTotalExecucao(int horaInicio, int minutoInicio, int horaTermino, int minutoTermino) {
        tempoTotalExecucao = "Inicio: " + (horaInicio) + "h" + (minutoInicio) + "m" + " -- Fim: " + horaTermino + "h" + minutoTermino + "m";
    }


    public void makeRelatorioPedido(Pedidos pedido, Caminhao caminhao, MyTimer timer) {
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

        tempoGastoPedido += ((timer.hora - 8) * 3600 + timer.minuto  * 60 + timer.segundo);

        if (timer.hora <= 17) {
            qtdePacotesFeitos = caminhao.pacotesCaminhao.size();
            qtdePedidosFinalizados++;
        }
    }

    public void criarRelatorio(String nomeArquivo) {
        ArquivoTextoEscrita arqEscrita = new ArquivoTextoEscrita();
        arqEscrita.abrirArquivo("relatório.md");
        arqEscrita.escrever("# Relatório - Ordenação pelo prazo + SRT");
        arqEscrita.escrever(" - Nome do Arquivo de pedidos: " + nomeArquivo);
        arqEscrita.escrever(" - Quantidade de pedidos: " + qtdePedidos);
        arqEscrita.escrever(" - Tempo médio gasto de pedidos em hora: " + Math.round((tempoGastoPedido / 3600) / qtdePedidos));
        arqEscrita.escrever(" - Quantidade de Pedidos finalizados antes do prazo estipulado: " + qtdePedidosFinalizadosAntesDoPrazo );
        arqEscrita.escrever("## Tempo de Execução:");
        arqEscrita.escrever(" - Expediente: 8h à 17h");
        //arqEscrita.escrever(" - Tempo lista COM prioridade: " + tempoListaCOMPrioridade);
        //arqEscrita.escrever(" - Tempo lista SEM prioridade: " + tempoListaSEMPrioridade);
        arqEscrita.escrever(" - Expediente total de empacotamento : " + tempoTotalExecucao);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos às 12h:");
        arqEscrita.escrever(" - Quantidade de pacotes feitos: " + qtdePacotesFeitosMeioDia);
        arqEscrita.escrever(" - Quantidade de pedidos feitos: " + qtdePedidosFinalizadosMeioDia);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos no caminhão no fim do expediente (17h):");
        arqEscrita.escrever(" - Quantidade de pacotes: " + qtdePacotesFeitos);
        arqEscrita.escrever(" - Quantidade de pedidos finalizados: " + qtdePedidosFinalizados);

        arqEscrita.fecharArquivo();
    }
}
