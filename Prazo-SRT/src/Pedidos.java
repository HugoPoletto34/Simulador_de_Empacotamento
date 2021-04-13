import java.util.ArrayList;

public class Pedidos {
    String nome;
    int qtdeProdutosPedido;
    int qtdeProdutosEmpacotados;
    int prazo;
    int qtdePacotesNecessario;
    double minutoFinalizado;
    ArrayList<Pacotes> pacotes = new ArrayList();


    public Pedidos(String nome, int qtdeProdutos, int prazo) {
        this.nome = nome;
        this.qtdeProdutosPedido = qtdeProdutos;
        this.qtdeProdutosEmpacotados = 0;
        this.prazo = prazo;
        this.minutoFinalizado = 0;
        this.qtdePacotesNecessario = (this.qtdeProdutosPedido / 20) + (this.qtdeProdutosPedido < 20 ? 1 : 0);
    }

    public void organizaPedidos(ArrayList<Pedidos> listaPedidos) {
        if (listaPedidos.size() >= 1) {
            for (int i = 0; i < (listaPedidos.size() - 1); i++) {
                int menor = i;

                for (int j = (i + 1); j < listaPedidos.size(); j++){
                    if (compMenor(listaPedidos, menor, j))
                        menor = j;

                }
                swap(listaPedidos, menor, i);
            }
        }
            
   }

    public boolean pedidoCompleto() {
        return this.qtdePacotesNecessario == 0;
    }

    public boolean temProdutosParaEmpacotar() {
        boolean result = this.qtdeProdutosEmpacotados != this.qtdeProdutosPedido;
        return result;
    }

    public void empacotandoProduto() {
        this.qtdeProdutosEmpacotados++;
    }

    private boolean compMenor(ArrayList<Pedidos> listaPedidos, int p1, int p2) {
        if (listaPedidos.get(p1).prazo > listaPedidos.get(p2).prazo) {
            return true;
        }
        else if (listaPedidos.get(p1).prazo == listaPedidos.get(p2).prazo) {
            if (listaPedidos.get(p1).qtdeProdutosPedido > listaPedidos.get(p2).qtdeProdutosPedido)
                return true;
            else
                return false;
        }
        else
            return false;
   }

    private void swap(ArrayList listaPedidos, int part1, int part2) {
        Object temp = listaPedidos.get(part1);
        listaPedidos.set(part1, listaPedidos.get(part2));
        listaPedidos.set(part2, temp);
    }
}
