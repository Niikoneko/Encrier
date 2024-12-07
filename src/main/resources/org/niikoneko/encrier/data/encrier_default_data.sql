-- Données par défaut pour les types de projet

INSERT INTO "type_projet" ("nom", "description") VALUES ('Roman', 'Un manuscrit de roman, organisé en chapitres.');
INSERT INTO "type_projet" ("nom", "description") VALUES ('Nouvelle', 'Un manuscrit court, sans forcement de chapitres.');
INSERT INTO "type_projet" ("nom", "description") VALUES ('Recueil de poèmes', 'Un recueil de poèmes où les chapitres sont plutôt des poèmes.');

-- Données par défaut pour les étapes de projet

INSERT INTO "stages" ("id", "nom", "type", "description") VALUES (1, 'Planification', 'Suivi', 'La planification du manuscrit');
INSERT INTO "stages" ("id", "nom", "type", "description") VALUES (2, 'Premier jet', 'Ecriture et suivi', 'L\'écriture du premier jet manuscrit');
INSERT INTO "stages" ("id", "nom", "type", "description") VALUES (3, 'Réécriture', 'Ecriture et suivi', 'La reprise corrective du manuscrit');
INSERT INTO "stages" ("id", "nom", "type", "description") VALUES (4, 'Bêta-lecture', 'Beta-lecture', 'Le suivi des bêta-lectures du manuscrit');
INSERT INTO "stages" ("id", "nom", "type", "description") VALUES (5, 'Envoi en maison d\'édition', 'Envoi ME', 'Le suivi des envois en maison d\'édition du manuscrit');
INSERT INTO "stages" ("id", "nom", "type", "description") VALUES (6, 'Terminé', 'Archive', 'Le statut final d\'un projet');

-- Données par défaut pour les statuts de Bêta-lecture

INSERT INTO "bl_status" ("nom", "description") VALUES ('En préparation', 'Préparation du dossier de BL avant envoi');
INSERT INTO "bl_status" ("nom", "description") VALUES ('Envoyé', 'Dossier envoyé et en attente de retour');
INSERT INTO "bl_status" ("nom", "description") VALUES ('Reçu', 'Retour de bêta-lecture reçu');

-- Données par défaut pour les status de Maison d'édition

INSERT INTO "me_status" ("nom", "description") VALUES ('En préparation', 'Préparation du dossier de ME avant envoi');
INSERT INTO "me_status" ("nom", "description") VALUES ('Prêt à envoyer', 'Dossier prêt pour envoi');
INSERT INTO "me_status" ("nom", "description") VALUES ('Envoyé', 'Dossier envoyé et en attente de retour');
INSERT INTO "me_status" ("nom", "description") VALUES ('Retour reçu', 'Retour de la maison reçu');
