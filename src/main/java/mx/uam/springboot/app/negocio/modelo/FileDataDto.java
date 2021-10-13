package mx.uam.springboot.app.negocio.modelo;

import lombok.Data;

/**
 * 
 * Objeto de transferencia de datos que mapea la información recibida del frontend acerca de los archivos que ha subido el usuario.
 */
@Data
public class FileDataDto {
	private String key; //El nombre clave que hemos asignado al archivo, p.ej: "AGA", "Historial de exámenes de recuperación", etc.
	private String value; //el nombre real del archivo que ha subido el usuario, p.ej: "aga_lic_2021I_izt_4a_sem.DBF".
}
