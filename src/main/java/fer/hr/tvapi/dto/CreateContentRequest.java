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
public class CreateContentRequest {

    private Long channelId;

    private List<ContentCreateDto> contentList;

}
