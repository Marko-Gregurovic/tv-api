package fer.hr.tvapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentDto {

    private Long id;

    private String name;

    private String description;

    private String startTime;

    private String endTime;

}
