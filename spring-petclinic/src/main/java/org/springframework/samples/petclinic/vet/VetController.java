package org.springframework.samples.petclinic.vet;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// Der VetController ist ein Spring MVC Controller, der HTTP-Anfragen im Zusammenhang mit Tierärzten verarbeitet.
@Controller
class VetController {

	private final VetRepository vetRepository;

	// Konstruktor-Injektion des VetRepository
	// Das Repository wird verwendet, um auf Tierarzt-Daten zuzugreifen.
	public VetController(VetRepository clinicService) {
		this.vetRepository = clinicService;
	}

	// Behandelt GET-Anfragen an "/vets.html".
	// Zeigt eine paginierte Liste der Tierärzte an.
	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Erzeugt eine Instanz von Vets, die eine Liste von Tierärzten enthält
		Vets vets = new Vets();
		// Holt die paginierte Liste von Tierärzten für die angegebene Seite
		Page<Vet> paginated = findPaginated(page);
		// Fügt die Tierärzte zur Vets-Liste hinzu
		vets.getVetList().addAll(paginated.toList());
		// Fügt Paginierungsinformationen und die Liste der Tierärzte dem Modell hinzu
		return addPaginationModel(page, paginated, model);
	}

	// Fügt dem Modell Paginierungsinformationen und die Liste der Tierärzte hinzu
	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		// Holt den Inhalt (Liste von Tierärzten) aus der paginierten Seite
		List<Vet> listVets = paginated.getContent();
		// Fügt die aktuelle Seite zum Modell hinzu
		model.addAttribute("currentPage", page);
		// Fügt die Gesamtanzahl der Seiten zum Modell hinzu
		model.addAttribute("totalPages", paginated.getTotalPages());
		// Fügt die Gesamtanzahl der Elemente (Tierärzte) zum Modell hinzu
		model.addAttribute("totalItems", paginated.getTotalElements());
		// Fügt die Liste der Tierärzte zum Modell hinzu
		model.addAttribute("listVets", listVets);
		// Gibt den Namen der View zurück
		return "vets/vetList";
	}

	// Findet eine paginierte Liste von Tierärzten für die angegebene Seite.
	private Page<Vet> findPaginated(int page) {
		int pageSize = 5; // Anzahl der Elemente pro Seite
		// Erstellt ein Pageable-Objekt mit der aktuellen Seite und der Seitengröße
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		// Ruft die paginierte Liste von Tierärzten aus dem Repository ab
		return vetRepository.findAll(pageable);
	}

	// Behandelt GET-Anfragen an "/vets".
	// Gibt eine Liste von Tierärzten als JSON oder XML zurück.
	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Erzeugt eine neue Instanz von Vets
		Vets vets = new Vets();
		// Fügt alle Tierärzte aus dem Repository zur Vets-Liste hinzu
		vets.getVetList().addAll(this.vetRepository.findAll());
		// Gibt das Vets-Objekt zurück, das automatisch in JSON oder XML serialisiert wird
		return vets;
	}
}
