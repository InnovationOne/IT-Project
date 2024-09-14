package org.springframework.samples.petclinic.owner;

// Importiert benötigte Klassen und Annotationen
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

// Markiert die Klasse als eine JPA-Entität, die in der Datenbank persistiert wird
@Entity
// Gibt an, welcher Datenbanktabelle diese Entität entspricht
@Table(name = "pets")
public class Pet extends NamedEntity {

	// Definiert eine Spalte in der Datenbanktabelle mit dem Namen 'birth_date'
	@Column(name = "birth_date")
	// Gibt das Datumsformat für die Darstellung an
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	// Definiert eine Viele-zu-Eins-Beziehung zu 'PetType'
	@ManyToOne
	// Gibt die Fremdschlüsselspalte 'type_id' in der 'pets'-Tabelle an
	@JoinColumn(name = "type_id")
	private PetType type;

	// Definiert eine Eins-zu-Viele-Beziehung zu 'Visit'
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	// Verknüpft 'visits' über die Fremdschlüsselspalte 'pet_id' in der 'visits'-Tabelle
	@JoinColumn(name = "pet_id")
	// Sortiert die 'visits' nach dem Datum aufsteigend
	@OrderBy("visit_date ASC")
	private Set<Visit> visits = new LinkedHashSet<>();

	// Setter-Methode für das Geburtsdatum des Haustiers
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
}

// Getter-Methode für das Geburtsdatum des Haustiers
public LocalDate getBirthDate() {
		return this.birthDate;
}

// Getter-Methode für den Typ des Haustiers (z.B. Hund, Katze)
public PetType getType() {
		return this.type;
}

// Setter-Methode für den Typ des Haustiers
public void setType(PetType type) {
		this.type = type;
}

// Gibt eine Sammlung aller Besuche des Haustiers zurück
public Collection<Visit> getVisits() {
		return this.visits;
}

// Fügt einen neuen Besuch zur Sammlung hinzu
public void addVisit(Visit visit) {
		getVisits().add(visit);
}
}
