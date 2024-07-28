CREATE TABLE cd.bookings
    (
       bookid integer NOT NULL,
       facid integer NOT NULL,
       memid integer NOT NULL,
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       CONSTRAINT bookings_pk PRIMARY KEY (bookid)
    );
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

SELECT facid, extract(MONTH FROM starttime) AS MONTH,
       sum(slots) AS slots
FROM cd.bookings
WHERE starttime >= '2012-01-01'
  AND starttime < '2013-01-01'
GROUP BY rollup(facid, MONTH)
ORDER BY facid, MONTH;