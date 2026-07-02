
INSERT INTO department (name, category) VALUES
('Produkcija', 'Umjetnički sektor'),
('Administracija', 'Uprava'),
('Tehnička ekipa', 'Pomoćno osoblje');

INSERT INTO location (country, city, address, description) VALUES
('Srbija', 'Beograd', 'Kralja Petra 12', 'Glavno kazalište u centru Beograda.'),
('Hrvatska', 'Zagreb', 'Ilica 45', 'Popularno kazalište za dramske izvedbe.'),
('Crna Gora', 'Podgorica', 'Njegoševa 7', 'Savremena scena za mlade umjetnike.'),
('Bosna i Hercegovina', 'Sarajevo', 'Maršala Tita 22', 'Kulturni centar s redovitim kazališnim programom.'),
('Hrvatska', 'Split', 'Obala hrvatskog narodnog preporoda 25', 'Otvorena pozornica uz more za ljetne predstave.');

INSERT INTO project (title, description, visible, status, start_date, end_date, location_id) VALUES
('Kazališna sezona 2025', 'Otvaranje nove kazališne sezone s nizom premijera.', true, 'IN_PROGRESS', '2025-09-01', '2025-12-15', 1),
('Festival alternativnog teatra', 'Međunarodni festival s eksperimentalnim predstavama.', true, 'PLANNED', '2025-06-10', '2025-06-20', 2),
('Obnova scene', 'Renovacija glavne pozornice i rasvjete.', false, 'COMPLETED', '2024-01-15', '2024-06-30', 3);