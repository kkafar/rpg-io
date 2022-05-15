package io.rpg.model.location;

import io.rpg.model.actions.Action;
import io.rpg.model.actions.ActionConsumer;
import io.rpg.model.data.Position;
import io.rpg.model.object.GameObject;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LocationModelTest {

  private LocationModel locationModel;

  @BeforeEach
  void setUp() {
    LocationModel.Builder builder = new LocationModel.Builder();
    locationModel = builder
        .setGameObjects(new ArrayList<>())
        .setTag("test")
        .build();
  }


  @Test
  void whenCallingSetActionConsumer_shouldSetActionConsumerForGameObjects() {
    GameObject object1 = new GameObject("obj1", new Position(1,1));
    GameObject object2 = new GameObject("obj2", new Position(2,1));

    locationModel.addGameObject(object1);
    locationModel.addGameObject(object2);

    ActionConsumer consumer = Mockito.mock(ActionConsumer.class);
    locationModel.setActionConsumer(consumer);

    Action action = Action.VOID;

    object1.setOnRightClickAction(action);
    object1.onRightClick();
    Mockito.verify(consumer, Mockito.times(1)).consumeAction(action);

    object2.setOnRightClickAction(action);
    object2.onRightClick();
    Mockito.verify(consumer, Mockito.times(2)).consumeAction(action);
  }
}