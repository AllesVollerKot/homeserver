import helper.NewsItem;
import helper.SMKAenderung;
import helper.VeranstaltungItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;


public class DataParser {

	public DataParser() {
	}

	public Document downloadDoc(String url) {
		Document doc = null;

		try {
			InputStream input = new URL(url).openStream();
			doc = Jsoup.parse(input, "CP1252", url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static Document getLoginDoc(String url) {
		Connection.Response response;
		Document doc = null;
		try {
			response = Jsoup.connect("https://www.hs-merseburg.de/jumpto/fuer-mitarbeiter/login/").method(Connection.Method.GET).execute();
			response = Jsoup.connect("https://www.hs-merseburg.de/jumpto/fuer-mitarbeiter/login/").data("user", "8cweier").data("pass", "l-w1=t+T")
					.data("submit", "Anmelden").data("logintype", "login").data("pid", "55").cookies(response.cookies())
					.method(Connection.Method.POST).execute();

			doc = Jsoup.connect(url).cookies(response.cookies()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public ArrayList<NewsItem> getNews(Document newsDoc) {
		ArrayList<NewsItem> newsListe = new ArrayList<NewsItem>();
		Source komplettSource = new Source(StringEscapeUtils.unescapeHtml4(newsDoc.toString()));
		List<Element> newsItemSource = komplettSource.getAllElementsByClass("news-list-item");
		for (Element newsElement : newsItemSource) {
			NewsItem newsItem = new NewsItem();

			// TITEL
			String[] newsGesplittetTitel = newsElement.toString().split("title=\"");
			newsGesplittetTitel = newsGesplittetTitel[1].split("\">");

			String titel = newsGesplittetTitel[0];
			newsItem.setTitle(titel);

			// TEXT
			List<Element> textElemente = newsElement.getAllElementsByClass("bodytext");
			Source textSource = new Source(textElemente.get(0).toString());
			String text = textSource.getTextExtractor().toString();
			text = text.replace("[mehr Information]", "");
			newsItem.setText(text);

			// LINK
			String newsLink = "http://www.hs-merseburg.de/";
			String[] newsGesplittetLink = newsElement.toString().split("<a href=\"");
			newsGesplittetLink = newsGesplittetLink[1].split("\"");
			newsLink = newsLink + newsGesplittetLink[0];
			newsLink = newsLink.replace("&amp;", "&");
			newsItem.setLink(newsLink);
			
			// TIME
			
			newsItem.setDate(this.getNewsTime(newsLink));			
			newsListe.add(newsItem);
			
		}
		return newsListe;
	}

	public java.util.Date getNewsTime(String link) {
		Document doc = this.downloadDoc(link);
		Elements newsTime = doc.getElementsByClass("news-single-timedata");
		String[] newsGesplittet = newsTime.toString().split(">");
		newsGesplittet = newsGesplittet[1].toString().split(" Alter: ");
	
		String timeString = newsGesplittet[0].trim();	
		java.util.Date javaDate = null;	
		String pattern = "EEEE, dd. MMMMM yyyy HH:mm";
	
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.GERMAN);
		try {
			javaDate = sdf.parse(timeString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return javaDate;	
	}

	public ArrayList<VeranstaltungItem> getEvents(Document eventsDoc) {

		ArrayList<VeranstaltungItem> veranstaltungsListe = new ArrayList<VeranstaltungItem>();
		// ALLE Veranstaltungs KLASSEN in ganzeSeite
		Elements veranstaltungsItems = eventsDoc.getElementsByClass("tx-bcevents-list-td-1");
		for (org.jsoup.nodes.Element element : veranstaltungsItems) {
			VeranstaltungItem veranstaltungsItem = new VeranstaltungItem();

			// TITEL
			Elements titleElemente = element.getElementsByClass("tx-bcevents-list-title");
			Source titleSource = new Source(titleElemente.toString());
			String title = titleSource.getTextExtractor().toString();
			veranstaltungsItem.setVeranstaltungTitle(title);

			// ORT
			Elements ortElemente = element.getElementsByClass("tx-bcevents-list-location");
			Source ortSource = new Source(ortElemente.toString());
			String ort = ortSource.getTextExtractor().toString();
			veranstaltungsItem.setVeranstaltungOrt(ort);

			// DATUM BEGINN
			Elements datumBeginnElemente = element.getElementsByClass("tx-bcevents-list-bdate");
			Source datumBeginnSource = new Source(datumBeginnElemente.toString());
			String beginnDatum = datumBeginnSource.getTextExtractor().toString();
			veranstaltungsItem.setVeranstaltungDatumBeginn(beginnDatum);

			// DATUM ENDE
			Elements datumEndeElemente = element.getElementsByClass("tx-bcevents-list-edate");
			Source datumEndeSource = new Source(datumEndeElemente.toString());
			String endeDatum = datumEndeSource.getTextExtractor().toString();
			veranstaltungsItem.setVeranstaltungDatumEnde(endeDatum);

			// ZEIT BEGINN
			Elements zeitBeginnElemente = element.getElementsByClass("tx-bcevents-list-btime");
			Source zeitBeginnSource = new Source(zeitBeginnElemente.toString());
			String beginnZeit = zeitBeginnSource.getTextExtractor().toString();
			veranstaltungsItem.setVeranstaltungZeitBeginn(beginnZeit);

			// ZEIT ENDE
			Elements zeitEndeElemente = element.getElementsByClass("tx-bcevents-list-etime");
			Source zeitEndeSource = new Source(zeitEndeElemente.toString());
			String endeZeit = zeitEndeSource.getTextExtractor().toString();
			veranstaltungsItem.setVeranstaltungZeitEnde(endeZeit);

			// LINK

			Elements linkElemente = element.getElementsByAttribute("href");
			String[] newsGesplittetLink = linkElemente.get(0).toString().split("<a href=\"");

			newsGesplittetLink = newsGesplittetLink[1].split("&amp;tx_bcevents%5BbackPid");
			String veranstaltungsLink = newsGesplittetLink[0].toString();
			veranstaltungsLink = veranstaltungsLink.replace("&amp;", "&");
			veranstaltungsItem.setVeranstaltungLink(veranstaltungsLink);
			veranstaltungsListe.add(veranstaltungsItem);
		}
		return veranstaltungsListe;
	}

	public ArrayList<NewsItem> getAenderung(Document AenderungDoc) {
		ArrayList<NewsItem> aenderungListe = new ArrayList<NewsItem>();
		Elements newsitems = AenderungDoc.getElementsByClass("news-list-item");

		for (int i = 0; i < (newsitems.size()); i++) {
			NewsItem newsItem = new NewsItem();
			org.jsoup.nodes.Element element = newsitems.get(i);

			// TEXT
			Elements textElemente = element.getElementsByClass("bodytext");
			Source textSource = new Source(textElemente.toString());
			String text = textSource.getTextExtractor().toString();

			text = text.replace("[mehr Information]", "");
			newsItem.setText(text);

			// TITEL
			Elements titelElemente = element.getElementsByAttribute("title");
			Source titleSource = new Source(titelElemente.toString());
			String title = titleSource.getTextExtractor().toString();
			title = title.replace("[mehr Information]", "");
			newsItem.setTitle(title);

			// LINK
			String baseUrl = "http://www.hs-merseburg.de/";
			Elements linkElemente = element.getElementsByAttribute("href");
			String[] newsGesplittetLink = linkElemente.get(0).toString().split("<a href=\"");
			newsGesplittetLink = newsGesplittetLink[1].split("\" title=\"");
			String newsLinkEndung = newsGesplittetLink[0].toString();
			String komplettlink = baseUrl + newsLinkEndung;
			newsItem.setLink(komplettlink);

			aenderungListe.add(newsItem);
		}
		return aenderungListe;
	}

	public ArrayList<SMKAenderung> getSMK(Document AenderungDoc) {
		ArrayList<SMKAenderung> smkListe = new ArrayList<SMKAenderung>();
		Elements newsitems = AenderungDoc.getElementsByClass("news-list-item");

		for (int i = 0; i < (newsitems.size()); i++) {
			SMKAenderung smkItem = new SMKAenderung();
			org.jsoup.nodes.Element element = newsitems.get(i);

			// TITEL
			Elements titelElemente = element.getElementsByAttribute("title");
			Source titleSource = new Source(titelElemente.toString());
			String title = titleSource.getTextExtractor().toString();
			title = title.replace("[mehr Information]", "");
			smkItem.setTitle(title);

			// TEXT
			// HTMLHelper helper = new HTMLHelper();
			String teil1 = "";
			String teil2 = "";

			// TEIL1
			String[] smkGesplittetText = element.toString().split("<p>");
			if (smkGesplittetText.length == 1) {
				teil1 = "";
			} else {
				smkGesplittetText = smkGesplittetText[1].split("</p>");
				teil1 = smkGesplittetText[0];
				// teil1 = helper.zeichenUmwandlung(smkGesplittetText[0]);
			}

			// TEIL2
			Elements bodyElemente = element.getElementsByClass("bodytext");
			Source bodySource = new Source(bodyElemente.toString());
			teil2 = bodySource.getTextExtractor().toString();
			teil2 = teil2.replace("[mehr Information]", "");

			if (teil1.isEmpty()) {
				teil1 = teil2;
				teil2 = "";
			}

			smkItem.setText(teil1);
			smkItem.setText2(teil2);

			// LINK
			String newsLink = "http://www.hs-merseburg.de/";
			Elements linkElemente = element.getElementsByAttribute("href");
			String[] newsGesplittetLink = linkElemente.get(0).toString().split("<a href=\"");
			newsGesplittetLink = newsGesplittetLink[1].split("\" title=\"");
			String newsLinkEndung = newsGesplittetLink[0].toString();
			newsLink = newsLink + newsLinkEndung;
			smkItem.setLink(newsLink);

			smkListe.add(smkItem);
		}
		return smkListe;
	}

	public String getNewsHtml(String url) {
		Document doc = null;
		String ausgabe = "";
		try {
			InputStream input = new URL(url).openStream();
			doc = Jsoup.parse(input, "CP1252", url);
			Elements seitenTeil = doc.getElementsByClass("news-single-item");
			String[] ohneBacklink = seitenTeil.toString().split("<hr class=\"clearer\" />");
			ausgabe = ohneBacklink[0];
			ausgabe = StringEscapeUtils.unescapeHtml4(ausgabe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ausgabe;
	}

	public String getEventHtml(String url) {
		Document doc = null;
		String ausgabe = "";
		try {
			InputStream input = new URL(url).openStream();
			doc = Jsoup.parse(input, "CP1252", url);
			Elements seitenTeil = doc.getElementsByClass("tx-bcevents-single");
			ausgabe = StringEscapeUtils.unescapeHtml4(seitenTeil.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ausgabe;
	}

	public String getWiwiHtml(String url) {
		Document doc = null;
		String ausgabe = "";
		try {
			InputStream input = new URL(url).openStream();
			doc = Jsoup.parse(input, "CP1252", url);
			Elements seitenTeil = doc.getElementsByClass("news-single-item");
			String[] ohneBacklink = seitenTeil.toString().split("<div class=\"news-single-backlink\">");
			ausgabe = ohneBacklink[0];
			ausgabe = StringEscapeUtils.unescapeHtml4(ausgabe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ausgabe;
	}

	public String getSMKHtml(String url) {
		Document doc = null;
		String ausgabe = "";
		doc = getLoginDoc(url);
		Elements seitenTeil = doc.getElementsByClass("news-single-item");
		String[] ohneBacklink = seitenTeil.toString().split("<div class=\"news-single-backlink\">");
		ausgabe = ohneBacklink[0];
		ausgabe = StringEscapeUtils.unescapeHtml4(ausgabe);
		return ausgabe;
	}

}
