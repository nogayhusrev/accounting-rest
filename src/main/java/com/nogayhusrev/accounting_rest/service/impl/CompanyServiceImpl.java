package com.nogayhusrev.accounting_rest.service.impl;


import com.nogayhusrev.accounting_rest.dto.CompanyDto;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.enums.CompanyStatus;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.CompanyRepository;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final MapperUtil mapperUtil;

    private final UserService userService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, UserService userService) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }


    @Override
    public CompanyDto findById(Long companyId) {
        return mapperUtil.convert(companyRepository.findById(companyId).get(), new CompanyDto());
    }

    @Override
    public List<CompanyDto> findAll() {
        return companyRepository.findAll()
                .stream()
                .filter(company -> company.getId() != 1)
                .sorted(Comparator.comparing(Company::getCompanyStatus).thenComparing(Company::getTitle))
                .map(each -> mapperUtil.convert(each, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto findByName(String name) {
        Company company = companyRepository.findAll().stream()
                .filter(savedCompany -> savedCompany.getTitle().equalsIgnoreCase(name))
                .findFirst().get();

        return mapperUtil.convert(company, new CompanyDto());


    }

    @Override
    public void save(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(mapperUtil.convert(companyDto, new Company()));

    }

    @Override
    public void delete(Long companyId) {
        throw new IllegalStateException("NOT IMPLEMENTED");
    }

    @Override
    public void update(CompanyDto companyDto, Long companyId) {

        Company savedCompany = companyRepository.findById(companyId).get();
        companyDto.setId(companyId);
        companyDto.setCompanyStatus(savedCompany.getCompanyStatus());
        companyDto.getAddress().setId(savedCompany.getAddress().getId());
        companyDto.getAddress().setCountry(savedCompany.getAddress().getCountry());
        Company updatedCompany = mapperUtil.convert(companyDto, new Company());

        companyRepository.save(updatedCompany);

    }

    @Override
    public boolean isExist(CompanyDto companyDto, Long companyId) {
        Long idCheck = companyRepository.findAll().stream()
                .filter(savedCompany -> savedCompany.getTitle().equalsIgnoreCase(companyDto.getTitle()))
                .filter(savedCompany -> savedCompany.getId() != companyId)
                .count();

        return idCheck > 0;

    }

    @Override
    public boolean isExist(CompanyDto companyDto) {
        return companyRepository.findAll().stream()
                .filter(savedCompany -> savedCompany.getTitle().equalsIgnoreCase(companyDto.getTitle()))
                .count() > 0;
    }


    @Override
    public void activate(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivate(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }

    @Override
    public List<CompanyDto> getCompaniesForCurrentUser() {
        List<Company> companies;
        if (userService.getCurrentUser().getRole().getDescription().equals("Root User")) {
            companies = companyRepository.findAll();
        } else {
            companies = companyRepository.findAll().stream()
                    .filter(company -> company.getTitle().equals(userService.getCurrentUser().getCompany().getTitle()))
                    .collect(Collectors.toList());
        }

        return companies.stream().map(company -> mapperUtil.convert(company, new CompanyDto())).collect(Collectors.toList());
    }

}
