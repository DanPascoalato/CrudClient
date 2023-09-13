package br.com.ada.reactivejavasw.service;

import br.com.ada.reactivejavasw.dto.ClientDTO;
import br.com.ada.reactivejavasw.dto.ResponseDTO;
import br.com.ada.reactivejavasw.model.Client;
import br.com.ada.reactivejavasw.model.converter.ClientConverter;
import br.com.ada.reactivejavasw.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ClientService {

    @Autowired
    private ClientConverter clientConverter;

    @Autowired
    private ClientRepository clientRepository;

    public Mono<ResponseDTO> create(ClientDTO clientDTO) {
        Client client = this.clientConverter.toClient(clientDTO);
        Mono<Client> clientMono = this.clientRepository.save(client);

        return clientMono
                .map(clientDocument -> new ResponseDTO("Cliente cadastrado com sucesso!",
                        this.clientConverter.toClientDTO(clientDocument),
                        clientDocument.getId(), // Adicione o ID aqui
                        LocalDateTime.now()))
                .onErrorReturn(new ResponseDTO("Erro ao cadastrar cliente",
                        new ClientDTO(),
                        null, // Defina o ID como null em caso de erro
                        LocalDateTime.now()));
    }


    public Flux<ResponseDTO<ClientDTO>> getAll() {
        Flux<Client> clientFlux = this.clientRepository.findAll();
        return clientFlux
                .map(client -> new ResponseDTO("Listagem de clientes retornada com sucesso!",
                        this.clientConverter.toClientDTO(client),
                        client.getId(), // Inclua o ID aqui
                        LocalDateTime.now()));
    }

    public Mono<ResponseDTO> findById(String id) {
        Mono<Client> clientMono = this.clientRepository.findById(id);
        return clientMono
                .map(client -> new ResponseDTO("Cliente encontrado com sucesso!",
                        this.clientConverter.toClientDTO(client),
                        client.getId(), // Inclua o ID aqui
                        LocalDateTime.now()))
                .defaultIfEmpty(new ResponseDTO<>("Cliente não encontrado.",
                        new ClientDTO(),
                        null, // Defina o ID como null se o cliente não for encontrado
                        LocalDateTime.now()));
    }

    public Mono<ResponseDTO<ClientDTO>> update(String id, ClientDTO updatedClientDTO) {
        return this.clientRepository.findById(id)
                .flatMap(existingClient -> {
                    existingClient.setName(updatedClientDTO.getName());
                    existingClient.setAge(updatedClientDTO.getAge());
                    existingClient.setEmail(updatedClientDTO.getEmail());

                    return this.clientRepository.save(existingClient);
                })
                .map(updatedClient -> new ResponseDTO<>("Cliente atualizado com sucesso!",
                        this.clientConverter.toClientDTO(updatedClient),
                        updatedClient.getId(), // Inclua o ID aqui
                        LocalDateTime.now()))
                .defaultIfEmpty(new ResponseDTO<>("Cliente não encontrado para atualização.",
                        new ClientDTO(),
                        null, // Defina o ID como null se o cliente não for encontrado
                        LocalDateTime.now()));
    }

    public Mono<ResponseDTO<Object>> delete(String id) {
        return this.clientRepository.findById(id)
                .flatMap(existingClient ->
                        this.clientRepository.delete(existingClient)
                                .then(Mono.just(new ResponseDTO<>("Cliente excluído com sucesso.", null, null, LocalDateTime.now())))
                )
                .defaultIfEmpty(new ResponseDTO<>("Cliente não encontrado para exclusão.",
                        null,
                        null,
                        LocalDateTime.now()));
    }


}

