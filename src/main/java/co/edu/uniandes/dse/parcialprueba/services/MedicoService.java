package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoService {
	@Autowired
	MedicoRepository medicoRepository;
	
	@Transactional
	public MedicoEntity createMedico(MedicoEntity medico) throws IllegalOperationException {
		log.info("Creación del médico");
		if(!medico.getRegistroMedico().startsWith("RM")) {
			throw new IllegalOperationException("El registro médico debe comenzar por RM");
		}
		
		return medicoRepository.save(medico);
		
	}
	
}
