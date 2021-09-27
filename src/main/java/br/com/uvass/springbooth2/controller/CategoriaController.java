package br.com.uvass.springbooth2.controller;

import br.com.uvass.springbooth2.model.entity.Categoria;
import br.com.uvass.springbooth2.model.entity.Produto;
import br.com.uvass.springbooth2.model.repository.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private CategoriaRepository categoriaRepository;

    public CategoriaController(JdbcTemplate jdbcTemplate) {
        categoriaRepository = new CategoriaRepository(jdbcTemplate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Categoria inserir(@RequestBody Categoria categoria) throws Exception {
        return categoriaRepository.adicionar(categoria);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ArrayList<Categoria> buscarPorId(@PathVariable Integer id) throws Exception {
        return categoriaRepository.buscarPorId(id);
    }

    @GetMapping("/{id}/produto")
    public List<Produto> buscarProdutosCategoria(
            @PathVariable int id,
            @RequestParam(required = false) String nome, Float valorMinimo, Float valorMaximo
    ) {
        return categoriaRepository.buscarProdutosPorCategoria(id, nome, valorMinimo, valorMaximo);
    }
}
