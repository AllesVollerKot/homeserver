import helper.Config;
import helper.NewsItem;
import helper.SMKAenderung;
import helper.VeranstaltungItem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JTextArea;

import org.jsoup.nodes.Document;

public class NewsDownloader {

	public JTextArea txtStatus;
	public JTextArea txtNews;

	public NewsDownloader(JTextArea txtStatus, JTextArea txtNews) {
		this.txtStatus = txtStatus;
		this.txtNews = txtNews;
	}

	public void appendStatus(String eingabe) {
		txtStatus.append(eingabe + "\n");		
	}

	public void appendNews(String eingabe) {
		txtNews.append(eingabe + "\n");
	}

	public void los() {
		// Listenparser initialisieren
		DataParser parser = new DataParser();

		boolean newsneu = false;
		boolean wiwineu = false;
		boolean iksneu = false;
		boolean eventneu = false;
		boolean smkneu = false;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM. HH:mm");

		txtStatus.append(dateFormat.format(new Date())
				+ " Downloadvorgang gestartet" + "\n");

		// Hole alle WebsiteDocs
		Document newsDoc = parser.downloadDoc("http://www.hs-merseburg.de");
		if (newsDoc != null) {
			txtStatus.append("newsdoc geladen" + "\n");
		}
		Document wiwiDoc = parser
				.downloadDoc("http://www.hs-merseburg.de/ww/aktuelles/praesenzstudium/stundenplanaenderungen");
		if (wiwiDoc != null) {
			txtStatus.append("wiwiDoc geladen" + "\n");
		}
		Document iksDoc = parser
				.downloadDoc("http://www.hs-merseburg.de/iks/aktuelles/stundenplanaenderungen");
		if (iksDoc != null) {
			txtStatus.append("iksDoc geladen" + "\n");
		}
		Document eventDoc = parser
				.downloadDoc("http://www.hs-merseburg.de/home/veranstaltungen");
		if (eventDoc != null) {
			txtStatus.append("eventDoc geladen" + "\n");
		}
		Document smkDoc = DataParser
				.getLoginDoc("http://www.hs-merseburg.de/smk/aktuelles/studienablauf/");
		if (smkDoc != null) {
			txtStatus.append("smkDoc geladen" + "\n");
		}

		ArrayList<NewsItem> newsDataListe = parser.getNews(newsDoc);

		// SQL Verbindung initialisieren
		MySQLAccess sql = new MySQLAccess();

		// News Data
		System.out.println("");

		Collections.sort(newsDataListe);

		for (NewsItem item : newsDataListe) {
			int test = sql
					.testData("SELECT * FROM news_data WHERE titel LIKE '%"
							+ item.getNewsTitle() + "%'");
			if (test == 0) {
				// Wenn neue daten schreibe sie in Datebank
				// html abrufen
				String html = parser.getNewsHtml(item.getNewsLink());
				sql.setNewsData(item.getNewsTitle(), item.getNewsText(),
						item.getNewsLink(), html, item.getDate());
				txtNews.append(dateFormat.format(new Date())
						+ " News hinzugef�gt: " + item.getNewsTitle() + "\n");
				eventneu = true;
			}
		}

		// Wiwi Data
		ArrayList<NewsItem> wiwiDataListe = parser.getAenderung(wiwiDoc);
		Collections.reverse(wiwiDataListe);
		for (NewsItem item : wiwiDataListe) {
			// check if item is in database
			int test = sql
					.testData("SELECT * FROM wiwi_data WHERE titel LIKE '%"
							+ item.getNewsTitle() + "%'");
			if (test == 0) {
				String html = parser.getWiwiHtml(item.getNewsLink());
				sql.setWiwiData(item.getNewsTitle(), item.getNewsText(),
						item.getNewsLink(), html);
				txtNews.append(dateFormat.format(new Date())
						+ " WiWi hinzugef�gt: " + item.getNewsTitle() + "\n");
				wiwineu = true;
			}
		}

		// IKS Data
		ArrayList<NewsItem> iksDataListe = parser.getAenderung(iksDoc);
		Collections.reverse(iksDataListe);
		for (NewsItem item : iksDataListe) {
			// check if item is in database
			int test = sql
					.testData("SELECT * FROM iks_data WHERE titel LIKE '%"
							+ item.getNewsTitle() + "%'");
			if (test == 0) {
				sql.setIksData(item.getNewsTitle(), item.getNewsText());
				txtNews.append(dateFormat.format(new Date())
						+ " IKS hinzugef�gt: " + item.getNewsTitle() + "\n");
				iksneu = true;
			}
		}

		// SMK DATA
		ArrayList<SMKAenderung> smkDataListe = parser.getSMK(smkDoc);
		Collections.reverse(smkDataListe);
		for (SMKAenderung item : smkDataListe) {
			// check if item is in database
			int test = sql
					.testData("SELECT * FROM smk_data WHERE titel LIKE '%"
							+ item.getNewsTitle() + "%'");
			if (test == 0) {
				String html = parser.getSMKHtml(item.getNewsLink());
				sql.setSmkData(item.getNewsTitle(), item.getNewsText(),
						item.getText2(), item.getNewsLink(), html);
				txtNews.append(dateFormat.format(new Date())
						+ " SMK hinzugef�gt: " + item.getNewsTitle() + "\n");
				smkneu = true;
			}
		}

		// Event Data
		ArrayList<VeranstaltungItem> eventDataListe = parser
				.getEvents(eventDoc);
		Collections.reverse(eventDataListe);
		for (VeranstaltungItem item : eventDataListe) {
			// check if item is in database
			int test = sql
					.testData("SELECT * FROM event_data WHERE titel LIKE '%"
							+ item.getVeranstaltungTitle() + "%'");
			if (test == 0) {
				String html = parser.getEventHtml(item.getVeranstaltungLink());
				sql.setEventData(item.getVeranstaltungTitle(),
						item.getVeranstaltungLink(),
						item.getVeranstaltungOrt(),
						item.getVeranstaltungZeitBeginn(),
						item.getVeranstaltungZeitEnde(),
						item.getVeranstaltungDatumBeginn(),
						item.getVeranstaltungDatumEnde(), html);
				txtNews.append(dateFormat.format(new Date())
						+ " Event hinzugef�gt: "
						+ item.getVeranstaltungTitle() + "\n");
				eventneu = true;
			}
		}

		// GCM SENDEN
		if (newsneu || wiwineu || iksneu || eventneu || smkneu) {
			sql.sendMessage("neu");
			txtStatus.append("GCM gesendet" + "\n");

		} else {
			txtStatus.append("keine neuen Daten" + "\n");
		}
		txtStatus.append(dateFormat.format(new Date())
				+ " Downloadvorgang beendet" + "\n");

		saveToFile();
	}

	public void saveToFile() {

		ObjectOutputStream outputStream = null;
		try {

			// Construct the LineNumberReader object
			outputStream = new ObjectOutputStream(new FileOutputStream(
					MainWindow.dataFileName));

			Config config = new Config(txtStatus.getText(), txtNews.getText());

			outputStream.writeObject(config);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
