package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BillRepositoryTest {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    UUID uuid = UUID.randomUUID();
    Bill bill;

    @BeforeEach
    void setUp() {
        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.UNPAID)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(uuid)
                .build();
        invoiceRepository.save(invoice);

        bill = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .invoice(invoice)
                .build();
        billRepository.save(bill);
    }
    @AfterEach
    void tearDown() {
        billRepository.deleteAll();
        invoiceRepository.deleteAll();
    }

    @Test
    void testFindById() {
        Optional<Bill> optionalBill = billRepository.findById(bill.getId());

        Assertions.assertTrue(optionalBill.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Bill> optionalBill = billRepository.findById(100);

        Assertions.assertFalse(optionalBill.isPresent());
    }
}
