package br.com.uvass.springbooth2.model.repository;

import br.com.uvass.springbooth2.model.entity.Categoria;
import br.com.uvass.springbooth2.model.entity.Produto;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {

    private JdbcTemplate jdbcTemplate;

    public CategoriaRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }


    public ArrayList<Categoria> buscarListaCategoria(Integer id, String imagemSimboloUrl, String nome, String descricao) throws Exception {
        String sql = "SELECT * FROM categoria";
        ArrayList<Categoria> buscar = (ArrayList<Categoria>) jdbcTemplate.query(sql, new Object[]{id, imagemSimboloUrl, nome, descricao}, new CategoriaMapper());

        if(buscar.size() > 0) {
            return (ArrayList<Categoria>) jdbcTemplate.query(sql, new Object[]{id, imagemSimboloUrl, nome, descricao}, new CategoriaMapper());
        }

        throw new Exception("A Categoria não foi encontrada");

    }

    public Categoria adicionar(Categoria categoria) throws Exception {
        String sql = "insert into categoria(id, imagemSimboloUrl, nome, descricao) values (?, ?, ?, ?)";
        int insert = jdbcTemplate.update(sql,
                categoria.getId(),
                categoria.getImagemSimboloUrl(),
                categoria.getNome(),
                categoria.getDescricao()
        );

        if(insert == 1) {
            return categoria;
        }

        throw new Exception("Houve um Erro ao inserir a categoria");
    }

    public ArrayList<Categoria> buscarPorId(Integer id) throws Exception {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        ArrayList<Categoria> buscar = (ArrayList<Categoria>) jdbcTemplate.query(sql, new CategoriaMapper(), id);

        if (buscar.size() > 0) {
            return (ArrayList<Categoria>) jdbcTemplate.query(sql, new Object[]{id}, new CategoriaMapper());
        }
        throw new Exception("Não encontramos nenhuma categoria com este ID.");
    }

    public List<Produto> buscarProdutosCategoria(Integer id, String nome, Float valorMinimo, Float valorMaximo) {
        if (nome == null && valorMinimo == null && valorMaximo == null) {
            return jdbcTemplate.query(
                    "select p.*, pc.categoriaId FROM produto p INNER JOIN produtoCategoria pc ON pc.produtoId = p.id WHERE pc.categoriaId = ?",
                    new ProdutoMapper(),
                    id
            );
        }
        if (nome != null && valorMinimo == null && valorMaximo == null) {
            return jdbcTemplate.query(
                    "select p.* FROM produto p INNER JOIN produtoCategoria pc ON pc.produtoId = p.id WHERE pc.categoriaId = ? AND p.nome = ?",
                    new ProdutoMapper(),
                    id,
                    nome
            );
        }
        return jdbcTemplate.query(
                "select p.*, pc.categoriaId FROM produto p INNER JOIN produtoCategoria pc ON pc.produtoId = p.id WHERE pc.categoriaId = ? AND p.nome = ? AND p.valorUnitario >= ? AND p.valorUnitario <= ?",
                new ProdutoMapper(),
                id,
                nome,
                valorMinimo,
                valorMaximo
        );
    }

}

