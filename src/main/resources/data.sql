INSERT INTO user (id, email, is_blocked, name, password, role)
SELECT 1, 'admin@gmail.com', 0, 'admin', '$2a$10$MsVZdCMCepXzSz5V9Ki.7ub29fEspdVERNxebBnqPqSO5QzFO6r8i', 'ADMIN'
    FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM user WHERE email = 'admin@gmail.com'
);

INSERT INTO user (id, email, is_blocked, name, password, role)
SELECT 2, 'testadmin@gmail.com', 0, 'testadmin', '$2a$10$YmGIa0wCSsD7ABq9q4YSo.E/Tg.YxOszwKGwXCreWDCvwFnERh80u', 'ADMIN'
    FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM user WHERE email = 'testadmin@gmail.com'
);