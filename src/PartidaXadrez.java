public class PartidaXadrez {

    private Tabuleiro tabuleiro;

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8,8);
        inicioJogo();
    }

    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinha()][tabuleiro.getColuna()];
        for (int i = 0; i < tabuleiro.getLinha(); i++) {
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    private void inicioJogo() {
        tabuleiro.colocarPeca(new Torre(tabuleiro, Cor.WHITE), new Posicao(2,1));
        tabuleiro.colocarPeca(new Rei(tabuleiro, Cor.BLACK), new Posicao(0,4));
        tabuleiro.colocarPeca(new Rei(tabuleiro, Cor.WHITE), new Posicao(7,4));
    }
}
