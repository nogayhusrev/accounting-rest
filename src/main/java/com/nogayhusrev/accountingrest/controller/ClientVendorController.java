package com.nogayhusrev.accountingrest.controller;


import com.nogayhusrev.accountingrest.dto.CategoryDto;
import com.nogayhusrev.accountingrest.dto.ClientVendorDto;
import com.nogayhusrev.accountingrest.dto.ResponseWrapper;
import com.nogayhusrev.accountingrest.service.AddressService;
import com.nogayhusrev.accountingrest.service.ClientVendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final AddressService addressService;

    public ClientVendorController(ClientVendorService clientVendorService, AddressService addressService) {
        this.clientVendorService = clientVendorService;
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> list() throws Exception {

        List<ClientVendorDto> clientVendorDtoList = clientVendorService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Client-Vendors are successfully retrieved", clientVendorDtoList, HttpStatus.OK));
    }

    @GetMapping("/{clientVendorId}")
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long clientVendorId) throws Exception {
        ClientVendorDto clientVendorDto = clientVendorService.findById(clientVendorId);
        return ResponseEntity.ok(new ResponseWrapper("Client-Vendor successfully retrieved", clientVendorDto, HttpStatus.OK));
    }


    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody ClientVendorDto clientVendorDto) throws Exception {

        if (clientVendorService.isExist(clientVendorDto)) {
            throw new Exception("This Client-Vendor name already exists");
        }

        clientVendorService.save(clientVendorDto);
        ClientVendorDto savedClientVendor = clientVendorService.findByName(clientVendorDto.getClientVendorName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Client-Vendor successfully created", savedClientVendor, HttpStatus.CREATED));
    }


    @PutMapping("/{clientVendorId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody ClientVendorDto clientVendorDto, @PathVariable Long clientVendorId) throws Exception {


        if (clientVendorService.isExist(clientVendorDto, clientVendorId)) {
            throw new Exception("This Client-Vendor name already exists");
        }

        clientVendorService.update(clientVendorDto, clientVendorId);
        ClientVendorDto updatedClientVendor = clientVendorService.findById(clientVendorId);

        return ResponseEntity.ok(new ResponseWrapper("Client-Vendor successfully updated", updatedClientVendor, HttpStatus.OK));
    }

    @DeleteMapping("/{clientVendorId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long clientVendorId) throws Exception {


        clientVendorService.delete(clientVendorId);
        return ResponseEntity.ok(new ResponseWrapper("Client-Vendor successfully deleted", HttpStatus.OK));
    }

}
