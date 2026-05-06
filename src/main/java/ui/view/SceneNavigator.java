package ui.view;

import javafx.scene.Parent;

@FunctionalInterface
public interface SceneNavigator {
    void navigateTo(Parent root);
}
