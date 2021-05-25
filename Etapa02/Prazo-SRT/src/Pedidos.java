import java.util.ArrayList;

public class Pedidos extends Geracao {
    String nome;
    int qtdeProdutosPedido;
    int qtdeProdutosEmpacotados;
    int prazo;
    int horaChegada;
    int qtdePacotesNecessario;
    int qtdPacotesEmbalados;
    boolean minutoFinalizado;
    ArrayList<Pacotes> pacotes = new ArrayList();

    public Pedidos() {
        super();
        this.nome = "";
        this.qtdeProdutosPedido = 0;
        this.qtdeProdutosEmpacotados = 0;
        this.prazo = -1;
        this.horaChegada = 0;
        this.minutoFinalizado = false;
        this.qtdePacotesNecessario = 0;

    }

    public Pedidos(String nome, int qtdeProdutos, int prazo, int horaChegada) {
        this.nome = nome;
        this.qtdeProdutosPedido = qtdeProdutos;
        this.qtdeProdutosEmpacotados = 0;
        this.prazo = prazo;
        this.horaChegada = horaChegada;
        this.minutoFinalizado = false;
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
        int i = 0;
        while (listaPedidos.get(0).prazo == 0 && i < listaPedidos.size()) {
            listaPedidos.add(listaPedidos.remove(0));
            i++;
        }
   }

   public void swap(ArrayList<Pedidos> listaPedidos, int i, int j) {
        Pedidos temp = listaPedidos.get(i);
        listaPedidos.set(i, listaPedidos.get(j));
        listaPedidos.set(j, temp);
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

    private boolean compMenor(ArrayList<Pedidos> listaPedidos, int p1, int p2) {
        if (listaPedidos.get(p1).prazo > listaPedidos.get(p2).prazo && listaPedidos.get(p1).prazo != -1 && listaPedidos.get(p2).prazo != -1) {
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

}
