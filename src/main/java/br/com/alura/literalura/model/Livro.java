package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;

    @Column(name = "languages")
    private String idioma;

    public Livro() {
        // Construtor padrão obrigatório pelo JPA
    }

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();

        if (dadosLivro.autores() != null && !dadosLivro.autores().isEmpty()) {
            var dadosAutor = dadosLivro.autores().get(0);

            Autor autor = new Autor();
            autor.setNome(dadosAutor.nome());
            autor.setBirthYear(dadosAutor.birthYear());
            autor.setDeathYear(dadosAutor.deathYear());

            this.autor = autor;
        }
        this.idioma = dadosLivro.idioma();

    }

    // Getters e setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "\nLivro: " + titulo + "Lingua nativa:" + idioma +
                (autor != null ? " | Autor: " + autor.getNome() : " | Autor: não informado");
    }
}
