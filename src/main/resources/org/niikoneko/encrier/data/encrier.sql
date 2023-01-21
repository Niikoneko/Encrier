CREATE SEQUENCE hibernate_sequence AS BIGINT  START WITH 1 INCREMENT BY 1;

CREATE TABLE "type_projet" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "nom" VARCHAR(255) NOT NULL UNIQUE,
   "description" VARCHAR(1000)
);

CREATE TABLE "projet" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "type_id" BIGINT NOT NULL,
   "nom" VARCHAR(255) NOT NULL UNIQUE,
   "description" VARCHAR(1000),
   "archive" BOOLEAN
);

CREATE TABLE "chapitre" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "projet_id" BIGINT NOT NULL,
   "chapitre_type" VARCHAR(255) NOT NULL,
   "titre" VARCHAR(255),
   "ordre" INT
);

CREATE TABLE "projet_mots" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "projet_id" BIGINT NOT NULL,
   "entry_date" DATE NOT NULL,
   "nombre_mots" BIGINT NOT NULL,
   "temps_session" INTERVAL DAY(4) TO MINUTE
);

CREATE TABLE "projet_chapitres" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "projet_id" BIGINT NOT NULL,
   "chapitre_id" BIGINT NOT NULL,
   "finish_date" date NOT NULL,
   "nombre_mots" BIGINT NOT NULL
);