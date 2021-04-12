package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
	private File file;

	public PedidoDAO(String filename) {
		file = new File(filename);
	}
	
	public List<Pedido> getAll() {
		List<Pedido> pedidos = new ArrayList<Pedido>();
		Pedido pedido = null;
		
        try(FileReader fileReader = new FileReader(file); BufferedReader bufferReader  = new BufferedReader(fileReader)){
        	boolean first = true;
	        while (bufferReader.ready()) {
	        	if (first) {
	        		bufferReader.readLine();
	        		first = false;
	        	}else {
		        	String texto = bufferReader.readLine();
		        	String[] textoSplit = texto.split(";");
		        	int qtd = Integer.parseInt(textoSplit[1]);
		        	int prazo = Integer.parseInt(textoSplit[2]);
		        	float prioridade;
		        	if (prazo == 0) {
		        		prioridade = ((float)(540*2)+qtd)/1000;
		        	}else {
		        		prioridade = ((float) (prazo*2)+qtd)/1000;
		        	}

		        	pedido = new Pedido(textoSplit[0], qtd, prazo, prioridade);
					pedidos.add(pedido);
	        	}
	        }
	        bufferReader.close();
		} catch (Exception e) {
			System.out.println("ERRO ao gravar produto no disco!");
			e.printStackTrace();
		}
	        
		return pedidos;
	}
}
