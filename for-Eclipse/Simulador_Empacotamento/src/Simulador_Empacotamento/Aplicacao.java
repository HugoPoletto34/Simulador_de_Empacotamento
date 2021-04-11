package Simulador_Empacotamento;

import java.io.*;
import java.util.ArrayList;


public class Aplicacao {
    public static void main(String[] args) {
        ArrayList<Pedidos> listaPedidos = new ArrayList();
        ArrayList<Pedidos> listaMenorPrioridade = new ArrayList();
        criaPedidos(listaPedidos, listaMenorPrioridade);
    }

    public static void criaPedidos(ArrayList<Pedidos> listaPedidos,  ArrayList<Pedidos> listaMenorPrioridade) {
        ArquivoTextoLeitura arq = new ArquivoTextoLeitura();
        arq.abrirArquivo("DadosEmpacotadeira.txt");
        int tam = Integer.parseInt(arq.ler());
        String[] ent;

        for (int i = 0; i < tam; i++) {
            ent = arq.ler().split(";");
            Pedidos pd = new Pedidos(ent[0], Integer.parseInt(ent[1]), Integer.parseInt(ent[2]));
            if (pd.prazo != 0) {
                listaPedidos.add(pd);
                pd.organizaPedidos(listaPedidos);
            }
            else {
                listaMenorPrioridade.add(pd);
                pd.organizaPedidos(listaMenorPrioridade);
            }
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