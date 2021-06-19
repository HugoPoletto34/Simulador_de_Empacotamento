import java.util.ArrayList;

public class ControleContainers {
    Container[] containersEmUso;
    ArrayList<Container> todosContainers;

    ControleContainers() {
        containersEmUso = new Container[4];
        todosContainers = new ArrayList<>();
    }

    public void encher(SyncList lists) {
        for (int i = 0; i < containersEmUso.length; i++) {
            containersEmUso[i] = getContainer(lists.listVip().get(i).produto);
            containersEmUso[i].containerEmUso = true;
        }
    }

    public Container getContainerEmUso(Produto p) {
        Container c = null;
        for (int i = 0; i < containersEmUso.length; i++) {
            if (containersEmUso[i].produtoAtual == p)
                c = containersEmUso[i];
        }
        return c;
    }

    public Container trocarContainer(int indice, ArrayList<Pedidos> listaVip) {
        Container resp = null;
        for (int i = 0; i < todosContainers.size(); i++) {
            resp = getContainer(listaVip.get(i).produto);
            if (!resp.containerEmUso) {
                containersEmUso[indice] = resp;
                i = todosContainers.size();
            }
        }

        return resp;
    }

    public void preencherListaProdutos() {
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        arq.abrirArquivo("../ListaProdutos.txt");
        int qtdIdsProdutos = Integer.parseInt(arq.ler());

        for (int i = 0; i < qtdIdsProdutos; i++) {
            String[] ent = arq.ler().split(";");
            Produto produto = new Produto(Integer.parseInt(ent[0]), Integer.parseInt(ent[1]));
            todosContainers.add(i, new Container(produto));
        }
        arq.fecharArquivo();
    }

    public Produto getProduto(int id) {
        for (Container todosContainer : todosContainers)
            if (todosContainer.produtoAtual.id == id)
                return todosContainer.produtoAtual;

        return null;
    }

    public Container getContainer(Produto produto) {
        for (Container todosContainer : todosContainers)
            if (todosContainer.produtoAtual.id == produto.id)
                return todosContainer;

        return null;
    }

    public int temContainer(int idProduto) {
        int indice = -1;

        for (int i = 0; i < containersEmUso.length; i++) {
            if (containersEmUso[i].produtoAtual.id == idProduto)
                indice = i;
        }

        return indice;
    }

    public Pedidos buscarPedido(String idThread, SyncList lists) {
        Pedidos pedidoParaExecucao;
        if (idThread.equals("Thread-0"))
            pedidoParaExecucao = pedidoPossivelParaExecucao(lists, lists.listVip());
        else
            pedidoParaExecucao = pedidoPossivelParaExecucao(lists, lists.listCommon());

        return pedidoParaExecucao;
    }

    private Pedidos pedidoPossivelParaExecucao(SyncList lists, ArrayList<Pedidos> lista) {
        int indice;
        Pedidos resp = null;
        int i = 0;
        do {
            indice = temContainer(lista.get(i).produto.id);
            if (indice != -1) {
                if (containersEmUso[indice].necessarioTrocar)
                    trocarContainer(indice, lists.listVip());
                else
                    resp = lista.remove(indice);
            }
            i++;
        } while (indice != -1 && i < lista.size());

        return resp;

    }

    public boolean vazio() {
        return containersEmUso.length == 0;
    }
}
