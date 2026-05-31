package br.edu.utfpr.td.tsi.agencia.noticias.controle;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Assunto;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.SituacaoNoticia;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AutorRepository;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AssuntosRepository;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.NoticiaRepository;

import org.springframework.data.mongodb.core.MongoTemplate;

@Controller
public class NoticiaController {

	@Autowired
	private NoticiaRepository noticiaRepository;

	@Autowired
	private AutorRepository autorRepository;

	@Autowired
	private AssuntosRepository assuntoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping(value = "/cadastrarNoticias")
	public String exibirPaginaCadastrarNoticia(Model model) {
		model.addAttribute("autores", autorRepository.findAll());
		model.addAttribute("assuntos", assuntoRepository.findAll());
		model.addAttribute("situacoes", SituacaoNoticia.values());
		return "cadastrarNoticia";
	}

	@GetMapping(value = "/limparBase")
	public String limparBase() {
		mongoTemplate.dropCollection("noticias");
		return "redirect:/listarNoticias";
	}

	@PostMapping(value = "/cadastrarNoticias")
	public String cadastrarDocumento(
			@RequestParam String titulo,
			@RequestParam("autor.id") String autorId,
			@RequestParam("assuntoRelacionado.id") String assuntoId,
			@RequestParam SituacaoNoticia situacao,
			@RequestParam String conteudo,
			Model model) {
		
		Noticia noticia = new Noticia();
		noticia.setTitulo(titulo);
		noticia.setConteudo(conteudo);
		noticia.setSituacao(situacao);
		
		Autor autor = new Autor();
		autor.setId(autorId);
		noticia.setAutor(autor);
		
		Assunto assunto = new Assunto();
		assunto.setId(assuntoId);
		noticia.setAssuntoRelacionado(assunto);
		
		noticia.setDataCriacao(LocalDate.now());
		
		long count = noticiaRepository.countByAutorIdAndAssuntoRelacionadoIdAndDataCriacao(
				noticia.getAutor().getId(), noticia.getAssuntoRelacionado().getId(), noticia.getDataCriacao());
		if (count >= 2) {
			model.addAttribute("erro", "Limite máximo de 2 notícias sobre o mesmo assunto por dia atingido para este autor.");
			model.addAttribute("autores", autorRepository.findAll());
			model.addAttribute("assuntos", assuntoRepository.findAll());
			model.addAttribute("situacoes", SituacaoNoticia.values());
			return "cadastrarNoticia";
		}

		noticia.setId(UUID.randomUUID().toString());
		noticiaRepository.save(noticia);
		return "redirect:/listarNoticias";
	}

	@GetMapping(value = "/listarNoticias")
	public String exibirPaginaListarNoticias(
			@RequestParam(required = false) String autorId,
			@RequestParam(required = false) String assuntoId,
			@RequestParam(required = false) SituacaoNoticia situacao,
			Model model) {
		List<Noticia> noticias = noticiaRepository.findAll();
		List<Noticia> filtered = new ArrayList<>();

		for (Noticia n : noticias) {
			if (autorId != null && !autorId.isEmpty() && (n.getAutor() == null || !n.getAutor().getId().equals(autorId))) continue;
			if (assuntoId != null && !assuntoId.isEmpty() && (n.getAssuntoRelacionado() == null || !n.getAssuntoRelacionado().getId().equals(assuntoId))) continue;
			if (situacao != null && n.getSituacao() != situacao) continue;

			if (n.getAutor() != null && n.getAutor().getId() != null) {
				n.setAutor(autorRepository.findById(n.getAutor().getId()).orElse(null));
			}
			if (n.getAssuntoRelacionado() != null && n.getAssuntoRelacionado().getId() != null) {
				n.setAssuntoRelacionado(assuntoRepository.findById(n.getAssuntoRelacionado().getId()).orElse(null));
			}
			filtered.add(n);
		}

		model.addAttribute("noticias", filtered);
		model.addAttribute("autores", autorRepository.findAll());
		model.addAttribute("assuntos", assuntoRepository.findAll());
		model.addAttribute("situacoes", SituacaoNoticia.values());
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
		model.addAttribute("autores", autorRepository.findAll());
		model.addAttribute("assuntos", assuntoRepository.findAll());
		model.addAttribute("situacoes", SituacaoNoticia.values());
		return "editarNoticia";
	}

	@PostMapping(value = "/editarNoticia")
	public String editarNoticia(
			@RequestParam String idNoticia,
			@RequestParam String titulo,
			@RequestParam("autor.id") String autorId,
			@RequestParam("assuntoRelacionado.id") String assuntoId,
			@RequestParam SituacaoNoticia situacao,
			@RequestParam String conteudo) {
		
		Noticia noticia = new Noticia();
		noticia.setId(idNoticia);
		noticia.setTitulo(titulo);
		noticia.setConteudo(conteudo);
		noticia.setSituacao(situacao);
		
		Autor autor = new Autor();
		autor.setId(autorId);
		noticia.setAutor(autor);
		
		Assunto assunto = new Assunto();
		assunto.setId(assuntoId);
		noticia.setAssuntoRelacionado(assunto);
		
		Noticia noticiaAntiga = noticiaRepository.findById(idNoticia).orElse(null);
		if (noticiaAntiga != null) {
			noticia.setDataCriacao(noticiaAntiga.getDataCriacao());
		}
		
		noticiaRepository.save(noticia);
		return "redirect:/listarNoticias";
	}
}
