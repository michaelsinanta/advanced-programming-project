package id.ac.ui.cs.advprog.bayarservice.service.payment;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.DetailPaymentLogResponse;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<String> getPaymentMethods();
    PaymentLog create(Integer invoiceId, PaymentRequest request);
    List<PaymentLog> getPaymentLog();
    List<PaymentLog> getPaymentLogByYearAndMonth(int year, int month);
    List<PaymentLog> getPaymentLogByYear(int year);
    List<PaymentLog> getPaymentLogByWeekAndYear(int year, int week);
    DetailPaymentLogResponse getPaymentLogDetail(UUID sessionId);
}
