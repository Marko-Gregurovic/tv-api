package fer.hr.tvapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChannelDto {

    private Long id;

    private String name;

    private String description;

    private String logo;

    private List<ContentDto> contentList;

}
