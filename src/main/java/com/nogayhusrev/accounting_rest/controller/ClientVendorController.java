package com.nogayhusrev.accounting_rest.controller;


import com.nogayhusrev.accounting_rest.dto.ClientVendorDto;
import com.nogayhusrev.accounting_rest.dto.ResponseWrapper;
import com.nogayhusrev.accounting_rest.service.AddressService;
import com.nogayhusrev.accounting_rest.service.ClientVendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientVendors")
@Tag(name = "Client-Vendor Controller", description = "Client-Vendor API")

public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final AddressService addressService;

    public ClientVendorController(ClientVendorService clientVendorService, AddressService addressService) {
        this.clientVendorService = clientVendorService;
        this.addressService = addressService;
    }

    @GetMapping
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read all Client-Vendors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Client-Vendors (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list() throws Exception {

        List<ClientVendorDto> clientVendorDtoList = clientVendorService.findAll();
        return ResponseEntity.ok(new ResponseWrapper("Client-Vendors are successfully retrieved", clientVendorDtoList, HttpStatus.OK));
    }

    @GetMapping("/{clientVendorId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Read one Client-Vendor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Client-Vendor (OK)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> list(@PathVariable Long clientVendorId) throws Exception {
        ClientVendorDto clientVendorDto = clientVendorService.findById(clientVendorId);
        return ResponseEntity.ok(new ResponseWrapper("Client-Vendor successfully retrieved", clientVendorDto, HttpStatus.OK));
    }


    @PostMapping
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Create a Client-Vendor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Client-Vendor (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> create(@RequestBody ClientVendorDto clientVendorDto) throws Exception {

        if (clientVendorService.isExist(clientVendorDto)) {
            throw new Exception("This Client-Vendor name already exists");
        }

        clientVendorService.save(clientVendorDto);
        ClientVendorDto savedClientVendor = clientVendorService.findByName(clientVendorDto.getClientVendorName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Client-Vendor successfully created", savedClientVendor, HttpStatus.CREATED));
    }


    @PutMapping("/{clientVendorId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Update a Client-Vendor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Client-Vendor (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> update(@RequestBody ClientVendorDto clientVendorDto, @PathVariable Long clientVendorId) throws Exception {


        if (clientVendorService.isExist(clientVendorDto, clientVendorId)) {
            throw new Exception("This Client-Vendor name already exists");
        }

        clientVendorService.update(clientVendorDto, clientVendorId);
        ClientVendorDto updatedClientVendor = clientVendorService.findById(clientVendorId);

        return ResponseEntity.ok(new ResponseWrapper("Client-Vendor successfully updated", updatedClientVendor, HttpStatus.OK));
    }

    @DeleteMapping("/{clientVendorId}")
    @RolesAllowed({"Manager", "Employee"})
    @Operation(summary = "Delete a Client-Vendor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Client-Vendor (CREATED)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long clientVendorId) throws Exception {


        clientVendorService.delete(clientVendorId);
        return ResponseEntity.ok(new ResponseWrapper("Client-Vendor successfully deleted", HttpStatus.OK));
    }

}
