ALTER TABLE "projet" ADD CONSTRAINT FK_PROJET_ON_TYPE FOREIGN KEY ("type_id") REFERENCES "type_projet" ("id");

ALTER TABLE "chapitre" ADD CONSTRAINT FK_CHAPITRE_ON_PROJET FOREIGN KEY ("projet_id") REFERENCES "projet" ("id") ON DELETE CASCADE;

ALTER TABLE "projet_mots" ADD CONSTRAINT FK_PROJET_MOTS_ON_PROJET FOREIGN KEY ("projet_id") REFERENCES "projet" ("id") ON DELETE CASCADE;

ALTER TABLE "projet_chapitres" ADD CONSTRAINT FK_PROJET_CHAPITRES_ON_CHAPITRE FOREIGN KEY ("chapitre_id") REFERENCES "chapitre" ("id") ON DELETE CASCADE;

ALTER TABLE "projet_chapitres" ADD CONSTRAINT FK_PROJET_CHAPITRES_ON_PROJET FOREIGN KEY ("projet_id") REFERENCES "projet" ("id") ON DELETE CASCADE;