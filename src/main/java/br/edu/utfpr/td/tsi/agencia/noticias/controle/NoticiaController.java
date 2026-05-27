package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AutorRepository;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.NoticiaRepository;

@Controller
public class NoticiaController {

	@Autowired
	private NoticiaRepository noticiaRepository;

	@Autowired
	private AutorRepository autorRepository;

	@GetMapping(value = "/cadastrarNoticias")
	public String exibirPaginaCadastrarNoticia(Model model) {
		List<Autor> autores = autorRepository.findAll();
		model.addAttribute("autores", autores);
		return "cadastrarNoticia";
	}

	@PostMapping(value = "/cadastrarNoticias")
	public String cadastrarDocumento(Noticia noticia) {
		noticia.setDataCriacao(LocalDate.now());
		noticia.setId(UUID.randomUUID().toString());
		noticiaRepository.save(noticia);
		return "redirect:/listarNoticias";
	}

	@GetMapping(value = "/listarNoticias")
	public String exibirPaginaListarNoticias(Model model) {
		List<Noticia> noticias = noticiaRepository.findAll();
		for(Noticia noticia : noticias) {
			if(noticia.getAutor() != null && noticia.getAutor().getId() != null) {
				Autor autor = autorRepository.findById(noticia.getAutor().getId()).orElse(null);
				noticia.setAutor(autor);
			}
		}
		model.addAttribute("noticias", noticias);
		return "listarNoticias";
	}

	@GetMapping(value = "/removerNoticia")
	public String removerDocumentos(@RequestParam String idNoticia) {
		noticiaRepository.deleteById(idNoticia);
		return "redirect:/listarNoticias";
	}

	@GetMapping(value = "/editarNoticia")
	public String exibirPaginaparaEditarNoticia(@RequestParam String idNoticia, Model model) {
		Noticia noticia = noticiaRepository.findById(idNoticia).orElse(null);
		model.addAttribute("noticia", noticia);
		return "editarNoticia";
	}

	@PostMapping(value = "/editarNoticia")
	public String editarNoticia(@RequestParam String idNoticia, Noticia noticia) {
		noticia.setId(idNoticia);
		
		Noticia noticiaAntiga = noticiaRepository.findById(idNoticia).orElse(null);
		if(noticiaAntiga != null) {
			noticia.setDataCriacao(noticiaAntiga.getDataCriacao());
		}
		
		noticiaRepository.save(noticia);
		return "redirect:/listarNoticias";
	}
}

