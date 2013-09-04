package helper;

import java.io.Serializable;

public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 594884236802684198L;
	private String txtNews;
	private String txtStatus;

	public String getTxtStatus() {
		return txtStatus;
	}

	public void setTxtStatus(String txtStatus) {
		this.txtStatus = txtStatus;
	}

	public Config(String txtStatus, String txtNews) {
		this.txtStatus = txtStatus;
		this.setTxtNews(txtNews);
	}

	public String getTxtNews() {
		return txtNews;
	}

	public void setTxtNews(String txtNews) {
		this.txtNews = txtNews;
	}
}
