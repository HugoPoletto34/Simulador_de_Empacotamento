public class Pacotes {
    int limite;
    Pedidos pedidoCliente;
    int qtdeProdutosInseridos;

    public Pacotes (Pedidos pedidoCliente) {
        this.limite = 5000;
        this.qtdeProdutosInseridos = 0;
        this.pedidoCliente = pedidoCliente;
    }

    public void inserirProduto() {
        this.qtdeProdutosInseridos++;
    }


    public boolean estaCheio() {
        return this.qtdeProdutosInseridos == 20;
    }


}
