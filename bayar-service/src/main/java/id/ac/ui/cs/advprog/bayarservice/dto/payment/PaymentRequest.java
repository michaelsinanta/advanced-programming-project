package id.ac.ui.cs.advprog.bayarservice.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = "sessionId is mandatory")
    private UUID sessionId;

    @Min(0)
    @NotNull(message = "totalAmount is mandatory")
    private Long totalAmount;

    @NotBlank(message = "paymentMethod is mandatory")
    private String paymentMethod;

    private Integer bankId;
}
