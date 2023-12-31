package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.dto.CategoryDto;
import com.nogayhusrev.accounting_rest.entity.Category;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.CategoryRepository;
import com.nogayhusrev.accounting_rest.service.CategoryService;
import com.nogayhusrev.accounting_rest.service.ProductService;
import com.nogayhusrev.accounting_rest.service.UserService;
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
    public CategoryDto findById(Long categoryId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Category category = categoryRepository.findById(categoryId).get();

        if (category.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(category, new CategoryDto());

        throw new AccountingProjectException("Category Not Found");

    }

    @Override
    public List<CategoryDto> findAll() throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());

        return categoryRepository.findAllByCompany(company).stream().sorted(Comparator.comparing(Category::getDescription)).map(category -> {

            CategoryDto categoryDto = mapperUtil.convert(category, new CategoryDto());
            try {
                if (hasProducts(category)) categoryDto.setHasProduct(true);
            } catch (AccountingProjectException e) {
                throw new RuntimeException(e);
            }

            return categoryDto;

        }).collect(Collectors.toList());

    }

    @Override
    public CategoryDto findByName(String name) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Category category = categoryRepository.findAll().stream()
                .filter(savedCategory -> savedCategory.getDescription().equalsIgnoreCase(name))
                .filter(savedCategory -> savedCategory.getCompany().getTitle().equals(userService.getCurrentUser().getCompany().getTitle()))
                .findFirst().get();

        if (category == null)
            throw new AccountingProjectException("Category Not Found");

        return mapperUtil.convert(category, new CategoryDto());

    }

    @Override
    public void save(CategoryDto categoryDto) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Category category = mapperUtil.convert(categoryDto, new Category());
        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        category.setCompany(company);

        if (categoryRepository.save(category) == null)
            throw new AccountingProjectException("Category Not Saved");


    }

    @Override
    public void delete(Long categoryId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Category category = categoryRepository.findById(categoryId).get();

        if (!category.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Category Not Found");


        if (hasProducts(category))
            throw new AccountingProjectException("Category Cannot Be Deleted. It Has Products");

        category.setIsDeleted(true);
        category.setDescription(category.getDescription() + "_" + category.getId() + "_DELETED");

        if (categoryRepository.save(category) == null)
            throw new AccountingProjectException("Category Cannot Be Deleted");

    }


    @Override
    public void update(CategoryDto categoryDto, Long categoryId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        CategoryDto savedCategory = mapperUtil.convert(categoryRepository.findById(categoryId).get(), new CategoryDto());

        if (!savedCategory.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Category Not Found");

        categoryDto.setCompany(savedCategory.getCompany());
        categoryDto.setId(categoryId);

        if (categoryRepository.save(mapperUtil.convert(categoryDto, new Category())) == null)
            throw new AccountingProjectException("Category Not Updated");

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

    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }

    private boolean hasProducts(Category category) throws AccountingProjectException {
        return productService.findAll().stream().filter(productDto -> productDto.getCategory().getDescription().equalsIgnoreCase(category.getDescription())).count() > 0;
    }


}
