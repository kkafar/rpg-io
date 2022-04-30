package io.rpg.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class TextPopupViewModel {

  @FXML
  private Label label;
  @FXML
  private Pane background;

  public void setText(String text) {
    label.setText(text);
  }

  public void setBackgroundImage(String url) {
    BackgroundImage backgroundImg = new BackgroundImage(
        new Image(url),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT
    );
    background.setBackground(new Background(backgroundImg));
  }
}
