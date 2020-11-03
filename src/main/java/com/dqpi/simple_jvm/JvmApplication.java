package com.dqpi.simple_jvm;


import com.dqpi.simple_jvm.vm.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author TheBIGMountain
 * @date 2020/11/1
 */
@EnableAsync
@SpringBootApplication
public class JvmApplication extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        launch(JvmApplication.class, MainView.class, args);
    }
    
    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        super.beforeInitialView(stage, ctx);
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
