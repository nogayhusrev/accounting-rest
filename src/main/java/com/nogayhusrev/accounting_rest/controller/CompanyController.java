package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.client.AddressClient;
import com.nogayhusrev.accounting_rest.dto.CompanyDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.service.AddressService;
import com.nogayhusrev.accounting_rest.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/companies")
@Tag(name = "COMPANY API", description = "Company CRUD Operations")

public class CompanyController {

    private final CompanyService companyService;
    private final AddressService addressService;
    private final AddressClient addressClient;

    public CompanyController(CompanyService companyService, AddressService addressService, AddressClient addressClient) {
        this.companyService = companyService;
        this.addressService = addressService;
        this.addressClient = addressClient;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {
        List<CompanyDto> companyDtoList = companyService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Companies are successfully retrieved", companyDtoList, HttpStatus.OK));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long companyId) throws Exception {
        CompanyDto companyDto = companyService.findById(companyId);
        return ResponseEntity.ok(new ResponseWrapper("Company successfully retrieved", companyDto, HttpStatus.OK));
    }


    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody CompanyDto companyDto) throws Exception {

        if (companyService.isExist(companyDto)) {
            throw new Exception("This Company title already exists");
        }

        companyService.save(companyDto);
        CompanyDto savedCompany = companyService.findByName(companyDto.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Company successfully created", savedCompany, HttpStatus.CREATED));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody CompanyDto companyDto, @PathVariable Long companyId) throws Exception {


        if (companyService.isExist(companyDto, companyId)) {
            throw new Exception("This Company title already exists");
        }

        companyService.update(companyDto, companyId);
        CompanyDto updatedCompany = companyService.findByName(companyDto.getTitle());
        return ResponseEntity.ok(new ResponseWrapper("Company successfully updated", updatedCompany, HttpStatus.OK));
    }


    @PutMapping("/activate/{companyId}")
    public ResponseEntity<ResponseWrapper> activate(@PathVariable Long companyId) {


        companyService.activate(companyId);

        CompanyDto activatedCompany = companyService.findById(companyId);

        return ResponseEntity.ok(new ResponseWrapper("Company successfully activated", activatedCompany, HttpStatus.OK));
    }


    @PutMapping("/deactivate/{companyId}")
    public ResponseEntity<ResponseWrapper> deactivate(@PathVariable Long companyId) {


        companyService.deactivate(companyId);

        CompanyDto activatedCompany = companyService.findById(companyId);

        return ResponseEntity.ok(new ResponseWrapper("Company successfully deactivated", activatedCompany, HttpStatus.OK));
    }

}
