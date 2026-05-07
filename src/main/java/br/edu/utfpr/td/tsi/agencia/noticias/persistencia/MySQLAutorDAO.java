package br.edu.utfpr.td.tsi.agencia.noticias.persistencia;

import org.springframework.stereotype.Component;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;

import java.util.List;

@Component
public class MySQLAutorDAO implements AutorDAO {

    @Override
    public void cadastrarAutor(Autor autor) {
        System.out.println("gravando na tabela autor");
        // Implementação para cadastrar autor no MySQL
    }

    @Override
    public Autor buscarAutor(String id) {

        // Implementação para buscar autor por ID no MySQL
        return null;
    }

    @Override
    public void removerAutor(String id) {
        // Implementação para remover autor por ID no MySQL
    }

    @Override
    public void atualizarAutor(Autor autor) {
        // Implementação para atualizar autor no MySQL
    }

    @Override
    public List<Autor> listarTodosAutor() {
        // Implementação para listar todos os autores no MySQL
        return null;
    }   

}
