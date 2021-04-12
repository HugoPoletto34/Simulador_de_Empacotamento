import java.util.ArrayList;

public class Pedidos {
    String nome;
    int qtdeProdutos;
    int prazo;

    public Pedidos(String nome, int qtdeProdutos, int prazo) {
        this.nome = nome;
        this.qtdeProdutos = qtdeProdutos;
        this.prazo = prazo;
    }

    public void organizaPedidos(ArrayList<Pedidos> listaPedidos) {
        if (listaPedidos.size() >= 1) {
            for (int i = 0; i < (listaPedidos.size() - 1); i++) {
                int menor = i;
                System.out.println(listaPedidos.get(menor).prazo);

                for (int j = (i + 1); j < listaPedidos.size(); j++){
                    if (compMenor(listaPedidos, menor, j))
                        menor = j;

                }
                swap(listaPedidos, menor, i);
            }
        }
            
   }

   private boolean compMenor(ArrayList<Pedidos> listaPedidos, int p1, int p2) {
        if (listaPedidos.get(p1).prazo > listaPedidos.get(p2).prazo) {
            return true;
        }
        else if (listaPedidos.get(p1).prazo == listaPedidos.get(p2).prazo) {
            if (listaPedidos.get(p1).qtdeProdutos > listaPedidos.get(p2).qtdeProdutos)
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
