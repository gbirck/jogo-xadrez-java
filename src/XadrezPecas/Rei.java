package XadrezPecas;

import JogoTabuleiro.Peca;
import JogoTabuleiro.Posicao;
import JogoTabuleiro.Tabuleiro;
import Xadrez.Cor;
import Xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

    public Rei(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return "R";
    }

    private boolean podeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }
    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        // above
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna());
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // below
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // left
        p.definirValores(posicao.getLinha(), posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // right
        p.definirValores(posicao.getLinha(), posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // nw
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // ne
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // sw
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // se
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistente(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        return mat;
    }
}
