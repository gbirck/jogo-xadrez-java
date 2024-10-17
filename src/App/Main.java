package App;

import Xadrez.PartidaXadrez;
import Xadrez.PecaXadrez;
import Xadrez.PosicaoXadrez;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PartidaXadrez partida = new PartidaXadrez();

        while (true) {

            UI.mostrarTabuleiro(partida.getPecas());
            System.out.println();
            System.out.print("Origem: ");
            PosicaoXadrez origem = UI.verPosicaoXadrez(sc);

            System.out.println();
            System.out.print("Destino: ");
            PosicaoXadrez destino = UI.verPosicaoXadrez(sc);

            PecaXadrez pecaCapturada = partida.fazerMovimentoXadrez(origem, destino);
        }

    }
}