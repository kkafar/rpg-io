package io.rpg;

import io.rpg.util.Result;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;

public class Main extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    Configurator.setRootLevel(Level.DEBUG);
    Logger logger = LogManager.getLogger(Main.class);

    Initializer worldInitializer = new Initializer("configurations/demo-config-1", stage);
    Result<Game, Exception>  initializationResult = worldInitializer.initialize();

    if (initializationResult.isError()) {
      logger.error("Initialization error");

      initializationResult.getErrorValueOpt().ifPresentOrElse(
          ex -> {
            logger.error(ex.getMessage());
            ex.printStackTrace();
          },
          () -> logger.error("No reason provided")
      );
      return;
    } else if (initializationResult.isOkValueNull()) {
      logger.error("Initialization returned null value");
      return;
    }

    Game game = initializationResult.getOkValue();

    stage.setScene(game.getWorldView());

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
