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

CREATE TABLE lst__set_types(
    type VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE lst__stages(
    stage VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE lst__genres(
    genre VARCHAR(255) UNIQUE NOT NULL
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

CREATE TABLE bios(
	id INTEGER NOT NULL,
	text TEXT NOT NULL,
	lang_code CHAR(2) NOT NULL,
	PRIMARY KEY(id, lang_code)
);

CREATE TABLE artists(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name VARCHAR(255),
    genre VARCHAR(255) REFERENCES lst__genres(genre),
    origin VARCHAR(255) NOT NULL,
	picture_name VARCHAR(255),
	cover_name VARCHAR(255),
    website VARCHAR(255),
    facebook VARCHAR(255),
    soundcloud VARCHAR(255),
    label VARCHAR(255),
	bio INTEGER /*NOT NULL*/ REFERENCES bios(id)
);

CREATE TABLE sets(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    begin_date INTEGER NOT NULL,
    end_date INTEGER NOT NULL,
    type VARCHAR(255) NOT NULL REFERENCES lst__set_types(type),
    artist INTEGER NOT NULL REFERENCES artists(id),
    stage VARCHAR(255) NOT NULL REFERENCES lst__stages(stage)
);

/********
 * Data *
 ********/
INSERT INTO "lst__location_types" VALUES (4, 'Scène', 'marker_stage', 'fr');
INSERT INTO "lst__location_types" VALUES (4, 'Stage', 'marker_stage', 'en');
INSERT INTO "lst__location_types" VALUES (2, 'Le Village', 'marker_chillout', 'fr');
INSERT INTO "lst__location_types" VALUES (2, 'The village', 'marker_chillout', 'en');
INSERT INTO "lst__location_types" VALUES (1, 'Stand Hadra', 'marker_hadra', 'fr');
INSERT INTO "lst__location_types" VALUES (1, 'Hadra''s booth', 'marker_hadra', 'en');
INSERT INTO "lst__location_types" VALUES (3, 'Douches', 'marker_shower', 'fr');
INSERT INTO "lst__location_types" VALUES (3, 'Showers', 'marker_shower', 'en');
INSERT INTO "lst__location_types" VALUES (5, 'Toilettes', 'marker_toilet', 'fr');
INSERT INTO "lst__location_types" VALUES (5, 'Toilets', 'marker_toilet', 'en');
INSERT INTO "lst__location_types" VALUES (10, 'Restaurants', 'marker_restaurant', 'fr');
INSERT INTO "lst__location_types" VALUES (10, 'Restaurants', 'marker_restaurant', 'en');
INSERT INTO "lst__location_types" VALUES (8, 'Camping', 'marker_camp', 'fr');
INSERT INTO "lst__location_types" VALUES (8, 'Campsite', 'marker_camp', 'en');
INSERT INTO "lst__location_types" VALUES (9, 'Poste de secours', 'marker_rescue', 'fr');
INSERT INTO "lst__location_types" VALUES (9, 'Rescue center', 'marker_rescue', 'en');
INSERT INTO "lst__location_types" VALUES (0, 'Bar', 'marker_bar', 'fr');
INSERT INTO "lst__location_types" VALUES (0, 'Bar', 'marker_bar', 'en');
INSERT INTO "lst__location_types" VALUES (6, 'Eau potable', 'marker_water', 'fr');
INSERT INTO "lst__location_types" VALUES (6, 'Drinkable water', 'marker_water', 'en');
INSERT INTO "lst__location_types" VALUES (11, 'Prévention', 'marker_prevent', 'fr');
INSERT INTO "lst__location_types" VALUES (11, 'Prevention', 'marker_prevent', 'en');
INSERT INTO "lst__location_types" VALUES (7, 'Départ navette', 'marker_shuttle', 'fr');
INSERT INTO "lst__location_types" VALUES (7, 'Shuttle', 'marker_shuttle', 'en');
INSERT INTO "lst__location_types" VALUES (12, 'Forêt enchantée', 'marker_forest', 'fr');
INSERT INTO "lst__location_types" VALUES (12, 'Psy forest', 'marker_forest', 'en');
INSERT INTO "lst__location_types" VALUES (13, 'Déchetterie ', 'marker_garbage', 'fr');
INSERT INTO "lst__location_types" VALUES (13, 'Garbage centre', 'marker_garbage', 'en');

INSERT INTO "locations" VALUES (1, 45.1150476749639, 5.61031765810242, 4, 1);
INSERT INTO "locations" VALUES (2, 45.1126881428484, 5.60822144150734, 4, 2);
INSERT INTO "locations" VALUES (3, 45.1120540264325, 5.60971945524216, 2, NULL);
INSERT INTO "locations" VALUES (4, 45.1144495482783, 5.61014599672546, 1, NULL);
INSERT INTO "locations" VALUES (5, 45.1133327759784, 5.60861981979599, 3, NULL);
INSERT INTO "locations" VALUES (6, 45.1158899629888, 5.60949421993485, 5, NULL);
INSERT INTO "locations" VALUES (7, 45.1167322385815, 5.6099904286026, 5, NULL);
INSERT INTO "locations" VALUES (8, 45.1135107038762, 5.60827381483307, 5, NULL);
INSERT INTO "locations" VALUES (9, 45.1111427031209, 5.60682810417404, 5, NULL);
INSERT INTO "locations" VALUES (10, 45.1119499170938, 5.60644581913948, 5, NULL);
INSERT INTO "locations" VALUES (11, 45.117376599911, 5.61078697443008, 5, NULL);
INSERT INTO "locations" VALUES (12, 45.1114104383883, 5.60823753476143, 10, NULL);
INSERT INTO "locations" VALUES (13, 45.1117435907525, 5.60889735817909, 10, NULL);
INSERT INTO "locations" VALUES (14, 45.1128974181447, 5.60982949606171, 8, NULL);
INSERT INTO "locations" VALUES (15, 45.1135996676171, 5.61031229368439, 8, NULL);
INSERT INTO "locations" VALUES (16, 45.117016982206, 5.61099618673325, 8, NULL);
INSERT INTO "locations" VALUES (17, 45.1140680315365, 5.60888260602951, 9, NULL);
INSERT INTO "locations" VALUES (18, 45.1142451237965, 5.60978658071747, 0, NULL);
INSERT INTO "locations" VALUES (19, 45.11177198433, 5.60832336544991, 0, NULL);
INSERT INTO "locations" VALUES (20, 45.1159202472705, 5.60969270340195, 6, NULL);
INSERT INTO "locations" VALUES (21, 45.1109826376142, 5.60695677995682, 6, NULL);
INSERT INTO "locations" VALUES (22, 45.1142421714965, 5.60790091753006, 6, NULL);
INSERT INTO "locations" VALUES (23, 45.1125499628566, 5.6093493103981, 6, NULL);
INSERT INTO "locations" VALUES (24, 45.114234600205, 5.6098535656929, 6, NULL);
INSERT INTO "locations" VALUES (25, 45.1143784545728, 5.61005741357803, 11, NULL);
INSERT INTO "locations" VALUES (26, 45.1165381193227, 5.6091870367527, 11, NULL);
INSERT INTO "locations" VALUES (27, 45.1161265585121, 5.60903019777527, 7, NULL);
INSERT INTO "locations" VALUES (28, 45.1134547517942, 5.61000242829323, 12, NULL);
INSERT INTO "locations" VALUES (29, 45.1160497886076, 5.608881264925, 13, NULL);
INSERT INTO "locations" VALUES (30, 45.111212, 5.609461, 4, 3);

INSERT INTO "location_descriptions" VALUES (1, 'The temple (scène principale)', 'fr');
INSERT INTO "location_descriptions" VALUES (1, 'The temple (main stage)', 'en');
INSERT INTO "location_descriptions" VALUES (2, 'The lotus (scène alternative)', 'fr');
INSERT INTO "location_descriptions" VALUES (2, 'The lotus (alternative stage)', 'en');
INSERT INTO "location_descriptions" VALUES (3, 'The moon (chillout)', 'fr');
INSERT INTO "location_descriptions" VALUES (3, 'The moon (chillout)', 'en');

INSERT INTO lst__genres VALUES("Psytrance");
INSERT INTO lst__genres VALUES("Progressive");
INSERT INTO lst__genres VALUES("Full On");
INSERT INTO lst__genres VALUES("Dark Trance");

INSERT INTO lst__set_types VALUES("DJ set");
INSERT INTO lst__set_types VALUES("Live act");
INSERT INTO lst__set_types VALUES("Live band");

INSERT INTO lst__stages VALUES("The temple");
INSERT INTO lst__stages VALUES("The lotus");
INSERT INTO lst__stages VALUES("The moon");

INSERT INTO "artists" VALUES (1, 'Adn Smith', NULL, 'FR', 'ph_adn_smith.jpg', NULL, 'http://www.electrikdream.com/artists/adn-smith', 'https://facebook.com/371083109642684', 'https://soundcloud.com/dj-adnsmith', 'Electrik Dream', 1);
INSERT INTO "artists" VALUES (2, 'Alen', NULL, 'HR', 'ph_alen.jpg', NULL, 'http://www.future-nature.info/', 'https://facebook.com/100000348731100', 'https://soundcloud.com/djalenmarsroom', 'Marsroom / Future Nature Festival', 2);
INSERT INTO "artists" VALUES (3, 'Aquafeel', NULL, 'GR', 'ph_aquafeel.jpg', NULL, NULL, 'https://facebook.com/161109350619587', 'https://soundcloud.com/aquafeel', 'Audioload Music / Spin Twist Records', 3);
INSERT INTO "artists" VALUES (4, 'Cubic Spline', NULL, 'FR', 'ph_cubic_spline.jpg', NULL, 'http://www.hadra.net', 'https://facebook.com/125368040871079', 'https://soundcloud.com/cubic_spline', 'Hadra Records', 4);
INSERT INTO "artists" VALUES (5, 'Curious Detail', NULL, 'FR', 'ph_curious_detail.jpg', NULL, NULL, 'https://facebook.com/251558628238185', 'https://soundcloud.com/curiousdetail', 'Hadra Records', 5);
INSERT INTO "artists" VALUES (6, 'Daksinamurti', NULL, 'DE', 'ph_daksinamurti.jpg', NULL, 'http://sangomarecords.com/', 'https://facebook.com/221626167864634', 'https://soundcloud.com/daksi', 'Sangoma Records/ Timecode Records', 6);
INSERT INTO "artists" VALUES (7, 'Darma', NULL, 'IL', 'ph_darma.jpg', NULL, 'http://solartechrecords.com/news/darma', 'https://facebook.com/244661212240063', 'https://soundcloud.com/darma-music', 'Solar Tech Records', 7);
INSERT INTO "artists" VALUES (8, 'Disorder', NULL, 'MX', 'ph_disorder.jpg', NULL, 'http://www.catalystrecords.org/', 'https://facebook.com/390635311029249', 'https://soundcloud.com/disordermusic', 'Catalyst Records', 8);
INSERT INTO "artists" VALUES (9, 'Driss', NULL, 'FR', 'ph_driss.jpg', NULL, 'http://www.hadra.net/label_artistpage.php?dj=1&&', 'https://facebook.com/174923559253391', 'https://soundcloud.com/dj-driss-1', 'Hadra Records', 9);
INSERT INTO "artists" VALUES (10, 'Drumatik', NULL, 'CH', 'ph_drumatik.jpg', NULL, NULL, 'https://facebook.com/538420813', 'https://soundcloud.com/drumatik-1', 'Peak Records', 10);
INSERT INTO "artists" VALUES (11, 'Dust', NULL, 'IT', 'ph_dust.jpg', NULL, 'http://www.looney-moon.com/', 'https://facebook.com/190994156477', 'https://soundcloud.com/thedust', 'Loone Moon Records', 11);
INSERT INTO "artists" VALUES (12, 'D_Root', NULL, 'FR', 'ph_d_root.jpg', NULL, 'http://www.d-root.com/', 'https://facebook.com/101117579959907', 'https://soundcloud.com/d_root', 'Hadra Records', 12);
INSERT INTO "artists" VALUES (13, 'Elyxir', NULL, 'FR', 'ph_elyxir.jpg', NULL, 'http://www.hadra.net/', NULL, NULL, 'Hadra Records', 13);
INSERT INTO "artists" VALUES (14, 'Entropia', NULL, 'GR', 'ph_entropia.jpg', NULL, NULL, 'https://facebook.com/142920919134471', 'https://soundcloud.com/entropia', 'Digital Nature Records', 14);
INSERT INTO "artists" VALUES (15, 'Fagin''s reject', NULL, 'UK', 'ph_fagins_reject.jpg', NULL, 'http://www.wildthingsrecords.co.uk/', 'https://facebook.com/141165769332246', 'https://soundcloud.com/fagins_reject', 'Wildthing Records', 15);
INSERT INTO "artists" VALUES (16, 'Fog', NULL, 'IT', 'ph_fog.jpg', NULL, 'http://www.looney-moon.com/', 'https://facebook.com/705091646186525', 'https://soundcloud.com/fog-peak-looneymoon-rec', 'Looney Moon Records', 16);
INSERT INTO "artists" VALUES (17, 'Fungus Funk', NULL, 'RU', 'ph_fungus_funk.jpg', NULL, 'https://myspace.com/fungusfunk', 'https://facebook.com/60490741963', 'https://soundcloud.com/fungus-funk', 'Psylife Records', 17);
INSERT INTO "artists" VALUES (18, 'Greg', NULL, 'FR', 'ph_greg.jpg', NULL, 'http://mandalarecords.org/', 'https://facebook.com/754377783', NULL, 'Mandala Records', 18);
INSERT INTO "artists" VALUES (19, 'Hetero Genesis', NULL, 'AR', 'ph_heterogenesis.jpg', NULL, 'http://cosmosvibration.wix.com/heterogenesis', 'https://facebook.com/59249177042', 'https://soundcloud.com/heterogenesis', 'Rizoma Records', 19);
INSERT INTO "artists" VALUES (20, 'Jahbo', NULL, 'DK', 'ph_jahbo.jpg', NULL, 'http://www.jahbo.info/HOME.html', 'https://facebook.com/516740432', 'https://soundcloud.com/jahbo', 'Parvati Records', 20);
INSERT INTO "artists" VALUES (21, 'Janux', NULL, 'IN', 'ph_janux.jpg', NULL, 'http://www.studiora.in', 'https://facebook.com/354208034620566', 'https://soundcloud.com/januxjehan', 'Mechanik Records / Third Eye Records', 21);
INSERT INTO "artists" VALUES (22, 'Kasadelica', NULL, 'IL', 'ph_kasadelica.jpg', NULL, 'http://www.uroborosrecords.com/artists/p_kasadelica.php', 'https://facebook.com/293917567343729', 'https://soundcloud.com/kasadelica', 'Uroboros Records', 22);
INSERT INTO "artists" VALUES (23, 'Kick & Base', NULL, 'DE', 'ph_kick_n_base.jpg', NULL, NULL, 'https://facebook.com/396452067105481', 'https://soundcloud.com/kickandbase', 'Savva Records', 23);
INSERT INTO "artists" VALUES (24, 'Kokmok', NULL, 'FR', 'ph_kokmok.jpg', NULL, 'http://www.hadra.net/index.php?goto=/home.php?lang=fr', 'https://facebook.com/220241861345716', 'https://soundcloud.com/kokmok-hadra', 'Hadra Records', 24);
INSERT INTO "artists" VALUES (25, 'Liquid Soul', NULL, 'CH', 'ph_liquid_soul.jpg', NULL, 'http://www.liquidsoul.ch', 'https://facebook.com/105173689038', 'https://soundcloud.com/liquid-soul', 'Iboga Records', 25);
INSERT INTO "artists" VALUES (26, 'LMX', NULL, 'FR', 'ph_LMX.jpg', NULL, NULL, 'https://facebook.com/360695137339559', 'https://soundcloud.com/lmx-music', 'Powow ', 26);
INSERT INTO "artists" VALUES (27, 'Lovpact', NULL, 'FR', 'ph_lovpact.jpg', NULL, 'http://www.hadra.net/index.php?goto=/home.php?lang=fr', 'https://facebook.com/363648290320761', 'https://soundcloud.com/lovpact', 'Hadra Records', 27);
INSERT INTO "artists" VALUES (28, 'Lunarave', NULL, 'FR', 'ph_lunarave.jpg', NULL, 'http://www.hadra.net/index.php?goto=/home.php?lang=fr', 'https://facebook.com/122274333592', 'https://soundcloud.com/lunarave', 'Hadra Records', 28);
INSERT INTO "artists" VALUES (29, 'Lupin', NULL, 'ES', 'ph_lupin.jpg', NULL, 'https://facebook.com/pages/Lupin/170625112979261', 'https://facebook.com/170625112979261', 'https://soundcloud.com/lupinmusic/', 'Ovnimoon Records', 29);
INSERT INTO "artists" VALUES (30, 'Meerkut', NULL, 'FR', 'ph_meerkut.jpg', NULL, 'http://funkyfreaksrecords.com/', 'https://facebook.com/121708634580343', 'https://soundcloud.com/meerkut', 'Funky Freaks Records / Hybrid Audio Source', 30);
INSERT INTO "artists" VALUES (31, 'Menog', NULL, 'PT', 'ph_menog.jpg', NULL, 'http://menog.com/', 'https://facebook.com/49960054377', 'https://soundcloud.com/menogmusic', 'Spectral records', 31);
INSERT INTO "artists" VALUES (32, 'Moonquake', NULL, 'UK', 'ph_moonquake.jpg', NULL, NULL, 'https://facebook.com/442477859143096', NULL, 'Hadra Records', 32);
INSERT INTO "artists" VALUES (33, 'Oddwave', NULL, 'FR', 'ph_oddwave.jpg', NULL, 'http://www.transubtil.com/', 'https://facebook.com/100000382698209', 'https://soundcloud.com/oddwave', 'Hadra Records', 33);
INSERT INTO "artists" VALUES (34, 'Outsiders', NULL, 'IL', 'ph_outsiders.jpg', NULL, NULL, 'https://facebook.com/137537022973132', 'https://soundcloud.com/outsidersmusic', 'TIP Records', 34);
INSERT INTO "artists" VALUES (35, 'Pragmatix', NULL, 'AR', 'ph_pragmatix.jpg', NULL, 'http://www.pragmatix.com.ar/', 'https://facebook.com/46627063730', 'https://soundcloud.com/pragmatix', 'Rizoma Records', 35);
INSERT INTO "artists" VALUES (36, 'Psykovsky', NULL, 'RU', 'ph_psykovsky.jpg', NULL, NULL, 'https://facebook.com/50391007721', NULL, 'Tantrumm Records', 36);
INSERT INTO "artists" VALUES (37, 'Psysex', NULL, 'IL', 'ph_psysex.jpg', NULL, 'https://myspace.com/psysex1', 'https://facebook.com/46430270822', 'https://soundcloud.com/psysex-1', 'Hadra Records', 37);
INSERT INTO "artists" VALUES (38, 'Raja Ram', NULL, 'UK', 'ph_raja_ram.jpg', NULL, 'http://www.tiprecords.com/', 'https://facebook.com/140525285987229', 'https://soundcloud.com/raja-ram', 'TIP Records', 38);
INSERT INTO "artists" VALUES (39, 'Sensient', NULL, 'AU', 'ph_sensient.jpg', NULL, 'http://www.zenonrecords.com/', 'https://facebook.com/144457295565390', 'https://soundcloud.com/sensient', 'Zenon Records', 39);
INSERT INTO "artists" VALUES (40, 'Shotu', NULL, 'FR', 'ph_shotu.jpg', NULL, 'http://www.hadra.net/index.php?goto=/home.php?lang=fr', 'https://facebook.com/137121816332052', 'https://soundcloud.com/shotu', 'Hadra Records', 40);
INSERT INTO "artists" VALUES (41, 'Skyfall', NULL, 'PT', 'ph_skyfall.jpg', NULL, 'http://www.nanomusic.net/artist/skyfall', 'https://facebook.com/475604252504782', 'https://soundcloud.com/skyfall-prog', 'Nano records', 41);
INSERT INTO "artists" VALUES (42, 'Sonic Entity', NULL, 'RS', 'ph_sonic_entity.jpg', NULL, 'http://www.yellowsunshineexplosion.com/', 'https://facebook.com/159168300788403', 'https://soundcloud.com/sonic-entity', 'Iono Music', 42);
INSERT INTO "artists" VALUES (43, 'Spectra Sonics', NULL, 'JP', 'ph_spectra_sonics.jpg', NULL, 'http://grasshopper-records.com/en', 'https://facebook.com/102950436491267', 'https://soundcloud.com/spectrasonicsmasaya', 'Grasshopper Records', 43);
INSERT INTO "artists" VALUES (44, 'Starling', NULL, 'IN', 'ph_starling.jpg', NULL, NULL, 'https://facebook.com/222242207825424', 'https://soundcloud.com/dj-starling-1', 'Hilltop Records', 44);
INSERT INTO "artists" VALUES (45, 'Steff Navarro', NULL, 'FR', 'ph_steff_navarro.jpg', NULL, NULL, NULL, 'https://soundcloud.com/steff-navarro', 'Reprezant MTP', 45);
INSERT INTO "artists" VALUES (46, 'Sub Zero', NULL, 'PT', 'ph_sub_zero.jpg', NULL, NULL, 'https://facebook.com/143467975663759', 'https://soundcloud.com/sub-zero', 'Psynopticz / Replicant Records', 46);
INSERT INTO "artists" VALUES (47, 'Sub6', NULL, 'IL', 'ph_sub6.jpg', NULL, 'http://www.sub6music.com/', 'https://facebook.com/122657104456950', 'https://soundcloud.com/sub6', 'Hommega Records', 47);
INSERT INTO "artists" VALUES (48, 'Sulima', NULL, 'RU', 'ph_sulima.jpg', NULL, 'http://cassini.timeweb.ru/error_domain.htm', 'https://facebook.com/760224270', 'https://soundcloud.com/sulima-alex', 'Geomagnetic Records', 48);
INSERT INTO "artists" VALUES (49, 'Sybarite', NULL, 'UK', 'ph_sybarite.jpg', NULL, 'http://www.tiprecords.com/', 'https://facebook.com/312463688880371', 'https://soundcloud.com/djlucasobrien', 'TIP Records', 49);
INSERT INTO "artists" VALUES (50, 'Talpa', NULL, 'RS', 'ph_talpa.jpg', NULL, NULL, 'https://facebook.com/123081384397725', 'https://soundcloud.com/talpator', 'Tesseract Studio', 50);
INSERT INTO "artists" VALUES (51, 'Tilt', NULL, 'FR', 'ph_tilt.jpg', NULL, 'http://www.hadra.net/', 'https://facebook.com/507563099302643', 'https://soundcloud.com/tilt-hadra', 'Hadra Records', 51);
INSERT INTO "artists" VALUES (52, 'Trio Suyaki', NULL, 'FR', 'ph_trio_saiyuki.jpg', NULL, 'http://www.nguyen-le.com/Site_Nu/Bonjour.html', 'https://facebook.com/1311618533', NULL, NULL, 52);
INSERT INTO "artists" VALUES (53, 'Tweakers', NULL, 'FR', 'ph_tweakers.jpg', NULL, 'http://www.hadra.net/', NULL, 'https://soundcloud.com/suddhashotu', 'Hadra Records', 53);
INSERT INTO "artists" VALUES (54, 'Wegha', NULL, 'HU', 'ph_wegha.jpg', NULL, 'http://ozorafestival.eu/', 'https://facebook.com/390287927726183', 'https://soundcloud.com/wegha', 'Starsheep System / Ozora Festival', 54);
INSERT INTO "artists" VALUES (55, 'Yamaga', NULL, 'FR', 'ph_yamaga.jpg', NULL, 'http://www.hadra.net/', 'https://facebook.com/589457921', 'https://soundcloud.com/yamaga-yann', 'Hadra Records', 55);
INSERT INTO "artists" VALUES (56, 'Acid Bart', NULL, 'FR', 'ph_acid_bart.jpg', NULL, '', 'https://facebook.com/100003174018281', 'https://soundcloud.com/acid-bart', 'Astek Production', 56);
INSERT INTO "artists" VALUES (57, 'Adam Shaikh', NULL, 'CA', 'ph_adham_shaikh.jpg', NULL, 'http://adhamshaikh.com/', 'https://facebook.com/657315224', 'https://soundcloud.com/adhamshaikh', 'Sonicturtle/Interchill/Dakini', 57);
INSERT INTO "artists" VALUES (58, 'Okapi', NULL, 'IT', 'ph_okapi.jpg', NULL, 'http://www.okapi.it/', 'https://facebook.com/724862121', 'https://soundcloud.com/okapi23', 'Looney Moon Experiment/ ', 58);
INSERT INTO "artists" VALUES (59, 'B-Brain', NULL, 'FR', 'ph_b_brain.jpg', NULL, 'http://hadra.net/', NULL, 'https://soundcloud.com/docteur-b-brain', 'Hadra Records', 59);
INSERT INTO "artists" VALUES (60, 'BeO', NULL, 'FR', 'ph_beo.jpg', NULL, 'http://www.beoproject.com/', 'https://facebook.com/444655895554300', 'https://soundcloud.com/beoproject', 'Atea', 60);
INSERT INTO "artists" VALUES (61, 'By the rain', NULL, 'TR', 'ph_by_the_rain.jpg', NULL, NULL, 'https://facebook.com/177466319034032', 'https://soundcloud.com/bytherain', 'Epic Tribe/ BMSS/ Trimurti', 61);
INSERT INTO "artists" VALUES (62, 'Dubz Cooker', NULL, 'FR', 'ph_dubz_cooker.jpg', NULL, NULL, NULL, 'https://soundcloud.com/dubz-cooker', 'Ethereal Decibel Company/ TSF Collectif', 62);
INSERT INTO "artists" VALUES (63, 'Globular', NULL, 'UK', 'ph_globular.jpg', NULL, 'http://globular.bandcamp.com/', 'https://facebook.com/129574943734883', 'https://soundcloud.com/globular', 'Independant', 63);
INSERT INTO "artists" VALUES (64, 'Hinkstep', NULL, 'SE', 'ph_jonas_tegenfeldt.jpg', NULL, NULL, 'https://facebook.com/128570650583193', 'https://soundcloud.com/hinkstep', 'Ovnimoon Records', 64);
INSERT INTO "artists" VALUES (65, 'Itchy & Scratchy', NULL, 'FR', 'ph_itchy_scratchy.jpg', NULL, 'http://www.hadra.net/', 'https://facebook.com/100001683281932', 'https://soundcloud.com/itchy-and-scratchy', 'Hadra Records', 65);
INSERT INTO "artists" VALUES (66, 'Kalifer', NULL, 'FR', 'ph_kalifer.jpg', NULL, 'http://kalifermix.weeloop.com/profile', 'https://facebook.com/37670663339', 'https://soundcloud.com/k-5', 'Rhytmes Actuels', 66);
INSERT INTO "artists" VALUES (67, 'Kliment', NULL, 'BG', 'ph_kliment.jpg', NULL, 'https://myspace.com/klimentd', 'https://facebook.com/14839263538', 'https://soundcloud.com/kliment', 'Zenon Records / Electrik Dream', 67);
INSERT INTO "artists" VALUES (68, 'Lab''s Cloud', NULL, 'ES', 'ph_labs_cloud.jpg', NULL, 'http://www.psychoabstract.com/', 'https://facebook.com/150688565046693', 'https://soundcloud.com/labs-cloud', 'Altar Records', 68);
INSERT INTO "artists" VALUES (69, 'Lakay', NULL, 'FR', 'ph_lakay.jpg', NULL, 'http://www.lakaymusic.net/', 'https://facebook.com/196404337062228', 'https://soundcloud.com/lakay-enjoy-people', 'Hadra Records', 69);
INSERT INTO "artists" VALUES (70, 'Light in Babylon', NULL, 'TR', 'ph_light_in_babylon.jpg', NULL, 'http://www.lightinbabylon.com/', 'https://facebook.com/179184152101500', NULL, NULL, 70);
INSERT INTO "artists" VALUES (71, 'Little Tune feat. Akor & Jibiz', NULL, 'FR', 'ph_little_tune.jpg', NULL, 'https://youtube.com/watch?v=Bu7DcF2WDGc', 'https://facebook.com/150570578399366', 'https://soundcloud.com/littletune', 'BSP CREW / LES KISMIK', 71);
INSERT INTO "artists" VALUES (72, 'Marek', NULL, 'IT', 'ph_marek.jpg', NULL, 'http://looneymoonexperiment.bandcamp.com/', 'https://facebook.com/289786241060250', 'https://soundcloud.com/marek_looney', 'Looney Moon Experiment', 72);
INSERT INTO "artists" VALUES (73, 'Mizoo', NULL, 'CH', 'ph_mizoo.jpg', NULL, NULL, 'https://facebook.com/100000550210004', 'https://soundcloud.com/mizoo', 'ULTIMAE / MOONLOOP / ONEFEEL', 73);
INSERT INTO "artists" VALUES (74, 'Nova', NULL, 'UK', 'ph_nova.jpg', NULL, 'http://www.ultimae.com/en/deejays/nova/index.html', NULL, 'https://soundcloud.com/nova_music', 'Ultimae Records', 74);
INSERT INTO "artists" VALUES (75, 'Nuage', NULL, 'ID', 'ph_nuage.jpg', NULL, 'http://www.ektoplazm.com/free-music/nuage-vivant', NULL, 'https://soundcloud.com/user7115093', 'Purple Hexagon', 75);
INSERT INTO "artists" VALUES (76, 'OneBigLove', NULL, 'IN', 'ph_onebiglove.jpg', NULL, NULL, 'https://facebook.com/155015555176', 'https://soundcloud.com/onebiglove', 'St Kilda Crew', 76);
INSERT INTO "artists" VALUES (77, 'Samsara', NULL, 'DK', 'ph_samsara.jpg', NULL, NULL, 'https://facebook.com/405802265229', 'https://soundcloud.com/dj-samsara-altar-records', 'Altar Records', 77);
INSERT INTO "artists" VALUES (78, 'Sephira', NULL, 'UK', 'ph_sephira.jpg', NULL, 'http://seph.net/', 'https://facebook.com/101414975244', 'https://soundcloud.com/sephira', 'BMSS', 78);
INSERT INTO "artists" VALUES (79, 'Sleeping Forest', NULL, 'FR', 'ph_sleeping_forest.jpg', NULL, 'http://www.lunarave.com/sleepingforest', 'https://facebook.com/181124572023605', 'https://soundcloud.com/sleeping_forest', 'Hadra Records', 79);
INSERT INTO "artists" VALUES (80, 'Sondorgo', NULL, 'HU', 'ph_sondorgo.jpg', NULL, 'https://www.facebook.com/sondorgo', 'https://facebook.com/132538535167', 'https://soundcloud.com/sondorgo', 'World Village & World Music Village', 80);
INSERT INTO "artists" VALUES (81, 'Soofa', NULL, 'FR', 'ph_soofa.jpg', NULL, NULL, 'https://facebook.com/181791811920666', 'https://soundcloud.com/soofa', 'Indépendant', 81);
INSERT INTO "artists" VALUES (82, 'Spoonbill', NULL, 'UK', 'ph_spoonbill.jpg', NULL, 'http://spoonbill.net.au/', 'https://facebook.com/121091294587355', 'https://soundcloud.com/spoonbill-sound', 'Omelette', 82);
INSERT INTO "artists" VALUES (83, 'Suduaya', NULL, 'FR', 'ph_suduaya.jpg', NULL, 'http://official.fm/suduayamusic', 'https://facebook.com/308055292586433', 'https://soundcloud.com/suduaya', 'Altar Records', 83);
INSERT INTO "artists" VALUES (84, 'Sunriser', NULL, 'FR', 'ph_sunriser.jpg', NULL, NULL, NULL, 'https://soundcloud.com/sun-riser-1', 'Reprezant MTP', 84);
INSERT INTO "artists" VALUES (85, 'Symbolico', NULL, 'IL', 'ph_symbolico.jpg', NULL, NULL, 'https://facebook.com/180246535361431', 'https://soundcloud.com/symbolico', 'Phantasm Records', 85);
INSERT INTO "artists" VALUES (86, 'Sysyphe', NULL, 'FR', 'ph_sysyphe.jpg', NULL, 'http://www.hadra.net/', 'https://facebook.com/100000032161863', 'https://soundcloud.com/sysyphe', 'Hadra Records', 86);
INSERT INTO "artists" VALUES (87, 'Val Vashar', NULL, 'HR', 'ph_val_vashar.jpg', NULL, NULL, 'https://facebook.com/100005269418509', 'https://soundcloud.com/val_vashar', 'Zenon Records', 87);
INSERT INTO "artists" VALUES (88, 'Vlastur feat. Dark Elf', NULL, 'GR', 'ph_vlastur_feat_dark_elf.jpg', NULL, 'https://myspace.com/vlastur', 'https://facebook.com/150921818262162', 'https://soundcloud.com/vlastur', 'Sonic Loom', 88);
INSERT INTO "artists" VALUES (89, 'Kalifer', NULL, 'FR', 'ph_kalifer.jpg', NULL, 'http://myspace.com/kalifer38', 'https://facebook.com/37670663339', 'https://soundcloud.com/k-5', 'Rhytmes Actuels', 89);
INSERT INTO "artists" VALUES (90, 'Kliment', NULL, 'BG', 'ph_kliment.jpg', NULL, 'https://myspace.com/klimentd', 'https://facebook.com/14839263538', 'https://soundcloud.com/kliment', 'Zenon Records / Electrik Records', 90);
INSERT INTO "artists" VALUES (91, 'Lab''s Cloud', NULL, 'ES', 'ph_labs_cloud.jpg', NULL, 'http://www.psychoabstract.com/', 'https://facebook.com/150688565046693', 'https://soundcloud.com/labs-cloud', 'Altar Records', 91);
INSERT INTO "artists" VALUES (92, 'Mizoo', NULL, 'CH', 'ph_mizoo.jpg', NULL, 'https://soundcloud.com/mizoo', 'https://facebook.com/100000550210004', 'https://soundcloud.com/mizoo', 'Ultimae / Moonloop / Onefeel', 92);
INSERT INTO "artists" VALUES (93, 'Sysyphe', NULL, 'FR', 'ph_sysyphe.jpg', NULL, 'http://www.hadra.net/', 'https://facebook.com/100000032161863', 'https://soundcloud.com/sysyphe', 'Hadra Records', 93);

INSERT INTO "bios" VALUES (1, 'Issu d''une famille de musiciens, Olivier aka ADN SMITH touche à plusieurs instruments dés son enfance et découvre les musiques électroniques en 1995.
Inspiré par divers artistes, son style évolue entre progressive & psytrance.
Connu et reconnu en région Rhônes Alpes,Ses tracklists, mélanges Groovy et Techniques de mixs ont laissé de bonnes impressions.', 'fr');
INSERT INTO "bios" VALUES (1, '', 'en');
INSERT INTO "bios" VALUES (2, '', 'fr');
INSERT INTO "bios" VALUES (2, 'The sound of Pink Floyd and Ozric Tentacles involve me into the psychedelic music, but the first contact with goa trance in the 1995. define me. Now, my sets vary from night psytrance to the psyprogressive trance.
Start to play music in a year 2000. on a first Future Nature Festival, which was organized by dj Nesho and me.
In a last ten years my main challenge (alongside the music) is to organize and define the theme and vision of Future Nature Festival.', 'en');
INSERT INTO "bios" VALUES (3, '', 'fr');
INSERT INTO "bios" VALUES (3, 'Aquafeel, who first started to experiment with psychedelic trance and the possibilities of various sequencers in 2006, and had his first release,Trust EP, on Spintwist Records no less than two years later, can be described as a natural musical genius.

His first live set at legendary Indian Spirit Festival in 2008 opened the way for his musical career, and today Aquafeel is a frequent favorite at various European psy trance festivals and parties.

Aquafeel personifies the hallmark of bittersweet melodies in perfect resonance with deep suggestive baselines. This, along with his touch and feel for tricky details and interesting grooves, takes the listener on a journey into deep melodic progressive trance.', 'en');
INSERT INTO "bios" VALUES (4, 'Passionné par la synthèse sonore, le duo Cubic Spline (Clément Bastiat et Olivier Richard) trouve son unicité dans des atmosphères mystérieuses et des textures sonores travaillées. Il a ainsi développé un son très mental et détaillé qui marie Ã merveille les racines de la trance goa, l''hypnotisme de la dark psytrance et la puissance des productions modernes.

Le duo a rejoint l''équipe du label Hadra Records au début de l''année 2010 et, après une première release sur la compilation Hadracadabra 5 du label, Cubic Spline sort son premier EP, « Unusual Path », en octobre 2010. Un an et demi après, le duo revient avec son premier album « Paradigm Shift », conte psytrance/dark retraçant l''histoire d''un scientifique en plein périple intergalactique.

Soyez prêts à ressentir les ondes puissantes d''un live psychédélique et transcendant !', 'fr');
INSERT INTO "bios" VALUES (4, '', 'en');
INSERT INTO "bios" VALUES (5, '', 'fr');
INSERT INTO "bios" VALUES (5, 'Since 2011, Curious Detail has been involved in psytrance in france and other countries. After enterning the milimetrical and powerfull world of psychedelism, he developped his own touch of funky-dark dancefloor-inclined psytrance.
He currently works to deliver a personal point of view through immersive soundscapes and rythmic music.', 'en');
INSERT INTO "bios" VALUES (6, '', 'fr');
INSERT INTO "bios" VALUES (6, 'Daksinamurti is an ethnologist and international psytrance artist from Marburg, Germany. Besides being a DJ he is managing the South Afrika based label TIMECODE and SANGOMA Records.
With more then a decade of experience as a musician he looks back to a colorful history – so far has represented labels like the legendary Shiva Space Technology, Nexus Media and is currently playing for Peak records. His live act “Android Spirit” together with Tickets/Brethren launched in 2013 and after a couple of compilation appearances they are currently working on their album to be released on Timecode Records.
Till has built a solid reputation within the international psyscene and shared his music and good vibes at various parties and festivals such as: Boom (Portugal), Universo Paralello (Brazil), S.U.N Festival, Alien Safari (South Africa), Gemini (USA), VuuV, Antaris, Fusion, Fullmoon Festival, Psycrowdelica (Germany), among others.', 'en');
INSERT INTO "bios" VALUES (7, '', 'fr');
INSERT INTO "bios" VALUES (7, 'Darma is Adam Belo, from the center of Israel, Tel Aviv. Adam has been producing music since 2000 as part of the duo Hipnotix; with his older brother Yaki. As Hipnotix, they have released music on labels including: HOMmega, and Noya Rec.. In 2010 they released their full length album entitled "Side Effect," which received great feedback from dance floors around the world including the countries of: Japan, Mexico, Brazil, Sweden, Russia, and many more. At the beginning of 2012, in an attempt to further develop the Hipnotix sound, Adam began incorporating inspiration from different music genres. The result was "One Night Stand" which became a number one seller on Beatport, and received support from fellow artists: Ace Ventura, Liquid Soul, Astrix, Captain Hook, Paul Oakenfold, and many others. This marked a new beginning in Adam''s musical career, as he understood the need to make something unique and different. The result was his brand new project Darma. His debut EP as Darma, "Deep Hole" which included collaborations with Ace Ventura & Vertical Mode, released June 25th, 2012 on Echoes Records; marking the beginning of a bright new musical journey. Looking to further his new project in 2013 and beyond, Darma joined the ranks of Solar Tech Records, one of today''s leading Trance music labels. With an impressive start, and many more releases in the pipeline, the future looks bright for this rising star in the global Psychedelic music scene!', 'en');
INSERT INTO "bios" VALUES (8, '', 'fr');
INSERT INTO "bios" VALUES (8, 'Disorder is to derange the physical or mental health or functions of; and it is exactly what Erick Medina does behind the machines. Disorder has evolved among the scene with a serious and sophisticated focus on music. He has been sharing his music through different places and he is becoming an important serious psytrance artist into the global scene; several releases ranked in top sells and inside of important DJ charts; his music is being played more and putting the peculiar disordered style on the dance floors. He will be playing this year in important festivals around the world, so stay tuned. His album is also being prepared for 2014, music based on sturdy beats and solid basslines to make it an elegant trip through high-tech psytrance. With a BPM range from 138 to 147, his music is a fresh delicacy to those who are looking for a new serious take on trance.', 'en');
INSERT INTO "bios" VALUES (9, 'Driss est un DJ emblématique du label Hadra Records et l''un de ses fondateurs. Son expérience de la scène trance en France et à l''international, en qualité d''organisateur ou pour ses DJsets acclamés dans certains des plus grands festivals mondiaux (Ozora en Hongrie, Aurora en Grèce, Transahara au Maroc et bien d''autres), n''a cessé de nourrir son attachement pour la Trance goa, un style musical auquel il s''identifie pleinement. Depuis l''aventure de l''association Spiritual Freequencies dont il est l''un des initiateurs, il participe à la création et au développement de l''association Hadra, de son label et de son festival !', 'fr');
INSERT INTO "bios" VALUES (9, '', 'en');
INSERT INTO "bios" VALUES (10, 'C''est sous le pseudonyme de Drumatik que le producteur américano-germanique, Benjamin Klingemann né en 1980, s''adonne aux expérimentations électroniques dans le registre du psychédélique !

Musicien dès son plus jeune âge, il suit d''abord le conservatoire en temps que batteur et se passionne pour le rythme ...

Il découvre ensuite l''univers de la musique électronique au milieux des années nonante et commence rapidement ses activités de dj ...

Il travaillera pendant dix ans dans un magasin de musique spécialisé qui lui permettra de monter son studio et se familiariser avec l''informatique musicale ....

Très vite, il rejoint le prestigieux label suisse "Peak records" et lance en 2004 ses premières productions et live set !
', 'fr');
INSERT INTO "bios" VALUES (10, '', 'en');
INSERT INTO "bios" VALUES (11, '', 'fr');
INSERT INTO "bios" VALUES (11, 'Dust’s unique sound has been released worldwide since 2008.

Melting groovy, full-on oriented basslines and rhythms with night time psychedelic sounds.

Dust has played live across the globe in countries such as Australia, Usa, Brazil, Israel, Russia, India, South Africa, New Caledonia, Canada, England, Germany, Greece, Austria, Cyprus, Portugal and Turkey.

The debut album ” I don’t like psychedelics” was released in February 2012 on Looney Moon Records, as well as the new album ” My friends love psychedelics” released in March 2013.

Alongside his Solo project, Dust is part of a few other collaboration projects such as Foam with Assioma, Dustinface with Phase ,Comadust with Comasector (hypogeo) and Mindshuffle with Ilai', 'en');
INSERT INTO "bios" VALUES (12, '', 'fr');
INSERT INTO "bios" VALUES (12, 'D_Root discovered electronic music and digital arts back in 1996, 13 years
ago. He knew from the start he wanted to make a career of writing
electronic music. In 1998, he started creating hartechno (hardtechno ?) and
performing in free parties up until 2002, at which point he wanted to
explore new horizons. From there on, his music evolved towards hybrid
melodic and rhythmic structures, a unique mix between techno and
psyketrance. With time, he has acquirred new equipment and improved his
technical skills.

Since 2004, D_Root has dedicated his time to creating trance music, mostly
progressive trance. He has clearly been influenced by the work of
Vibrasphere or Liquid Soul? In the last three years, concert dates bookings
have increased continuously, D_Root has performed in different parties and
festivals in France, Switzerland, Belgium, Burkina Faso. On different
occasions, he shared the stage with famous artists such as Gaudium, DayDin,
Astral projection, HyperFrequencies, Quantize...', 'en');
INSERT INTO "bios" VALUES (13, 'Elyxir est un DJ Hadra qui fut très impliqué dans l''association il y a quelques années. Il sortit en 2007 sa première compilation « Sunrise » avec DJ Inner-G sur Hadra Records qui fait encore aujourd''hui vibrer les dancefloor ! Il développe un concept à mi-chemin entre le dj-set et la performance live, dans lequel il insère et triture des leads et des samples en live.', 'fr');
INSERT INTO "bios" VALUES (13, '', 'en');
INSERT INTO "bios" VALUES (14, '', 'fr');
INSERT INTO "bios" VALUES (14, 'Behind ''Entropia'' project is Nikos ''D.mind'' Tsantanis ,born and raised in Thessaloniki Greece. D.mind began exploring music at an early age and he came across trance electronica back at 1997. With the new millennium, the new progressive wave appeared in the trance scene and he had finally found the style that suited him best. So he started collecting music and mixing at private events in the beginning and over the years he got to play at various major events in his hometown, as dj, sharing the stage with some of the biggest names in the scene. In the meanwhile a passion for music production grew so he started experimenting with his own sound and composing his own music. Nick has been studying sound technology for the past few years and with endless hours in the studio he has taken the quality of his productions to a whole new level.', 'en');
INSERT INTO "bios" VALUES (15, '', 'fr');
INSERT INTO "bios" VALUES (15, 'Fagin''s Reject is London born Phil Getty.
Phil comes from a psychedelic rock and metal background and was first exposed to Goatrance in 1992 at London based Goa parties, and quickly began to collect the limited vinyl it was released on. Soon after, he made his way to Goa and has been attending parties and festivals all over the world ever since.
After returning from India in 1994 he became a member of Lost Sector, an early Techno-Acid group made up of Bill Robin from Warp Technique/Bill & Ben, and Benny Ill from Horsepower Productions/Tempa, which had several releases on Fishtank Records.
Originally a bassist and funk DJ, Phil started producing Psytrance around 2003 out of a need to express musical ideas. It was only after attaining a music technology qualification in 2008 that making music became more serious to him, and so Fagin''s Reject was born.
Since then, Fagin''s Reject has spent two happy years with UK underground label Psynon Records, where he played all over Europe and had many releases, but is now on a mission to push his sound further and deeper into unchartered territory, with his original, dark funky style.', 'en');
INSERT INTO "bios" VALUES (16, '', 'fr');
INSERT INTO "bios" VALUES (16, 'It’s hard to describe in few words the crazy vortex of creativity and events that have seen dj Fog involved from the end of the nineties. To say it in few words Roberto started djing in 1999 and played at first party in 2001, in 2004 he opened Looney Moon party org with a bunch of friends, and started producing events all over italy and abroad. In 2008 the group decided to open Looney Moon records. In 2011, in order to enrich the range of productions, and thanx to the involvement of more trustable friends, was born a new record label for more experimental productions and chill out: Looney Moon Experiment. Since 2005 Roberto has been pushing underground sounds playing at many events all over the world including the bigger ones like BOOM, UNIVERSO PARALLELO, ANTARIS, HADRA, SUMMER NEVER ENDS, FREQS OF NATURE, and many more. Label dj for the swiss label Peak rec from 2006 and label manager of Looney Moon rec since the very beginning together with dj Phobos, dj Fog has rocked countless events all over the globe. All his sets on any style are rich of unreleased stuff from all the artist-friends. Apart from the Looney Moon activities Robi is now one of main guys behind Momento Demento Festival and Collider Art Gallery (Looney Moon Visions) and artistic and logistic consultant for several other events in europe, and not only.', 'en');
INSERT INTO "bios" VALUES (17, '', 'fr');
INSERT INTO "bios" VALUES (17, 'Fungus Funk aka Serge Prilepa, the legendary Russian psytrance artist began writing psychedelic tunes right before the Millenium and started to release the first Fungus Funk tracks in 2001 on record labels like Acidance Records, Discovalley or Crystal Matrix and many others.
His famous debut album called "F People" came out on Acidance in 2006 overflowing with groovy baselines, aggressive synths, warm melodies and those signature psychedelic atmospheres you can still experience on every Fungus Funk release ever since, be it an own composition or a remix made for a fellow musician.
The original Fungus Funk sound always keeps a masterful balance of complex textures and arrangements that lead the listener through a deep and interesting journey of that hazy period of time between the psychedelic night and morning.', 'en');
INSERT INTO "bios" VALUES (18, '(Label Director & Manager@Mandala records/ Dragon Tribe@France) ', 'fr');
INSERT INTO "bios" VALUES (18, '', 'en');
INSERT INTO "bios" VALUES (19, '', 'fr');
INSERT INTO "bios" VALUES (19, 'Heterogenesis is the combination of japo and Ale , two of the best artists on the bright side of the psytrance scene in Argentina.

Japo aka Pragmatix has already surprised the world with his promo album called Inevitablemente Mistico released on 2005 as well as releases in Dark Prisma, Geomagneticand Headroom, playing in several parties around Argentina delighting dancers with his melodies , and now he''s working on his first full album under rizoma records label.

Ale , Cosmos Vibrations has released tracks on Dark Prisma and Trasition Records, his first album was realeased under the label ovnimoon records , and he''s second album was realesed under rizoma records

Heterogenesis was created to explore the morning experience of psychedelic trance, combining gentle grooves with insightful melodies taking the listener to a pleasent trip to other dimensions designed to awaken a revolution inside us.', 'en');
INSERT INTO "bios" VALUES (20, '', 'fr');
INSERT INTO "bios" VALUES (20, 'Jahbo was born as Rolike Bunzendahl in 1981 in a hippy society in the north of Denmark. As an original member of Parvati Records, he started to release and represent the label and act as a confidential person for Guiseppe. Together they pioneered a special base for this extraordinary music.

Working in a music shop in his day to day life, he is constantly refining his production skills and staying on the cutting edge of sound design and creation. His state of the art knowledge is necessary to define his highly recognizable style. His tracks, known by all who love the Parvati sound, have a special charm of a tripped-out, mushroomy atmosphere.

Morphing soundscapes, wobbling basslines, a splash of the right humor, twisted effects and surrealistic leads are a defining earmark of Jahbo''s tracks. Playing around the globe at many major festivals and parties, he has become an integral part of the scene. Exceptional live gigs and absorbing DJ sets are to be expected.', 'en');
INSERT INTO "bios" VALUES (21, '', 'fr');
INSERT INTO "bios" VALUES (21, 'One of the pioneer DJs of India who has played all over the planet.
Dj Janux ::the name comes from ''Janus'', a mythical Roman god who has two faces: one dark, one light - one looking into the past, one looking into the future.

A powerful Dj who can read any situation and play the perfect vibe for that space & time. Janux''s style ranges from groovy night, to magical twilight, to strong & psychedelic morning sounds. His high energy performances packed full with killer mixing, sorcery, and dance power, transform the party into a powerful spirit.

His roots in music started in the mid 80''s in Goa where he was a kid growing up in the early roots of the trance scene. He has been DJing since 1998, and has played at parties and festivals all over the world including Tree of Life and Universal Religion. His Dj journey has put him behind the decks in NYC (where he started the Dj collective called “Sound Species”), London, Amsterdam, Oslo, Turkey, Nepal, Bangkok, Goa, and all over the rest of India.

Janux hosts all the big psytrance parties that happen in Bombay, where he is the head of music programming at the legendary Blue Frog, the only club in India that does regular psy nights with domestic & international artists.

Janux has also opened Goa’s first professional music production & recording studio: Studio Ra (www.studiora.in)', 'en');
INSERT INTO "bios" VALUES (22, '', 'fr');
INSERT INTO "bios" VALUES (22, 'Kasadelica project is created by Guy Kasif from Israel. The ﬁrst experience with the psy world came to light in the end of the 90''s in underground trance parties. with time passing, his interest in trance music has increased and became a major part of his life. After a few years guy took off from his homeland and dedicated his time for traveling around the world exploring cultures and participating in psy festivals all around. After a while guy wanted to send his own message to the dance ﬂoor and began playing in parties in india and europe and started to have is own ideas for music. Guy started to experiment in the production of trance music.

His inspiration came from the simple idea of creating tunes that will send the persons to a higher state of mind like he experienced in his adventures. Kasadelica like to describe his style as darkpsy, organic and communicative space music. The strong tone in his music is combined from fat base line unique crispy sounds that are alive, but most important is the drive.', 'en');
INSERT INTO "bios" VALUES (23, '', 'fr');
INSERT INTO "bios" VALUES (23, 'Kick & Base is a dj duo from Hamburg consisting of Jonas Nils Winterfeld and Waldek Biskup. Their collaboration started in spring 2011. After a few successful experiments, Kick and Base started to perform as dj team on a regular basis.
Waldek began his musical career in 1995 as dj Dharma, particularly playing psychedelic and progressive trance music at many psytrance parties in his homeland Poland and later across Europe. While continuing to dj, he was constantly developing his production skills. Together with his wife Aga, he started the project Lightsphere.
Jonas is very well known in Hamburgs psytrance scene as a dj and promoter. Since his first contact with psytrance music in 2000, he has constantly been involved in the scene. First as a promoter, later in 2005 he also started djing. His style is a bright range of driving progressive trance.', 'en');
INSERT INTO "bios" VALUES (24, 'Aujourdhui Dj représentant de lunivers de la Trance Progressive sur le label Hadra Records et programmateur du Hadra trance festival, Didier alias Kokmok est à lorigine issu de la scène techno et house du sud de la France.
Kokmok sortira en 2008 et 2013 ; « Obsessiv Progressiv Vol1 & 2 » qui sont le fruit d''une collaboration avec Easy Riders, Darma, Echotek, Aerospace, Funky Dragon, Zyce, Ovnimoon ou encore Ritmo. Cest le début d''une belle aventure qui le conduira sur les plus beaux dance floor européens où Kokmok confirmera son style : basses prog et rythmes groovy au rendez-vous !!', 'fr');
INSERT INTO "bios" VALUES (24, '', 'en');
INSERT INTO "bios" VALUES (25, '', 'fr');
INSERT INTO "bios" VALUES (25, 'Nicola Capobianco, based in switzerland, started at early age getting addicted to the magic of electronic music. He began playing progressive trance from the end of 1993. In 2001, after many years as DJ and producing in the studio, where he has played and performed at big raves and festivals, next to people like Tiesto and Armin van Buuren, he decided to start a new project and Liquid Soul was born.
In 2006 he founded his own label Mikrokosmos records. Besides solo project Sleek, he also partners Martin Knecht in the deadly duo “Earsugar”.

Being behind the best selling albums on Iboga Records as Liquid Soul, „synthetic vibes“, „love in stereo“ and „cocktails“, over 100 hypnotic releases on top labels and playing worldwide over a decade, he finally won the Beatport award for the best psytrance track in 2009.
In 2010 he achieved his biggest success by winning the price of the best Beatport artist, best Beatport track and the Dj Awards in Ibiza in psy trance categories.
In 2013 he released his 3rd studio Album "Revolution"!', 'en');
INSERT INTO "bios" VALUES (26, 'La Mère à Xavier, c''est un savoir faire culinaire affuté, combiné à un instinct maternel incroyable, le tout passé au mixeur avec des herbes de Provence, une gousse d''ail et une bonne dose de Progressive bien grasse... Oublie ton régime végétarien et tes graines germées, et viens t''faire péter une bonne grosse potée.', 'fr');
INSERT INTO "bios" VALUES (26, '', 'en');
INSERT INTO "bios" VALUES (27, 'Hadra Records est fier de vous présenter LovPacT, nouveau projet trance-progressive combinant le groove psychédélique du renommé LunaRave et les mélodies envoutantes & mélancoliques de Sleeping Forest, dernière recrue ambiant du label Hadra. Après de nombreuses collaborations secrètes,et d''innombrables scéances de studio, leur premier album RétrodeliK voit enfin le jour en février 2013 sur le label français ! Nous vous attendons nombreux cet été pour découvrir leur prochain album , au Festival hadra 2014 !!', 'fr');
INSERT INTO "bios" VALUES (27, '', 'en');
INSERT INTO "bios" VALUES (28, 'Le cosmos, le voyage, le temps et l''Histoire...autant de notions qui définissent l''univers de Lunarave. C''est en écumant, dès 2005, les scènes de la région Rhône-Alpes au sein des associations Psynap''s et Hadra avec entre autres des dates aux Nuits Sonores, au Hadra Trance Festival ou encore à la Natural Trance à Grenoble, que Lunarave fait connaître son style et sa griffe !
Son live act détonnant est acclamé de date en date et Lunarave sera bientôt repéré au delà des frontières ou il fera des sets remarqués en Suisse, Allemagne, Belgique, Autriche , Italie mais surtout en Grèce, ou son premier album : ''The 4th Sun'', rencontre un franc succès.
Aujourd''hui, plus que jamais, Lunarave est en pleine effervéscence et nous concocte simultanément les sortie de plusieux projet, comme LovPacT , en duo avec Sleeping forest, dernière recrue ambiant du label, Angry LunA, dans un style hybride entre full-on et hi-tech, ou encore LunaRooT, vous l''aurez compris, une collaboration psy-gressive originale avec D_RooT...et pour couronner le tout, le 2nd opus Lunarave - Do you Know Who you are? est enfin disponible sur Hadra records!!
Inutile d''en écrire plus, écoutez et laissez-vous emporter par cet artiste complet !', 'fr');
INSERT INTO "bios" VALUES (28, '', 'en');
INSERT INTO "bios" VALUES (29, '', 'fr');
INSERT INTO "bios" VALUES (29, 'Lupin is Miguel Solans from Lleida, Spain. Born in 1985 and involved inelectronic music since he was 15, is actually a respected producer and dj within the progressive psytrance scene all over the world. After many releases in labels like YSE, Synergetic , Ovnimoon Records, Plusquam Records or Fatali Records to name a few, his live act is asked in countries like Mexico, Chile, Brazil, Greece or hungary among others.When you see him expect a blasting live act, a dj set, or all together!', 'en');
INSERT INTO "bios" VALUES (30, 'Bercé dans un monde aux sonorités psychédéliques dès son plus jeune âge, la rencontre avec les énergies et la transe lui a donné l''envie de créer ses propres sons, sa propre vision de la Trance psychédélique,
Les portes de sa musique s''ouvrent sur des textures granuleuses et gluantes, des rythmiques cadencées, un kick bass pêchu et une atmosphère aux vibrations infinies...', 'fr');
INSERT INTO "bios" VALUES (30, 'Lulled into a world of psychedelic sounds from an early age, Tom meets the desire to create those clean sounds when he discovered psychedelic trance, The door opens with grainy textures and gooey, groovy rhythms, kick punchy bass and atmosphere to infinite vibrations..', 'en');
INSERT INTO "bios" VALUES (31, '', 'fr');
INSERT INTO "bios" VALUES (31, 'Musician since almost his birth, played from guitar to Drums in his Metal Band in early 90s.
His introduction and interest for electronic music started in 1995 with the Techno scene in Portugal,in 1997 he goes to his first Trance event.
Boom Festival 1998 made him think about doing this type of music.
Four years later, in 2002, Dani released his first album "Natural Behaviour" under the name Menog
on the label Starsounds Rec. from Greece.
Menog is one of the founders and manager of Spectral Records.
In 2005 he releases EMOTIONS! cd002 from Spectral Records, and 2nd solo album.
He released several tracks on various compilations and labels worldwide,
like Spectral Records, Nutek, 3Division, Shiva Space Tech, Phantasm , SpaceTribe Music, Digital Psionics ,
Acidance, Timecode, Nexus Media, Planet Ben, Fungi rec , Noize Conspiracy, Hadra , etc...
Lately Menog´s music is being played worldwide thru his emotional and enthusiastic live appearances.
In the last years he played all over the world,like Australia,Japan,India,Brazil,Israel,South Africa,
Russia,Dubai,Morocco,USA,Finland,Cyprus,Macedonia,Turkey,Greece,Mexico,Germany,
Belgium,Italy,Sweden,Denmark,England,Spain,Portugal, etc...
Menog´s 3rd album was released on Nutek Records (CPU''s label), which was a Remix album,
something that Dani wanted to do for a long time already...
Rmx´s from CPU, Space Tribe, Bliss, Rinkadink, Shift,Audialize, just to name a few...
His 4th album "Musically Speaking" was released in the end of 2008 on Spectral Records,and its been hitting dance floors all over the world.
Menog released his Digital EP called "I see Change", out on Spectral Records in 2012.


Emotions are what he wants to transmit to people when playing live, on stage.
His music is a story with a beginning, middle and ending.
His tracks are made with a single purpose... to make people DANCE!!', 'en');
INSERT INTO "bios" VALUES (32, '', 'fr');
INSERT INTO "bios" VALUES (32, 'Nico got into electronic music as a teenager. He discovered psytrance for the first time in 1999. Moving to London in 2002 allowed him to quickly find his place amongst the English psytrance community.
After several years as an active member of the Hadra association and numerous performances in the UK and Europe, time came to release his first compilation: Epicentre came out on Hadra Records in July 2009. Moonquake developed a distinctive style deeply influenced by the British underground sound. His sets are adapted to nightime as well as the early hours of the morning, and change according to the moods of the dancefloors. His performances are always characterised by a very psychedelic atmosphere and a dynamic fluidity.', 'en');
INSERT INTO "bios" VALUES (33, 'ODDWAVE fait ses premiers pas dans la musique dès l''âge de 12 ans en tant que
guitariste dans divers groupes ( funk/ jazz fusion/ métal / hip hop) et se
passionne très vite pour la production. Il monte un studio d''enregistrement et
produit plusieurs albums pour ses differents projets pendant plus de 5
ans.

A l''âge de 18 ans, il découvre plus sérieusement la musique électronique et
plus particulièrement la trance. Apres une grosse période de composition, il signe
son premier EP de 6 titres SIGNAL TO NOISE chez Spliff Muic (PLUSQUAM
RECORDS) .

Il rentre par la suite en tant qu''artiste et technicien du son dans
les associations SOUNDZ PAINTERZ puis TRANSUBTIL.
Au fil des années suivantes il se fait connaitre de beaucoup de programmateurs de soirées et festivals
qui lui permettrons de jouer auprès de grands noms comme NEELIX, ACE VENTURA, CAPTAIN HOOK,
SYMPHONIX, TALAMASCA, SONIC SPECIES et d''acquérir une notoriété auprès des passionnés de musique électronique.

A la suite de cela, des artistes comme Dj KOKMOK et Dj TILT lui demande de produire
des morceaux pour leurs compilations respectives «Obsessiv Progressiv 2» et «Progressive Equation».

Aujourd''hui il intègre le label HADRA Records qui lui offre la chance de sortir son premier album "X2" disponible le 13/12/13.
Passionné et amoureux du son, ODDWAVE est en constante recherche de
nouvelles sonorités, de projets, de partage, d''évolution, le tout dans un esprit libre et ouvert ;)', 'fr');
INSERT INTO "bios" VALUES (33, '', 'en');
INSERT INTO "bios" VALUES (34, '', 'fr');
INSERT INTO "bios" VALUES (34, 'Outsiders is the exciting project from two significant producers based in sunny Israel – Haim Lev & Guy Malka.
The Outsiders name has been fast developing and their releases have been backed and supported by an impressive international fan base consisting of producers and rave heads alike. Outsiders have released a bundle of successful original releases, a bounty full of collaboration & remix tracks from the likes of Tristan, Symbolic, Laughing Buddha, Avalon, Electric Universe, Rinkadink, Sonic Species, Burn In Noise, to name few.
The Outsiders sound is always evolving and is highly influenced by the duos global interactions.
The duo is currently working on their full studio album which will be released by TIP World records later on in 2014.', 'en');
INSERT INTO "bios" VALUES (35, '', 'fr');
INSERT INTO "bios" VALUES (35, 'Pragmatix is an artist born in Buenos Aires, Argentina. When he was eleven he started playing guitar, and then in high school he played with friends in several bands until he discovered eletronic music around 2001. Listening to bands like Hux Flux, Hallucinogen and Infected Mushroom his head and soul opened and making psytrance was just a matter of time. Using music as his spiritual path and creative expression, Pragmatix quickly became one of the most promising artists in Argentina after the release of his demo “Inevitablemente Mistico”, filled with very creative, original and psychedelic tracks, including collaborations with several local artists and a little glimpse of his dub side, which he also explores in his performances as a chill out dj.

In September, 2005, he released several tracks for the argentinian compilation Amalgama from Dark Prisma Records and Transition Records. and in 2006 he released another few tracks for Geomagnetic Recs, Dark Prisma Recs, Headroom Productions, and started the side project with his friend alejo called Heterogenesis. In 2007 he released the debut album of Heterogenesis called “Revoluciones Maquinicas” with Dark Prisma Recs and some tracks one compilations like Kadgila Records

His style combines mysterious and dark atmospheres with colorful and playful psychedelic melodies, playing with different textures and a lot of grooves, making tracks perfect for the dancefloor and listening at home. He has also collaborated with a lot of local artists, giving a lot of diversity to his productions.

Currently he has been playing in several outdoors and indoors parties in Buenos Aires and all Argentiona. Also played in festival and partie’s like Universo Paralello 8, 9, 10 & 11 (brasil), Summer Never Ends (swiss), Freqs Of Nature (germany), Green Magic (Japan), Tundra (Lithuania), Spirit Base (hungary), Halkidiki (greece), Trancendence (brasil), Festival Fora do Tempo (brasil), Festival Monte Mapu (chile) Respect (brasil), Flip Out (brasil), Porto Das Aguas Festival (brasil), MoonFlower Festival (argentina)', 'en');
INSERT INTO "bios" VALUES (36, '', 'fr');
INSERT INTO "bios" VALUES (36, 'Psykovsky is a maverick psytrance producer from Russia, began playing in groups as early as 12 and first achieved success as the member of Transdriver. Since 2000 he has worked with a number of different solo and collaborative projects, and is nowadays acknowledged as one of the most auspicious psytrance artists. With undiminished enthusiasm Psykovsky continues to engross listeners and successfully performs all over the world.', 'en');
INSERT INTO "bios" VALUES (37, '', 'fr');
INSERT INTO "bios" VALUES (37, 'Udi has been drawn to music since he was a child. At 12 he started spinning vinyl and dj’ing at home, and by 16 was blasting house music on the Tel Aviv club scene, raising the bar of dj’ing throughout the scene. At 18 udi settled into his music career, building a trance music production lab, known as the Basement Studio.

After a long battle with a recurring crash and encountering the error message “Sysex error code 4”…
Udi followed the guidance the studion provided and took the name Psysex.
Psysex hit the shelves with “4 days of madness” (with dave saragosi) and "alien cop" (with bansi from Gms) on 12inch vinyl with “krembo records”. Following this, he quickly started releasing tracks with his Friend yoni oshrat and within two years, at the young age when udi was 20. He released his first album with yoni - "Expressions of rage” under hommega records,

During this period, Udi kept on exploring new avenues of music and was involved in ongoing projects such as:

-Domestic goblin
-Unstables with pixel

The success of Psysex’s album took udi and yoni across the globe from japan to Brazil and down across South America, playing their music and moving the dance floor. In 2001, they released their second album "Hardcore Blastoff”. Immediately, tracks like “Little Black Hole”, Dominatrix, Dirty 80’s (with Infected Mushroom) gained huge dancefloor and dj popularity, increasing Psysex’s popularity among lover os psytrance around the world.
In 2002 Udi and Yoni took the project to the next level by producing a concept album about an alien invasion, “Come In Peace”, which was released in june 2003. This album represented the peak of creativity and collaboration between Udi and Yoni, following it’s release they went on a 2 year world tour, playing at the biggest festival’s, parties and clubs.

The long period of touring, lack of studio work and creativity flow took their toll. In 2005 Yoni and Udi decided to release one last remixes album together of their classic hits. Known as “Remixed”

In 2006 Udi realized he couldn’t abandon his love, and performed in new places around the world -- Boom-Portugal, Zoom-Switzerland and Tribe Brazil, all the while producing and collaborating with other artists.

2007 was spent back in the Basement Studio, crafting his first solo album still under the name Psysex. The album, “Healing” was released in early 2008 on hommega.

After the excitement of the Healing album settled down, Udi found himself contemplating life and rethinking both his inner self and his music. Becoming a vegan and spending time in the high mountains of the Himalayas, changed Udi’s perception oflife and music, this time taking 6 years to complete his life masterpiece "Mind Penetration" That was released with "Hadra Records” in France on January 2014', 'en');
INSERT INTO "bios" VALUES (38, '', 'fr');
INSERT INTO "bios" VALUES (38, 'Had a band in the sixties..Quintessence....5 albums with Island records
Formed The Infinity Project....50 releases on TIP

Formed TIP records 94....

CO Founded Shpongle with Simon Posford..in 1996....

1200 Micrograms in 2002.........

Tip been going for 20 years...just Psy trance', 'en');
INSERT INTO "bios" VALUES (39, '', 'fr');
INSERT INTO "bios" VALUES (39, 'Sensient is Tim Larner from Melbourne, Australia. His music is deep, intelligent, progressive and absorbs the influence of many other musical styles (minimal, techno, breaks, glitch, jazz fusion) He has released 5 solo albums plus many compilation tracks on respected labels like Iboga, Plusquam, Flow, Plastik Park, Tatsu, ZMA and more. He also has made remixes for some of the big names in the scene including Boris Brechja, Ace Ventura, Perfect Stranger, Shiva Chandra and BigWigs. In 2003 Sensient created Zenon Records which has now developed into one of the world’s best and most respected psytrance labels. Zenon has formed an entire musical movement which has heavily influenced the Psytrance Music/Festival scene worldwide. Sensient has played at some of the biggest international festivals including Boom, Ozora, Voov, Universo Parallelo, Glade, Rainbow Serpent, Sonica, Fusion and more.', 'en');
INSERT INTO "bios" VALUES (40, '', 'fr');
INSERT INTO "bios" VALUES (40, 'David AKA Shotu discovered electronic music in 1996 at free parties in France. In 2003 he met the Hadra tribe, and here he soon found his place. He has played across the global at parties and festivals such as Universo Paralello and Boom Festival. Shotu style is night-time psychedelic style and has 3 albums under his belt ''Jungle Expedition'' ''Conception'' and ''Friends"', 'en');
INSERT INTO "bios" VALUES (41, '', 'fr');
INSERT INTO "bios" VALUES (41, 'SKYFALL is the Progressive Trance project of Daniel Bernardo Aka MENOG.
He''s been heavily into music since childhood. After having played guitar and drums in his Metal Band in early 90s, he got introduced to underground Electronic Music scene in Portugal around ''95. His first visit to the Boom Festival in 1998 inspired him to start his own project MENOG.
In 2002, Dani released his first album "Natural Behavior" as MENOG and has been rocking parties allover the globe ever since.
In 2013 Daniel decides to explore the more spaced and slower side of Psychedelic Trance and formed SKYFALL; the name inspired by his passion for skydiving.
His First EP was Released on Sourcecode rec , "Higher Self" was 17th on top 100 on Beatport.
Dani joined NANO rec just after the release of his first EP
His "Not Quite Human" EP was released on NANO November 2013 and reached 7th Place on Beatport.
His third EP “Freefall” came out at Iono Music and Reached number 5 at TOP 100 .
SKYFALL stands for massive, driving Progressive grooves that take the dancefloor on a journey, a perfect balance between cybersonic soundscapes and human musicality/emotions.
Stay Tuned, as SKYFALL is bound to take the scene by storm.', 'en');
INSERT INTO "bios" VALUES (42, '', 'fr');
INSERT INTO "bios" VALUES (42, 'Name behind Sonic Entity is Nikola Gasic from Belgrade, Serbia.
Started with production at the beginning of 2008.
after a decade of involving into Progressive and Psychedelic scene.
This young producer bringing fresh sound to the scene,
that he will try to show on incoming releases and live shows.
His debut album ''''Twister'''' is released in summer 2012
on Yellow Sunshine Explosion.
Also have EPs, single tracks and remixes on many labels such are Iono Music,YSE Recordings,Tesseract Studio,BMSS Records
Synergetic Rec,Midijum Records,Ovnimoon rec...', 'en');
INSERT INTO "bios" VALUES (43, '', 'fr');
INSERT INTO "bios" VALUES (43, 'DJ Masaysa`s project- Spectra Sonics.
With his serious middle groove style, and amazing live sets, Spectra Sonics is well renowned within the Japanese trance scene as a top middle groove psy-trance artist. In 2010 he joined Grasshopper Records, but until then he had had many offers and requests for music by labels in Japan, such as Wakyo Records and Grasshopper Recs. He has also had offers from European labels such as 24/7Records and Mutagen Records. At still a very young age, Spectra Sonics is well expected to be a leading next generation artist.', 'en');
INSERT INTO "bios" VALUES (44, '', 'fr');
INSERT INTO "bios" VALUES (44, 'DJ Starling from one of the World’s party havens - Goa, India was born in the late 80s. His induction to DJ’ing came through trance , full on twilight at the age of just 14 and later progressed into different genres of music.

For nearly 10 years now he has played for some of renowned clubs and party places in India and around the globe.

Together with his father, he owns one of the oldest well known places for moonlight parties in Goa, “Hilltop”… Needless to say, that he is the in-house DJ for the weekly parties held at Hilltop.

It’s is scintillating electromagnetic music that entertains crowds of 10,000 and more, at some of the biggest events in Goa and that being the Christmas and New Year parties at Hilltop which has been a crowd puller for nearly 27 years. These annual events, each carry on for nearly 36-72 hours and are attended by not only party hoppers from around the world but also by well known media celebrities.

Of recent Starling has come out with Hilltop’s first VA compilation – “The Hilltop Chronicals Vol. 1” with international artists such as Orca/ Phatmatix/ Psymetrix/ Ajja/ Arjuna/ Rev/ Entropy and many more....The focus has been to create a sonic journey. He feeds off the energy from the dance floor and with music running blue in his blood. He absolutely loves to mix for big events....

In this journey as a DJ, Starling has also played alongside international artists such as Protoculture, GMS, Psychotic micro, Panayota, Freaked frequency, Z-Machine, Atomic pulse, Alien project, at various venues in (Dubai/spirals)(Goa/India), (Pebbels/Bangalore), (Bullfrog /Mumbai), (Pasha /The park, Chennai), (Bottles & Chimney /Hyderabad), (1 lounge /Pune) , Nepal...and the list goes on… In India he is much sort out after for his diverse music sense and has spun alongside India’s Famous Dj’s such as Clement, Ajit, Joel, Rohit Barker, Whosane!, Ivan , Jalebee Cartel, Lost Stories etc.. to name a few.

His style varies from Full-On to Aggressive Twilight packed with rolling, powerful bass lines and twisted sounds…. On his flip side of entertaining any crowd Starling has a flare with mixing Commercial, Progessive, Tech, Minimal House and Hip-Hop.

This Lad is known to bring the crowd in a state of a frenzy!!!', 'en');
INSERT INTO "bios" VALUES (45, 'Montpelliérain de souche et Issu du milieu rave du sud la France ; Il fait partie de ces artistes pour qui la musique, plus qu’une passion est devenue un véritable art de vivre.
Steff Tombe dans le chaudron électronique et le djing dés le début des années 90, d’abord marqué par les Spirales Tribe sur une plage du languedoc… puis les Boréalis et des artistes comme : Andrew Watherall, Alex Patterson (the Orb), derrick may ou System seven …
Aujourd’hui Fort d’une solide carrière sur la scène international house /teckno , son style et sa musique se sont façonnée au grés des rencontres ,des dancefloors sur les 4 continents (Singapour, chili ,Tunisia,Dubaï ,Brésil ,Argentine, USA ,Ibiza, Malaysia ,Allemagne ,Italie, Nouméa ,Tahiti ,Bora Bora ,Thaïlande…)
Résident en Espagne : ‘en la isla blanca ‘ durant 4 ans, Il decouvre et goutte aux machines analogiques et à la production dans les studios d’Etnica cachés dans le nord d’Ibiza ou il passe la plus part des longues journées et soirées d’hiver dans le début des années 2000.
Il intègre le temps d une année le collectif Unclesound à son retour en France ; de cette collaboration naissent des productions et remix deep house et tribal sur des labels tel que : Mercado Parallelo Record Kandy Music, Etoka Record ou Clean House.
Aujourd’hui actif au sein de ‘Reprezant MTP’ à Montpellier et Résident des incontournables soirées Trance Progressives SHROOMs proposant tous les mois dans le sud de la France des musiciens francais et internationnaux (d root ,lost shaman ,flowjob,Bright light,sysphe,Polaris...)
Il est aussi l’architecte de la programmation musicale du Quartz Festival, présentant des artistes de talent de la scène deep underground et trance tel que : Lish, Rémy Otezuka, NU, Matthew Dekay ou kokmok…
De retour En studio en ce debut 2014, il travail avec seb plazolman sur leur nouveau projet commun « SUNRISER »
Sur le dancefloor, Steff est du matin. Il distille au » pti dej « une musique tribal et colorée : à la frontière entre une Deep Teckno acidifiée et une Trance Progressive guerrière, un savant mélange de grooves épurés, et de leads hypnotiques le tout posés sur de solides basslines …', 'fr');
INSERT INTO "bios" VALUES (45, '', 'en');
INSERT INTO "bios" VALUES (46, '', 'fr');
INSERT INTO "bios" VALUES (46, 'Sub-Zero is the solo project of Adelino Afonso born in 1984 in Coimbra (Portugal). Since his childhood, Adelino always had a strong relation with music. At 13 he got his first electric guitar and with it the need to create his own music.

In the year 2000 Adelino started going to techno and psy trance parties. There he found his electronic musical language. He started Djing and experimenting with various sequencers such as “E-jay” and “Fruity loops” with which he learned the basics of electronic music production.

During the year 2002 Adelino joined forces with his friend Mario Rodrigues (Droidbeatz) and created a project that is now known as “Poizon”.
After some years of gaining musical and production knowledge, Adelino created the "Sub-Zero" project that was originaly known as “Reset”, as a way to explore different psychedelic fields. His music is defined by low rolling fat basslines, classical psychedelic synth work, intricate percussive rhythms, imaginative noises from synthesizers and lots, lots more ...', 'en');
INSERT INTO "bios" VALUES (47, '', 'fr');
INSERT INTO "bios" VALUES (47, 'Carley made to satisfy ravers, audiophiles, critical listeners,and those who were looking for more content, Sub6 took a leading part of pushing the Psytrance out of the underground forests to the masses around the globe. their fresh fusion of Psytrance and other electronic styles eventually became the "mainstream within the genre"', 'en');
INSERT INTO "bios" VALUES (48, '', 'fr');
INSERT INTO "bios" VALUES (48, 'Sulima is a much respected Moscow based psychedelic trance producer. He is one of the artists behind duo Manifold, as well as one of the experienced post-production engineers in Moscow. Since 2001 Sulima starts to write a psy-trance. Since 2003 Sulima create project Manifold. 2005 - released album on Dejavu Records. 2009 - released solo album on lable BlitzStudios Rec. In 2011-2013 - released EP and album on Geomagnetic records. The past few years has been actively playing at parties.', 'en');
INSERT INTO "bios" VALUES (49, '', 'fr');
INSERT INTO "bios" VALUES (49, 'Sybarite had it''s first release ''Mint Condition'' back in 2002 on Lucas'' compilation Reefer Madness on TIP Records. Due to Lucas'' extensive touring and Ric''s commitment to his film and TV music productions, they didn''t have much time to make music together until they both moved near each-other in the West country of England.
After the success of their track ''Liquid Lunched'' on Lucas'' now legendary collaboration album ''God Save the Machine'' (TIP 2006) they have been stepping up the pace. Their remix of Avalon''s ''Play It, Bahia" released on Nano records in 2012 was highly revered as was their remix of the ManMadeMan classic ''Halt Production'' (Goa productions 2013). They then had a string of releases on labels such as Nano, Digital Om, Moonloop, and a downtempo track ''Wolfgang''s tea party'' on Raja Ram''s popular album ''Pipedreams'' (TIP). Their most recent track ''Sonic Staircase'' was released in 2014 on Dj Tristan''s album ''UK Psychedelic'' and has been rocking dance-floors worldwide.
There''s no stopping this dynamic duo now.. with the success of their debut live performance at Planet Shroom in the UK in Jan 2014 they are super excited and honoured to be playing their first gig in France at the awesome Hadra Festival.', 'en');
INSERT INTO "bios" VALUES (50, '', 'fr');
INSERT INTO "bios" VALUES (50, 'Juric Goran aka TALPA, born in 1982. in Subotica, Serbia. At the age of 15, he started to make electronic music out of hobby. It lasted for 3 years, the time it took him to know the basics and the tools of production, which he began to use more and more every day for 16 hours. Even without any serious works, the first that showed the interest in his music was label from Australia, Sundance records, which released two of his tracks on a compilation. That’s when a serious work in the field of production began. Soon after that he made a deal with the same label for an album which was released in October 2004, named “Art of Being Non”. After he served the army in 2005, began the first appearances in the world .In the year of 2008. his second album "When The Somberness Becomes A Game" was released for same label, giving everyone a unique view on his music. In the meantime, he was releasing his tracks on various labels, such as Suntrip Rec, UP / MMR Records. Fabula Rec, Boom Rec, Digital Psionic. His 3rd album was released back in 2011 for the same label under the name "The Path". Since 2009 Talpa has been part of serbian label TesseracTstudio.', 'en');
INSERT INTO "bios" VALUES (51, '', 'fr');
INSERT INTO "bios" VALUES (51, 'Tilt begins the mix in 1993 and crosses by several style like tek and hardtek, play with artists of international fame as Garnier, Zinno, Carl Craig, Crystal Distortion...
In 2000, he discovers psytrance, organizes numerous evenings and joins as DJ the French label Avigmatic then the English label Free Spirit records.
Having played in diverse country of Europe, in sides of the biggest, (Infected Mushroom, Neelix, Astral Projection, Painkiller, Space Tribe), he joins the team Hadra under his two projects Sangohan and Tilt.
Revealed by the technical quality and the story of its mixes, within two years, Tilt plays on numerous progressive stages with artists as Day Din, Egorythmia, Loud, Zyce...
We recognize him a dynamic and steady style, tinged with psychedelism, always in search of a technique and of a coherence always more worked.
2013, Tilt realesed his first compilation Progressive Equation on Hadra Records.
https://www.facebook.com/compilprog', 'en');
INSERT INTO "bios" VALUES (52, 'Nguyên Lê, né à Paris de parents vietnamiens, incarne la mosaique multi-culturelle qui fait vibrer la scène du jazz & de la world music de la capitale française. Autodidacte en musique mais diplomé en Philosophie & en Arts Plastiques, Lê a développé un son distinctif qui englobe les influences du rock, funk, du jazz autant que des musiques d’Inde, du Maghreb & du Vietnam. Ses principales collaborations ont été avec Ultramarine, l’O.N.J., Michel Portal, Ornette Coleman, Ray Charles, Peter Erskine, Uri Caine, Vince Mendoza, Herbie Hancock. Depuis 1989 il a produit 14 albums sous son nom, dont le dernier « Songs of Freedom » est considéré par la presse comme « un chef d’œuvre », un « époustouflant déferlement de créativité & d’invention ». Son disque « Purple, Celebrating Jimi Hendrix » a vendu plus de 25 000 albums dans le monde. Il a recu à l’unanimité le Django d’Or de la guitare en 2006, le rang de Chevalier des Arts et des Lettres en 2010, le prix Django 2011 de l’Académie du Jazz & le titre de "Man of the Year" au Vietnam en 2013.', 'fr');
INSERT INTO "bios" VALUES (52, '', 'en');
INSERT INTO "bios" VALUES (53, 'On ne présente plus Shotu, artiste novateur et référence du label Hadra Records, qui parcourt la France et le monde depuis 2002 dans les soirées et festival psytrance. C''est vers sa région d''origine, à Nantes, que Shotu rencontre Suddha. La musique électronique et notamment la découverte de la psytrance les a rassemblé.En 2004, ils sortent leur première track grâce à l''aide du danois Jahbo, qu''ils ont rencontré lors d''une soirée Hadra et qui leur a délivré les secrets du live. Dans leur volonté d''aller toujours plus loin, ils n''ont cessé de composer et de progresser ensemble. Cette année, ils ont initié un projet réunissant leur deux univers : Tweakers, sur le label Hadra Records.', 'fr');
INSERT INTO "bios" VALUES (53, '', 'en');
INSERT INTO "bios" VALUES (54, '', 'fr');
INSERT INTO "bios" VALUES (54, 'This is not a success story.
As the youngest son, growing up in the shadow of his older brother and his heartless gang, Andris came under the spell of colorful machines and press button gadgets very early on in life, creating his own dream world. As he elaborates: “I started using audio editors in 1994, I made house-like music with a program called Fastracker, and we made a remix of Queen’s Another one bites the dust song with a record player and a pc...”, which in itself is enough to suggest fishy aesthetics. Then came goatrance.
He first came across this genre in 1996 at a party organized by Kenguru, where he fell in love with the style and following the path of a spoilt dandy, he spent quite a lot of pocketmoney on vinyl records.
At that time in Hungary there were already many talented DJs spinning psychedelic music, like Oleg, Jirzsij and Virág, but the psytrance scene was still in its infancy, so it was easy for Andere to have his place in the sun. (This is history, but if healthy goasnobism had developed at the right time in Hungariandom, Wegha Andere might not have been able to crush the parties that the committed performers cultivating the profession with humbleness and on an artistic level had built up with engineered preciseness during the night.)
So it happened in 1998 that Wegha’s name also appeared on the lineup of the Halásztelek Party, regarded cultic today as the Hungarian pioneer of open-air party series, and then stayed there throughout a whole decade, for what seemed an eternity.
Then, as one of the permanent actors of the Budapest psychedelic scene, he kept ascending towards the world’s stage at the side of the great and unrepeatable dj Virág: from 2000 to 2005 they hosted Sunday Deep Smile sessions to fame, and in 2001 Turbopauza was formed, a popular live-act (bass guitar, conga/percussion, KORG SX sampler, Roland Phantom keyboard), as many believed, deservant of a much better fate than met.
The formation had lots of gigs ''till 2008, among them on the main and chill stage at Ozora.
(http://www.myspace.com/turbopauza , soundcloud.com/turbopauza)

This, of course, in no way satisfied Wegha’s aspirations: from 2007 he was also co-managing a club, the justly infamous/famous Szociál KLub in Közraktár on the bank of Danube, the headquarters of his own club-nights featuring international psytrance artists, with which the inglorious achiever has appeared at several places in the Budapest nights since then.

Ten long years had to pass for this pleasant fairy-world to get fed up with Andere’s conceit, arrogance and inexpertness... but finally, at the 2008 O.Z.O.R.A. Festival (psychedelic tribal gathering) the visitors did not have to read his name among those in the lineup. However, Destiny - this capricious and cruel dictator of our lives - once again put the long-suffering, treehugger colony’s tolerance to the test: the Organizers could do nothing else but put Wegha on the main stage in place of Simon Posford, who was verifiably absent due to sickness. The ‘dog and pony show’ then called a dj set, was followed by a longer, almost ethereal silence, to our biggest relief... and though our plain old common sense would have made us expect seven years of Canaan after seven years of famine, unfortunately, belieing the Bible, after a short break, 2010 became the truly black letter year for every refined partygoer, for this was when Wegha Andere, this wolf in sheep’s clothing ‘einstand’-ed the best weekday, Thursday: at Margit Island’s duly popular ChaChaChaCabrio in the summer and setting up base at the U26 from autumn. Then everyone''s fears came true: there was a new crack in the psychedelic space-time continuum: at the revived Halásztelek in the Pilis, and we could also hear his unworthy shows on Avatar too.
And all of those people who still had hopes that the disturbance in the Force would cease and the divine order of the Universe would be restored, underestimated Wegha, this psytrance Anakin Skywalker.
Because this villain, having deposited his soul at the feet of the Devil by way of a Faustian contract, would sure enough yank even the angels from the heavens – there’s no question about how he got to the head position of an international festival. Stuff like this happen and that’s it.
/olrajtovics&novishari/', 'en');
INSERT INTO "bios" VALUES (55, 'Yamaga est DJ resident du labal Hadra depuis 2009. C''est également un des membres fondateur de l''association.
Ses DJ sets énergiques sont tournés vers la psytrance nocturne et mettent en avant les sons psychedeliques et les ambiances colorées de la scene psytrance anglaise et internationale.', 'fr');
INSERT INTO "bios" VALUES (55, '', 'en');
INSERT INTO "bios" VALUES (56, 'DJ et organisateur de party depuis les années 90, ACID BART est un des fondateurs du collectif TPR Sound System qui a diffusé son style musical principalement en région grenobloise mais aussi dans le sud de la france et dans plusieurs pays d''Europe.
ACIDBART évolue toujours dans différents style electro tout en gardant sa ligne directive pumpping et mental. pour Hadra ACIDBART prépare un set tekno minimal progressive.', 'fr');
INSERT INTO "bios" VALUES (56, '', 'en');
INSERT INTO "bios" VALUES (57, '', 'fr');
INSERT INTO "bios" VALUES (57, 'Adham Shaikh is a Juno and Emmy Award-nominated music producer, composer, sound designer, and DJ who brings his uniquely powerful global sounds to the world stage and screen, not to mention many a crowded dance floor.

Adham has been nominated for an emmy award for his contributions to National Geographic’s Untamed America’s and has scored original soundtracks for numerous film and television productions ,
Sacred Planet (Disney Imax), Velcrow Ripper’s Fierce Light, Occupy Love (National Film Board, Fierce Light Films), The Edge of the World: BC’s Early Years (Knowledge), Rainbow Jaguars’ Earth Pilgrim, Word Love (Avanti Pictures), Getting Married (TVO), and Secrets: A Parent’s Guide to the Teen Sexual Revolution (CBC’s Passionate Eye, Make Believe Media), for which he received a Leo Award nomination for Best Musical Score in a Documentary Program. His music has also been licensed to an extensive list of media productions, including Suzuki Speaks (Avanti Pictures). Look out for his latest original work on the television series Deconstructing Dinner (itv) and Working People (Knowledge)

As a music producer and composer, Shaikh skillfully weaves organic and electronic sounds into global music tapestries that take listeners on sonic journeys transcending time and place. He has released 13 albums and many individual compositions, among them the 2004 release, Fusion, which was nominated for a Juno Award (Canadian Grammy) in the World Music category and Universal Frequencies (2010) which won best album (BC Independent Music awards) Music labels from around the globe have released his music, including White Swann, Interchill, Dakini Records, Electronic Soundscapes, Liquid Sound Design, Native State, One World Music, Water Music, Nia, Sounds True, Stoned Asia, and the infamous Six Degrees. Recently, Shaikh co-produced and mixed the award-winning debut CD for global fusion group Delhi 2 Dublin, and has finished re-mixing tracks for internationally renowned global bass artists Nickodemus, Deya Dova, Kaya Project, Tripswitch and David Starfire. Shaikh has most recently completed remixes for Delhi2Dublin, Deset Dwellers, Eccodeck, Issa Bagayogo, Ganga Giri , and the Footsteps in Africa Project (Turag remix)

Shaikh’s work as both a live performer and a DJ has taken him to international festivals, events, and night clubs, including the Waveform Festival (UK), Envision Festival (Costa Rica), Water Women Festival (Ecuador) Samothraki Dance Festival (Greece), Dakini Nights (Tokyo), Luminate (New Zealand), The Maori Peace Festival at Parihaka (New Zealand), Hadra Trance (France) and Boom Festival (Portugal). He has toured extensively across Canada, with highlight performances at the Vancouver Folk Festival, Vancouver Jazz Festival, and the Shambhala Music Festival.
Highlights include headlining sets at Royal Ontario Museum, Basscoast Festival, Shambhala Festival , Lightning in a Bottle,(ca) Beloved Festival (Or), Symbiosis(ca), Raindance (ca), SonicBloom (co), Meme
(Winnepeg) and Snowblower Festival (Calgary)
From Folk and Jazz Festivals to premier electronic festivals and clubs, adham brings the show and the heat for any occasion', 'en');
INSERT INTO "bios" VALUES (58, '', 'fr');
INSERT INTO "bios" VALUES (58, 'Økapi is Filippo Paolini, an Italian turntablist and sample cutup artist. Filippo has recorded several solo albums, as well as recording in the duo Metaxu and with the trio Dogon. He has performed live for national Italian State radio broadcasts (RAI) with renowned avant-turntablist, Christian Marclay and collaborates with numerous international artists such as Mike Cooper, Peter Brotzmann, Mike Patton, Matt Gustafson, Zu, Damo Suzuki, Andy EX, Kawabata Makoto, Metamkine...
ØKapi''s album releases illustrate his unique and edgy use of turntables and computer beyond the hip-hop school of chopped up music, creating music that veers from orchestral to lounge with quirky experimental electronics that maintain a delicate and spacious sound throughout.
His amazing skills at sample mixing are simply outstanding and during Økapi live sets he mixes: techno/broken beat/electro-/8-bit with melodies from Klezmer, polka, children''s music, idiot pop and hawaiian classics to name but a few.....', 'en');
INSERT INTO "bios" VALUES (59, 'B-Brain est issu d''un sound system du sud de la France.
Ses productions sont composés d''un mélange éclectique oscillant entre ses influences reggae/dub et jungle/drum&bass .
avec une musique tantôt douce et melodique, tantôt rythmée et endiablée. Mélange de styles savamment maîtrisé.
Son ?album est une ode à ses influences world, hip hop et dub. On y découvre également son affection pour la jungle
et pour les phases de scratchs, qui rythment un bon nombre compositions et donnent sa particularité au style B.Brain.', 'fr');
INSERT INTO "bios" VALUES (59, '', 'en');
INSERT INTO "bios" VALUES (60, 'BeO se veut le reflet d''une rencontre artistique expérimentale entre le son et l’image. Green Stone compose la musique teintée d’électro, de dub, d’ambient, de drum & bass et de hip-hop ; Yo crée le grain sonore rétro, underground, cinématographique ; Melvin Haze réalise ensuite un film narratif abstrait et Makar conçoit les atmosphères lumineuses. Leur live « All One » évoque une réflexion sur la place de l’homme face à son environnement. Chaque performance scénique est unique, et amène le public à rêver, danser et ... s''interroger ?', 'fr');
INSERT INTO "bios" VALUES (60, 'BeO is an experimental sound and image project carried out by a pool of artist.
Green Stone composed the music tinged with Electro, Dub, Ambient, Drum & Bass and Hip-Hop. Yo brought a retro, underground, cinematic sound touch. And Melvin Haze made a narrative abstract film. Their live album "All One" wants to be a reflection on the place occupied by man in his environment. Every stage performance is unique and makes the audience dream, dance, and perhaps reconsider ideas?', 'en');
INSERT INTO "bios" VALUES (61, '', 'fr');
INSERT INTO "bios" VALUES (61, 'By The Rain is a music project by Burak Ozsoy.
Burak was born in turkey, but since he has lived in many countries such as Australia, Bahrain, Malaysia, Thailand, Singapore, USA, Turkey; sharing his music and art.

it was long ago when he started music with bands and rocking heavy tunes.
in the last few years, he has been actively in the psychedelic scene with his live chill out project By The Rain and blasting psytrance dj sets, Releasing By The Rain Music on records such as; Trimurti records,Microcosmos records, Altar records, Spiritech records, Speed sounds records & various other label compilations.
Burak has been an active part of Epic Tribe in Malaysia, creating most artworks for the psychedelic community and managing Belantara’s Alternative / chill out stage for the last 2 gatherings.

Burak is also a part ‘’Art of Nabura’’ which is an art project with his partner Nabeela.

you can check out his work and get in contact with him:

https://soundcloud.com/bytherain
https://www.facebook.com/bytherain
https://www.facebook.com/nabura
http://burakozsoy.carbonmade.com', 'en');
INSERT INTO "bios" VALUES (62, 'Adepte de dub et son techno, commence la compo et mix dubstep sur vinyl il y a 5 ans. Arpente le sud ouest pour débusquer free party et autres pirateries. Découvre la scene psychédélique au cours d''excursions dans le bush australien et les hauteurs Népalaises (Slytrance, Mr bill, Merkaba, Grouch, Hedonix, Psykovsky...).

Membre des associations Ethereal Decibel Company et TSF collectif.
Membre du collectif Stendhal, lieu de vie autogéré a Paris.

Live PsyDub
Collaborations avec Abstracker https://soundcloud.com/4bstr4ck3r
DJ set psygressive, dark prog, dark techno, psytrance', 'fr');
INSERT INTO "bios" VALUES (62, '', 'en');
INSERT INTO "bios" VALUES (63, '', 'fr');
INSERT INTO "bios" VALUES (63, 'Dripping, twisted sound-scapes and driving dubbed-out bass lines take centre stage with Globular''s music... Having been initiated into the world of psychedelia via the likes of the incredible ''Mystery of the Yeti'' albums, ''Are You Shpongled?'' and ''Blumenkraft'' sometime around mid ''05, Globular began playing around with sounds to see if he could come up with his own audible hallucinations... Since then his styles have constantly merged and morphed, and are continuing to do so, whilst keeping a steady undertone of trippy dub influences, designed to tickle the mind!

Nearly all of Globular''s music is available for free/donation from globular.bandcamp.com...

"This guy’s music is stellar. From Bristol, UK, you can just tell that this guy puts an immense amount of energy into making his detailed exotic sounds. It’s feel-good music that seems to offer energy and healing power as you listen. His sounds are very organic and have a soothing, natural flow, yet are delightfully psychedelic and drippy. It’s kind of like very controlled chaos in the best way possible. This music invokes a sense of tribal roots blended oh so nicely with electronica that is so on point. If you’re into Ott, I have no doubt in my mind that you’ll enjoy Globular’s tunes." - Taradactyl', 'en');
INSERT INTO "bios" VALUES (64, '', 'fr');
INSERT INTO "bios" VALUES (64, 'I''m 27 year old audio engineer born and raised in sweden. I started playing drums by the age of 5 and later in my teens I started to produce drum n bass music on my computer. I heard infected mushrooms album "converting vegeterians" and I was hooked on psychedelic downtempo music and shortly after that hinkstep was born. Experimenting with diffrent style and made it my own.', 'en');
INSERT INTO "bios" VALUES (65, 'Itchy & Scratchy regroupe deux amis passionnés de musiques électroniques, et plus particulièrement de techno.
Ils ont intégré le label Hadra courant 2013.

Leur Musique est un mélange de Bass Music Tribale saupoudrée d''une puissante psygressive en passant par des grooves dark prog à l’esthétique techno minimalistes, le tout nappé d''une atmosphère assurément Dark.

Sur scène, ils proposent des mix entre live et DJ sets avec une évolution et une cohérence qui leur est propre.
Apprêtez vous à explorer l''inexplorable !!!', 'fr');
INSERT INTO "bios" VALUES (65, '', 'en');
INSERT INTO "bios" VALUES (66, 'C''est bercé par la musique Celtique et le mouvement Gothique , que découvre mon addiction aux mélodies envoutantes et profondes.
Je découvre la scène électronique en 2002 sur les Teknivals français .

Attaché aux mouvement underground grenoblois , en 2003 je commence la composition de "Live" sur machines électroniques analogiques , puis l''écriture de " track" via ordinateur .

Toujours à la recherche de nouvelles textures , c''est naturellement que je me tourne vers le "DJing" en 2008.
En 2012 , je fais mon entrée en tant que "DJ psy-prog" dans l''association " Les Rytmes Actuels" au coté de compositeurs et Djs de renoms tel que DJ SYDNEY ,GNAÏA , DJ LOIC , THE SPACE BAR ...

Aujourd''hui inspiré par le bien-être , la nature et l''amour , j''évolue sur des rythmes Downtempo , Chill et PsyProgressive .', 'fr');
INSERT INTO "bios" VALUES (66, '', 'en');
INSERT INTO "bios" VALUES (67, '', 'fr');
INSERT INTO "bios" VALUES (67, 'Kliment Dichev is born in Sofia, Bulgaria in 1981. Characterized with a deep atmosphere and solid groove, his sound lifts up the crowd into an emotional journey on the dance floor. Featuring few projects, the variety of his music productions ranges from slowest dubby ambient to faster psychadelic project Once upon a Time. Albums and eps on Zenon records, Electrick dream, Blue hour sounds, Iboga etc.', 'en');
INSERT INTO "bios" VALUES (68, '', 'fr');
INSERT INTO "bios" VALUES (68, 'At approximately 10 years he began to buy those vinyls that arrived at the music stores of Valencia.
In 1990 he made a program on Radio Klara, called “Acid Combustión” and based on EBM, Acid, New beat, Trance and Electro styles.
In 1995 he listened to Psychedelic Goa Trance and it got him hooked completely, because it had the best things of those styles.. In 1996 he bought a computer and it is here when his period as a musician begins, initially experimenting with many styles and developing his production.

After some years producing Psychedelic Trance as PSYCHO ABSTRACT, in 2004 he began a new project focused in ambient & Down Tempo music under the name: LAB''S CLOUD.

The Lab''s Cloud Style:
Down Tempo, sweet, floating. A smooth organic ride. Beautiful and delicious music oriented to chill outs, with space atmospheres, some chilling vocals, some ethnic sounds and a touch of real instruments (percussion, guitars..etc). A definite trip for the soul.', 'en');
INSERT INTO "bios" VALUES (69, 'Depuis quelques années, Lakay propose un son métissé et coloré aux influences multiples entre la world music, le dub, la jungle et la trance. Trompettiste et multi-instrumentiste, entouré d''artistes de differents horisons comme la danseuse Yshar ou le saxophoniste Renaud Vincent, ses concerts sont des moments d''échange et de partage favorisant le voyage de l''esprit. Après avoir joué son album Enjoy People dans differents festivals européens, lakay revient avec un nouveau live tout frais et toujours aussi psyché, un nouveau voyage a découvrir', 'fr');
INSERT INTO "bios" VALUES (69, '', 'en');
INSERT INTO "bios" VALUES (70, '', 'fr');
INSERT INTO "bios" VALUES (70, 'This project began when two travellers, Michal and Julien, met Metehan Cifti (santur player) in 2010 in Istanbul. Since then they cooperate together and enjoyed playing in the streets, cafes and well-known performance halls in Istanbul.

At May 2010 they made their first professional demo album `Istanbul`. At the summer of the same year they played in Europe in festivals. With the time they audience increased in Turkey and in the Middle East, and create the base of the band in this inspiring land.

In November 2011, they release their second demo album `Life sometimes doesn’t give you space` with cooperation with additional new musicians including the bass guitarist Fethi Hıncal, the percussionist Gürkan Özkan and hardy-gardy player Eléonore Fourniau.

In 2012 they made their first professional quality studio album, `Life sometimes doesn''t give you space'', including many guests musicians.

This project is the fusion of different ethnicities and cultures, the singer is Israeli from Iranian origin, the santur player is Turkish the guitarist is French and they cooperate with musicians from different origin as well (Turkish, French and more)

Despite the political tensions in Middle East affecting the daily life of the musicians, the band is still highly creative and gives through music all the love, hope and peace that it carries in his heart with the wish that every human will follow the vision of a world full of light.', 'en');
INSERT INTO "bios" VALUES (71, 'Pour sa 3ème et dernière venue sur le Festival Hadra de Lans en Vercors, Little Tune et ses 2 compatriotes formeront pour l''occasion un trio hybride alliant le mixe Dj, le scratch et le saxophone. Ils proposeront un set balkan-beat & electro-swing associés à d''autres sonorités tels que l''electro-funk, ghetto funk ou ghetto jungle.
Venant tous les 3 d''univers musicaux différents, Litte Tune, Dj Akor au scratch et saxo Jibiz se font remarqués depuis longtemps dans leurs projets et groupes respectifs (BSP Crew, Monkey Theorem, Shamanik sound System, Patko, Les Kimisk...) qui tournent tout au long de l''année dans les salles et festivals de France et de navarre
RDV pour cette rave manouch avec ces 3 joyeux gitans dj et musicien et leur set exposif qui pourraient faire bouger même le nouveau maire de Lans en Vercors!', 'fr');
INSERT INTO "bios" VALUES (71, '', 'en');
INSERT INTO "bios" VALUES (72, '', 'fr');
INSERT INTO "bios" VALUES (72, 'some of the alternative stage / chill out in wich had the honor to be part of:

Doof Festival 2013 - Israel
Sonica Festival 2013 - Italy
Modem Festival 2013 - Croatia
HillTop Festival 2013 - India
Human Evolution 2013 - Italy
Modular Synergy 2013 - India
Modem Festival 2012 - Croatia
Freaks Of Nature 2012 - Germany', 'en');
INSERT INTO "bios" VALUES (73, 'Mizoo est un mélomane passionné depuis son enfance. Il découvre la musique électronique en 1988. Il est actif derrière les platines depuis 1992. Il rejoint Ultimae records en 2007. Il intègre également Moonloop records en 2009. Depuis 1992, il propose ses mixs aux 4 coins des horizons helvétiques et francaises, avec des incursions au Maroc, en Grèce, à Londres, en Italie, en Turquie et en Hongrie. Il oscille entre ambient, electronica, downtempo, deep tech-house en proposant également une touche de psychedelic progressive trance, le tout mélangé avec des extraits de films. De 2009 à 2013, Il a sorti 4 compilations pour le festival TimeGate en Suisse et Greenosophy sur Ultimae records. En décembre 2013, il a aussi intègré one feel music.

Pour le set @alternatif floor il proposera un mélange de downtempo_electronica & deep progressive. ', 'fr');
INSERT INTO "bios" VALUES (73, '', 'en');
INSERT INTO "bios" VALUES (74, '', 'fr');
INSERT INTO "bios" VALUES (74, 'London-based DJ, promoter and music journalist, Massimo Terranova aka Nova is a prolific sound selector at the forefront of the ambient/electronica music scene. His flawless mixture of ambient, world beats, IDM, downtempo and dub textures, blended in pure London''s eclectic style, have made him a truly sought after talent in the eclectic DJ world.', 'en');
INSERT INTO "bios" VALUES (75, '', 'fr');
INSERT INTO "bios" VALUES (75, 'Nuage, is Olivier, a French musician living in India, who has been producing music since the 1980s. He has been part of several projects including 2 Puissance 30, I Fly, Jaffa Project, Manitu, Virtuart, and Underhead. Currently he is working on several projects including Nuage, Sacred Tree Portal, and Wavespell.', 'en');
INSERT INTO "bios" VALUES (76, '', 'fr');
INSERT INTO "bios" VALUES (76, 'I Fly, 2 R.A.R. Crew, Bambara Vibes, Djul''z Concept, Karamatix 767, Mont Blanc, Orenok.....', 'en');
INSERT INTO "bios" VALUES (77, '', 'fr');
INSERT INTO "bios" VALUES (77, 'Christian Borgmann aka DJ Samsara was born in 1988, on a small island in the southern part of Denmark. But now lives in the northern part, where he has lived most of his life. His first contact with electronic music was around 2003 and later in 2005 he discovered psychedelic trance, which immediately caught his attention.

As the interest of this newly found genre grew and after attending several parties and festivals in- and outside Denmark, he decided to jump behind the decks himself. In 2008 he got his first gig and after that, he has played more or less in every corner of Denmark and in countries like Germany, Bulgaria and Turkey.

In 2011 he signed up with 2 psychedelic trance labels, Damaru Records based in Germany and Vantara Vichitra Records based in India.

2011 was also the year, were he together with his close friend DJ Furvus, started to work on their first charity compilation, named "A Conversation Between Two Trees" which got released in start of 2012 on Critical Beats. They both agreed on, that this compilation shouldn''t be the last of the kind, so later in 2012, they started to co-operate with another charity/non-profit label, called "Random Records", who were more psychedelic trance oriented. So in the summer of 2013, they released their second charity compilation, named "Raindance", which turned out to be the most selling release on Random Records.

It was not only psychedelic trance music, which caught his fascination, electronic ambient was the other sub genre that did too. After some years with many thoughts of playing ambient aswell, he met the ambient DJ and producer Erot (Tore Mortensen) who inspired him to start. In 2011 the two friends created a ambient project “Erot & Samsara” that already, short after the initiation, joined the DJ team of the Canadian based label, Altar Records and so did he with his solo project as well.', 'en');
INSERT INTO "bios" VALUES (78, '', 'fr');
INSERT INTO "bios" VALUES (78, 'Sephira, a Blue Magnetic Monkey whose tool of choice is Magic and Whose guide is the 4 / 4 beat of the psybient realm. His debut album showcases Sephira''s unique fusion of downtempo ambient spaces utilising twilight overtones and driving, rolling baselines that takes you on a journey through psychedelic soundscapes of a playful nature. If you have ever heard one of his mixes then you know where his passions lie.', 'en');
INSERT INTO "bios" VALUES (79, 'Sleeping Forest, est une artiste ambient qui a récemment intégré le label Hadra. Son style allie des influences tournées vers le rock psychédélique et la world music à une minutie propre aux musiques électroniques. Son premier album « Rise of Nature » sort le 15 février 2013 sur Hadra Records. L''atmosphère quelle y développe est à la fois lancinante, mélancolique et épique. Ses compositions regorgent de mélodies chargées en émotions et de nappes en constante évolution. On y retrouve des rythmiques entêtantes dans lesquelles les instruments percussifs traditionnels sont mis à lhonneur et de savants agencements mélodiques qui rappellent de nombreux courants musicaux tels que le classique, le rock progressif, le trip-hop ou encore les musiques de film. Elle utilise également de nombreux samples de nature qui permettent une immersion encore plus totale dans son univers. Cet album est fait pour faire vibrer et bercer son auditeur dans un monde de plénitude dont seule Sleeping Forest a le secret.', 'fr');
INSERT INTO "bios" VALUES (79, '', 'en');
INSERT INTO "bios" VALUES (80, 'Après une belle entrée sur la scène internationale en 2012 avec leur album « Tamburising – Lost Music of the Balkans », ce groupe hongrois de Tambura atypique entreprend une attaque de charme avec son nouvel album à sortir à l''automne 2014 : « Tamburocket », un OVNI entre archaïsme et modernité, sur les pas de Béla Bartók.', 'fr');
INSERT INTO "bios" VALUES (80, '', 'en');
INSERT INTO "bios" VALUES (81, '', 'fr');
INSERT INTO "bios" VALUES (81, 'Born in 1984 in Carthage (Tunisia), Sofiane Hamila has been a passionate of music from his early childhood days on.

In 2002, he decides to leave Tunisia to live in France where he discovers the whole universe of techno music and rave parties and goes to many technivals in France and Trance festivals all over Europe. He develops his underground network and starts to orientate himself musically more towards Minimal and progressive trance trying to bridge the gap between these two musical domains.
Since 2012, He performs under the name "Soofa“ in many trance festivals and he recently played in major festivals such as Universo Parallelo 12.', 'en');
INSERT INTO "bios" VALUES (82, '', 'fr');
INSERT INTO "bios" VALUES (82, 'Spoonbill is a restless sonic author constantly defying genres and experimenting with the potential of the vast sonic canvas. He has carved a unique niche within contemporary electronic music, building a worldwide reputation for his idiosyncratic sound design and richly textured high production values.', 'en');
INSERT INTO "bios" VALUES (83, '', 'fr');
INSERT INTO "bios" VALUES (83, '', 'en');
INSERT INTO "bios" VALUES (84, '', 'fr');
INSERT INTO "bios" VALUES (84, 'Sun Riser is a new project born in 2014 ,made and raised by Steff Navarro and Sebastien Plazolman ... targeting down beat ,deep progressive and hypnotic music world ...', 'en');
INSERT INTO "bios" VALUES (85, '', 'fr');
INSERT INTO "bios" VALUES (85, 'Or Ron Stern, musical producer born in Israel in the city of Rishon-Lezion, is known as the founder of the world-renowned project SYMBOLICO. Or Ron began his musical life at the age of 13 when he first listened to psytrance music artists such as: Juno-Reactor, Infected mushroom, Sun project, Goa Gil, Raja Ram, Simon Posford and many more. As a kid, psytrance music was a portal for flying with his imagination to long distances. It did not take long till he realized that he has a message to send to the world as a musical expression. Until the age of 21, Or Ron took his musical life only as a hobby; working on trance projects such as the PSYCHOPET on Impulse tracker and Propellorheads Reason sequencers.
At last, 22-year-old Or Ron decided to take it to the next level and go further and deeper into the world of sound and production. It was a turning point when he realized that psytrance alone is not satisfying enough for his colorful, mosaic-like spirit. He began to learn different types of musical genres beginning from the funky, and sometimes glitchy, world of hip hop; to the jazzy world of swing, the groovy sounds of dub, to the ethereal planes of downtempo psychedelic music. Upon the discovery of this new musical portal, the Indigo Child project (the first Symbolico) was born. What is better than a psychedelic project??? - A psychedelic project that lets you move your ass with its sexy rhythm. Indigo child was all about creating those funky Breakbeats with psytrance influences, mixing them with samples from the world of pop.
With time and self-development, the project “SYMBOLICO: Universal Knowledge Decoded Musically” was born.
The timeless and limitless realm of being was a doorway for Or Ron; a doorway of creativity, the same portal which opened at the very beginning when Or Ron listened to psytrance as a child; this portal is what UNIVERSAL KNOWLEDGE DECODED MUSICALLY is all about: a musical expression of a higher level of knowledge. It’s a type of knowledge one stores in the heart rather than the mind.
Or Ron: “For me music is an experience. Good music is an experience which continues even after the music itself is already over”.', 'en');
INSERT INTO "bios" VALUES (86, 'En 2005, Sysyphe intègre Hadra et, rapidement, participe à l’élaboration de compilations (Hadravision(2008) , Hadravison 2(2013)), sort un album « Running up that hill »(2010) et participe aussi à la programmation de la scène alternative du festival Hadra ( Downtempo, psychill, Ambient)
Depuis, en live ou en DJ set , en ambient ou en downtempo, il cultive ses influences gothique mais lumineuses en gardant un seul mot en tête «Harmonies de vibrations et de couleurs musicales »', 'fr');
INSERT INTO "bios" VALUES (86, '', 'en');
INSERT INTO "bios" VALUES (87, '', 'fr');
INSERT INTO "bios" VALUES (87, 'I play progressive and chill out trance music. No matter if style is progg, tech, minimalistic,
downbeat or whatever - common denominator is psychedelic. In approach to music I keep
my old school D.I.Y. philosophy and punk as fuck attitude. Having fun and enjoying is only
goal regarding my musical career. From mid 2009 I became part of Zenon Records family
and Glitchy.Tonic.Records team as a label DJ. Also I have mission to promote trance culture
in Croatia.', 'en');
INSERT INTO "bios" VALUES (88, '', 'fr');
INSERT INTO "bios" VALUES (88, 'Vlastur started his journey into music as a Bass player. Back in the mid-80''s, he was a founding member of the Cohash Funk, an underground funky Athenian group. In 1998 alongside Frequency Freak, fellow Cohash funk guitarist and Spiris, a dubby drummer, formed "Apeuthias Syndesi" now known as Direct Connection, a dub experimental laboratory. They released "Dub Infection" in 2007. Even though he started as a Bassist in Direct Connection, he was the main FX Sound Engineer of the band.

In the meanwhile he was also working as a session musician with various artists and bands. In 2004 he teamed up with Palyrria an ethnic-electronica band and in 2005 recorded "Methexy" with them and has played in many of their gigs.

The (InterAxion Dub) project is the first fully length work of Vlastur. Influenced by all his beloved sounds from Dub to Dubstep trough Psy, Ambient, Drum n'' Bass and Traditional Mediterranean sounds !

Vlastur is the live project of him. The active members right now are: Dark Elf (Synth/Machines) + Tolis aka DreamMadeTeller on percussions and vox, also Akbar on percussions, Wee (Guitar) + G.Verix as a sound engineer, while other have been participating the previous years, like Frequency Freak (Guitar/Synths), Spyro K. (Drums), Elena (Vocals), Pan Kaperneka (Flute) and Dinos Z. on the sound consoles.
Vlastur believes that the sound evolves constantly while the vibe remains the same, from Rock n'' Roll to Punk and from Dub to Psy.', 'en');
INSERT INTO "bios" VALUES (89, 'C''est bercé par la musique Celtique et le mouvement Gothique , que découvre mon addiction aux mélodies envoutantes et profondes.
Je découvre la scène électronique en 2002 sur les Teknivals français .

Attaché aux mouvement underground grenoblois , en 2003 je commence la composition de "Live" sur machines électroniques analogiques , puis l''écriture de " track" via ordinateur .

Toujours à la recherche de nouvelles textures , c''est naturellement que je me tourne vers le "DJing" en 2008.
En 2012 , je fais mon entrée en tant que "DJ psy-prog" dans l''association " Les Rytmes Actuels" au coté de compositeurs et Djs de renoms tel que DJ SYDNEY ,GNAÏA , DJ LOIC , THE SPACE BAR ...

Aujourd''hui inspiré par le bien-être , la nature et l''amour , j''évolue sur des rythmes Downtempo , Chill et PsyProgressive .', 'fr');
INSERT INTO "bios" VALUES (89, '', 'en');
INSERT INTO "bios" VALUES (90, '', 'fr');
INSERT INTO "bios" VALUES (90, 'Kliment Dichev is born in Sofia, Bulgaria in 1981. Characterized with a deep atmosphere and solid groove, his sound lifts up the crowd into an emotional journey on the dance floor. Featuring few projects, the variety of his music productions ranges from slowest dubby ambient to faster psychadelic project Once upon a Time. Albums and eps on Zenon records, Electrick dream, Blue hour sounds, Iboga etc.', 'en');
INSERT INTO "bios" VALUES (91, '', 'fr');
INSERT INTO "bios" VALUES (91, 'I''m Raul Jordan, born in 1978 in Valencia, Spain.
After some years producing Psychedelic Trance as PSYCHO ABSTRACT, in 2004 I began a new project focused in ambient & Down Tempo music under the name: LAB''S CLOUD. Aside of this I''m working for more than a decade as R+D engineer, developing profesional audio processors (EQ, Limiters, crossover...) for VMB Española. At the present with this experience as sound engineer I''m offering a mastering services, too.', 'en');
INSERT INTO "bios" VALUES (92, 'Mizoo est un mélomane passionné depuis son enfance. Il découvre la musique électronique en 1988. Il est actif derrière les platines depuis 1992. Il rejoint Ultimae records en 2007. Il intègre également Moonloop records en 2009. Depuis 1992, il propose ses mixs aux 4 coins des horizons helvétiques et francaises, avec des incursions au Maroc, en Grèce, à Londres, en Italie, en Turquie et en Hongrie. Il oscille entre ambient, electronica, downtempo, deep tech-house en proposant également une touche de psychedelic progressive trance, le tout mélangé avec des extraits de films. De 2009 à 2013, Il a sorti 4 compilations pour le festival TimeGate en Suisse et Greenosophy sur Ultimae records. En décembre 2013, il a intégré aussi one feel music.

Pour le set au chill-out, il proposera un set 100% ambient.
', 'fr');
INSERT INTO "bios" VALUES (92, '', 'en');
INSERT INTO "bios" VALUES (93, 'En 2005, Sysyphe intègre Hadra et, rapidement, participe à l’élaboration de compilations (Hadravision(2008) , Hadravison 2(2013)), sort un album « Running up that hill »(2010) et participe aussi à la programmation de la scène alternative du festival Hadra ( Downtempo, psychill, Ambient)
Depuis, en live ou en DJ set , en ambient ou en downtempo, il cultive ses influences gothique mais lumineuses en gardant un seul mot en tête «Harmonies de vibrations et de couleurs musicales »', 'fr');
INSERT INTO "bios" VALUES (93, '', 'en');

INSERT INTO "sets" VALUES (1, 1408579200, 1408579200, 'DJ', 1, 'The temple');
INSERT INTO "sets" VALUES (2, 1408579200, 1408579200, 'DJ', 2, 'The temple');
INSERT INTO "sets" VALUES (3, 1408579200, 1408579200, 'Live', 3, 'The temple');
INSERT INTO "sets" VALUES (4, 1408579200, 1408579200, 'Live', 4, 'The temple');
INSERT INTO "sets" VALUES (5, 1408579200, 1408579200, 'Live', 5, 'The temple');
INSERT INTO "sets" VALUES (6, 1408579200, 1408579200, 'DJ', 6, 'The temple');
INSERT INTO "sets" VALUES (7, 1408579200, 1408579200, 'Live', 7, 'The temple');
INSERT INTO "sets" VALUES (8, 1408579200, 1408579200, 'Live', 8, 'The temple');
INSERT INTO "sets" VALUES (9, 1408579200, 1408579200, 'DJ', 9, 'The temple');
INSERT INTO "sets" VALUES (10, 1408579200, 1408579200, 'Live', 10, 'The temple');
INSERT INTO "sets" VALUES (11, 1408579200, 1408579200, 'Live', 11, 'The temple');
INSERT INTO "sets" VALUES (12, 1408579200, 1408579200, 'Live', 12, 'The temple');
INSERT INTO "sets" VALUES (13, 1408579200, 1408579200, 'DJ', 13, 'The temple');
INSERT INTO "sets" VALUES (14, 1408579200, 1408579200, 'Live', 14, 'The temple');
INSERT INTO "sets" VALUES (15, 1408579200, 1408579200, 'Live', 15, 'The temple');
INSERT INTO "sets" VALUES (16, 1408579200, 1408579200, 'DJ', 16, 'The temple');
INSERT INTO "sets" VALUES (17, 1408579200, 1408582800, 'Live', 17, 'The temple');
INSERT INTO "sets" VALUES (18, 1408579200, 1408579200, 'DJ', 18, 'The temple');
INSERT INTO "sets" VALUES (19, 1408579200, 1408579200, 'Live', 19, 'The temple');
INSERT INTO "sets" VALUES (20, 1408579200, 1408582800, 'DJ', 20, 'The temple');
INSERT INTO "sets" VALUES (21, 1408579200, 1408582800, 'DJ', 21, 'The temple');
INSERT INTO "sets" VALUES (22, 1408579200, 1408582800, 'Live', 22, 'The temple');
INSERT INTO "sets" VALUES (23, 1408579200, 1408582800, 'DJ', 23, 'The temple');
INSERT INTO "sets" VALUES (24, 1408579200, 1408582800, 'DJ', 24, 'The temple');
INSERT INTO "sets" VALUES (25, 1408579200, 1408582800, 'Live', 25, 'The temple');
INSERT INTO "sets" VALUES (26, 1408579200, 1408582800, 'Live', 26, 'The temple');
INSERT INTO "sets" VALUES (27, 1408579200, 1408582800, 'Live', 27, 'The temple');
INSERT INTO "sets" VALUES (28, 1408579200, 1408582800, 'Live', 28, 'The temple');
INSERT INTO "sets" VALUES (29, 1408579200, 1408582800, 'Live', 29, 'The temple');
INSERT INTO "sets" VALUES (30, 1408579200, 1408582800, 'Live', 30, 'The temple');
INSERT INTO "sets" VALUES (31, 1408579200, 1408582800, 'Live', 31, 'The temple');
INSERT INTO "sets" VALUES (32, 1408579200, 1408582800, 'DJ', 32, 'The temple');
INSERT INTO "sets" VALUES (33, 1408579200, 1408582800, 'Live', 33, 'The temple');
INSERT INTO "sets" VALUES (34, 1408579200, 1408582800, 'Live', 34, 'The temple');
INSERT INTO "sets" VALUES (35, 1408579200, 1408582800, 'Live', 35, 'The temple');
INSERT INTO "sets" VALUES (36, 1408579200, 1408582800, 'Live', 36, 'The temple');
INSERT INTO "sets" VALUES (37, 1408579200, 1408582800, 'Live', 37, 'The temple');
INSERT INTO "sets" VALUES (38, 1408579200, 1408582800, 'DJ', 38, 'The temple');
INSERT INTO "sets" VALUES (39, 1408579200, 1408582800, 'Live', 39, 'The temple');
INSERT INTO "sets" VALUES (40, 1408579200, 1408582800, 'DJ', 40, 'The temple');
INSERT INTO "sets" VALUES (41, 1408579200, 1408582800, 'Live', 41, 'The temple');
INSERT INTO "sets" VALUES (42, 1408579200, 1408582800, 'Live', 42, 'The temple');
INSERT INTO "sets" VALUES (43, 1408579200, 1408582800, 'Live', 43, 'The temple');
INSERT INTO "sets" VALUES (44, 1408579200, 1408582800, 'DJ', 44, 'The temple');
INSERT INTO "sets" VALUES (45, 1408579200, 1408582800, 'DJ', 45, 'The temple');
INSERT INTO "sets" VALUES (46, 1408579200, 1408582800, 'Live', 46, 'The temple');
INSERT INTO "sets" VALUES (47, 1408579200, 1408582800, 'Live', 47, 'The temple');
INSERT INTO "sets" VALUES (48, 1408579200, 1408582800, 'Live', 48, 'The temple');
INSERT INTO "sets" VALUES (49, 1408579200, 1408582800, 'Live', 49, 'The temple');
INSERT INTO "sets" VALUES (50, 1408579200, 1408582800, 'Live', 50, 'The temple');
INSERT INTO "sets" VALUES (51, 1408579200, 1408582800, 'DJ', 51, 'The temple');
INSERT INTO "sets" VALUES (52, 1408579200, 1408582800, 'GIG', 52, 'The temple');
INSERT INTO "sets" VALUES (53, 1408579200, 1408582800, 'Live', 53, 'The temple');
INSERT INTO "sets" VALUES (54, 1408579200, 1408582800, 'DJ', 54, 'The temple');
INSERT INTO "sets" VALUES (55, 1408579200, 1408582800, 'DJ', 55, 'The temple');
INSERT INTO "sets" VALUES (56, 1408579200, 1408582800, 'DJ', 56, 'The lotus');
INSERT INTO "sets" VALUES (57, 1408579200, 1408582800, 'Live', 57, 'The lotus');
INSERT INTO "sets" VALUES (58, 1408579200, 1408582800, 'Live', 58, 'The lotus');
INSERT INTO "sets" VALUES (59, 1408579200, 1408582800, 'Live', 59, 'The lotus');
INSERT INTO "sets" VALUES (60, 1408579200, 1408582800, 'Live', 60, 'The lotus');
INSERT INTO "sets" VALUES (61, 1408579200, 1408582800, 'Live', 61, 'The lotus');
INSERT INTO "sets" VALUES (62, 1408579200, 1408582800, 'Live', 62, 'The lotus');
INSERT INTO "sets" VALUES (63, 1408579200, 1408582800, 'Live', 63, 'The lotus');
INSERT INTO "sets" VALUES (64, 1408579200, 1408582800, 'Live', 64, 'The lotus');
INSERT INTO "sets" VALUES (65, 1408579200, 1408582800, 'Live', 65, 'The lotus');
INSERT INTO "sets" VALUES (66, 1408579200, 1408582800, 'DJ', 66, 'The lotus');
INSERT INTO "sets" VALUES (67, 1408579200, 1408582800, 'Live', 67, 'The lotus');
INSERT INTO "sets" VALUES (68, 1408579200, 1408582800, 'Live', 68, 'The lotus');
INSERT INTO "sets" VALUES (69, 1408579200, 1408582800, 'Live', 69, 'The lotus');
INSERT INTO "sets" VALUES (70, 1408579200, 1408582800, 'GIG', 70, 'The lotus');
INSERT INTO "sets" VALUES (71, 1408579200, 1408582800, 'DJ', 71, 'The lotus');
INSERT INTO "sets" VALUES (72, 1408579200, 1408582800, 'DJ', 72, 'The lotus');
INSERT INTO "sets" VALUES (73, 1408579200, 1408582800, 'DJ', 73, 'The lotus');
INSERT INTO "sets" VALUES (74, 1408579200, 1408582800, 'DJ', 74, 'The lotus');
INSERT INTO "sets" VALUES (75, 1408579200, 1408582800, 'DJ', 75, 'The lotus');
INSERT INTO "sets" VALUES (76, 1408579200, 1408582800, 'DJ', 76, 'The lotus');
INSERT INTO "sets" VALUES (77, 1408579200, 1408582800, 'DJ', 77, 'The lotus');
INSERT INTO "sets" VALUES (78, 1408579200, 1408582800, 'DJ', 78, 'The lotus');
INSERT INTO "sets" VALUES (79, 1408579200, 1408582800, 'Live', 79, 'The lotus');
INSERT INTO "sets" VALUES (80, 1408579200, 1408582800, 'GIG', 80, 'The lotus');
INSERT INTO "sets" VALUES (81, 1408579200, 1408582800, 'DJ', 81, 'The lotus');
INSERT INTO "sets" VALUES (82, 1408579200, 1408582800, 'Live', 82, 'The lotus');
INSERT INTO "sets" VALUES (83, 1408579200, 1408582800, 'Live', 83, 'The lotus');
INSERT INTO "sets" VALUES (84, 1408579200, 1408582800, 'DJ', 84, 'The lotus');
INSERT INTO "sets" VALUES (85, 1408579200, 1408582800, 'Live', 85, 'The lotus');
INSERT INTO "sets" VALUES (86, 1408579200, 1408582800, 'DJ', 86, 'The lotus');
INSERT INTO "sets" VALUES (87, 1408579200, 1408582800, 'DJ', 87, 'The lotus');
INSERT INTO "sets" VALUES (88, 1408579200, 1408582800, 'GIG', 88, 'The lotus');
INSERT INTO "sets" VALUES (89, 1408579200, 1408582800, 'DJ', 89, 'The moon');
INSERT INTO "sets" VALUES (90, 1408579200, 1408582800, 'Live', 90, 'The moon');
INSERT INTO "sets" VALUES (91, 1408579200, 1408582800, 'Live', 91, 'The moon');
INSERT INTO "sets" VALUES (92, 1408579200, 1408582800, 'DJ', 92, 'The moon');
INSERT INTO "sets" VALUES (93, 1408579200, 1408582800, 'DJ', 93, 'The moon');


