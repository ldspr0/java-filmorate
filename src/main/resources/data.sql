--GENRES
--INSERT INTO genres (name, created_at, updated_at) -- postgres
MERGE INTO genres (name, created_at, updated_at) KEY (name) -- h2
VALUES ('Комедия', '2025-02-20', '2025-02-20'),
    ('Драма', '2025-02-20', '2025-02-20'),
    ('Мультфильм', '2025-02-20', '2025-02-20'),
    ('Триллер', '2025-02-20', '2025-02-20'),
    ('Документальный', '2025-02-20', '2025-02-20'),
    ('Боевик', '2025-02-20', '2025-02-20');
--ON CONFLICT (name) DO NOTHING; -- postgres

--MPA_RATING
--INSERT INTO mpas (name, created_at, updated_at) -- postgres
MERGE INTO mpas (name, created_at, updated_at) KEY (name) -- h2
VALUES ('G', '2025-02-20', '2025-02-20'),
    ('PG', '2025-02-20', '2025-02-20'),
    ('PG-13', '2025-02-20', '2025-02-20'),
    ('R', '2025-02-20', '2025-02-20'),
    ('NC-17', '2025-02-20', '2025-02-20');
--ON CONFLICT (name) DO NOTHING; -- postgres

