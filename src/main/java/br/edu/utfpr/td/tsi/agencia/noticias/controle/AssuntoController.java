package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Assunto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AssuntosRepository;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.UUID;

@Controller
public class AssuntoController {

    @Autowired
    private AssuntosRepository assuntoRepository;

    @GetMapping("cadastrarAssunto")
    public String CadastrarAssunto(Model model) {
        model.addAttribute("assunto", new Assunto());
        return "cadastrarAssunto";
    }

    @PostMapping("ReceberFormularioCadastrarAssunto")
    public String ReceberFormularioCadastrarAssunto(Assunto assunto) {
        assunto.setId(UUID.randomUUID().toString());
        assuntoRepository.save(assunto);
        return "redirect:/listarAssuntos";
    }

    @GetMapping("listarAssuntos")
    public String ListarAssunto(Model model) {
        List<Assunto> assuntos = assuntoRepository.findAll();
        model.addAttribute("assuntos", assuntos);
        return "listarAssuntos";
    }

    @GetMapping("removerAssunto")
    public String removerAssunto(@RequestParam String idAssunto) {
        assuntoRepository.deleteById(idAssunto);
        return "redirect:/listarAssuntos";
    }

    @GetMapping("editarAssunto")
    public String exibirPaginaEditarAssunto(@RequestParam String idAssunto, Model model) {
        Assunto assunto = assuntoRepository.findById(idAssunto).orElse(null);
        model.addAttribute("assunto", assunto);
        return "editarAssunto";
    }

    @PostMapping("editarAssunto")
    public String editarAssunto(Assunto assuntoAtualizado) {
        assuntoRepository.save(assuntoAtualizado);
        return "redirect:/listarAssuntos";
    }

}
