package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.dto.ClientVendorDto;
import com.nogayhusrev.accounting_rest.dto.InvoiceDto;
import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.entity.Invoice;
import com.nogayhusrev.accounting_rest.enums.ClientVendorType;
import com.nogayhusrev.accounting_rest.enums.InvoiceStatus;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.InvoiceRepository;
import com.nogayhusrev.accounting_rest.service.ClientVendorService;
import com.nogayhusrev.accounting_rest.service.InvoiceProductService;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, UserService userService, ClientVendorService clientVendorService, @Lazy InvoiceProductService invoiceProductService) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.clientVendorService = clientVendorService;
        this.invoiceProductService = invoiceProductService;
    }


    @Override
    public InvoiceDto findById(Long invoiceId) throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Invoice invoice = invoiceRepository.findById(invoiceId).get();

        if (invoice.getCompany().getTitle().equals(userService.getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(invoice, new InvoiceDto());

        throw new AccountingProjectException("Invoice Not Found");

    }

    @Override
    public List<InvoiceDto> findAll() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Company currentUserCompany = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());

        return invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getCompany().getTitle().equalsIgnoreCase(currentUserCompany.getTitle()))
                .sorted(Comparator.comparing(Invoice::getInvoiceNo).reversed())
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto findByName(String invoiceNo) throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Invoice invoice = invoiceRepository.findAll().stream()
                .filter(savedInvoice -> savedInvoice.getInvoiceNo().equals(invoiceNo))
                .findFirst().get();

        if (invoice.getCompany().getTitle().equals(userService.getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(invoice, new InvoiceDto());

        throw new AccountingProjectException("Invoice Not Found");

    }

    @Override
    public List<InvoiceDto> findPurchaseInvoices() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        return findAll().stream()
                .filter(invoice -> invoice.getInvoiceType().getValue().equals(InvoiceType.PURCHASE.getValue()))
                .map(invoiceDto -> {
                    try {
                        invoiceDto.setInvoiceProducts(invoiceProductService.findInvoiceProductsByInvoiceId(invoiceDto.getId()));
                    } catch (AccountingProjectException e) {
                        throw new RuntimeException(e);
                    }
                    return invoiceDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> findSaleInvoices() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        return findAll().stream()
                .filter(invoice -> invoice.getInvoiceType().getValue().equals(InvoiceType.SALES.getValue()))
                .map(invoiceDto -> {
                    try {
                        invoiceDto.setInvoiceProducts(invoiceProductService.findInvoiceProductsByInvoiceId(invoiceDto.getId()));
                    } catch (AccountingProjectException e) {
                        throw new RuntimeException(e);
                    }
                    return invoiceDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> findVendors() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        return clientVendorService.findAll().stream()
                .filter(clientVendorDto -> clientVendorDto.getClientVendorType().getValue().equalsIgnoreCase(ClientVendorType.VENDOR.getValue()))
                .filter(clientVendorDto -> clientVendorDto.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> findClients() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        return clientVendorService.findAll().stream()
                .filter(clientVendorDto -> clientVendorDto.getClientVendorType().getValue().equalsIgnoreCase(ClientVendorType.CLIENT.getValue()))
                .filter(clientVendorDto -> clientVendorDto.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public String generateInvoiceNo(InvoiceType invoiceType) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        List<Invoice> invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceType(company, invoiceType);
        if (invoices.size() == 0) {
            return invoiceType.name().charAt(0) + "-001";
        }
        Invoice lastInvoiceOfTheCompany = invoices.stream()
                .max(Comparator.comparing(Invoice::getInsertDateTime)).get();
        int newOrder = Integer.parseInt(lastInvoiceOfTheCompany.getInvoiceNo().substring(2)) + 1;
        return invoiceType.name().charAt(0) + "-" + String.format("%03d", newOrder);

    }

    @Override
    public void save(InvoiceDto invoiceDto, InvoiceType invoiceType) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setInvoiceProducts(new ArrayList<>());
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(userService.getCurrentUser().getCompany());
        invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));

    }

    @Override
    public void approve(Long invoiceId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Invoice invoice = invoiceRepository.findById(invoiceId).get();

        if (!invoice.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Not Found");

        invoiceProductService.completeApprovalProcedures(invoiceId, invoice.getInvoiceType());


        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDto> findLastThreeInvoices() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        return findAll().stream()
                .sorted(Comparator.comparing(InvoiceDto::getId).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> findInvoiceByInvoiceStatus(InvoiceStatus invoiceStatus) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());

        return invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getCompany().getTitle().equalsIgnoreCase(company.getTitle()))
                .filter(invoice -> invoice.getInvoiceStatus().equals(invoiceStatus))
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void printInvoice(Long invoiceId) throws AccountingProjectException {

        InvoiceDto invoiceDto = mapperUtil.convert(invoiceRepository.findById(invoiceId).get(), new InvoiceDto());
        calculateInvoiceDetails(invoiceDto);

    }


    @Override
    public void save(InvoiceDto invoiceDto) throws AccountingProjectException {
        throw new AccountingProjectException("SAVE METHOD NOT IMPLEMENTED");
    }

    @Override
    public void delete(Long invoiceId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Invoice invoice = invoiceRepository.findById(invoiceId).get();

        if (!invoice.getCompany().getTitle().equals(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Not Found");

        invoiceProductService.findInvoiceProductsByInvoiceId(invoiceId)
                .forEach(invoiceProductDto -> {
                    try {
                        invoiceProductService.delete(invoiceProductDto.getId());
                    } catch (AccountingProjectException e) {
                        throw new RuntimeException(e);
                    }
                });
        invoice.setIsDeleted(true);
        invoice.setInvoiceNo(invoice.getInvoiceNo() + "_DELETED");
        invoiceRepository.save(invoice);
    }


    @Override
    public void update(InvoiceDto invoiceDto, Long invoiceId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        if (!invoiceDto.getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Not Found");


        Invoice invoice = invoiceRepository.findById(invoiceId).get();

        invoiceDto.setInvoiceNo(invoice.getInvoiceNo());

        invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));

    }

    @Override
    public boolean isExist(InvoiceDto invoiceDto, Long invoiceId) {
        Long idCheck = invoiceRepository.findAll().stream()
                .filter(savedInvoice -> savedInvoice.getInvoiceNo().equalsIgnoreCase(invoiceDto.getInvoiceNo()))
                .filter(savedProduct -> savedProduct.getId() != invoiceId)
                .count();

        return idCheck > 0;
    }

    @Override
    public boolean isExist(InvoiceDto invoiceDto) {
        return invoiceRepository.findAll().stream()
                .filter(savedInvoice -> savedInvoice.getInvoiceNo().equalsIgnoreCase(savedInvoice.getInvoiceNo()))
                .count() > 0;
    }

    private void calculateInvoiceDetails(InvoiceDto invoiceDto) throws AccountingProjectException {
        invoiceDto.setPrice(getTotalPriceOfInvoice(invoiceDto.getId()));
        invoiceDto.setTax(getTotalTaxOfInvoice(invoiceDto.getId()));
        invoiceDto.setTotal(getTotalPriceOfInvoice(invoiceDto.getId()).add(getTotalTaxOfInvoice(invoiceDto.getId())));
    }

    @Override
    public BigDecimal getTotalPriceOfInvoice(Long invoiceId) throws AccountingProjectException {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.findInvoiceProductsByInvoiceId(invoice.getId());
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice()
                        .multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalTaxOfInvoice(Long invoiceId) throws AccountingProjectException {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.findInvoiceProductsByInvoiceId(invoice.getId());
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice()
                        .multiply(BigDecimal.valueOf(p.getQuantity() * p.getTax() / 100d))
                        .setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getProfitLossOfInvoice(Long invoiceId) throws AccountingProjectException {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.findInvoiceProductsByInvoiceId(invoice.getId());
        return invoiceProductsOfInvoice.stream()
                .map(InvoiceProductDto::getProfitLoss)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }


}
