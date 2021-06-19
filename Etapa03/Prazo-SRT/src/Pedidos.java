import java.util.ArrayList;

public class Pedidos {
    String nome;
    int qtdeProdutosPedido;
    int qtdeProdutosEmpacotados;
    int prazo;
    int minutoEntrado;
    int qtdePacotesNecessario;
    int qtdPacotesEmbalados;
    double tempoMinutoParaFinalizar;
    boolean minutoFinalizado;
    Produto produto;
    ArrayList<Pacotes> pacotes = new ArrayList();

    public Pedidos(String nome, int qtdeProdutos, int prazo, int minutoEntrado, Produto produto) {
        this.nome = nome;
        this.qtdeProdutosPedido = qtdeProdutos;
        this.qtdeProdutosEmpacotados = 0;
        this.prazo = prazo;
        this.minutoEntrado = minutoEntrado;
        this.minutoFinalizado = false;
        this.produto = produto;
        this.qtdePacotesNecessario = (this.qtdeProdutosPedido / produto.volume) + (this.qtdeProdutosPedido < produto.volume ? 1 : 0);
        this.tempoMinutoParaFinalizar = (BracoRobotico.getTempoProducao() + Esteira.getTempoMinutoTransicao() * this.qtdePacotesNecessario);
    }

    public boolean pedidoCompleto() {
        return this.qtdeProdutosEmpacotados == this.qtdeProdutosPedido;
    }

    public boolean temProdutosParaEmpacotar() {
        boolean result = this.qtdeProdutosEmpacotados != this.qtdeProdutosPedido;
        return result;
    }

    public void empacotandoProduto() {
        this.qtdeProdutosEmpacotados++;
    }
}
