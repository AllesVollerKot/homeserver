package helper;
import java.io.Serializable;

public class VeranstaltungItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1134151487097547912L;
	private String veranstaltungTitle;
	private String veranstaltungLink;
	private String veranstaltungOrt;
	private String veranstaltungDatumBeginn;
	private String veranstaltungDatumEnde;
	private String veranstaltungZeitBeginn;
	private String veranstaltungZeitEnde;

	public VeranstaltungItem() {
		veranstaltungTitle = "";
		veranstaltungLink = "";
		veranstaltungOrt = "";
		veranstaltungDatumBeginn = "";
		veranstaltungDatumEnde = "";
		veranstaltungZeitBeginn = "";
		veranstaltungZeitEnde = "";
	}

	@Override
	public String toString() {
		return veranstaltungTitle;
	}

	public String getVeranstaltungTitle() {
		return veranstaltungTitle;
	}

	public void setVeranstaltungTitle(String veranstaltungTitle) {
		this.veranstaltungTitle = veranstaltungTitle;
	}

	public String getVeranstaltungLink() {
		return veranstaltungLink;
	}

	public void setVeranstaltungLink(String veranstaltungLink) {
		this.veranstaltungLink = veranstaltungLink;
	}

	public String getVeranstaltungOrt() {
		return veranstaltungOrt;
	}

	public void setVeranstaltungOrt(String veranstaltungOrt) {
		this.veranstaltungOrt = veranstaltungOrt;
	}

	public String getVeranstaltungDatumBeginn() {
		return veranstaltungDatumBeginn;
	}

	public void setVeranstaltungDatumBeginn(String veranstaltungDatumBeginn) {
		this.veranstaltungDatumBeginn = veranstaltungDatumBeginn;
	}

	public String getVeranstaltungDatumEnde() {
		return veranstaltungDatumEnde;
	}

	public void setVeranstaltungDatumEnde(String veranstaltungDatumEnde) {
		this.veranstaltungDatumEnde = veranstaltungDatumEnde;
	}

	public String getVeranstaltungZeitBeginn() {
		return veranstaltungZeitBeginn;
	}

	public void setVeranstaltungZeitBeginn(String veranstaltungZeitBeginn) {
		this.veranstaltungZeitBeginn = veranstaltungZeitBeginn;
	}

	public String getVeranstaltungZeitEnde() {
		return veranstaltungZeitEnde;
	}

	public void setVeranstaltungZeitEnde(String veranstaltungZeitEnde) {
		this.veranstaltungZeitEnde = veranstaltungZeitEnde;
	}

}