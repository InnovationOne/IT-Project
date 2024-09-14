package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

// Die Klasse Vets dient als Wrapper für eine Liste von Vet-Objekten.
// Sie wird verwendet, um die Liste der Tierärzte in XML oder JSON zu serialisieren, insbesondere bei der Bereitstellung von RESTful APIs.
@XmlRootElement
public class Vets {

	private List<Vet> vets;

	// Gibt die Liste der Tierärzte zurück.
	// Initialisiert die Liste bei Bedarf.
	@XmlElement
	public List<Vet> getVetList() {
		if (vets == null) {
			vets = new ArrayList<>();
		}
		return vets;
	}

}
