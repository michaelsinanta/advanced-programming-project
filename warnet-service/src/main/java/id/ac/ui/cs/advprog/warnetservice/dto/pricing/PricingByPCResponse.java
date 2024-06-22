package id.ac.ui.cs.advprog.warnetservice.dto.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingByPCResponse {
    Integer pricingId;
    String namaPricing;
    Integer harga;
    Integer durasi;
    String makananId;
}
