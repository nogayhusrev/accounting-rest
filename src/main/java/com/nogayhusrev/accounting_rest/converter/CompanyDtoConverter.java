package com.nogayhusrev.accounting_rest.converter;

import com.nogayhusrev.accounting_rest.dto.CompanyDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class CompanyDtoConverter implements Converter<String, CompanyDto> {

    private final CompanyService companyService;

    public CompanyDtoConverter(@Lazy CompanyService companyService) {
        this.companyService = companyService;
    }


    //    @SneakyThrows
    @Override
    public CompanyDto convert(String id) {
        // it throws error if user selects "Select" even with @SneakyThrows
        if (id == null || id.isBlank())
            return null;
        try {
            return companyService.findById(Long.parseLong(id));
        } catch (AccountingProjectException e) {
            throw new RuntimeException(e);
        }
    }

}