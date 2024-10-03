package programmer.restful.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchContactRequest {

    private String name;

    private String phone;

    private String email;

    @NotNull
    private Integer size;

    @NotNull
    private Integer page;

}
