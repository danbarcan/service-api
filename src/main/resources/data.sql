INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_SERVICE');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

insert into categories(id, description) values (0, 'cat1');
insert into categories(id, description) values (1, 'cat2');

INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-1, 'dan_claw20@yahoo.com', 'danut', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567890', null, 'danut', 1, null);
INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-3, 'fortza_madrid@yahoo.com', 'danut1', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567890', null, 'danut1', 1, null);

INSERT INTO service_details(id, address, cui, name) VALUES (-1, 'p-aci', 123456, 'repara tot');
INSERT INTO service_details(id, address, cui, name) VALUES (-2, 'p-aci2', 123456, 'repara tot2');

INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (0, 'dan.barcan1994@gmail.com', 'service', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567891', null, 'service', 2, -1);
INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-2, 'service1@service.com', 'service1', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567891', null, 'service1', 2, -2);

INSERT INTO cars(id, make, model, year, user_id) VALUES (-1, 'audi', 'a1', 2000, -1);
INSERT INTO cars(id, make, model, year, user_id) VALUES (0, 'audi', 'a2', 2001, -1);

INSERT INTO job(id, description, parts_type, service_id, user_id, car_id, LAT, LNG) VALUES (-1, 'stricat', 1, null, -1, -1, 42.323, 32.32);
INSERT INTO job(id, description, parts_type, service_id, user_id, car_id, LAT, LNG) VALUES (-2, 'stricat1', 1, null, -1, -1, 42.323, 32.32);
INSERT INTO job(id, description, parts_type, service_id, user_id, car_id, LAT, LNG) VALUES (0, 'stricat2', 1, null, -1, 0, 42.323, 32.32);

INSERT INTO offers(accepted, cost, description, duration, job_id, user_id)
  VALUES (false, 100, 'repede si bine', 100, -1, 0);
INSERT INTO offers(accepted, cost, description, duration, job_id, user_id)
  VALUES (false, 200, 'repede si mai bine', 100, -2, 0);
INSERT INTO offers(accepted, cost, description, duration, job_id, user_id)
  VALUES (false, 300, 'repede si prost', 100, -1, 0);
--INSERT INTO offers(accepted, cost, description, duration, job_id, user_id) VALUES (false, 400, 'incet si prost', 200, 0, 0);