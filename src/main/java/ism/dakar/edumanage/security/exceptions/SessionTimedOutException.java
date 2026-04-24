package ism.dakar.edumanage.security.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@AllArgsConstructor
@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class SessionTimedOutException extends  RuntimeException {
   private  String message;
}
