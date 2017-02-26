CREATE USER 'bookinguser'@'localhost' IDENTIFIED BY 'bookingpass';
CREATE USER 'bookinguser'@'%' IDENTIFIED BY 'bookingpass';
GRANT ALL PRIVILEGES ON bookingdb.* TO 'bookinguser'@'localhost';
GRANT ALL PRIVILEGES ON bookingdb.* TO 'bookinguser'@'%';
