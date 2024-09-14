package org.springframework.samples.petclinic.owner;

// Importiert benötigte Klassen und Pakete
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller; // Markiert die Klasse als Controller in Spring MVC
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Verarbeitet Validierungsfehler von Formularen
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping; // Handhabt GET-Anfragen
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // Bindet Pfadvariablen aus der URL
import org.springframework.web.bind.annotation.PostMapping; // Handhabt POST-Anfragen
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid; // Validiert Eingabedaten
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Markiert die Klasse als Controller, der HTTP-Anfragen verarbeitet
@Controller
class OwnerController {

	// Konstante für den Pfad zum Formular zur Erstellung oder Aktualisierung eines Besitzers
	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	// Repository für den Zugriff auf Besitzer-Daten
	private final OwnerRepository owners;

	// Konstruktor zur Injektion des OwnerRepository
	public OwnerController(OwnerRepository clinicService) {
			this.owners = clinicService;
	}

	// Initialisiert den WebDataBinder, um bestimmte Felder von der Bindung auszuschließen
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
			// Verhindert, dass das "id"-Feld von externen Anfragen gebunden wird (Sicherheitsmaßnahme)
			dataBinder.setDisallowedFields("id");
	}

	// Fügt dem Modell ein Attribut hinzu oder lädt es basierend auf der ownerId
	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
			// Wenn keine ownerId vorhanden ist, wird ein neuer Owner erstellt, ansonsten wird er aus der Datenbank geladen
			return ownerId == null ? new Owner() : this.owners.findById(ownerId);
	}

	// Zeigt das Formular zur Erstellung eines neuen Besitzers an
	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
			Owner owner = new Owner();
			model.put("owner", owner); // Fügt einen neuen Owner dem Modell hinzu
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM; // Gibt den Pfad zum Formular zurück
	}

	// Verarbeitet die Einreichung des Formulars zur Erstellung eines neuen Besitzers
	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, RedirectAttributes redirectAttributes) {
			// Überprüft, ob Validierungsfehler aufgetreten sind
			if (result.hasErrors()) {
					redirectAttributes.addFlashAttribute("error", "Es gab einen Fehler bei der Erstellung des Besitzers.");
					return VIEWS_OWNER_CREATE_OR_UPDATE_FORM; // Bei Fehlern wird das Formular erneut angezeigt
			}

			this.owners.save(owner); // Speichert den neuen Besitzer in der Datenbank
			redirectAttributes.addFlashAttribute("message", "Neuer Besitzer erstellt");
			return "redirect:/owners/" + owner.getId(); // Leitet zur Detailseite des neuen Besitzers weiter
	}

	// Zeigt das Suchformular für Besitzer an
	@GetMapping("/owners/find")
	public String initFindForm() {
			return "owners/findOwners"; // Gibt den Pfad zum Suchformular zurück
	}

	// Verarbeitet die Suche nach Besitzern basierend auf dem Nachnamen
	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
					Model model) {

			// Setzt den Nachnamen auf einen leeren String, wenn er nicht gesetzt ist, um alle Ergebnisse anzuzeigen
			if (owner.getLastName() == null) {
					owner.setLastName("");
			}

			// Sucht nach Besitzern mit Paginierung
			Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
			if (ownersResults.isEmpty()) {
					result.rejectValue("lastName", "notFound", "nicht gefunden");
					return "owners/findOwners"; // Bei keinen Ergebnissen wird das Suchformular erneut angezeigt
			}

			if (ownersResults.getTotalElements() == 1) {
					owner = ownersResults.iterator().next();
					return "redirect:/owners/" + owner.getId(); // Bei genau einem Ergebnis wird zur Detailseite weitergeleitet
			}

			// Fügt die Suchergebnisse dem Modell hinzu und zeigt die Liste der Besitzer an
			return addPaginationModel(page, model, ownersResults);
	}

	// Hilfsmethode zum Hinzufügen von Paginierungsinformationen zum Modell
	private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
		List<Owner> listOwners = paginated.getContent(); // Holt die aktuelle Seite der Besitzer
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList"; // Gibt den Pfad zur Liste der Besitzer zurück
	}


	// Hilfsmethode zur Suche nach Besitzern mit Paginierung basierend auf dem Nachnamen
	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
		int pageSize = 5; // Anzahl der Einträge pro Seite
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable); // Führt die Suche durch
	}

	// Zeigt das Formular zur Aktualisierung eines bestehenden Besitzers an
	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
			Owner owner = this.owners.findById(ownerId); // Lädt den Besitzer aus der Datenbank
			model.addAttribute(owner); // Fügt den Besitzer dem Modell hinzu
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM; // Gibt den Pfad zum Formular zurück
	}

	// Verarbeitet die Aktualisierung eines bestehenden Besitzers
	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId,
					RedirectAttributes redirectAttributes) {
			if (result.hasErrors()) {
					redirectAttributes.addFlashAttribute("error", "Es gab einen Fehler bei der Aktualisierung des Besitzers.");
					return VIEWS_OWNER_CREATE_OR_UPDATE_FORM; // Bei Fehlern wird das Formular erneut angezeigt
			}

			owner.setId(ownerId); // Setzt die ID des Besitzers sicherheitshalber
			this.owners.save(owner); // Speichert die Änderungen in der Datenbank
			redirectAttributes.addFlashAttribute("message", "Besitzerdaten aktualisiert");
			return "redirect:/owners/{ownerId}"; // Leitet zur aktualisierten Detailseite weiter
	}

	// Zeigt die Detailseite eines Besitzers an
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
			ModelAndView mav = new ModelAndView("owners/ownerDetails"); // Erstellt ein neues ModelAndView
			Owner owner = this.owners.findById(ownerId); // Lädt den Besitzer aus der Datenbank
			mav.addObject(owner); // Fügt den Besitzer dem ModelAndView hinzu
			return mav; // Gibt das ModelAndView zurück, um die Detailseite anzuzeigen
	}
}
