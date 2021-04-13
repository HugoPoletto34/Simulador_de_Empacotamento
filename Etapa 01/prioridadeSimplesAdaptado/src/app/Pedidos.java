package app;

import java.util.ArrayList;

public class Pedidos implements Comparable<Pedidos>{
	String nome;
	int qtdeProdutosPedido;
	int qtdeProdutosEmpacotados;
	int prazo;
	int qtdePacotesNecessario;
	double prioridade;
	ArrayList<Pacotes> pacotes = new ArrayList<Pacotes>();

	public Pedidos(String nome, int qtdeProdutos, int prazo) {
		this.nome = nome;
		this.qtdeProdutosPedido = qtdeProdutos;
		this.qtdeProdutosEmpacotados = 0;
		this.prazo = prazo;
		if (this.prazo == 0) {
			this.prioridade = ((float) (540 * 2) + this.qtdeProdutosPedido) / 1000;
		} else {
			this.prioridade = ((float) (prazo * 2) + this.qtdeProdutosPedido) / 1000;
		}
		this.qtdePacotesNecessario = (this.qtdeProdutosPedido / 20) + (this.qtdeProdutosPedido < 20 ? 1 : 0);
	}

	public void organizaPedidos(ArrayList<Pedidos> listaPedidos) {
		if (listaPedidos.size() >= 1) {
			for (int i = 0; i < (listaPedidos.size() - 1); i++) {
				int menor = i;

				for (int j = (i + 1); j < listaPedidos.size(); j++) {
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
		} else if (listaPedidos.get(p1).prazo == listaPedidos.get(p2).prazo) {
			if (listaPedidos.get(p1).qtdeProdutosPedido > listaPedidos.get(p2).qtdeProdutosPedido)
				return true;
			else
				return false;
		} else
			return false;
	}

	private void swap(ArrayList<Pedidos> listaPedidos, int part1, int part2) {
		Object temp = listaPedidos.get(part1);
		listaPedidos.set(part1, listaPedidos.get(part2));
		listaPedidos.set(part2, (Pedidos) temp);
	}
	
	@Override
	public int compareTo(Pedidos p) {
		return Double.compare(this.prioridade, p.prioridade);
	}
}
