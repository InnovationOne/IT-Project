package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Ein Controller, der eine Ausnahme auslöst, um Fehlerhandling zu demonstrieren
@Controller
class CrashController {

	// Behandelt GET-Anfragen an "/oups" und löst eine RuntimeException aus
	@GetMapping("/oups") // Mappt HTTP GET-Anfragen an "/oups" auf diese Methode
	public String triggerException() {
		// Wirft eine RuntimeException mit einer erklärenden Nachricht
		throw new RuntimeException(
				"Expected: controller used to showcase what " + "happens when an exception is thrown");
	}
}