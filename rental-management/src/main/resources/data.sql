INSERT IGNORE INTO car_rental.roles (name) VALUES
	 ('ROLE_ADMIN'),
	 ('ROLE_RENTALER'),
	 ('ROLE_USER');
	 
INSERT IGNORE INTO car_rental.location(id, city_name) values (1, "Sài Gòn");

INSERT IGNORE INTO car_rental.category(id,name) values(1,"Bất động sản"), (2,"Phòng trọ"), (3,"Chung cư mini")