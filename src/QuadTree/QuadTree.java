package QuadTree;

import java.util.*;

import postalCode.PostalCode;
import geodetic.*;

/** Basic binary search tree that supports insertion and three traversal patterns. */
public class QuadTree {
	/** Fundamental root QuadNode of the entire QuadTree. */
	private QuadNode root;
	/** Defines threshold when a QuadNode is deemed to be full, and any further additions, force a split. */
	private int maxItemsBeforeSplit;
	/** Tracks the number of PostalCode objects being stored. */
	private int size;
	/**
	 * copy of the QuadNode root used to reset the root in findClosest method
	 */
	private QuadNode rootCopy;
	/**
	 * Contains the Array of closest PostalCode
	 */
	private ArrayList<PostalCode> pcArray = null;
	/**
	 * QuadTree size accessor
	 * @return size of the QuadTree
	 */
	public int size(){return size;}

	
	/**
	 * QuadTree is used to support the organization of PostalCode data for high-performance searching. Looking for PostalCode nearest to an arbitrary latitude/longitude value. 
	 * @param coordTopLeft Defines top-left corner of the rectangular area that identifies the root QuadNode (which is the full geographic boundaries of the entire search space). 
	 * @param coordBottomRight Defines bottom-right corner of the rectangular area that identifies the root QuadNode (which is the full geographic boundaries of the entire search space).
	 * @param maxItemsBeforeSplit Each QuadNode can hold up to maxItemsBeforeSplit PostalCode objects before splitting into an additional set of four QuadNode children (ne,se, nw, sw)
	 */
	public QuadTree(Coordinate coordTopLeft, Coordinate coordBottomRight, int maxItemsBeforeSplit) {
		this.maxItemsBeforeSplit = maxItemsBeforeSplit;
		root = new QuadNode(coordTopLeft, coordBottomRight);
		rootCopy = root;
	}
	
	/**
	 * A given Coordinate will be searched in the QuadTree.
	 * @param targetCoord
	 * @return Returns a reference-to the closest matching PostalCode.
	 */
	public ArrayList<PostalCode> findClosest(Coordinate targetCoord) {
		double closeDistance=Double.MAX_VALUE;
		double calcDistance;
		
		if(root.containsCoordinate(targetCoord)){
			if (root.ne!=null && root.ne.containsCoordinate(targetCoord)){
			   root=root.ne;
			   findClosest(targetCoord);
			}
			else if(root.se!=null && root.se.containsCoordinate(targetCoord)){
				root=root.se;
				findClosest(targetCoord);
			}
			else if(root.nw!=null && root.nw.containsCoordinate(targetCoord)){
				root=root.nw;
				findClosest(targetCoord);
			}
			else if(root.sw!=null && root.sw.containsCoordinate(targetCoord)){
				root=root.sw;
				findClosest(targetCoord);
			}
			else{
				for(Coordinate coord : root.getMap().keySet()){
					
					calcDistance= coord.distanceTo(targetCoord);
					
					if(calcDistance < closeDistance){
						closeDistance = calcDistance;
						pcArray = root.getMap().get(coord);
					}
				} 
				root=rootCopy;//reset root
			}
		}
		
		return pcArray;
	}
	
	/**
	 * Adds a new PostalCode object to the QuadTree. The QuadTree will be placed in the target left node. If the leaf node is full, that node will split and redistribute its contents into the four child nodes (NE SE SW NW).
	 * @param postalCode
	 * @return Success, return null. Non-null represents PostalCode that is out-of-bounds with respect to the QuadTree predefined spatial region.
	 */
	public boolean add(PostalCode postalCode) {
		if ( ! root.containsCoordinate(postalCode.getCoordinate())) // check for out-of-bounds coordinate value for QuadTree
			return false;
		root.add(postalCode);
		++size;
		return true; // success ... PostalCode object was not deemed to be out-of-bounds
	} // end add(PostalCode postalCode)
	
  // Beautiful illustration of the PRE-ORDER RECURSIVE ALGORITHM
	public void displayPreOrder() { System.out.print("\nPre-Order Traversal: "); displayPreOrder(root); }
	private void displayPreOrder(QuadNode node) {
		if (node == null)
			return;
		System.out.print(node); // The ESSENCE of PRE-ORDER: Act on the node BEFORE embarking on any NW NE SW SE branch processing.
		displayPreOrder(node.nw);
		displayPreOrder(node.ne);
		displayPreOrder(node.sw);
		displayPreOrder(node.se);
	} // end displayPreOrder()
	
  // Beautiful illustration of the POST-ORDER RECURSIVE ALGORITHM
	public void displayPostOrder() { System.out.print("\nPost-Order Traversal: "); displayPostOrder(root); }
	private void displayPostOrder(QuadNode node) {
		if (node == null)
			return;
		displayPostOrder(node.nw);
		displayPostOrder(node.ne);
		displayPostOrder(node.sw);
		displayPostOrder(node.se);
		System.out.print(node); // The ESSENCE of POST-ORDER: Act on the node AFTER finishing all NW NE SW SE branch processing.
	} // end displayPostOrder()
	public ArrayList<PostalCode> getPostalCodeArray(){
		return pcArray;
	}
	@Override
	public String toString() {
		return "QuadTree: size:" + size;
	}
	
	// ***************************************************************************
	// ***************************************************************************
	// Start inner class Node
	// Inner class to support the storage of items to be added to the tree, in this case, reference-to values for PostalCode objects 
	private class QuadNode {
		// coordTopLeft and coordBottomRight define the spatial boundaries of this QuadNode.
		private Coordinate coordTopLeft;
		private Coordinate coordBottomRight;

		private Map<Coordinate,ArrayList<PostalCode>> postalCodeMap; // The use of "private" is technically NOT required, because they are specified in a "private" class.
		private QuadNode nw;
		private QuadNode ne;
		private QuadNode sw;
		private QuadNode se;

		private QuadNode(Coordinate coordTopLeft, Coordinate coordBottomRight) {
			this.coordTopLeft = coordTopLeft; this.coordBottomRight = coordBottomRight;
			postalCodeMap = new HashMap<Coordinate,ArrayList<PostalCode>>(maxItemsBeforeSplit);
		}

		private QuadNode(PostalCode postalCode) {
			postalCodeMap = new HashMap<Coordinate,ArrayList<PostalCode>>(maxItemsBeforeSplit);
			if( postalCodeMap.get(postalCode.getCoordinate())==null){ //if the value is null
				postalCodeMap.put(postalCode.getCoordinate(),new ArrayList<>());//create a new LinkedHashSet
				postalCodeMap.get(postalCode.getCoordinate()).add(postalCode);//put the key and LinkedHashSet in the Map
			}else{
				 postalCodeMap.get(postalCode.getCoordinate()).add(postalCode);//else add the paragraph to the existing LinkedHashSet
			}
			++size; // instance field of the outer class BinarySearchTree. It is accessible because this class is "non-static." 
		}

		/**
		 * Method attempts to add the PostalCode object to this QuadNode. There are 3 possible cases:
		 * 1. This QuadNode is already an INTERNAL node, the PostalCode CANNOT be stored here. Suggests recursive calls (though this is handled through the call to addToCorrectQuadrant()
		 * 2a. LEAF node, and it has room for additional PostalCode objects.  
		 * 2b. LEAF node, and it DOES NOT have room for additional PostalCode objects. Must convert this LEAF node to an INTERNAL node.
		 * @param postalCode Reference-to value for the PostalCode object being added to the QuadTree
		 * @return The true/false value is an indicator of success (or failure) in adding the PostalCode object.
		 */
		public boolean add(PostalCode postalCode) {
			// case 1: This QuadNode is already an INTERNAL node, I CANNOT store the PostalCode here. Suggests recursive calls (though this is handled through the call to addToCorrectQuadrant()
			if (postalCodeMap == null) { // if true, INTERNAL node which does not accept PostalCode objects here.
				// recursively add to a child QuadNode
				// All four child QuadNodes MUST already exist (nw, ne, sw, se)
				// In which QuadNode should the new PostalCode object reside, 
				return addToCorrectQuadrant(postalCode);
			} else { // LEAF node because already have an existing Collection of PostalCode objects
				// case 2a: LEAF node, and it has room for additional PostalCode objects. EASY ONE.
				if (postalCodeMap.size() < maxItemsBeforeSplit) {
					if( postalCodeMap.get(postalCode.getCoordinate())==null){ //if the value is null
						postalCodeMap.put(postalCode.getCoordinate(),new ArrayList<>());//create a new LinkedHashSet
						postalCodeMap.get(postalCode.getCoordinate()).add(postalCode);//put the key and LinkedHashSet in the Map
					}else{
						 postalCodeMap.get(postalCode.getCoordinate()).add(postalCode);//else add the paragraph to the existing LinkedHashSet
					}
					++size; // instance field of the outer class BinarySearchTree. It is accessible because this class is "non-static."
					return true;
				} else {
					// case 2b: LEAF node, and it DOES NOT have room for additional PostalCode objects. Must convert this LEAF node to an INTERNAL node. 
					// The coordCenter will be used to define selected corners of each of the four new QuadNodes (ne, se, sw, nw) to be created here.
					Coordinate coordCenter = new Coordinate( (coordTopLeft.getLatitude()+coordBottomRight.getLatitude())/2.0, (coordTopLeft.getLongitude()+coordBottomRight.getLongitude())/2.0 );

					ne = new QuadNode(new Coordinate(coordTopLeft.getLatitude(), coordCenter.getLongitude()), new Coordinate(coordCenter.getLatitude(), coordBottomRight.getLongitude()));
					se = new QuadNode(coordCenter, coordBottomRight);
					sw = new QuadNode(new Coordinate(coordCenter.getLatitude(), coordTopLeft.getLongitude()), new Coordinate(coordBottomRight.getLatitude(), coordCenter.getLongitude()));
					nw = new QuadNode(coordTopLeft, coordCenter);
					for (Coordinate coordinate : postalCodeMap.keySet()) {
						// The following call will distribute the existingPostalCode objects into the correct child QuadNode objects (ne, se, sw, nw).
						for(PostalCode postalCode2 : postalCodeMap.get(coordinate)){
					    addToCorrectQuadrant(postalCode2); // no need to return from these calls, since the existingPostalCode MUST already be within bounds.
						}
					}
					postalCodeMap = null;
					return addToCorrectQuadrant(postalCode);
				}
			}
		}
		
		private boolean addToCorrectQuadrant(PostalCode postalCode) {
			if (ne.containsCoordinate(postalCode.getCoordinate())) 
				return ne.add(postalCode);
			else if (nw.containsCoordinate(postalCode.getCoordinate()))
				return nw.add(postalCode);
			else if (se.containsCoordinate(postalCode.getCoordinate()))
				return se.add(postalCode);
			else if (sw.containsCoordinate(postalCode.getCoordinate()))
				return sw.add(postalCode);
			return false; // if no matches in the if-else-if structure, then the Coordinate is NOT within any of the four child QuadNode objects.
		} // end addToCorrectQuadrant()
		
		/** 
		 * Check if a given Coordinate would reside in this QuadNode.
		 * @param candidateCoordinate Checking if the Coordinate value would reside in this QuadNode.
		 */
		private boolean containsCoordinate(Coordinate candidateCoordinate) {
			if (candidateCoordinate.getLatitude() <= coordTopLeft.getLatitude() && candidateCoordinate.getLatitude() >= coordBottomRight.getLatitude() && 
					candidateCoordinate.getLongitude() >= coordTopLeft.getLongitude() && candidateCoordinate.getLongitude() <= coordBottomRight.getLongitude())
				return true;
			else
				return false;
		} // ends containsCoordinate()
		
		public HashMap<Coordinate,ArrayList<PostalCode>> getMap(){
			return (HashMap<Coordinate,ArrayList<PostalCode>>) postalCodeMap;
		}

		@Override
		public String toString() {
			return postalCodeMap.toString(); // TODO change this method.
		}
	} // end inner class QuadNode
	// End inner class QuadNode
	//***************************************************************************
	//***************************************************************************
} // end class QuadTree