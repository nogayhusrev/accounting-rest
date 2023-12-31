package com.nogayhusrev.accounting_rest.converter;

import com.nogayhusrev.accounting_rest.dto.InvoiceDto;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class InvoiceDtoConverter implements Converter<String, InvoiceDto> {

    private final InvoiceService invoiceService;

    public InvoiceDtoConverter(@Lazy InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @Override
    public InvoiceDto convert(String id) {
        // it throws error if user selects "Select" even with @SneakyThrows
        if (id == null || id.isBlank())
            return null;
        try {
            return invoiceService.findById(Long.parseLong(id));
        } catch (AccountingProjectException e) {
            throw new RuntimeException(e);
        }
    }

}