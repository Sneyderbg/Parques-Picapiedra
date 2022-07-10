module appModule {
    requires java.base;
    requires java.logging;

    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires javafx.controls;

    opens app to javafx.fxml;

    exports app;
}