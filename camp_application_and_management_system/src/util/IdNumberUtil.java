/**
 * 
 */
package util;

import java.util.Map;
import model.camp.Camp;
import model.camp.Enquiry;
import model.camp.Suggestion;

/**
 * 
 */
public class IdNumberUtil {

	/**
	 * 
	 */	
	public static int findLowestAvailableCampId(Map<Integer, Camp> campData) {
        int lowestAvailable = 1; // Start with the first possible integer key

        while (campData.containsKey(lowestAvailable)) {
            lowestAvailable++; // Increment if the key is in use
        }

        return lowestAvailable;
    }
	
	public static int findLowestAvailableEnquiryId(Map<Integer, Enquiry> enquiryData) {
        int lowestAvailable = 1; // Start with the first possible integer key

        while (enquiryData.containsKey(lowestAvailable)) {
            lowestAvailable++; // Increment if the key is in use
        }

        return lowestAvailable;
    }
	
	public static int findLowestAvailableSuggestionId(Map<Integer, Suggestion> suggestionData) {
		int lowestAvailable = 1; // Start with the first possible integer key

        while (suggestionData.containsKey(lowestAvailable)) {
            lowestAvailable++; // Increment if the key is in use
        }

        return lowestAvailable;
	}

}