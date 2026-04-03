package br.edu.utfpr.td.tsi.agencia.noticias.persistencia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;

public class BancoDados {

	private List<Noticia> noticias = new ArrayList<Noticia>();

	public void persistirNoticia(Noticia documento) {
		this.noticias.add(documento);
	}

	public List<Noticia> listarNoticia() {
		return noticias;
	}
	
	public Noticia localizarNoticia(String id) {
		Noticia noticiaLocalizada = null;
		for (Noticia noticia : noticias) {
			if(noticia.getId().equals(id)) {
				noticiaLocalizada = noticia;
			}
		}
		return noticiaLocalizada;
	}

	public void removerDocumento(String id) {
		List<Noticia> listaAtualizada = new ArrayList<Noticia>();
		for (Noticia noticia : this.noticias) {
			if (!noticia.getId().equals(id)) {
				listaAtualizada.add(noticia);
			}
		}
		this.noticias = listaAtualizada;
	}
}
