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

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicoService.class)
public class MedicoServiceTest {
	@Autowired
	private MedicoService medicoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<MedicoEntity> medicoList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}


	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			MedicoEntity medicoEntity = factory.manufacturePojo(MedicoEntity.class);
			entityManager.persist(medicoEntity);
			medicoList.add(medicoEntity);
		}
	}
	
	@Test
	void testCreateAuthor() throws IllegalOperationException {
		MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
		newEntity.setRegistroMedico("RM3257");
		MedicoEntity result = medicoService.createMedico(newEntity);
		assertNotNull(result);

		MedicoEntity entity = entityManager.find(MedicoEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getApellido(), entity.getApellido());
		assertEquals(newEntity.getRegistroMedico(), entity.getRegistroMedico());
	}
	
	@Test
	void testCreateMedicoRegistroMedicoInvalido() {
		assertThrows(IllegalOperationException.class, ()->{
			MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
			String rm = "4356278";
			newEntity.setRegistroMedico(rm);
			medicoService.createMedico(newEntity);
		});
	}
	

}
