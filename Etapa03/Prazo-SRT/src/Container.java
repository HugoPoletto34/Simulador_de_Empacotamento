public class Container {
    int capacidadeMaxima;
    Produto produtoAtual;
    int qtdProdutosInseridos;
    int qtdProdutosAtual;
    boolean tentativaRetirar;
    boolean necessarioTrocar;
    boolean containerEmUso;
    String idade;

    Container(Produto produto) {
        this.capacidadeMaxima = 1_000_000;
        this.produtoAtual = produto;
        this.qtdProdutosInseridos = (int) Math.floor(this.capacidadeMaxima / produto.volume);
        this.qtdProdutosAtual = this.qtdProdutosInseridos;
        this.tentativaRetirar = false;
        this.containerEmUso = false;
        this.tentativaRetirar = false;
        this.idade = "1000";
    }

    public boolean vazia() {
        return this.qtdProdutosAtual == 0;
    }

    public boolean possivelRetirarQuantidade(int qtdProdutos) {
        if (qtdProdutosAtual < qtdProdutos && !tentativaRetirar)
            this.tentativaRetirar = true;
        else if (tentativaRetirar || vazia())
            this.necessarioTrocar = true;

        return qtdProdutosAtual >= qtdProdutos;
    }

    public int retirarProdutos(int qtdProdutos) {
        if (!vazia() && possivelRetirarQuantidade(qtdProdutos)) {
            computarUso();
            this.qtdProdutosAtual -= qtdProdutos;
        }
        return qtdProdutos;
    }

    private void computarUso() {
        this.idade.toCharArray()[0] = '1';
    }

    public void shift() {
        this.idade = '0' + this.idade.substring(0, 2);
    }
}
