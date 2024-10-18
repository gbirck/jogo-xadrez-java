package XadrezPecas;

import JogoTabuleiro.Tabuleiro;
import JogoTabuleiro.Posicao;
import Xadrez.PartidaXadrez;
import Xadrez.PecaXadrez;
import Xadrez.Cor;

public class Peao extends PecaXadrez {

    private PartidaXadrez partida;

    public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partida) {
        super(tabuleiro, cor);
        this.partida = partida;
    }

    @Override
    public boolean[][] possiveisMovimentos() {

        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        if (getCor() == Cor.WHITE) {
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().checagemPosicao(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().checagemPosicao(p) && getTabuleiro().posicaoExistente(p2) && !getTabuleiro().checagemPosicao(p2) && getContadorMovimento() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistente(p) && temUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistente(p) && temUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // #specialmove en passant white
            if (posicao.getLinha() == 3) {
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExistente(left) && temUmaPecaAdversaria(left) && getTabuleiro().peca(left) == partida.getVulneravel()) {
                    mat[left.getLinha() - 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExistente(right) && temUmaPecaAdversaria(right) && getTabuleiro().peca(right) == partida.getVulneravel()) {
                    mat[right.getLinha() - 1][right.getColuna()] = true;
                }
            }
        } else {
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().checagemPosicao(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().checagemPosicao(p) && getTabuleiro().posicaoExistente(p2) && !getTabuleiro().checagemPosicao(p2) && getContadorMovimento() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistente(p) && temUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistente(p) && temUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // #specialmove en passant black
            if (posicao.getLinha() == 4) {
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExistente(left) && temUmaPecaAdversaria(left) && getTabuleiro().peca(left) == partida.getVulneravel()) {
                    mat[left.getLinha() + 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExistente(right) && temUmaPecaAdversaria(right) && getTabuleiro().peca(right) == partida.getVulneravel()) {
                    mat[right.getLinha() + 1][right.getColuna()] = true;
                }
            }
        }
        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }
}