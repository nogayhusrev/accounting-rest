package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.dto.InvoiceProductDto;
import com.nogayhusrev.accounting_rest.dto.ProductDto;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.entity.Invoice;
import com.nogayhusrev.accounting_rest.entity.InvoiceProduct;
import com.nogayhusrev.accounting_rest.entity.Product;
import com.nogayhusrev.accounting_rest.enums.InvoiceType;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.InvoiceProductRepository;
import com.nogayhusrev.accounting_rest.service.InvoiceProductService;
import com.nogayhusrev.accounting_rest.service.InvoiceService;
import com.nogayhusrev.accounting_rest.service.ProductService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final ProductService productService;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, UserService userService, InvoiceService invoiceService, ProductService productService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.productService = productService;
    }


    @Override
    public List<InvoiceProductDto> findInvoiceProductsByInvoiceId(Long invoiceId) throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        if (!invoiceService.findById(invoiceId).getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Not Found");

        List<InvoiceProductDto> invoiceProductDtoList = invoiceProductRepository.findInvoiceProductsByInvoiceId(invoiceId).stream()
                .sorted(Comparator.comparing((InvoiceProduct each) -> each.getInvoice().getInvoiceNo()).reversed())
                .map(each -> {
                    InvoiceProductDto dto = mapperUtil.convert(each, new InvoiceProductDto());
                    dto.setTotal(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity() * (each.getTax() + 100) / 100d)));
                    return dto;
                })
                .collect(Collectors.toList());

        if (invoiceProductDtoList == null)
            throw new AccountingProjectException("Invoice Products Not Found");

        return invoiceProductDtoList;

    }

    @Override
    public List<InvoiceProduct> findInvoiceProductsByInvoiceType(InvoiceType invoiceType) throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        return invoiceProductRepository.findAll().stream()
                .filter(invoiceProduct -> invoiceProduct.getInvoice().getCompany().getTitle().equals(company.getTitle()))
                .filter(invoiceProduct -> invoiceProduct.getInvoice().getInvoiceType().equals(invoiceType))
                .collect(Collectors.toList());
    }


    @Override
    public void saveInvoiceProductByInvoiceId(InvoiceProductDto invoiceProductDto, Long invoiceId) throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        if (!invoiceService.findById(invoiceId).getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Not Found");


        Invoice invoice = mapperUtil.convert(invoiceService.findById(invoiceId), new Invoice());
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceProduct.setInvoice(invoice);
        invoiceProduct.setProfitLoss(BigDecimal.ZERO);
        save(mapperUtil.convert(invoiceProduct, new InvoiceProductDto()));
    }

    @Override
    public void completeApprovalProcedures(Long invoiceId, InvoiceType type) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        if (!invoiceService.findById(invoiceId).getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Not Found");

        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAllByInvoice_Id(invoiceId);
        if (type == InvoiceType.SALES) {
            for (InvoiceProduct salesInvoiceProduct : invoiceProductList) {
                if (checkProductQuantity(mapperUtil.convert(salesInvoiceProduct, new InvoiceProductDto()))) {
                    updateQuantityOfProduct(salesInvoiceProduct, type);
                    salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getQuantity());
                    invoiceProductRepository.save(salesInvoiceProduct);
                    setProfitLossOfInvoiceProductsForSalesInvoice(salesInvoiceProduct);
                } else {
                    throw new AccountingProjectException("This sale cannot be completed due to insufficient quantity of the product");
                }
            }
        } else {
            for (InvoiceProduct purchaseInvoiceProduct : invoiceProductList) {
                updateQuantityOfProduct(purchaseInvoiceProduct, type);
                purchaseInvoiceProduct.setRemainingQuantity(purchaseInvoiceProduct.getQuantity());
                invoiceProductRepository.save(purchaseInvoiceProduct);
            }
        }
    }

    @Override
    public boolean checkProductQuantity(InvoiceProductDto salesInvoiceProduct) {
        return salesInvoiceProduct.getProduct().getQuantityInStock() >= salesInvoiceProduct.getQuantity();

    }

    @Override
    public List<InvoiceProduct> findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType type, Product product, Integer remainingQuantity) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        return invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(type, product, remainingQuantity);
    }

    @Override
    public List<InvoiceProduct> findAllInvoiceProductsByProductId(Long id) throws AccountingProjectException {

        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        return invoiceProductRepository.findAllInvoiceProductByProductId(id);
    }

    @Override
    public InvoiceProductDto findById(Long invoiceProductId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(invoiceProductId).get();

        if (invoiceProduct.getInvoice().getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Product Not Found");


        return mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> findAll() throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        Company company = mapperUtil.convert(userService.getCurrentUser().getCompany(), new Company());
        return invoiceProductRepository.findAll().stream()
                .filter(invoiceProduct -> invoiceProduct.getInvoice().getCompany().getTitle().equals(company.getTitle()))
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceProductDto findByName(String name) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        throw new AccountingProjectException("FIND_BY_NAME METHOD NOT IMPLEMENTED");
    }

    @Override
    public void save(InvoiceProductDto invoiceProductDto) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDto, new InvoiceProduct()));
    }

    public void delete(Long invoiceProductId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(invoiceProductId).get();

        if (invoiceProduct.getInvoice().getCompany().getTitle().equalsIgnoreCase(userService.getCurrentUser().getCompany().getTitle()))
            throw new AccountingProjectException("Invoice Product Not Found");

        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public void update(InvoiceProductDto invoiceProductDto, Long aLong) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");


        throw new AccountingProjectException("UPDATE METHOD NOT IMPLEMENTED");
    }

    @Override
    public boolean isExist(InvoiceProductDto invoiceProductDto, Long invoiceProductId) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        throw new AccountingProjectException("DELETE METHOD NOT IMPLEMENTED");
    }

    @Override
    public boolean isExist(InvoiceProductDto invoiceProductDto) throws AccountingProjectException {
        if (isNotAdminOrManager())
            throw new AccountingProjectException("You Are Not Manager Or Employee");

        throw new AccountingProjectException("IS_EXIST METHOD NOT IMPLEMENTED");
    }


    private void updateQuantityOfProduct(InvoiceProduct invoiceProduct, InvoiceType type) throws AccountingProjectException {
        ProductDto productDto = mapperUtil.convert(invoiceProduct.getProduct(), new ProductDto());
        if (type.equals(InvoiceType.SALES)) {
            productDto.setQuantityInStock(productDto.getQuantityInStock() - invoiceProduct.getQuantity());
        } else {
            productDto.setQuantityInStock(productDto.getQuantityInStock() + invoiceProduct.getQuantity());
        }
        productService.update(productDto, productDto.getId());
    }

    private void setProfitLossOfInvoiceProductsForSalesInvoice(InvoiceProduct salesInvoiceProduct) throws AccountingProjectException {
        List<InvoiceProduct> availableProductsForSale =
                findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType.PURCHASE, salesInvoiceProduct.getProduct(), 0);
        for (InvoiceProduct availableProduct : availableProductsForSale) {
            if (salesInvoiceProduct.getRemainingQuantity() <= availableProduct.getRemainingQuantity()) {
                BigDecimal costTotalForQty = availableProduct.getPrice().multiply(
                        BigDecimal.valueOf(salesInvoiceProduct.getRemainingQuantity() * (availableProduct.getTax() + 100) / 100d));
                BigDecimal salesTotalForQty = salesInvoiceProduct.getPrice().multiply(
                        BigDecimal.valueOf(salesInvoiceProduct.getRemainingQuantity() * (salesInvoiceProduct.getTax() + 100) / 100d));
                BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss().add(salesTotalForQty.subtract(costTotalForQty));
                availableProduct.setRemainingQuantity(availableProduct.getRemainingQuantity() - salesInvoiceProduct.getRemainingQuantity());
                salesInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(availableProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
                break;
            } else {
                BigDecimal costTotalForQty = availableProduct.getPrice()
                        .multiply(BigDecimal.valueOf(availableProduct.getRemainingQuantity() * (availableProduct.getTax() + 100) / 100d));
                BigDecimal salesTotalForQty = salesInvoiceProduct.getPrice().multiply(
                        BigDecimal.valueOf(availableProduct.getRemainingQuantity() * (salesInvoiceProduct.getTax() + 100) / 100d));
                BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss()
                        .add(salesTotalForQty.subtract(costTotalForQty));
                salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getRemainingQuantity() - availableProduct.getRemainingQuantity());
                availableProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(availableProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
            }
        }
    }


    private boolean isNotAdminOrManager() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Manager")
                &&
                !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Employee");
    }

}
