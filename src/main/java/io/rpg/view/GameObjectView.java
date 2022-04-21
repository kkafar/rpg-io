package io.rpg.view;

import io.rpg.config.model.GameObjectConfig;
import io.rpg.model.data.GameObjectStateChange;
import io.rpg.model.data.MouseClickedEvent;
import io.rpg.model.data.Position;
import io.rpg.viewmodel.GameObjectViewModel;
import io.rpg.viewmodel.LocationViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class GameObjectView extends ImageView
    implements MouseClickedEvent.Emitter, GameObjectStateChange.Observer {
  private Path path;
  private final Set<MouseClickedEvent.Observer> onClickedObservers;

  public GameObjectView(@NotNull Path assetPath, @NotNull Position position) {
    this.path = assetPath;
    this.setImage(new Image(path.toString()));
    // todo: better position class
    this.setX(position.col);
    this.setY(position.row);
    this.onClickedObservers = new HashSet<>();
    this.setOnMouseClicked(event -> emitOnMouseClickedEvent(new MouseClickedEvent(this, event)));
  }

  @Override
  public void emitOnMouseClickedEvent(MouseClickedEvent event) {
    onClickedObservers.forEach(listener -> listener.onMouseClickedEvent(event));
  }

  @Override
  public void addOnClickedObserver(MouseClickedEvent.Observer observer) {
    onClickedObservers.add(observer);
  }

  @Override
  public void removeOnClickedObserver(MouseClickedEvent.Observer observer) {
    onClickedObservers.remove(observer);
  }

  @Override
  public void onGameObjectStateChange(GameObjectStateChange event) {
    // TODO: implement update logic here or create view model class but it
    // is even more boilerplate
  }
}
