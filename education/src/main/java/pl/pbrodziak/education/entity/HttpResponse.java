package pl.pbrodziak.education.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpResponse {
    private int httpStatusCode; //200,201,400
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
