package io.rpg;

import io.rpg.gui.DisplayLayer;
import io.rpg.gui.LocationController;
import io.rpg.gui.model.LocationModel;
import io.rpg.model.*;
import io.rpg.model.GameObject;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    Image someMap = new Image("file:assets/map.png");
    Image someMap10x10 = new Image("file:assets/map10x10.png");
    Image someDude1 = new Image("file:assets/someDude.png");
    Image someDude2 = new Image("file:assets/someDudeButGreen.png");
    Image player = new Image("file:assets/stone.png");
    Game game=new Game();
    DisplayLayer displayLayer = new DisplayLayer(stage);
    LocationModel locationModel=displayLayer.showLocation();
    LocationController locationController=displayLayer.getLocationController();
    locationController.setGame(game);
    try{
      game.addGameObject(new GameObject(new Vector(0,0), someDude1));
      game.addGameObject(new GameObject(new Vector(0,5), someDude2));
      game.addGameObject(new GameObject(new Vector(5,5), someDude2));
      game.addGameObject(new Player(new Vector(7,7), player));
//      locationModel=displayLayer.showLocation();
      locationModel.setBackgroundImage(someMap);
      locationModel.setGame(game);
    }catch(Exception e){
        e.printStackTrace();
    }
    AnimationTimer animationTimer=new AnimationTimer() {
      long lastUpdate=-1;
      @Override
      public void handle(long now) {
        if(lastUpdate!=-1){
          float difference=(now-lastUpdate)/1e6f;
          game.update(difference);
          locationModel.update(difference);
        }
        lastUpdate=now;
      }

    };

    animationTimer.start();

//    DisplayLayer displayLayer = new DisplayLayer(stage);
//    displayLayer.showLocation()
//            .setBackgroundImage(someMap)
//            .addMapObject(new GameObject(new Vector(0,0), someDude1))
//            .addMapObject(new GameObject(new Vector(0,5), someDude1))
//            .addMapObject(new GameObject(new Vector(5,5), someDude2));

//    new Thread(() -> {
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      Platform.runLater(() ->{
//        displayLayer.showLocation()
//                .setBackgroundImage(someMap10x10)
//                .addMapObject(new GameObject(new Vector(0,0), someDude1))
//                .addMapObject(new GameObject(new Vector(0,2), someDude1))
//                .addMapObject(new GameObject(new Vector(3,5), someDude2));
//      });
//    }).start();

  }

  public static void main(String[] args) {
    launch();
  }
}