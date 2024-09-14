// Paketdeklaration: Definiert den Namespace der Klasse innerhalb des Projekts
package org.springframework.samples.petclinic.model;

// Importiert benötigte Klassen und Schnittstellen
import java.io.Serializable; // Ermöglicht die Serialisierung des Objekts

import jakarta.persistence.GeneratedValue; // Annotation zur automatischen Generierung von Primärschlüsselwerten
import jakarta.persistence.GenerationType; // Gibt die Strategie für die ID-Generierung an
import jakarta.persistence.Id; // Markiert ein Feld als Primärschlüssel
import jakarta.persistence.MappedSuperclass; // Kennzeichnet die Klasse als gemappte Superklasse in JPA

// Diese Annotation markiert die Klasse als eine gemappte Superklasse in JPA (Java Persistence API)
// @MappedSuperclass bedeutet, dass die Felder dieser Klasse von abgeleiteten Klassen geerbt werden,
// aber die Klasse selbst wird nicht als eigenständige Entität in der Datenbank behandelt.
@MappedSuperclass
public class BaseEntity implements Serializable {

	// Markiert das Feld 'id' als Primärschlüssel in der Datenbank
	@Id
	// Gibt an, dass der Wert von 'id' automatisch generiert werden soll
	// GenerationType.IDENTITY lässt die Datenbank die ID generieren, z.B. durch Auto-Inkrement
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Getter-Methode für das Feld 'id'
	public Integer getId() {
		return id;
	}

	// Setter-Methode für das Feld 'id'
	public void setId(Integer id) {
		this.id = id;
	}

	// Methode, um zu prüfen, ob das Objekt neu ist (d.h., ob es noch keine ID hat)
	// Gibt true zurück, wenn 'id' null ist
	public boolean isNew() {
		return this.id == null;
	}
}
