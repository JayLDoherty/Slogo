package view.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import model.IndexedImage;
import view.components.Factory;
import view.visualization.Turtle;
import view.visualization.TurtleDisplay;

/**
 * @author Jay Doherty
 * This class creates a table of images from an observable list of image paths.
 */
public class ImageSelectorView extends BorderPane {

	private ObservableList<IndexedImage> data;
	private TurtleDisplay turtleDisplay;
	private Controller controller;
	
	/**
	 * 
	 */
	protected ImageSelectorView(Controller controller, TurtleDisplay turtleDisplay) {
		this.controller = controller;
		this.turtleDisplay = turtleDisplay;
		data = FXCollections.observableArrayList(new ArrayList<IndexedImage>());
		this.setupData();
		this.setup();
	}

	private void setup() {
		TableView<IndexedImage> table = new TableView<IndexedImage>();
		table.setPrefHeight(200);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(false);
		table.getStyleClass().add("panel-table");
		table.prefHeightProperty().bind(heightProperty());

		TableColumn<IndexedImage, Integer> indexColumn = new TableColumn<IndexedImage, Integer>("Index");
		indexColumn.setCellValueFactory(e -> e.getValue().indexProperty().asObject());

		TableColumn<IndexedImage, String> imageColumn = new TableColumn<IndexedImage, String>("Image");
		imageColumn.setCellValueFactory(e -> e.getValue().pathProperty());
		imageColumn.setCellFactory(column -> {
			return new TableCell<IndexedImage, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem("", empty);
					if (item != null) {
						this.setOnMouseClicked(e -> {
							TextInputDialog dialog = new TextInputDialog("1");
							dialog.setTitle("Choose Image Index");
							dialog.setHeaderText("Change Image by choosing an image index from the palette");
							dialog.setContentText("Index:");
							try {
								String result = dialog.showAndWait().get();
								Turtle turtle = turtleDisplay.getTurtleManager().getAllTurtles().get(indexColumn.getCellData(this.getIndex()));
								turtle.setShapeIndex(Integer.parseInt(result));
								turtle.setImage(controller.getImagePalette().get(Integer.parseInt(result) - 1).pathProperty().get());
							} catch (Exception ex) {
								
							}
						});
						Image img = new Image(getClass().getClassLoader().getResourceAsStream(item));
						this.setBackground(new Background(new BackgroundImage(img, null, null, null, null)));
						this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
						this.setPrefSize(img.getWidth(), img.getHeight());
					}
				}
			};
		});
		imageColumn.setOnEditCommit(new EventHandler<CellEditEvent<IndexedImage, String>>() {
			@Override
			public void handle(CellEditEvent<IndexedImage, String> event) {
				IndexedImage imageClicked = ((IndexedImage) event.getTableView().getItems().get(event.getTablePosition().getRow()));
				System.out.println(imageClicked.indexProperty().get());
			}
		});

		table.setItems(data);
		table.getColumns().add(indexColumn);
		table.getColumns().add(imageColumn);

		setCenter(table);
	}
	
	private void setupData() {
		initializeTurtleImages();
		listenForTurtleChanges();
	}
	
	private void initializeTurtleImages() {
		Map<Integer, Turtle> allTurtles = turtleDisplay.getTurtleManager().getAllTurtles();
		List<IndexedImage> images = controller.getImagePalette();
		for(Integer i : allTurtles.keySet()) {
			IndexedImage turtleImage = new IndexedImage(i, images.get(allTurtles.get(i).getShapeIndex().get() - 1).pathProperty().get());
			allTurtles.get(i).getShapeIndex().addListener((obs, oldVal, newVal) -> {
				turtleImage.pathProperty().set(images.get(newVal.intValue() - 1).pathProperty().get());
				this.setup();
			});
			data.add(turtleImage);
		}
	}
	
	private void listenForTurtleChanges() {
		turtleDisplay.getTurtleManager().getActiveTurtles().addListener((ListChangeListener<Turtle>) e -> {
			data.clear();
			this.initializeTurtleImages();
		});
	}
}
