package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AutorRepository;

@Controller
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @GetMapping("cadastrarAutor")
    public String exibirPaginaCadastrarAutor(Model model) {
        model.addAttribute("autor", new Autor());
        return "cadastrarAutor";
    }

   @PostMapping("/autor/salvar")
    public String ReceberFormualarioCadastrarAutor(Autor autor) {
        autor.setId(UUID.randomUUID().toString());
        autorRepository.save(autor);
        return "index";
    }

    @GetMapping("listarAutores")
    public String exibirPaginaListarAutores(Model model) {
        List<Autor> autores = autorRepository.findAll();
        model.addAttribute("autores", autores);
        return "listarAutores";
    }

    @GetMapping("removerAutor")
    public String removerAutor(@RequestParam String idAutor) {
        autorRepository.deleteById(idAutor);
        return "redirect:/listarAutores";
    }

    @GetMapping("editarAutor")
    public String exibirPaginaEditarAutor(@RequestParam String idAutor, Model model) {
        Autor autor = autorRepository.findById(idAutor).orElse(null);
        model.addAttribute("autor", autor);
        return "editarAutor";
    }

    @PostMapping("editarAutor")
    public String editarAutor(Autor autorAtualizado) {
        autorRepository.save(autorAtualizado);
        return "redirect:/listarAutores";
    }

    @PostMapping("ListarAutoresPorEmail")
    public String exibirPaginaListarAutoresPorEmail(String email, Model model) {
        Autor autor = autorRepository.findOneByEmail(email);
        List<Autor> autores = new ArrayList<>();
        if (autor != null) {
            autores.add(autor);
        }
        model.addAttribute("autores", autores);
        return "listarAutores";
    }

}
