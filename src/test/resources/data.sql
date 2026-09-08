INSERT INTO departement (id, code, nom) VALUES (900, '34', 'Herault');
INSERT INTO departement (id, code, nom) VALUES (901, '75', 'Paris');

INSERT INTO ville (id, nom, nb_habs, id_dept) VALUES (900, 'Montpellier', 300000, 900);
INSERT INTO ville (id, nom, nb_habs, id_dept) VALUES (901, 'Sete', 45000, 900);
INSERT INTO ville (id, nom, nb_habs, id_dept) VALUES (902, 'Beziers', 80000, 900);
INSERT INTO ville (id, nom, nb_habs, id_dept) VALUES (903, 'Paris', 2000000, 901);