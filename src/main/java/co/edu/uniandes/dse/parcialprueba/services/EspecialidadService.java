package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EspecialidadService {
	@Autowired
	EspecialidadRepository especialidadRepository;
	
	@Transactional
	public EspecialidadEntity createEspecialidad(EspecialidadEntity especialidad) throws IllegalOperationException {
		log.info("Creación de la especialidad");
		if(especialidad.getDescripcion().length()<10) {
			throw new IllegalOperationException("La descripción debe tener más de 10 caracteres.");
		}
		
		return especialidadRepository.save(especialidad);
		
	}
	

}
