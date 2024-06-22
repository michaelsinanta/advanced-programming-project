package id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo;

import java.util.List;

import id.ac.ui.cs.advprog.warnetservice.model.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SessionDetailResponse {
    Session session;
    List<SessionPricingResponse> sessionPricingList;
}