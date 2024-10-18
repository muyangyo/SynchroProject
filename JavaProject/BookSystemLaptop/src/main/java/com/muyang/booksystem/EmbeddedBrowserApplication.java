package com.muyang.booksystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EmbeddedBrowserApplication extends Application {

    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(EmbeddedBrowserApplication.class, args);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        webView.getEngine().load("http://localhost:8080"); // 加载Spring Boot应用的页面

        Scene scene = new Scene(webView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Embedded Browser");
        primaryStage.setOnCloseRequest(event -> {
            applicationContext.close(); // 当浏览器关闭时，关闭Spring Boot应用
        });
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close(); // 确保Spring Boot应用在JavaFX应用关闭时也关闭
    }
}