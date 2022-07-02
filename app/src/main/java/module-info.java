module ParchisModule {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens app to javafx.fxml;

    exports app;

    exports parchis;
}
