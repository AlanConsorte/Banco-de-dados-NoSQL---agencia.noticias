package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.BancoDados;

@Controller
public class NoticiaController {

	private BancoDados bancoDados = new BancoDados();

	@GetMapping(value = "/cadastrarNoticia")
	public String exibirPaginaCadastrarNoticia() {
		return "cadastrarNoticia";
	}

	@PostMapping(value = "/cadastrarNoticia")
	public String cadastrarDocumento(Noticia noticia) {
		noticia.setId(UUID.randomUUID().toString());
		bancoDados.persistirNoticia(noticia);
		System.out.println(noticia);
		return "index";
	}

	@GetMapping(value = "/listarNoticias")
	public String exibirPaginaListarNoticias(Model model) {
		List<Noticia> noticias = bancoDados.listarNoticia();
		System.out.println(noticias);
		model.addAttribute("noticias", noticias);
		return "listarNoticias";
	}

	@GetMapping(value = "/removerNoticia")
	public String removerDocumentos(@RequestParam String idDocumento) {
		System.out.println("removendo noticia de id " + idDocumento);
		bancoDados.removerDocumento(idDocumento);
		return "index";
	}
	
	@GetMapping(value = "/editarNoticia")
	public String exibirPaginaparaEditarNoticia(@RequestParam String idNoticia, Model model) {
		Noticia noticia = bancoDados.localizarNoticia(idNoticia);
		model.addAttribute("noticia", noticia);
		return "editarNoticia";
		
	}
	
	@PostMapping(value = "/editarNoticia")
	public String editarNoticia(Noticia noticiaAtualizada, @RequestParam String idNoticia, Model model) {
		String id = noticiaAtualizada.getId();
		bancoDados.removerDocumento(id);
		bancoDados.persistirNoticia(noticiaAtualizada);
		return "redirect:/listarNoticias";
	}

}
