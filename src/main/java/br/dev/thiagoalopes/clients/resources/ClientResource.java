package br.dev.thiagoalopes.clients.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.dev.thiagoalopes.clients.dto.ClientDTO;
import br.dev.thiagoalopes.clients.resources.exceptions.ResourceArgumentException;
import br.dev.thiagoalopes.clients.services.ClientService;

@RestController
@RequestMapping("clients")
public class ClientResource {
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping
	public ResponseEntity<Page<ClientDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction
			) {
		
		try {
			PageRequest pageRequest = PageRequest
					.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
			
			return ResponseEntity.ok(this.clientService.findAll(pageRequest));
		} catch (IllegalArgumentException e) {
			throw new ResourceArgumentException("Wrong argument", e);
		}
		
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(this.clientService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<ClientDTO> store(@RequestBody ClientDTO ClientDTO) {
		
		ClientDTO = this.clientService.store(ClientDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(ClientDTO.getId()).toUri();

		return ResponseEntity.created(uri)
				.body(ClientDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ClientDTO> store(@PathVariable Long id, @RequestBody ClientDTO ClientDTO) {
		return ResponseEntity.ok(this.clientService.update(id, ClientDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> store(@PathVariable Long id) {
		this.clientService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
