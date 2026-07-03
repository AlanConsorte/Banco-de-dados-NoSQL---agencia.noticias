package br.edu.utfpr.td.tsi.agencia.noticias.servico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.edu.utfpr.td.tsi.agencia.noticias.modelo.Noticia;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class SolrService {

    @Value("${solr.url}")
    private String solrUrl;

    private SolrClient solrClient;

    @PostConstruct
    public void init() {
        // Inicializa o cliente HttpSolrClient do Solr 8
        this.solrClient = new HttpSolrClient.Builder(solrUrl).build();
        System.out.println("SolrClient inicializado com sucesso para a URL: " + solrUrl);
    }

    @PreDestroy
    public void close() {
        if (this.solrClient != null) {
            try {
                this.solrClient.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar SolrClient: " + e.getMessage());
            }
        }
    }

    /**
     * Indexa ou atualiza uma notícia no Apache Solr.
     */
    public void indexarNoticia(Noticia noticia) {
        if (noticia == null || noticia.getId() == null) {
            return;
        }

        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", noticia.getId());
            doc.addField("titulo", noticia.getTitulo());
            doc.addField("conteudo", noticia.getConteudo());
            
            solrClient.add(doc);
            solrClient.commit();
            System.out.println("Notícia indexada com sucesso no Solr: ID " + noticia.getId());
        } catch (SolrServerException | IOException e) {
            System.err.println("Erro ao indexar notícia no Solr: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Remove uma notícia do índice do Apache Solr pelo seu ID.
     */
    public void deletarNoticia(String id) {
        if (id == null || id.isEmpty()) {
            return;
        }

        try {
            solrClient.deleteById(id);
            solrClient.commit();
            System.out.println("Notícia removida do Solr: ID " + id);
        } catch (SolrServerException | IOException e) {
            System.err.println("Erro ao remover notícia do Solr: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca no Solr os IDs das notícias que contêm o termo de pesquisa no conteúdo ou título.
     * Retorna uma lista de IDs (valores de referência).
     */
    public List<String> buscarIdsPorTermo(String termo) {
        List<String> ids = new ArrayList<>();
        if (termo == null || termo.trim().isEmpty()) {
            return ids;
        }

        try {
            SolrQuery query = new SolrQuery();
            // Permite busca exata e também parcial por curingas no conteúdo ou no título
            String queryStr = String.format("conteudo:(*%1$s*) OR titulo:(*%1$s*)", termo.trim());
            query.setQuery(queryStr);
            query.setRows(1000); // Limite de resultados

            QueryResponse response = solrClient.query(query);
            SolrDocumentList results = response.getResults();

            for (SolrDocument doc : results) {
                Object idObj = doc.getFieldValue("id");
                if (idObj != null) {
                    ids.add(idObj.toString());
                }
            }
            System.out.println("Busca Solr por '" + termo + "' retornou " + ids.size() + " IDs.");
        } catch (SolrServerException | IOException e) {
            System.err.println("Erro ao buscar notícias no Solr: " + e.getMessage());
            e.printStackTrace();
        }

        return ids;
    }
}
