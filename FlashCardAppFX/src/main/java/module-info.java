module com.flashcardapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires org.apache.commons.codec;

    opens com.flashcardapp.gui to javafx.fxml;
    opens com.flashcardapp.util to com.fasterxml.jackson.databind, org.json, org.apache.commons.codec;

    exports com.flashcardapp;
}