package br.com.teddy.store.controller;

import br.com.teddy.store.domain.Customer;
import br.com.teddy.store.domain.Gender;
import br.com.teddy.store.dto.customer.CustomerDTO;
import br.com.teddy.store.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class CustomerControllerTest {

    private final CustomerController controller;

    @Autowired
    public CustomerControllerTest(CustomerController controller) {
        this.controller = controller;
    }

    @BeforeAll
    void setup() {
        for (int i = 0; i < 20; i++) {
            this.controller.save(getCustomer());
        }
    }

    @AfterAll
    void rollback() {
        this.controller.getAll()
                .forEach(item -> this.controller.delete(item.getId()));
    }

    @Test
    @Order(1)
    void getAllPaged() {
        Page<Customer> all = this.controller.getAllPaged(PageRequest.of(0, 20));
        // Este método ainda nao foi implementado
        assertEquals(0, all.stream().count());
    }

    @Test
    @Order(2)
    void getAll() {
        List<CustomerDTO> all = this.controller.getAll();
        assertEquals(20, all.size());
    }

    @Test
    @Order(3)
    void getById() {
        CustomerDTO byId = this.controller.getById(1L);
        assertNotNull(byId);
    }

    @Test
    @Order(4)
    void save() {
        this.controller.save(getCustomer());
        List<CustomerDTO> all = this.controller.getAll();
        assertEquals(21, all.size());
    }

    @Test
    @Order(5)
    void delete() {
        CustomerDTO primeiro = this.controller.getAll().get(0);
        controller.delete(primeiro.getId());
        CustomerDTO byId = controller.getById(primeiro.getId());
        assertNotNull(byId);
    }

    @Test
    @Order(6)
    void testSaveWithWrongPassword() {
        Customer customer = getCustomer();
        customer.setPassword("@Senhh12");
        ResponseEntity<CustomerDTO> save = this.controller.save(customer);
        assertEquals(HttpStatus.BAD_REQUEST, save.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdate() {
        Customer customer = getCustomer();
        customer.setId(1L);
        customer.setEmail("novo-email@hotmail.com");
        ResponseEntity save = this.controller.update(customer);
        assertEquals(HttpStatus.OK, save.getStatusCode());
        assertEquals("novo-email@hotmail.com", ((CustomerDTO) save.getBody()).getEmail());
    }

    @Test
    @Order(8)
    void testUpdatePassword() {
        Customer customer = getCustomer();
        customer.setId(1L);
        customer.setNewPassword("@Senhh11");
        customer.setPasswordConfirm("@Senhh11");
        ResponseEntity save = this.controller.updateCustomerPassword(customer);
        assertEquals(HttpStatus.OK, save.getStatusCode());
    }

    @Test
    @Order(9)
    void testUpdateWithWrongPasswordFormatMustFail() {
        Customer customer = getCustomer();
        customer.setNewPassword("@");
        customer.setPasswordConfirm("123");
        ResponseEntity save = this.controller.update(customer);
        assertEquals(HttpStatus.BAD_REQUEST, save.getStatusCode());
    }

    @Test
    @Order(10)
    void testUpdateWithoutIdentifierMustFail() {
        Customer customer = getCustomer();
        ResponseEntity save = this.controller.update(customer);
        assertEquals(HttpStatus.BAD_REQUEST, save.getStatusCode());
    }

    @Test
    @Order(11)
    void testUpdateInvalidIdentifierMustFail() {
        Customer customer = getCustomer();
        customer.setId(9999L);
        assertThrows(ValidationException.class, () ->this.controller.update(customer));
    }

    private Customer getCustomer() {
        Customer customer = new Customer("DIY Coder", "diy-coder@outlook.com", "00000000000", Gender.MALE, LocalDate.now(),
                "*55 11 123 456 789", "@Senha12", "@Senha12", "@Senha12", null, null);

        return customer;
    }
}