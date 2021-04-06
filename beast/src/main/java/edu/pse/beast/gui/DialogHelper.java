package edu.pse.beast.gui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class DialogHelper {
	public static Dialog<String> generateDialog(List<String> inputNames,
			List<Node> inputs) {
		Point position = MouseInfo.getPointerInfo().getLocation();
		Dialog<String> dialog = new Dialog<String>();
		dialog.setX(position.getX());
		dialog.setY(position.getY());
		ButtonType buttonType = new ButtonType("OK", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(buttonType,
				ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		for (int i = 0; i < inputNames.size(); ++i) {
			grid.add(new Label(inputNames.get(i)), 0, i);
			grid.add(inputs.get(i), 1, i);
		}

		dialog.getDialogPane().setContent(grid);
		return dialog;
	}
}