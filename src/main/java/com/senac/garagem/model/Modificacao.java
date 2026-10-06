package com.senac.garagem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Modificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da modificação é obrigatório")
    private String nome;

    @NotNull(message = "A categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    private CategoriaModificacao categoria;

    @PositiveOrZero(message = "O preço não pode ser negativo")
    private Double precoReais;

    @Min(value = 0, message = "O ganho de potência não pode ser negativo")
    private Integer ganhoCv;

    @JsonIgnore
    @ManyToMany(mappedBy = "modificacoes")
    private Set<Carro> carros = new HashSet<>();
}
