public class Tabuleiro {

    private Integer linha;
    private Integer coluna;
    private Peca[][] pecas;

    public Tabuleiro(Integer linha, Integer coluna) {
        this.linha = linha;
        this.coluna = coluna;
        pecas = new Peca[linha][coluna];
    }

    public Integer getLinha() {
        return linha;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }

    public Integer getColuna() {
        return coluna;
    }

    public void setColuna(Integer coluna) {
        this.coluna = coluna;
    }
}
