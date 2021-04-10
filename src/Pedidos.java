import java.util.ArrayList;

public class Pedidos {
    String nome;
    int qtdeProdutos;
    int prazo;
    double tempoTransicao = 0.5;

    public Pedidos(String nome, int qtdeProdutos, int prazo) {
        this.nome = nome;
        this.qtdeProdutos = qtdeProdutos;
        this.prazo = prazo;
    }

    public void organizaPedidos(int qtdePedidos, Pedidos[] listaPedidos, int posic) {
        if (qtdePedidos != 1) {
            ArrayList listaMenorPrioridade = new ArrayList();

            listaMenorPrioridade.clone();
            for (int i = 0; i < (qtdePedidos - 1); i++) {
                int menor = i;

                for (int j = (i + 1); j < qtdePedidos; j++){
                    if (listaPedidos[menor].prazo > listaPedidos[j].prazo && listaPedidos[j].prazo != 0){
                        menor = j;
                    }
                    if (listaPedidos[j].prazo == 0)
                        listaMenorPrioridade.add(listaPedidos[j]);
                }
                swap(listaPedidos, menor, i);
            }
        }
            
    }

    private void swap(Pedidos[] listaPedidos, int part1, int part2) {
        Pedidos temp = listaPedidos[part1];
        listaPedidos[part1] = listaPedidos[part2];
        listaPedidos[part2] = temp;

    }
}
