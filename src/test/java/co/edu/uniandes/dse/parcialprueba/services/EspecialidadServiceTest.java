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
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EspecialidadService.class)
public class EspecialidadServiceTest {
	@Autowired
	private EspecialidadService especialidadService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<EspecialidadEntity> especialidadList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}


	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			EspecialidadEntity especialidadEntity = factory.manufacturePojo(EspecialidadEntity.class);
			entityManager.persist(especialidadEntity);
			especialidadList.add(especialidadEntity);
		}
	}
	
	@Test
	void testCreateAuthor() throws IllegalOperationException {
		EspecialidadEntity newEntity = factory.manufacturePojo(EspecialidadEntity.class);
		
		EspecialidadEntity result = especialidadService.createEspecialidad(newEntity);
		assertNotNull(result);

		EspecialidadEntity entity = entityManager.find(EspecialidadEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
	}
	
	@Test
	void testCreateEspecialidadDescripcionInvalido() {
		assertThrows(IllegalOperationException.class, ()->{
			EspecialidadEntity newEntity = factory.manufacturePojo(EspecialidadEntity.class);
			String descripcion = "pesimo";
			newEntity.setDescripcion(descripcion);
			especialidadService.createEspecialidad(newEntity);
		});
	}
	

}
