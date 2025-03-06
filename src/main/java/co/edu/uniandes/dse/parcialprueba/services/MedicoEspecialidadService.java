package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Optional;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MedicoEspecialidadService {
	@Autowired
	MedicoRepository medicoRepository;
	
	@Autowired
	EspecialidadRepository especialidadRepository;
	
	
	@Transactional
	public EspecialidadEntity addEspecialidad(Long idMedico, Long idEspecialidad) throws EntityNotFoundException {
		log.info("Inicio de asociación entre una especialidad al médico con id = {0}",idMedico);
		Optional<MedicoEntity> medicoEntity = medicoRepository.findById(idMedico);
		Optional<EspecialidadEntity> especialidadEntity = especialidadRepository.findById(idEspecialidad);

		if (medicoEntity.isEmpty())
			throw new EntityNotFoundException("No hay un medico asociado con el ID ingresado");

		if (especialidadEntity.isEmpty())
			throw new EntityNotFoundException("No hay una especialidad asociada con el ID ingresado");

		especialidadEntity.get().getMedicos().add(medicoEntity.get());
		log.info("Termina  la asociación entre una especialidad al médico con id = {0}",idMedico);
		return especialidadEntity.get();
		}

}
