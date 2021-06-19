public class BracoRobotico {

    public static int getTempoProducao() {
        return 5;
    }

    public void inserirProdutos(Pedidos pedido, Pacotes pacote, Esteira esteira, MyTimer timer, Relogio relogio) throws IllegalAccessException {
        while (esteira.temProdutos() && !pacote.cabeProduto(pedido.produto) && pedido.temProdutosParaEmpacotar()) {
            esteira.retirarProdutoEsteira();
            pacote.inserirProduto(pedido.produto);
            pedido.empacotandoProduto();
        }
        pedido.qtdPacotesEmbalados++;
        timer.incrementaSegundo(getTempoProducao());
        relogio.atualizarRelogio();
    }

    public void colocarPacoteNaEsteira(Pacotes pacote, Caminhao caminhao, MyTimer timer) {
        caminhao.AdicionarNoCaminhao(pacote);
    }
}
