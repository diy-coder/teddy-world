package br.com.teddy.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AttrResponseDTO {
    protected Long id;
    protected LocalDateTime createdAt;
    protected LocalDateTime deletedAt;
}
