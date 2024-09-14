package org.springframework.samples.petclinic.owner;

// Importieren der notwendigen Klassen und Annotationen
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

// Kennzeichnet die Klasse als JPA-Entität für die Persistenzschicht
@Entity
// Ordnet die Entität der Datenbanktabelle "visits" zu
@Table(name = "visits")
public class Visit extends BaseEntity {

	// Mappt das Feld auf die Spalte "visit_date" in der Datenbank
	@Column(name = "visit_date")
	// Definiert das Format für die Darstellung und Verarbeitung des Datums
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// Feld für das Datum des Besuchs
	private LocalDate date;

	// Stellt sicher, dass dieses Feld nicht null und nicht leer ist
	@NotBlank
	// Feld für die Beschreibung des Besuchs
	private String description;

	// Initialisiert das Datum des Besuchs mit dem aktuellen Datum
	public Visit() {
		this.date = LocalDate.now();
	}

	// Getter-Methode für das Datum des Besuchs
	public LocalDate getDate() {
		return this.date;
	}

	// Setter-Methode für das Datum des Besuchs
	public void setDate(LocalDate date) {
		this.date = date;
	}

	// Getter-Methode für die Beschreibung des Besuchs
	public String getDescription() {
		return this.description;
	}

	// Setter-Methode für die Beschreibung des Besuchs
	public void setDescription(String description) {
		this.description = description;
	}
}