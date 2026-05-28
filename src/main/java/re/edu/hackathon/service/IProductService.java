package re.edu.hackathon.service;

import org.springframework.data.domain.Page;
import re.edu.hackathon.dto.request.ProductPatchRequest;
import re.edu.hackathon.dto.request.ProductRequest;
import re.edu.hackathon.dto.response.ProductResponse;

public interface IProductService {
    Page<ProductResponse> findAll(String keyword, Boolean deleted, int page, int size, String sortBy, String direction);

    ProductResponse findById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    ProductResponse patch(Long id, ProductPatchRequest request);

    void delete(Long id, boolean hardDelete);
}
