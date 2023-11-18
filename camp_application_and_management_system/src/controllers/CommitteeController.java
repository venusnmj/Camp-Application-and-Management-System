/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.Map;

import interfaces.ICampView;
import interfaces.IEnquiryCommitteeService;
import interfaces.IEnquiryView;
import interfaces.IReportGeneratorService;
import interfaces.ISuggestionCommitteeService;
import interfaces.ISuggestionView;
import model.camp.Camp;
import model.camp.Enquiry;
import model.camp.Suggestion;
import model.user.Committee;
import services.EnquiryCommitteeService;
import services.ReportGeneratorService;
import services.SuggestionCommitteeService;
import stores.DataStore;
import util.SelectorUtil;
import util.TextDecoratorUtil;
import view.AllCampDetailsView;
import view.CommonView;
import view.EnquiryView;
import view.SuggestionView;

/**
 * 
 */
public class CommitteeController extends StudentController {

	private Camp camp = null;
	
	private static final IEnquiryCommitteeService enquiryCommitteeService = new EnquiryCommitteeService();
	
	private static final ISuggestionCommitteeService suggestionCommitteeService = new SuggestionCommitteeService();
	
	private static final IReportGeneratorService reportGeneratorService = new ReportGeneratorService();
	
	/**
	 * 
	 */
	public CommitteeController() {}
	
	public void start(Committee committee, Camp camp) {
		
		ICampView campView;
		IEnquiryView enquiryView;
		ISuggestionView suggestionView;
		int choice, choice2;
		boolean back;
		
		this.camp = camp;
		
		do {
			CommonView.printNavbar("CAMS > Committee");
			System.out.printf("Current points for %s : %d", committee.getName(), committee.getPoint());
			
			System.out.println(TextDecoratorUtil.underlineText("Camp details for " + camp.getName()));
			System.out.println("1. View camp details");
			
			System.out.println(TextDecoratorUtil.underlineText("\nEnquiries and Suggestion"));
			System.out.println("2. View enquiries");
			System.out.println("3. Reply to enquiries");
			System.out.println("4. View suggestions");
			System.out.println("5. Submit/edit/delete suggestions");
			
			System.out.println(TextDecoratorUtil.underlineText("\nReports"));
			System.out.println("6. Generate camp report");
			
			System.out.println("0. Return to student view");
			
			choice = sc.nextInt();
			
			switch(choice) {
				case 1:
					CommonView.printNavbar("CAMS > Committee > View camp details");
					campView = new AllCampDetailsView();
					campView.displayCamp(camp);
					break;
				case 2:
					CommonView.printNavbar("CAMS > Committee > View enquires");
					enquiryView = new EnquiryView();
					viewEnquiries(enquiryView);
					break;
				case 3:
					CommonView.printNavbar("CAMS > Committee > Reply to enquires");
					enquiryView = new EnquiryView();
					replyToEnquiries(enquiryView);
					break;
				case 4:
					CommonView.printNavbar("CAMS > Committee > View suggestions");
					suggestionView = new SuggestionView();
					viewSuggestions(suggestionView);
					break;
				case 5:
					CommonView.printNavbar("CAMS > Committee > Submit/edit/delete suggestions");
					back = false;
	                do {
	                	System.out.println("1. Submit suggestion");
	                	System.out.println("2. Edit suggestions");
	                	System.out.println("3. Delete suggestions");
	                	System.out.println("4. Return to homepage");
	                	choice2 = sc.nextInt();
	                	switch(choice2) {
	                	case 1:
	                		CommonView.printNavbar("CAMS > Student > Submit/Edit/Delete enquiries > Submit enquiries");
	                		submitSuggestion();
	                		break;
	                	case 2:
	                		CommonView.printNavbar("CAMS > Student > Submit/Edit/Delete enquiries > Edit enquiries");
	                		editSuggestion();
	                		break;
	                	case 3:
	                		CommonView.printNavbar("CAMS > Student > Submit/Edit/Delete enquiries > Delete enquiries");
	                		deleteSuggestion();
	                		break;
	                	case 4:
	                		back = true;
	                		break;
	                	}
	                } while (back == false);
	                break;
				case 6:
					CommonView.printNavbar("CAMS > Committee > Generate reports");
					generateReport();
					break;
				case 0:
					System.out.println("Exiting committee menu...");
					return;
				default:
					System.out.println("Invalid choice.");
					break;
			}
			if (choice >= 1 && choice <= 4) {
				CommonView.pressEnterToContinue();
			}
		} while (true);
	}
	
	private void viewEnquiries(IEnquiryView enquiryView) {
		ArrayList<Enquiry> enquiries = enquiryCommitteeService.viewEnquiries(camp);
		
		if (enquiries.isEmpty()) {
			System.out.println("There are no enquiries.");
		}
		else {
			for (Enquiry enquiry : enquiries) {
				enquiryView.displayEnquiries(enquiry);
				System.out.println();
			}
		}
	}
	
	private void replyToEnquiries(IEnquiryView enquiryView) {
		ArrayList<Enquiry> enquiries = enquiryCommitteeService.viewEnquiries(camp);
		Enquiry selectedEnquiry = SelectorUtil.enquirySelector(enquiries);
		
		System.out.println("The selected enquiry is: ");
		enquiryView.displayEnquiries(selectedEnquiry);
		System.out.print("Enter your reply to the enquiry: ");
		String reply = sc.nextLine();
		
		boolean success = enquiryCommitteeService.replyToEnquiry(selectedEnquiry, reply);
		
		if (success) {
			System.out.println("Succesfully replied to enquiry!");
		}
		else {
			System.out.println("Reply to enquiry unsuccessful.");
		}
	}
	
	private void viewSuggestions(ISuggestionView suggestionView) {
		ArrayList<Suggestion> suggestions = suggestionCommitteeService.viewAllSuggestions();
		
		if (suggestions.isEmpty()) {
			System.out.println("You have not made any enquiries.");
		}
		else {
			for (Suggestion suggestion : suggestions) {
				suggestionView.displaySuggestions(suggestion);
				System.out.println();
			}
		}
	}
	
	private void submitSuggestion() {		
		boolean success = suggestionCommitteeService.submitSuggestions(camp);
		
		if (success) {
			System.out.println("Enquiry submitted successfully.\n"
					+ "Please be patients while we get back to you.");
		}
		else {
			System.out.println("Enquiry not submitted");
		}
	}
	
	private void editSuggestion() {
		ArrayList<Suggestion> suggestions = suggestionCommitteeService.viewProcessingSuggestions();
		Suggestion selectedSuggestion = SelectorUtil.suggestionSelector(suggestions);
		
		if (selectedSuggestion != null) {
			System.out.println("Current question: " + selectedSuggestion.getQuestion());
			System.out.println("Enter edited question");
			String newQuestion = sc.nextLine();
			
			boolean success = suggestionCommitteeService.editSuggestion(selectedSuggestion, newQuestion);
			
			if (success) {
				System.out.println("Enquiry edited successfully.");
			}
			else {
				System.out.println("Enquiry not edited.");
			}
		}
	}
	
	private void deleteSuggestion() {
		Map<Integer, Camp> campData = DataStore.getCampData();
		
		ArrayList<Suggestion> suggestions = suggestionCommitteeService.viewProcessingSuggestions();
		Suggestion selectedSuggestion = SelectorUtil.suggestionSelector(suggestions);
		String input = null;
		
		if (selectedSuggestion != null) {
	    	do {
	    		System.out.printf("Deleting suggestion for camp %d - %s\n", campData.get(selectedSuggestion.getCampID()).getName(), selectedSuggestion.getQuestion());
		    	System.out.println("Please confirm the option. Do note that deleted camps will be deleted permanently. (Y/N)");
		    	input = sc.next();
		    	if (input.equals("Y") || input.equals("y")) {
		    		suggestionCommitteeService.deleteSuggestion(selectedSuggestion);
		    		System.out.println("Enquiry deleted successfully.");
		    		break;
		    	}
		    	else if (input.equals("N") || input.equals("n")) {
		    		break;
		    	}
		    	else {
		    		System.out.println("Invalid input. Please input Y or N.");
		    	}
	    	} while (true);
	    }
	}
	
	private void generateReport() {
		System.out.println("Please select filters for report:");
		System.out.println("1. Attendees only");
		System.out.println("2. Camp committee only");
		System.out.println("Press any other number to see both attendee and camp committee");
		int filter = sc.nextInt();
		reportGeneratorService.generateCampReport(camp, filter);
	}
}