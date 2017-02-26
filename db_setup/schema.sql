CREATE DATABASE bookingdb;

CREATE TABLE bookingdb.employee
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    telephone VARCHAR(255) NOT NULL
);
CREATE TABLE bookingdb.room
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL,
    end_time TIME NOT NULL,
    name VARCHAR(255),
    start_time TIME NOT NULL
);
CREATE TABLE bookingdb.reservation
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    duration TIME NOT NULL,
    end_date DATETIME,
    start_date DATETIME NOT NULL,
    submission_date DATETIME,
    employee_id BIGINT(20) NOT NULL,
    room_id BIGINT(20) NOT NULL,
    CONSTRAINT FKoq2iacdgt8val8v26jn0iw83q FOREIGN KEY (employee_id) REFERENCES bookingdb.employee (id),
    CONSTRAINT FKm8xumi0g23038cw32oiva2ymw FOREIGN KEY (room_id) REFERENCES bookingdb.room (id)
);
CREATE INDEX FKm8xumi0g23038cw32oiva2ymw ON bookingdb.reservation (room_id);
CREATE INDEX FKoq2iacdgt8val8v26jn0iw83q ON bookingdb.reservation (employee_id);