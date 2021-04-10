import java.io.*;


public class Aplicacao {
    public static void main(String[] args) {
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        MyTimer timer = new MyTimer();
        arq.abrirArquivo("DadosEmpacotadeira.txt");
        int tam = Integer.parseInt(arq.ler());
        String[] ent;
        Pedidos[] listaPedidos = new Pedidos[tam];
        int qtdePedidos = 0;
        //timer.initTimer();

        for (int i = 0; i < tam; i++) {
            ent = arq.ler().split(";");
            listaPedidos[i] = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]));
            qtdePedidos++;
            listaPedidos[i].organizaPedidos(qtdePedidos, listaPedidos, i);
        }


        arq.fecharArquivo();
    }
}

class ArquivoTextoLeitura {

    private BufferedReader entrada;

    public void abrirArquivo(String nomeArquivo){

        try {
            entrada = new BufferedReader(new FileReader(nomeArquivo));
        }
        catch (FileNotFoundException excecao) {
            System.out.println("Arquivo não encontrado");
        }
    }

    public void fecharArquivo() {

        try {
            entrada.close();
        }
        catch (IOException excecao) {
            System.out.println("Erro no fechamento do arquivo de leitura: " + excecao);
        }
    }

    public String ler() {

        String textoEntrada;

        try {
            textoEntrada = entrada.readLine();
        }
        catch (EOFException excecao) { //Exceção de final de arquivo.
            return null;
        }
        catch (IOException excecao) {
            System.out.println("Erro de leitura: " + excecao);
            return null;
        }
        return textoEntrada;
    }
}