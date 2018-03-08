package com.topdesk.raspee.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity( name = "sitzung" )
@Data
public class Sitzung {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="duration")
	private long duration;
	
	@Column(name="creation_date")
	private Date creationDate;
	
}
