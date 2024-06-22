package id.ac.ui.cs.advprog.bayarservice.dto.warnet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PCResponse {
    private Integer id;

    private Integer noPC;

    private Integer noRuangan;
}
