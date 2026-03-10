INSERT INTO games (title, studio, genre, platform, release_year, rating, available, created_at, updated_at)
SELECT 'Hades', 'Supergiant Games', 'Roguelike', 'PC', 2020, 9.4, TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Hades');

INSERT INTO games (title, studio, genre, platform, release_year, rating, available, created_at, updated_at)
SELECT 'Sea of Stars', 'Sabotage Studio', 'RPG', 'Nintendo Switch', 2023, 8.8, TRUE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Sea of Stars');

INSERT INTO games (title, studio, genre, platform, release_year, rating, available, created_at, updated_at)
SELECT 'Forza Horizon 5', 'Playground Games', 'Racing', 'Xbox Series', 2021, 9.1, FALSE, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Forza Horizon 5');
