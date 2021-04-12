package app;

public class Pedido implements Comparable<Pedido>{	
	private String cliente;
	private Integer qtd;
	private Integer prazo;
	private Float prioridade;

	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public Integer getQtd() {
		return qtd;
	}
	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}
	public Integer getPrazo() {
		return prazo;
	}
	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}
	public float getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(float prioridade) {
		this.prioridade = prioridade;
	}
	
	public Pedido(String cliente, int qtd, int prazo, float prioridade) {
		setCliente(cliente);
		setQtd(qtd);
		setPrazo(prazo);
		setPrioridade(prioridade);
	}

	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Cliente: " + cliente + "   Quant.:" + qtd + "   Prazo: " + prazo+" Prioridade: "+ prioridade;
	}

	@Override
	public int compareTo(Pedido p) {
		return Float.compare(getPrioridade(), p.getPrioridade());
	}
}
