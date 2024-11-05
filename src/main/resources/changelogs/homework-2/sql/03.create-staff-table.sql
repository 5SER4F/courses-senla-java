CREATE TABLE IF NOT EXISTS staff (
  id uuid PRIMARY KEY,
  firstname varchar NOT NULL,
  surname varchar NOT NULL,
  nickname varchar NOT NULL,
  birth_date date NOT NULL
);

--Создание первого сотрудника
INSERT INTO "user" (id,role, username, password,email, account_status) VALUES
('10766e95-f75a-499e-80c5-0e220e99e616','STAFF', 'SUPERADMIN', '$2a$10$TobvPFFDhK9kLP.TWWQ4p.G6ZBKzmtLLHjn4GO2SLr0a7ZnGwOr3.', 'superemail@gmail.com', 'CREATED');

INSERT INTO staff (id,firstname, surname, nickname, birth_date) VALUES
('10766e95-f75a-499e-80c5-0e220e99e616','SUPERFIRSTNAME', 'SUPERSURNAME', 'SUPERSURNICKNAME', '1995-07-16');