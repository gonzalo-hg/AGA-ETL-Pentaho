package mx.uam.etl.backend.business.model;

import lombok.Data;

/**
 * 
 * Este es un DTO utilizado en los controladores para mapear y devolver un ResponseBody personalizado
 *
 */
@Data
public class ResponseTransfer {
	private String responseBody;

	public ResponseTransfer(String text) {
		super();
		this.responseBody = text;
	} 
}
