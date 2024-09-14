// Paketdeklaration: Definiert den Namespace der Klasse innerhalb des Projekts
package org.springframework.samples.petclinic.owner;

// Importiert benötigte Klassen und Pakete
import java.util.ArrayList; // Für die Verwendung von ArrayList
import java.util.List; // Für die Verwendung von List

import org.springframework.core.style.ToStringCreator; // Hilfsklasse zum Erstellen von Strings
import org.springframework.samples.petclinic.model.Person; // Basisklasse Person
import org.springframework.util.Assert; // Für Vorbedingungen und Validierung

// JPA- und Validierungsannotations
import jakarta.persistence.CascadeType; // Gibt den Typ des Kaskadierens an
import jakarta.persistence.Column; // Verknüpft ein Feld mit einer Datenbankspalte
import jakarta.persistence.Entity; // Markiert die Klasse als JPA-Entität
import jakarta.persistence.FetchType; // Gibt an, wie Daten abgerufen werden (eager/lazy)
import jakarta.persistence.JoinColumn; // Definiert die Spalte für die Verbindung zwischen Tabellen
import jakarta.persistence.OneToMany; // Beziehungsannotation für One-to-Many
import jakarta.persistence.OrderBy; // Gibt die Sortierreihenfolge der Ergebnisse an
import jakarta.persistence.Table; // Gibt den Tabellennamen in der Datenbank an
import jakarta.validation.constraints.Digits; // Validierung für numerische Felder
import jakarta.validation.constraints.NotBlank; // Validierung für nicht leere Strings

	// Die Klasse wird als JPA-Entität markiert und der zugehörige Tabellenname wird angegeben
	@Entity
	@Table(name = "owners")
	public class Owner extends Person {

	// Verknüpft das Feld 'address' mit der Spalte 'address' in der Datenbank
	@Column(name = "address")
	// Validierungsannotation, die sicherstellt, dass 'address' nicht null oder leer ist
	@NotBlank
	private String address;

	// Verknüpft das Feld 'city' mit der Spalte 'city' in der Datenbank
	@Column(name = "city")
	// Validierungsannotation, die sicherstellt, dass 'city' nicht null oder leer ist
	@NotBlank
	private String city;

	// Verknüpft das Feld 'telephone' mit der Spalte 'telephone' in der Datenbank
	@Column(name = "telephone")
	// Validierungsannotation, die sicherstellt, dass 'telephone' nicht null oder leer ist
	@NotBlank
	// Validierung, die sicherstellt, dass 'telephone' nur Ziffern enthält und maximal 10 Stellen hat
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	// Definiert eine One-to-Many-Beziehung zwischen Owner und Pet
  // Ein Owner kann mehrere Pets haben
	@OneToMany(
		cascade = CascadeType.ALL, // Alle Änderungen an Owner werden auf Pets kaskadiert
		fetch = FetchType.EAGER // Pets werden sofort mitgeladen (eager loading)
	)
	// Definiert die Verknüpfungsspalte in der Pet-Tabelle, die auf den Owner verweist
	@JoinColumn(name = "owner_id")
	// Gibt an, dass die Pets nach dem Feld 'name' sortiert werden sollen
	@OrderBy("name")
	private List<Pet> pets = new ArrayList<>();

	// Getter-Methode für 'address'
	public String getAddress() {
			return this.address;
	}

	// Setter-Methode für 'address'
	public void setAddress(String address) {
			this.address = address;
	}

	// Getter-Methode für 'city'
	public String getCity() {
			return this.city;
	}

	// Setter-Methode für 'city'
	public void setCity(String city) {
			this.city = city;
	}

	// Getter-Methode für 'telephone'
	public String getTelephone() {
			return this.telephone;
	}

	// Setter-Methode für 'telephone'
	public void setTelephone(String telephone) {
			this.telephone = telephone;
	}

	// Getter-Methode für die Liste der Pets
	public List<Pet> getPets() {
			return this.pets;
	}

	// Methode zum Hinzufügen eines neuen Pets
	public void addPet(Pet pet) {
		if (pet.isNew()) {
				getPets().add(pet);
		}
	}

	
	// Gibt das Pet mit dem gegebenen Namen zurück, oder null, wenn keines gefunden wurde.
	public Pet getPet(String name) {
		return getPet(name, false);
	}

	// Gibt das Pet mit der gegebenen ID zurück, oder null, wenn keines gefunden wurde.
	public Pet getPet(Integer id) {
		for (Pet pet : getPets()) {
			if (!pet.isNew()) {
				Integer compId = pet.getId();
				if (compId.equals(id)) {
					return pet;
				}
			}
		}
		return null;
	}

	// Gibt das Pet mit dem gegebenen Namen zurück, oder null, wenn keines gefunden wurde.
	public Pet getPet(String name, boolean ignoreNew) {
		name = name.toLowerCase();
		for (Pet pet : getPets()) {
			String compName = pet.getName();
			if (compName != null && compName.equalsIgnoreCase(name)) {
				if (!ignoreNew || !pet.isNew()) {
					return pet;
				}
			}
		}
		return null;
	}

	// Überschreibt die toString()-Methode, um eine String-Repräsentation des Owners zu liefern
	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId())
			.append("new", this.isNew())
			.append("lastName", this.getLastName())
			.append("firstName", this.getFirstName())
			.append("address", this.address)
			.append("city", this.city)
			.append("telephone", this.telephone)
			.toString();
	}

	// Fügt den gegebenen Besuch (Visit) dem Pet mit der angegebenen ID hinzu.
	public void addVisit(Integer petId, Visit visit) {

		Assert.notNull(petId, "Pet identifier must not be null!");
		Assert.notNull(visit, "Visit must not be null!");

		Pet pet = getPet(petId);

		Assert.notNull(pet, "Invalid Pet identifier!");

		pet.addVisit(visit);
	}
}
