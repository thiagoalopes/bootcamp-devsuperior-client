package br.dev.thiagoalopes.clients.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.thiagoalopes.clients.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
