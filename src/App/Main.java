package App;

import Xadrez.PartidaXadrez;
import Xadrez.PecaXadrez;
import Xadrez.PosicaoXadrez;
import Xadrez.XadrezException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PartidaXadrez partida = new PartidaXadrez();
        List<PecaXadrez> capturada = new ArrayList<>();

        while (!partida.getCheckMate()) {
            try {
                UI.limpaTela();
                UI.mostrarPartida(partida, capturada);
                System.out.println();
                System.out.print("Origem: ");
                PosicaoXadrez origem = UI.verPosicaoXadrez(sc);

                boolean[][] possiveisMovimentos = partida.possiveisMovimentos(origem);
                UI.limpaTela();
                UI.mostrarTabuleiro(partida.getPecas(), possiveisMovimentos);
                System.out.println();
                System.out.print("Destino: ");
                PosicaoXadrez destino = UI.verPosicaoXadrez(sc);

                PecaXadrez pecaCapturada = partida.fazerMovimentoXadrez(origem, destino);

                if (pecaCapturada != null) {
                    capturada.add(pecaCapturada);
                }

                if (partida.getPromoted() != null) {
                    System.out.print("Insira a peça para promoçao (C/B/Y/T): ");
                    String tipo = sc.nextLine().toUpperCase();
                    while (!tipo.equals("C") && !tipo.equals("B") && !tipo.equals("Y") & !tipo.equals("T")) {
                        System.out.print("Valor invalido! Insira a peça para promoçao (C/B/Y/T): ");
                        tipo = sc.nextLine().toUpperCase();
                    }
                    partida.substituirPeca(tipo);
                }
            } catch (XadrezException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.limpaTela();
        UI.mostrarPartida(partida, capturada);
    }
}