public class BracoRobotico {

    public static int getTempoProducao() {
        return 5;
    }

    public void inserirProdutos(Pedidos pedido, Pacotes pacote, Esteira esteira, MyTimer timer, Relogio relogio) {
        while (esteira.temProdutos() && !pacote.cabeProduto(pedido.produto) && pedido.temProdutosParaEmpacotar()) {
            esteira.retirarProdutoEsteira();
            pacote.inserirProduto(pedido.produto);
            pedido.empacotandoProduto();
        }
        pedido.qtdPacotesEmbalados++;
        timer.incrementaSegundo(getTempoProducao());
        relogio.atualizarRelogio();
    }

    public void colocarPacoteNaEsteira(Pacotes pacote, Caminhao caminhao) {
        caminhao.AdicionarNoCaminhao(pacote);
    }
}
