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
INSERT INTO "lst__location_types" VALUES (11, 'Prévention', 'marker_prevention', 'fr');
INSERT INTO "lst__location_types" VALUES (11, 'Prevention', 'marker_prevention', 'en');
INSERT INTO "lst__location_types" VALUES (7, 'Départ navette', 'marker_camp', 'fr');
INSERT INTO "lst__location_types" VALUES (7, 'Shuttle', 'marker_camp', 'en');
INSERT INTO "lst__location_types" VALUES (12, 'Forêt enchantée', 'marker_forest', 'fr');
INSERT INTO "lst__location_types" VALUES (12, 'Psy forest', 'marker_forest', 'en');
INSERT INTO "lst__location_types" VALUES (13, 'Déchetterie ', 'marker_garbage', 'fr');
INSERT INTO "lst__location_types" VALUES (13, 'Garbage centre', 'marker_garbage', 'en');

INSERT INTO "locations" VALUES (1, 45.1150476749639, 5.61031765810242, 4, NULL);
INSERT INTO "locations" VALUES (2, 45.1126881428484, 5.60822144150734, 4, NULL);
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

INSERT INTO "location_descriptions" VALUES (1, 'Scène principale', 'fr');
INSERT INTO "location_descriptions" VALUES (1, 'Main stage', 'en');
INSERT INTO "location_descriptions" VALUES (2, 'Scène alternative', 'fr');
INSERT INTO "location_descriptions" VALUES (2, 'Alternative stage', 'en');
INSERT INTO "location_descriptions" VALUES (3, 'Arrivée de la navette', 'fr');
INSERT INTO "location_descriptions" VALUES (3, 'Shuttle arrival', 'en');
INSERT INTO "location_descriptions" VALUES (4, 'Départ de la navette', 'fr');
INSERT INTO "location_descriptions" VALUES (4, 'Shuttle departure', 'en');


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

INSERT INTO "bios" VALUES (1, 'From a star system many light years away comes Merkaba, a forward thinking and inspiring light being incarnated into human form in Australia. Drawing musical influence from fusion jazz, funk, dub step, techno and break beat, Merkaba skilfully combines these to create his own style of psychedelic progressive trance and an epic dance floor journey of healing and awakening.', 'en');
INSERT INTO "bios" VALUES (2, 'Humerous is a Professional Producer/Dj from South Africa who plays a fusion of Tech, Prog and Psy sets. With a wide range of sounds with mega Groove, Pads, Rhythm and melodic Leads. ', 'en');
INSERT INTO "bios" VALUES (3, '“Stretch” is the solo live act of Julien Fougea from Digital Talk. Be prepared for a legendary performance with absolutely fresh, body and mind grooving production.', 'en');
INSERT INTO "bios" VALUES (4, 'Yamaga est DJ resident du labal Hadra depuis 2009. C''est également un des membres fondateur de l''association.
Ses DJ sets énergiques sont tournés vers la psytrance nocturne et mettent en avant les sons psychedeliques et les ambiances colorées de la scene psytrance anglaise.

Manu, bercé très jeune par la musique reggae, dub & ragga, s''intéresse aux sons électroniques vers la fin des années 90''s. La Psytrance et le mouvement venu de Goa l''attirent indéniablement. Il s''est d''abord investi dans l''organisation d''évènements avec l''association grenobloise Hadra. Des 2005 il est le régisseur technique des soirées tels que Digital illusion, Altitude ou le Hadra Trance Festival.
La rencontre avec David aka dj Leptit et la découverte d''artistes comme GOW, Jahbo, Ajja ou Evp, lui permettent aux fils des années, d''affiner ses goûts musicaux.
C''est tout naturellement qu''il apprend à mixer un style résolument dark psychedelic ! Il rejoint l''équipe des dj''s du label Hadra Records en 2008. Grâce à une excellente technique, ces djset explosifs sont d''''une efficacité redoutable !!', 'fr');
INSERT INTO "bios" VALUES (5, 'Malice in Wonderland''s music is characterized by a complex & solid production. The wide variations of sounds, rhythmic structures and textures which seem to move through otherworldly sounddimensions are giving the compositions a very unique and special character.', 'en');
INSERT INTO "bios" VALUES (6, 'Created in 2001, Electrypnose is exploring the electronic music-and-noise world and tries to share its sound universe.
Based in Switzerland, the laboratory doesn''t stop to be active since the beginning of the project. After the first track releaed in 2003 by Peak records, more than 100 pieces of work have been released on record labels from all over.
Beside working in the studio, Vince is travelling regulary around the globe to present his new material and to share time with the comunity.', 'en');
INSERT INTO "bios" VALUES (7, 'His project Harmonic Rebel aims to influence the audience with psychedelic music that recreates that shift in life and widens the horizon for new infinite possibilities. His music can be described as chunky, groovy, trippy psychedelic trance that is designed to be performed in several times of the night and day.', 'en');
INSERT INTO "bios" VALUES (8, 'David AKA Shotu discovered electronic music in 1996 at free parties in France. In 2003 he met the Hadra tribe, and here he soon found his place. He has played across the global at parties and festivals such as Universo Paralello and Boom Festival. Shotu style is night-time psychedelic style and has 2 albums under his belt ''Jungle Expedition'' and ''Conception''
Now preparing the third album : "SHOTU - Friends"', 'en');
INSERT INTO "bios" VALUES (9, 'EVERBLAST is: Earthling (Ibiza) and Chromatone (SF, USA) ...', 'en');
INSERT INTO "bios" VALUES (10, 'Earthspace is the psychedelic trance project from Matheus Nogueira from Fortaleza, northeast Brazil. His music has unique dynamics which blends smooth agressive basslines and hardcore acid synths. Extreme creative constructions which flows from experimental psychedelia to dancefloors burners. He is currently resident @ NuAct collective, one of the most important organizations for alternative culture and psychedelic events in northeast Brazil and in half of 2012 he was invited to join with great and respected brazilian label Mosaico Records.', 'en');
INSERT INTO "bios" VALUES (11, 'DJ GINO is founder and executive producer of SONICA FESTIVAL. Now a veteran about psychedelic music gatherings, he begins his active participation since 1990, immediately keen on acid-techno frequencies of that age. His dancefloor-driving experience melted with his professional know-how in event management led him to project and create SONICA FESTIVAL together with other partners from Rome, where He''s based since 1998. ', 'en');
INSERT INTO "bios" VALUES (12, 'Le projet SINE DIE est créé en 2004, né du désir de composer une musique inspirée de l''esprit Trance Goa d''antan mêlé aux sonorités beaucoup plus psychédéliques des soirées psytrance d''aujourd''hui. Cette alchimie donne ainsi naissance à  un style très novateur, dynamique, coloré et résolument orienté dancefloor.', 'fr');
INSERT INTO "bios" VALUES (13, 'Headroom likes to break the realm of the expected. He strives for the best quality production in high tech trance, keeping it fun, psychedelic & a little bit twisted. He has been ‘eyes wide shut - ears wide open’ in his studio, working on a new tale to add to his offbeat sound story with a positively new chapter coming your way soon. Headroom embraces the rush of a full floor of full on. fans, in full emotion', 'en');
INSERT INTO "bios" VALUES (14, 'LovPacT est un nouveau projet trance-progressive combinant le groove psychédélique du renommé LunaRave et les mélodies envoutantes & mélancoliques de Sleeping Forest, dernière recrue ambiant du label Hadra. Après de nombreuses collaborations secrètes,et d''innombrables scéances, leur premier album voit enfin le jour : Retrodelik à découvrir en février 2013 sur le label français !', 'fr');
INSERT INTO "bios" VALUES (15, 'mayaXperience" - a.k.a. *dj maya* (Peter Vlach) & *dj Sun Experience* (Thorsten Schuch) - is a well known Psy-Prog duo, based in Vienna. After many years of playing powerful dj-sets together on stage, they decided to combine in one name - finally *mayaXperience* was born in 2011.', 'en');
INSERT INTO "bios" VALUES (16, 'PROTONICA are Piet Kaempfer and Ralf Dietze from Berlin (Germany). With Piet''s intuition for harmonies as a skilled pianist and certified sound designer Ralf''s feeling for propulsive grooves, Protonica deliver you their very own positive pulsing sound - a blend of psychedelic and progressive trance. ', 'en');
INSERT INTO "bios" VALUES (18, 'Le groupe français Secret Vibes est une formation electro-acoustique inspirée de sonorités aux horizons divers, (musiques ethniques, lyriques et électro). Samplers, effets spéciaux, percussions, didgeridoos, flûtes & voix aux multiples facettes, le groupe collabore avec de nombreux musiciens, danseurs et performers et offre des lives plein de fantaisie ! Secret Vibes est entré sur Hadra Records au début 2011.', 'fr');
INSERT INTO "bios" VALUES (19, 'D_Root discovered electronic music and digital arts back in 1996, 13 years ago. He knew from the start he wanted to make a career of writing electronic music. In 1998, he started creating hartechno (hardtechno ?) and
performing in free parties up until 2002, at which point he wanted to explore new horizons. From there on, his music evolved towards hybrid melodic and rhythmic structures, a unique mix between techno and
psyketrance. With time, he has acquirred new equipment and improved his technical skills.

Since 2004, D_Root has dedicated his time to creating trance music, mostly progressive trance. He has clearly been influenced by the work of Vibrasphere or Liquid Soul? In the last three years, concert dates bookings

have increased continuously, D_Root has performed in different parties and festivals in France, Switzerland, Belgium, Burkina Faso. On differentoccasions, he shared the stage with famous artists such as Gaudium, DayDin,Astral projection, HyperFrequencies, Quantize...

He signed with the label Hadra, his 1st album out in July of 2010. Meanwhile, you will be able to discover and share D_Root?s unique musical

universe on diverse occasions where he is scheduled to appear for an dynamic, progressive live performance. At the same time, D_Root and Lunarave are collaborating on a progressive/fullon « LUNAROOT » project, which will reserve you many surprises.', 'en');
INSERT INTO "bios" VALUES (20, 'Le cosmos, le voyage, le temps et l''Histoire...autant de notions qui définissent l''univers de Lunarave. C''est en écumant, dès 2005, les scènes de la région Rhône-Alpes au sein des associations Psynap''s et Hadra avec entre autres des dates aux Nuits Sonores, au Hadra Trance Festival ou encore à la Natural Trance à Grenoble, que Lunarave fait connaître son style et sa griffe !
Son live act détonnant est acclamé de date en date et Lunarave sera bientôt repéré au delà des frontières ou il fera des sets remarqués en Suisse, Allemagne, Belgique, Autriche , Italie mais surtout en Grèce, ou son premier album : ''The 4th Sun'', rencontre un franc succès.
Aujourd''hui, plus que jamais, Lunarave est en pleine effervéscence et nous concocte simultanément les sortie de plusieux projet, comme LovPacT , en duo avec Sleeping forest, dernière recrue ambiant du label, Angry LunA, dans un style hybride entre full-on et hi-tech, ou encore LunaRooT, vous l''aurez compris, une collaboration psy-gressive originale avec D_RooT...
Et si tout ce déroule comme prévu, nous aurons probablement le plaisir de voir un second opus Lunarave pointer le bout de son nez d''ici la fin 2013!!
Inutile d''en écrire plus, écoutez et laissez-vous emporter par cet artiste complet !', 'fr');
INSERT INTO "bios" VALUES (21, 'Justin Chaos is a riddle, wrapped in a mystery, inside an enigma. Having spent the last fifteen years on the loose in the global psychedelic scene, not only has he built a formidable reputation as a promoter, connector and facilitator of big projects, but he has refined and widely shared his skills as an impeccable dj weaving between genres in his eternal search for organised chaos.

We are thrilled to have him on board as a member of the Zero One team spreading the musical love !!!', 'en');
INSERT INTO "bios" VALUES (22, 'Chris Rich is a psychedelic trance producer from the South – West of England and is a happy memeber of the Bom Shanka family. Chris'' sound makes a great energy on the dance-floor, big rolling bass-lines mixed with twisted atmosphere and leads, seamlessly combining the elements of night and day.', 'en');
INSERT INTO "bios" VALUES (24, 'GiLL a.k.a HYPER FREQUENCiES started his career in the Eighties as a bass player in several psychedelic rock bands. In 1994, GiLL discovered electronic music, bought two Technics MKII and taught himself how to mix. A year later, GiLL played at his first party and was instantly hooked on psychedelic trance. After djing for a few years on many parties, the next logical step was to return to his roots and start producing music again. GiLL was soon spotted by Mick CHAOS and, in 1998, he released his debut track “First Elevation” on Mick’s CHAOS Unlimited records. The first track was followed by “Drum Explorer”, a collaboration
with JAÏA, released on the compilation “ZEP TEPi”, also from CHAOS Unltd. both tracks
were released under his own name GiLL. After such a promising start, suddenly, all went really quiet around GiLL… A mysterious studio bug kept damaging his speakers every time he started to work. For the best part of a year, the problem could not be solved, until late in 2000, a specialist discovered that, the so-called “HYPERFREQUENCiES”, were responsible for all the technical problems Gill had experienced. A year worth of work had been lost, but GiLL s’ artist name “HYPER FREQUENCiES” was born. From there on, everything fell into place. Numerous releases on well-known labels such as Mechanik Records, Avatar records,
Parvati records and Turbo Trance Records followed and confirmed his talent. In 2003,
HYPER FREQUENCiES blasted the psytrance scene with his debut album “Red Crystal Moon” released on Mechanik Records. The album was an instant success.
Over the next few years, HYPER FREQUENCiES became a regular on compilations of labels such as Peak Records, Hadra, Timecode, Quantika, Spirit Zone, Geomagnetic, to name just a few. He toured the world intensively with his energetic live performances and created trancefloor mayhem wherever he played. To this day, HYPER FREQUENCiES
has performed in 30 countries and has released 70 tracks on 25 labels.
His current side projects include ADRENOKROHM (with PSYLOM), CRAZY LiONS CULT (with NEUROMOTORS). In 2006, Gill started to work on his long-awaited second album. For almost two years, he produced and carefully selected ten deep, tribal and psychedelic tracks, frequently editing them and testing them in many international parties and festivals including Universo Paralello, Rhythms of Peace, Hadra and Soulclipse. The end result, “PHANTASMATiKA”, represents HYPER FREQUENCiES phantasmagorical universe…unreal, mystic and deeply psychedelic. The much-awaited album is out
since December 2007 on Syncronize Records....
HYPER FREQUENCiES joined MANDALA Records in 2011 & released his third solo album "IN SPiRiTUS" on July 2012... Stay Tuned !!!', 'en');
INSERT INTO "bios" VALUES (25, 'Biorhythm consists of Carl Sharples (Luna) and Kieron Grieve (Rubix Qube). Born and bred in Cape Town. Bringing powerful rhythms to Festivals in South Africa and abroad, they have introduced their new cutting edge psychedelic sound to the world of trance. They both have their advanced diploma in sound engineering + music, and are the founders of The Village and The Village Records. Both of them have over 9 years of Biorhythm experience behind them and with 4 Albums released, they dont show any signs of stopping the Biorhythm wave! They also have a variety of aliases, where they express their musical talents in other genre''s. The Biorhythm live set can best be described as a raw and powerful experience. With influences like Lost ''n Found, Absolum and Digital Talk, they bring a plethora of thunderous music and soundscapes to all dancefloors around the world. Each member bringing their own unique flavor to the table, Biorhythm holds a reputation for taking people on a twilight journey of epic proportions!', 'en');
INSERT INTO "bios" VALUES (26, 'Gerhard Olivier aka Lost & Found, The well established South African producer has been hard at work and in the final stages of completing his second full-length album "Consumed" due for release on Hadra records (News coming soon). Lost & Found''s sound is packed with superb quality and variation which is always fascinating and always danceable.', 'en');
INSERT INTO "bios" VALUES (27, 'Hatta started to play DJ in ''96 as he was impressed by the festival called"RAINBOW 2000".In 1999,2000,he was invited to play at LX new year count down festival in Thailand .In 2002,he collaborated with Solstice Music to release Outback Eclipse Festival memorial compilation CD from the major records company.Hatta is known as the best distributor for domestic labels(Light Music,Fineplay Records,Arcadia Music) and many labels, contributing his great work for Japanese music market .In 2007,Hatta became A&R for Madskippers which is DJ-Tsuyoshi''s label, in the same year he joined the Wakyo Inc to take care of world wide distribution side.In 2008,Hatta set up his own label called "Grasshopper Records"busy doing his online mix at top radio station every week.He has played as DJ in Brazil (Universo Paralello) Mexico (Aeon Gathering) Thailand(The Experience Festival)until now. His compiled CD V.A./Round of night is selling on records shop /digital dl store .', 'en');
INSERT INTO "bios" VALUES (28, 'L'' un des premiers DJ a avoir jouer avec des Tests Pressing vinyls ( morceaux en avant premiere ) des labels tel que Matsuri ( Prana ), DragonFly , Phantasm , Transient , T.i.p ..... et amener les sons " Goa " en France et tout specialement en Rhone Alpes dans le Gard et l ''Herault .............', 'fr');
INSERT INTO "bios" VALUES (29, 'Broken Toy is James Copeland from Cape Town, South Africa.

His unique take on producing fullon psytrance with a twisted and quirky edge has earned him his position as one of the most low, down and dirty dancefloor manipulators in the scene today. Combining a forward thinking attitude with respect to the oldschool, you can expect a hard-hitting yet groovy dancefloor experience mixing elements of funk, indie-electro, and rocking psytrance.

It''s hard to imagine, but 2013 already marks 10 years of Broken Toy! Having started his career back in 2001 as a member of the project “Damage”, he was one of the pioneers of the South African psytrance scene alongside such artists as Rinkadink, Protoculture, and Shift. Soon after, he started the Broken Toy solo project and joined Uk based Alchemy records back in 2003.

Since then he has released over 70 tracks on various labels such as Nano, Spun, Timecode, Solar tech, etc and 2 full length albums on Alchemy records - his self-titled debut album and 2007`s “The low, down, dirty sound of…”

He has toured to over 20 countries and has played at major festivals all over the world such as Boom festival in Portugal, Glade festival in the UK, Full Moon festival in Germany, SOnica in Italy, Universo Parallelo in Brazil as well as all the major parties in South Africa such as Origin, Rezonance, Vortex, H20, Alien Safari, etc.

2010 saw him branching out in 2 new directions - going back to his roots as a metal guitarist and bass player with his tongue-in-cheek

psy-rock project “Super evil” and also with an electro breaks project called “Nesono”. More recently in 2011, he`s taken things down a notch and made some Swing/gypsy-jazz flavoured tech house in his new self titled project, James Copeland.

With so many creative outlets, an uncluttered and refined vision of Broken Toy has emerged in 2013, set to ignite dancefloors across the world once more. Look out for a new album as well as the Broken Toy 10 year anniversary VA featuring remixes by all his friends across the globe out soon!

Stay tuned and enjoy!', 'en');
INSERT INTO "bios" VALUES (30, 'Après avoir joué dans divers pays d''Europe, au coté des plus grands, (Infected Mushroom, Neelix, Astral Projection, Painkiller,Space Tribe...), il intègre la team Hadra sous ses deux projets Sangohan et Tilt.
Révèlé par la qualité technique et l''histoire de ses mix, en moins de deux ans, Tilt joue sur de nombreuses scènes progressives avec des artistes comme Day Din, Egorythmia, Loud,Zyce...
On lui reconnaît alors un style dynamique et soutenu, teinté de psychédélisme, à la recherche d''une technique et d''une cohérence toujours plus soignées.
Tilt travaille actuellement sur une compilation progressive, dont la sortie est prévue fin d année 2013,sur le label Hadra Record.', 'fr');
INSERT INTO "bios" VALUES (31, 'The Commercial Hippies (Anton Raubenheimer and Gareth Tacon) started their musical history together DJing breakbeat at parties in and around Cape Town, South Africa.

After a couple years of DJing the 2 decided to try their hand at producing Psy-Trance and soon after the concept of The Commercial Hippies was born.

Their first release "Where dreams come from" on Nano Records received a great response from trance lovers the world round and instantly caught the attention of many a Psy-Trance fan.

Since these early days TCH have grown from strength to strength and have released a number of extremely well received tracks such as “3 Star Hotel”, "Imaginoid" and their monster remix of Lark''s "Tricksy" to name a few.

After being in the studio for the better half of 2005 and the early months of 2006 they released their intricately detailed debut album “If you cant stand the heat, get out of the chai room” on Nano Records.

Their 2nd album "From Beyond" was released in 2009 to great reception and confirmed TCH as a force to be reckoned with!

Since "From Beyond" TCH have been touring around the globe and in September 2012 released their 5 track EP "Underground Overground" available online as a free download from their website www.thecommercialhippies.com

With a string of upcoming free and commercial releases, TCH are set for another busy year in 2013, and as usual you can expect TCH to be pushing the boundaries of the Psy-Trance genre, always wanting to try something new, and not conform to the status quo.', 'en');
INSERT INTO "bios" VALUES (32, 'Groove inspektorz est un projet né en 2012 qui réunit « Hypnos » et « Beat’N’Juice » 2 Live acts Prog de « Natural Beat Makerz » basés a Toulouse.

Après avoir écumé de nombreux dancefloors chacun de leur coté, ces deux potes de longue date dans la même optique musicale s’installent ensemble et montent ce projet qui leur permet de mettre en commun leur vision de la Trance Prog.

Une musique originale et groovy rythmée par de grosse basslines et des influences comme le hip hop ou le rock’n’roll.

Le studio est désormais leur lieu de vie, les sessions se multiplient et a chaque fois la recette fonctionne !
Déterminés a répandre les sourires et la bonne humeur sur tous les dancefloors qu’ils enflammeront, restez a l’écoute pour de nouvelles aventures !', 'fr');
INSERT INTO "bios" VALUES (33, 'During the following years this event became internationally renowned as the festival forProgressive Trance, featuring fantastic lineups from year to year and becoming one of the classical Trance festivals in the European outdoor season. Already now Montagu and Golkondahad a major influence on the German Progressive scene which also led to an ever growing number of bookings to international destinations. But still there was energy and passion leftfor their very own interpretation of modern Progressive music, so they started producing their own stuff under the moniker Symphonix. Fed from heaps of DJ experiences on world widedance floors, also this project became rapidly very successful. Already being international DJs and producers as well as promoters now, Montagu and Golkonda found it to be kind ofdecent to start an own label, too. So since 2006 Blue Tunes Records delivers frequently sophisticated, always up-to-date Progressive Trance. And just to have this list complete,the two brothers run another club music influenced project named True Lies and are involved in various other acts as well.
As this short outline reveals, there are few persons being that diversely involved into theProgressive Trance scene since its very early days like Montagu and Golkonda are.Their lifestyle is high-class Trance music and quite everything that has to do with it for
breakfast, lunch and dinner. Their vast experience with the music and the scene and their numerous journeys around the world have a major influence on their DJ sets which can be described as up-to-date Progressive Trance with a sometimes housey or electroid edge, depending on the time and atmosphere on a dance floor.', 'en');
INSERT INTO "bios" VALUES (34, 'With musical inspiration from all over the world and the ages a new journey is born featuring a touch of funk, of the 70''s, breakbeats, ethnic, world fusion, dub, afro-beat, electronica, psychedelic and progressive trance the combination of all these elements makes for a very ecclectic and even avantgarde sound some say...
Very groovy and flowing, subtly shifting atmospheres, down to earth dancing pulses and yet flying high like divine ecstatic dream states...
After a few years of performing, writing and recording music together in various projects flutist Pearce Van Der Merwe and percussionist-multi-instrumentalist Daniel Symons decided to create a duo together called COSMOSOPHY. Following the success of their debut solo albums "Flooting Grooves--Upsyde Downe", "Dymons-Druids Brew" and their bands'' album "The Peaking Godddess Collective-Organika" produced with Ajja, Master Margherita, DJ Gaspard, and Tanya (founders of Peak Records), Pearce and Dan have created new concoctions of sounds different to all the previous releases.', 'en');
INSERT INTO "bios" VALUES (35, 'Dj gui start your adventure in psytrance world going in 1998 in the freaks parties at south of Bahia/ Brasil, and after that he falling in love for the psytrance tunnes and start playing in parties and make festivals like "Cachoeira alta dance festival/ Earth dance festival/ Brasil, and others (11 editions until now), near the year 2004 i was invite to make part of HADRA team. In last 2 years, i start with my own music productions, a project called !FUCKYEAH!, trying mix many styles in same style with focus in psytrance tunes , and now i have a honour into make part again of the line-up from this beautiful HADRA festival !!', 'en');
INSERT INTO "bios" VALUES (36, 'Hoodwink AKA Kelton Jones has been producing his own style of Psy-trance for a number of years now. His style manages to cover the domain of night whilst still maintaining a positive feeling, pulling people towards the speakers, but still retaining an edgy attitude.
The groovy aspect makes it great for the daytime too, so we are sure his music will go down well at any time. He has previously released tracks on each of the Wildthings Records compilations, and has released 2 Albums with the Label, ”Audio Illusion” and “Sound Mirrors” and is currently busy writing his third album to be released in the very near future.', 'en');
INSERT INTO "bios" VALUES (37, 'Pionnier de la scène psychédélique suisse, il y organise des « Goa party » dès 94 et joue dans plusieurs Live Act pour le mythique label londonien Return to the Source. Sa quête sonore le fait explorer toutes les « couleurs » de ce qu’il appelle l’ADM (Adult Dance Music), allant de la Tribal-Psy-Prog à la Twisted-Forest-Trance. Son objectif restant toujours l’énergie et la good vibe du dance-floor. Mais l’essentiel dans un set PsyberpunK, c’est l’histoire racontée et en général elle est intensément psychédélique.', 'fr');
INSERT INTO "bios" VALUES (38, 'Passionné par la synthèse sonore, le duo Cubic Spline (Clément Bastiat et Olivier Richard) trouve son unicité dans des atmosphères mystérieuses et des textures sonores travaillées. Il a ainsi développé un son très mental et détaillé qui marie Ã merveille les racines de la trance goa, l''hypnotisme de la dark psytrance et la puissance des productions modernes.

Le duo a rejoint l''équipe du label Hadra Records au début de l''année 2010 et, après une première release sur la compilation Hadracadabra 5 du label, Cubic Spline sort son premier EP, « Unusual Path », en octobre 2010. Un an et demi après, le duo revient avec son premier album « Paradigm Shift », conte psytrance/dark retraçant l''histoire d''un scientifique en plein périple intergalactique.

Soyez prêts à ressentir les ondes puissantes d''un live psychédélique et transcendant !', 'fr');
INSERT INTO "bios" VALUES (39, 'Vertical is Joonas Lehtinen, born and bred in Finland. He has always been in to electronic music and around 1997 he started to experiment with compositions of his own.

Even though his first contact with Goa trance was in 1996, he really started to get interested in to the world of psytrance in 2001 through various Finnish and foreign projects. 2002 he attended to his first psyparty which was definetily the kick start for him to make this kind of music.

He gets his inspiration from parties and everyday life, nature, space and what not.
Nowadays his music is a mixture of funny and serious, mind and dancefloor friendly psytrance. In his tracks he has got his own individual vibe which makes him stand out.

His music has taken him to a tour in Japan, France, festivals in Germany and Russia, parties in France, Russia, Switzerland and more yet to come.', 'en');
INSERT INTO "bios" VALUES (40, 'Colin started Alien Safari and started Djing at the same time in 1995. It has been a wild ride since then making crazy parties and playing in beautiful places all over the world. Love is the key to a good dj set or party or anything really.', 'en');
INSERT INTO "bios" VALUES (41, 'DHARMA is a Project started in 2009 in the city of Udine (IT) by Enrico Alpini. After 8 years studying as a drummer, several years of djing, and thanks to the inspiration of many events he attended in the past, he threw himself into producing psytrance music.
Due to the strong inspiration and good quality of the music, and a long friendship from the italian parties, even before having a live act ready, Dharma joined forces with Looney Moon Records to spread his fresh and original underground sound all over the globe.
Characterized by powerful funky basslines, researched acidic atmospheres and a happy and mild forestish sound choice in the leads, all his bright influences will transport you to a dynamic and euphoric journey into new sound lanscapes.
Simplicity, humility and passion are the core values at the root of this project, along with brotherhood, cosmic love, holographic awareness and quantum enightenment!', 'en');
INSERT INTO "bios" VALUES (42, '.. keeping it psychedelic but twisted, translating his good taste in music into hypnotic, groovy tunes of his own property, in his studio, working to make a new tale to add to his tracks sound story with a nice develop. And that’s what Endeavour is all about: psytrance with alucinating melodies, uplifting textures, serious and clear basslines and a evolving sound.,', 'en');
INSERT INTO "bios" VALUES (43, 'Driss est un DJ emblématique du label Hadra Records et l''un de ses fondateurs. Son expérience de la scène trance en France et à l''international, en qualité d''organisateur ou pour ses DJsets acclamés dans certains des plus grands festivals mondiaux (Ozora en Hongrie, Aurora en Grèce, Transahara au Maroc et bien d''autres), n''a cessé de nourrir son attachement pour la Trance goa, un style musical auquel il s''identifie pleinement. Depuis l''aventure de l''association Spiritual Freequencies dont il est l''un des initiateurs, il participe à la création et au développement de l''association Hadra, de son label et de son festival !

Après ses premières scènes en 2006, notamment au sein de l''équipe des GanjaTree, JimSon découvre Hadra cette même année et décide de devenir son ambassadeur en Belgique, projet concrétisé en novembre 2009 avec l''organisation de la première Hadra Label Party en Belgique (à Elewijt). En 2010, JimSon fait son entrée au sein du label Hadra Records qu''il promeut à l''étranger et dont le catalogue alimente largement ses DJ Sets en France et à l''étranger. Première compilation « Pangaea », à venir sur le label pour Juin 2011 !', 'fr');
INSERT INTO "bios" VALUES (44, '"A-Team" , one of the leading acts on Nutek Records, is a collaboration project of Painkiller and Bliss.
they collaborated one millennium ago under the name ("Shaff-Puff"),and later on created their individual projects.
A-Team holds up a unique style of trance music, translating their instrumental experience into the latest edition of
hi tech production, the blend of the 2 minds creates an explosion of new ideas and carry you on intensively into Trance.
They released a double album at first (Fast Forward), than a single album (145 Street), a compilation (As hard as it gets) and 2 eps (Smashing Napkins and Piranha). coming up a new Remixes ep and new A-Team ep to shake up your summer.', 'en');
INSERT INTO "bios" VALUES (45, 'Dj Natron aka Laugi hails from the Northeast of Germany, has been born in November of 1983, and has been living in Hamburg since 2001. Pystrance began enticing him in 1999. Loving the laughing people on the floor and the music which put them into meditation he also started to dj in 2002 and went from organising his first small open air in 2004 to organising the IntactXpanda in Hamburg every Winter and the Psychedelic Experience Open Air every May as well. His contribution to the quest for the original Psytrance sound was founding his own label Solar Tech Records in March of 2009. Its motto already says it all: Serious Psytrance. Thru many ups and downs, thankful for the support of his family and friends Dj Natron keeps playing music in between Progressive and FullOn, at 140-145 bpm – deep high quality on the dot with a progressive touch, pumping baselines intended to let people fly. Preferably at sunset or dawn he has had sets at all larger European festivals. The first compilations of Solar Tech were teeming with rolling, deep basses, mind-bending sound figures and hypnotic melodies. Pushing but still relaxed this music is definitely aiming at the mind! In the future international djs from all continents will present their visions of original Psytrance music here.', 'en');
INSERT INTO "bios" VALUES (46, 'Their first experiences with writing and producing music goes back to 2003. After many years of experimentation and graduating as sound engineers they were ready to let the world listen to their excellent productions. Many well received releases followed at Ovnimoon, BMSS and Geomagnetic Records. By the end of 2012 they got noticed and signed by Dacru Records to release their debut full album with this Belgian label in 2013.', 'en');
INSERT INTO "bios" VALUES (47, 'DJ représentant de l''univers de la Trance Progressive sur le label Hadra Records et programmateur du Hadra Trance Festival, Didier, alias Kokmok conçoit des sets dans un style très dansant où basses prog et rythmes groovy sont au rendez-vous ! Kokmok a sorti sa première release en 2008 : « Obsessiv Progressiv » et s’apprête a faire trembler les dancefloors avec un second opus, sorti début 2013.', 'fr');
INSERT INTO "bios" VALUES (48, 'Lyctum (Dejan Jovanovic - Serbia/Belgrade) is one of the
few producers who dares to step out of the Psychedelic
Trance formula, creating new soundscapes with an uplifting
mood and high amount of atmosphere. It comes as no surprise
that his previous work has been released for many respected
labes and his collaborations now days include artists like:
Zyce, Nerso, Sideform, Aquafeel, Zen Mechanics, John 00
Felming and many more, Dejan released his first album in 2012
and got rated to the 2nd place of most selling album in the
Progressive-Psy-Trance genre. As well his music was
included in Paul Oakenfold and John 00 Fleming radio mixes.', 'en');
INSERT INTO "bios" VALUES (49, 'Aerospace is Guy Youngman (aka DJ Guyshanti) from Nahariya, Israel. A DJ and producer since 1998. In 2005 Guy began his solo progress…', 'en');
INSERT INTO "bios" VALUES (50, 'Facebook bio (PLEASE REVIEW): Looking back on more then one decade of intense involvement in the Trance scene, Benjamin Halfmann aka Audiomatic witnessed the rich evolution of this sub culture with all its innovations, variations and all its ups and downs. Even more: During the years his project constantly gained quality and became a reference for sophisticated, high-functional Progressive Trance. Therefore Audiomatic became an integral ingredient of this scene and has shaped its musical development with his unique sounds which have been part of legendary parties and festivals all around the globe.

It all started with some powerful Heavy Metal: Benjamin Halfmann banged his head to the music of AC/DC and posters of Punk, Hardcore and Metal bands adorned the walls of his room up to the age of 15 years. Then he discovered the fascinating worlds of Psy Trance and rapidly became engaged as a DJ, first public performances followed in 1999.

But soon a strong desire to convert his creativity into own music arose, and so Audiomatic got hungry for more. His producer’s breakfast consisted of some tasty Fruity Loops and this simple production software instantly made him aware that the games on his computer were not only a waste of time, but also of hard disc space.

Not even one year later his music already convinced the label management of Leviathan Music and so the track “Woodstock” became the first official release in 2002. Various contributions on different compilations followed and the year 2004 delivered the first full length Album “Multiplayer”. Together with Stefan “Magnetrixx” Lewin, who has always been a good friend and important source of inspiration for Benjamin Halfmann, the project “Audiomatrixx” was formed, gained euphoric feedback and also brought to life a studio album in 2008. The same year, Audiomatic’s second album was released. Of course this is not the end of his productivity: Dedicating the next work to his fellow citizens in mind, the album “Weekend Society” became another pleasant success in 2011.
', 'fr');
INSERT INTO "bios" VALUES (50, 'Facebook bio (PLEASE REVIEW): Looking back on more then one decade of intense involvement in the Trance scene, Benjamin Halfmann aka Audiomatic witnessed the rich evolution of this sub culture with all its innovations, variations and all its ups and downs. Even more: During the years his project constantly gained quality and became a reference for sophisticated, high-functional Progressive Trance. Therefore Audiomatic became an integral ingredient of this scene and has shaped its musical development with his unique sounds which have been part of legendary parties and festivals all around the globe.

It all started with some powerful Heavy Metal: Benjamin Halfmann banged his head to the music of AC/DC and posters of Punk, Hardcore and Metal bands adorned the walls of his room up to the age of 15 years. Then he discovered the fascinating worlds of Psy Trance and rapidly became engaged as a DJ, first public performances followed in 1999.

But soon a strong desire to convert his creativity into own music arose, and so Audiomatic got hungry for more. His producer’s breakfast consisted of some tasty Fruity Loops and this simple production software instantly made him aware that the games on his computer were not only a waste of time, but also of hard disc space.

Not even one year later his music already convinced the label management of Leviathan Music and so the track “Woodstock” became the first official release in 2002. Various contributions on different compilations followed and the year 2004 delivered the first full length Album “Multiplayer”. Together with Stefan “Magnetrixx” Lewin, who has always been a good friend and important source of inspiration for Benjamin Halfmann, the project “Audiomatrixx” was formed, gained euphoric feedback and also brought to life a studio album in 2008. The same year, Audiomatic’s second album was released. Of course this is not the end of his productivity: Dedicating the next work to his fellow citizens in mind, the album “Weekend Society” became another pleasant success in 2011.
', 'en');
INSERT INTO "bios" VALUES (51, 'Shangaan Electro is the high-speed dance phenomenon from South Africa that is rising from streets into clubs, homes and venues all around the globe.
The creation of charismatic producer, record label mogul and businessman Nozinja,  this is a very contemporary product of Africa. Hailing from rural Limpopo but now based in Soweto, Nozinja saw the chance to update Shangaan music for the 21st Century, replacing its traditional bass/guitar instrumentation with midi-keyboard sounds and repitched vocal samples (in English and seemingly sampled from rave anthems). Propelled by jacking four-to-the-floor beats and trademark drum-fills, the sound quickly became a hit at weekly street parties in Soweto, with young and old competing to show off their moves to this dizzyingly fast music, which can reach speeds of up to 189 beats per minute.', 'en');
INSERT INTO "bios" VALUES (52, 'Rafael Aragón aka Rafiralfiro is a latin/arabic rooted french musician / dj / composer / producer. Born & raised in Paris, between downtown and suburbs, he grew up surrounded by different cultures that really built him, first as a human being and later as an artist.', 'en');
INSERT INTO "bios" VALUES (55, 'Traversing the cosmos, gliding across dimensions beyond time and space, Kalya Scintilla brings universal shamanic journeys through his music to planet earth straight from his heart. His music paints sacred soundscapes with world fusion beats from ancient futures hidden amongst our forgotten memories to bring forth lush healing vibrations to activate the dormant codes within us. ', 'en');
INSERT INTO "bios" VALUES (56, 'Depuis 2010, avec ADN, amoureux des nouvelles technologies qui gravitent autour de la musique et de la vidéo, ils s’amusent ensemble sur de nouveaux projets mi mix mi live qu’ils partagent volontier en festival... Que ce soit à Ozora , à Ambiosonic, à Avalon ou pour les World People en France et à Goa, ils accélèrent le tempo en nous régalant sur des sons ethnik, des ambiances tribales, des samples d’ailleurs mariés à des nappes d’émotions, accompagnées de voix du monde...', 'fr');
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
INSERT INTO "bios" VALUES (59, 'Electrypnose, the electric hypnosis, is Vince Lebarde''s multi-flavored musical project.

Created in 2001, Electrypnose is exploring the electronic music-and-noise world and tries to share its sound universe.
Based in Switzerland, the laboratory doesn''t stop to be active since the beginning of the project. After the first track releaed in 2003 by Peak records, more than 100 pieces of work have been released on record labels from all over.
Beside working in the studio, Vince is travelling regulary around the globe to present his new material and to share time with the comunity.d', 'en');
INSERT INTO "bios" VALUES (60, 'Working as a DJ and musician since 1993 he''s been spinning acidizing rhythms and kicked the crowd with exhausting psychedelic trance music. Consequent grooves with kickin'' melodies shaped his sets on European dancefloors. Successful with techno and ambient releases in the mid 90''s, he''s additionally been working as a reporter and photographer for German music magazines like Frontpage, X-Mag, Loop and others. Over the years Dense used to organize several techno and trance events in Germany (D''vents).
In 2008 he refined his striking style of performing progressive ambient music. Luckily he took his ability to play the right music at the right time from the dancefloor to the chill out floor. Meanwhile Dense is one of the most popular ambient DJs in Germany - performing chillgressive® style.
Beside deejaying and after broadcasting the 6 a.m. Saturday morning chillout radio show on Trancefan Radio for years, since 2010 his homebase is Chromanova.fm, based in Berlin. He''s responsible for the program of the Chromanova.fm Chillout and Ambient Radio stream where you can catch him exclusively on Sundays with his weekly four hour radio show „Chill On!", what is part and parcel of ambient music scene by now and supported by most of the important chill out labels.
His project together with GMO is working well with the second album release „Tales From The Yellow Kangaroo“, out since September 2012 on Altar Rec. Single tracks like „Remount" or „Moonflower“ have just been released on the compilations „Floating Spirals" (Vimana Rec.) and „Spring“ (Altar Rec.) and give an outlook on his solo album „Exhale", scheduled for July 2013.', 'en');
INSERT INTO "bios" VALUES (61, 'Zen Baboon is composed of Daniel Rosado and Henrique Palhavã. Both were born in the 70`s in Portugal and since an early age kept a close connection with the music world. Musically and without mention any names and styles, both love projects with great sound quality and with original construction.
Daniel is a sound engineer that worked in a Lisbon studio for National Geographic documentaries and started producing electronic music in the late 90′s. Henrique, a zoologist, lives and works in his own farm and started as an ambient dj and producer in 2001. They got together in 2003 and decided to melt their musical preferences and create Zen Baboon.
With two tracks on Ambient Planet Vol 2, one on Electrik Dream VA Cosmic Yellow, they are now working on some new releases and remixes for other compilations and their first Zen Baboon album "Suber" to be released on Electrik Dream in 2013.
They have played in major parties and festivals like Boomfestival and keep the continuous learning process awake.', 'en');
INSERT INTO "bios" VALUES (62, 'Electronic activist depuis le debut des année 90''s.
Taj est devenu le manager du label Electrik dream en 2005.
En ce moment, il travail sur un projet Chill Out Ethnik - Downtempo "UASCA" en collaboration avec Dj Fluxo (Quest 4 goa - PT).
lls sortiront leur premier album "Cosmos Umbilical" courant 2013.
Lorqu'' il mixe,il est reconnu pour « raconter une histoire »
et prendre une foule à chaque étape vers l''illumination.
Ce chemin peut prendre plusieurs styles de « musique » du, Downtempo - Ambient - Electro - Progressive, Techno, Trance – Psy Trance, avec le même [DJ]] set : Taj ne connait aucune frontière entre les styles et les tempos.
Lors de l'' edition Hadra festival 2013 Taj jouera un set Electro, Uptempo, Trance, Progressiv sur l'' Alternativ Stage avec un immense plaisir !!!', 'fr');
INSERT INTO "bios" VALUES (63, 'Inspiré par des musiciens comme Jean-Michel Jarre et Vangelis, mais aussi par la musique classique et cinématographique en général, Akshan commence à composer dès 1997.
Malgré une activité professionnelle intense, il passe tout son temps libre dans son studio, mettant de côté les contraintes de théorie musicale. Son oreille, l''intuition et l''inspiration guident sa créativité.
Fort de ses expériences et de l''influence d''artistes comme Aes Dana, Asura (Ultimae Records) et Juno Reactor, Akshan explore les royaumes du downtempo et de la psychédélique ambiant, sans oublier les compositions orientées dancefloor.
Sa rencontre avec DJ Zen (manager du label Altar Records) est alors inéluctable, comme l''atteste la sortie des albums "The Tree of Life" en 2012 et de "The Rise Of Atlantis" en 2013.
Mystique et ethnique, cinématographique et symphonique, électronique et acoustique…autant de qualificatifs qui caractérisent cette musique, unique et atypique, inspirante et inspirée, qui vous transcendera et vous accompagnera dans la danse.', 'fr');
INSERT INTO "bios" VALUES (64, 'Down-tempo does not have to equate to diminished energy, in fact it can cause a roaring of the spirit matching anything that pounds away at 140bpm. Suitable surrounds and subtle sounds are as equally uplifting by reaching for rhythms that are in tune with the most peaceful elements of the soul. Psy-ambient duo Land Switcher are substantial examples of this finest of artistic abilities by taking listeners/participants on a round tour of the expanses of the cosmos of human experience.

As individuals Freddy Chauvin and Antoine Martineau are proven talented musicians with formative Dub Roots but in co-joining in 2010 they have taken on new capabilities as the collaborative components of Land Switcher. With joyfully received live show taking them across the continents and an ever-expanding back-catalogue they are now beginning to mine the full potential of their greater whole.

Land Switcher is a cosmic journey through colors, grooves and intriguing atmospheres...', 'en');
INSERT INTO "bios" VALUES (65, 'C''est à son retour d''Inde en 2007 que le flûtiste Guillaume Barraud, un des rares spécialistes de la flûte bansuri en Europe, fait la connaissance du joueur de sitar Kengo Saito. L''un est disciple du légendaire Hariprasad Chaurasia et l''autre s''est formé auprès du maître Kushal Das. Leur longue collaboration prend de multiples formes au fil des ans. Ils se réunissent à nouveau, cette fois aux côtés du tabliste Nabankur Battacharya, originaire de Calcutta. Flute&Luth nous plonge au coeur d''une tradition millénaire. Le chant subtil et envoûtant de la flûte, les mélodies intriquées du sitar s''entremêlent aux battements de transe des tablas, et nous emmène dans un voyage sublime et mystérieux.', 'fr');
INSERT INTO "bios" VALUES (66, 'Surnommé Dj tout terrain ou Général, Véritable tête de pont dans le métissage musical, DA, producteur sur son label No Fridge, Dj Click s’est depuis longtemps fait le complice des projets les plus bouillants : avec son groupe Click Here ou l’extravertie Rona Hartner pour les cultures tsiganes, comme avec les marocains Gnawa Njoum d’Essaouira et les Hamadcha de Fés, ou encore dans son collectif électro‐jazz UHT°, en attendant ses mixes brasileiros de Zuko 103, Dj Dolorès ou africanistes avec Issa Bagayogo. Sur scène ou en studio il a collaboré avec Dulsori (Vinari show), Dj Panko (Ojos de Brujo), Transglobal Underground, Marcelinho da Lua, Dhoad, Smadj, Va Fan fahre, Parno Graszt..
Il a produit une dizaine d’albums dans son studio, de nombreux remixes comme ceux de Manu Chao, Watcha Clan, Warsaw Village Band, Nicolette, Mahala Raï Banda, Burhan Öçal, Boogie Balagan ou Rachid Taha, et est paru sur de prestigieuses compilations. Click en explorateur alchimiste, invente un nouveau genre digital folk, produit une musique sans visa, met de l’urbain dans le rural et du vivant dans la mécanique électronique.
Programmé sur les principaux festivals, clubs et salons internationaux (Womex Seville 2006, Babel Med Marseille 2009, AWME Melbourne 2011, ApaMM Ulsan 2012, Medimex Bari 2012, Porto Musical Recife 2013), de la Corée du Sud au Brésil, des Balkans à l’Australie, de L''Afrique du Nord au Japon, il répand son style partout dans le monde.', 'fr');
INSERT INTO "bios" VALUES (67, 'Scott Sterling''s inspiration is the drum and drumming, the creation of rhythm with the human hand. Equally important is the primal connection between drummer and dancer. An expert percussionist of many years training, he is immersed in rhythm and all its modalities, from the hypnotic, trance-inducing pulse to the energy of ecstatic dance.

Bringing the drums to bellydancers, yogis, and the emerging transformational festival culture, Drumspyder weaves the rhythms and tonalities of the Mediterranean and live Arabic percussion into his own style of electronic dance fusion, distinguished by its funky, (belly)danceable grooves and rhythmically intense, percussion-driven sound.
He works in a broad range of tempos - ranging from slinky, sexy downtempo through funky mid-tempo whomp to high-energy tribal house. He is also ready to collaborate with dancers or other types of stage performers in improvised Arabic-style drum solos or choreographed pieces.

Originating in San Francisco''s crucible of bellydance, bass culture, and world music fusion, Drumspyder has released 3 albums worth of original music (most recently "Kytheria" on Dakini Records) and is a prolific remixer, lending his signature live percussion and melodic touch to the Desert Dwellers, the Spy from Cairo, Mirabai Ceiba, and various traditional Arabic ensembles, with many more on the way. In addition, his music has been featured in numerous dance DVDs and theatrical productions. A second full-length album is in the works for 2013.', 'en');
INSERT INTO "bios" VALUES (68, 'Bayawaka aka Golan Aflalo (Enig''matik Records) is a DJ and promoter located in Tel Aviv, Israel.
Originally Born and raised in Jerusalem , Since a very young age started loving and feel the MUSIC.. Classic rock, hard rock, metal & blues, 60s, 70s and israeli music .
In the Middle of The 90''s electronic music comes to my life ! and Since I''m addicted to electronic music like trance , techno Progressive from all the kinds and colors.
the trance is changing every few years and since 2008 I''m mostly in the direction of Bass music ranging from Glitch to Psystep and Chillout.
BAYAWAKA its a special lizard turned colors just like the music I love to play, Many different styles that connect together like colors ...
Living in Tel Aviv and just got back from a successful European summer festival tour having DJed at:
Tree of Life, Freqs of Nature, Antaris, Boom Festival, Utopia after Boom, Ozora, Lost Theory, Vuuv Festival and Transylvania Calling.
Golan is heavily involved in the Tel Aviv bowling Art and Culture scene, being an active club and festival promoter as well as producing art and music exhibitions in the city.', 'en');
INSERT INTO "bios" VALUES (69, 'DJ GINO is founder and executive producer of SONICA FESTIVAL. Now a veteran about psychedelic music gatherings, he begins his active participation since 1990, immediately keen on acid-techno frequencies of that age. In 1993 he buys his first desks technics-1200, after having been collecting a large vinyl archive and, getting in touch with local insiders, Gino starts to collaborate for the production of several parties in Italy (Naples, his native land) with famous artists from London and Detroit underground scene. After a pause, gone-by 1996 to 1999, when he''s been traveling throughout South American as well as Oriental cultures, Gino comes back to psychedelic scene attending most of the best events around the globe. His dancefloor-driving experience melted with his professional know-how in event management led him to project and create SONICA FESTIVAL together with other partners from Rome, where He''s based since 1998.

Gino plays his natty dj-set in many international festivals and clubs.
His style is an harmonic fusion of psychedelic and cyber frequencies, and his musical research aims to constantly upgrade the worldwide dance music trends. He is usually able to present two different dj-set, for open-air or indoor stages, dynamically shifting clever selections of contemporary acid sounds with pumping and rocking beats.

Gino released Chacruna on December 2008, his first VA Comp. on Echoes Records, and on May 2010, he founded Sonica Recordings releasing Sonica VA Comp Vol II on June 2010; Healing Lights VA Compiled by Djane Gaby on May 2012 and just released last Oct 2012 the Sonica VA Comp Vol III', 'en');
INSERT INTO "bios" VALUES (70, 'James Copeland is an electronic musician based in Cape Town, South Africa with over a decade of experience rocking dancefloors around the world. Whether its psytrance with his Broken Toy project, cyber-metal with Super evil or electrobreaks with his Nesono project - this is a guy who isnt afraid to move into new territory.

Most recently he felt the need to move away from synthetic and "cyber" sounding dance music and make some tunes focused on vintage sounds and tropical flavours. The result is the first of his projects to bear his real name. Infectious jazz-infused tones and cheeky basslines move from double time swing through to brassy, tech-house beats meeting a bit of 70s funk and balkan beats on the way. Its fun music for the drunker side of the dancefloor - the only thing serious here is the groove!

Its due to this musical hyperactivity that the James Copeland "vintage swing tech" sound has grown and evolved at such amazing speed. Kicking off with a winning remixes for the legendary Green Velvet and fellow south africans Goldfish, he`s gone on to release on Bart and Baker`s acclaimed Electroswing compilations on Wagram records as well as EPs with overseas labels What! What!, Bedroomuzik, Broken records and TRibal vision. His next label collaboration saw him teaming up with electro swing and house music legend Tavo and his 3Star Deluxe record label for the release of his take of the Jungle Book classic, "King of the swingers" and a string of other releases soon on the way.

Having already rocked local dancefloors at premier events like Rocking the Daisies, Earthdance, and The Flamjangled Tea party, the past year has taken him to some interesting festivals abroad like Body and Soul in Ireland, Tree of Life in Turkey, Electronic Brain in Canada as well as random gigs as far afield as India, the UK and Australia. The Copeland express might be rolling into your town soon , so look alive and stay tuned!', 'en');
INSERT INTO "bios" VALUES (71, 'Composers Daniel Rosado and Henrique Palhavã got together in 2003 to make their ambient music Zen Baboon. Zen Racoon is their techno approach.', 'en');
INSERT INTO "bios" VALUES (72, 'Music collector since the early 90''s. Started deejaying and producing in 2001. In 2003 was invited to be part of music artists of Boomfestival''s Ambient area with Paulo Dias aka dj high.
A zoolist and farmer, combines his music with his way of living.
Member of Zen Baboon and Zen Racoon.', 'en');
INSERT INTO "bios" VALUES (73, 'SYNC24 is Daniel Segerstad from Carbon Based Lifeforms. Started in early years experimeting with tape recorders and simple music players on the Commodore 64 and gradually upgrading equipment and techniques. Having CBL as his main focus and some other projects on the side there''s always room for personal reflections. SYNC24 is the result of sudden impulse of inspiration, late nights without interference from others.
more info : ultimae.com', 'en');
INSERT INTO "bios" VALUES (74, 'Carbon Based Lifeforms (CBL) are Johannes Hedberg and Daniel Segerstad (né Ringström), both born in 1976 and based in Göteborg, south-west Sweden.
They met when both 15 and are still, amazingly, working happily together, over 20 years later.
CBL itself was formed in 1996, as an offshoot from other projects, but it soon became their focal point, culminating in their first release on ‘mp3.com’ in 1998.
Even before this, 1996''s "The Path" is their unreleased and highly sought-after demo that any listeners fortunate to hear the contents will immediately recognise as the genesis of the group''s unique sound.
The duo signed with the Lyon-based ‘Ultimae’ label in 2002 and have since released four official full-length albums.
Their music has been extraordinarily well received by connoisseurs within the ambient scene and clearly beyond, since in 2012 they were also commissioned to write the soundtrack to the independently-produced movie ''The Mansion'', whose release date will be announced in due course.
In addition to their own work, they have actively participated in successful collaborations with several other artists, including Magnus Birgersson of Solar Fields in a project to compose the music for the Swedish dancer Olof Persson’s ‘Fusion’ performance in 1999. Johannes often creates the building blocks of sounds and harmonies and could happily tweak sounds forever, whereas Daniel develops the rhythms and sculpts the ideas into tracks.', 'en');
INSERT INTO "bios" VALUES (75, 'Sleeping Forest, est une artiste ambient qui a récemment intégré le label Hadra. Son style allie des influences tournées vers le rock psychédélique et la world music à une minutie propre aux musiques électroniques. Son premier album « Rise of Nature » sort le 15 février 2013 sur Hadra Records. L''atmosphère qu''elle y développe est à la fois lancinante, mélancolique et épique. Ses compositions regorgent de mélodies chargées en émotions et de nappes en constante évolution. On y retrouve des rythmiques entêtantes dans lesquelles les instruments percussifs traditionnels sont mis à l''honneur et de savants agencements mélodiques qui rappellent de nombreux courants musicaux tels que le classique, le rock progressif, le trip-hop ou encore les musiques de film. Elle utilise également de nombreux samples de nature qui permettent une immersion encore plus totale dans son univers. Cet album est fait pour faire vibrer et bercer son auditeur dans un monde de plénitude dont seule Sleeping Forest a le secret.', 'fr');
INSERT INTO "bios" VALUES (76, 'Laurence’s first published work was on a Liquid Sound compillation in 2001 "Mana Medecine" for which he did collaborations with Youth, Humphrey Bacchus & Nigel Watson (Opus 3).He then went on to collaborate with Michele adamson (Shpongle) and Russell Davies,son of kinks founder member Dave davies. They produced a trio of tracks for Zulu Lounge Records (Mexico) which received great acclaim around the world.
This lead to the birth of KUBA and the release of his debut album "Inside out" on Liquid Sound Design, record label of veteran producer Youth (Killing Joke/The ORB). In the 6 years since releasing this album kuba has released a further four albums on Chillcode records, and built a devoted following of fans globally which has seen him perform at countless festivals and parties world-wide', 'en');
INSERT INTO "bios" VALUES (77, 'B-Brain fait son entrée dans le monde de la nuit comme décorateur scénographe et organisateur rejoignant un sound system du sud de la France. Ses productions sont depuis lors composés d''un mélange éclectique oscillant entre ses influences reggae/dub et jungle/drum&bass . En 2010, toujours activiste de la team Hadra et des formation de DJing, il intègre le label Hadra Records et nous offre des lives et DJsets aux rythmes saccadé avec des basses puissantes et envoûtante.', 'fr');
INSERT INTO "bios" VALUES (78, 'Soom T is an Internationally touring artist, performing a live concert of reggae and dub with a fresh twist of hip-hop. She spreads a philosophical renegade message inspiring a positive evolution of the soul. Supporting the world wide legalisation of marijuana, her bass driven music will leave you feeling refreshed and ready for more.', 'en');
INSERT INTO "bios" VALUES (79, 'Trompettiste et multi-instrumentiste, c''est dans les années 2000 que Lakay découvre les musiques électroniques en côtoyant les free-parties.
Issu d''influences à la fois traditionnelles et undergrounds, son live nous propose un doux voyage vacillant entre le dub & dubstep, la trance downtempo et la jungle psychédélique.
Le mélange coloré des synthés électroniques et des instruments traditionnels nous offre une musique à la fois énergique et méditative propice au voyage de l''esprit. Les boucles électroniques se mêlent aux mélodies de la trompette jazz, du seung Thaïlandais ou encore au groove des guitares espagnoles autour d''un métissage riche et vivant. Depuis 2008, il nous invite à découvrir ses univers vers l''éveil des sens et des émotions...', 'fr');
INSERT INTO "bios" VALUES (80, 'le projet Digidep, incarné par Arash Kian, est un savant mélange entre trip-hop, IDM et break-beat. Egalement influencé par la trance depuis son entrée sur le label Hadra Records, il a su développer, au fil des années, un son unique nourri de textures synthétiques et d''une grande sensibilité pour les sonorités instrumentales que de nombreuses années de pratique des percussions ont grandi. Digidep est à la croisée des styles ambiances indus et psychédéliques, passages survoltés et planants sont les ingrédients d''une musique précise et soignée, aussi bien dansante que méditative !', 'fr');
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
INSERT INTO "bios" VALUES (83, 'Issus de parcours musicaux différents dans le Reggae/Dub et le Beatbox,
Toinan (Scratchy) et Difa (Itchy) se réunissent autour de leur passion commune, la musique électronique, et fondent le projet "Itchy & Scratchy" en 2009.
Si les premières collaborations se font en "Jungle", très vite le duo s''ouvre à d''autres styles notamment la Bass Music , la Minimal et la Trance...
Leur musique est un univers sombre mélangeant Bass Music lourde et puissante à des sonorités Psygressive en passant par des grooves Dark Prog et Minimal qui vous feront explorer l''inexplorable !', 'fr');
INSERT INTO "bios" VALUES (84, '6NOK est un subtile mélange entre un dandy chic et un punk à chien.....passionné de tekno depuis toujours,membre actif du collectif lyonnais KONECTIK, 6NOK délivre des sets teintés minimal tekno avec comme mots d''ordres du groove et de l''amour...Alors, affutez vos claquettes et préparez bien vos zigomatiques,sinon gare au claquage!', 'fr');
INSERT INTO "bios" VALUES (85, 'Bercé au son du rock gothique, Philip Contamin aka Sysyphe découvre les sonorités électro avec des formations comme The Orb, Solar Quest en 93.

Depuis 94, musicien (bassiste), Dj/compositeur et organisateur de soirées, il s’implique toujours dans le milieu musical électronique.

A partir de 2005, il intègre Hadra et participe à l''élaboration de compilations (Hadravision, Hadracadabra V, Hadravision II (2013)).

Depuis 2007, impliqué dans l’organisation du festival Hadra, il participe activement à la programmation de la scène alternative en tant que responsable de la programmation de la partie chill-out / ambient/ downtempo.

En 2010, il sort son album « Running up that hill » dans un style psy-ambient/ downtempo masterisé par Vincent Villuis (Aes dana) d''Ultimae records.

Depuis, toujours en quête d''approfondir ses compositions, il développe, dans ses créations, un son mélodique, suave, tout en cultivant ses premières influences gothiques tant dans ses longs dj sets que dans sa propre musique.', 'fr');
INSERT INTO "bios" VALUES (86, 'Erot is Tore Mortensen born in 1986 in Aalborg, Denmark. In 2004 he started producing music just for the fun, but in 2007 it got a bit more serious. He has been faithfull to his unique style, but always pushing new boundaries and experimentations, and the result is the wide spectrum of styles since pure Chill Out, to more ambient tunes, from psychill passing through uptempo chill to even progressive music.', 'en');
INSERT INTO "bios" VALUES (87, 'A la croisée des Mémoires...
"Quand les chants du monde se font récit... Quand les cultures s''entremêlent et témoignent du passé, du présent... mélodieuses, percussives et tribales. Comme l’histoire qui est tienne et qui m’a montré un chemin, min hounak… Ont retenti alors les voix de demain, et je me souviens…

"Un répertoire de musique aux couleurs de l’Orient et du Maghreb alliant sonorités traditionnelles et compositions actuelles. Fortement inspiré par l’intensité et la générosité de la musique et de la poésie arabe : Mahmoud Darwich, Adonis, Youssef Al Khal…

Chems, (guitare voix) / David Bruley (Percussions Orientales) / Hassan Abd Alrahman (Oud, Ney, voix) / Yannick Benahmed ( contrebasse).', 'fr');
INSERT INTO "bios" VALUES (88, 'Nikel Gorr ?
C''est une partie de jeu de l''oie. C''est un déjeuner sur l''herbe. C''est un tour sur soi-même, c''est le feu de la vie, c''est la pointe dans son centre et le passant qui s''ignore. C''est ton pied dans ma main, c''est le sol qui rigole, c''est la route qui s''évade par-delà les oiseaux, c''est la vibe pure comme du beurre salé. C''est une fleur arrachée à offrir à un verre, c''est le souffle coupé d''un coquillage hurlant, c''est le ciel et la mer tapis dans le béton, c''est le voyage qui ne s''arrête jamais de commencer.
Nikel Gorr c''est tout ça, mais par les oreilles.', 'fr');

INSERT INTO "artists" VALUES (1, 'Merkaba', NULL, 'AU', 'ph_merkaba', NULL, 'http://merkabamusic.bandcamp.com/', 'https://facebook.com/125869530759273', 'https://soundcloud.com/merkabamusic ', 'Zenon Records', 1);
INSERT INTO "artists" VALUES (2, 'Humerous', NULL, 'ZA', 'ph_humerous', NULL, NULL, NULL, 'https://soundcloud.com/humerous', 'PsynOpticz Productions', 2);
INSERT INTO "artists" VALUES (3, 'Stretch', NULL, 'FR', 'ph_stretch', NULL, NULL, NULL, NULL, 'Timecode Records', 3);
INSERT INTO "artists" VALUES (4, 'Yamaga vs Manu', NULL, 'FR', 'ph_yamaga', NULL, 'https://soundcloud.com/yamaga-yann', NULL, 'https://soundcloud.com/manu-hadra', 'Hadra Records', 4);
INSERT INTO "artists" VALUES (5, 'Malice In Wonderland', NULL, 'AT', 'ph_malice_in_wonderland', NULL, 'http://miw.snappages.com/', 'https://facebook.com/123409511028924', 'https://soundcloud.com/malice-in-wonderland', '2to6 Records', 5);
INSERT INTO "artists" VALUES (6, 'Electrypnose', NULL, 'CH', 'ph_electrypnose', NULL, 'http://www.electrypnose.com', 'https://facebook.com/256314211128486', 'https://soundcloud.com/electrypnose', 'Electrypnosis Media', 6);
INSERT INTO "artists" VALUES (7, 'Harmonic Rebel', NULL, 'GB', 'ph_harmonic_rebel', NULL, 'www.psynonrecords.com', 'https://facebook.com/222028371154090', 'https://soundcloud.com/harmonic-rebel', 'Psynon Records', 7);
INSERT INTO "artists" VALUES (8, 'Shotu', NULL, 'FR', 'ph_shotu', NULL, 'www.hadra.net', 'https://facebook.com/137121816332052', 'https://soundcloud.com/shotu', 'Hadra Records', 8);
INSERT INTO "artists" VALUES (9, 'Everblast', NULL, 'US – UK', 'ph_everblast', NULL, 'http://zero1-music.com', 'https://facebook.com/147794921934708', 'https://soundcloud.com/everblast', 'Zero 1 Music', 9);
INSERT INTO "artists" VALUES (10, 'Earthspace', NULL, 'BR', 'ph_earthspace', NULL, NULL, 'https://facebook.com/165670406797175', 'https://soundcloud.com/earthspacelive ', 'Mosaico Records', 10);
INSERT INTO "artists" VALUES (11, 'Gino', NULL, 'IT', 'ph_gino', NULL, NULL, NULL, 'https://soundcloud.com/twinlights', 'Sonica Recording', 11);
INSERT INTO "artists" VALUES (12, 'Sine Die', NULL, 'FR', 'ph_sine_die', NULL, NULL, NULL, 'https://soundcloud.com/sine_die', 'Hadra Records', 12);
INSERT INTO "artists" VALUES (13, 'Headroom', NULL, 'ZA', 'ph_headroom', NULL, 'http://www.nanomusic.net/', 'https://facebook.com/11366717836', 'https://soundcloud.com/headroomusic ', 'Nano Records', 13);
INSERT INTO "artists" VALUES (14, 'Lovpact', NULL, 'FR', 'ph_lovpact', NULL, 'http://www.lunarave.com/#!site', 'https://facebook.com/363648290320761', 'https://soundcloud.com/lovpact ', 'Hadra Records', 14);
INSERT INTO "artists" VALUES (15, 'Mayaxperience', NULL, 'AT', 'ph_mayaxperience', NULL, NULL, 'https://facebook.com/160401917347003', 'https://soundcloud.com/mayaxperience', 'SoundLabPirates Records', 15);
INSERT INTO "artists" VALUES (16, 'Protonica', NULL, 'DE', 'ph_protonica', NULL, 'http://www.protonica.de', 'https://facebook.com/211934438510', 'https://soundcloud.com/protonica', 'Iono Music', 16);
INSERT INTO "artists" VALUES (17, 'Huda G.', NULL, 'fr', 'ph_huda_g', NULL, NULL, NULL, NULL, NULL, 17);
INSERT INTO "artists" VALUES (18, 'Secret Vibes', NULL, 'FR', 'ph_secretvibes', NULL, 'http://www.secretvibes.fr', 'https://facebook.com/72376177317', 'https://soundcloud.com/secret-vibes', 'Hadra Records', 18);
INSERT INTO "artists" VALUES (19, 'D_Root', NULL, 'FR', 'ph_d_root', NULL, NULL, NULL, 'https://soundcloud.com/d_root', 'Hadra Records', 19);
INSERT INTO "artists" VALUES (20, 'Lunarave', NULL, 'FR', 'ph_lunarave', NULL, 'http://www.lunarave.com', 'https://facebook.com/122274333592', 'https://soundcloud.com/lunarave', 'Hadra Records', 20);
INSERT INTO "artists" VALUES (21, 'Justin Chaos', NULL, 'AR', 'ph_justinchaos', NULL, NULL, 'https://facebook.com/695276801', NULL, NULL, 21);
INSERT INTO "artists" VALUES (22, 'Chris Rich', NULL, 'UK', 'ph_chrisrich', NULL, 'http://www.bomshanka.com/people/chrisRich', 'https://facebook.com/137345059711439', 'https://soundcloud.com/chrisrich', 'Bom Shanka Music', 22);
INSERT INTO "artists" VALUES (23, 'Ataro', NULL, 'UK', 'ph_ataro', NULL, NULL, NULL, 'https://soundcloud.com/ataro', 'Damaru Records, Free Radical Records', 23);
INSERT INTO "artists" VALUES (24, 'Hyper Frequencies', NULL, 'FR', 'ph_hyper_frequencies', NULL, NULL, 'https://facebook.com/210987502248345', 'https://soundcloud.com/hyperfrequencies', NULL, 24);
INSERT INTO "artists" VALUES (25, 'Biorhythm', NULL, 'ZA', 'ph_biorhythm', NULL, 'http://www.spectralrecords.com/', 'https://facebook.com/93826142725', 'https://soundcloud.com/biorhtyhm', 'Spectral Records / The Village Records', 25);
INSERT INTO "artists" VALUES (26, 'Lost & Found', NULL, 'ZA', 'ph_lostfound', NULL, NULL, 'https://facebook.com/33265208964', 'https://soundcloud.com/lostandfound', 'Hadra Records', 26);
INSERT INTO "artists" VALUES (27, 'Hatta', NULL, 'JP', 'ph_hatta', NULL, 'http://grasshopper-records.com/en', NULL, 'https://soundcloud.com/dj-hatta', 'Grasshopper Records', 27);
INSERT INTO "artists" VALUES (28, 'Loic', NULL, 'FR', 'ph_loic', NULL, NULL, 'https://facebook.com/100002958471327', 'https://soundcloud.com/loic-castello', 'Free-spirit Records', 28);
INSERT INTO "artists" VALUES (29, 'Broken Toy', NULL, 'ZA', 'ph_brokentoy', NULL, NULL, 'https://facebook.com/13177391747', 'https://soundcloud.com/brokentoy', 'Nano Records', 29);
INSERT INTO "artists" VALUES (30, 'Tilt', NULL, 'FR', 'ph_tilt', NULL, NULL, 'https://facebook.com/100003957510601', NULL, 'Hadra Records', 30);
INSERT INTO "artists" VALUES (31, 'The Commercial Hippies', NULL, 'ZA', 'ph_thecommercialhippies', NULL, 'http://thecommercialhippies.com', 'https://facebook.com/77937599359', 'https://soundcloud.com/thecommercialhippies', 'Nano Records', 31);
INSERT INTO "artists" VALUES (32, 'Groove Inspektorz', NULL, 'FR', 'ph_groove_inspektorz', NULL, 'http://www.groove-inspektorz.com', 'https://facebook.com/516798728349545', 'https://soundcloud.com/groove-inspektorz', 'Natural Beat Makerz Records', 32);
INSERT INTO "artists" VALUES (33, 'Golkonda', NULL, 'DE', 'ph_golkonda', NULL, 'http://www.bluetunes-records.com/engine/artist-montagugolkonda', 'https://facebook.com/10150097822395164', NULL, NULL, 33);
INSERT INTO "artists" VALUES (34, 'Cosmosophy', NULL, 'CH', 'ph_cosmosophy', NULL, 'http://www.elestialrecords.com', 'https://facebook.com/213227098722198', 'https://soundcloud.com/elestialmusic', 'Elestial Records', 34);
INSERT INTO "artists" VALUES (35, '! Fuckyeah !', NULL, 'BR', 'ph_fuckyeah', NULL, NULL, 'https://facebook.com/645286712', 'https://soundcloud.com/livefuckyeah', 'Nutek Records / Hadra Records', 35);
INSERT INTO "artists" VALUES (36, 'Hoodwink', NULL, 'UK', 'ph_hoodwink', NULL, 'http://www.wildthingsrecords.co.uk', 'https://facebook.com/214454087948', 'https://soundcloud.com/hoodwinkwildthingsrecords', NULL, 36);
INSERT INTO "artists" VALUES (37, 'Psyberpunk', NULL, 'CH', 'ph_psyberpunk', NULL, 'http://www.psyberpunk.ch', 'https://facebook.com/57443366816', 'https://soundcloud.com/psyberpunk-1/', NULL, 37);
INSERT INTO "artists" VALUES (38, 'Cubic Spline', NULL, 'FR', 'ph_cubicspline', NULL, 'www.hadra.net', 'https://facebook.com/125368040871079', 'https://soundcloud.com/cubic_spline', 'Hadra Records', 38);
INSERT INTO "artists" VALUES (39, 'Vertical', NULL, 'FI', 'ph_vertical', NULL, 'http://parvati-records.com', 'https://facebook.com/139691559396860', 'https://soundcloud.com/vertical_parvati ', 'Parvati Records', 39);
INSERT INTO "artists" VALUES (40, 'Zigganaut', NULL, 'ZA', 'ph_zigganaut', NULL, NULL, NULL, 'https://soundcloud.com/zigganaut-aka-wobblz', 'Alien Safari Records', 40);
INSERT INTO "artists" VALUES (41, 'Dharma', NULL, 'IT', 'ph_dharma', NULL, NULL, NULL, 'https://soundcloud.com/pigiu ', NULL, 41);
INSERT INTO "artists" VALUES (42, 'Endeavour', NULL, 'fr', 'ph_endeavour', NULL, NULL, 'https://facebook.com/100000466317049', 'https://soundcloud.com/endeavour ', NULL, 42);
INSERT INTO "artists" VALUES (43, 'Driss vs Jimson', NULL, 'FR', 'ph_driss', NULL, 'https://soundcloud.com/jimson-hadra', NULL, 'https://soundcloud.com/dj-driss-1', 'Hadra Records', 43);
INSERT INTO "artists" VALUES (44, 'A-Team', NULL, 'ES - IL', 'ph_ateam', NULL, 'http://www.nutek.org/ateam', 'https://facebook.com/50734344308', 'https://soundcloud.com/the-a-team', 'Nutek Records', 44);
INSERT INTO "artists" VALUES (45, 'Natron', NULL, 'DE', 'ph_natron', NULL, 'http://solartechrecords.com/artists/dj-natron', 'https://facebook.com/218938104804440', 'https://soundcloud.com/natronsoundz ', NULL, 45);
INSERT INTO "artists" VALUES (46, 'Spirit Architect', NULL, 'MK', 'ph_spirit_architect', NULL, NULL, NULL, 'https://soundcloud.com/spirit-architect ', NULL, 46);
INSERT INTO "artists" VALUES (47, 'Kokmok', NULL, 'FR', 'ph_kokmok', NULL, 'http://www.hadra.net/index.php?goto=/label_artistpage.php?dj=9&&l', 'https://facebook.com/220241861345716', 'https://soundcloud.com/kokmok-hadra', 'Hadra Records', 47);
INSERT INTO "artists" VALUES (48, 'Lyctum', NULL, 'RS', 'ph_lyctum', NULL, 'http://lyctumweb.wordpress.com', 'https://facebook.com/111107822309185', 'https://soundcloud.com/leectum ', 'Tesseract Records', 48);
INSERT INTO "artists" VALUES (49, 'Aerospace', NULL, 'fr', 'ph_aerospace', NULL, 'http://www.digitalnature.info', 'https://facebook.com/10492762657', 'https://soundcloud.com/aerospace ', NULL, 49);
INSERT INTO "artists" VALUES (50, 'Audiomatic', NULL, 'DE', 'ph_audiomatic', NULL, NULL, 'https://facebook.com/48034833862', 'https://soundcloud.com/benniaudiomatic ', 'Spin Twist Records', 50);
INSERT INTO "artists" VALUES (51, 'Shangaan Electro', NULL, 'ZA', 'ph_shangaan_electro', NULL, NULL, NULL, NULL, 'Honest Jon''s Records', 51);
INSERT INTO "artists" VALUES (52, 'Rafael Aragon', NULL, 'FR', 'ph_rafael_aragon', NULL, NULL, 'https://facebook.com/759705969', 'https://soundcloud.com/rafiralfiro ', 'Caballito / Fresh Poulp', 52);
INSERT INTO "artists" VALUES (53, 'Mateba', NULL, 'FR', 'ph_mateba', NULL, NULL, 'https://facebook.com/145767595584908', 'https://soundcloud.com/mateba ', 'Inside Recordings', 53);
INSERT INTO "artists" VALUES (54, 'Goth Trad', NULL, 'JP', 'ph_goth_trad', NULL, 'http://www.gothtrad.com', 'https://facebook.com/135940969804126', 'https://soundcloud.com/goth-trad ', 'Deep Medi Records', 54);
INSERT INTO "artists" VALUES (55, 'Kalya Scintilla', NULL, 'AU', 'ph_kalya_scintilla', NULL, 'http://merkabamusic1.bandcamp.com/', 'https://facebook.com/121242094567692', 'https://soundcloud.com/kalyascintilla ', 'Merkaba Music', 55);
INSERT INTO "artists" VALUES (56, 'Opale''s ADN', NULL, 'FR', 'ph_opale_adn', NULL, NULL, NULL, 'https://soundcloud.com/opale-karine ', 'Ultimae Records', 56);
INSERT INTO "artists" VALUES (57, 'Starspine', NULL, 'ZA', 'ph_starspine', NULL, 'http://www.nutek.org/index_2.html', 'https://facebook.com/416883498362565', 'https://soundcloud.com/starspine ', 'Nutek Rec. - Alchemy Rec', 57);
INSERT INTO "artists" VALUES (58, 'Gnaïa ', NULL, 'fr', 'ph_gnala', NULL, 'http://gnaia.net/', 'https://facebook.com/49782851637', 'https://soundcloud.com/gnaia', 'Oreades Prod', 58);
INSERT INTO "artists" VALUES (59, 'Electrypnose', NULL, 'Ch', 'ph_electrypnose', NULL, 'http://www.electrypnose.com/', 'https://facebook.com/256314211128486', 'https://soundcloud.com/electrypnose', 'Electrypnosis Media', 59);
INSERT INTO "artists" VALUES (60, 'Dense', NULL, 'De', 'ph_dense', NULL, 'http://www.chillgressivetunes.com/', 'https://facebook.com/364103207017193', 'https://soundcloud.com/chillgressivetunes', 'Chillgressive Tunes', 60);
INSERT INTO "artists" VALUES (61, 'Zen Baboon', NULL, 'Pt', 'ph_zen_baboon', NULL, 'http://www.electrikdream.com/', 'https://facebook.com/118447265557', 'https://soundcloud.com/zen-baboon', 'Electrik Dream', 61);
INSERT INTO "artists" VALUES (62, 'Tajmahal', NULL, 'fr', 'ph_tajmahal', NULL, 'http://www.electrikdream.com/artists/tajmahal', 'https://facebook.com/151174286726', NULL, 'Electrik Dream / Ultimae Rec', 62);
INSERT INTO "artists" VALUES (63, 'Akshan', NULL, 'fr', 'ph_akshan', NULL, 'http://www.altar-records.com/atlantis.html', 'https://facebook.com/121973874578593', 'https://soundcloud.com/akshanmusik', 'Altar Rec. ', 63);
INSERT INTO "artists" VALUES (64, 'Landswitcher', NULL, 'fr', 'ph_landswitcher', NULL, NULL, 'https://facebook.com/127910370624749', 'https://soundcloud.com/landswitcher', 'Free-spirit Rec.', 64);
INSERT INTO "artists" VALUES (65, 'Flute&Luth', NULL, 'fr', 'ph_fluteluth', NULL, NULL, 'https://facebook.com/100003246476169', 'https://soundcloud.com/bansouri/welcome-mp3', 'Association Yaman', 65);
INSERT INTO "artists" VALUES (66, 'DJ Click', NULL, 'fr', 'ph_dj_click', NULL, 'http://www.nofridge.com/', 'https://facebook.com/130807350323129', 'https://soundcloud.com/click-here', 'No Fridge ', 66);
INSERT INTO "artists" VALUES (67, 'Drumspyder ', NULL, 'Us', 'ph_drumspyder', NULL, 'http://www.drumspyder.com/drumspyder/home.html', 'https://facebook.com/166638194821', 'https://soundcloud.com/drumspyder', 'Dakini ', 67);
INSERT INTO "artists" VALUES (68, 'Bayawaka', NULL, 'il', 'ph_bayawaka', NULL, 'http://enigmatikrecords.com/', 'https://facebook.com/158366557620150', 'https://soundcloud.com/golan-28', 'Enigmatik Record / Tel Aviv', 68);
INSERT INTO "artists" VALUES (69, 'Gino', NULL, 'it', 'ph_gino_alternative', NULL, 'http://www.sonica-dance-festival.eu/', 'https://facebook.com/100000213537892', 'https://soundcloud.com/twinlights', 'Sonica Recordings ', 69);
INSERT INTO "artists" VALUES (70, 'James Copeland', NULL, 'ZA', 'ph_james_copeland', NULL, NULL, 'https://facebook.com/143759328995932', 'https://soundcloud.com/jamescopeland', '3star Deluxe', 70);
INSERT INTO "artists" VALUES (71, 'Zen Racoon', NULL, 'Pt', 'ph_zen_racoon', NULL, 'http://www.electrikdream.com/', 'https://facebook.com/109124547846', 'https://soundcloud.com/zen-racoon', NULL, 71);
INSERT INTO "artists" VALUES (72, 'Henrique', NULL, 'Pt', 'ph_henriq', NULL, 'http://www.electrikdream.com/', 'https://facebook.com/118447265557', 'https://soundcloud.com/henriq-aka-zen-babo', 'Electrik Dream / Boomfestival', 72);
INSERT INTO "artists" VALUES (73, 'Sync24 ', NULL, 'Se', 'ph_sync_24', NULL, 'http://sync24.se/', 'https://facebook.com/70770237537', NULL, 'Ultimae', 73);
INSERT INTO "artists" VALUES (74, 'Carbon Based Lifeforms ', NULL, 'Se', 'ph_carbon_based_lifeforms', NULL, 'http://carbonbasedlifeforms.net/', 'https://facebook.com/45457854115', NULL, 'Ultimae', 74);
INSERT INTO "artists" VALUES (75, 'Sleeping Forest', NULL, 'fr', 'ph_sleeping_forest', NULL, 'http://www.lunarave.com/sleepingforest', 'https://facebook.com/181124572023605', 'https://soundcloud.com/sleeping_forest', NULL, 75);
INSERT INTO "artists" VALUES (76, 'Kuba', NULL, 'Uk', 'ph_kuba', NULL, NULL, 'https://facebook.com/901810054', 'https://soundcloud.com/kuba-laurence-harvey', 'Liquid Sound Design,chillcode ', 76);
INSERT INTO "artists" VALUES (77, 'B-Brain', NULL, 'fr', 'ph_bbrain', NULL, 'http://hadra.net/', NULL, 'https://soundcloud.com/docteur-b-brain', 'Hadra Rec.', 77);
INSERT INTO "artists" VALUES (78, 'Soom T & Renegade Masters', NULL, 'Uk', 'ph_soom_t', NULL, 'http://www.renegademasters.com/', 'https://facebook.com/194522907258192', 'https://soundcloud.com/renegade-masters', 'Renegade Masters Rec.', 78);
INSERT INTO "artists" VALUES (79, 'Lakay', NULL, 'fr', 'ph_lakay', NULL, '', 'https://facebook.com/196404337062228', 'https://soundcloud.com/lakay-enjoy-people', 'Hadra Rec.', 79);
INSERT INTO "artists" VALUES (80, 'Digidep', NULL, 'fr', 'ph_digidep', NULL, NULL, 'https://facebook.com/100000549770712', 'https://soundcloud.com/digidep', NULL, 80);
INSERT INTO "artists" VALUES (81, 'Celt Islam', NULL, 'Uk', 'ph_celt_islam', NULL, NULL, 'https://facebook.com/615082714', 'https//soundcloud.com/celt-islam', 'Urban Sedated Rec.', 81);
INSERT INTO "artists" VALUES (82, 'D.Rec', NULL, 'fr', 'ph_drec', NULL, 'http://www.mixcloud.com/derek-teum/', NULL, NULL, 'Hadra Rec.', 82);
INSERT INTO "artists" VALUES (83, 'Itchy & Scratchy', NULL, 'fr', 'ph_itchy_scratchy', NULL, 'http://www.hadra.net/', 'https://facebook.com/100001683281932', 'https://soundcloud.com/itchy-and-scratchy', 'Hadra Rec.', 83);
INSERT INTO "artists" VALUES (84, '6nok', NULL, 'fr', 'ph_6nok', NULL, NULL, NULL, 'https://soundcloud.com/#6nokspurk', 'Konectik', 84);
INSERT INTO "artists" VALUES (85, 'Sysyphe', NULL, 'fr', 'ph_sysyphe', NULL, 'http://www.hadra.net/', 'https://facebook.com/100000032161863', 'https://soundcloud.com/sysyphe', 'Hadra Rec./ Ultimae Rec.', 85);
INSERT INTO "artists" VALUES (86, 'Erot', NULL, 'Dk', 'ph_erot', NULL, NULL, 'https://facebook.com/104121702984398', 'https://soundcloud.com/erot', 'Altar Rec.', 86);
INSERT INTO "artists" VALUES (87, 'Trio Bassma', NULL, 'fr', 'ph_trio_bassma', NULL, 'http://www.collectifbassma.org/', 'https://facebook.com/356086816205', NULL, 'Collectif Bassma ', 87);
INSERT INTO "artists" VALUES (88, 'Nikel Gorr', NULL, 'fr', 'ph_nikel_gorr', NULL, 'http://www.nikelgorr.com/', 'https://facebook.com/328503880606242', 'https://soundcloud.com/nikelgorr', NULL, 88);
INSERT INTO "sets" VALUES (1, 1377198000, 1377201600, 'Live', 1, 'Main stage');
INSERT INTO "sets" VALUES (2, 1377201600, 1377207000, 'DJ', 2, 'Main stage');
INSERT INTO "sets" VALUES (3, 1377207000, 1377210600, 'Live', 3, 'Main stage');
INSERT INTO "sets" VALUES (4, 1377210600, 1377217800, 'DJ', 4, 'Main stage');
INSERT INTO "sets" VALUES (5, 1377217800, 1377221400, 'Live', 5, 'Main stage');
INSERT INTO "sets" VALUES (6, 1377221400, 1377225000, 'Live', 6, 'Main stage');
INSERT INTO "sets" VALUES (7, 1377225000, 1377228600, 'Live', 7, 'Main stage');
INSERT INTO "sets" VALUES (8, 1377228600, 1377232200, 'Live', 8, 'Main stage');
INSERT INTO "sets" VALUES (9, 1377232200, 1377235800, 'Live', 9, 'Main stage');
INSERT INTO "sets" VALUES (10, 1377235800, 1377239400, 'Live', 10, 'Main stage');
INSERT INTO "sets" VALUES (11, 1377239400, 1377246600, 'DJ', 11, 'Main stage');
INSERT INTO "sets" VALUES (12, 1377246600, 1377250200, 'Live', 12, 'Main stage');
INSERT INTO "sets" VALUES (13, 1377250200, 1377253800, 'Live', 13, 'Main stage');
INSERT INTO "sets" VALUES (14, 1377253800, 1377257400, 'Live', 14, 'Main stage');
INSERT INTO "sets" VALUES (15, 1377257400, 1377264600, 'DJ', 15, 'Main stage');
INSERT INTO "sets" VALUES (16, 1377264600, 1377268200, 'Live', 16, 'Main stage');
INSERT INTO "sets" VALUES (17, 1377268200, 1377275400, 'DJ', 17, 'Main stage');
INSERT INTO "sets" VALUES (18, 1377282600, 1377288000, 'GIG', 18, 'Main stage');
INSERT INTO "sets" VALUES (19, 1377288000, 1377291600, 'Live', 19, 'Main stage');
INSERT INTO "sets" VALUES (20, 1377291600, 1377295200, 'Live', 20, 'Main stage');
INSERT INTO "sets" VALUES (21, 1377295200, 1377302400, 'DJ', 21, 'Main stage');
INSERT INTO "sets" VALUES (22, 1377302400, 1377306000, 'Live', 22, 'Main stage');
INSERT INTO "sets" VALUES (23, 1377306000, 1377309600, 'Live', 23, 'Main stage');
INSERT INTO "sets" VALUES (24, 1377309600, 1377313200, 'Live', 24, 'Main stage');
INSERT INTO "sets" VALUES (25, 1377313200, 1377316800, 'Live', 25, 'Main stage');
INSERT INTO "sets" VALUES (26, 1377316800, 1377320400, 'Live', 26, 'Main stage');
INSERT INTO "sets" VALUES (27, 1377320400, 1377327600, 'DJ', 27, 'Main stage');
INSERT INTO "sets" VALUES (28, 1377327600, 1377334800, 'DJ', 28, 'Main stage');
INSERT INTO "sets" VALUES (29, 1377334800, 1377338400, 'Live', 29, 'Main stage');
INSERT INTO "sets" VALUES (30, 1377338400, 1377345600, 'DJ', 30, 'Main stage');
INSERT INTO "sets" VALUES (31, 1377345600, 1377349200, 'Live', 31, 'Main stage');
INSERT INTO "sets" VALUES (32, 1377349200, 1377352800, 'Live', 32, 'Main stage');
INSERT INTO "sets" VALUES (33, 1377352800, 1377360000, 'DJ', 33, 'Main stage');
INSERT INTO "sets" VALUES (34, 1377369000, 1377374400, 'GIG', 34, 'Main stage');
INSERT INTO "sets" VALUES (35, 1377374400, 1377378000, 'Live', 35, 'Main stage');
INSERT INTO "sets" VALUES (36, 1377378000, 1377381600, 'Live', 36, 'Main stage');
INSERT INTO "sets" VALUES (37, 1377381600, 1377387000, 'DJ', 37, 'Main stage');
INSERT INTO "sets" VALUES (38, 1377387000, 1377390600, 'Live', 38, 'Main stage');
INSERT INTO "sets" VALUES (39, 1377390600, 1377394200, 'Live', 39, 'Main stage');
INSERT INTO "sets" VALUES (40, 1377394200, 1377399600, 'DJ', 40, 'Main stage');
INSERT INTO "sets" VALUES (41, 1377399600, 1377403200, 'Live', 41, 'Main stage');
INSERT INTO "sets" VALUES (42, 1377403200, 1377406800, 'Live', 42, 'Main stage');
INSERT INTO "sets" VALUES (43, 1377406800, 1377414000, 'DJ', 43, 'Main stage');
INSERT INTO "sets" VALUES (44, 1377414000, 1377419400, 'Live', 44, 'Main stage');
INSERT INTO "sets" VALUES (45, 1377419400, 1377426600, 'DJ', 45, 'Main stage');
INSERT INTO "sets" VALUES (46, 1377426600, 1377430200, 'DJ', 46, 'Main stage');
INSERT INTO "sets" VALUES (47, 1377430200, 1377435600, 'DJ', 47, 'Main stage');
INSERT INTO "sets" VALUES (48, 1377435600, 1377439200, 'Live', 48, 'Main stage');
INSERT INTO "sets" VALUES (49, 1377439200, 1377442800, 'Live', 49, 'Main stage');
INSERT INTO "sets" VALUES (50, 1377442800, 1377446400, 'Live', 50, 'Main stage');
INSERT INTO "sets" VALUES (51, 1377192600, 1377198000, 'GIG', 51, 'Alternative stage');
INSERT INTO "sets" VALUES (52, 1377198000, 1377207000, 'DJ', 52, 'Alternative stage');
INSERT INTO "sets" VALUES (53, 1377207000, 1377212400, 'Live', 53, 'Alternative stage');
INSERT INTO "sets" VALUES (54, 1377212400, 1377217800, 'Live', 54, 'Alternative stage');
INSERT INTO "sets" VALUES (55, 1377217800, 1377223200, 'Live', 55, 'Alternative stage');
INSERT INTO "sets" VALUES (56, 1377223200, 1377228600, 'Live', 56, 'Alternative stage');
INSERT INTO "sets" VALUES (57, 1377228600, 1377234000, 'DJ', 57, 'Alternative stage');
INSERT INTO "sets" VALUES (58, 1377234000, 1377239400, 'Live', 58, 'Alternative stage');
INSERT INTO "sets" VALUES (59, 1377239400, 1377244800, 'Live', 59, 'Alternative stage');
INSERT INTO "sets" VALUES (60, 1377244800, 1377253800, 'DJ', 60, 'Alternative stage');
INSERT INTO "sets" VALUES (61, 1377253800, 1377259200, 'Live', 61, 'Alternative stage');
INSERT INTO "sets" VALUES (62, 1377259200, 1377271800, 'DJ', 62, 'Alternative stage');
INSERT INTO "sets" VALUES (63, 1377271800, 1377277200, 'DJ', 63, 'Alternative stage');
INSERT INTO "sets" VALUES (64, 1377277200, 1377282600, 'DJ', 64, 'Alternative stage');
INSERT INTO "sets" VALUES (65, 1377289800, 1377295200, 'GIG', 65, 'Alternative stage');
INSERT INTO "sets" VALUES (66, 1377295200, 1377302400, 'DJ', 66, 'Alternative stage');
INSERT INTO "sets" VALUES (67, 1377302400, 1377307800, 'Live', 67, 'Alternative stage');
INSERT INTO "sets" VALUES (68, 1377307800, 1377315000, 'DJ', 68, 'Alternative stage');
INSERT INTO "sets" VALUES (69, 1377315000, 1377322200, 'DJ', 69, 'Alternative stage');
INSERT INTO "sets" VALUES (70, 1377322200, 1377327600, 'Live', 70, 'Alternative stage');
INSERT INTO "sets" VALUES (71, 1377327600, 1377331200, 'Live', 71, 'Alternative stage');
INSERT INTO "sets" VALUES (72, 1377331200, 1377338400, 'DJ', 72, 'Alternative stage');
INSERT INTO "sets" VALUES (73, 1377338400, 1377342000, 'Live', 73, 'Alternative stage');
INSERT INTO "sets" VALUES (74, 1377342000, 1377347400, 'Live', 74, 'Alternative stage');
INSERT INTO "sets" VALUES (75, 1377347400, 1377352800, 'DJ', 75, 'Alternative stage');
INSERT INTO "sets" VALUES (76, 1377352800, 1377358200, 'Live', 76, 'Alternative stage');
INSERT INTO "sets" VALUES (77, 1377358200, 1377361800, 'DJ', 77, 'Alternative stage');
INSERT INTO "sets" VALUES (78, 1377361800, 1377369000, 'GIG', 78, 'Alternative stage');
INSERT INTO "sets" VALUES (79, 1377374400, 1377379800, 'Live', 79, 'Alternative stage');
INSERT INTO "sets" VALUES (80, 1377379800, 1377385200, 'Live', 80, 'Alternative stage');
INSERT INTO "sets" VALUES (81, 1377385200, 1377390600, 'DJ', 81, 'Alternative stage');
INSERT INTO "sets" VALUES (82, 1377390600, 1377396000, 'DJ', 82, 'Alternative stage');
INSERT INTO "sets" VALUES (83, 1377396000, 1377403200, 'DJ', 83, 'Alternative stage');
INSERT INTO "sets" VALUES (84, 1377403200, 1377410400, 'DJ', 84, 'Alternative stage');
INSERT INTO "sets" VALUES (85, 1377410400, 1377419400, 'DJ', 85, 'Alternative stage');
INSERT INTO "sets" VALUES (86, 1377419400, 1377427500, 'Live', 86, 'Alternative stage');
INSERT INTO "sets" VALUES (87, 1377430200, 1377433800, 'GIG', 87, 'Alternative stage');
INSERT INTO "sets" VALUES (88, 1377435600, 1377440100, 'GIG', 88, 'Alternative stage');
