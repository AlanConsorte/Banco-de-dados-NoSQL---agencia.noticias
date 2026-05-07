package br.edu.utfpr.td.tsi.agencia.noticias.persistencia;

import java.util.List;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;

public interface AutorDAO {

    public void cadastrarAutor(Autor autor);

    public Autor buscarAutor(String id);

    public void removerAutor(String id);

    public void atualizarAutor(Autor autor);

    public List<Autor> listarTodosAutor();

}
