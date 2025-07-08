package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.model.ResultadoBusca;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.*;

@Component
public class Principal implements CommandLineRunner {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private List<Livro> livros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();


    @Override
    public void run(String... args) throws Exception {
        exibeMenu();
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    ----------LIVROS DOMÍNIO PÚBLICO----------

                    1 - buscar livro pelo título
                    2 - listar livros registrados
                    3 - listar autores registrados
                    4 - listar autores estivaram/estão vivos em um determinado periodo
                    5 - listar livros em um determinado idioma
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroWeb();
                    break;
                case 2:
                    listarLivrosBuscados();
                    break;
                case 3:
                    listarAutoresCadastrados();
                    break;
                case 4:
                    buscaAutoresFiltro();
                    break;
                case 5:
                    buscarLivroPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        }
    }

    private DadosLivro getInfoLivro() {
        System.out.println("Informe o nome do livro que deseja buscar: ");
        var nomeLivro = leitura.nextLine().trim().toLowerCase();

        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "+"));
        ResultadoBusca resultado = conversor.obterDados(json, ResultadoBusca.class);
        List<DadosLivro> livros = resultado.livros();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse nome.");
            return null;
        }

        return livros.stream()
                .filter(l -> l.titulo().toLowerCase().startsWith(nomeLivro))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Nenhum livro encontrado que comece com: " + nomeLivro);
                    return null;
                });
    }

    private void buscarLivroWeb() {
        DadosLivro dadosLivro = getInfoLivro();
        if (dadosLivro != null) {
            Livro livro = new Livro(dadosLivro);
            livroRepository.save(livro);
            System.out.println(dadosLivro);
        }
    }

    private void listarLivrosBuscados(){
        livros = livroRepository.findAll();
        System.out.println("Livros Cadastrados no Banco:");
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutoresCadastrados(){
        autores = autorRepository.findAll();
        System.out.println("Autores Cadastrados no Banco:");
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(System.out::println);
    }

    private void buscaAutoresFiltro(){

            System.out.println("Inicio do Periodo (ano)? ");
            var anoInicio = leitura.nextInt();
            leitura.nextLine();
            System.out.println("Fim do Periodo (ano)? ");
            var anoFim = leitura.nextInt();
            leitura.nextLine();
            List<Autor> autoresEncontrados = autorRepository.autoresVivosEntre(anoInicio, anoFim);
            System.out.println("Autores vivos entre " + anoInicio + " e " + anoFim);
            autoresEncontrados.forEach(a ->
                    System.out.println("Autor(a): " + a.getNome() +
                            " | Nascimento Autor(a): " + a.getBirthYear() +
                            " | Falecimento Autor(a): " + a.getDeathYear()));

    }

    private void buscarLivroPorIdioma() {
        System.out.println("Informe o idioma que deseja buscar pela sigla: ");
        var idioma = leitura.nextLine();

        List<Livro> livros = livroRepository.findByIdiomaContaining(idioma);

        if (livros.isEmpty()) {
            System.out.println("Livros nesse idioma não encontrados :/");
        } else {
            System.out.println("Livros encontrados:");
            livros.forEach(System.out::println);
        }
    }
}
