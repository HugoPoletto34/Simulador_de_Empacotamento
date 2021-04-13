package app;

public class Esteira {
    int qtdeProdutosAtual;

    public Esteira() {
        this.qtdeProdutosAtual = 0;
    }

    public boolean retirarProdutoEsteira() {
        if (temProdutos()) {
            this.qtdeProdutosAtual--;
            return true;
        }
        else
            return false;
    }

    public boolean temProdutos() {
        return this.qtdeProdutosAtual > 0;
    }

    public void rodaProdutos(int qtdeProdutos) {
        this.qtdeProdutosAtual = qtdeProdutos;
    }
}
