package org.formacio.servei;

import java.util.Optional;

import javax.transaction.Transactional;

import org.formacio.domain.Factura;
import org.formacio.domain.LiniaFactura;
import org.formacio.repositori.FacturesRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FacturesService {

	@Autowired
	FacturesRepositori facturasRepo;
	
	@Autowired
	FidalitzacioService fidalitzacioService;
	
	/*
	 * Aquest metode ha de carregar la factura amb id idFactura i afegir una nova linia amb les dades
	 * passades (producte i totalProducte)
	 * 
	 * S'ha de retornar la factura modificada
	 * 
	 * Per implementar aquest metode necessitareu una referencia (dependencia) a FacturesRepositori
	 */
	public Factura afegirProducte (long idFactura, String producte, int totalProducte) {
	
		Optional<Factura> factura = facturasRepo.findById(idFactura);
		if(factura.isPresent()) {
			LiniaFactura liniaFactura = new LiniaFactura();
			liniaFactura.setProducte(producte);
			liniaFactura.setTotal(totalProducte);
			factura.get().getLinies().add(liniaFactura);
			facturasRepo.save(factura.get());
			
			
			if(factura.get().getLinies().size() >= 4) {
				String email = factura.get().getClient().getEmail();
				fidalitzacioService.notificaRegal(email);
			}
			return factura.get();
			
		} else {
			return null;
		}

		
	}
}
