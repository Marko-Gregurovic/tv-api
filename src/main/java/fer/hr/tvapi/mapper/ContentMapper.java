package fer.hr.tvapi.mapper;

import fer.hr.tvapi.constants.DateTimeFormats;
import fer.hr.tvapi.dto.ContentDto;
import fer.hr.tvapi.entity.Content;

import java.util.List;
import java.util.stream.Collectors;

public class ContentMapper {

    public static ContentDto mapToContentDto(Content content) {
        return ContentDto
                .builder()
                .id(content.getId())
                .name(content.getName())
                .description(content.getDescription())
                .startTime(content.getStartTime().format(DateTimeFormats.dateTimeFormat))
                .endTime(content.getEndTime().format(DateTimeFormats.dateTimeFormat))
                .build();
    }

    public static List<ContentDto> mapListToContentDto(List<Content> contentList) {
        return contentList.stream().map(content -> mapToContentDto(content)).collect(Collectors.toList());
    }

}
