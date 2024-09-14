package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.model.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;

// Die Klasse Vet repräsentiert einen Tierarzt in der Anwendung.
// Sie erbt von Person und fügt zusätzliche Eigenschaften wie Spezialisierungen hinzu.
@Entity
@Table(name = "vets")
public class Vet extends Person {

	// Ein Set von Spezialisierungen, die diesem Tierarzt zugeordnet sind.
	// Verwenden von @ManyToMany, da ein Tierarzt mehrere Spezialisierungen haben
	// kann und eine Spezialisierung mehreren Tierärzten zugeordnet sein kann.
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	private Set<Specialty> specialties;

	// Gibt das interne Set von Spezialisierungen zurück.
	// Initialisiert das Set bei Bedarf.
	protected Set<Specialty> getSpecialtiesInternal() {
		if (this.specialties == null) {
			this.specialties = new HashSet<>();
		}
		// Gibt das Set von Spezialisierungen zurück.
		return this.specialties;
	}

	// Setzt das interne Set von Spezialisierungen.
	protected void setSpecialtiesInternal(Set<Specialty> specialties) {
		this.specialties = specialties;
	}

	// Gibt eine unveränderliche Liste der Spezialisierungen zurück, sortiert nach
	// dem Namen der Spezialisierung.
	@XmlElement
	public List<Specialty> getSpecialties() {
		List<Specialty> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
		// Sortiert die Liste der Spezialisierungen nach ihrem Namen
		PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
		// Gibt eine unveränderliche Liste zurück, um die Kapselung zu wahren
		return Collections.unmodifiableList(sortedSpecs);
	}

	// Gibt die Anzahl der Spezialisierungen dieses Tierarztes zurück.
	public int getNrOfSpecialties() {
		return getSpecialtiesInternal().size();
	}

	// Fügt diesem Tierarzt eine neue Spezialisierung hinzu.
	public void addSpecialty(Specialty specialty) {
		getSpecialtiesInternal().add(specialty);
	}

}
