import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ControleContainers {
    Container[] containersEmUso;
    ArrayList<Container> todosContainers;
    int nContainersEmUso;
    int qtdOperacoes;

    ControleContainers() {
        containersEmUso = new Container[4];
        todosContainers = new ArrayList<>();
        nContainersEmUso = 0;
        qtdOperacoes = 0;
    }

    public void encher(SyncList lists) {
        int pos = 0;
        for (int i = 0; nContainersEmUso != 4; i++) {
            Container c = getContainer(lists.listVip().get(i).produto.volume);
            if (!containerProdutoEstaInserido(c)) {
                containersEmUso[pos] = c;
                containersEmUso[pos].containerEmUso = true;
                nContainersEmUso++;
                pos++;
            }
        }
    }

    public boolean containerProdutoEstaInserido(Container c) {
        boolean resp = false;
        if (!vazio()) {
            for (int i = 0; i < nContainersEmUso; i++) {
                if (containersEmUso[i].produtoAtual.volume == c.produtoAtual.volume) {
                    resp = true;
                    break;
                }
            }
        }
        return resp;
    }

    public Container getContainerEmUso(Produto p) {
        Container c = null;
        for (Container container : containersEmUso) {
            if (container.produtoAtual.volume == p.volume)
                c = container;
        }
        return c;
    }

    public Container proxContainerDisponivel() {
        for (Container todosContainer : todosContainers) {
            if (!todosContainer.containerEmUso)
                return todosContainer;
        }
        return null;
    }

    public void trocarContainerPorProximo(int indice, Relogio relogio, Semaphore containersLock, Relatorio r) {
        System.out.println("Trocando para proximo container: substituindo container " + indice);
        Container aux = proxContainerDisponivel();
        todosContainers.get(indice).reporProdutosContainer();
        trocarContainer(indice, aux, relogio, containersLock, r);
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

    public Container getContainer(int volume) {
        for (Container todosContainer : todosContainers)
            if (todosContainer.produtoAtual.volume == volume)
                return todosContainer;

        return null;
    }

    public int iContainerEmUso(int volProduto) {
        int indice = -1;

        for (int i = 0; i < containersEmUso.length; i++) {
            if (containersEmUso[i].produtoAtual.volume == volProduto)
                indice = i;
        }

        return indice;
    }

    public Pedidos buscarPedido(String idThread,SyncList lists, Relogio relogio, Semaphore containersLock, Relatorio r) {
        Pedidos pedidoParaExecucao;
        if (idThread.equals("Thread-0"))
            pedidoParaExecucao = pedidoPossivelParaExecucao(lists.listVip(), relogio, containersLock, r);
        else
            pedidoParaExecucao = pedidoPossivelParaExecucao(lists.listCommon(), relogio, containersLock, r);

        return pedidoParaExecucao;
    }

    private Pedidos pedidoPossivelParaExecucao(ArrayList<Pedidos> lista, Relogio relogio, Semaphore containersLock, Relatorio r) {
        int indice;
        Pedidos resp = null;
        int i = 0;
        if (lista.size() != 0) {
            do {
                Pedidos p = lista.get(i);
                indice = iContainerEmUso(p.produto.volume);
                if (indice != -1) {
                    if (containersEmUso[indice].avaliarQtdProdutosContainer()) {
                        trocarContainerPorProximo(indice, relogio, containersLock, r);
                        indice = -1;
                    }
                    else
                        resp = lista.remove(i);
                } else {
                    if (p.precisaSerExecutadoComUrgencia(relogio)) {
                        int indTroca = indiceContainerMenorIdade();
                        trocarContainer(indTroca, getContainer(p.produto.volume), relogio, containersLock, r);
                        indice = indTroca;
                        resp = lista.remove(i);
                    }
                }
                i++;
            } while (indice == -1 && i < lista.size());

            if (resp == null) {
                int indTroca = indiceContainerMenorIdade();
                trocarContainer(indTroca, getContainer(lista.get(0).produto.volume), relogio, containersLock, r);
                resp = lista.remove(0);
            }
        }
        else
            System.out.println("Lista de Pedidos Vazio!");


        return resp;

    }

    private int proxContainer(ArrayList<Pedidos> lista, int iComecar) {
        boolean resp;
        do {
            resp = containerProdutoEstaInserido(getContainerEmUso(lista.get(iComecar++).produto));
        } while (!resp);

        return lista.get(iComecar).produto.volume;
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
        String idadeX = new String(containersEmUso[x].idade);
        String idadeY = new String(containersEmUso[y].idade);
        if (Integer.parseInt(idadeX) > Integer.parseInt(idadeY))
            return true;
        else if (Integer.parseInt(idadeX) == Integer.parseInt(idadeY)){
            return (containersEmUso[x].qtdProdutosInseridos - containersEmUso[x].qtdProdutosAtual) < (containersEmUso[y].qtdProdutosInseridos - containersEmUso[y].qtdProdutosAtual);
        }
        else
            return false;
    }

    public void trocarContainer(int indice, Container container, Relogio relogio, Semaphore containersLock, Relatorio r) {
        try {
            containersLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Trocando container " + indice);
        double tempoTrocaContainers = 30;
        containersEmUso[indice].containerEmUso = false;
        restaurarTempoVida(indice);
        containersEmUso[indice] = container;
        restaurarTempoVida(indice);
        containersEmUso[indice].containerEmUso = true;
        relogio.relogio.incrementaSegundo(tempoTrocaContainers);
        r.qtdTrocasContainers++;
        containersLock.release();
    }

    private void restaurarTempoVida(int indice) {
        containersEmUso[indice].idade = new char[]{'1', '0', '0', '0'};
    }

    public boolean vazio() {
        return nContainersEmUso == 0;
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
