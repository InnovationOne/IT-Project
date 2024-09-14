package org.springframework.samples.petclinic;

// Importieren der notwendigen Spring-Boot-Klassen und Annotationen
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

// Annotation, die die Klasse als Spring-Boot-Anwendung definiert und die Komponenten-Scan- und Auto-Konfigurationsmechanismen aktiviert
@SpringBootApplication
// Importiert die Klasse PetClinicRuntimeHints, um Runtime Hints für die AOT-Kompilierung bereitzustellen
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication {

	// Hauptmethode der Anwendung, der Einstiegspunkt beim Starten der Anwendung
	public static void main(String[] args) {
		// Startet die Spring-Boot-Anwendung, wobei PetClinicApplication die Hauptkonfigurationsklasse ist
		SpringApplication.run(PetClinicApplication.class, args);
	}
}