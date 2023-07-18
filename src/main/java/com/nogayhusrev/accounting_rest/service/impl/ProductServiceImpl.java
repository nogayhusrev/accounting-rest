package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.dto.ProductDto;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.entity.Product;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.ProductRepository;
import com.nogayhusrev.accounting_rest.service.ProductService;
import com.nogayhusrev.accounting_rest.service.UserService;
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
    public ProductDto findById(Long productId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Product product = productRepository.findById(productId).get();

        if (product.getCategory().getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(product, new ProductDto());

        throw new AccountingProjectException("Product Not Found");
    }

    @Override
    public List<ProductDto> findAll() throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        return productRepository.findAll().stream()
                .filter(product -> product.getCategory().getCompany().getTitle().equalsIgnoreCase(company.getTitle()))
                .sorted(Comparator.comparing((Product product) -> product.getCategory().getDescription())
                        .thenComparing(Product::getName))
                .map(product -> mapperUtil.convert(product, new ProductDto())).collect(Collectors.toList());
    }

    @Override
    public ProductDto findByName(String name) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Product product = productRepository.findAll().stream()
                .filter(savedProduct -> savedProduct.getName().equalsIgnoreCase(name))
                .findFirst().get();

        if (product.getCategory().getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(product, new ProductDto());

        throw new AccountingProjectException("Product Not Found");
    }

    @Override
    public void save(ProductDto productDto) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Product product = mapperUtil.convert(productDto, new Product());
        product.setQuantityInStock(0);
        productRepository.save(product);

    }

    @Override
    public void delete(Long productId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Product product = productRepository.findById(productId).get();

        if (!product.getCategory().getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Product Not Found");

        if (product.getQuantityInStock() > 0)
            throw new AccountingProjectException("Product Has Stock, Not Deleted");

        product.setIsDeleted(true);
        product.setName(product.getName() + "_" + product.getId() + "_DELETED");

        productRepository.save(product);
    }

    @Override
    public void update(ProductDto productDto, Long productId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Product product = productRepository.findById(productId).get();

        if (!product.getCategory().getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Product Not Found");

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

    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }
}
