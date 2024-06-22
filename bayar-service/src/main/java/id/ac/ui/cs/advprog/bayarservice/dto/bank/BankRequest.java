package id.ac.ui.cs.advprog.bayarservice.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankRequest {
    private String name;
    private Integer adminFee;
}
