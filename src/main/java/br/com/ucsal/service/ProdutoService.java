package br.com.ucsal.service;

import br.com.ucsal.model.Produto;

import java.util.List;

public interface ProdutoService {
    void adicionarProduto(String nome, double preco);

    void removerProduto(Integer id);

    Produto obterProdutoPorId(Integer id);

    void atualizarProduto(Produto p);

    List<Produto> listarProdutos();
}
