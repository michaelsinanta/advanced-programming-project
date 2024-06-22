package id.ac.ui.cs.advprog.bayarservice.dto.warnet;

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
public class SessionResponse {
    private UUID id;

    private PCResponse pc;

    private LocalDateTime datetimeStart;

    private LocalDateTime datetimeEnd;
}
