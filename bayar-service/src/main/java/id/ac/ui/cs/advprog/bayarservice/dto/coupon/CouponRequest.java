package id.ac.ui.cs.advprog.bayarservice.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {
    @NotNull(message = "name is mandatory")
    private String name;

    @Min(0)
    @NotNull(message = "discount is mandatory")
    private Long discount;
}
