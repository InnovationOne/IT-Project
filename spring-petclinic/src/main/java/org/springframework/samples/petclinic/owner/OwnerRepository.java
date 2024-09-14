package org.springframework.samples.petclinic.owner;

// Importiert benötigte Klassen für die Arbeit mit Datenbanken und JPA-Repositories
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query; // Ermöglicht benutzerdefinierte JPQL-Abfragen
import org.springframework.data.repository.Repository; // Basisinterface für Repositories
import org.springframework.data.repository.query.Param; // Ermöglicht benannte Parameter in Abfragen
import org.springframework.transaction.annotation.Transactional; // Verwaltung von Datenbanktransaktionen

// Definiert ein Repository-Interface für die Entität "Owner" mit der primären Schlüsselklasse "Integer"
public interface OwnerRepository extends Repository<Owner, Integer> {

    // Definiert eine JPQL-Abfrage, um alle PetTypes (Haustierarten) sortiert nach Namen zu finden
    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    @Transactional(readOnly = true) // Kennzeichnet die Methode als schreibgeschützt (keine Datenänderungen)
    List<PetType> findPetTypes(); // Methode, die eine Liste von PetType zurückgibt

    // Definiert eine JPQL-Abfrage, um Besitzer anhand des Nachnamens zu finden, mit Unterstützung für Paginierung
    @Query("SELECT DISTINCT owner FROM Owner owner left join owner.pets WHERE owner.lastName LIKE :lastName%")
    @Transactional(readOnly = true)
    Page<Owner> findByLastName(@Param("lastName") String lastName, Pageable pageable);
    // Die Methode gibt eine Seite von Besitzern zurück, deren Nachname mit dem gegebenen Parameter beginnt

    // Definiert eine JPQL-Abfrage, um einen Besitzer anhand seiner ID zu finden und lädt gleichzeitig seine Haustiere
    @Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id")
    @Transactional(readOnly = true)
    Owner findById(@Param("id") Integer id);
    // Die Methode gibt einen einzelnen Owner zurück, wobei die Haustiere bereits geladen sind (Fetch Join)

    // Speichert einen Besitzer in der Datenbank (kann zum Erstellen oder Aktualisieren verwendet werden)
    void save(Owner owner);

    // Definiert eine JPQL-Abfrage, um alle Besitzer zu finden, mit Unterstützung für Paginierung
    @Query("SELECT owner FROM Owner owner")
    @Transactional(readOnly = true)
    Page<Owner> findAll(Pageable pageable);
    // Die Methode gibt eine Seite von Besitzern zurück, nützlich für die Anzeige großer Mengen von Daten
}
