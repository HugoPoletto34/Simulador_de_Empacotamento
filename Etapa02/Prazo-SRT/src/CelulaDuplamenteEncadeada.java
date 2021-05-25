public class CelulaDuplamenteEncadeada {
    private Pedidos item;
    private CelulaDuplamenteEncadeada anterior;
    private CelulaDuplamenteEncadeada proximo;

    CelulaDuplamenteEncadeada() {
        this.setItem(new Pedidos());
        this.setAnterior(null);
        this.setProximo(null);
    }

    CelulaDuplamenteEncadeada(Pedidos item) {

        this.setItem(item);
        this.setAnterior(null);
        this.setProximo(null);
    }

    public Pedidos getItem() {
        return item;
    }

    public void setItem(Pedidos item) {
        this.item = item;
    }

    public CelulaDuplamenteEncadeada getAnterior() {
        return anterior;
    }

    public void setAnterior(CelulaDuplamenteEncadeada anterior) {
        this.anterior = anterior;
    }

    public CelulaDuplamenteEncadeada getProximo() {
        return proximo;
    }

    public void setProximo(CelulaDuplamenteEncadeada proximo) {
        this.proximo = proximo;
    }
}
