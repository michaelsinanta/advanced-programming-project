package id.ac.ui.cs.advprog.warnetservice.dto.perpanjang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtendSessionRequest {
    private UUID id;
    private Integer pricingId;
    private Integer quantity;
}
