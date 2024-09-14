package org.springframework.samples.petclinic.owner;

// Importieren der notwendigen Klassen und Annotationen
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Markiert die Klasse als Controller in der Spring MVC Architektur
@Controller
// Der VisitController verwaltet die HTTP-Anfragen im Zusammenhang mit Besuchen (Visits) von Haustieren
class VisitController {

	// Repository zum Zugriff auf Owner-Daten
	private final OwnerRepository owners;

	// Konstruktor zur Injektion des OwnerRepositorys
	public VisitController(OwnerRepository owners) {
		this.owners = owners;
	}

	//Initialisiert den WebDataBinder
	@InitBinder
	// Verhindert, dass das Feld 'id' gebunden wird, um Manipulationen zu vermeiden
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	// Lädt ein Haustier mit einem neuen Visit in das Modell
	// Diese Methode wird vor jedem Request-Handler aufgerufen, der ein 'visit' ModelAttribute erwartet
	@ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
                                  Map<String, Object> model) {
			// Findet den Besitzer anhand der ID
			Owner owner = this.owners.findById(ownerId);

			// Holt das Haustier des Besitzers anhand der Haustier-ID
			Pet pet = owner.getPet(petId);

			// Fügt das Haustier und den Besitzer dem Modell hinzu
			model.put("pet", pet);
			model.put("owner", owner);

			// Erstellt einen neuen Besuch und fügt ihn dem Haustier hinzu
			Visit visit = new Visit();
			pet.addVisit(visit);

			// Gibt den neuen Besuch zurück
			return visit;
    }

	// Handhabt GET-Anfragen zum Initialisieren des Formulars für einen neuen Besuch
	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String initNewVisitForm() {
		// Gibt den Namen der View zurück, um das Formular anzuzeigen
		return "pets/createOrUpdateVisitForm";
	}

	// Handhabt POST-Anfragen zum Verarbeiten des Formulars für einen neuen Besuch
	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@ModelAttribute Owner owner, @PathVariable int petId, @Valid Visit visit,
                                      BindingResult result, RedirectAttributes redirectAttributes) {
																				
		if (result.hasErrors()) {
				// Bei Validierungsfehlern zurück zum Formular
				return "pets/createOrUpdateVisitForm";
		}

		// Fügt den Besuch dem entsprechenden Haustier des Besitzers hinzu
		owner.addVisit(petId, visit);

		// Speichert den Besitzer mit dem neuen Besuch im Repository
		this.owners.save(owner);

		// Fügt eine Flash-Nachricht hinzu, die nach der Umleitung angezeigt wird
		redirectAttributes.addFlashAttribute("message", "Your visit has been booked");

		// Umleitung zur Detailseite des Besitzers
		return "redirect:/owners/{ownerId}";
	}
}