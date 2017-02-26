INSERT INTO bookingdb.employee (email, first_name, last_name, telephone) VALUES ('John@mail.com', 'John', 'Smith', '+02938451345');
INSERT INTO bookingdb.employee (email, first_name, last_name, telephone) VALUES ('Garry@mail.com', 'Garry', 'Johnson', '+01948556375');

INSERT INTO bookingdb.room (code, end_time, name, start_time) VALUES ('523C', '22:00:00', 'Bahamas room', '09:00:00');
INSERT INTO bookingdb.room (code, end_time, name, start_time) VALUES ('728A', '22:00:00', 'Caribbean room', '09:00:00');

INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:35:00', '2017-03-13 10:00:00', '2017-02-21 02:41:25', 1, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:30:00', '2017-03-13 11:00:00', '2017-02-21 02:41:25', 2, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:15:00', '2017-03-13 11:35:00', '2017-02-21 02:41:25', 1, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('01:00:00', '2017-03-13 16:00:00', '2017-02-21 02:41:25', 2, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:15:00', '2017-03-13 18:35:00', '2017-02-21 02:41:25', 1, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:35:00', '2017-03-13 10:00:00', '2017-02-21 02:41:25', 1, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:30:00', '2017-03-13 11:00:00', '2017-02-21 02:41:25', 1, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:15:00', '2017-03-13 11:35:00', '2017-02-21 02:41:25', 2, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('01:00:00', '2017-03-13 16:00:00', '2017-02-21 02:41:25', 1, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:15:00', '2017-03-13 18:35:00', '2017-02-21 02:41:25', 1, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:30:00', '2225-12-22 11:20:02', '2017-02-26 05:39:59', 2, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:30:00', '2007-12-22 11:20:02', '2017-02-26 05:40:14', 1, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:30:00', '2007-12-22 10:20:02', '2017-02-26 05:40:20', 1, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('00:30:00', '2007-12-22 14:20:02', '2017-02-26 05:40:25', 2, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('02:30:00', '2017-04-12 10:10:02', '2017-02-26 05:42:29', 1, 1);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('02:30:00', '2017-04-12 16:10:02', '2017-02-26 05:42:44', 2, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('02:30:00', '2016-04-12 16:10:02', '2017-02-26 05:43:01', 1, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('04:30:00', '2016-04-12 10:10:02', '2017-02-26 05:43:10', 2, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('02:30:00', '2017-03-10 12:10:02', '2017-02-26 05:43:35', 1, 2);
INSERT INTO bookingdb.reservation (duration, start_date, submission_date, employee_id, room_id) VALUES ('02:30:00', '2017-03-10 18:10:02', '2017-02-26 05:43:40', 1, 2);
