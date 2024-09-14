package org.springframework.samples.petclinic.owner;

// Importiert benötigte Klassen und Annotationen
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

// Markiert die Klasse als eine Komponente, die von Spring verwaltet wird
@Component
public class PetTypeFormatter implements Formatter<PetType> {

	// Repository zum Zugriff auf Owner- und PetType-Daten
	private final OwnerRepository owners;

	// Konstruktor mit Autowiring, um das OwnerRepository zu injizieren
	@Autowired
	public PetTypeFormatter(OwnerRepository owners) {
		this.owners = owners;
	}

	// Überschreibt die 'print'-Methode des Formatter-Interfaces
	@Override
	public String print(PetType petType, Locale locale) {
		// Gibt den Namen des PetType zurück, z. B. "Hund" oder "Katze"
		return petType.getName();
	}

	// Überschreibt die 'parse'-Methode, um einen String in ein PetType-Objekt zu konvertieren
	@Override
	public PetType parse(String text, Locale locale) throws ParseException {
		// Holt alle verfügbaren PetTypes aus dem Repository
		Collection<PetType> findPetTypes = this.owners.findPetTypes();
		// Durchläuft die Liste der PetTypes
		for (PetType type : findPetTypes) {
				// Vergleicht den Namen des PetType mit dem gegebenen Text
				if (type.getName().equals(text)) {
						// Gibt das entsprechende PetType-Objekt zurück
						return type;
				}
		}
		// Wenn kein passender PetType gefunden wird, wird eine ParseException geworfen
		throw new ParseException("type not found: " + text, 0);
	}
}
