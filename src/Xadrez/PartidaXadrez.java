package Xadrez;

import JogoTabuleiro.Peca;
import JogoTabuleiro.Posicao;
import JogoTabuleiro.Tabuleiro;
import XadrezPecas.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PartidaXadrez {

    private static Tabuleiro tabuleiro;
    private int turno;
    private Cor jogador;
    private boolean check;
    private boolean checkMate;
    private PecaXadrez vulneravel;
    private PecaXadrez promoted;

    private final List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private final List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogador = Cor.WHITE;
        inicioJogo();
    }

    public int getTurno() {
        return turno;
    }


    public Cor getJogador() {
        return jogador;
    }


    public boolean getCheck() {
        return check;
    }


    public boolean getCheckMate() {
        return checkMate;
    }


    public PecaXadrez getVulneravel() {
        return vulneravel;
    }


    public PecaXadrez getPromoted() {
        return promoted;
    }

    public static PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoOriginal) {
        Posicao posicao = posicaoOriginal.toPosicao();
        validarPosicaoOrigem(posicao);
        return tabuleiro.peca(posicao).possiveisMovimentos();
    }

    public PecaXadrez fazerMovimentoXadrez(PosicaoXadrez posOrigem, PosicaoXadrez posDestino) {
        Posicao origem = posOrigem.toPosicao();
        Posicao destino = posDestino.toPosicao();
        validarPosicaoOrigem(origem);
        validarPosicaoDestino(origem, destino);
        Peca pecaCapturada = fazerMovimento(origem, destino);

        if (testCheck(jogador)) {
            desfazerMovimento(origem, destino, pecaCapturada);
            throw new XadrezException("Voce nao pode se colocar em check");
        }

        PecaXadrez pecaMovida = (PecaXadrez)tabuleiro.peca(destino);

        // #specialmove promotion
        promoted = null;
        if (pecaMovida instanceof Peao) {
            if ((pecaMovida.getCor() == Cor.WHITE && destino.getLinha() == 0) || (pecaMovida.getCor() == Cor.BLACK && destino.getLinha() == 7)) {
                promoted = (PecaXadrez)tabuleiro.peca(destino);
                promoted = substituirPeca("R");
            }
        }

        check = testCheck(adversario(jogador));

        if (testCheckMate(adversario(jogador))) {
            checkMate = true;
        }
        else {
            proxTurno();
        }

        if (pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
            vulneravel = pecaMovida;
        }
        else {
            vulneravel = null;
        }

        return (PecaXadrez)pecaCapturada;
    }

    public PecaXadrez substituirPeca(String tipo) {
        if (promoted == null) {
            throw new IllegalStateException("Nenhuma peça para ser promovida");
        }
        if (!tipo.equals("C") && !tipo.equals("B") && !tipo.equals("Y") & !tipo.equals("T")) {
            return promoted;
        }

        Posicao pos = promoted.getPosicaoXadrez().toPosicao();
        Peca p = tabuleiro.removerPeca(pos);
        pecasNoTabuleiro.remove(p);

        PecaXadrez novaPeca = novaPeca(tipo, promoted.getCor());
        tabuleiro.colocarPeca(novaPeca, pos);
        pecasNoTabuleiro.add(novaPeca);

        return novaPeca;
    }

    private PecaXadrez novaPeca(String tipo, Cor cor) {
        if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
        if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
        if (tipo.equals("Y")) return new Rainha(tabuleiro, cor);
        return new Torre(tabuleiro, cor);
    }

    private Peca fazerMovimento(Posicao origem, Posicao destino) {
        PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(origem);
        p.adicionarContadorMovimento();
        Peca pecaCapturada = tabuleiro.removerPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
            tabuleiro.colocarPeca(torre, destinoT);
            torre.adicionarContadorMovimento();
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao sourceT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao targetT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(sourceT);
            tabuleiro.colocarPeca(torre, targetT);
            torre.adicionarContadorMovimento();
        }

        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
                Posicao posicaoPeao;
                if (p.getCor() == Cor.WHITE) {
                    posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                }
                else {
                    posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.removerPeca(posicaoPeao);
                pecasCapturadas.add(pecaCapturada);
                pecasNoTabuleiro.remove(pecaCapturada);
            }
        }
        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
        PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(destino);
        p.diminuirContadorMovimento();
        tabuleiro.colocarPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocarPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
            tabuleiro.colocarPeca(torre, origemT);
            torre.diminuirContadorMovimento();
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
            tabuleiro.colocarPeca(torre, origemT);
            torre.diminuirContadorMovimento();
        }

        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulneravel) {
                PecaXadrez peao = (PecaXadrez)tabuleiro.removerPeca(destino);
                Posicao posicaoPeao;
                if (p.getCor() == Cor.WHITE) {
                    posicaoPeao = new Posicao(3, destino.getColuna());
                }
                else {
                    posicaoPeao = new Posicao(4, destino.getColuna());
                }
                tabuleiro.colocarPeca(peao, posicaoPeao);
            }
        }
    }

    private void validarPosicaoOrigem(Posicao posicao) {
        if (!tabuleiro.checagemPosicao(posicao)) {
            throw new XadrezException("Nenhuma peça na posição de origem");
        }
        if (jogador != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
            throw new XadrezException("A peça selecionada nao é sua");
        }
        if (!tabuleiro.peca(posicao).temAlgumMovimentoPossivel()) {
            throw new XadrezException("Nenhum movimento possivel para essa peça");
        }
    }

    private void validarPosicaoDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).possivelMovimento(destino)) {
            throw new XadrezException("A peça selecionada não pode se mover para o destino");
        }
    }

    private void proxTurno() {
        turno++;
        jogador = (jogador == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
    }

    private Cor adversario(Cor cor) {
        return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
    }

    private PecaXadrez rei(Cor cor) {
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list) {
            if (p instanceof Rei) {
                return (PecaXadrez) p;
            }
        }
        throw new IllegalStateException("There is no " + cor + " king on the board");
    }

    private boolean testCheck(Cor cor) {
        Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
        List<Peca> pecasAdversarias = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == adversario(cor)).collect(Collectors.toList());
        for (Peca p : pecasAdversarias) {
            boolean[][] mat = p.possiveisMovimentos();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Cor cor) {
        if (!testCheck(cor)) {
            return false;
        }
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list) {
            boolean[][] mat = p.possiveisMovimentos();
            for (int i=0; i<tabuleiro.getLinhas(); i++) {
                for (int j=0; j<tabuleiro.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().toPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca pecaCapturada = fazerMovimento(origem, destino);
                        boolean testCheck = testCheck(cor);
                        desfazerMovimento(origem, destino, pecaCapturada);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecasNoTabuleiro.add(peca);
    }

    private void inicioJogo() {
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('b', 1, new Bispo(tabuleiro, Cor.WHITE));
        colocarNovaPeca('c', 1, new Cavalo(tabuleiro, Cor.WHITE));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.WHITE));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('f', 1, new Cavalo(tabuleiro, Cor.WHITE));
        colocarNovaPeca('g', 1, new Bispo(tabuleiro, Cor.WHITE));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.WHITE, this));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.WHITE, this));

        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('b', 8, new Bispo(tabuleiro, Cor.BLACK));
        colocarNovaPeca('c', 8, new Cavalo(tabuleiro, Cor.BLACK));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.BLACK));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('f', 8, new Cavalo(tabuleiro, Cor.BLACK));
        colocarNovaPeca('g', 8, new Bispo(tabuleiro, Cor.BLACK));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.BLACK, this));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.BLACK, this));
    }
}
