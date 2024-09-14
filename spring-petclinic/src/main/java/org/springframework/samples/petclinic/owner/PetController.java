package org.springframework.samples.petclinic.owner;

// Importiert benötigte Klassen und Annotationen
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Markiert die Klasse als Controller in Spring MVC
@Controller
// Definiert das Basis-URL-Mapping für alle Methoden in dieser Klasse
@RequestMapping("/owners/{ownerId}")
class PetController {

	// Konstantenpfad für die View zum Erstellen oder Aktualisieren eines Haustiers
	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	// Repository für Owner-Entitäten, um Datenbankoperationen durchzuführen
	private final OwnerRepository owners;

	// Konstruktor zur Injektion des OwnerRepository
	public PetController(OwnerRepository owners) {
			this.owners = owners;
	}

	// Fügt dem Modell eine Liste von PetType hinzu, um sie in der View zu verwenden
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
			return this.owners.findPetTypes();
	}

	// Sucht einen Owner basierend auf der ownerId aus dem Pfad und fügt ihn dem Modell hinzu
	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {

			Owner owner = this.owners.findById(ownerId);
			if (owner == null) {
					throw new IllegalArgumentException("Owner ID not found: " + ownerId);
			}
			return owner;
	}

	// Sucht ein Pet basierend auf ownerId und optional petId und fügt es dem Modell hinzu
	@ModelAttribute("pet")
	public Pet findPet(@PathVariable("ownerId") int ownerId,
										 @PathVariable(name = "petId", required = false) Integer petId) {

			if (petId == null) {
					// Erstellt ein neues Pet, wenn keine petId vorhanden ist
					return new Pet();
			}

			Owner owner = this.owners.findById(ownerId);
			if (owner == null) {
					throw new IllegalArgumentException("Owner ID not found: " + ownerId);
			}
			// Holt das Pet des Owners basierend auf petId
			return owner.getPet(petId);
	}

	// Initialisiert einen WebDataBinder für Owner-Objekte
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
			// Verhindert das Ändern des 'id'-Feldes
			dataBinder.setDisallowedFields("id");
	}

	// Initialisiert einen WebDataBinder für Pet-Objekte mit einem Validator
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
			// Setzt einen benutzerdefinierten Validator für Pet-Objekte
			dataBinder.setValidator(new PetValidator());
	}

	// Zeigt das Formular zum Erstellen eines neuen Haustiers an
	@GetMapping("/pets/new")
	public String initCreationForm(Owner owner, ModelMap model) {
			Pet pet = new Pet();
			owner.addPet(pet);
			// Fügt das neue Pet dem Modell hinzu
			model.put("pet", pet);
			// Gibt den Pfad zur View zurück
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	// Verarbeitet das Formular zum Erstellen eines neuen Haustiers
	@PostMapping("/pets/new")
	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model,
																		RedirectAttributes redirectAttributes) {
		// Überprüft, ob der Name bereits existiert
		if (StringUtils.hasText(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
				result.rejectValue("name", "duplicate", "already exists");
		}

		// Überprüft, ob das Geburtsdatum in der Zukunft liegt
		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
				result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		owner.addPet(pet);
		if (result.hasErrors()) {
				// Bei Fehlern wird das Formular erneut angezeigt
				model.put("pet", pet);
				return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		// Speichert den Owner und damit auch das neue Pet
		this.owners.save(owner);
		// Fügt eine Flash-Nachricht hinzu
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		// Weiterleitung zur Owner-Detailseite
		return "redirect:/owners/{ownerId}";
	}

	// Zeigt das Formular zum Bearbeiten eines bestehenden Haustiers an
	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm(Owner owner, @PathVariable("petId") int petId, ModelMap model,
															 RedirectAttributes redirectAttributes) {
			Pet pet = owner.getPet(petId);
			// Fügt das zu bearbeitende Pet dem Modell hinzu
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}


	// Verarbeitet das Formular zum Aktualisieren eines bestehenden Haustiers
	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, ModelMap model,
																	RedirectAttributes redirectAttributes) {

		String petName = pet.getName();

		// Überprüft, ob der Pet-Name bereits existiert und nicht dem aktuellen Pet gehört
		if (StringUtils.hasText(petName)) {
				Pet existingPet = owner.getPet(petName.toLowerCase(), false);
				if (existingPet != null && existingPet.getId() != pet.getId()) {
						result.rejectValue("name", "duplicate", "already exists");
				}
		}

		// Überprüft, ob das Geburtsdatum in der Zukunft liegt
		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
				result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
				// Bei Fehlern wird das Formular erneut angezeigt
				model.put("pet", pet);
				return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		owner.addPet(pet);
		this.owners.save(owner);
		// Fügt eine Flash-Nachricht hinzu
		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return "redirect:/owners/{ownerId}";
	}
}