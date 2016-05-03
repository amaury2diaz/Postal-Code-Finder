package fxlauncher;

import geodetic.Coordinate;
import postalCode.PostalCode;
import postalCode.PostalCodeIndex;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/*	File Name: FXLauncher Class
 *	Course Name: Data Structures CST 8130
 *	Lab	Section:CST 8130-301
 *	Student	Name: Amaury Diaz Diaz
 *	Date: Apr-14-2015
 */
public class FXLauncher extends Application {
	/**
	 * PostalCodeIndex object
	 */
	private PostalCodeIndex postalCodeIndex;
	/**
	 * Listview of PostalCode objects
	 */
	private ListView<PostalCode> listClosestPostalCodes;
	/**
	 * VBox object used for scene representation
	 */
	private VBox motherVBox;
	/**
	 * VBox object used for scene representation
	 */
	private VBox childVBox;
	/**
	 * HBox object used for scene representation
	 */
	private HBox fatherHBox;
	/**
	 * Scene object used to show the javafx app
	 */
	private Scene scene;
	/**
	 * Button object used to search the closest PostalCodes
	 */
	private Button searchCoordinate;
	/**
	 * TextField object used to allow the user to enter a latitude
	 */
	private TextField latitude;
	/**
	 * TextField object used to allow the user to enter a longitude
	 */
	private TextField longitude;
	/**
	 * Label object used to display that there is no data to show at the beginning of execution
	 */
	private Label noData;
	/**
	 * Label object used to display that anything else than a number was entered by the user
	 */
	private Label invalidInput;
	
	private Label outOfBoundaries;
	
	public static void main(String[] args) {launch(args);}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		// Initialize the HBox and VBoxes as well as the postalCodeIndex object
		motherVBox = new VBox();
		childVBox = new VBox();
		fatherHBox = new HBox();  
		postalCodeIndex = new PostalCodeIndex();
		
		primaryStage.setTitle("Postal Code Finder");//tittle of the app
		
		//initialize the TextFields
		latitude = new TextField();latitude.setPromptText("Enter Latitude value");
	    longitude = new TextField();longitude.setPromptText("Enter Longitude value");
	    
		
		searchCoordinate = new Button("Search");//initialize the button 
		searchCoordinate.setDefaultButton(true);// allow the user to press enter since it is the only button in the app
			
		
		searchCoordinate.setOnAction(event->{//when the button is pressed
			try{
				double latitudeDouble = Double.parseDouble(latitude.getText());//parse the text enterd by the user to be a double
				double longitudeDouble = Double.parseDouble(longitude.getText());//parse the text enterd by the user to be a double
				
				//get a listView with the closest PostalCode
				if(postalCodeIndex.findClosest(new Coordinate(latitudeDouble,longitudeDouble)) != null){
				    listClosestPostalCodes = new ListView<>(postalCodeIndex.findClosest(new Coordinate(latitudeDouble,longitudeDouble)));
				    childVBox.getChildren().setAll(listClosestPostalCodes);//set the children to be the ListView
				}
				else{
					outOfBoundaries = new Label("Out of boundaries");
					outOfBoundaries.setFont(new Font("Arial",26));
					outOfBoundaries.setTextFill(Color.RED);
					childVBox.getChildren().setAll(outOfBoundaries);
				}
				
			}
			catch (NumberFormatException e) { // user must have entered non-numeric characters . . . give message . . . then ignore invalid entry.
				invalidInput = new Label("Invalid Input");
				invalidInput.setFont(new Font("Arial",26));
				invalidInput.setTextFill(Color.RED);
				childVBox.getChildren().setAll(invalidInput);
				latitude.setText("Invalid input");
				longitude.setText("Invalid input");
			}
			
			
		});
		
		fatherHBox = new HBox(latitude,longitude,searchCoordinate);
		//no Data yet to retrieve
		noData = new Label("No data to display");
		noData.setFont(new Font("Arial",26));
		noData.setTextFill(Color.RED);
		childVBox = new VBox(noData);
		childVBox.setAlignment(Pos.CENTER);
		
		motherVBox.getChildren().addAll(fatherHBox,childVBox);
		scene = new Scene(motherVBox,400,375);
		scene.getStylesheets().add("/view/StyleSheet.css");//inport the css file
		primaryStage.getIcons().add(new Image("http://www.keenada.com/students/diaz0064/lab1/img/26356.png"));//set a new icon for the app
		primaryStage.setScene(scene);
		//have a maximum size for the Stage
		primaryStage.setMaxHeight(400);
		primaryStage.setMaxWidth(375);
		primaryStage.show();
		
	}
	

}
