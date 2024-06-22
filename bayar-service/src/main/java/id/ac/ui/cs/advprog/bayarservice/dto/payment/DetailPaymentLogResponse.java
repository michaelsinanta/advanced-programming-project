package id.ac.ui.cs.advprog.bayarservice.dto.payment;

import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailPaymentLogResponse {
    Invoice invoice;

    PaymentLog paymentLog;

    Bank bank;
}
