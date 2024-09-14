package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Der Controller für die Startseite der Anwendung
@Controller
class WelcomeController {

	// Behandelt GET-Anfragen an die Wurzel-URL "/" und gibt die Willkommensseite zurück
	@GetMapping("/") // Mappt HTTP GET-Anfragen an "/" auf diese Methode
	public String welcome() {
		// Gibt den Namen der View zurück, die angezeigt werden soll
		return "welcome";
	}
}