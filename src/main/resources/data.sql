INSERT INTO USER (id, email, is_blocked, name, password, role)
SELECT 1, 'admin@gmail.com', 0, 'admin', '$2a$10$MsVZdCMCepXzSz5V9Ki.7ub29fEspdVERNxebBnqPqSO5QzFO6r8i', 'ADMIN'
    FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM USER WHERE email = 'admin@gmail.com'
);