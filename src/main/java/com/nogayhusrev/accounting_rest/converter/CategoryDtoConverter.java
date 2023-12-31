package com.nogayhusrev.accounting_rest.converter;

import com.nogayhusrev.accounting_rest.dto.CategoryDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.CategoryService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class CategoryDtoConverter implements Converter<String, CategoryDto> {

    private final CategoryService categoryService;

    public CategoryDtoConverter(@Lazy CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public CategoryDto convert(String id) {
        // it throws error if user selects "Select" even with @SneakyThrows
        if (id == null || id.isBlank())
            return null;
        try {
            return categoryService.findById(Long.parseLong(id));
        } catch (AccountingProjectException e) {
            throw new RuntimeException(e);
        }
    }

}