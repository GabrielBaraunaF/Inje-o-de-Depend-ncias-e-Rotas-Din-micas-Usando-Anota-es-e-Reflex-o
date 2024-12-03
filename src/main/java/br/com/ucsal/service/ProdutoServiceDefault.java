package br.com.ucsal.service;

import java.util.List;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Named;
import br.com.ucsal.model.Produto;
import br.com.ucsal.persistencia.ProdutoRepository;


@Named("default")
public class ProdutoServiceDefault implements ProdutoService {


    @Inject("hsql")
    private ProdutoRepository<Produto, Integer> produtoRepository;

    public ProdutoServiceDefault() {

    }

    public ProdutoServiceDefault(ProdutoRepository<Produto, Integer> produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void adicionarProduto(String nome, double preco) {
        Produto produto = new Produto(null, nome, preco);
        produtoRepository.adicionar(produto);
    }

    @Override
    public void removerProduto(Integer id) {
        produtoRepository.remover(id);
    }

    @Override
    public Produto obterProdutoPorId(Integer id) {
        return produtoRepository.obterPorID(id);
    }

    @Override
    public void atualizarProduto(Produto p) {
        produtoRepository.atualizar(p);
    }

    @Override
    public List<Produto> listarProdutos() {
        return produtoRepository.listar();
    }
}

