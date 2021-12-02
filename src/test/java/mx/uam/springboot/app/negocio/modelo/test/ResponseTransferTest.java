package mx.uam.springboot.app.negocio.modelo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import mx.uam.springboot.app.negocio.modelo.ResponseTransfer;

class ResponseTransferTest {

	/**
	 * Prueba que los setters y getters generados por lombok estén habilitados
	 */
	@Test
	void isLombokWorkingTest() {
		ResponseTransfer responseTransfer = new ResponseTransfer("cuerpoPrueba");
		String expectedResponseBody = "cuerpoPrueba";
		assertEquals(responseTransfer.getResponseBody(), expectedResponseBody, "Los métodos getters y setters de la clase ResponseTransfer no funcionan. Sugerencia: Agregue la anotación @Data en la clase");
		responseTransfer.setResponseBody("cuerpoNuevo");
		expectedResponseBody = "cuerpoNuevo";
		assertEquals(responseTransfer.getResponseBody(), expectedResponseBody, "Los métodos getters y setters de la clase ResponseTransfer no funcionan. Sugerencia: Agregue la anotación @Data en la clase");
	}

}
