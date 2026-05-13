package br.edu.utfpr.td.tsi.agencia.noticias.persistencia.mongoDB;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.NoticiaDAO;

@Component
public class MongoDBNoticiaDAO implements NoticiaDAO {

	private String connectionString = "mongodb://localhost:27017";

	private String databaseName = "2159929-Noticias-Web";

    @Override
    public void persistirNoticia(Noticia noticia) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("noticias");

			Document document = new Document();
			document.append("_id", noticia.getId());
			document.append("titulo", noticia.getTitulo());
			document.append("assunto", noticia.getAssunto());
			document.append("conteudo", noticia.getConteudo());
			document.append("dataCriacao",
					Date.from(noticia.getDataCriacao().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			Autor autor = noticia.getAutor();
			if (autor != null) {
				Document documentAutor = new Document();
				if (autor.getId() == null) {
					autor.setId(UUID.randomUUID().toString());
				}
				documentAutor.append("_id", autor.getId());
				documentAutor.append("nome", autor.getNome());
				documentAutor.append("email", autor.getEmail());
				if (autor.getDataNascimento() != null) {
					documentAutor.append("dataNascimento",
							Date.from(autor.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				}

				document.append("autor", documentAutor);
			}

			// Se já existe, faz update. Se não, insere.
			Document filtro = new Document("_id", noticia.getId());
			if (collection.find(filtro).first() != null) {
				collection.replaceOne(filtro, document);
			} else {
				// Garante que novas notícias tenham id
				if (noticia.getId() == null || noticia.getId().isEmpty()) {
					noticia.setId(UUID.randomUUID().toString());
					document.put("_id", noticia.getId());
				}
				collection.insertOne(document);
			}
		}
    }

    @Override
    public Noticia localizarNoticia(String id) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("noticias");

			Document document = collection.find(new Document("_id", id)).first();
			if (document != null) {
				String titulo = document.getString("titulo");
				String assunto = document.getString("assunto");
				String conteudo = document.getString("conteudo");
				Date dataCriacaoDate = document.getDate("dataCriacao");
				LocalDate dataCriacao = LocalDate.ofInstant(dataCriacaoDate.toInstant(), ZoneId.systemDefault());

				Noticia noticia = new Noticia(id);
				noticia.setId(id);
				noticia.setTitulo(titulo);
				noticia.setAssunto(assunto);
				noticia.setConteudo(conteudo);
				noticia.setDataCriacao(dataCriacao);

				Document documentAutor = (Document) document.get("autor");
				String idAutor = documentAutor.getString("_id");
				String nomeAutor = documentAutor.getString("nome");
				String emailAutor = documentAutor.getString("email");
				Date dataNascimentoDate = documentAutor.getDate("dataNascimento");
				LocalDate dataNascimento = LocalDate.ofInstant(dataNascimentoDate.toInstant(), ZoneId.systemDefault());

				Autor autor = new Autor();
				autor.setId(idAutor);
				autor.setNome(nomeAutor);
				autor.setEmail(emailAutor);
				autor.setDataNascimento(dataNascimento);
				noticia.setAutor(autor);

				return noticia;
			}
		}
		return null;
    }

    @Override
    public void removerNoticia(String id) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("noticias");

			collection.deleteOne(new Document("_id", id));
		}
    }

    @Override
    public Noticia atualizarNoticia(String id) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("noticias");

			Document document = collection.find(new Document("_id", id)).first();
			if (document != null) {
				String titulo = document.getString("titulo");
				String assunto = document.getString("assunto");
				String conteudo = document.getString("conteudo");
				Date dataCriacaoDate = document.getDate("dataCriacao");
				LocalDate dataCriacao = LocalDate.ofInstant(dataCriacaoDate.toInstant(), ZoneId.systemDefault());

				Noticia noticia = new Noticia(id);
				noticia.setId(id);
				noticia.setTitulo(titulo);
				noticia.setAssunto(assunto);
				noticia.setConteudo(conteudo);
				noticia.setDataCriacao(dataCriacao);

				Document documentAutor = (Document) document.get("autor");
				String idAutor = documentAutor.getString("_id");
				String nomeAutor = documentAutor.getString("nome");
				String emailAutor = documentAutor.getString("email");
				Date dataNascimentoDate = documentAutor.getDate("dataNascimento");
				LocalDate dataNascimento = LocalDate.ofInstant(dataNascimentoDate.toInstant(), ZoneId.systemDefault());

				Autor autor = new Autor();
				autor.setId(idAutor);
				autor.setNome(nomeAutor);
				autor.setEmail(emailAutor);
				autor.setDataNascimento(dataNascimento);
				noticia.setAutor(autor);

				return noticia;
			}
        }
		
		return null;
    }

    @Override
    public List<Noticia> listarNoticias() {
        ArrayList<Noticia> noticias = new ArrayList<>();

		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("noticias");

			FindIterable<Document> documentosEncontrados = collection.find();
			for (Document document : documentosEncontrados) {

				String id = document.getString("_id");
				String titulo = document.getString("titulo");
				String assunto = document.getString("assunto");
				String conteudo = document.getString("conteudo");
				Date dataCriacaoDate = document.getDate("dataCriacao");
				LocalDate dataCriacao = LocalDate.ofInstant(dataCriacaoDate.toInstant(), ZoneId.systemDefault());

				Noticia noticia = new Noticia(id);
				noticia.setId(id);
				noticia.setTitulo(titulo);
				noticia.setAssunto(assunto);
				noticia.setConteudo(conteudo);
				noticia.setDataCriacao(dataCriacao);

				Document documentAutor = (Document) document.get("autor");
				String idAutor = documentAutor.getString("_id");
				String nomeAutor = documentAutor.getString("nome");
				String emailAutor = documentAutor.getString("email");
				Date dataNascimentoDate = documentAutor.getDate("dataNascimento");
				LocalDate dataNascimento = LocalDate.ofInstant(dataNascimentoDate.toInstant(), ZoneId.systemDefault());

				Autor autor = new Autor();
				autor.setId(idAutor);
				autor.setNome(nomeAutor);
				autor.setEmail(emailAutor);
				autor.setDataNascimento(dataNascimento);
				noticia.setAutor(autor);

				noticias.add(noticia);
			}
		}
		return noticias;
    }

}
