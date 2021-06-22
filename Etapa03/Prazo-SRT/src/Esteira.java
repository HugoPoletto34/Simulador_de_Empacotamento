public class Esteira {
    int qtdeProdutosAtual;

    public static double getTempoMinutoTransicao() {
        return 0.5;
    }

    public Esteira() {
        this.qtdeProdutosAtual = 0;
    }

    public void retirarProdutoEsteira() {
        if (temProdutos()) {
            this.qtdeProdutosAtual--;
        }
    }

    public boolean temProdutos() {
        return this.qtdeProdutosAtual > 0;
    }

    public void rodaProdutos(int qtdeProdutos, ControleContainers controleContainers) {
        this.qtdeProdutosAtual = qtdeProdutos;
        controleContainers.regitrarUso();
    }
}
