package br.edu.utfpr.td.tsi.agencia.noticias.modelo;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "noticias")
public class Noticia {

	@Id
	private String id;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataCriacao;
	private String titulo;
	private Assunto assuntoRelacionado = new Assunto();
	private Autor autor = new Autor();
	private String conteudo;
	private SituacaoNoticia situacao;

	public Noticia() {
	}

	public Noticia(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Assunto getAssuntoRelacionado() {
		return assuntoRelacionado;
	}

	public void setAssuntoRelacionado(Assunto assuntoRelacionado) {
		this.assuntoRelacionado = assuntoRelacionado;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public SituacaoNoticia getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoNoticia situacao) {
		this.situacao = situacao;
	}

}
