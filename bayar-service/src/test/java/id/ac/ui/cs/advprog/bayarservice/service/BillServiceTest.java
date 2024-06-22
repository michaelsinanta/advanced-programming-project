package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.BillRepository;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
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
class BillServiceTest {

    @InjectMocks
    private BillServiceImpl billService;

    @Mock
    private BillRepository billRepository;

    @Mock
    private InvoiceServiceImpl invoiceService;

    Bill bill;

    Bill newBill;

    Invoice invoice;

    BillRequest updateRequest;

    @BeforeEach
    void setUp() {
        invoice = Invoice.builder()
                .id(1)
                .totalAmount(50000L)
                .sessionId(UUID.randomUUID())
                .build();

        bill = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .invoice(invoice)
                .build();

        updateRequest = BillRequest.builder()
                .name("Coffee Updated")
                .quantity(10)
                .price(10000)
                .subTotal(100000L)
                .build();

        newBill = Bill.builder()
                .name("Coffee Updated")
                .quantity(10)
                .price(10000)
                .subTotal(100000L)
                .invoice(invoice)
                .build();
    }

    @Test
    void whenFindByIdAndFoundShouldReturnBill() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.of(bill));

        Bill result = billService.findById(1);

        verify(billRepository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(bill, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BillDoesNotExistException.class, () -> billService.findById(1));
    }

    @Test
    void whenDeleteAndFoundByIdShouldDeleteBill() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.of(bill));

        billService.delete(1);

        verify(billRepository, atLeastOnce()).findById(any(Integer.class));
        verify(billRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteAndNotFoundByIdShouldThrowException() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BillDoesNotExistException.class, () -> billService.delete(1));
    }

    @Test
    void whenCreateBillShouldReturnBill() {
        when(invoiceService.findBySessionId(any(UUID.class))).thenReturn(invoice);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        BillRequest billRequest = new BillRequest("Coffee", 5, 10000, 50000L, invoice.getSessionId());
        Bill result = billService.create(billRequest);

        verify(billRepository, atLeastOnce()).save(any(Bill.class));
        Assertions.assertEquals(bill, result);
    }

    @Test
    void whenUpdateBillAndFoundShouldReturnTheUpdatedBill() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.of(bill));

        when(billRepository.save(any(Bill.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Bill.class));

        when(invoiceService.findById(any())).thenReturn(invoice);

        Bill result = billService.update(0, updateRequest);
        verify(billRepository, atLeastOnce()).save(any(Bill.class));
        Assertions.assertEquals(newBill, result);
    }

    @Test
    void whenUpdateBillAndNotFoundShouldThrowException() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(BillDoesNotExistException.class,
                () -> billService.update(0, updateRequest));
    }
}
