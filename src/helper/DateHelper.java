package helper;

public class DateHelper {

	public DateHelper() {

	}

	public String umwandler(String input) {
		String output = input;

		output = output.replaceAll("Montag", "Monday");
		output = output.replaceAll("Dienstag", "Tuesday");
		output = output.replaceAll("Mittwoch", "Wednesday");
		output = output.replaceAll("Donnerstag", "Thursday");
		output = output.replaceAll("Freitag", "Friday");
		output = output.replaceAll("Samstag", "Saturday");
		output = output.replaceAll("Sonntag", "Sunday");

		output = output.replaceAll("Januar", "January");
		output = output.replaceAll("Februar", "February");
		output = output.replaceAll("März", "March");
		output = output.replaceAll("April", "April");
		output = output.replaceAll("Mai", "May");
		output = output.replaceAll("Juni", "June");
		output = output.replaceAll("Juli", "July");
		output = output.replaceAll("August", "August");
		output = output.replaceAll("September", "September");
		output = output.replaceAll("Oktober", "October");
		output = output.replaceAll("November", "November");
		output = output.replaceAll("Dezember", "December");

		return output;

	}

}
