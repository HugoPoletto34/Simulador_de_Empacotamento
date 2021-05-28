package src;

public class Relatorio {
    public int qtdePedidos;
    public String tempoTotalExecucao;
    public int qtdePedidosFinalizadosAntesDoPrazo;
    public int qtdePacotesFeitosMeioDia;
    public int qtdePedidosFinalizadosMeioDia;
    public int qtdePacotesFeitos;
    public int qtdePedidosFinalizados;
    public double tempoGastoPedido;
    public boolean jaPassouMeioDia = false;

    public void setTempoTotalExecucao(int hora, int minuto) {
        tempoTotalExecucao = hora + "h" + minuto + "m";
    }

    public void makeRelatorioPedido(Pedidos pedido, Caminhao caminhao, MyTimer timer, Relogio relogio) {
        qtdePedidos++;
        if (pedido.prazo != 0)
            qtdePedidosFinalizadosAntesDoPrazo += (pedido.minutoFinalizado ? 1 : 0);
        else
            qtdePedidosFinalizadosAntesDoPrazo++;

        if (!jaPassouMeioDia && relogio.relogio.hora >= 12) {
            qtdePacotesFeitosMeioDia = caminhao.pacotesCaminhao.size();
            jaPassouMeioDia = true;
        }
        else if (!jaPassouMeioDia)
            qtdePedidosFinalizadosMeioDia++;

        tempoGastoPedido += ((timer.hora) * 3600 + timer.minuto  * 60 + timer.segundo);

        if (relogio.relogio.hora <= 17) {
            qtdePacotesFeitos = caminhao.pacotesCaminhao.size();
            qtdePedidosFinalizados++;
        }
    }

    public void criarRelatorio(int id, String nomeArquivo) {
        ArquivoTextoEscrita arqEscrita = new ArquivoTextoEscrita();
        arqEscrita.abrirArquivo("relatorio_conjunto-0" + (id + 1) +".md");
        arqEscrita.escrever("# Relatório - Ordenação Prioridade Simples");
        arqEscrita.escrever(" - Nome do Arquivo de pedidos: " + nomeArquivo);
        arqEscrita.escrever(" - Total de pedidos no conjunto-0" + (id + 1) + ": " + qtdePedidos);
        arqEscrita.escrever(" - Tempo médio gasto de pedidos em hora: " + Math.round((tempoGastoPedido / 3600) / qtdePedidos));
        arqEscrita.escrever(" - Quantidade de Pedidos finalizados antes do prazo estipulado: " + qtdePedidosFinalizadosAntesDoPrazo );
        arqEscrita.escrever("## Tempo de Execução:");
        arqEscrita.escrever(" - Expediente: 8h à 17h");
        arqEscrita.escrever(" - Expediente total de empacotamento: " + tempoTotalExecucao);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos às 12h:");
        arqEscrita.escrever(" - Quantidade de pacotes feitos: " + qtdePacotesFeitosMeioDia);
        arqEscrita.escrever(" - Quantidade de pedidos finalizados: " + qtdePedidosFinalizadosMeioDia);
        arqEscrita.escrever("## Quantidade de Pacotes e pedidos no caminhão no fim do expediente (17h):");
        arqEscrita.escrever(" - Quantidade de pacotes: " + qtdePacotesFeitos);
        arqEscrita.escrever(" - Quantidade de pedidos finalizados: " + qtdePedidosFinalizados);

        arqEscrita.fecharArquivo();
    }
}
