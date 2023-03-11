package stijn.dev.resource.controllers.interfaces;

import javafx.scene.*;
import javafx.scene.input.*;

public interface IHasNextButton {

    void onNext();

    default void setKeyBehavior(Scene scene){
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode()== KeyCode.ENTER){
                onNext();
            }
        });
    }
}
