package org.springframework.samples.petclinic.owner;

// Importiert die NamedEntity-Klasse, die wahrscheinlich Felder wie 'id' und 'name' enthält
import org.springframework.samples.petclinic.model.NamedEntity;

// Importiert JPA-Annotationen für die Entitätszuordnung
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Markiert die Klasse als eine JPA-Entität, die in der Datenbank persistiert wird
@Entity
// Ordnet diese Entität der Tabelle 'types' in der Datenbank zu
@Table(name = "types")
public class PetType extends NamedEntity {
    // Die Klasse ist leer, erbt aber von NamedEntity
}
