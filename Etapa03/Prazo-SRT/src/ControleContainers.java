import java.util.ArrayList;

public class ControleContainers {
    Container[] containersEmUso;
    ArrayList<Container> todosContainers;
    int qtdOperacoes;

    ControleContainers() {
        containersEmUso = new Container[4];
        todosContainers = new ArrayList<>();
        qtdOperacoes = 0;
    }

    public void encher(SyncList lists) {
        for (int i = 0; i < containersEmUso.length; i++) {
            containersEmUso[i] = getContainer(lists.listVip().get(i).produto);
            containersEmUso[i].containerEmUso = true;
        }
    }

    public Container getContainerEmUso(Produto p) {
        Container c = null;
        for (Container container : containersEmUso) {
            if (container.produtoAtual == p)
                c = container;
        }
        return c;
    }

    public void trocarContainerPorProximo(int indice, ArrayList<Pedidos> listaVip, MyTimer timer) {
        Container aux;
        for (int i = 0; i < todosContainers.size(); i++) {
            aux = getContainer(listaVip.get(i).produto);
            if (!aux.containerEmUso) {
                trocarContainer(indice, aux, timer);
                i = todosContainers.size();
            }
        }

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

    public int iContainerEmUso(int idProduto) {
        int indice = -1;

        for (int i = 0; i < containersEmUso.length; i++) {
            if (containersEmUso[i].produtoAtual.id == idProduto)
                indice = i;
        }

        return indice;
    }

    public Pedidos buscarPedido(String idThread, SyncList lists, MyTimer timer) {
        Pedidos pedidoParaExecucao;
        if (idThread.equals("Thread-0"))
            pedidoParaExecucao = pedidoPossivelParaExecucao(lists, lists.listVip(), timer);
        else
            pedidoParaExecucao = pedidoPossivelParaExecucao(lists, lists.listCommon(), timer);

        return pedidoParaExecucao;
    }

    private Pedidos pedidoPossivelParaExecucao(SyncList lists, ArrayList<Pedidos> lista, MyTimer timer) {
        int indice;
        Pedidos resp = null;
        int i = 0;
        do {
            Pedidos p = lista.get(i);
            indice = iContainerEmUso(p.produto.id);
            if (indice != -1) {
                if (containersEmUso[indice].necessarioTrocar)
                    trocarContainerPorProximo(indice, lists.listVip(), timer);
                resp = lista.remove(indice);
            }
            else {
                if (p.precisaSerExecutadoComUrgencia(timer)) {
                    int indTroca = indiceContainerMenorIdade();
                    trocarContainer(indTroca, getContainer(p.produto), timer);
                }
            }
            i++;
        } while (indice != -1 && i < lista.size());

        return resp;

    }

    private int indiceContainerMenorIdade() {
        int menor = 0;
        for (int i = 1; i < containersEmUso.length; i++) {
            if (compMenorIdadeOuQtdProdutosAtual(i, menor))
                menor = i;
        }
        return menor;
    }

    private boolean compMenorIdadeOuQtdProdutosAtual(int x, int y) {
        if (Integer.parseInt(containersEmUso[x].idade) < Integer.parseInt(containersEmUso[y].idade))
            return true;
        else if (Integer.parseInt(containersEmUso[x].idade) == Integer.parseInt(containersEmUso[y].idade)){
            return containersEmUso[x].qtdProdutosAtual < containersEmUso[y].qtdProdutosAtual;
        }
        else
            return false;
    }

    private void trocarContainer(int indice, Container container, MyTimer timer) {
        double tempoTrocaContainers = 30;
        containersEmUso[indice].containerEmUso = false;
        restaurarTempoVida(indice);
        containersEmUso[indice] = container;
        restaurarTempoVida(indice);
        containersEmUso[indice].containerEmUso = true;
        timer.incrementaSegundo(tempoTrocaContainers);
    }

    private void restaurarTempoVida(int indice) {
        containersEmUso[indice].idade = "1000";
    }



    public boolean vazio() {
        return containersEmUso.length == 0;
    }

    public void regitrarUso() {
        this.qtdOperacoes++;
        if (qtdOperacoes % 4 + 1 == 4) {
            realizarShiftContainersEmUso();
        }
    }

    private void realizarShiftContainersEmUso() {
        for (Container container : containersEmUso) {
            container.shift();
        }
    }
}
