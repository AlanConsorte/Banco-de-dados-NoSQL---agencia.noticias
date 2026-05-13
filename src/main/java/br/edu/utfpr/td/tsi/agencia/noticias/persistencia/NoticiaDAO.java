package br.edu.utfpr.td.tsi.agencia.noticias.persistencia;

import java.util.List;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;

public interface NoticiaDAO {

    public void persistirNoticia(Noticia noticia);

    public Noticia localizarNoticia(String id);
    
    public void removerNoticia(String id);

    public Noticia atualizarNoticia(String id);

    public List<Noticia> listarNoticias();


}
