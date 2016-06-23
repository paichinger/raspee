package com.topdesk.raspee.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity( name = "sitzung" )
@NoArgsConstructor
@Getter
@Setter
public class Sitzung {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="duration")
	private long duration;
	
	@Column(name="creation_date")
	private Date creationDate;
	
}
