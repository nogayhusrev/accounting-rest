package com.nogayhusrev.accountingrest.service.impl;

import com.nogayhusrev.accountingrest.dto.CategoryDto;
import com.nogayhusrev.accountingrest.entity.Category;
import com.nogayhusrev.accountingrest.entity.Company;
import com.nogayhusrev.accountingrest.mapper.MapperUtil;
import com.nogayhusrev.accountingrest.repository.CategoryRepository;
import com.nogayhusrev.accountingrest.service.CategoryService;
import com.nogayhusrev.accountingrest.service.ProductService;
import com.nogayhusrev.accountingrest.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final MapperUtil mapperUtil;

    private final UserService userService;
    private final ProductService productService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, UserService userService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.productService = productService;
    }


    @Override
    public CategoryDto findById(Long categoryId) {
        return mapperUtil.convert(categoryRepository.findById(categoryId).get(), new CategoryDto());
    }

    @Override
    public List<CategoryDto> findAll() {
        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        return categoryRepository
                .findAllByCompany(company)
                .stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(category -> {

                    CategoryDto categoryDto = mapperUtil.convert(category, new CategoryDto());
                    if (hasProducts(category))
                        categoryDto.setHasProduct(true);

                    return categoryDto;

                }).collect(Collectors.toList());
    }

    @Override
    public CategoryDto findByName(String name) {
        Category category = categoryRepository.findAll().stream()
                .filter(savedCategory -> savedCategory.getDescription().equalsIgnoreCase(name))
                .findFirst().get();

        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public void save(CategoryDto categoryDto) {
        Category category = mapperUtil.convert(categoryDto, new Category());
        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        category.setCompany(company);

        categoryRepository.save(category);
    }

    @Override
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();

        if (hasProducts(category))
            return;

        category.setIsDeleted(true);
        category.setDescription(category.getDescription() + "_" + category.getId() + "_DELETED");
        categoryRepository.save(category);
    }


    private boolean hasProducts(Category category) {
        return productService.findAll().stream()
                .filter(productDto -> productDto.getCategory().getDescription().equalsIgnoreCase(category.getDescription()))
                .count() > 0;
    }

    @Override
    public void update(CategoryDto categoryDto, Long categoryId) {
        CategoryDto savedCategory = mapperUtil.convert(categoryRepository.findById(categoryId).get(), new CategoryDto());

        categoryDto.setCompany(savedCategory.getCompany());
        categoryDto.setId(categoryId);
        categoryRepository.save(mapperUtil.convert(categoryDto, new Category()));
    }

    @Override
    public boolean isExist(CategoryDto categoryDto, Long categoryId) {

        Long idCheck = categoryRepository.findAll().stream()
                .filter(savedCategory -> savedCategory.getDescription().equalsIgnoreCase(categoryDto.getDescription()))
                .filter(savedCategory -> savedCategory.getId() != categoryId)
                .count();


        return idCheck > 0;
    }

    @Override
    public boolean isExist(CategoryDto categoryDto) {
        return categoryRepository.findAll().stream()
                .filter(savedCategory -> savedCategory.getDescription().equalsIgnoreCase(categoryDto.getDescription()))
                .count() > 0;
    }


}
