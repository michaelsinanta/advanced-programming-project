package id.ac.ui.cs.advprog.bayarservice.dto.invoice;

import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @NotNull(message = "sessionId is mandatory")
    private UUID sessionId;

    private String paymentMethod;

    @Min(0)
    private Integer adminFee;

    @Min(0)
    private Long totalAmount;

    @Min(0)
    private Long discount;

    public Invoice toEntity() {
        Invoice entity = new Invoice();

        entity.setSessionId(this.sessionId);
        entity.setDiscount(this.discount);
        if (this.paymentMethod != null) {
            entity.setPaymentMethod(PaymentMethod.valueOf(this.paymentMethod));
        }
        entity.setTotalAmount(this.totalAmount);

        return entity;
    }
}