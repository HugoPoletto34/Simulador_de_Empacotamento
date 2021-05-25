import java.util.ArrayList;

public class ListaDuplamenteEncadeada extends Geracao{
    private CelulaDuplamenteEncadeada primeiro;
    private CelulaDuplamenteEncadeada ultimo;
    private int n;

    public CelulaDuplamenteEncadeada getPrimeiro() {
        return primeiro;
    }

    public void setPrimeiro(CelulaDuplamenteEncadeada primeiro) {
        this.primeiro = primeiro;
    }

    public CelulaDuplamenteEncadeada getUltimo() {
        return ultimo;
    }

    public void setUltimo(CelulaDuplamenteEncadeada ultimo) {
        this.ultimo = ultimo;
    }

    public ListaDuplamenteEncadeada() {
        CelulaDuplamenteEncadeada sentinela = new CelulaDuplamenteEncadeada();

        primeiro = sentinela;
        ultimo = sentinela;
        n = 0;
    }

    public ListaDuplamenteEncadeada(Pedidos pedido) {
        CelulaDuplamenteEncadeada sentinela;

        sentinela = new CelulaDuplamenteEncadeada(pedido);
        primeiro = sentinela;
        ultimo = sentinela;
        n = 0;
    }


    public boolean vazia() {

        boolean resp;

        if (primeiro == ultimo)
            resp = true;
        else
            resp = false;
        return resp;
    }

    public int size() {
        return n;
    }

    public void inserirInicio(Pedidos item) {
        CelulaDuplamenteEncadeada nova = new CelulaDuplamenteEncadeada(item);
        if (vazia()) {
            primeiro.setProximo(nova);
            ultimo = nova;
        } else {
            nova.setProximo(primeiro.getProximo());
            primeiro.getProximo().setAnterior(nova);
        }

        primeiro.setProximo(nova);
        n++;
    }

    public void inserirPosicao(Pedidos item, int posicao) throws Exception{

        CelulaDuplamenteEncadeada celPosicao;
        CelulaDuplamenteEncadeada nova;

        if ((posicao >= 0) && (posicao <= n)) {
            celPosicao = primeiro;
            for (int i = 0; i < posicao; i++) {
                celPosicao = celPosicao.getProximo();
            }

            nova = new CelulaDuplamenteEncadeada(item);

            nova.setAnterior(celPosicao.getAnterior());
            nova.setProximo(celPosicao.getProximo());

            if (posicao == n)
                ultimo = nova;

            n++;

        } else
            throw new Exception ("Não foi possível inserir o item na lista: posição inválida!");
    }

    public void inserirFim(Pedidos item) {
        CelulaDuplamenteEncadeada nova = new CelulaDuplamenteEncadeada(item);
        ultimo.setProximo(nova);
        nova.setAnterior(ultimo);
        ultimo = nova;
        n++;
    }

    public Pedidos removerInicio() {
        CelulaDuplamenteEncadeada removido = primeiro.getProximo();
        primeiro.setProximo(removido.getProximo());
        n--;
        return removido.getItem();
    }
/*
    public Pedidos removerPosicao(int posicao) throws Exception {
        CelulaDuplamenteEncadeada retirado;

        if (! vazia() ) {
            if ((posicao >= 0) && (posicao < n)) {
                retirado = getPosicao(posicao);

                CelulaDuplamenteEncadeada anterior = retirado.getAnterior();
                CelulaDuplamenteEncadeada posterior = retirado.getProximo();

                anterior.setProximo(retirado.getProximo());
                posterior.setAnterior(retirado.getAnterior());
                n--;

                if (posicao == n)
                    ultimo = anterior;
            } else
                throw new Exception ("Não foi possível retirar o item da lista: posição inválida!");
        } else
            throw new Exception ("Não foi possível retirar o item da lista: a lista está vazia!");

        return retirado.getItem();
    }
*/
    public Pedidos removerFim() throws Exception {
        CelulaDuplamenteEncadeada removido = ultimo;
        CelulaDuplamenteEncadeada penultimo = primeiro.getProximo();
        for ( ; penultimo.getProximo() != ultimo; penultimo = penultimo.getProximo()) {}
        ultimo = penultimo;
        ultimo.setProximo(null);
        n--;
        return removido.getItem();
    }

    public Pedidos remove(Pedidos pedido) {
        if (!vazia()) {
            CelulaDuplamenteEncadeada aux = primeiro.getProximo();
            int i = 0;
            for ( ; i < n && (pedido.prazo != aux.getItem().prazo || pedido.qtdeProdutosPedido != aux.getItem().qtdeProdutosPedido); i++){
                aux = aux.getProximo();
            }
            if (aux != null) {
                CelulaDuplamenteEncadeada anterior = aux.getAnterior();
                CelulaDuplamenteEncadeada posterior = aux.getProximo();

                try {
                    anterior.setProximo(aux.getProximo());
                }
                catch (Exception e){

                }

                try {
                    posterior.setAnterior(aux.getAnterior());
                }
                catch (Exception e){

                }


                n--;
                return aux.getItem();
            }
            return null;
        }
        return null;
    }


    public void addArrays(ArrayList<Pedidos> lista01, ArrayList<Pedidos> lista02) throws Exception {
        for (int i = 0; i < lista01.size(); i++) {
            inserirFim(lista01.get(i));
        }
        for (int j = 0; j < lista02.size(); j++) {
            inserirFim(lista02.get(j));
        }
    }



}
