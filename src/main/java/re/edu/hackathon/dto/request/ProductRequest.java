package re.edu.hackathon.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "Ma san pham khong duoc de trong")
    @Size(max = 50, message = "Ma san pham toi da 50 ky tu")
    private String code;

    @NotBlank(message = "Ten san pham khong duoc de trong")
    @Size(max = 150, message = "Ten san pham toi da 150 ky tu")
    private String name;

    @NotBlank(message = "SKU khong duoc de trong")
    @Size(max = 80, message = "SKU toi da 80 ky tu")
    private String sku;

    @Size(max = 2000, message = "Chi tiet toi da 2000 ky tu")
    private String detail;

    @NotNull(message = "So luong kho khong duoc de trong")
    @Min(value = 0, message = "So luong kho phai lon hon hoac bang 0")
    private Integer stockQuantity;

    @NotNull(message = "Don gia ban khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Don gia ban phai lon hon 0")
    private BigDecimal salePrice;
}
