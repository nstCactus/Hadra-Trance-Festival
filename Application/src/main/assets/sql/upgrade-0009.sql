CREATE TABLE IF NOT EXISTS alarms(
    id INTEGER NOT NULL,
    set_id INTEGER NOT NULL,
    timestamp INTEGER NOT NULL,
    PRIMARY KEY(id)
);

DROP TABLE lst__stages;
CREATE TABLE lst__stages(
    stage VARCHAR(255) UNIQUE NOT NULL,
    location_id INT NOT NULL
);

INSERT INTO lst__stages VALUES("The temple", 1);
INSERT INTO lst__stages VALUES("The lotus", 2);
INSERT INTO lst__stages VALUES("The moon", 30);
