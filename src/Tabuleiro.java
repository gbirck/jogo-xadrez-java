public class Tabuleiro {

    private Integer linhas;
    private Integer colunas;
    private Peca[][] pecas;

    public Tabuleiro(Integer linhas, Integer colunas) {
        if (linhas < 1 || colunas < 1) {
            throw new TabulerioException("Erro criando tabuleiro: é necessário que haja ao menos 1 linha e 1 coluna");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        pecas = new Peca[linhas][colunas];
    }

    public Integer getLinhas() {
        return linhas;
    }

    public Integer getColunas() {
        return colunas;
    }

    public Peca peca(int linha, int coluna) {
        if (!posicaoExistente(linha, coluna)) {
            throw new TabulerioException("Erro: posição não esta no tabuleiro");
        }
        return pecas[linha][coluna];
    }

    public Peca peca(Posicao posicao) {
        if (!posicaoExistente(posicao)) {
            throw new TabulerioException("Erro: posição não esta no tabuleiro");
        }
        return pecas[posicao.getLinha()][posicao.getColuna()];
    }

    public void colocarPeca(Peca peca, Posicao posicao) {
        if (checagemPosicao(posicao)) {
            throw new TabulerioException("Erro: ja existe uma peça na posição " + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
        peca.posicao = posicao;
    }

    private boolean posicaoExistente(int linha, int coluna) {
        return linha >= 0 && linha < linhas && colunas >= 0 && coluna < colunas;
    }

    public boolean posicaoExistente(Posicao posicao) {
        return posicaoExistente(posicao.getLinha(), posicao.getColuna());
    }

    public boolean checagemPosicao(Posicao posicao) {
        if (!posicaoExistente(posicao)) {
            throw new TabulerioException("Erro: posição não esta no tabuleiro");
        }
        return peca(posicao) != null;
    }
}
