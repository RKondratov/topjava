DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES (to_timestamp('30.01.2020 10:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Завтрак', 500, 100000),
       (to_timestamp('30.01.2020 13:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Обед', 1000, 100000),
       (to_timestamp('30.01.2020 20:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Ужин', 500, 100000),
       (to_timestamp('31.01.2020 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Еда на граничное значение', 100, 100000),
       (to_timestamp('31.01.2020 10:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Завтрак', 1000, 100000),
       (to_timestamp('31.01.2020 13:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Обед', 500, 100000),
       (to_timestamp('31.01.2020 20:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'Ужин', 410, 100000);
