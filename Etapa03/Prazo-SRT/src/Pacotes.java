public class Pacotes {
    int limite;
    Pedidos pedidoCliente;
    int volumeOcupado;
    int qtdeProdutosInseridos;

    public Pacotes (Pedidos pedidoCliente) {
        this.limite = 5000;
        this.volumeOcupado = 0;
        this.qtdeProdutosInseridos = 0;
        this.pedidoCliente = pedidoCliente;
    }

    public void inserirProduto(Produto produto) {
        if (cabeProduto(produto)) {
            this.qtdeProdutosInseridos++;
            this.volumeOcupado += produto.volume;
        }
    }

    public boolean cabeProduto(Produto produto) {
        return volumeOcupado + produto.volume > limite;
    }

    public int quantosProdutosCabeNoPacote(int volumeProduto) {
        return Math.round(this.limite / volumeProduto);
    }

}
