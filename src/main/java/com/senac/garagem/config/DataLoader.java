package com.senac.garagem.config;

import com.senac.garagem.model.*;
import com.senac.garagem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final FabricanteRepository fabricantes;
    private final ProprietarioRepository proprietarios;
    private final FichaTecnicaRepository fichas;
    private final ModificacaoRepository modificacoes;
    private final CarroRepository carros;

    public DataLoader(FabricanteRepository fabricantes, ProprietarioRepository proprietarios,
                      FichaTecnicaRepository fichas, ModificacaoRepository modificacoes,
                      CarroRepository carros) {
        this.fabricantes = fabricantes;
        this.proprietarios = proprietarios;
        this.fichas = fichas;
        this.modificacoes = modificacoes;
        this.carros = carros;
    }

    @Override
    public void run(String... args) {
        if (fabricantes.count() > 0) return;

        Fabricante nissan = fabricante("Nissan", "Japão");
        Fabricante toyota = fabricante("Toyota", "Japão");
        fabricante("Volkswagen", "Alemanha");

        Proprietario rafa = proprietario("Rafael Souza", "Rafa Turbo", "rafa@garagem.com");
        Proprietario cami = proprietario("Camila Torres", "Cami Drift", "cami@garagem.com");

        Modificacao turbina = modificacao("Turbina Garrett GTX3582", CategoriaModificacao.TURBO, 12500.0, 180);
        Modificacao coilover = modificacao("Coilover ajustável", CategoriaModificacao.SUSPENSAO, 6800.0, 0);
        Modificacao escape = modificacao("Escape inox 3 polegadas", CategoriaModificacao.ESCAPAMENTO, 2400.0, 25);
        Modificacao freios = modificacao("Kit de freio Brembo", CategoriaModificacao.FREIOS, 15000.0, 0);
        Modificacao rodas = modificacao("Rodas forjadas aro 18", CategoriaModificacao.RODAS, 9000.0, 0);

        FichaTecnica fichaR34 = ficha(650, 78.5, 3.4, 310);
        FichaTecnica fichaSupra = ficha(520, 65.0, 3.9, 290);

        carro("Skyline GT-R R34", 1999, "Azul Bayside", Tracao.INTEGRAL, 280, nissan, rafa, fichaR34,
                List.of(turbina, coilover, escape, freios));
        carro("Supra A80", 1997, "Laranja", Tracao.TRASEIRA, 330, toyota, cami, fichaSupra,
                List.of(turbina, escape, rodas));
    }

    private Fabricante fabricante(String nome, String pais) {
        Fabricante f = new Fabricante();
        f.setNome(nome);
        f.setPaisOrigem(pais);
        return fabricantes.save(f);
    }

    private Proprietario proprietario(String nome, String apelido, String email) {
        Proprietario p = new Proprietario();
        p.setNome(nome);
        p.setApelido(apelido);
        p.setEmail(email);
        return proprietarios.save(p);
    }

    private Modificacao modificacao(String nome, CategoriaModificacao categoria, Double preco, Integer ganho) {
        Modificacao m = new Modificacao();
        m.setNome(nome);
        m.setCategoria(categoria);
        m.setPrecoReais(preco);
        m.setGanhoCv(ganho);
        return modificacoes.save(m);
    }

    private FichaTecnica ficha(Integer cv, Double torque, Double zeroACem, Integer vmax) {
        FichaTecnica f = new FichaTecnica();
        f.setPotenciaCv(cv);
        f.setTorqueKgfm(torque);
        f.setZeroACemSegundos(zeroACem);
        f.setVelocidadeMaximaKmh(vmax);
        return fichas.save(f);
    }

    private void carro(String modelo, int ano, String cor, Tracao tracao, int potenciaOriginal,
                       Fabricante fabricante, Proprietario dono, FichaTecnica ficha, List<Modificacao> mods) {
        Carro c = new Carro();
        c.setModelo(modelo);
        c.setAno(ano);
        c.setCor(cor);
        c.setTracao(tracao);
        c.setPotenciaOriginalCv(potenciaOriginal);
        c.setFotoUrl("/img/" + modelo.toLowerCase().replaceAll("[^a-z0-9]+", "-") + ".jpg");
        c.setFabricante(fabricante);
        c.setProprietario(dono);
        c.setFichaTecnica(ficha);
        c.setModificacoes(new HashSet<>(mods));
        carros.save(c);
    }
}