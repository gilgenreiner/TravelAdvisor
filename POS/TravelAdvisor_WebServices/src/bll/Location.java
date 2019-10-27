package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Location {
	private UUID id;
	private String bezeichnung;
	private String beschreibung;
	private boolean aktiv;
	public int punkte;
	private List<Branche> branchen;
	private Besitzer besitzer;
	private Point koordinaten;
	private String img;
	
	//image[]	
	
	public Location() {
		branchen = new ArrayList<Branche>();
		bezeichnung = "";
		beschreibung = "";
		koordinaten = new Point();
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(String id) {
		try {
			this.id = UUID.fromString(id);
		} 
		catch(Exception ex) {
			this.id = null;
		}
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public boolean isAktiv() {
		return aktiv;
	}
	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}
	public int getPunkte() {
		return punkte;
	}
	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}
	public List<Branche> getBranchen() {
		return branchen;
	}
	public void setBranchen(List<Branche> branche) {
		this.branchen = branche;
	}
	public void addBranche(Branche branche) {
		this.branchen.add(branche);
	}
	public Besitzer getBesitzer() {
		return besitzer;
	}
	public void setBesitzer(Besitzer besitzer) {
		this.besitzer = besitzer;
	}
	public Point getKoordinaten() {
		return koordinaten;
	}
	public void setKoordinaten(Point koordinaten) {
		this.koordinaten = koordinaten;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
	public void generateUUID() {
		this.id = UUID.randomUUID();
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@Override
	public String toString() {
		return this.bezeichnung + " mit der id: " + this.id;
	}
	
}
