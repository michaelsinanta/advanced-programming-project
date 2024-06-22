package id.ac.ui.cs.advprog.bayarservice.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest {

    private String name;
    private Integer price;
    private Integer quantity;
    private Long subTotal;
    private UUID sessionId;
}
