package org.uhanov.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerConfig {

    @ExceptionHandler({DbConnectionException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDbConnectionException(final Exception e) {
        log.error("ОШИБКА ПРИ ПОДКЛЮЧЕНИИ К БАЗЕ ДАННЫХ\n" + exceptionToString(e));
        return "Внутренняя ошибка сервера";
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(final Exception e) {
        log.warn(
                "Запрос не существующего ресурса:" + e.getMessage()
        );
        return "Сущность требуемая для выполнения запроса не была найдена" + e.getMessage();

    }

    @ExceptionHandler({MoneyTransferException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMoneyTransferException(final Exception e) {
        log.warn(
                "Ошибка при перводе средств:" + e.getMessage()
        );
        return "На счету у пользователя не достаточно средств для перевода" + e.getMessage();
    }

    @ExceptionHandler({PurchaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlePurchaseException(final Exception e) {
        log.warn(
                "Ошибка при совершение покупки:" + e.getMessage()
        );
        return "На счету у пользователя не достаточно средств для покупки" + e.getMessage();
    }

    @ExceptionHandler({SignUpException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleSignUpException(final Exception e) {
        log.warn(
                "Ошибка создания аккаунта" + e.getMessage()
        );
        return "Ошибка создания аккаунта" + e.getMessage();
    }

    @ExceptionHandler({InvalidRoleException.class, InvalidLoginException.class, InvalidTokenException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthException(final Exception e) {
        log.warn(
                "Ошибка аутентификации:" + e.getMessage()
        );
        return "Ошибка аутентификации" + e.getMessage();
    }

    private String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
