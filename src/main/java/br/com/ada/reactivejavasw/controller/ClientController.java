package br.com.ada.reactivejavasw.controller;

import br.com.ada.reactivejavasw.dto.ClientDTO;
import br.com.ada.reactivejavasw.dto.ResponseDTO;
import br.com.ada.reactivejavasw.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(description = "Create a client",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    public Mono<ResponseDTO> create(@RequestBody ClientDTO clientDTO) {
        return this.clientService.create(clientDTO);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(description = "Find all clients")
    public Flux<ResponseDTO<ClientDTO>> getAll() {
        return this.clientService.getAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(description = "Find by ID of client")
    public Mono<ResponseDTO> findById(@PathVariable("id") String id) {
        return this.clientService.findById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Update a client")
    public Mono<ResponseDTO<ClientDTO>> update(@PathVariable("id") String id, @RequestBody ClientDTO updatedClientDTO) {
        return this.clientService.update(id, updatedClientDTO);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Delete a client")
    public Mono<ResponseDTO<Object>> delete(@PathVariable("id") String id) {
        return this.clientService.delete(id);
    }
}

