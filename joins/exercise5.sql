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

CREATE TABLE cd.members
    (
       memid integer NOT NULL,
       surname character varying(100) NOT NULL,
       firstname character varying(100) NOT NULL,
       address character varying(100) NOT NULL,
       zipcode integer NOT NULL,
       telephone character varying(100) NOT NULL,
       recommendedby integer,
       joindate timestamp NOT NULL,
       CONSTRAINT members_pk PRIMARY KEY (memid),
       CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
            REFERENCES cd.members(memid) ON DELETE SET NULL
    );

CREATE TABLE cd.bookings
    (
       bookid integer NOT NULL,
       facid integer NOT NULL,
       memid integer NOT NULL,
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       CONSTRAINT bookings_pk PRIMARY KEY (bookid),
       CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
       CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
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

INSERT INTO bookings (bookid, facid, memid, starttime, slots) VALUES
(0, 3, 1, '2012-07-03 11:00:00', 2),
(1, 1, 1, '2012-07-03 08:00:00', 1),
(2, 4, 0, '2012-07-03 18:00:00', 2),
(3, 0, 1, '2012-07-03 19:00:00', 2),
(4, 3, 1, '2012-08-03 10:00:00', 1),
(5, 2, 1, '2012-08-03 15:00:00', 1),
(6, 0, 2, '2012-08-04 09:00:00', 3),
(7, 1, 2, '2012-08-04 15:00:00', 3),
(8, 2, 3, '2012-09-04 13:30:00', 2),
(9, 1, 0, '2012-10-04 15:00:00', 2),
(10, 4, 0, '2012-11-04 17:30:00', 3),
(11, 4, 0, '2012-12-04 12:30:00', 2),
(12, 2, 0, '2013-01-04 14:00:00', 1),
(13, 0, 1, '2013-02-04 15:30:00', 2),
(14, 1, 2, '2013-03-04 14:00:00', 2);

  SELECT o.member, o.facility
  FROM (SELECT DISTINCT(m.firstname, m.surname, names.facility), (m.firstname || ' ' || m.surname) as member,  names.facility
  FROM cd.members as m
  INNER JOIN
    (SELECT f.name AS facility, b.memid AS memid
	 FROM cd.bookings AS b
	 INNER JOIN cd.facilities as f ON b.facid = f.facid) as names
	 ON m.memid = names.memid
	 WHERE names.facility like 'Tennis Court%'
  ORDER BY (m.firstname, m.surname, names.facility)) as o;
