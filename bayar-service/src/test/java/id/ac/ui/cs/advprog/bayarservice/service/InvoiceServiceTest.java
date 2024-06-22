package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentStatus;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    Invoice invoice;
    Invoice newInvoice;
    Bill bill;
    Bill newBill;
    InvoiceRequest createRequest;
    InvoiceRequest updateRequest;
    UUID uuid;

    @BeforeEach
    void setUp() {
        createRequest = InvoiceRequest.builder()
                .sessionId(uuid)
                .build();

        updateRequest = InvoiceRequest.builder()
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .totalAmount(210000L)
                .adminFee(20000)
                .discount(10000L)
                .build();

        invoice = Invoice.builder()
                .id(1)
                .sessionId(uuid)
                .paymentStatus(PaymentStatus.UNPAID)
                .build();

        newInvoice = Invoice.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(210000L)
                .discount(10000L)
                .build();

        bill = Bill.builder()
                .name("Coffee")
                .quantity(10)
                .price(10000)
                .subTotal(100000L)
                .build();

        newBill = Bill.builder()
                .name("Boba Tea")
                .quantity(10)
                .price(20000)
                .subTotal(200000L)
                .build();

         uuid = UUID.randomUUID();
    }

    @Test
    void whenCreateInvoiceShouldReturnTheCreatedInvoice() {
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            var invoice = invocation.getArgument(0, Invoice.class);
            invoice.setId(1);
            return invoice;
        });

        Invoice result = invoiceService.create(createRequest);

        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
        Assertions.assertEquals(invoice, result);
    }

    @Test
    void whenCreateInvoiceAndAlreadyExistShouldThrowException() {
        when(invoiceRepository.findBySessionId(any())).thenReturn(Optional.of(invoice));

        Assertions.assertThrows(InvoiceAlreadyExistException.class,
                () -> invoiceService.create(createRequest));
    }

    @Test
    void whenUpdateInvoiceAndNotFoundShouldThrowException() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(InvoiceDoesNotExistException.class, () -> invoiceService.findById(1));
    }

    @Test
    void whenFindByIdAndFoundShouldReturnInvoice() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.findById(1);

        verify(invoiceRepository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(invoice, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(InvoiceDoesNotExistException.class, () -> invoiceService.findById(1));
    }

    @Test
    void whenFindBySessionIdAndFoundShouldReturnInvoice() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.findBySessionId(uuid);

        verify(invoiceRepository, atLeastOnce()).findBySessionId(any(UUID.class));
        Assertions.assertEquals(invoice, result);
    }

    @Test
    void whenFindBySessionIdAndNotFoundShouldThrowException() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(InvoiceDoesNotExistException.class,
                () -> invoiceService.findBySessionId(uuid));
    }
}

