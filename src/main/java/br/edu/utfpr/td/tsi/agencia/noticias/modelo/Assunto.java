package br.edu.utfpr.td.tsi.agencia.noticias.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assuntos")
public class Assunto {

    @Id
    private String id;
    private String nomeAssunto;

    public String getId() {
        return id;
    }

    public String getAssunto() {
        return nomeAssunto;
    }

    public void setAssunto(String nomeAssunto) {
        this.nomeAssunto = nomeAssunto;
    }

    public void setId(String id) {
        this.id = id;
    }

}
