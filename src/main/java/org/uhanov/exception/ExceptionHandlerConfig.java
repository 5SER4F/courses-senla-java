package org.uhanov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler({DbConnectionException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDbConnectionException(final Exception e) {
        return "Внутренняя ошибка сервера";
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(final ResourceNotFoundException e) {
        return "Сущность требуемая для выполнения запроса не была найдена" + e.getMessage();

    }

    @ExceptionHandler({MoneyTransferException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMoneyTransferException(final Exception e) {
        return "На счету у пользователя не достаточно средств для перевода" + e.getMessage();
    }

    @ExceptionHandler({PurchaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlePurchaseException(final Exception e) {
        return "На счету у пользователя не достаточно средств для покупки" + e.getMessage();
    }

    @ExceptionHandler({InvalidRoleException.class, InvalidLoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthException(final Exception e) {
        return "Ошибка аутентификации" + e.getMessage();
    }
}
