package br.com.teddy.store.controller.generics;

import br.com.teddy.store.dao.IDAO;
import br.com.teddy.store.domain.DomainEntity;
import br.com.teddy.store.domain.EnumException;
import br.com.teddy.store.domain.EnumOperation;
import br.com.teddy.store.dto.AttrResponseDTO;
import br.com.teddy.store.dto.FactoryResponseDTO;
import br.com.teddy.store.exception.ValidationException;
import br.com.teddy.store.strategy.IStrategy;
import br.com.teddy.store.strategy.StrategyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class RestBasicController<T extends DomainEntity, I extends AttrResponseDTO> {

    final IDAO basicDAO;
    private StrategyFactory strategyFactory;

    public RestBasicController(IDAO basicDAO, StrategyFactory strategyFactory) {
        this.basicDAO = basicDAO;
        this.strategyFactory = strategyFactory;
    }

    /**
     * @param page responsible for changing the default paging behaviour
     * @return pageable list of results
     */
    @GetMapping(value = {"full"})
    public Page<T> getAllPaged(@PageableDefault(size = 50) Pageable page) {
        return Page.empty();
    }

    /**
     * @return Return a list of all DTOs
     */
    @GetMapping(value = {"", "all"})
    public List<I> getAll() {
        // Remover este tipo de tratamento, tornar mais generico
        FactoryResponseDTO.hasError = false;
        List<T> all = (List<T>) this.basicDAO.list(null);
        List<I> collect = all.stream().map(item -> (I) FactoryResponseDTO.createDTO(item, "GET")).collect(Collectors.toList());
        return collect;
    }

    @GetMapping(value = "{id}")
    public I getById(@PathVariable("id") Long id) {
        T domain = (T) basicDAO.get(id);
        return (I) FactoryResponseDTO.createDTO(domain, "GET");
    }

    /**
     * create a resource
     *
     * @param t -> generic type to be persisted
     * @return Response entity containing the resource location and the resource it self
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<I> save(@RequestBody T t) {
        FactoryResponseDTO.hasError = false;
        FactoryResponseDTO.message = "";

        List<IStrategy> strategyList = pegaRegrasAplicaveisAoCadadstro(t, EnumOperation.CREATE);
        StringBuilder stringBuilder = this.applicarRegrasAoRegistro(t, strategyList);

        URI uri = null;
        if (stringBuilder.length() == 0) {
            DomainEntity saved = basicDAO.create(t);
            uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(saved.getId()).toUri();
        } else {
            FactoryResponseDTO.hasError = true;
            FactoryResponseDTO.message = stringBuilder.toString();

            AttrResponseDTO create = FactoryResponseDTO.createDTO(t, "CREATE");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((I) create);
        }

        AttrResponseDTO create = FactoryResponseDTO.createDTO(t, "CREATE");
        return ResponseEntity.created(uri).body((I) create);

    }

    /**
     * update a resource
     *
     * @param t -> generic type to be persisted
     * @return Response entity containing the resource location and the resource it self
     */
    @PutMapping(consumes = "application/json")
    public ResponseEntity<I> update(@RequestBody T t) {
        FactoryResponseDTO.hasError = false;
        FactoryResponseDTO.message = "";

        List<IStrategy> strategyList = pegaRegrasAplicaveisAoCadadstro(t, EnumOperation.UPDATE);
        StringBuilder stringBuilder = this.applicarRegrasAoRegistro(t, strategyList);

        if (stringBuilder.length() > 0) {
            FactoryResponseDTO.hasError = (true);
            FactoryResponseDTO.message = stringBuilder.toString();

            I response = (I) FactoryResponseDTO.createDTO(t, "UPDATE");

            return new ResponseEntity<I>(response, HttpStatus.BAD_REQUEST);
        }

        DomainEntity domainEntity = this.basicDAO.get(t.getId());
        if (null == domainEntity) {
            throw new ValidationException(EnumException.ITEM_NAO_ENCONTRADO);
        }

        T entity = (T) basicDAO.update(t);

        I update = (I) FactoryResponseDTO.createDTO(entity, "UPDATE");

        return ResponseEntity.ok(update);

    }

    private List<IStrategy> pegaRegrasAplicaveisAoCadadstro(@RequestBody T t, EnumOperation operation) {
        Class type = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return this.strategyFactory.getRules(type, operation);
    }

    /**
     * delete a resource searching by its ID
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<T> delete(@PathVariable Long id) {
        T t = (T) basicDAO.get(id);
        basicDAO.delete(t.getId());
        return ResponseEntity.noContent().build();
    }

    private StringBuilder applicarRegrasAoRegistro(DomainEntity domainEntity, List<IStrategy> bnsRules) {
        StringBuilder stringBuilder = new StringBuilder();
        for (IStrategy bnsRule : bnsRules) {
            String msg = bnsRule.applyBusinessRule(domainEntity);
            if (msg != null) {
                stringBuilder.append(msg);
            }
        }
        return stringBuilder;
    }
}
