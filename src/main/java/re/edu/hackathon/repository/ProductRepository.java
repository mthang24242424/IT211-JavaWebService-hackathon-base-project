package re.edu.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import re.edu.hackathon.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByCode(String code);

    boolean existsBySku(String sku);

    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsBySkuAndIdNot(String sku, Long id);
}
