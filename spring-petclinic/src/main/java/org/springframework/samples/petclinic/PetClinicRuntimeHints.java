package org.springframework.samples.petclinic;

// Importieren der notwendigen Klassen für Runtime Hints und Modellklassen
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

// Implementiert die Schnittstelle RuntimeHintsRegistrar zur Registrierung von benutzerdefinierten Runtime Hints
public class PetClinicRuntimeHints implements RuntimeHintsRegistrar {

	// Überschreibt die Methode registerHints, um benutzerdefinierte Hints zu registrieren
	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		// Registriert Ressourcenmuster, damit sie bei der AOT-Kompilierung berücksichtigt werden
		hints.resources().registerPattern("db/*"); // Registriert alle Ressourcen unter "db/*" (z.B. SQL-Skripte)
        hints.resources().registerPattern("messages/*"); // Registriert alle Ressourcen unter "messages/*" (z.B. Properties für Internationalisierung)
        hints.resources().registerPattern("META-INF/resources/webjars/*"); // Registriert WebJAR-Ressourcen (Frontend-Abhängigkeiten)
        hints.resources().registerPattern("mysql-default-conf"); // Registriert die MySQL-Standardkonfigurationsdatei

        // Registriert Modellklassen für die Serialisierung, um sie während der Laufzeit serialisieren/deserialisieren zu können
        hints.serialization().registerType(BaseEntity.class); // Registriert BaseEntity für die Serialisierung
        hints.serialization().registerType(Person.class); // Registriert Person für die Serialisierung
        hints.serialization().registerType(Vet.class); // Registriert Vet für die Serialisierung
	}

}
