CREATE TABLE PLAYER
(
ID INTEGER,
CHAT_ID INTEGER,
NICKNAME VARCHAR(120),
TEAM INTEGER,
GOLD INTEGER
);

CREATE TABLE SHIP_TYPE
(
ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
NAME VARCHAR(120),
MEAN_SPEED INTEGER,
SPEED_DEVIATION INTEGER,
MEAN_POWER INTEGER,
POWER_DEVIATION INTEGER,
MEAN_TONNAGE INTEGER,
TONNAGE_DEVIATION INTEGER
);

CREATE TABLE SHIP
(
ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
OWNER_ID INTEGER,
NAME VARCHAR(120),
TYPE_ID INTEGER,
SPEED INTEGER,
POWER INTEGER,
TONNAGE INTEGER,
EMPLOYED INTEGER,
LOCATION INTEGER
);

CREATE TABLE PORT
(
ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
X INTEGER,
Y INTEGER,
NAME VARCHAR(120),
OWNER INTEGER
);

CREATE TABLE ROUTE
(
FROM_PORT INTEGER,
TO_PORT INTEGER,
DISTANCE INTEGER,
REWARD INTEGER
);
