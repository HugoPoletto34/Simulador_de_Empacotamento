package app;

import java.util.Collections;
import java.util.List;

public class Application {
	public static void main(String[] args) {
		
		List<Pedido> pedidos = new PedidoDAO("DadosEmpacotadeira.txt").getAll();
    			
		Collections.sort(pedidos);
		
		for (Pedido pedido: pedidos) {
			System.out.println(pedido);
		}
	}
}
