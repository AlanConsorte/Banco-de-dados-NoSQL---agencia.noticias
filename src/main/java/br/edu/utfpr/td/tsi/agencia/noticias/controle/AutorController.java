package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.BancoDados;

@Controller
public class AutorController {

    BancoDados bancoDados = new BancoDados();

    @GetMapping("cadastrarAutor")
    public String exibirPaginaCadastrarAutor(Model model) {
        
        model.addAttribute("autor", new Autor());
        return "cadastrarAutor";
    }

    @PostMapping("/autor/salvar")
    public String ReceberFormualarioCadastrarAutor(Autor autor) {
        bancoDados.cadastrar(autor);
        autor.setId(UUID.randomUUID().toString());
        return "index";
    }

    @GetMapping("listarAutores")
    public String exibirPaginaListarAutores(Model model) {
        List<Autor> autores = bancoDados.listarAutores();
        model.addAttribute("autores", autores);
        return "listarAutores";
    }

    @GetMapping("removerAutor")
    public String removerAutor(@RequestParam String idAutor) {
        bancoDados.removerAutor(idAutor);
        return "index";
    }

    @GetMapping("editarAutor")
    public String exibirPaginaEditarAutor(@RequestParam String idAutor, Model model) {
        Autor autor = bancoDados.localizarAutor(idAutor);
        model.addAttribute("autor", autor);
        return "editarAutor";
    }

    @PostMapping("editarAutor")
    public String editarAutor(Autor autorAtualizado) {
        bancoDados.atualizarAutor(autorAtualizado);
        return "redirect:/listarAutores";
    }

}
