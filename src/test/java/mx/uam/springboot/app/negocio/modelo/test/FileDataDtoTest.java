package mx.uam.springboot.app.negocio.modelo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import mx.uam.springboot.app.negocio.modelo.FileDataDto;

class FileDataDtoTest {

	/**
	 * Prueba que los setters y getters generados por lombok estén habilitados
	 */
	@Test
	void isLombokWorkingTest() {
		FileDataDto fileDataDto = new FileDataDto();
		fileDataDto.setKey("llavePrueba");
		fileDataDto.setValue("valorPrueba");
		String expectedKey = "llavePrueba";
		String expectedValue = "valorPrueba";
		assertEquals(fileDataDto.getKey(), expectedKey, "Los métodos getters y setters de la clase FileDataDto no funcionan. Sugerencia: Agregue la anotación @Data en la clase");
		assertEquals(fileDataDto.getValue(), expectedValue, "Los métodos getters y setters de la clase FileDataDto no funcionan. Sugerencia: Agregue la anotación @Data en la clase");
	}

}
