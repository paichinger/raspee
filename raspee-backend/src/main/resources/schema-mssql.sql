CREATE TABLE sitzung(
	id BIGINT NOT NULL IDENTITY (1,1),
	duration BIGINT,
	creation_date DATE DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
)