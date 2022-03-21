package mx.uam.etl.backend.errormanagement;

public class DabataBaseNotGeneratedException extends IllegalArgumentException {

	private static final long serialVersionUID = 1546749534567007391L;

	public DabataBaseNotGeneratedException(String message) {
		super(message);
	}

	public DabataBaseNotGeneratedException() {
		super("No se ha podido Generar la Base de Datos. Verifique que los archivos cargados cumplan con la estructura correcta para la ingesta de datos.");
	}
	
	
}
