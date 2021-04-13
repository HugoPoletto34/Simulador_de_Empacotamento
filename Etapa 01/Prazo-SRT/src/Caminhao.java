import java.util.ArrayList;

public class Caminhao {
    ArrayList<Pacotes> pacotesCaminhao;

    public Caminhao() {
        pacotesCaminhao = new ArrayList<>();
    }

    public void AdicionarNoCaminhao(Pacotes pacote) {
        pacotesCaminhao.add(pacote);
    }
}
