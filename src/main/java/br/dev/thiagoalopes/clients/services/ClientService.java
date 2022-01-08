package br.dev.thiagoalopes.clients.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.dev.thiagoalopes.clients.dto.ClientDTO;
import br.dev.thiagoalopes.clients.entities.Client;
import br.dev.thiagoalopes.clients.repositories.ClientRepository;
import br.dev.thiagoalopes.clients.services.exceptions.DatabaseException;
import br.dev.thiagoalopes.clients.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(PageRequest pageRequest) {
		return this.clientRepository.findAll(pageRequest)
				.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		return new ClientDTO(this.clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found")));
	}
	
	@Transactional
	public ClientDTO store(ClientDTO clientDTO) {
		
		Client client = new Client();
		
		this.copyDtoToEntity(clientDTO, client);
		
		this.clientRepository.save(client);
		
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO ClientDTO) {
		
		Client client = null;
		
		try {
			
			client = this.clientRepository.getById(id);
			this.copyDtoToEntity(ClientDTO, client);
			client = this.clientRepository.save(client);
			
			return new ClientDTO(client);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Resource not found", e);
		}
	}
	
	public void delete(Long id) {
		
		try {
			this.clientRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Resource not found", e);
			
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Database error", e);
		}
	}
	
	private void copyDtoToEntity(ClientDTO clientDTO, Client client) {
		
		client.setName(clientDTO.getName());
		client.setCpf(clientDTO.getCpf());
		client.setIncome(clientDTO.getIncome());
		client.setBirthDate(clientDTO.getBirthDate());
		client.setChildren(clientDTO.getChildren());
	}
}
