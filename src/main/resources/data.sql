--GENRES
--INSERT INTO genres (name, created_at, updated_at) -- postgres
MERGE INTO genres (name, created_at, updated_at) KEY (name) -- h2
VALUES ('Comedy', '2025-02-20', '2025-02-20'),
    ('Drama', '2025-02-20', '2025-02-20'),
    ('Cartoon', '2025-02-20', '2025-02-20'),
    ('Thriller', '2025-02-20', '2025-02-20'),
    ('Documental', '2025-02-20', '2025-02-20'),
    ('Action', '2025-02-20', '2025-02-20');
--ON CONFLICT (name) DO NOTHING; -- postgres

--MPA_RATING
--INSERT INTO mpas (name, created_at, updated_at) -- postgres
MERGE INTO mpas (name, created_at, updated_at) KEY (name) -- h2
VALUES ('G', '2025-02-20', '2025-02-20'),
    ('PG', '2025-02-20', '2025-02-20'),
    ('PG13', '2025-02-20', '2025-02-20'),
    ('R', '2025-02-20', '2025-02-20'),
    ('NC17', '2025-02-20', '2025-02-20');
--ON CONFLICT (name) DO NOTHING; -- postgres

