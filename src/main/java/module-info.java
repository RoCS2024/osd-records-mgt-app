module com.prefect.user.management.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires porms;
    requires ums;
    requires sims;
    requires java.sql;
    requires org.apache.logging.log4j.slf4j2.impl;

    opens com.prefect.user.management.app to javafx.fxml;
    opens com.prefect.user.management.app.controllers.main to javafx.fxml;
    opens com.prefect.user.management.app.controllers.dashboard to javafx.fxml;
    opens com.prefect.user.management.app.controllers.modal to javafx.fxml;
    opens com.prefect.user.management.app.controllers.search to javafx.fxml;

    exports com.prefect.user.management.app;
    exports com.prefect.user.management.app.controllers.main;
    exports com.prefect.user.management.app.controllers.dashboard;
    exports com.prefect.user.management.app.controllers.modal;
    exports com.prefect.user.management.app.controllers.search;

}