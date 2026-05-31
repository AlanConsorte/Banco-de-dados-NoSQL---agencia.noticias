package br.edu.utfpr.td.tsi.agencia.noticias.modelo;

public enum SituacaoNoticia {
    EM_PRODUCAO("Em Produção"),
    PUBLICADA("Publicada"),
    CANCELADA("Cancelada");

    private String descricao;

    SituacaoNoticia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
