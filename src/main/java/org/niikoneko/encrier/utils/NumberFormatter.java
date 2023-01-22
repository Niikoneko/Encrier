package org.niikoneko.encrier.utils;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * Création d'un TextFormater pour les champs numériques
 */
public class NumberFormatter extends TextFormatter<String> {

    public NumberFormatter() {
        super(new UnaryOperator<Change>() {
            @Override
            public Change apply(Change change) {
                change.setText(change.getText().replaceAll("\\D", ""));
                return change;
            }
        });
    }
}
