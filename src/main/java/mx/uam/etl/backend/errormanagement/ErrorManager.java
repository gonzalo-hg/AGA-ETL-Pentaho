package mx.uam.etl.backend.errormanagement;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorManager {

	private long totalErrors = 0;

	public long getTotalErrors() {
		return totalErrors;
	}

	public HttpStatus getHttpStatusForException(Exception e) {

		logError(e);
		
		if (e instanceof IllegalArgumentException || e instanceof DabataBaseNotGeneratedException) {
			return HttpStatus.BAD_REQUEST;
		}

		/*if (e instanceof OperationNotFoundException || e instanceof InvestigationNotFoundException || e instanceof ObjectNotFoundException) {
			return HttpStatus.NOT_FOUND;
		}
		
		if (e instanceof EnumConstantNotPresentException) {
			return HttpStatus.BAD_REQUEST;
		}
		
		if (e instanceof DateTimeParseException) {
			return HttpStatus.BAD_REQUEST;
		}*/

		return HttpStatus.INTERNAL_SERVER_ERROR;

	}

	public void logError(Exception e) {
		totalErrors++;
		log.error(e.getMessage(), e);
	}

	public void logError(Exception e, String message) {
		totalErrors++;
		log.error(message, e);
	}

}
