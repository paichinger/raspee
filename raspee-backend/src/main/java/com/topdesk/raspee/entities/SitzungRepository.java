package com.topdesk.raspee.entities;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SitzungRepository extends CrudRepository<Sitzung, UUID>  {
	
	List<Sitzung> findTop10ByOrderByDurationDesc();

}
