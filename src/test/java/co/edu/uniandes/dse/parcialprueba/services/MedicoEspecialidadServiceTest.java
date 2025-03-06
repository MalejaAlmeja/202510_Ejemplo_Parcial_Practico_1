package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ MedicoEspecialidadService.class, EspecialidadService.class })
public class MedicoEspecialidadServiceTest {
	@Autowired
	private MedicoEspecialidadService medicoEspecialidadService;
	@Autowired
	private EspecialidadService especialidadService;
	@Autowired
	private TestEntityManager entityManager;
	
	private PodamFactory factory = new PodamFactoryImpl();

	private MedicoEntity medico = new MedicoEntity();
	private List<EspecialidadEntity> especialidadList = new ArrayList<>();
	
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}
	
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
	}
	
	
	private void insertData() {
		medico = factory.manufacturePojo(MedicoEntity.class);
		entityManager.persist(medico);

		for (int i = 0; i < 3; i++) {
			EspecialidadEntity entity = factory.manufacturePojo(EspecialidadEntity.class);
			entity.getMedicos().add(medico);
			entityManager.persist(entity);
			especialidadList.add(entity);
			medico.getEspecialidades().add(entity);
		}
	}
	
	@Test
	void testAddEspecialidad() throws EntityNotFoundException, IllegalOperationException{
		EspecialidadEntity nuevaEspecialidad = factory.manufacturePojo(EspecialidadEntity.class);
		especialidadService.createEspecialidad(nuevaEspecialidad);
		
		EspecialidadEntity especialidadEntity = medicoEspecialidadService.addEspecialidad(medico.getId(), nuevaEspecialidad.getId());
		assertNotNull(especialidadEntity);
		
		assertEquals(especialidadEntity.getId(), nuevaEspecialidad.getId());
		assertEquals(especialidadEntity.getNombre(), nuevaEspecialidad.getNombre());
		assertEquals(especialidadEntity.getDescripcion(), nuevaEspecialidad.getDescripcion());
	}
	
	@Test
	void testAddEspecialidadMedicoInvalido() {
		assertThrows(EntityNotFoundException.class, () -> {
			EspecialidadEntity newBook = factory.manufacturePojo(EspecialidadEntity.class);
			entityManager.persist(newBook);
			medicoEspecialidadService.addEspecialidad(0L, newBook.getId());
		});
	}
	
	@Test
	void testAddEspecialidadEspecialidadInvalida() {
		assertThrows(EntityNotFoundException.class, () -> {
			medicoEspecialidadService.addEspecialidad(medico.getId(), 0L);
		});
	}

}
