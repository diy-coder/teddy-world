package br.com.teddy.store.controller;

import br.com.teddy.store.controller.generics.RestBasicController;
import br.com.teddy.store.dao.customer.CustomerDAO;
import br.com.teddy.store.domain.Customer;
import br.com.teddy.store.dto.customer.CustomerDTO;
import br.com.teddy.store.facade.Facade;
import br.com.teddy.store.strategy.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("customer")
public class CustomerController extends RestBasicController<Customer, CustomerDTO> {

    private final Facade facade;

    @Autowired
    public CustomerController(CustomerDAO basicService, Facade facade, StrategyFactory strategyFactory) {
        super(basicService, strategyFactory);
        this.facade = facade;
    }

    @PatchMapping("/customer")
    public ResponseEntity updateCustomerPassword(@RequestBody Customer customer) {
        return ResponseEntity.ok(facade.updatePassword(customer));
    }

}

