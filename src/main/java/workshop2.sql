CREATE DATABASE workshop2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
# ---------------------------------------
CREATE TABLE users(
    id INT(11) NOT NULL auto_increment ,
    email VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL UNIQUE ,
    username VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL ,
    password VARCHAR(60) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
# --------------------------------------
DESCRIBE users;
DROP TABLE users;
# Dodawanie uzytkownika ----------------
INSERT INTO users(id, email, username, password) VALUES (NULL,'tata','tatusiek','mojehaslo');
# Zmiana danych ------------------------
UPDATE users SET email= 'takisobie' WHERE id=1;
# Pobieranie po id ---------------------
SELECT * FROM users WHERE id=1;
# Usuwanie po id -----------------------
DELETE  FROM users;
# Pobieranie wszystkich uzytkownikow ---
SELECT * FROM users;
DROP DATABASE workshop2;
SHOW TABLES FROM workshop2;