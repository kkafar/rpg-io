package io.rpg.model.location;

import io.rpg.model.data.LocationModelStateChange;
import io.rpg.model.data.Position;
import io.rpg.model.object.GameObject;
import io.rpg.util.Result;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Represents single location in our game.
 */
public class LocationModel implements LocationModelStateChange.Emitter {
  private String tag;
  private List<GameObject> gameObjects;
  private final HashMap<GameObject, ChangeListener<Point2D>> positionListeners;
  private final HashMap<Position, GameObject> positionGameObjectMap;
  public final Point2D bounds;

  private final Set<LocationModelStateChange.Observer> locationModelStateChangeObservers;

  public LocationModel(@NotNull String tag, @NotNull List<GameObject> gameObjects) {
    this();
    this.tag = tag;
    this.gameObjects = gameObjects;

  }

  private LocationModel() {
    this.locationModelStateChangeObservers = new LinkedHashSet<>();
    this.positionListeners = new HashMap<>();
    this.positionGameObjectMap = new HashMap<>();
    bounds = new Point2D(9.5, 9.5); // TODO: 09.05.2022 Implement loading from config
  }

  public String getTag() {
    return tag;
  }

  public Optional<GameObject> getObject(Position position) {
    return Optional.ofNullable(positionGameObjectMap.get(position));
  }

  /**
   * Private setter for Builder usage only. Notice that ownership of {@link GameObject}s is not
   * transferred to LocationModel.
   * TODO: Transfer ownership of objects to LoactionModel.
   *
   * @param gameObjects game object for location
   */
  private void setGameObjects(List<GameObject> gameObjects) {
    this.gameObjects = gameObjects;
    gameObjects.forEach(this::registerGameObject);
    gameObjects.forEach(g -> checkAndCorrectBoundsCrossing(g, g.getExactPosition()));
    gameObjects.forEach(g -> positionGameObjectMap.put(g.getPosition(), g));
  }

  public void addGameObject(GameObject gameObject) {
    gameObjects.add(gameObject);
    positionGameObjectMap.put(gameObject.getPosition(), gameObject);
    registerGameObject(gameObject);
    checkAndCorrectBoundsCrossing(gameObject, gameObject.getExactPosition());
  }

  private void registerGameObject(GameObject gameObject) {
    ChangeListener<Point2D> positionListener =
        (observable, oldValue, newValue) -> onGameObjectPositionChange(gameObject, oldValue, newValue);
    gameObject.getExactPositionProperty()
              .addListener(positionListener);
    positionListeners.put(gameObject, positionListener);
  }

  public void removeGameObject(GameObject gameObject) {
    gameObjects.remove(gameObject);
    positionGameObjectMap.remove(gameObject.getPosition());
    unRegisterGameObject(gameObject);
  }

  private void unRegisterGameObject(GameObject gameObject) {
    ChangeListener<Point2D> positionListener = positionListeners.remove(gameObject);
    gameObject.getExactPositionProperty()
              .removeListener(positionListener);
  }

  private void onGameObjectPositionChange(GameObject gameObject, Point2D oldPosition, Point2D newPosition) {
    checkAndCorrectBoundsCrossing(gameObject, newPosition);

    Position newPos = new Position(newPosition);
    Position oldPos = new Position(oldPosition);
    if (newPos.equals(oldPos)) {
      return;
    }

    // Collision check
    if (positionGameObjectMap.containsKey(newPos) && !positionGameObjectMap.get(newPos)
                                                                           .equals(gameObject)) {
      gameObject.setExactPosition(oldPosition);
      return;
    }

    if (gameObject.equals(positionGameObjectMap.get(oldPos))) {
      changeField(gameObject, oldPos, newPos);
    }
  }

  private void changeField(GameObject gameObject, Position oldPos, Position newPos) {
    positionGameObjectMap.remove(oldPos);
    positionGameObjectMap.put(newPos, gameObject);
  }


  private void checkAndCorrectBoundsCrossing(GameObject gameObject, Point2D newPosition) {
    Point2D boundPosition = getBoundPosition(newPosition);
    if (boundPosition.equals(newPosition)) {
      return;
    }

    gameObject.setExactPosition(boundPosition);
    Point2D boundsCrossedDirection = newPosition.subtract(boundPosition)
                                                .normalize();

    emitBoundCrossedEvent(boundsCrossedDirection);
  }

  private void emitBoundCrossedEvent(Point2D boundsCrossedDirection) {
    // TODO: 10.05.2022 Bound crossed action
    System.out.println(boundsCrossedDirection);
  }

  private Point2D getBoundPosition(Point2D pos) {
    double x = Math.max(0, Math.min(bounds.getX(), pos.getX()));
    double y = Math.max(0, Math.min(bounds.getY(), pos.getY()));
    return new Point2D(x, y);
  }

  @Override
  public void addOnLocationModelStateChangeObserver(LocationModelStateChange.Observer observer) {
    locationModelStateChangeObservers.add(observer);
  }

  @Override
  public void removeOnLocationModelStateChangeObserver(LocationModelStateChange.Observer observer) {
    locationModelStateChangeObservers.remove(observer);
  }

  @Override
  public void emitLocationModelStateChange(LocationModelStateChange event) {
    locationModelStateChangeObservers.forEach(observer -> {
      observer.onLocationModelStateChange(event);
    });
  }


  public Result<Void, Void> validate() {
    if (tag == null || gameObjects == null) {
      return Result.error(null);
    }
    return Result.ok(null);
  }

  public static class Builder {
    private final LocationModel locationModel;

    private final Logger logger;

    public Builder() {
      logger = LogManager.getLogger(Builder.class);
      locationModel = new LocationModel();
    }

    public Builder setGameObjects(@NotNull List<GameObject> gameObjects) {
      locationModel.setGameObjects(gameObjects);
      return this;
    }

    public Builder setTag(@NotNull String tag) {
      locationModel.tag = tag;
      return this;
    }

    public LocationModel build() {
      Result<Void, Void> result = locationModel.validate();

      // TODO(@kkafar): Handle this in better way. Consider returning the result.
      if (result.isError()) {
        logger.error("Error occurred while building LocationModel.");
      }

      return locationModel;
    }
  }

}
