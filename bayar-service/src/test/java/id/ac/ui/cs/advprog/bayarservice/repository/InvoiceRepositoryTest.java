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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InvoiceRepositoryTest {

    @MockBean
    private BillRepository billRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    UUID uuid = UUID.randomUUID();

    Invoice invoice;

    @BeforeEach
    void setUp() {
        invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.UNPAID)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(uuid)
                .build();
        invoiceRepository.save(invoice);

        Bill bill = Bill.builder()
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
        invoiceRepository.deleteAll();
        billRepository.deleteAll();
    }

    @Test
    void testFindById() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoice.getId());
        Assertions.assertTrue(optionalInvoice.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(100);

        Assertions.assertFalse(optionalInvoice.isPresent());
    }

    @Test
    void testFindAll() {
        List<Invoice> optionalInvoiceList = invoiceRepository.findAll();

        Assertions.assertNotNull(optionalInvoiceList);
    }

    @Test
    void testFindBySessionId() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findBySessionId(invoice.getSessionId());
        Assertions.assertTrue(optionalInvoice.isPresent());
    }

    @Test
    void testFindBySessionIdNotFound() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findBySessionId(UUID.randomUUID());

        Assertions.assertFalse(optionalInvoice.isPresent());
    }
}
