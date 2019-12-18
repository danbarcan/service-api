INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-1, 'dan_claw20@yahoo.com', 'danut', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567890', null, 'danut', 1, null);
INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-3, 'fortza_madrid@yahoo.com', 'danut1', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567890', null, 'danut1', 1, null);

INSERT INTO service_details(id, address, cui, name, lat, lng) VALUES (-1, 'p-aci', 123456, 'repara tot', 0, 0);
INSERT INTO service_details(id, address, cui, name, lat, lng) VALUES (-2, 'p-aci2', 123456, 'repara tot2', 0, 0);

INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (0, 'dan.barcan1994@gmail.com', 'service', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567891', null, 'service', 2, -1);
INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-2, 'service1@service.com', 'service1', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567891', null, 'service1', 2, -2);

-- insert into manufacturers (id, image_url, name, url) values (1, 'https://www.autokarma.ro/image/cache/catalog/Branduri/AIXAM-100x100.png', 'AIXAM', 'https://www.autokarma.ro/piese-auto-aixam');
-- insert into models (id, image_url, name, url, manufacturer_id) values (1, 'https://www.autokarma.ro/image/cache/catalog/Modele/AIXAM/400-145x67.jpg', '400', 'https://www.autokarma.ro/piese-auto-aixam?grupa_modele=400', 1);
-- insert into type_year (id, image_url, name, url, model_id) values (1, 'https://www.autokarma.ro/image/cache/catalog/Modele/AIXAM/400-145x67.jpg', 'AIXAM 40012.1998 - prezent', 'https://www.autokarma.ro/piese-auto-aixam-400-1998', 1);
-- insert into details (id, body, capacity, engine_code, from_date, fuel, power, to_date, type, type_year_id) values (1, 'hatchback', '50 cmc', '', '01.2000', 'benzina', '4 KW - 5 CP', 'prezent', '0.05, 4 KW', '1');
-- insert into details (id, body, capacity, engine_code, from_date, fuel, power, to_date, type, type_year_id) values (2, 'hatchback', '400 cmc', '', '06.2000', 'diesel', '4 KW - 5 CP', '08.2002', '0.4, 4 KW', '1');

INSERT INTO cars(id, details_id, user_id) VALUES (-1, 1, -1);
INSERT INTO cars(id, details_id, user_id) VALUES (0, 2, -1);

INSERT INTO job(id, description, parts_type, service_id, user_id, car_id, LAT, LNG) VALUES (-1, 'stricat', 1, null, -1, -1, 42.323, 32.32);
INSERT INTO job(id, description, parts_type, service_id, user_id, car_id, LAT, LNG) VALUES (-2, 'stricat1', 1, null, -1, -1, 42.323, 32.32);
INSERT INTO job(id, description, parts_type, service_id, user_id, car_id, LAT, LNG) VALUES (0, 'stricat2', 1, null, -1, 0, 42.323, 32.32);

INSERT INTO offers(accepted, cost, description, duration, job_id, user_id)
  VALUES (false, 100, 'repede si bine', 100, -1, 0);
INSERT INTO offers(accepted, cost, description, duration, job_id, user_id)
  VALUES (false, 200, 'repede si mai bine', 100, -2, 0);
INSERT INTO offers(accepted, cost, description, duration, job_id, user_id)
  VALUES (false, 300, 'repede si prost', 100, -1, -2);
--INSERT INTO offers(accepted, cost, description, duration, job_id, user_id) VALUES (false, 400, 'incet si prost', 200, 0, 0);