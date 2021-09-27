package br.com.uvass.springbooth2.controller;

import br.com.uvass.springbooth2.model.entity.Produto;
import br.com.uvass.springbooth2.model.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private ProdutoRepository produtoRepository;

    public ProdutoController(JdbcTemplate jdbcTemplate) {
        produtoRepository = new ProdutoRepository(jdbcTemplate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Produto inserir(@RequestBody Produto produto) throws Exception {
        return produtoRepository.adicionar(produto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ArrayList<Produto> buscarProduto(@RequestParam String nome, @RequestParam Float valorMinimo, @RequestParam Float valorMaximo) throws Exception {
        return  produtoRepository.buscarProdutoPorFiltro(nome, valorMinimo, valorMaximo);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ArrayList<Produto> buscarPorId(@PathVariable Integer id) throws Exception {
        return produtoRepository.buscarPorId(id);
    }
}
