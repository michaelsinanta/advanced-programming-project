package id.ac.ui.cs.advprog.warnetservice.dto.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionRequest {
    private Integer pcId;
    private Integer pricingId;
    private Integer quantity;
}
