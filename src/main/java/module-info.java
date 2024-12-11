module com.example.excel {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;

    // Экспортируем пакеты приложения
    opens com.example.excel to javafx.fxml;
    exports com.example.excel;

}