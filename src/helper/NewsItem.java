package helper;

import java.util.Date;

public class NewsItem implements Comparable<NewsItem> {

	private String newsTitle;
	private String newsLink;
	private String newsText;
	private String html;
	private int id;
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public NewsItem() {
		newsTitle = "";
		newsLink = "";
		newsText = "";
		html = "";
		id = 0;
		date = new Date();
	}

	public NewsItem(String titel, String text, String link) {
		newsTitle = titel;
		newsLink = link;
		newsText = text;
		html = "";
		id = 0;
		date = new Date();
	}

	public NewsItem(int id, String titel, String text, String link) {
		newsTitle = titel;
		newsLink = link;
		newsText = text;
		html = "";
		this.id = id;
		date = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return newsTitle;
	}

	public void setTitle(String title) {
		newsTitle = title;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public String getNewsLink() {
		return newsLink;
	}

	public String getNewsText() {
		return newsText;
	}

	public void setLink(String link) {
		newsLink = link;
	}

	public void setText(String text) {
		newsText = text;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	@Override
	public int compareTo(NewsItem vergleicher) {

		// Item ist früher als (kleiner) als vergleicher
		if (date.getTime() < vergleicher.getDate().getTime()) {
			return -1;
		} else {
			if (date.getTime() > vergleicher.getDate().getTime()) {
				return 1;
			} else {
				if (date.getTime() == vergleicher.getDate().getTime()) {
					return 0;
				}

			}
		}
		return 0;
	}
}