package fer.hr.tvapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateChannelDto {

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1)
    private String description;

    private String logo;

}
