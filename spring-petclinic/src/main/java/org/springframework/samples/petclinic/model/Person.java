// Paketdeklaration: Definiert den Namespace der Klasse innerhalb des Projekts
package org.springframework.samples.petclinic.model;

// Importiert notwendige Klassen und Annotationen für die Persistenz und Validierung
import jakarta.persistence.Column; // Ermöglicht das Mapping von Klassenfeldern zu Datenbankspalten
import jakarta.persistence.MappedSuperclass; // Markiert die Klasse als Basisklasse für JPA-Entitäten
import jakarta.validation.constraints.NotBlank; // Validierungsannotation, die sicherstellt, dass ein String weder null noch leer ist

// Diese Annotation markiert die Klasse als eine gemappte Superklasse in JPA (Java Persistence API)
// @MappedSuperclass bedeutet, dass die Eigenschaften dieser Klasse von abgeleiteten Entitätsklassen geerbt werden,
// aber die Klasse selbst wird nicht als eigenständige Entität in der Datenbank behandelt
@MappedSuperclass
public class Person extends BaseEntity {

	// Annotation, die das Feld 'firstName' mit der Spalte 'first_name' in der Datenbank verknüpft
	@Column(name = "first_name")
	// Validierungsannotation, die sicherstellt, dass 'firstName' nicht null oder leer ist
	@NotBlank
	private String firstName;

	// Annotation, die das Feld 'lastName' mit der Spalte 'last_name' in der Datenbank verknüpft
	@Column(name = "last_name")
	// Validierungsannotation, die sicherstellt, dass 'lastName' nicht null oder leer ist
	@NotBlank
	private String lastName;

	// Getter-Methode für das Feld 'firstName'
	public String getFirstName() {
		return this.firstName;
	}

	// Setter-Methode für das Feld 'firstName'
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	// Getter-Methode für das Feld 'lastName'
	public String getLastName() {
		return this.lastName;
	}

	// Setter-Methode für das Feld 'lastName'
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
