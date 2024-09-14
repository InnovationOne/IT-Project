package org.springframework.samples.petclinic.vet;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Die Klasse Specialty repräsentiert eine Spezialisierung oder ein Fachgebiet das einem Tierarzt zugeordnet werden kann.
// Sie erbt von NamedEntity, das einen Namen enthält.
@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {
  // Keine zusätzlichen Felder oder Methoden erforderlich,
  // da alle benötigten Eigenschaften von NamedEntity geerbt werden.
}
