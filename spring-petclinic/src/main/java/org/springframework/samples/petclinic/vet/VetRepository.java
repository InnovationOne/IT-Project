package org.springframework.samples.petclinic.vet;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

// Das VetRepository ist ein Interface, das den Datenzugriff für Vet-Entitäten ermöglicht.
// Es erweitert das Repository-Interface von Spring Data und definiert Methoden, um alle Tierärzte abzurufen, entweder als Liste oder paginiert.
public interface VetRepository extends Repository<Vet, Integer> {
	// Ruft alle Tierärzte aus der Datenbank ab. Die Ergebnisse werden im Cache unter dem Schlüssel "vets" gespeichert, um die Leistung zu verbessern.
	@Transactional(readOnly = true)
	@Cacheable("vets")
	Collection<Vet> findAll() throws DataAccessException;

	// Ruft eine paginierte Liste von Tierärzten ab. Die Ergebnisse werden im Cache unter dem Schlüssel "vets" gespeichert, um die Leistung zu verbessern.
	@Transactional(readOnly = true)
	@Cacheable("vets")
	Page<Vet> findAll(Pageable pageable) throws DataAccessException;

}
