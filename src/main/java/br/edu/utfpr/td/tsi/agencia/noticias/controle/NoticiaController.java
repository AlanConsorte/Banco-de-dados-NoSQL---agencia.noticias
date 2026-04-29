package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.BancoDados;

@Controller
public class NoticiaController {

	private BancoDados bancoDados = new BancoDados();

	@GetMapping(value = "/cadastrarNoticias")
	public String exibirPaginaCadastrarNoticia(Model model) {
		List<Autor> autores = bancoDados.listarAutores();
		model.addAttribute("autores", autores);
		return "cadastrarNoticia";
	}

	@PostMapping(value = "/cadastrarNoticias")
	public String cadastrarDocumento(Noticia noticia) {
		if (noticia.getAutor() != null && noticia.getAutor().getId() != null) {
			Autor autorSelecionado = bancoDados.localizarAutor(noticia.getAutor().getId());
			if (autorSelecionado != null) {
				noticia.setAutor(autorSelecionado);
			}
		}
		noticia.setId(UUID.randomUUID().toString());
		bancoDados.persistirNoticia(noticia);
		return "redirect:/listarNoticias";
	}

	@GetMapping(value = "/listarNoticias")
	public String exibirPaginaListarNoticias(Model model) {
		List<Noticia> noticias = bancoDados.listarNoticias();
		model.addAttribute("noticias", noticias);
		return "listarNoticias";
	}

	@GetMapping(value = "/removerNoticia")
	public String removerDocumentos(@RequestParam String idDocumento) {
		bancoDados.removerDocumento(idDocumento);
		return "index";
	}

	@GetMapping(value = "/editarNoticia")
	public String exibirPaginaparaEditarNoticia(@RequestParam String idNoticia, Model model) {
		Noticia noticia = bancoDados.localizarNoticia(idNoticia);
		List<Autor> autores = bancoDados.listarAutores();
		model.addAttribute("noticia", noticia);
		model.addAttribute("autores", autores);
		return "editarNoticia";

	}

	@PostMapping(value = "/editarNoticia")
	public String editarNoticia(Noticia noticiaAtualizada, @RequestParam String idNoticia, Model model) {
		// Busca a notícia original para garantir que dataCriacao não será nulo
		Noticia noticiaOriginal = bancoDados.localizarNoticia(idNoticia);
		if (noticiaOriginal != null) {
			noticiaAtualizada.setId(noticiaOriginal.getId());
			noticiaAtualizada.setDataCriacao(noticiaOriginal.getDataCriacao());
		}
		if (noticiaAtualizada.getAutor() != null && noticiaAtualizada.getAutor().getId() != null) {
			Autor autorSelecionado = bancoDados.localizarAutor(noticiaAtualizada.getAutor().getId());
			if (autorSelecionado != null) {
				noticiaAtualizada.setAutor(autorSelecionado);
			}
		}
		bancoDados.persistirNoticia(noticiaAtualizada);
		return "redirect:/listarNoticias";
	}

}
