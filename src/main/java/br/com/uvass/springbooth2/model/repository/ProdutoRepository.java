package br.com.uvass.springbooth2.model.repository;

import br.com.uvass.springbooth2.model.entity.Categoria;
import br.com.uvass.springbooth2.model.entity.Produto;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;


public class ProdutoRepository {

    private JdbcTemplate jdbcTemplate;

    public ProdutoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public ArrayList<Produto> buscarPorId(Integer id) throws Exception {
        String sql = "SELECT * FROM produto WHERE id = ?";
        ArrayList<Produto> buscar = (ArrayList<Produto>) jdbcTemplate.query(sql, new ProdutoMapper(), id);

        if (buscar.size() > 0) {
            return (ArrayList<Produto>) jdbcTemplate.query(sql, new Object[]{id}, new ProdutoMapper());
        }
        throw new Exception("Não encontramos nenhum produto com este ID.");
    }


    public ArrayList<Produto> buscarPeloFiltro(String nome, Float valorMinimo, Float valorMaximo) throws Exception {
        String sql = "SELECT * FROM PRODUTO WHERE nome = ? AND valorUnitario >= ? AND valorUnitario <= ?";
        ArrayList<Produto> buscar = (ArrayList<Produto>) jdbcTemplate.query(sql, new Object[]{nome, valorMinimo, valorMaximo}, new ProdutoMapper());

        if(buscar.size() > 0) {
            return (ArrayList<Produto>) jdbcTemplate.query(sql, new Object[]{nome, valorMinimo, valorMaximo}, new ProdutoMapper());
        }

        throw new Exception("Não encontramos este produto");

    }

    public Produto adicionar(Produto produto) throws Exception {
        String sql = "insert into produto(id, nome, descricao, fotoUrl, dataCadastro, dataUltimaAtualizacao, valorUnitario) values (?, ?, ?, ?, ?, ?, ?)";
        int insert = jdbcTemplate.update(sql,
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getFotoUrl(),
                produto.getDataCadastro(),
                produto.getDataUltimaAtualizacao(),
                produto.getValorUnitario()
        );

        if(insert == 1) {
            for(Categoria categoria : produto.getCategoriaList()) {
                jdbcTemplate.update("INSERT INTO produtoCategoria(id, produtoId, categoriaId) VALUES(?, ?, ?)",
                        4, produto.getId(), categoria.getId()
                );
            }

            List<Categoria> categoriaList = jdbcTemplate.query("SELECT c.* FROM categoria c, produto p, produtoCategoria pc WHERE p.id = pc.produtoId AND c.id = pc.categoriaId AND produtoId = ?",
                    new CategoriaMapper(), produto.getId()
            );

            produto.setCategoriaList(categoriaList);

            return produto;
        }

        throw new Exception("Houve um Erro ao inserir o produto");
    }

}
