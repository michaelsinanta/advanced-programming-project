package id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllSessionResponse {
    UUID id;
    Integer pcId;
    Integer noPC;
    Integer noRuangan;
    LocalDateTime datetimeStart;
    LocalDateTime datetimeEnd;
}
