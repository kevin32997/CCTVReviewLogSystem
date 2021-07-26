package dev.zndev.reviewlogger.models.others;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response {

    private String message;
    private boolean status;
    private List<?> list;

}
