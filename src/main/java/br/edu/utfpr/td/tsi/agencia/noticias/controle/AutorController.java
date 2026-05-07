package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AutorDAO;

@Controller
public class AutorController {

    @Autowired
    private AutorDAO autorDAO; 

    @GetMapping("cadastrarAutor")
    public String exibirPaginaCadastrarAutor(Model model) {
        
        model.addAttribute("autor", new Autor());
        return "cadastrarAutor";
    }

    @PostMapping("/autor/salvar")
    public String ReceberFormualarioCadastrarAutor(Autor autor) {
        autorDAO.cadastrarAutor(autor);
        autor.setId(UUID.randomUUID().toString());
        return "index";
    }

    @GetMapping("listarAutores")
    public String exibirPaginaListarAutores(Model model) {
        List<Autor> autores = autorDAO.listarTodosAutor();
        model.addAttribute("autores", autores);
        return "listarAutores";
    }

    @GetMapping("removerAutor")
    public String removerAutor(@RequestParam String idAutor) {
        autorDAO.removerAutor(idAutor);
        return "index";
    }

    @GetMapping("editarAutor")
    public String exibirPaginaEditarAutor(@RequestParam String idAutor, Model model) {
        Autor autor = autorDAO.buscarAutor(idAutor);
        model.addAttribute("autor", autor);
        return "editarAutor";
    }

    @PostMapping("editarAutor")
    public String editarAutor(Autor autorAtualizado) {
        autorDAO.atualizarAutor(autorAtualizado);
        return "redirect:/listarAutores";
    }

}
