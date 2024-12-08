CREATE SEQUENCE hibernate_sequence AS BIGINT  START WITH 1 INCREMENT BY 1;

CREATE TABLE "type_projet" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "nom" VARCHAR(255) NOT NULL UNIQUE,
   "description" VARCHAR(1000)
);

CREATE TABLE "stage" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "nom" VARCHAR(255) NOT NULL UNIQUE,
   "type" VARCHAR(255) NOT NULL,
   "description" VARCHAR(1000)
);

CREATE TABLE "projet" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "type_id" BIGINT NOT NULL,
   "nom" VARCHAR(255) NOT NULL UNIQUE,
   "description" VARCHAR(1000),
   "stage_id" BIGINT
);

CREATE TABLE "stage_projet" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "projet_id" BIGINT NOT NULL,
   "id_stage" BIGINT NOT NULL,
   "ordre" INTEGER NOT NULL,
   "nom" VARCHAR(255) NOT NULL
);

CREATE TABLE "chapitre" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "projet_id" BIGINT NOT NULL,
   "chapitre_type" VARCHAR(255) NOT NULL,
   "titre" VARCHAR(255),
   "ordre" INTEGER,
   "notes" CLOB(2M)
);

CREATE TABLE "projet_mots" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "stage_id" BIGINT NOT NULL,
   "entry_date" DATE NOT NULL,
   "nombre_mots" BIGINT NOT NULL,
   "temps_session" INTERVAL DAY(4) TO MINUTE
);

CREATE TABLE "projet_chapitres" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "stage_id" BIGINT NOT NULL,
   "chapitre_id" BIGINT NOT NULL,
   "finish_date" date NOT NULL,
   "nombre_mots" BIGINT NOT NULL
);

CREATE TABLE "tracklist" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "stage_id" BIGINT,
   "chapitre_id" BIGINT,
   "cochable" BOOLEAN NOT NULL,
   "coche" BOOLEAN NOT NULL,
   "categorie" VARCHAR(128) NOT NULL,
   "description" VARCHAR(255) NOT NULL
);

CREATE TABLE "beta_lecteur" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "stage_id" BIGINT NOT NULL,
   "nom" VARCHAR(255) NOT NULL,
   "id_status" BIGINT NOT NULL
);

CREATE TABLE "bl_status" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "nom" VARCHAR(255) NOT NULL,
   "description" VARCHAR(1000)
);

CREATE TABLE "bl_question" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "stage_id" BIGINT NOT NULL,
   "question" VARCHAR(512) NOT NULL
);

CREATE TABLE "reponse_bl" (
   "id" BIGINT IDENTITY PRIMARY KEY,
   "id_bl" BIGINT NOT NULL,
   "id_question" BIGINT NOT NULL,
   "reponse" CLOB(2K)
);

CREATE TABLE "maison_edition" (
  "id" BIGINT IDENTITY PRIMARY KEY,
  "stage_id" BIGINT NOT NULL,
  "nom" VARCHAR(128) NOT NULL,
  "soumission_ouvertes" BOOLEAN NOT NULL,
  "coordonnées" VARCHAR(255) NOT NULL,
  "deadline" DATE,
  "id_status" BIGINT NOT NULL
);

CREATE TABLE "me_status" (
  "id" BIGINT IDENTITY PRIMARY KEY,
  "nom" VARCHAR(64) NOT NULL,
  "description" VARCHAR(1000)
);

CREATE TABLE "me_dossier" (
  "id" BIGINT IDENTITY PRIMARY KEY,
  "id_me" BIGINT NOT NULL,
  "type" VARCHAR(64) NOT NULL,
  "element" VARCHAR(255) NOT NULL,
  "coche" BOOLEAN NOT NULL
);
