package io.rpg.util;

import io.rpg.config.model.GameObjectConfig;
import io.rpg.config.model.PlayerConfig;
import io.rpg.model.object.GameObject;
import io.rpg.model.object.Player;


import java.util.LinkedList;

/**
 * Exposes collection of methods to create {@link io.rpg.model.object.GameObject} class instances.
 */
public class GameObjectFactory {
  /**
   * Creates {@link GameObject} instance basing on information contained in config.
   *
   * @param config description of object properties
   * @return game object created based on information located in config
   */
  public static GameObject fromConfig(GameObjectConfig config) {
    if (config instanceof PlayerConfig) {
      return new Player(config.getTag(), config.getPosition(), config.getAssetPath());
    } else {
      GameObject gameObject = new GameObject(config.getTag(), config.getPosition());
      // TODO: Create ActionFactory & inflate the actions
      return gameObject;
    }
  }

  /**
   * Creates list of {@link GameObject} instances based on information contained in list of configs.
   * This method guarantees that {@link GameObject}s located in result list are in the same order,
   * i.e. first {@link GameObject} is created from first {@link GameObjectConfig}, second from second
   * etc.
   *
   * @param configs descriptions of object properties
   * @return game objects created based on information located in config list
   */
  public static LinkedList<GameObject> fromConfigs(Iterable<GameObjectConfig> configs) {
    LinkedList<GameObject> gameObjects = new LinkedList<>();
    for (GameObjectConfig config : configs) {
      gameObjects.add(fromConfig(config));
    }
    return gameObjects;
  }
}
