package id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo;

import java.time.LocalDateTime;

import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionPricingResponse {
    private Pricing pricing;
    private LocalDateTime waktuPembelian;
    private Integer quantity;
}