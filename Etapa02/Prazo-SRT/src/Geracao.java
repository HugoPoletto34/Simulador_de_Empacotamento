import java.text.ParseException;
import java.util.Date;

public class Geracao {


    public Geracao(){
        super();
    }

    public void swapObject(ListaDuplamenteEncadeada lista, int i, int j) {
        CelulaDuplamenteEncadeada celI = get(i, lista);
        CelulaDuplamenteEncadeada celJ = get(j, lista);
        Pedidos temp = celI.getItem();
        celI.setItem(celJ.getItem());
        celJ.setItem(temp);
    }

    public CelulaDuplamenteEncadeada get(int posicao, ListaDuplamenteEncadeada lista) {
        CelulaDuplamenteEncadeada result = lista.getPrimeiro().getProximo();
        for (int i = 1; i < posicao; i++) {
            result = result.getProximo();
        }
        return result;
    }



}