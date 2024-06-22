package id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.SessionDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPCResponse {
    private Integer id;
    private Integer noPC;
    private Integer noRuangan;
    private Boolean beingUsed;
    private SessionDetail activeSession;
}

