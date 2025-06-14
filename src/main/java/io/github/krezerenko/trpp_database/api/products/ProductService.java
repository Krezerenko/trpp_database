package io.github.krezerenko.trpp_database.api.products;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public Product findProductById(Long id)
    {
        return productRepository.findById(id).orElse(null);
    }
}
