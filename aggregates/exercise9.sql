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
SELECT f.name,
       sum(slots * CASE WHEN memid = 0 THEN f.guestcost
                       ELSE f.membercost END) AS revenue
FROM cd.bookings b
INNER JOIN cd.facilities f ON b.facid = f.facid
GROUP BY f.name
ORDER BY revenue;