/*************
 * Structure *
 *************/
CREATE TABLE lst__location_types(
 id INTEGER NOT NULL,
 label VARCHAR(255) NOT NULL,
 icon VARCHAR(255) NOT NULL,
 lang_code CHAR(2) NOT NULL,
 PRIMARY KEY(id, lang_code)
);

CREATE TABLE location_descriptions(
 id INTEGER NOT NULL,
 description VARCHAR(255) NOT NULL,
 lang_code CHAR(2) NOT NULL,
 PRIMARY KEY(id, lang_code)
);

CREATE TABLE locations(
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 latitude DOUBLE NOT NULL,
 longitude DOUBLE NOT NULL,
 location_type INT NOT NULL REFERENCES lst__location_types(id),
 location_description INT REFERENCES location_descriptions(id)
);

CREATE TABLE lst__genres(
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 label VARCHAR(255) NOT NULL
);

CREATE TABLE artists(
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 name VARCHAR(255),
 genre INTEGER NOT NULL REFERENCES lst__genres(id),
 origin VARCHAR(255) NOT NULL,
 bio TEXT,
 picture_name VARCHAR(255)
);

CREATE TABLE lst__set_types(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,label VARCHAR(255) NOT NULL);

CREATE TABLE sets(
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 begin_date INTEGER NOT NULL,
 end_date INTEGER NOT NULL,
 type INTEGER NOT NULL REFERENCES lst__set_types(id),
 artist INTEGER NOT NULL REFERENCES artists(id)
);

/********
 * Data *
 ********/
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (1, "Toilettes", "marker_toilet", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (1, "Restrooms", "marker_toilet", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (2, "Douches", "marker_shower", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (2, "Showers", "marker_shower", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (3, "Eau potable", "marker_water", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (3, "(Drinkable) Water", "marker_water", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (4, "Restaurants", "marker_restaurant", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (4, "Restaurants", "marker_restaurant", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (5, "Bar", "marker_bar", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (5, "Bar", "marker_bar", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (6, "Scène principale", "marker_stage", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (6, "Main stage", "marker_stage", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (7, "Scène alternative", "marker_stage", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (7, "alternative stage", "marker_stage", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (8, "Stand Hadra", "marker_hadra", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (8, "Hadra's stand", "marker_hadra", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (9, "Camping", "marker_camp", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (9, "Camp", "marker_camp", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (10, "Poste de secours", "marker_rescue", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (10, "Rescue center", "marker_rescue", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (11, "Chillout", "marker_chillout", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (11, "Chillout", "marker_chillout", "en");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (12, "Navette", "marker_shuttle", "fr");
INSERT INTO lst__location_types (id, label, icon, lang_code) VALUES (12, "Shuttle", "marker_shuttle", "en");

INSERT INTO locations (latitude, longitude, location_type) VALUES (45.114371, 5.610150, 8); /* Stand Hadra */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.115044, 5.610304, 6); /* Scène principale */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.118143, 5.610199, 7); /* Scène alternative */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.111134, 5.606830, 1); /* Toilettes */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.113493, 5.608273, 1); /* Toilettes */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.115874, 5.609513, 1); /* Toilettes */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.116741, 5.610014, 1); /* Toilettes */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.116902, 5.609991, 3); /* Eau potable */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.115910, 5.609687, 3); /* Eau potable */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.113413, 5.608430, 3); /* Eau potable */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.113327, 5.608629, 2); /* Douches */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.117330, 5.610073, 4); /* Restaurants */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.116522, 5.609435, 4); /* Restaurants */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.116244, 5.610409, 4); /* Restaurants */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.114238, 5.609780, 5); /* Bar */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.117712, 5.610042, 5); /* Bar */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.116917, 5.609494, 11);/* Chillout */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.114072, 5.608847, 10);/* Poste de secours */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.113572, 5.610338, 9); /* Camping */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.112864, 5.609796, 9); /* Camping */
INSERT INTO locations (latitude, longitude, location_type) VALUES (45.116137, 5.609024, 12); /* Navette */

UPDATE locations SET location_description = 1 WHERE location_type = 12;
UPDATE locations SET location_description = 2 WHERE location_type = 11;

INSERT INTO location_descriptions VALUES (1, "Arrivée de la navette (site)", "fr");
INSERT INTO location_descriptions VALUES (1, "Shuttle destination (site)", "en");
INSERT INTO location_descriptions VALUES (2, "L'éveil des sens", "fr");
INSERT INTO location_descriptions VALUES (2, "Hippie stuff", "en");

INSERT INTO lst__genres VALUES(1, "Psytrance");
INSERT INTO lst__genres VALUES(2, "Progressive");
INSERT INTO lst__genres VALUES(3, "Full On");
INSERT INTO lst__genres VALUES(5, "Dark Trance");

INSERT INTO artists VALUES(1, "Protonica", 2, "Allemagne", "Blablabla", "");
INSERT INTO artists VALUES(2, "A-Team", 3, "Israël / Espagne", "Blablabla", "");
INSERT INTO artists VALUES(3, "Electrypnose", 1, "Suisse", "Blablabla", "");
INSERT INTO artists VALUES(4, "Hyper Frequencies", 1, "France", "Blablabla", "");
INSERT INTO artists VALUES(5, "Everblast", 3, "États-Unis / Royaume-Uni", "Blablabla", "");
INSERT INTO artists VALUES(6, "Hoodwink", 2, "Royaume-Uni", "Blablabla", "");

INSERT INTO lst__set_types VALUES(1, "DJ set");
INSERT INTO lst__set_types VALUES(2, "Live act");
INSERT INTO lst__set_types VALUES(3, "Live band");

INSERT INTO sets VALUES(1, "4154176800", "4154182200", 1, 1);
INSERT INTO sets VALUES(2, "4154182200", "4154185800", 2, 2);
INSERT INTO sets VALUES(3, "4154185800", "4154191200", 3, 3);
INSERT INTO sets VALUES(4, "4154191200", "4154194800", 2, 4);
INSERT INTO sets VALUES(5, "4154194800", "4154200200", 3, 5);
INSERT INTO sets VALUES(6, "4154200200", "4154203800", 1, 6);