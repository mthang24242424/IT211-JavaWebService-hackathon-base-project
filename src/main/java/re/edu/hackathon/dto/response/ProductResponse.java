package re.edu.hackathon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private String sku;
    private String detail;
    private Integer stockQuantity;
    private BigDecimal salePrice;
    private Boolean deleted;
}
