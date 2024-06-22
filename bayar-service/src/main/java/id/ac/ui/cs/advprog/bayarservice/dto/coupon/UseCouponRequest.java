package id.ac.ui.cs.advprog.bayarservice.dto.coupon;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UseCouponRequest {
    @NotNull(message = "name is mandatory")
    private String name;
}
