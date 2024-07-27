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

INSERT INTO facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) VALUES
(0, 'Tennis Court 1', 5, 25, 10000, 200),
(1, 'Tennis Court 2', 5, 25, 8000, 200),
(2, 'Badminton Court', 0, 15.5, 4000, 50),
(3, 'Table Tennis', 0, 5, 320, 10),
(5, 'Massage Room 1', 35, 80, 4000, 3000);

SELECT *
  FROM cd.facilities
  WHERE facid IN (1,5);