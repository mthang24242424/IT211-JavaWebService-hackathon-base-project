package re.edu.hackathon.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.hackathon.dto.request.ProductPatchRequest;
import re.edu.hackathon.dto.request.ProductRequest;
import re.edu.hackathon.dto.response.ProductResponse;
import re.edu.hackathon.entity.Product;
import re.edu.hackathon.exception.CustomException;
import re.edu.hackathon.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(String keyword, Boolean deleted, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(buildSpecification(keyword, deleted), pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return toResponse(getProduct(id));
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsByCode(request.getCode())) {
            throw new CustomException(HttpStatus.CONFLICT, "Ma san pham da ton tai");
        }
        if (productRepository.existsBySku(request.getSku())) {
            throw new CustomException(HttpStatus.CONFLICT, "SKU da ton tai");
        }

        Product product = new Product();
        applyFullUpdate(product, request);
        product.setDeleted(false);
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getProduct(id);
        validateUniqueCodeAndSku(id, request.getCode(), request.getSku());
        applyFullUpdate(product, request);
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse patch(Long id, ProductPatchRequest request) {
        Product product = getProduct(id);
        if (request.getCode() != null && productRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new CustomException(HttpStatus.CONFLICT, "Ma san pham da ton tai");
        }
        if (request.getSku() != null && productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new CustomException(HttpStatus.CONFLICT, "SKU da ton tai");
        }

        if (request.getCode() != null) product.setCode(request.getCode());
        if (request.getName() != null) product.setName(request.getName());
        if (request.getSku() != null) product.setSku(request.getSku());
        if (request.getDetail() != null) product.setDetail(request.getDetail());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());
        if (request.getSalePrice() != null) product.setSalePrice(request.getSalePrice());
        if (request.getDeleted() != null) product.setDeleted(request.getDeleted());

        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id, boolean hardDelete) {
        Product product = getProduct(id);
        if (hardDelete) {
            productRepository.delete(product);
            return;
        }
        product.setDeleted(true);
        productRepository.save(product);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Khong tim thay san pham"));
    }

    private Specification<Product> buildSpecification(String keyword, Boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), pattern)
                ));
            }
            if (deleted != null) {
                predicates.add(criteriaBuilder.equal(root.get("deleted"), deleted));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void validateUniqueCodeAndSku(Long id, String code, String sku) {
        if (productRepository.existsByCodeAndIdNot(code, id)) {
            throw new CustomException(HttpStatus.CONFLICT, "Ma san pham da ton tai");
        }
        if (productRepository.existsBySkuAndIdNot(sku, id)) {
            throw new CustomException(HttpStatus.CONFLICT, "SKU da ton tai");
        }
    }

    private void applyFullUpdate(Product product, ProductRequest request) {
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setDetail(request.getDetail());
        product.setStockQuantity(request.getStockQuantity());
        product.setSalePrice(request.getSalePrice());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getSku(),
                product.getDetail(),
                product.getStockQuantity(),
                product.getSalePrice(),
                product.getDeleted()
        );
    }
}
