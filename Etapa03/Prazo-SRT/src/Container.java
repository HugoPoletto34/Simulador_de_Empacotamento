public class Container {
    int capacidadeMaxima;
    Produto produtoAtual;
    int qtdProdutosInseridos;
    int qtdProdutosAtual;
    boolean necessarioTrocar;
    boolean containerEmUso;
    boolean emTroca = false;
    char[] idade;

    Container(Produto produto) {
        this.capacidadeMaxima = 1_000_000;
        this.produtoAtual = produto;
        this.qtdProdutosInseridos = (int) Math.floor(this.capacidadeMaxima / produto.volume);
        this.qtdProdutosAtual = this.qtdProdutosInseridos;
        this.necessarioTrocar = false;
        this.containerEmUso = false;
        this.idade = new char[]{'1', '0', '0', '0'};
    }

    public boolean vazia() {
        return this.qtdProdutosAtual == 0;
    }

    public boolean avaliarQtdProdutosContainer() {
        return this.qtdProdutosAtual <= 15 || necessarioTrocar;
    }

    public boolean possivelRetirarQuantidade(int qtdProdutos) {
        return qtdProdutosAtual >= qtdProdutos;
    }

    public int retirarProdutos(int qtdProdutos) {
        if (!vazia() && possivelRetirarQuantidade(qtdProdutos)) {
            computarUso();
            this.qtdProdutosAtual -= qtdProdutos;
            return qtdProdutos;
        }
        if (!vazia()) {
            this.necessarioTrocar = true;
            int qtdDisponivel = this.qtdProdutosAtual;
            this.qtdProdutosAtual = 0;
            return qtdDisponivel;
        }
        else
            this.necessarioTrocar = true;
            return 0;
    }

    public void computarUso() {
        this.idade[0] = '1';
    }

    public void shift() {
        if (!emTroca) {
            emTroca = true;
            for (int i = 3; i > 0; i--) {
                this.idade[i] = this.idade[i - 1];
            }
            this.idade[0] = '0';
            emTroca = false;
        }
    }

    public void reporProdutosContainer() {
        this.qtdProdutosAtual = this.qtdProdutosInseridos;
        this.necessarioTrocar = false;
    }
}
