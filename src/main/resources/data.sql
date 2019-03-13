INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_SERVICE');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (-1, 'dan@dan.com', 'danut', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567890', null, 'danut', 1, null);

INSERT INTO service_details(id, address, cui, name) VALUES (-1, 'p-aci', 123456, 'repara tot');

INSERT INTO users(id, email, name, password, phone_number, rating, username, role_id, service_details_id)
	VALUES (0, 'service@service.com', 'service', '$2a$10$/S2JpiypueclFU3N.TuWle9BOwTOo5ydm/DoL8XHniNpRiTMwF22W', '1234567891', null, 'service', 2, -1);

INSERT INTO cars(id, make, model, year, user_id) VALUES (-1, 'audi', 'a1', 2000, -1);
INSERT INTO cars(id, make, model, year, user_id) VALUES (0, 'audi', 'a2', 2001, -1);

INSERT INTO job(id, description, location, parts_type, "timestamp", service_id, user_id) VALUES (-1, 'stricat', 'in zona', 1, current_timestamp, null, -1);
INSERT INTO job(id, description, location, parts_type, "timestamp", service_id, user_id) VALUES (-2, 'stricat1', 'in zona1', 1, current_timestamp, null, -1);
INSERT INTO job(id, description, location, parts_type, "timestamp", service_id, user_id) VALUES (0, 'stricat2', 'in zona2', 1, current_timestamp, null, -1);

INSERT INTO offers(accepted, cost, description, duration, "timestamp", job_id, user_id)
  VALUES (false, 100, 'repede si bine', 100, current_timestamp, -1, 0);
INSERT INTO offers(accepted, cost, description, duration, "timestamp", job_id, user_id)
  VALUES (false, 200, 'repede si mai bine', 100, current_timestamp, -2, 0);
INSERT INTO offers(accepted, cost, description, duration, "timestamp", job_id, user_id)
  VALUES (false, 300, 'repede si prost', 100, current_timestamp, -1, 0);
INSERT INTO offers(accepted, cost, description, duration, "timestamp", job_id, user_id)
  VALUES (false, 400, 'incet si prost', 200, current_timestamp, -0, 0);