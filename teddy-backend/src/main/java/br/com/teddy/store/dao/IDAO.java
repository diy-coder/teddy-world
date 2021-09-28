package br.com.teddy.store.dao;

import br.com.teddy.store.domain.DomainEntity;

import java.util.List;

public interface IDAO {
     DomainEntity create(DomainEntity domainEntity);
     DomainEntity delete(Long id);
     DomainEntity update(DomainEntity domainEntity);
     List<DomainEntity> list(DomainEntity domainEntity);
     DomainEntity get(Long id);
}
