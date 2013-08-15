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
INSERT INTO "lst__location_types" VALUES (0, 'Bar', 'marker_bar', 'fr');
INSERT INTO "lst__location_types" VALUES (0, 'Bar', 'marker_bar', 'en');
INSERT INTO "lst__location_types" VALUES (1, 'Stand Hadra', 'marker_hadra', 'fr');
INSERT INTO "lst__location_types" VALUES (1, 'Hadra''s boot', 'marker_hadra', 'en');
INSERT INTO "lst__location_types" VALUES (2, 'Espace des sens', 'marker_chillout', 'fr');
INSERT INTO "lst__location_types" VALUES (2, 'Chillout', 'marker_chillout', 'en');
INSERT INTO "lst__location_types" VALUES (3, 'Douches', 'marker_shower', 'fr');
INSERT INTO "lst__location_types" VALUES (3, 'Showers', 'marker_shower', 'en');
INSERT INTO "lst__location_types" VALUES (4, 'Scène', 'marker_stage', 'fr');
INSERT INTO "lst__location_types" VALUES (4, 'Stage', 'marker_stage', 'en');
INSERT INTO "lst__location_types" VALUES (5, 'Toilettes', 'marker_toilet', 'fr');
INSERT INTO "lst__location_types" VALUES (5, 'Toilets', 'marker_toilet', 'en');
INSERT INTO "lst__location_types" VALUES (6, 'Eau potable', 'marker_water', 'fr');
INSERT INTO "lst__location_types" VALUES (6, 'Drinkable water', 'marker_water', 'en');
INSERT INTO "lst__location_types" VALUES (7, 'Navette', 'marker_shuttle', 'fr');
INSERT INTO "lst__location_types" VALUES (7, 'Shuttle', 'marker_shuttle', 'en');
INSERT INTO "lst__location_types" VALUES (8, 'Camping', 'marker_camp', 'fr');
INSERT INTO "lst__location_types" VALUES (8, 'Campsite', 'marker_camp', 'en');
INSERT INTO "lst__location_types" VALUES (9, 'Poste de secours', 'marker_rescue', 'fr');
INSERT INTO "lst__location_types" VALUES (9, 'Rescue center', 'marker_rescue', 'en');
INSERT INTO "lst__location_types" VALUES (10, 'Restaurants', 'marker_restaurant', 'fr');
INSERT INTO "lst__location_types" VALUES (10, 'Restaurants', 'marker_restaurant', 'en');

INSERT INTO "locations" VALUES (1, 45.1142451237965, 5.60978658071747, 0, NULL);
INSERT INTO "locations" VALUES (2, 45.1177315966413, 5.61004943720093, 0, NULL);
INSERT INTO "locations" VALUES (3, 45.1144495482783, 5.61014599672546, 1, NULL);
INSERT INTO "locations" VALUES (4, 45.1169215122848, 5.60950226656189, 2, NULL);
INSERT INTO "locations" VALUES (5, 45.1133327759784, 5.60861981979599, 3, NULL);
INSERT INTO "locations" VALUES (6, 45.1150476749639, 5.61031765810242, 4, NULL);
INSERT INTO "locations" VALUES (7, 45.118121492956, 5.61019964090576, 4, NULL);
INSERT INTO "locations" VALUES (8, 45.1158899629888, 5.60949421993485, 5, NULL);
INSERT INTO "locations" VALUES (9, 45.1167322385815, 5.6099904286026, 5, NULL);
INSERT INTO "locations" VALUES (10, 45.1135107038762, 5.60827381483307, 5, NULL);
INSERT INTO "locations" VALUES (11, 45.1111427031209, 5.60682810417404, 5, NULL);
INSERT INTO "locations" VALUES (12, 45.1159202472705, 5.60969270340195, 6, NULL);
INSERT INTO "locations" VALUES (13, 45.1169177268169, 5.60997433534851, 6, NULL);
INSERT INTO "locations" VALUES (14, 45.1134009186431, 5.60843474737396, 6, NULL);
INSERT INTO "locations" VALUES (15, 45.1161265585121, 5.60903019777527, 7, NULL);
INSERT INTO "locations" VALUES (16, 45.1128974181447, 5.60982949606171, 8, NULL);
INSERT INTO "locations" VALUES (17, 45.1135996676171, 5.61031229368439, 8, NULL);
INSERT INTO "locations" VALUES (18, 45.1140918049545, 5.60885317198029, 9, NULL);
INSERT INTO "locations" VALUES (19, 45.1173341267819, 5.61007625929108, 10, NULL);
INSERT INTO "locations" VALUES (20, 45.1162363385016, 5.61041958204498, 10, NULL);
INSERT INTO "locations" VALUES (21, 45.1165429642504, 5.60945398679962, 10, NULL);

INSERT INTO "location_descriptions" VALUES (1, 'Scène principale', 'fr');
INSERT INTO "location_descriptions" VALUES (1, 'Scène principale', 'en');
INSERT INTO "location_descriptions" VALUES (2, 'Scène alternatve', 'fr');
INSERT INTO "location_descriptions" VALUES (2, 'Scène alternatve', 'en');

INSERT INTO lst__genres VALUES("Psytrance");
INSERT INTO lst__genres VALUES("Progressive");
INSERT INTO lst__genres VALUES("Full On");
INSERT INTO lst__genres VALUES("Dark Trance");

INSERT INTO lst__set_types VALUES("DJ set");
INSERT INTO lst__set_types VALUES("Live act");
INSERT INTO lst__set_types VALUES("Live band");

INSERT INTO lst__stages VALUES("Main stage");
INSERT INTO lst__stages VALUES("Alternative stage");
INSERT INTO lst__stages VALUES("Visuals stage");

INSERT INTO "bios" VALUES (1, '', 'fr');
INSERT INTO "bios" VALUES (1, '', 'en');
INSERT INTO "bios" VALUES (2, '', 'fr');
INSERT INTO "bios" VALUES (2, '', 'en');
INSERT INTO "bios" VALUES (3, '', 'fr');
INSERT INTO "bios" VALUES (3, '“Stretch” is the solo live act of Julien Fougea from Digital Talk. Be prepared for a legendary performance with absolutely fresh, body and mind grooving production.', 'en');
INSERT INTO "bios" VALUES (4, '', 'fr');
INSERT INTO "bios" VALUES (4, '', 'en');
INSERT INTO "bios" VALUES (5, '', 'fr');
INSERT INTO "bios" VALUES (5, 'Malice in Wonderland''s music is characterized by a complex & solid production. The wide variations of sounds, rhythmic structures and textures which seem to move through otherworldly sounddimensions are giving the compositions a very unique and special character.', 'en');
INSERT INTO "bios" VALUES (6, '', 'fr');
INSERT INTO "bios" VALUES (6, 'Created in 2001, Electrypnose is exploring the electronic music-and-noise world and tries to share its sound universe.
Based in Switzerland, the laboratory doesn''t stop to be active since the beginning of the project. After the first track releaed in 2003 by Peak records, more than 100 pieces of work have been released on record labels from all over.
Beside working in the studio, Vince is travelling regulary around the globe to present his new material and to share time with the comunity.', 'en');
INSERT INTO "bios" VALUES (7, '', 'fr');
INSERT INTO "bios" VALUES (7, 'His project Harmonic Rebel aims to influence the audience with psychedelic music that recreates that shift in life and widens the horizon for new infinite possibilities. His music can be described as chunky, groovy, trippy psychedelic trance that is designed to be performed in several times of the night and day.', 'en');
INSERT INTO "bios" VALUES (8, 'Facebook bio (PLEASE REVIEW): David AKA Shotu discovered electronic music in 1996 at free parties in France. In 2003 he met the Hadra tribe, and here he soon found his place. He has played across the global at parties and festivals such as Universo Paralello and Boom Festival. Shotu style is night-time psychedelic style and has 2 albums under his belt ''Jungle Expedition'' and ''Conception''
Now preparing the SHOTU & Friends album for end of 2012 !!', 'fr');
INSERT INTO "bios" VALUES (8, 'Facebook bio (PLEASE REVIEW): David AKA Shotu discovered electronic music in 1996 at free parties in France. In 2003 he met the Hadra tribe, and here he soon found his place. He has played across the global at parties and festivals such as Universo Paralello and Boom Festival. Shotu style is night-time psychedelic style and has 2 albums under his belt ''Jungle Expedition'' and ''Conception''
Now preparing the SHOTU & Friends album for end of 2012 !!', 'en');
INSERT INTO "bios" VALUES (9, '', 'fr');
INSERT INTO "bios" VALUES (9, 'EVERBLAST is: Earthling (Ibiza) and Chromatone (SF, USA) ...', 'en');
INSERT INTO "bios" VALUES (10, '', 'fr');
INSERT INTO "bios" VALUES (10, 'Matheus Nogueira is the thinking head behind Earthspace, today making part of one of the most importants psychedelic crew from northeast of Brazil, NuAct Productions and currently making part too of respected brazilian label Mosaico Records. ', 'en');
INSERT INTO "bios" VALUES (11, '', 'fr');
INSERT INTO "bios" VALUES (11, 'DJ GINO is founder and executive producer of SONICA FESTIVAL. Now a veteran about psychedelic music gatherings, he begins his active participation since 1990, immediately keen on acid-techno frequencies of that age. His dancefloor-driving experience melted with his professional know-how in event management led him to project and create SONICA FESTIVAL together with other partners from Rome, where He''s based since 1998. ', 'en');
INSERT INTO "bios" VALUES (12, 'Le projet SINE DIE est créé en 2004, né du désir de composer une musique inspirée de l''esprit Trance Goa d''antan mêlé aux sonorités beaucoup plus psychédéliques des soirées psytrance d''aujourd''hui. Cette alchimie donne ainsi naissance à  un style très novateur, dynamique, coloré et résolument orienté dancefloor.', 'fr');
INSERT INTO "bios" VALUES (12, '', 'en');
INSERT INTO "bios" VALUES (13, '', 'fr');
INSERT INTO "bios" VALUES (13, 'Headroom likes to break the realm of the expected. He strives for the best quality production in high tech trance, keeping it fun, psychedelic & a little bit twisted. He has been ‘eyes wide shut - ears wide open’ in his studio, working on a new tale to add to his offbeat sound story with a positively new chapter coming your way soon. Headroom embraces the rush of a full floor of full on. fans, in full emotion', 'en');
INSERT INTO "bios" VALUES (14, 'LovPacT est un nouveau projet trance-progressive combinant le groove psychédélique du renommé LunaRave et les mélodies envoutantes & mélancoliques de Sleeping Forest, dernière recrue ambiant du label Hadra. Après de nombreuses collaborations secrètes,et d''innombrables scéances, leur premier album voit enfin le jour : Retrodelik à découvrir en février 2013 sur le label français !', 'fr');
INSERT INTO "bios" VALUES (14, '', 'en');
INSERT INTO "bios" VALUES (15, '', 'fr');
INSERT INTO "bios" VALUES (15, 'mayaXperience" - a.k.a. *dj maya* (Peter Vlach) & *dj Sun Experience* (Thorsten Schuch) - is a well known Psy-Prog duo, based in Vienna. After many years of playing powerful dj-sets together on stage, they decided to combine in one name - finally *mayaXperience* was born in 2011.', 'en');
INSERT INTO "bios" VALUES (16, '', 'fr');
INSERT INTO "bios" VALUES (16, 'PROTONICA are Piet Kaempfer and Ralf Dietze from Berlin (Germany). With Piet''s intuition for harmonies as a skilled pianist and certified sound designer Ralf''s feeling for propulsive grooves, Protonica deliver you their very own positive pulsing sound - a blend of psychedelic and progressive trance. ', 'en');
INSERT INTO "bios" VALUES (17, '', 'fr');
INSERT INTO "bios" VALUES (17, '', 'en');
INSERT INTO "bios" VALUES (18, '', 'fr');
INSERT INTO "bios" VALUES (18, '', 'en');
INSERT INTO "bios" VALUES (19, '', 'fr');
INSERT INTO "bios" VALUES (19, '', 'en');
INSERT INTO "bios" VALUES (20, '', 'fr');
INSERT INTO "bios" VALUES (20, '', 'en');
INSERT INTO "bios" VALUES (21, '', 'fr');
INSERT INTO "bios" VALUES (21, '', 'en');
INSERT INTO "bios" VALUES (22, '', 'fr');
INSERT INTO "bios" VALUES (22, '', 'en');
INSERT INTO "bios" VALUES (23, '', 'fr');
INSERT INTO "bios" VALUES (23, '', 'en');
INSERT INTO "bios" VALUES (24, '', 'fr');
INSERT INTO "bios" VALUES (24, '', 'en');
INSERT INTO "bios" VALUES (25, '', 'fr');
INSERT INTO "bios" VALUES (25, '', 'en');
INSERT INTO "bios" VALUES (26, '', 'fr');
INSERT INTO "bios" VALUES (26, '', 'en');
INSERT INTO "bios" VALUES (27, '', 'fr');
INSERT INTO "bios" VALUES (27, '', 'en');
INSERT INTO "bios" VALUES (28, '', 'fr');
INSERT INTO "bios" VALUES (28, '', 'en');
INSERT INTO "bios" VALUES (29, '', 'fr');
INSERT INTO "bios" VALUES (29, '', 'en');
INSERT INTO "bios" VALUES (30, '', 'fr');
INSERT INTO "bios" VALUES (30, '', 'en');
INSERT INTO "bios" VALUES (31, '', 'fr');
INSERT INTO "bios" VALUES (31, '', 'en');
INSERT INTO "bios" VALUES (32, '', 'fr');
INSERT INTO "bios" VALUES (32, '', 'en');
INSERT INTO "bios" VALUES (33, '', 'fr');
INSERT INTO "bios" VALUES (33, '', 'en');
INSERT INTO "bios" VALUES (34, '', 'fr');
INSERT INTO "bios" VALUES (34, '', 'en');
INSERT INTO "bios" VALUES (35, '', 'fr');
INSERT INTO "bios" VALUES (35, '', 'en');
INSERT INTO "bios" VALUES (36, '', 'fr');
INSERT INTO "bios" VALUES (36, '', 'en');
INSERT INTO "bios" VALUES (37, '', 'fr');
INSERT INTO "bios" VALUES (37, '', 'en');
INSERT INTO "bios" VALUES (38, '', 'fr');
INSERT INTO "bios" VALUES (38, '', 'en');
INSERT INTO "bios" VALUES (39, '', 'fr');
INSERT INTO "bios" VALUES (39, '', 'en');
INSERT INTO "bios" VALUES (40, '', 'fr');
INSERT INTO "bios" VALUES (40, '', 'en');
INSERT INTO "bios" VALUES (41, '', 'fr');
INSERT INTO "bios" VALUES (41, '', 'en');
INSERT INTO "bios" VALUES (42, '', 'fr');
INSERT INTO "bios" VALUES (42, '', 'en');
INSERT INTO "bios" VALUES (43, '', 'fr');
INSERT INTO "bios" VALUES (43, '', 'en');
INSERT INTO "bios" VALUES (44, '', 'fr');
INSERT INTO "bios" VALUES (44, '', 'en');
INSERT INTO "bios" VALUES (45, '', 'fr');
INSERT INTO "bios" VALUES (45, '', 'en');
INSERT INTO "bios" VALUES (46, '', 'fr');
INSERT INTO "bios" VALUES (46, '', 'en');
INSERT INTO "bios" VALUES (47, '', 'fr');
INSERT INTO "bios" VALUES (47, '', 'en');
INSERT INTO "bios" VALUES (48, '', 'fr');
INSERT INTO "bios" VALUES (48, '', 'en');
INSERT INTO "bios" VALUES (49, '', 'fr');
INSERT INTO "bios" VALUES (49, '', 'en');
INSERT INTO "bios" VALUES (50, '', 'fr');
INSERT INTO "bios" VALUES (50, '', 'en');
INSERT INTO "bios" VALUES (51, '', 'fr');
INSERT INTO "bios" VALUES (51, 'Shangaan Electro is the high-speed dance phenomenon from South Africa that is rising from streets into clubs, homes and venues all around the globe.
The creation of charismatic producer, record label mogul and businessman Nozinja,  this is a very contemporary product of Africa. Hailing from rural Limpopo but now based in Soweto, Nozinja saw the chance to update Shangaan music for the 21st Century, replacing its traditional bass/guitar instrumentation with midi-keyboard sounds and repitched vocal samples (in English and seemingly sampled from rave anthems). Propelled by jacking four-to-the-floor beats and trademark drum-fills, the sound quickly became a hit at weekly street parties in Soweto, with young and old competing to show off their moves to this dizzyingly fast music, which can reach speeds of up to 189 beats per minute.', 'en');
INSERT INTO "bios" VALUES (52, '', 'fr');
INSERT INTO "bios" VALUES (52, 'Rafael Aragón aka Rafiralfiro is a latin/arabic rooted french musician / dj / composer / producer. Born & raised in Paris, between downtown and suburbs, he grew up surrounded by different cultures that really built him, first as a human being and later as an artist.', 'en');
INSERT INTO "bios" VALUES (53, '', 'fr');
INSERT INTO "bios" VALUES (53, '', 'en');
INSERT INTO "bios" VALUES (54, '', 'fr');
INSERT INTO "bios" VALUES (54, '', 'en');
INSERT INTO "bios" VALUES (55, '', 'fr');
INSERT INTO "bios" VALUES (55, 'Traversing the cosmos, gliding across dimensions beyond time and space, Kalya Scintilla brings universal shamanic journeys through his music to planet earth straight from his heart. His music paints sacred soundscapes with world fusion beats from ancient futures hidden amongst our forgotten memories to bring forth lush healing vibrations to activate the dormant codes within us. ', 'en');
INSERT INTO "bios" VALUES (56, 'Depuis 2010, avec ADN, amoureux des nouvelles technologies qui gravitent autour de la musique et de la vidéo, ils s’amusent ensemble sur de nouveaux projets mi mix mi live qu’ils partagent volontier en festival... Que ce soit à Ozora , à Ambiosonic, à Avalon ou pour les World People en France et à Goa, ils accélèrent le tempo en nous régalant sur des sons ethnik, des ambiances tribales, des samples d’ailleurs mariés à des nappes d’émotions, accompagnées de voix du monde...', 'fr');
INSERT INTO "bios" VALUES (56, '', 'en');
INSERT INTO "bios" VALUES (57, '', 'fr');
INSERT INTO "bios" VALUES (57, 'Starspine is one of South Africa''s original trance dj''s and has been playing since 1996. He has played for all the major festivals and promotors in South Africa such as Vortex 2000 Millenium Party, Solipse - Zambia 2001, Vortex/ Etnicanet Solar Eclipse 2002, and abroad has played in Barcelona, Madrid, London & events such as the Omni Festival in Spain.

He was also a resident at the famous techno club GASS in Johannesburg introducing psytrance to the club on a weekly basis for 2 years alongside internationals such as Aaron Liberator and Stirling Moss.

He has been a label dj for Alchemy Records since their inception and compiled their compilation Wild Life: Surfing on Soundwaves. In addition he is a label dj and graphic designer for Nutek records.

In addition he used to form one half of the duo Bent Sentient. They pioneered the South African sound and were the first SA psytrance act to enjoy international exposure, with releases on Alchemy Records (UK), Candyflip (GR), Digital Psionics (OZ), Sundance records (OZ) and South African labels such as Afrogalactic and Nano.
His dj''ing style would best be described as a range from groove and percussion based hard edged psytrance, to lush sunshine daytime sets guaranteed to put a smile on your face. Pumping at the bottom end and floating at the tops but always psychedelic. He also plays a wide variety of chillout music.

Design work website: http://www.coroflot.com/flowmotion', 'en');
INSERT INTO "bios" VALUES (58, 'Depuis ses débuts, ses productions surprennent par leur éclectisme.
Son album "Cinématik Funk " ainsi que le très jungle "audio architecture", mélangent tous les deux trance, drum and bass, jungle, jazz & hip-hop.
Très inspiré par les musiques du monde, il nous enduit de rythmes psychédéliques, de voies et instruments orientaux et ethniques..
Influences que l''on retrouvera quelques années plus tard sur les très hypnotiques "Harmony for Mecha", le très dansant "Sound of Nauzica" et son dernier lp , "Nahual " .. mélange de trance tribale, progressive trance et techno.
Son dernier album est une invitation vers un projet hypnotique, où chaque son résonne tel un mantra, bercé par une ambiance psychédélique, trip hop, downtempo & progressive ; tantôt sacré, tantôt new age ...
On le retrouve aussi régulièrement pour des live orienté dancefloor , groovy , où rythmes psychédéliques, tribaux et goa se rencontrent pour nous emmener vers une Energie envoutante qui invite au lâcher prise et à la trance.
Préparez vous à un voyage magique & envoutant...', 'fr');
INSERT INTO "bios" VALUES (58, '', 'en');
INSERT INTO "bios" VALUES (59, '', 'fr');
INSERT INTO "bios" VALUES (59, 'Electrypnose, the electric hypnosis, is Vince Lebarde''s multi-flavored musical project.

Created in 2001, Electrypnose is exploring the electronic music-and-noise world and tries to share its sound universe.
Based in Switzerland, the laboratory doesn''t stop to be active since the beginning of the project. After the first track releaed in 2003 by Peak records, more than 100 pieces of work have been released on record labels from all over.
Beside working in the studio, Vince is travelling regulary around the globe to present his new material and to share time with the comunity.d', 'en');
INSERT INTO "bios" VALUES (60, '', 'fr');
INSERT INTO "bios" VALUES (60, 'Working as a DJ and musician since 1993 he''s been spinning acidizing rhythms and kicked the crowd with exhausting psychedelic trance music. Consequent grooves with kickin'' melodies shaped his sets on European dancefloors. Successful with techno and ambient releases in the mid 90''s, he''s additionally been working as a reporter and photographer for German music magazines like Frontpage, X-Mag, Loop and others. Over the years Dense used to organize several techno and trance events in Germany (D''vents).
In 2008 he refined his striking style of performing progressive ambient music. Luckily he took his ability to play the right music at the right time from the dancefloor to the chill out floor. Meanwhile Dense is one of the most popular ambient DJs in Germany - performing chillgressive® style.
Beside deejaying and after broadcasting the 6 a.m. Saturday morning chillout radio show on Trancefan Radio for years, since 2010 his homebase is Chromanova.fm, based in Berlin. He''s responsible for the program of the Chromanova.fm Chillout and Ambient Radio stream where you can catch him exclusively on Sundays with his weekly four hour radio show „Chill On!", what is part and parcel of ambient music scene by now and supported by most of the important chill out labels.
His project together with GMO is working well with the second album release „Tales From The Yellow Kangaroo“, out since September 2012 on Altar Rec. Single tracks like „Remount" or „Moonflower“ have just been released on the compilations „Floating Spirals" (Vimana Rec.) and „Spring“ (Altar Rec.) and give an outlook on his solo album „Exhale", scheduled for July 2013.', 'en');
INSERT INTO "bios" VALUES (61, '', 'fr');
INSERT INTO "bios" VALUES (61, 'Zen Baboon is composed of Daniel Rosado and Henrique Palhavã. Both were born in the 70`s in Portugal and since an early age kept a close connection with the music world. Musically and without mention any names and styles, both love projects with great sound quality and with original construction.
Daniel is a sound engineer that worked in a Lisbon studio for National Geographic documentaries and started producing electronic music in the late 90′s. Henrique, a zoologist, lives and works in his own farm and started as an ambient dj and producer in 2001. They got together in 2003 and decided to melt their musical preferences and create Zen Baboon.
With two tracks on Ambient Planet Vol 2, one on Electrik Dream VA Cosmic Yellow, they are now working on some new releases and remixes for other compilations and their first Zen Baboon album "Suber" to be released on Electrik Dream in 2013.
They have played in major parties and festivals like Boomfestival and keep the continuous learning process awake.', 'en');
INSERT INTO "bios" VALUES (62, '
Electronic activist depuis le debut des année 90''s.
Taj est devenu le manager du label Electrik dream en 2005.
En ce moment, il travail sur un projet Chill Out Ethnik - Downtempo "UASCA" en collaboration avec Dj Fluxo (Quest 4 goa - PT).
lls sortiront leur premier album "Cosmos Umbilical" courant 2013.
Lorqu'' il mixe,il est reconnu pour « raconter une histoire »
et prendre une foule à chaque étape vers l''illumination.
Ce chemin peut prendre plusieurs styles de « musique » du, Downtempo - Ambient - Electro - Progressive, Techno, Trance – Psy Trance, avec le même [DJ]] set : Taj ne connait aucune frontière entre les styles et les tempos.
Lors de l'' edition Hadra festival 2013 Taj jouera un set Electro, Uptempo, Trance, Progressiv sur l'' Alternativ Stage avec un immense plaisir !!!', 'fr');
INSERT INTO "bios" VALUES (62, '', 'en');
INSERT INTO "bios" VALUES (63, 'Inspiré par des musiciens comme Jean-Michel Jarre et Vangelis, mais aussi par la musique classique et cinématographique en général, Akshan commence à composer dès 1997.
Malgré une activité professionnelle intense, il passe tout son temps libre dans son studio, mettant de côté les contraintes de théorie musicale. Son oreille, l''intuition et l''inspiration guident sa créativité.
Fort de ses expériences et de l''influence d''artistes comme Aes Dana, Asura (Ultimae Records) et Juno Reactor, Akshan explore les royaumes du downtempo et de la psychédélique ambiant, sans oublier les compositions orientées dancefloor.
Sa rencontre avec DJ Zen (manager du label Altar Records) est alors inéluctable, comme l''atteste la sortie des albums "The Tree of Life" en 2012 et de "The Rise Of Atlantis" en 2013.
Mystique et ethnique, cinématographique et symphonique, électronique et acoustique…autant de qualificatifs qui caractérisent cette musique, unique et atypique, inspirante et inspirée, qui vous transcendera et vous accompagnera dans la danse.', 'fr');
INSERT INTO "bios" VALUES (63, '', 'en');
INSERT INTO "bios" VALUES (64, '', 'fr');
INSERT INTO "bios" VALUES (64, 'Down-tempo does not have to equate to diminished energy, in fact it can cause a roaring of the spirit matching anything that pounds away at 140bpm. Suitable surrounds and subtle sounds are as equally uplifting by reaching for rhythms that are in tune with the most peaceful elements of the soul. Psy-ambient duo Land Switcher are substantial examples of this finest of artistic abilities by taking listeners/participants on a round tour of the expanses of the cosmos of human experience.

As individuals Freddy Chauvin and Antoine Martineau are proven talented musicians with formative Dub Roots but in co-joining in 2010 they have taken on new capabilities as the collaborative components of Land Switcher. With joyfully received live show taking them across the continents and an ever-expanding back-catalogue they are now beginning to mine the full potential of their greater whole.

Land Switcher is a cosmic journey through colors, grooves and intriguing atmospheres...', 'en');
INSERT INTO "bios" VALUES (65, 'C''est à son retour d''Inde en 2007 que le flûtiste Guillaume Barraud, un des rares spécialistes de la flûte bansuri en Europe, fait la connaissance du joueur de sitar Kengo Saito. L''un est disciple du légendaire Hariprasad Chaurasia et l''autre s''est formé auprès du maître Kushal Das. Leur longue collaboration prend de multiples formes au fil des ans. Ils se réunissent à nouveau, cette fois aux côtés du tabliste Nabankur Battacharya, originaire de Calcutta. Flute&Luth nous plonge au coeur d''une tradition millénaire. Le chant subtil et envoûtant de la flûte, les mélodies intriquées du sitar s''entremêlent aux battements de transe des tablas, et nous emmène dans un voyage sublime et mystérieux.', 'fr');
INSERT INTO "bios" VALUES (65, '', 'en');
INSERT INTO "bios" VALUES (66, 'Surnommé Dj tout terrain ou Général, Véritable tête de pont dans le métissage musical, DA, producteur sur son label No Fridge, Dj Click s’est depuis longtemps fait le complice des projets les plus bouillants : avec son groupe Click Here ou l’extravertie Rona Hartner pour les cultures tsiganes, comme avec les marocains Gnawa Njoum d’Essaouira et les Hamadcha de Fés, ou encore dans son collectif électro‐jazz UHT°, en attendant ses mixes brasileiros de Zuko 103, Dj Dolorès ou africanistes avec Issa Bagayogo. Sur scène ou en studio il a collaboré avec Dulsori (Vinari show), Dj Panko (Ojos de Brujo), Transglobal Underground, Marcelinho da Lua, Dhoad, Smadj, Va Fan fahre, Parno Graszt..
Il a produit une dizaine d’albums dans son studio, de nombreux remixes comme ceux de Manu Chao, Watcha Clan, Warsaw Village Band, Nicolette, Mahala Raï Banda, Burhan Öçal, Boogie Balagan ou Rachid Taha, et est paru sur de prestigieuses compilations. Click en explorateur alchimiste, invente un nouveau genre digital folk, produit une musique sans visa, met de l’urbain dans le rural et du vivant dans la mécanique électronique.
Programmé sur les principaux festivals, clubs et salons internationaux (Womex Seville 2006, Babel Med Marseille 2009, AWME Melbourne 2011, ApaMM Ulsan 2012, Medimex Bari 2012, Porto Musical Recife 2013), de la Corée du Sud au Brésil, des Balkans à l’Australie, de L''Afrique du Nord au Japon, il répand son style partout dans le monde.', 'fr');
INSERT INTO "bios" VALUES (66, '', 'en');
INSERT INTO "bios" VALUES (67, '', 'fr');
INSERT INTO "bios" VALUES (67, 'Scott Sterling''s inspiration is the drum and drumming, the creation of rhythm with the human hand. Equally important is the primal connection between drummer and dancer. An expert percussionist of many years training, he is immersed in rhythm and all its modalities, from the hypnotic, trance-inducing pulse to the energy of ecstatic dance.

Bringing the drums to bellydancers, yogis, and the emerging transformational festival culture, Drumspyder weaves the rhythms and tonalities of the Mediterranean and live Arabic percussion into his own style of electronic dance fusion, distinguished by its funky, (belly)danceable grooves and rhythmically intense, percussion-driven sound.
He works in a broad range of tempos - ranging from slinky, sexy downtempo through funky mid-tempo whomp to high-energy tribal house. He is also ready to collaborate with dancers or other types of stage performers in improvised Arabic-style drum solos or choreographed pieces.

Originating in San Francisco''s crucible of bellydance, bass culture, and world music fusion, Drumspyder has released 3 albums worth of original music (most recently "Kytheria" on Dakini Records) and is a prolific remixer, lending his signature live percussion and melodic touch to the Desert Dwellers, the Spy from Cairo, Mirabai Ceiba, and various traditional Arabic ensembles, with many more on the way. In addition, his music has been featured in numerous dance DVDs and theatrical productions. A second full-length album is in the works for 2013.', 'en');
INSERT INTO "bios" VALUES (68, '', 'fr');
INSERT INTO "bios" VALUES (68, '
Bayawaka aka Golan Aflalo (Enig''matik Records) is a DJ and promoter located in Tel Aviv, Israel.
Originally Born and raised in Jerusalem , Since a very young age started loving and feel the MUSIC.. Classic rock, hard rock, metal & blues, 60s, 70s and israeli music .
In the Middle of The 90''s electronic music comes to my life ! and Since I''m addicted to electronic music like trance , techno Progressive from all the kinds and colors.
the trance is changing every few years and since 2008 I''m mostly in the direction of Bass music ranging from Glitch to Psystep and Chillout.
BAYAWAKA its a special lizard turned colors just like the music I love to play, Many different styles that connect together like colors ...
Living in Tel Aviv and just got back from a successful European summer festival tour having DJed at:
Tree of Life, Freqs of Nature, Antaris, Boom Festival, Utopia after Boom, Ozora, Lost Theory, Vuuv Festival and Transylvania Calling.
Golan is heavily involved in the Tel Aviv bowling Art and Culture scene, being an active club and festival promoter as well as producing art and music exhibitions in the city.', 'en');
INSERT INTO "bios" VALUES (69, '', 'fr');
INSERT INTO "bios" VALUES (69, 'DJ GINO is founder and executive producer of SONICA FESTIVAL. Now a veteran about psychedelic music gatherings, he begins his active participation since 1990, immediately keen on acid-techno frequencies of that age. In 1993 he buys his first desks technics-1200, after having been collecting a large vinyl archive and, getting in touch with local insiders, Gino starts to collaborate for the production of several parties in Italy (Naples, his native land) with famous artists from London and Detroit underground scene. After a pause, gone-by 1996 to 1999, when he''s been traveling throughout South American as well as Oriental cultures, Gino comes back to psychedelic scene attending most of the best events around the globe. His dancefloor-driving experience melted with his professional know-how in event management led him to project and create SONICA FESTIVAL together with other partners from Rome, where He''s based since 1998.

Gino plays his natty dj-set in many international festivals and clubs.
His style is an harmonic fusion of psychedelic and cyber frequencies, and his musical research aims to constantly upgrade the worldwide dance music trends. He is usually able to present two different dj-set, for open-air or indoor stages, dynamically shifting clever selections of contemporary acid sounds with pumping and rocking beats.

Gino released Chacruna on December 2008, his first VA Comp. on Echoes Records, and on May 2010, he founded Sonica Recordings releasing Sonica VA Comp Vol II on June 2010; Healing Lights VA Compiled by Djane Gaby on May 2012 and just released last Oct 2012 the Sonica VA Comp Vol III', 'en');
INSERT INTO "bios" VALUES (70, '', 'fr');
INSERT INTO "bios" VALUES (70, 'James Copeland is an electronic musician based in Cape Town, South Africa with over a decade of experience rocking dancefloors around the world. Whether its psytrance with his Broken Toy project, cyber-metal with Super evil or electrobreaks with his Nesono project - this is a guy who isnt afraid to move into new territory.

Most recently he felt the need to move away from synthetic and "cyber" sounding dance music and make some tunes focused on vintage sounds and tropical flavours. The result is the first of his projects to bear his real name. Infectious jazz-infused tones and cheeky basslines move from double time swing through to brassy, tech-house beats meeting a bit of 70s funk and balkan beats on the way. Its fun music for the drunker side of the dancefloor - the only thing serious here is the groove!

Its due to this musical hyperactivity that the James Copeland "vintage swing tech" sound has grown and evolved at such amazing speed. Kicking off with a winning remixes for the legendary Green Velvet and fellow south africans Goldfish, he`s gone on to release on Bart and Baker`s acclaimed Electroswing compilations on Wagram records as well as EPs with overseas labels What! What!, Bedroomuzik, Broken records and TRibal vision. His next label collaboration saw him teaming up with electro swing and house music legend Tavo and his 3Star Deluxe record label for the release of his take of the Jungle Book classic, "King of the swingers" and a string of other releases soon on the way.

Having already rocked local dancefloors at premier events like Rocking the Daisies, Earthdance, and The Flamjangled Tea party, the past year has taken him to some interesting festivals abroad like Body and Soul in Ireland, Tree of Life in Turkey, Electronic Brain in Canada as well as random gigs as far afield as India, the UK and Australia. The Copeland express might be rolling into your town soon , so look alive and stay tuned!', 'en');
INSERT INTO "bios" VALUES (71, '', 'fr');
INSERT INTO "bios" VALUES (71, 'Composers Daniel Rosado and Henrique Palhavã got together in 2003 to make their ambient music Zen Baboon. Zen Racoon is their techno approach.', 'en');
INSERT INTO "bios" VALUES (72, '', 'fr');
INSERT INTO "bios" VALUES (72, 'Music collector since the early 90''s. Started deejaying and producing in 2001. In 2003 was invited to be part of music artists of Boomfestival''s Ambient area with Paulo Dias aka dj high.
A zoolist and farmer, combines his music with his way of living.
Member of Zen Baboon and Zen Racoon.', 'en');
INSERT INTO "bios" VALUES (73, '', 'fr');
INSERT INTO "bios" VALUES (73, 'SYNC24 is Daniel Segerstad from Carbon Based Lifeforms. Started in early years experimeting with tape recorders and simple music players on the Commodore 64 and gradually upgrading equipment and techniques. Having CBL as his main focus and some other projects on the side there''s always room for personal reflections. SYNC24 is the result of sudden impulse of inspiration, late nights without interference from others.
more info : ultimae.com', 'en');
INSERT INTO "bios" VALUES (74, '', 'fr');
INSERT INTO "bios" VALUES (74, 'Carbon Based Lifeforms (CBL) are Johannes Hedberg and Daniel Segerstad (né Ringström), both born in 1976 and based in Göteborg, south-west Sweden.
They met when both 15 and are still, amazingly, working happily together, over 20 years later.
CBL itself was formed in 1996, as an offshoot from other projects, but it soon became their focal point, culminating in their first release on ‘mp3.com’ in 1998.
Even before this, 1996''s "The Path" is their unreleased and highly sought-after demo that any listeners fortunate to hear the contents will immediately recognise as the genesis of the group''s unique sound.
The duo signed with the Lyon-based ‘Ultimae’ label in 2002 and have since released four official full-length albums.
Their music has been extraordinarily well received by connoisseurs within the ambient scene and clearly beyond, since in 2012 they were also commissioned to write the soundtrack to the independently-produced movie ''The Mansion'', whose release date will be announced in due course.
In addition to their own work, they have actively participated in successful collaborations with several other artists, including Magnus Birgersson of Solar Fields in a project to compose the music for the Swedish dancer Olof Persson’s ‘Fusion’ performance in 1999. Johannes often creates the building blocks of sounds and harmonies and could happily tweak sounds forever, whereas Daniel develops the rhythms and sculpts the ideas into tracks.', 'en');
INSERT INTO "bios" VALUES (75, 'Sleeping Forest, est une artiste ambient qui a récemment intégré le label Hadra. Son style allie des influences tournées vers le rock psychédélique et la world music à une minutie propre aux musiques électroniques. Son premier album « Rise of Nature » sort le 15 février 2013 sur Hadra Records. Latmosphère quelle y développe est à la fois lancinante, mélancolique et épique. Ses compositions regorgent de mélodies chargées en émotions et de nappes en constante évolution. On y retrouve des rythmiques entêtantes dans lesquelles les instruments percussifs traditionnels sont mis à lhonneur et de savants agencements mélodiques qui rappellent de nombreux courants musicaux tels que le classique, le rock progressif, le trip-hop ou encore les musiques de film. Elle utilise également de nombreux samples de nature qui permettent une immersion encore plus totale dans son univers. Cet album est fait pour faire vibrer et bercer son auditeur dans un monde de plénitude dont seule Sleeping Forest a le secret.', 'fr');
INSERT INTO "bios" VALUES (75, '', 'en');
INSERT INTO "bios" VALUES (76, '', 'fr');
INSERT INTO "bios" VALUES (76, 'Laurence’s first published work was on a Liquid Sound compillation in 2001 "Mana Medecine" for which he did collaborations with Youth, Humphrey Bacchus & Nigel Watson (Opus 3).He then went on to collaborate with Michele adamson (Shpongle) and Russell Davies,son of kinks founder member Dave davies. They produced a trio of tracks for Zulu Lounge Records (Mexico) which received great acclaim around the world.
This lead to the birth of KUBA and the release of his debut album "Inside out" on Liquid Sound Design, record label of veteran producer Youth (Killing Joke/The ORB). In the 6 years since releasing this album kuba has released a further four albums on Chillcode records, and built a devoted following of fans globally which has seen him perform at countless festivals and parties world-wide', 'en');
INSERT INTO "bios" VALUES (77, 'B-Brain fait son entrée dans le monde de la nuit comme décorateur scénographe et organisateur rejoignant un sound system du sud de la France. Ses productions sont depuis lors composés d''un mélange éclectique oscillant entre ses influences reggae/dub et jungle/drum&bass . En 2010, toujours activiste de la team Hadra et des formation de DJing, il intègre le label Hadra Records et nous offre des lives et DJsets aux rythmes saccadé avec des basses puissantes et envoûtante.', 'fr');
INSERT INTO "bios" VALUES (77, '', 'en');
INSERT INTO "bios" VALUES (78, '', 'fr');
INSERT INTO "bios" VALUES (78, 'Soom T is an Internationally touring artist, performing a live concert of reggae and dub with a fresh twist of hip-hop. She spreads a philosophical renegade message inspiring a positive evolution of the soul. Supporting the world wide legalisation of marijuana, her bass driven music will leave you feeling refreshed and ready for more.', 'en');
INSERT INTO "bios" VALUES (79, 'Trompettiste et multi-instrumentiste, c''est dans les années 2000 que Lakay découvre les musiques électroniques en côtoyant les free-parties.
Issu d''influences à la fois traditionnelles et undergrounds, son live nous propose un doux voyage vacillant entre le dub & dubstep, la trance downtempo et la jungle psychédélique.
Le mélange coloré des synthés électroniques et des instruments traditionnels nous offre une musique à la fois énergique et méditative propice au voyage de l''esprit. Les boucles électroniques se mêlent aux mélodies de la trompette jazz, du seung Thaïlandais ou encore au groove des guitares espagnoles autour d''un métissage riche et vivant. Depuis 2008, il nous invite à découvrir ses univers vers l''éveil des sens et des émotions...', 'fr');
INSERT INTO "bios" VALUES (79, '', 'en');
INSERT INTO "bios" VALUES (80, 'le projet Digidep, incarné par Arash Kian, est un savant mélange entre trip-hop, IDM et break-beat. Egalement influencé par la trance depuis son entrée sur le label Hadra Records, il a su développer, au fil des années, un son unique nourri de textures synthétiques et d''une grande sensibilité pour les sonorités instrumentales que de nombreuses années de pratique des percussions ont grandi. Digidep est à la croisée des styles ambiances indus et psychédéliques, passages survoltés et planants sont les ingrédients d''une musique précise et soignée, aussi bien dansante que méditative !', 'fr');
INSERT INTO "bios" VALUES (80, '', 'en');
INSERT INTO "bios" VALUES (81, '', 'fr');
INSERT INTO "bios" VALUES (81, 'Celt Islam is an English Muslim, a Sufi and a member of the northern British alternative dub/breaks outfit "Nine Invisibles " and " Analogue Fakir " . Celt-Islam uses a fusion of music from Dub/Electro/Drum andBass and collaborates it with Islamic/world grooves to create a Dub driven dance crossover.

Celt Islam has played at various Dub/DnB/Dubstep nights in UK and Europe alongside Public Enemy''s Bomb Squad { Hank Shocklee } , Fleck, DJ UMB, The Orb, Hardfloor , Engine Earz,Rex Offender aka Smart Monkey,DJ Bobby Friction, DJ Pathaan and was chosen by the BBC to perform at the BBC Introducing stage Glastonbury 2010. [http://www.bbc.co.uk/glastonbury/2010/artists/celtislam/]

Celt Islam works with many musicians and artists including Inder Goldfinger { Ian Brown-Natacha Atlas-Apesta} Mick Reed { 1919 , Ship of fools and Nine Invisibles } , Oova Matique , Dr weevil [ Desert Storm/ DMC] , Peppery aka Bongo Chilli , D Bo General and DanMan { Iration Steppers }.
He creates music in quite a diverse range of styles and genres. His Sufi inspired Global Grooves create a sometimes Meditative Dub transfixing all who comes across it. His work includes Electro-Dub driven Drum and Bass to satisfy the dance world.

Celt Islam is probably best known to date for his work in the genre of his creation – Sufi-Dub – which he singlehandedly developed and continues to define. He does so in collaboration with well known names such as Inder Goldfinger, tabla player for Stone Roses legend, Ian Brown’s band and Natacha Atlas and Dawoud Kringle who has recorded and performed with the likes of Lauryn Hill.

Celt Islam’s Sufi-Dub work has appeared on some best selling compilations including two by UK global music tastemakers Shisha Sound System. Offers are currently on the table from other tastemakers and labels for licensing of his works for compilations.
He is also beginning to gain greater exposure in the UK as BBC Radio 1xtra and BBC Asian network''s DJ’s like Bobby Friction and Nihal make his music a regular feature on their shows.

Celt Islam has released 4 albums titled Dervish ,AL Mizan , Baghdad and Urban Sutra which is a mixture of Heavy heavy basslines, dub step, dancehall and d+b wrapped up with a very dubby world music sound. The album features Bongo Chilli , Inder Goldfinger , Danman { Iration steppers } , Dawoud Kringle and Masala .

Links for Celt Islam albums available on CD and Digital Download

Dervish : http://urbansedatedrecords.bandcamp.com/album/celt-islam-dervish

Al Mizan : http://urbansedatedrecords.bandcamp.com/album/celt-islam-al-mizan-the-balance

BaghDad : http://www.amazon.co.uk/Baghdad-Celt-Islam/dp/B006FE5JEC

Urban Sutra : http://urbansedatedrecords.bandcamp.com/album/celt-islam-urban-sutra

Electro Dunya :
http://www.psyshop.com/shop/CDs/ajv/ajv1cd006.html', 'en');
INSERT INTO "bios" VALUES (82, '', 'fr');
INSERT INTO "bios" VALUES (82, '', 'en');
INSERT INTO "bios" VALUES (83, 'Issus de parcours musicaux différents dans le Reggae/Dub et le Beatbox,
Toinan (Scratchy) et Difa (Itchy) se réunissent autour de leur passion commune, la musique électronique, et fondent le projet "Itchy & Scratchy" en 2009.
Si les premières collaborations se font en "Jungle", très vite le duo s''ouvre à d''autres styles notamment la Bass Music , la Minimal et la Trance...
Leur musique est un univers sombre mélangeant Bass Music lourde et puissante à des sonorités Psygressive en passant par des grooves Dark Prog et Minimal qui vous feront explorer l''inexplorable !', 'fr');
INSERT INTO "bios" VALUES (83, '', 'en');
INSERT INTO "bios" VALUES (84, '6NOK est un subtile mélange entre un dandy chic et un punk à chien.....passionné de tekno depuis toujours,membre actif du collectif lyonnais KONECTIK, 6NOK délivre des sets teintés minimal tekno avec comme mots d''ordres du groove et de l''amour...Alors, affutez vos claquettes et préparez bien vos zigomatiques,sinon gare au claquage!', 'fr');
INSERT INTO "bios" VALUES (84, '', 'en');
INSERT INTO "bios" VALUES (85, 'Bercé au son du rock gothique, Philip Contamin aka Sysyphe découvre les sonorités électro avec des formations comme The Orb, Solar Quest en 93.

Depuis 94, musicien (bassiste), Dj/compositeur et organisateur de soirées, il s’implique toujours dans le milieu musical électronique.

A partir de 2005, il intègre Hadra et participe à l''élaboration de compilations (Hadravision, Hadracadabra V, Hadravision II (2013)).

Depuis 2007, impliqué dans l’organisation du festival Hadra, il participe activement à la programmation de la scène alternative en tant que responsable de la programmation de la partie chill-out / ambient/ downtempo.

En 2010, il sort son album « Running up that hill » dans un style psy-ambient/ downtempo masterisé par Vincent Villuis (Aes dana) d''Ultimae records.

Depuis, toujours en quête d''approfondir ses compositions, il développe, dans ses créations, un son mélodique, suave, tout en cultivant ses premières influences gothiques tant dans ses longs dj sets que dans sa propre musique.', 'fr');
INSERT INTO "bios" VALUES (85, '', 'en');
INSERT INTO "bios" VALUES (86, '', 'fr');
INSERT INTO "bios" VALUES (86, 'Erot is Tore Mortensen born in 1986 in Aalborg, Denmark. In 2004 he started producing music just for the fun, but in 2007 it got a bit more serious. He has been faithfull to his unique style, but always pushing new boundaries and experimentations, and the result is the wide spectrum of styles since pure Chill Out, to more ambient tunes, from psychill passing through uptempo chill to even progressive music.', 'en');
INSERT INTO "bios" VALUES (87, 'A la croisée des Mémoires...
"Quand les chants du monde se font récit... Quand les cultures s''entremêlent et témoignent du passé, du présent... mélodieuses, percussives et tribales. Comme l’histoire qui est tienne et qui m’a montré un chemin, min hounak… Ont retenti alors les voix de demain, et je me souviens…

"Un répertoire de musique aux couleurs de l’Orient et du Maghreb alliant sonorités traditionnelles et compositions actuelles. Fortement inspiré par l’intensité et la générosité de la musique et de la poésie arabe : Mahmoud Darwich, Adonis, Youssef Al Khal…

Chems, (guitare voix) / David Bruley (Percussions Orientales) / Hassan Abd Alrahman (Oud, Ney, voix) / Yannick Benahmed ( contrebasse).', 'fr');
INSERT INTO "bios" VALUES (87, '', 'en');
INSERT INTO "bios" VALUES (88, 'Nikel Gorr ?
C''est une partie de jeu de l''oie. C''est un déjeuner sur l''herbe. C''est un tour sur soi-même, c''est le feu de la vie, c''est la pointe dans son centre et le passant qui s''ignore. C''est ton pied dans ma main, c''est le sol qui rigole, c''est la route qui s''évade par-delà les oiseaux, c''est la vibe pure comme du beurre salé. C''est une fleur arrachée à offrir à un verre, c''est le souffle coupé d''un coquillage hurlant, c''est le ciel et la mer tapis dans le béton, c''est le voyage qui ne s''arrête jamais de commencer.
Nikel Gorr c''est tout ça, mais par les oreilles.', 'fr');
INSERT INTO "bios" VALUES (88, '', 'en');
INSERT INTO "bios" VALUES (89, '', 'fr');
INSERT INTO "bios" VALUES (89, '', 'en');

INSERT INTO "artists" VALUES (1, 'Merkaba', NULL, 'AU', NULL, NULL, NULL, NULL, 'https://soundcloud.com/merkabamusic ', 'Zenon Records', 1);
INSERT INTO "artists" VALUES (2, 'Humerous', NULL, 'ZA', NULL, NULL, NULL, NULL, 'https://soundcloud.com/humerous', 'PsynOpticz Productions', 2);
INSERT INTO "artists" VALUES (3, 'Stretch', NULL, 'FR', NULL, NULL, NULL, NULL, NULL, 'Timecode Records', 3);
INSERT INTO "artists" VALUES (4, 'Yamaga vs Manu', NULL, 'FR', NULL, NULL, 'https://soundcloud.com/yamaga-yann', NULL, 'https://soundcloud.com/manu-hadra', 'Hadra Records', 4);
INSERT INTO "artists" VALUES (5, 'Malice In Wonderland', NULL, 'AT', NULL, NULL, NULL, NULL, 'https://soundcloud.com/malice-in-wonderland', '2to6 Records', 5);
INSERT INTO "artists" VALUES (6, 'Electrypnose', NULL, 'CH', 'ph_electrypnose', 'cover_electrypnose__72', 'http://www.electrypnose.com/', 'https://facebook.com/256314211128486', 'https://soundcloud.com/electrypnose', 'Electrypnosis Media', 6);
INSERT INTO "artists" VALUES (7, 'Harmonic Rebel', NULL, 'GB', 'ph_harmonic_rebel', 'cover_harmonic_rebel__0', 'www.psynonrecords.com', 'https://facebook.com/222028371154090', 'https://soundcloud.com/harmonic-rebel', 'Psynon Records', 7);
INSERT INTO "artists" VALUES (8, 'Shotu', NULL, 'FR', 'ph_shotu', 'cover_shotu__63', 'www.hadra.net', 'https://facebook.com/137121816332052', 'https://soundcloud.com/shotu', 'Hadra Records', 8);
INSERT INTO "artists" VALUES (9, 'Everblast', NULL, 'US – UK', 'ph_everblast', 'cover_everblast__38', 'http://www.zero1-music.com', 'https://facebook.com/147794921934708', 'https://soundcloud.com/everblast', 'Zero 1 Music', 9);
INSERT INTO "artists" VALUES (10, 'Earthspace', NULL, 'BR', 'ph_earthspace', 'cover_earthspace__78', NULL, 'https://facebook.com/165670406797175', 'https://soundcloud.com/earthspacelive ', 'Mosaico Records', 10);
INSERT INTO "artists" VALUES (11, 'Gino', NULL, 'IT', NULL, NULL, NULL, NULL, 'https://soundcloud.com/twinlights', 'Sonica Recording', 11);
INSERT INTO "artists" VALUES (12, 'Sine Die', NULL, 'FR', NULL, NULL, NULL, NULL, 'https://soundcloud.com/sine_die', 'Hadra Records', 12);
INSERT INTO "artists" VALUES (13, 'Headroom', NULL, 'ZA', NULL, NULL, NULL, NULL, 'https://soundcloud.com/headroomusic ', 'Nano Records', 13);
INSERT INTO "artists" VALUES (14, 'Lovpact', NULL, 'FR', NULL, NULL, 'http://www.lunarave.com/#!site', NULL, 'https://soundcloud.com/lovpact ', 'Hadra Records', 14);
INSERT INTO "artists" VALUES (15, 'Mayaxperience', NULL, 'AT', 'ph_mayaxperience', 'cover_mayaxperience__0', NULL, 'https://facebook.com/160401917347003', 'https://soundcloud.com/mayaxperience', 'SoundLabPirates Records', 15);
INSERT INTO "artists" VALUES (16, 'Protonica', NULL, 'DE', 'ph_protonica', 'cover_protonica__0', 'http://www.protonica.de/', 'https://facebook.com/211934438510', 'https://soundcloud.com/protonica', 'Iono Music', 16);
INSERT INTO "artists" VALUES (17, 'Huda G.', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 17);
INSERT INTO "artists" VALUES (18, 'Secret Vibes', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 18);
INSERT INTO "artists" VALUES (19, 'D_Root', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 19);
INSERT INTO "artists" VALUES (20, 'Lunarave', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 20);
INSERT INTO "artists" VALUES (21, 'Justin Chaos', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 21);
INSERT INTO "artists" VALUES (22, 'Chris Rich', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 22);
INSERT INTO "artists" VALUES (23, 'Ataro', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 23);
INSERT INTO "artists" VALUES (24, 'Hyper Frequencies', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 24);
INSERT INTO "artists" VALUES (25, 'Biorhythm', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 25);
INSERT INTO "artists" VALUES (26, 'Lost & Found', NULL, 'ZA', NULL, NULL, NULL, NULL, NULL, NULL, 26);
INSERT INTO "artists" VALUES (27, 'Hatta', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 27);
INSERT INTO "artists" VALUES (28, 'Broken Toy', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 28);
INSERT INTO "artists" VALUES (29, 'Loic', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 29);
INSERT INTO "artists" VALUES (30, 'Tilt', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 30);
INSERT INTO "artists" VALUES (31, 'The Commercial Hippies', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 31);
INSERT INTO "artists" VALUES (32, 'Groove Inspektorz', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 32);
INSERT INTO "artists" VALUES (33, 'Golkonda', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 33);
INSERT INTO "artists" VALUES (34, 'Cosmosophy', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 34);
INSERT INTO "artists" VALUES (35, '! Fuckyeah !', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 35);
INSERT INTO "artists" VALUES (36, 'Hoodwink', NULL, 'UK', NULL, NULL, NULL, NULL, NULL, NULL, 36);
INSERT INTO "artists" VALUES (37, 'Psyberpunk', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 37);
INSERT INTO "artists" VALUES (38, 'Cubic Spline', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/cubic_spline', NULL, 38);
INSERT INTO "artists" VALUES (39, 'Vertical', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/vertical_parvati ', NULL, 39);
INSERT INTO "artists" VALUES (40, 'Zigganaut', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/zigganaut-aka-wobblz', NULL, 40);
INSERT INTO "artists" VALUES (41, 'Dharma', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/pigiu ', NULL, 41);
INSERT INTO "artists" VALUES (42, 'Endeavour', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/endeavour ', NULL, 42);
INSERT INTO "artists" VALUES (43, 'Driss vs Jimson', NULL, 'fr', NULL, NULL, 'https://soundcloud.com/jimson-hadra', NULL, 'https://soundcloud.com/dj-driss-1', NULL, 43);
INSERT INTO "artists" VALUES (44, 'A-Team', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/the-a-team', NULL, 44);
INSERT INTO "artists" VALUES (45, 'Natron', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/natronsoundz ', NULL, 45);
INSERT INTO "artists" VALUES (46, 'Spirit Architect', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/spirit-architect ', NULL, 46);
INSERT INTO "artists" VALUES (47, 'Kokmok', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/kokmok-hadra', NULL, 47);
INSERT INTO "artists" VALUES (48, 'Lyctum', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/leectum ', NULL, 48);
INSERT INTO "artists" VALUES (49, 'Aerospace', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/aerospace ', NULL, 49);
INSERT INTO "artists" VALUES (50, 'Audiomatic', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/benniaudiomatic ', NULL, 50);
INSERT INTO "artists" VALUES (51, 'Shangaan Electro', NULL, 'ZA', NULL, NULL, NULL, NULL, NULL, 'Honest Jon''s Records', 51);
INSERT INTO "artists" VALUES (52, 'Rafael Aragon', NULL, 'FR', 'ph_rafael_aragon', NULL, NULL, 'https://facebook.com/759705969', 'https://soundcloud.com/rafiralfiro ', 'Caballito / Fresh Poulp', 52);
INSERT INTO "artists" VALUES (53, 'Mateba', NULL, 'FR', 'ph_mateba', 'cover_mateba__0', NULL, 'https://facebook.com/145767595584908', 'https://soundcloud.com/mateba ', 'Inside Recordings', 53);
INSERT INTO "artists" VALUES (54, 'Goth Trad', NULL, 'JP', 'ph_goth_trad', 'cover_goth_trad__50', 'http://www.gothtrad.com', 'https://facebook.com/135940969804126', 'https://soundcloud.com/goth-trad ', 'Deep Medi Records', 54);
INSERT INTO "artists" VALUES (55, 'Kalya Scintilla', NULL, 'AU', 'ph_kalya_scintilla', 'cover_kalya_scintilla__0', 'http://merkabamusic1.bandcamp.com/', 'https://facebook.com/121242094567692', 'https://soundcloud.com/kalyascintilla ', 'Merkaba Music', 55);
INSERT INTO "artists" VALUES (56, 'Opale''s ADN', NULL, 'FR', NULL, NULL, NULL, NULL, 'https://soundcloud.com/opale-karine ', 'Ultimae Records', 56);
INSERT INTO "artists" VALUES (57, 'Starspine', NULL, 'ZA', 'ph_starspine', 'cover_starspine__0', 'http://www.nutek.org/index_2.html', 'https://facebook.com/416883498362565', 'https://soundcloud.com/starspine ', 'Nutek Rec. - Alchemy Rec', 57);
INSERT INTO "artists" VALUES (58, 'Gnaïa ', NULL, 'fr', 'ph_gnaa_', 'cover_gnaa___0', 'http://gnaia.net/', 'https://facebook.com/49782851637', 'https://soundcloud.com/gnaia', 'Oreades Prod', 58);
INSERT INTO "artists" VALUES (59, 'Electrypnose', NULL, 'Ch', 'ph_electrypnose', 'cover_electrypnose__72', 'http://www.electrypnose.com/', 'https://facebook.com/256314211128486', 'https://soundcloud.com/electrypnose', 'Electrypnosis Media', 59);
INSERT INTO "artists" VALUES (60, 'Dense', NULL, 'De', 'ph_dense', 'cover_dense__67', 'http://www.chillgressivetunes.com/', 'https://facebook.com/364103207017193', 'https://soundcloud.com/chillgressivetunes', 'Chillgressive Tunes', 60);
INSERT INTO "artists" VALUES (61, 'Zen Baboon', NULL, 'Pt', NULL, NULL, 'http://www.electrikdream.com/', 'https://soundcloud.com/zen-baboon', 'https://soundcloud.com/zen-baboon', 'Electrik Dream', 61);
INSERT INTO "artists" VALUES (62, 'Tajmahal', NULL, 'fr', 'ph_tajmahal', 'cover_tajmahal__74', 'http://www.electrikdream.com/artists/tajmahal', 'https://facebook.com/151174286726', NULL, 'Electrik Dream / Ultimae Rec', 62);
INSERT INTO "artists" VALUES (63, 'Akshan', NULL, 'fr', 'ph_akshan', 'cover_akshan__33', 'http://www.altar-records.com/atlantis.html', 'https://facebook.com/121973874578593', 'https://soundcloud.com/akshanmusik', 'Altar Rec. ', 63);
INSERT INTO "artists" VALUES (64, 'Landswitcher', NULL, 'fr', 'ph_landswitcher', 'cover_landswitcher__79', NULL, 'https://facebook.com/127910370624749', 'https://soundcloud.com/landswitcher', 'Free-spirit Rec.', 64);
INSERT INTO "artists" VALUES (65, 'Flute&Luth', NULL, 'fr', 'ph_fluteluth', NULL, NULL, 'https://facebook.com/100003246476169', 'https://soundcloud.com/bansouri/welcome-mp3', 'Association Yaman', 65);
INSERT INTO "artists" VALUES (66, 'DJ Click', NULL, 'fr', 'ph_dj_click', 'cover_dj_click__0', 'http://www.nofridge.com/', 'https://facebook.com/130807350323129', 'https://soundcloud.com/click-here', 'No Fridge ', 66);
INSERT INTO "artists" VALUES (67, 'Drumspyder ', NULL, 'Us', 'ph_drumspyder_', 'cover_drumspyder___0', 'http://www.drumspyder.com/drumspyder/home.html', 'https://facebook.com/166638194821', 'https://soundcloud.com/drumspyder', 'Dakini ', 67);
INSERT INTO "artists" VALUES (68, 'Bayawaka', NULL, 'il', 'ph_bayawaka', 'cover_bayawaka__67', 'http://enigmatikrecords.com/', 'https://facebook.com/158366557620150', 'https://soundcloud.com/golan-28', 'Enigmatik Record / Tel Aviv', 68);
INSERT INTO "artists" VALUES (69, 'Gino', NULL, 'it', 'ph_gino', NULL, 'http://www.sonica-dance-festival.eu/', 'https://facebook.com/100000213537892', 'https://soundcloud.com/twinlights', 'Sonica Recordings ', 69);
INSERT INTO "artists" VALUES (70, 'James Copeland', NULL, 'ZA', 'ph_james_copeland', 'cover_james_copeland__49', NULL, 'https://facebook.com/143759328995932', 'https://soundcloud.com/jamescopeland', '3star Deluxe', 70);
INSERT INTO "artists" VALUES (71, 'Zen Racoon', NULL, 'Pt', 'ph_zen_racoon', 'cover_zen_racoon__59', 'http://www.electrikdream.com/', 'https://facebook.com/109124547846', 'https://soundcloud.com/zen-racoon', NULL, 71);
INSERT INTO "artists" VALUES (72, 'Henrique', NULL, 'Pt', 'ph_henrique', 'cover_henrique__0', 'http://www.electrikdream.com/', 'https://facebook.com/118447265557', 'https://soundcloud.com/henriq-aka-zen-babo', 'Electrik Dream / Boomfestival', 72);
INSERT INTO "artists" VALUES (73, 'Sync24 ', NULL, 'Se', 'ph_sync24_', 'cover_sync24___0', 'http://sync24.se/', 'https://facebook.com/70770237537', NULL, 'Ultimae', 73);
INSERT INTO "artists" VALUES (74, 'Carbon Based Lifeforms ', NULL, 'Se', 'ph_carbon_based_lifeforms_', 'cover_carbon_based_lifeforms___0', 'http://carbonbasedlifeforms.net/', 'https://facebook.com/45457854115', NULL, 'Ultimae', 74);
INSERT INTO "artists" VALUES (75, 'Sleeping Forest', NULL, 'fr', 'ph_sleeping_forest', 'cover_sleeping_forest__80', 'http://www.lunarave.com/sleepingforest', 'https://facebook.com/181124572023605', 'https://soundcloud.com/sleeping_forest', NULL, 75);
INSERT INTO "artists" VALUES (76, 'Kuba', NULL, 'Uk', 'ph_kuba', NULL, NULL, 'https://facebook.com/901810054', 'https://soundcloud.com/kuba-laurence-harvey', 'Liquid Sound Design,chillcode ', 76);
INSERT INTO "artists" VALUES (77, 'B-Brain', NULL, 'fr', NULL, NULL, 'http://hadra.net/', NULL, 'https://soundcloud.com/docteur-b-brain', 'Hadra Rec.', 77);
INSERT INTO "artists" VALUES (78, 'Soom T & Renegade Masters', NULL, 'Uk', 'ph_soom_t__renegade_masters', 'cover_soom_t__renegade_masters__0', 'http://www.renegademasters.com/', 'https://facebook.com/194522907258192', 'https://soundcloud.com/renegade-masters', 'Renegade Masters Rec.', 78);
INSERT INTO "artists" VALUES (79, 'Lakay', NULL, 'fr', 'ph_lakay', NULL, '', 'https://facebook.com/196404337062228', 'https://soundcloud.com/lakay-enjoy-people', 'Hadra Rec.', 79);
INSERT INTO "artists" VALUES (80, 'Digidep', NULL, 'fr', 'ph_digidep', NULL, NULL, 'https://facebook.com/100000549770712', 'https://soundcloud.com/digidep', NULL, 80);
INSERT INTO "artists" VALUES (81, 'Celt Islam', NULL, 'Uk', 'ph_celt_islam', NULL, NULL, 'https://facebook.com/615082714', 'https//soundcloud.com/celt-islam', 'Urban Sedated Rec.', 81);
INSERT INTO "artists" VALUES (82, 'D.Rec', NULL, 'fr', NULL, NULL, 'http://www.mixcloud.com/derek-teum/', NULL, NULL, 'Hadra Rec.', 82);
INSERT INTO "artists" VALUES (83, 'Itchy & Scratchy', NULL, 'fr', 'ph_itchy__scratchy', NULL, 'http://www.hadra.net/', 'https://facebook.com/100001683281932', 'https://soundcloud.com/itchy-and-scratchy', 'Hadra Rec.', 83);
INSERT INTO "artists" VALUES (84, '6nok', NULL, 'fr', NULL, NULL, NULL, NULL, 'https://soundcloud.com/#6nokspurk', 'Konectik', 84);
INSERT INTO "artists" VALUES (85, 'Sysyphe', NULL, 'fr', 'ph_sysyphe', NULL, 'http://www.hadra.net/', 'https://facebook.com/100000032161863', 'https://soundcloud.com/sysyphe', 'Hadra Rec./ Ultimae Rec.', 85);
INSERT INTO "artists" VALUES (86, 'Erot', NULL, 'Dk', 'ph_erot', 'cover_erot__0', NULL, 'https://facebook.com/104121702984398', 'https://soundcloud.com/erot', 'Altar Rec.', 86);
INSERT INTO "artists" VALUES (87, 'Trio Bassma', NULL, 'fr', 'ph_trio_bassma', 'cover_trio_bassma.102.jpg', 'http://www.collectifbassma.org/', 'https://facebook.com/356086816205', NULL, 'Collectif Bassma ', 87);
INSERT INTO "artists" VALUES (88, 'Nikel Gorr', NULL, 'fr', 'ph_nikel_gorr', 'cover_nikel_gorr__0', 'http://www.nikelgorr.com/', 'https://facebook.com/328503880606242', 'https://soundcloud.com/nikelgorr', '-unsigned-', 88);
INSERT INTO "artists" VALUES (89, 'nouvel_artiste', NULL, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, 89);

INSERT INTO "sets" VALUES (1, 1377198000, 1377201600, '2', 1, 'Main stage');
INSERT INTO "sets" VALUES (2, 1377201600, 1377207000, '1', 2, 'Main stage');
INSERT INTO "sets" VALUES (3, 1377207000, 1377210600, '2', 3, 'Main stage');
INSERT INTO "sets" VALUES (4, 1377210600, 1377217800, '1', 4, 'Main stage');
INSERT INTO "sets" VALUES (5, 1377217800, 1377221400, '2', 5, 'Main stage');
INSERT INTO "sets" VALUES (6, 1377221400, 1377225000, '1', 6, 'Main stage');
INSERT INTO "sets" VALUES (7, 1377225000, 1377228600, '2', 7, 'Main stage');
INSERT INTO "sets" VALUES (8, 1377228600, 1377232200, '2', 8, 'Main stage');
INSERT INTO "sets" VALUES (9, 1377232200, 1377235800, '2', 9, 'Main stage');
INSERT INTO "sets" VALUES (10, 1377235800, 1377239400, '2', 10, 'Main stage');
INSERT INTO "sets" VALUES (11, 1377239400, 1377246600, '1', 11, 'Main stage');
INSERT INTO "sets" VALUES (12, 1377246600, 1377250200, '2', 12, 'Main stage');
INSERT INTO "sets" VALUES (13, 1377250200, 1377253800, '2', 13, 'Main stage');
INSERT INTO "sets" VALUES (14, 1377253800, 1377257400, '2', 14, 'Main stage');
INSERT INTO "sets" VALUES (15, 1377257400, 1377264600, '1', 15, 'Main stage');
INSERT INTO "sets" VALUES (16, 1377264600, 1377268200, '2', 16, 'Main stage');
INSERT INTO "sets" VALUES (17, 1377268200, 1377275400, '1', 17, 'Main stage');
INSERT INTO "sets" VALUES (18, 1377282600, 1377288000, '3', 18, 'Main stage');
INSERT INTO "sets" VALUES (19, 1377288000, 1377291600, '2', 19, 'Main stage');
INSERT INTO "sets" VALUES (20, 1377291600, 1377295200, '2', 20, 'Main stage');
INSERT INTO "sets" VALUES (21, 1377295200, 1377302400, '1', 21, 'Main stage');
INSERT INTO "sets" VALUES (22, 1377302400, 1377306000, '2', 22, 'Main stage');
INSERT INTO "sets" VALUES (23, 1377306000, 1377309600, '2', 23, 'Main stage');
INSERT INTO "sets" VALUES (24, 1377309600, 1377313200, '2', 24, 'Main stage');
INSERT INTO "sets" VALUES (25, 1377313200, 1377316800, '2', 25, 'Main stage');
INSERT INTO "sets" VALUES (26, 1377316800, 1377320400, '1', 26, 'Main stage');
INSERT INTO "sets" VALUES (27, 1377320400, 1377327600, '1', 27, 'Main stage');
INSERT INTO "sets" VALUES (28, 1377295740, 1377331200, '2', 28, 'Main stage');
INSERT INTO "sets" VALUES (29, 1377331200, 1377338400, '1', 29, 'Main stage');
INSERT INTO "sets" VALUES (30, 1377338400, 1377345600, '1', 30, 'Main stage');
INSERT INTO "sets" VALUES (31, 1377345600, 1377349200, '2', 31, 'Main stage');
INSERT INTO "sets" VALUES (32, 1377349200, 1377352800, '2', 32, 'Main stage');
INSERT INTO "sets" VALUES (33, 1377352800, 1377360000, '1', 33, 'Main stage');
INSERT INTO "sets" VALUES (34, 1377369000, 1377374400, '3', 34, 'Main stage');
INSERT INTO "sets" VALUES (35, 1377374400, 1377378000, '2', 35, 'Main stage');
INSERT INTO "sets" VALUES (36, 1377378000, 1377381600, '2', 36, 'Main stage');
INSERT INTO "sets" VALUES (37, 1377381600, 1377387000, '1', 37, 'Main stage');
INSERT INTO "sets" VALUES (38, 1377387000, 1377390600, '2', 38, 'Main stage');
INSERT INTO "sets" VALUES (39, 1377390600, 1377394200, '2', 39, 'Main stage');
INSERT INTO "sets" VALUES (40, 1377394200, 1377399600, '1', 40, 'Main stage');
INSERT INTO "sets" VALUES (41, 1377399600, 1377403200, '2', 41, 'Main stage');
INSERT INTO "sets" VALUES (42, 1377403200, 1377406800, '2', 42, 'Main stage');
INSERT INTO "sets" VALUES (43, 1377406800, 1377414000, '1', 43, 'Main stage');
INSERT INTO "sets" VALUES (44, 1377414000, 1377419400, '2', 44, 'Main stage');
INSERT INTO "sets" VALUES (45, 1377419400, 1377426600, '1', 45, 'Main stage');
INSERT INTO "sets" VALUES (46, 1377426600, 1377430200, '1', 46, 'Main stage');
INSERT INTO "sets" VALUES (47, 1377430200, 1377435600, '1', 47, 'Main stage');
INSERT INTO "sets" VALUES (48, 1377435600, 1377439200, '3', 48, 'Main stage');
INSERT INTO "sets" VALUES (49, 1377439200, 1377442800, '2', 49, 'Main stage');
INSERT INTO "sets" VALUES (50, 1377442800, 1377446400, '2', 50, 'Main stage');
INSERT INTO "sets" VALUES (51, 1377192600, 1377198000, '3', 51, 'Alternative stage');
INSERT INTO "sets" VALUES (52, 1377198000, 1377207000, '1', 52, 'Alternative stage');
INSERT INTO "sets" VALUES (53, 1377207000, 1377212400, '2', 53, 'Alternative stage');
INSERT INTO "sets" VALUES (54, 1377212400, 1377217800, '2', 54, 'Alternative stage');
INSERT INTO "sets" VALUES (55, 1377217800, 1377223200, '2', 55, 'Alternative stage');
INSERT INTO "sets" VALUES (56, 1377223200, 1377228600, '2', 56, 'Alternative stage');
INSERT INTO "sets" VALUES (57, 1377228600, 1377234000, '1', 57, 'Alternative stage');
INSERT INTO "sets" VALUES (58, 1377234000, 1377239400, '2', 58, 'Alternative stage');
INSERT INTO "sets" VALUES (59, 1377239400, 1377244800, '2', 59, 'Alternative stage');
INSERT INTO "sets" VALUES (60, 1377244800, 1377253800, '1', 60, 'Alternative stage');
INSERT INTO "sets" VALUES (61, 1377253800, 1377259200, '2', 61, 'Alternative stage');
INSERT INTO "sets" VALUES (62, 1377259200, 1377271800, '1', 62, 'Alternative stage');
INSERT INTO "sets" VALUES (63, 1377271800, 1377277200, '1', 63, 'Alternative stage');
INSERT INTO "sets" VALUES (64, 1377277200, 1377282600, '1', 64, 'Alternative stage');
INSERT INTO "sets" VALUES (65, 1377289800, 1377295200, '3', 65, 'Alternative stage');
INSERT INTO "sets" VALUES (66, 1377295200, 1377302400, '1', 66, 'Alternative stage');
INSERT INTO "sets" VALUES (67, 1377302400, 1377307800, '2', 67, 'Alternative stage');
INSERT INTO "sets" VALUES (68, 1377307800, 1377315000, '1', 68, 'Alternative stage');
INSERT INTO "sets" VALUES (69, 1377315000, 1377322200, '1', 69, 'Alternative stage');
INSERT INTO "sets" VALUES (70, 1377322200, 1377327600, '2', 70, 'Alternative stage');
INSERT INTO "sets" VALUES (71, 1377327600, 1377331200, '2', 71, 'Alternative stage');
INSERT INTO "sets" VALUES (72, 1377331200, 1377338400, '1', 72, 'Alternative stage');
INSERT INTO "sets" VALUES (73, 1377338400, 1377342000, '2', 73, 'Alternative stage');
INSERT INTO "sets" VALUES (74, 1377342000, 1377347400, '2', 74, 'Alternative stage');
INSERT INTO "sets" VALUES (75, 1377347400, 1377352800, '1', 75, 'Alternative stage');
INSERT INTO "sets" VALUES (76, 1377352800, 1377358200, '2', 76, 'Alternative stage');
INSERT INTO "sets" VALUES (77, 1377358200, 1377361800, '1', 77, 'Alternative stage');
INSERT INTO "sets" VALUES (78, 1377361800, 1377369000, '3', 78, 'Alternative stage');
INSERT INTO "sets" VALUES (79, 1377374400, 1377379800, '2', 79, 'Alternative stage');
INSERT INTO "sets" VALUES (80, 1377379800, 1377385200, '2', 80, 'Alternative stage');
INSERT INTO "sets" VALUES (81, 1377385200, 1377390600, '1', 81, 'Alternative stage');
INSERT INTO "sets" VALUES (82, 1377390600, 1377396000, '1', 82, 'Alternative stage');
INSERT INTO "sets" VALUES (83, 1377396000, 1377403200, '1', 83, 'Alternative stage');
INSERT INTO "sets" VALUES (84, 1377403200, 1377410400, '1', 84, 'Alternative stage');
INSERT INTO "sets" VALUES (85, 1377410400, 1377419400, '1', 85, 'Alternative stage');
INSERT INTO "sets" VALUES (86, 1377419400, 1377427500, '2', 86, 'Alternative stage');
INSERT INTO "sets" VALUES (87, 1377430200, 1377433800, '3', 87, 'Alternative stage');
INSERT INTO "sets" VALUES (88, 1377435600, 1377440100, '3', 88, 'Alternative stage');
INSERT INTO "sets" VALUES (89, 1377122400, 1377126000, '1', 89, 'Visuals stage');
