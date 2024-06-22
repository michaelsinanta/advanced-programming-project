package id.ac.ui.cs.advprog.warnetservice.dto.pricing;

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
public class SessionDetail {
    UUID sessionId;
    LocalDateTime datetimeStart;
    LocalDateTime datetimeEnd;
}
