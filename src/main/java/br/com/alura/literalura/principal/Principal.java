package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;

import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";

    private DadosLivro getInfoLivro(){
        System.out.println("Informe o nome do livro que deseja buscar: ");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "+"));
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        return dados;
    }


    public void exibeMenu() {
        DadosLivro dados = getInfoLivro();
        System.out.println("Título encontrado: " + dados.titulo());
    }
}
