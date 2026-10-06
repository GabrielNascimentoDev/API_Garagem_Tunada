package com.senac.garagem.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O modelo é obrigatório")
    @Size(max = 100)
    private String modelo;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1900, message = "Ano inválido")
    @Max(value = 2100, message = "Ano inválido")
    private Integer ano;

    @Size(max = 40)
    private String cor;

    @NotNull(message = "A tração é obrigatória")
    @Enumerated(EnumType.STRING)
    private Tracao tracao;

    @Min(value = 1, message = "A potência original deve ser maior que zero")
    private Integer potenciaOriginalCv;

    @Size(max = 500)
    private String fotoUrl;

    @NotNull(message = "O fabricante é obrigatório")
    @ManyToOne
    @JoinColumn(name = "fabricante_id")
    private Fabricante fabricante;

    @NotNull(message = "O proprietário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @OneToOne
    @JoinColumn(name = "ficha_tecnica_id", unique = true)
    private FichaTecnica fichaTecnica;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "carro_modificacao",
            joinColumns = @JoinColumn(name = "carro_id"),
            inverseJoinColumns = @JoinColumn(name = "modificacao_id"))
    private Set<Modificacao> modificacoes = new HashSet<>();
}