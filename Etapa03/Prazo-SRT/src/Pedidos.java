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
    ArrayList pacotes;

    public Pedidos(String nome, int qtdeProdutos, int prazo, int minutoEntrado, Produto produto) {
        this.nome = nome;
        this.qtdeProdutosPedido = qtdeProdutos;
        this.qtdeProdutosEmpacotados = 0;
        this.prazo = prazo;
        this.minutoEntrado = minutoEntrado;
        this.minutoFinalizado = false;
        this.produto = produto;
        this.pacotes = new ArrayList<>();
        this.qtdePacotesNecessario = (this.qtdeProdutosPedido / produto.volume) + (this.qtdeProdutosPedido < produto.volume ? 1 : 0);
        this.tempoMinutoParaFinalizar = (BracoRobotico.getTempoProducao() + Esteira.getTempoMinutoTransicao() * this.qtdePacotesNecessario);
    }

    public Pedidos() {

    }

    public boolean pedidoCompleto() {
        return this.qtdeProdutosEmpacotados == this.qtdeProdutosPedido;
    }

    public int minutoMaximoParaFim() {
        return this.minutoEntrado + this.prazo;
    }

    public boolean precisaSerExecutadoComUrgencia(Relogio relogio) {
        double minutoAtual = relogio.getTempoMinutosSemHorasIniciais();
        double minutoMaximoParaFim = minutoMaximoParaFim();
        return minutoMaximoParaFim - (minutoAtual + tempoMinutoParaFinalizar) <= 1 && minutoMaximoParaFim > minutoAtual;
    }

    public boolean temProdutosParaEmpacotar() {
        return this.qtdeProdutosEmpacotados != this.qtdeProdutosPedido;
    }

    public void empacotandoProduto() {
        this.qtdeProdutosEmpacotados++;
    }
}
