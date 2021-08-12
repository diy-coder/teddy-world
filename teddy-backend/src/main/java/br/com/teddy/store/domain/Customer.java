package br.com.teddy.store.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "_customer")
public class Customer extends DomainEntity{
    @NotNull(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode estar em branco")
    private String fullName;

    @Email(message = "Insira um e-mail válido")
//    @Column(unique = true)
    private String email;

    private String password;
    private String cpf;

    @Transient
    private String passwordConfirm;
}