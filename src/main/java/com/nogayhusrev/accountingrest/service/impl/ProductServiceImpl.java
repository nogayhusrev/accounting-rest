package com.nogayhusrev.accountingrest.service.impl;

import com.nogayhusrev.accountingrest.dto.ProductDto;
import com.nogayhusrev.accountingrest.entity.Company;
import com.nogayhusrev.accountingrest.entity.Product;
import com.nogayhusrev.accountingrest.mapper.MapperUtil;
import com.nogayhusrev.accountingrest.repository.ProductRepository;
import com.nogayhusrev.accountingrest.service.ProductService;
import com.nogayhusrev.accountingrest.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, UserService userService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }


    @Override
    public ProductDto findById(Long productId) {
        return mapperUtil.convert(productRepository.findById(productId).get(), new ProductDto());
    }

    @Override
    public List<ProductDto> findAll() {
        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        return productRepository.findAll().stream()
                .filter(product -> product.getCategory().getCompany().getTitle().equalsIgnoreCase(company.getTitle()))
                .sorted(Comparator.comparing((Product product) -> product.getCategory().getDescription())
                        .thenComparing(Product::getName))
                .map(product -> mapperUtil.convert(product, new ProductDto())).collect(Collectors.toList());
    }

    @Override
    public ProductDto findByName(String name) {
        Product product =  productRepository.findAll().stream()
                .filter(savedProduct -> savedProduct.getName().equalsIgnoreCase(name))
                .findFirst().get();

        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public void save(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto, new Product());
        product.setQuantityInStock(0);
        productRepository.save(product);

    }

    @Override
    public void delete(Long productId) {
        Product product = productRepository.findById(productId).get();

        if (product.getQuantityInStock() > 0)
            return;

        product.setIsDeleted(true);
        product.setName(product.getName() + "_" + product.getId() + "_DELETED");

        productRepository.save(product);
    }

    @Override
    public void update(ProductDto productDto, Long productId) {
        productDto.setId(productId);
        productRepository.save(mapperUtil.convert(productDto, new Product()));
    }

    @Override
    public boolean isExist(ProductDto productDto, Long productId) {
        Long idCheck = productRepository.findAll().stream()
                .filter(savedProduct -> savedProduct.getName().equalsIgnoreCase(productDto.getName()))
                .filter(savedProduct -> savedProduct.getId() != productId)
                .count();

        return idCheck > 0;
    }

    @Override
    public boolean isExist(ProductDto productDto) {
        return productRepository.findAll().stream()
                .filter(savedProduct -> savedProduct.getName().equalsIgnoreCase(productDto.getName()))
                .count() > 0;
    }
}
