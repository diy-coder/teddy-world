package br.com.teddy.store.dto;

import br.com.teddy.store.domain.Customer;
import br.com.teddy.store.domain.DomainEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private String fullName;
    private String email;
    private boolean hasError;
    private String message;

    public ResponseDTO(DomainEntity domainEntity) {
        this.id = domainEntity.getId();
        this.createdAt = domainEntity.getCreatedAt();
        this.deletedAt = domainEntity.getDeletedAt();

        if(domainEntity instanceof Customer) {
            Customer customer = (Customer) domainEntity;
            this.email = customer.getEmail();
            this.fullName = customer.getFullName();
        }
    }


}