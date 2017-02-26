CREATE USER 'bookinguser'@'localhost' IDENTIFIED BY 'bokingpass';
CREATE USER 'bookinguser'@'%' IDENTIFIED BY 'bokingpass';
GRANT ALL PRIVILEGES ON bookingdb.* TO 'bookinguser'@'localhost';
GRANT ALL PRIVILEGES ON bookingdb.* TO 'bookinguser'@'%';