CREATE TABLE cd.facilities
    (
       facid integer NOT NULL,
       name character varying(100) NOT NULL,
       membercost real NOT NULL,
       guestcost real NOT NULL,
       initialoutlay real NOT NULL,
       monthlymaintenance real NOT NULL,
       CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );

CREATE TABLE cd.bookings
        (
           bookid integer NOT NULL,
           facid integer NOT NULL,
           memid integer NOT NULL,
           starttime timestamp NOT NULL,
           slots integer NOT NULL,
           CONSTRAINT bookings_pk PRIMARY KEY (bookid),
           CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid)
        );

  INSERT INTO facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) VALUES
  (0, 'Tennis Court 1', 5, 25, 10000, 200),
  (1, 'Tennis Court 2', 5, 25, 8000, 200),
  (2, 'Badminton Court', 0, 15.5, 4000, 50),
  (3, 'Table Tennis', 0, 5, 320, 10),
  (4, 'Massage Room 1', 35, 80, 4000, 3000);

INSERT INTO members (memid, surname, firstname, address, zipcode, telephone, recommendedby, joindate) VALUES
(0, 'GUEST', 'GUEST', 'GUEST', 0, '(000) 000-0000', NULL, '2012-07-01 00:00:00'),
(1, 'Smith', 'Darren', '8 Bloomsbury Close, Boston', 4321, '555-555-5555', NULL, '2012-07-02 12:02:05'),
(2, 'Smith', 'Tracy', '8 Bloomsbury Close, New York', 4321, '555-555-5555', NULL, '2012-07-02 12:08:23'),
(3, 'Rownam', 'Tim', '23 Highway Way, Boston', 23423, '(844) 693-0723', NULL, '2012-07-03 09:32:15'),
(4, 'Joplette', 'Janice', '20 Crossing Road, New York', 234, '(833) 942-4710', 1, '2012-07-03 10:25:05'),
(5, 'Butters', 'Gerald', '1065 Huntingdon Avenue, Boston', 56754, '(844) 078-4130', 1, '2012-07-09 10:44:09'),
(6, 'Tracy', 'Burton', '3 Tunisia Drive, Boston', 45678, '(822) 354-9973', NULL, '2012-07-15 08:52:55');

SELECT b.facid, SUM(b.slots) AS "Total Slots"
FROM cd.bookings AS b
WHERE b.starttime >= '2012-09-01' AND b.starttime < '2012-10-01'
GROUP BY b.facid
ORDER BY "Total Slots";