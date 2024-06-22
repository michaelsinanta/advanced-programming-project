package id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PCRequest {
    private Integer noPC;
    private Integer noRuangan;
}
