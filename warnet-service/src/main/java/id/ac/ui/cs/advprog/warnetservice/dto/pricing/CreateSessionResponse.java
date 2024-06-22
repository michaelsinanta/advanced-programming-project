package id.ac.ui.cs.advprog.warnetservice.dto.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionResponse {
    UUID id;
    Integer pcId;
    Integer noPC;
    Integer noRuangan;
    Integer pricingId;
    String namaPricing;
    Integer totalHarga;
    Integer totalDurasi;
}
