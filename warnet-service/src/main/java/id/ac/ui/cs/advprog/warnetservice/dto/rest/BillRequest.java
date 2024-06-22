package id.ac.ui.cs.advprog.warnetservice.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest {

    private String name;
    private Integer price;
    private Integer quantity;
    private Long subTotal;
    private String sessionId;
}
