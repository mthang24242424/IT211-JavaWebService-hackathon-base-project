package re.edu.hackathon.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import re.edu.hackathon.common.ApiResponse;
import re.edu.hackathon.dto.request.ProductPatchRequest;
import re.edu.hackathon.dto.request.ProductRequest;
import re.edu.hackathon.dto.response.ProductResponse;
import re.edu.hackathon.service.IProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<ProductResponse> products = productService.findAll(keyword, deleted, page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Lay danh sach san pham thanh cong", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> findById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Lay thong tin san pham thanh cong", product));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "Them san pham thanh cong", product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse product = productService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Cap nhat san pham thanh cong", product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> patch(
            @PathVariable Long id,
            @Valid @RequestBody ProductPatchRequest request
    ) {
        ProductResponse product = productService.patch(id, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Cap nhat mot phan san pham thanh cong", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard
    ) {
        productService.delete(id, hard);
        String message = hard ? "Xoa cung san pham thanh cong" : "Xoa mem san pham thanh cong";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, null));
    }
}
