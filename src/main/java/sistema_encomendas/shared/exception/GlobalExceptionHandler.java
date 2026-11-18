package sistema_encomendas.shared.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail naoEncontrado(RecursoNaoEncontradoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Falhas de validação dos DTOs (@Valid). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(e -> e.getField(), e -> e.getDefaultMessage(), (a, b) -> a));
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos");
        problema.setProperty("erros", erros);
        return problema;
    }

    /** Regras de negócio lançadas pelas entidades. */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ProblemDetail regraDeNegocio(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
}
