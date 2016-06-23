package com.topdesk.raspee.entities;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SitzungRepository extends CrudRepository<Sitzung, UUID>  {

}
