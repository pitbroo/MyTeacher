package pl.pbrodziak.education.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {

    private String instructorId;
    private String courseName;
    private String price;
    private String category;

    public CourseDto() {
    }
}
