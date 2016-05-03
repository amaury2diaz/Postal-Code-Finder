package postalCode;

import geodetic.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


import java.util.ArrayList;

import QuadTree.QuadTree;
import javafx.collections.*;

/*	File Name: PostalCodeIndex Class
 *	Course Name: Data Structures CST 8130
 *	Lab	Section:CST 8130-301
 *	Student	Name: Amaury Diaz Diaz
 *	Date: Apr-14-2015
 */
/*
 * References:(Rex Woollard (2015) personal communication)
 */
/**
 * This class is the core of the whole program. It reads the Postal Code
 * data from a file and then it adds it to a QuadTree. 
 */
public class PostalCodeIndex {
	/**
	 * A QuadTree object where the PostalCode objects are going to be stored
	 */
	private QuadTree postalCodeQuadTree;
	/**
	 * 
	 */
	private ArrayList<PostalCode> closestPostalCodes;
	

    /**
	 * This constructor reads the data from the file and adds it to a QuadTree ofPostalCode
	 * objects.
	 */
	public PostalCodeIndex() {
		File file = new File("postal_codes.csv");
		postalCodeQuadTree = new QuadTree(new Coordinate(80.0,-142.0),new Coordinate(41.5,-52.0),290);//initialize the QuadTree
		try (BufferedReader input = Files.newBufferedReader(file.toPath())) {
			String inputLine = input.readLine();
			while ((inputLine = input.readLine()) != null) {// watch for the end of file looking for null											
				PostalCode postalCode = new PostalCode(inputLine);
				postalCodeQuadTree.add(postalCode);
			}//end of while

		} catch (IOException e) {
			e.printStackTrace();
		}//end of catch
	
	}//end of PostalCodeIndex() constructor
    
	/**
	 * Accessor of a QuadTree object
	 * @return QuadTree object
	 */
	public QuadTree getPostalCodeQuadTree(){
		return postalCodeQuadTree;
	}
	
	/**
	 * 
	 * @param targetCoord The Coordinate to find
	 * @return An observableList of the ArrayList returned
	 */
	public ObservableList<PostalCode> findClosest(Coordinate targetCoord){
		closestPostalCodes=postalCodeQuadTree.findClosest(targetCoord);
		if(closestPostalCodes != null){
		return FXCollections.observableList(closestPostalCodes);
		}
		else return null;
	}

}//end of PostalCodeIndex()
