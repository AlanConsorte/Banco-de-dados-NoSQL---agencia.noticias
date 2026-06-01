package br.edu.utfpr.td.tsi.agencia.noticias.persistencia;

import java.time.LocalDate;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Assunto;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.SituacaoNoticia;

public interface NoticiaRepository extends MongoRepository<Noticia, String> {

    long countByAutorIdAndAssuntoRelacionadoIdAndDataCriacao(String autorId, String assuntoId, LocalDate dataCriacao);

    Noticia findAllByAutor(Autor autor);

    Noticia findAllByAssunto(Assunto assunto);

    Noticia findAllbysituacaoNoticia(SituacaoNoticia situacaoNoticia);

}