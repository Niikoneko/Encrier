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
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens org.niikoneko.encrier to javafx.fxml;
    opens org.niikoneko.encrier.formulaires to javafx.fxml;
    opens org.niikoneko.encrier.jpa to org.hibernate.orm.core;
    exports org.niikoneko.encrier;
}