public class BracoRobotico {
    int tempoProducao;

    public BracoRobotico() {
        this.tempoProducao = 5;
    }

    public void inserirProdutos(Pedidos pedido, Pacotes pacote, Esteira esteira, MyTimer timer, Relogio relogio) throws IllegalAccessException {
        while (esteira.temProdutos() && !pacote.estaCheio() && pedido.temProdutosParaEmpacotar()) {
            esteira.retirarProdutoEsteira();
            pacote.inserirProduto();
            pedido.empacotandoProduto();
        }
        pedido.qtdPacotesEmbalados++;
        timer.incrementaSegundo(tempoProducao);
        relogio.atualizarRelogio();
    }

    public void colocarPacoteNaEsteira(Pacotes pacote, Caminhao caminhao, MyTimer timer) {
        caminhao.AdicionarNoCaminhao(pacote);
    }
}
