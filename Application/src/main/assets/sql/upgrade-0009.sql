CREATE TABLE IF NOT EXISTS alarms(
    id INTEGER NOT NULL,
    set_id INTEGER NOT NULL,
    timestamp INTEGER NOT NULL,
    PRIMARY KEY(id)
);