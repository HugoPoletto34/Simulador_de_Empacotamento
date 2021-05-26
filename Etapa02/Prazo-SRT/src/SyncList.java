import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SyncList {

    private ArrayList<Pedidos> listVip;
    private ArrayList<Pedidos> listCommon;

    public SyncList(){
        this.listVip = new ArrayList();
        this.listCommon = new ArrayList();
    }

    public synchronized void addToListVip(Pedidos i){
        this.listVip.add(i);
    }

    public synchronized void addToListCommon(Pedidos i){
        this.listCommon.add(i);
    }

    public synchronized boolean removeListVip(Pedidos pos){
        return this.listVip.remove(pos);
    }

    public synchronized Pedidos removeListVip(int pos){
        return this.listVip.remove(pos);
    }

    public synchronized boolean removeListCommon(Pedidos pos){
        return this.listCommon.remove(pos);
    }

    public synchronized Pedidos removeListCommon(int pos){
        return this.listCommon.remove(pos);
    }


    public synchronized int getSizeListVip(){
        return this.listVip.size();
    }

    public synchronized int getSizeListCommon(){
        return this.listCommon.size();
    }

    public synchronized void organizaPedidos() {
        ordenacaoSelecao(listVip);
        ordenacaoSelecao(listCommon);
        splitPedidos();
    }

    private void ordenacaoSelecao(ArrayList<Pedidos> lista) {
        if (lista.size() >= 1) {
            for (int i = 0; i < (lista.size() - 1); i++) {
                int menor = i;

                for (int j = (i + 1); j < lista.size(); j++){
                    if (compMenor(lista, menor, j))
                        menor = j;

                }
                swap(lista, menor, i);
            }
        }
    }

    private synchronized void splitPedidos() {
        int i = 0;
        while (listCommon.get(0).prazo == 0 && i < listCommon.size()) {
            listCommon.add(listCommon.remove(0));
            i++;
        }
        i = 0;
        while (listCommon.get(0).prazo == 20 && i < listCommon.size()) {
            listVip.add(listCommon.remove(0));
            i++;
        }
    }

    public synchronized void swap(ArrayList<Pedidos> lista, int i, int j) {
        Pedidos temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }

    private synchronized boolean compMenor(ArrayList<Pedidos> listaPedidos, int p1, int p2) {
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
