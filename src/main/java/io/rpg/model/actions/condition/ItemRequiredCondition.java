package io.rpg.model.actions.condition;

import io.rpg.model.actions.ConditionType;
import org.jetbrains.annotations.NotNull;

public class ItemRequiredCondition extends Condition {

  @NotNull
  private final String requiredItemTag;

  public ItemRequiredCondition(@NotNull final String tag) {
    super(ConditionType.ITEM_REQUIRED);
    this.requiredItemTag = tag;
  }

  @Override
  public boolean acceptEngine(ConditionEngine engine) {
    return engine.evaluateItemRequiredCondition(this);
  }

  @NotNull
  public String getItemTag() {
    return requiredItemTag;
  }
}
