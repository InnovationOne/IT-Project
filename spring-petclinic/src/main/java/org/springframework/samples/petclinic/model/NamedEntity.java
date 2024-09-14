// Paketdeklaration: Definiert den Namespace der Klasse innerhalb des Projekts
package org.springframework.samples.petclinic.model;

// Importiert die notwendigen Annotationen für die Persistenzschicht
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;


// Diese Annotation markiert die Klasse als eine gemappte Superklasse in JPA (Java Persistence API)
// In JPA bedeutet @MappedSuperclass, dass die Felder dieser Klasse von ihren Unterklassen geerbt werden
@MappedSuperclass
public class NamedEntity extends BaseEntity {

	// Definiert eine Spalte in der Datenbanktabelle mit dem Namen 'name'
  // Das Feld 'name' wird dieser Spalte zugeordnet
	@Column(name = "name")
	private String name;

	// Getter-Methode für das Feld 'name'
	public String getName() {
		return this.name;
	}

	// Setter-Methode für das Feld 'name'
	public void setName(String name) {
		this.name = name;
	}

	// Überschreibt die toString()-Methode der Oberklasse
  // Gibt den Wert von 'name' zurück, wenn ein Objekt dieser Klasse als String dargestellt wird
	@Override
	public String toString() {
		return this.getName();
	}
}
