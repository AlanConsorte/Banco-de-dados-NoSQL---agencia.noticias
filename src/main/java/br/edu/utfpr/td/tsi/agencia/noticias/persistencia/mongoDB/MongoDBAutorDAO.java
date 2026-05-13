package br.edu.utfpr.td.tsi.agencia.noticias.persistencia.mongoDB;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.time.LocalDate;

import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Autor;
import br.edu.utfpr.td.tsi.agencia.noticias.persistencia.AutorDAO;

@Component
public class MongoDBAutorDAO implements AutorDAO {

    private String connectionString = "mongodb://localhost:27017";

	private String databaseName = "2159929-Noticias-Web";

    @Override
    public void cadastrarAutor(Autor autor) {
        if (autor.getId() == null) {
			autor.setId(UUID.randomUUID().toString());
		}

		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("autores");

			Document document = new Document();
			document.append("_id", autor.getId());
			document.append("nome", autor.getNome());
			document.append("email", autor.getEmail());
			document.append("dataNascimento",
					Date.from(autor.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			collection.insertOne(document);
		}
    }

    @Override
    public Autor buscarAutor(String id) {
        if (id == null || id.isEmpty()) {
			return null;
		}

		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("autores");
			Document document = collection.find(new Document("_id", id)).first();
			if (document != null) {
				Autor autor = new Autor();
				autor.setId(document.getString("_id"));
				autor.setNome(document.getString("nome"));
				autor.setEmail(document.getString("email"));
				Date dataNascimentoDate = document.getDate("dataNascimento");
				if (dataNascimentoDate != null) {
					autor.setDataNascimento(
							LocalDate.ofInstant(dataNascimentoDate.toInstant(), ZoneId.systemDefault()));
				}
				return autor;
			}
		}

		return null;
    }

    @Override
    public void removerAutor(String id) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("autores");

			collection.deleteOne(new Document("_id", id));
		}
    }

    @Override
    public void atualizarAutor(Autor autor) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("autores");

			Document document = new Document();
			document.append("_id", autor.getId());
			document.append("nome", autor.getNome());
			document.append("email", autor.getEmail());
			document.append("dataNascimento",
					Date.from(autor.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			collection.replaceOne(new Document("_id", autor.getId()), document);
		}
    }

    @Override
    public List<Autor> listarTodosAutor() {
        ArrayList<Autor> autores = new ArrayList<>();

		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			MongoDatabase database = mongoClient.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection("autores");

			FindIterable<Document> documentosEncontrados = collection.find();
			for (Document document : documentosEncontrados) {
				Autor autor = new Autor();
				autor.setId(document.getString("_id"));
				autor.setNome(document.getString("nome"));
				autor.setEmail(document.getString("email"));
				Date dataNascimentoDate = document.getDate("dataNascimento");
				if (dataNascimentoDate != null) {
					autor.setDataNascimento(
							LocalDate.ofInstant(dataNascimentoDate.toInstant(), ZoneId.systemDefault()));
				}
				autores.add(autor);
			}
		}

		return autores;
    }   



}
