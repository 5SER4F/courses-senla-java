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
       CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );

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

select firstname, surname, hours, rank() over (order by hours desc) from
	(select firstname, surname,
		((sum(bks.slots)+10)/20)*10 as hours

		from cd.bookings bks
		inner join cd.members mems
			on bks.memid = mems.memid
		group by mems.memid
	) as subq
order by rank, surname, firstname;