package com.nogayhusrev.accounting_rest.service.impl;


import com.nogayhusrev.accounting_rest.dto.CompanyDto;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.enums.CompanyStatus;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
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
    public CompanyDto findById(Long companyId) throws AccountingProjectException {


        if (isNotRoot())
            throw new AccountingProjectException("You Are Not Root");

        Company company;

        try {
            company = companyRepository.findById(companyId).get();
        } catch (Exception e) {
            throw new AccountingProjectException("Company Not Found");
        }


        return mapperUtil.convert(company, new CompanyDto());


    }

    @Override
    public List<CompanyDto> findAll() throws AccountingProjectException {

        if (isNotRoot())
            throw new AccountingProjectException("You Are Not Root");

        List<CompanyDto> companyDtoList;

        try {
            companyDtoList = companyRepository.findAll()
                    .stream()
                    .filter(company -> company.getId() != 1)
                    .sorted(Comparator.comparing(Company::getCompanyStatus).thenComparing(Company::getTitle))
                    .map(each -> mapperUtil.convert(each, new CompanyDto()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AccountingProjectException("Company Not Found");
        }

        return companyDtoList;

    }

    @Override
    public CompanyDto findByName(String name) throws AccountingProjectException {

        if (isNotRoot())
            throw new AccountingProjectException("You Are Not Root");

        Company company;

        try {
            company = companyRepository.findAll().stream()
                    .filter(savedCompany -> savedCompany.getTitle().equalsIgnoreCase(name))
                    .findFirst().get();

        } catch (Exception e) {
            throw new AccountingProjectException("Company Not Found");
        }


        return mapperUtil.convert(company, new CompanyDto());


    }

    @Override
    public void save(CompanyDto companyDto) throws AccountingProjectException {
        if (isNotRoot())
            throw new AccountingProjectException("You Are Not Root");

        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(mapperUtil.convert(companyDto, new Company()));

    }

    @Override
    public void delete(Long companyId) throws AccountingProjectException {

        if (isNotRoot())
            throw new AccountingProjectException("You Are Not Root");

        throw new AccountingProjectException("DELETE METHOD NOT IMPLEMENTED");
    }

    @Override
    public void update(CompanyDto companyDto, Long companyId) throws AccountingProjectException {

        if (isNotRoot())
            throw new AccountingProjectException("You Are Not Root");

        Company savedCompany ;

        try {
            savedCompany = companyRepository.findById(companyId).get();
        }catch (Exception e){
            throw new AccountingProjectException("Company Not Found");
        }

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
    public void activate(Long companyId) throws AccountingProjectException {
        Company company ;

        try {
            company = companyRepository.findById(companyId).get();
        }catch (Exception e){
            throw new AccountingProjectException("Company Not Found");
        }

        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivate(Long companyId) throws AccountingProjectException {
        Company company ;

        try {
            company = companyRepository.findById(companyId).get();
        }catch (Exception e){
            throw new AccountingProjectException("Company Not Found");
        }
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }


    private boolean isNotRoot() {
        return !userService.getCurrentUser().getRole().getDescription().equalsIgnoreCase("Root");
    }


}
