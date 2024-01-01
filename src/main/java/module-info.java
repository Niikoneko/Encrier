module org.niikoneko.encrier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires org.hsqldb;
    requires java.sql;
    requires java.desktop;

    opens org.niikoneko.encrier to javafx.fxml;
    opens org.niikoneko.encrier.userInterface.formulaires to javafx.fxml;
    exports org.niikoneko.encrier;
    exports org.niikoneko.encrier.userInterface;
    opens org.niikoneko.encrier.userInterface to javafx.fxml;
}