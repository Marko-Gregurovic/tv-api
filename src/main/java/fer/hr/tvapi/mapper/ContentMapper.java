package fer.hr.tvapi.mapper;

import fer.hr.tvapi.constants.DateTimeFormats;
import fer.hr.tvapi.dto.ContentDto;
import fer.hr.tvapi.dto.ContentCreateDto;
import fer.hr.tvapi.entity.Channel;
import fer.hr.tvapi.entity.Content;
import fer.hr.tvapi.repository.CategoryRepository;

import java.time.LocalDateTime;
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
                .category(content.getCategory().getName())
                .build();
    }

    public static List<ContentDto> mapListToContentDto(List<Content> contentList) {
        return contentList.stream().map(content -> mapToContentDto(content)).collect(Collectors.toList());
    }

    public static Content mapFromContentCreateDto(ContentCreateDto contentCreateDto, CategoryRepository categoryRepository, Channel channel) {
        return Content.builder()
                .name(contentCreateDto.getName())
                .description(contentCreateDto.getDescription())
                .channel(channel)
                .category(categoryRepository.findByName(contentCreateDto.getCategory()))
                .endTime(LocalDateTime.parse(contentCreateDto.getEndTime()))
                .startTime(LocalDateTime.parse(contentCreateDto.getStartTime()))
                .build();
    }

    public static List<Content> mapFromContentCreateDtoList(List<ContentCreateDto> contentCreateDtoList, CategoryRepository categoryRepository, Channel channel) {
        return contentCreateDtoList.stream()
                .map(contentCreateDto -> mapFromContentCreateDto(contentCreateDto, categoryRepository, channel))
                .collect(Collectors.toList());
    }
}
