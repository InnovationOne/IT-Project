package org.springframework.samples.petclinic.owner;

// Importiert benötigte Hilfsklassen und Schnittstellen
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// Definiert eine Klasse, die das Validator-Interface implementiert
public class PetValidator implements Validator {

	// Definiert eine Konstante für Fehlermeldungen
	private static final String REQUIRED = "required";

	// Überschreibt die 'validate' Methode des 'Validator' Interfaces
	@Override
	public void validate(Object obj, Errors errors) {
			// Wandelt das gegebene Objekt in ein 'Pet' Objekt um
			Pet pet = (Pet) obj;
			// Holt den Namen des Haustiers
			String name = pet.getName();

			// Überprüft, ob der Name nicht leer oder null ist
			if (!StringUtils.hasText(name)) {
					// Fügt einen Validierungsfehler für das Feld 'name' hinzu
					errors.rejectValue("name", REQUIRED, REQUIRED);
			}

			// Überprüft, ob das Haustier neu ist und keinen Typ hat
			if (pet.isNew() && pet.getType() == null) {
					// Fügt einen Validierungsfehler für das Feld 'type' hinzu
					errors.rejectValue("type", REQUIRED, REQUIRED);
			}

			// Überprüft, ob das Geburtsdatum nicht gesetzt ist
			if (pet.getBirthDate() == null) {
					// Fügt einen Validierungsfehler für das Feld 'birthDate' hinzu
					errors.rejectValue("birthDate", REQUIRED, REQUIRED);
			}
	}


	// Überschreibt die 'supports' Methode, um anzugeben, welche Klassen dieser Validator unterstützt
	@Override
	public boolean supports(Class<?> clazz) {
			// Prüft, ob die Klasse 'Pet' oder eine Unterklasse davon ist
			return Pet.class.isAssignableFrom(clazz);
	}
}
