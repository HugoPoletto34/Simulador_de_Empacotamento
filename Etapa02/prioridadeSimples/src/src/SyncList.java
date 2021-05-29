package src;

import java.util.ArrayList;
import java.util.Collections;

public class SyncList {

    private ArrayList<Pedidos> listVip;
    private ArrayList<Pedidos> listCommon;

    public SyncList(){
        this.listVip = new ArrayList<Pedidos>();
        this.listCommon = new ArrayList<Pedidos>();
    }

    public ArrayList<Pedidos> listVip() {
        return listVip;
    }

    public ArrayList<Pedidos> listCommon() {
        return listCommon;
    }
    
    public void imprimeListas() {

    	for (int i = 0; i < listVip.size(); i++) {
        	System.out.println(listVip.get(i).prioridade);
        }
    	
    	for (int i = 0; i < listCommon.size(); i++) {
        	System.out.println(listCommon.get(i).prioridade);
        }

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
    
    public synchronized void organizaPedidosPrioridade() {
    	Collections.sort(listVip);
    	Collections.sort(listCommon);
    	splitPedidos();
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

}
