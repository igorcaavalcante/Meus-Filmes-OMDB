package com.igor.omdbfilmes;

public class Filme {
    private String id;
    private String titulo;
    private String diretor;
    private String atores;
    private String genero;
    private String capa;
    private String produtora;
    private String duracao;
    private String autor;
    private String ano;
    private String classificacao;

    public Filme(String id, String titulo, String diretor, String atores, String genero, String capa, String produtora, String duracao, String autor, String ano, String classificacao) {
        this.id = id;
        this.titulo = titulo;
        this.diretor = diretor;
        this.atores = atores;
        this.genero = genero;
        this.capa = capa;
        this.produtora = produtora;
        this.duracao = duracao;
        this.autor = autor;
        this.ano = ano;
        this.classificacao = classificacao;
    }

    public Filme(){

    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDiretor() {
        return diretor;
    }

    public String getAtores() {
        return atores;
    }

    public String getGenero() {
        return genero;
    }

    public String getCapa() {
        return capa;
    }

    public String getProdutora() {
        return produtora;
    }

    public String getDuracao() {
        return duracao;
    }

    public String getAutor() {
        return autor;
    }

    public String getAno() {
        return ano;
    }

    public String getClassificacao() {
        return classificacao;
    }
}
