package fer.hr.tvapi.mapper;

import fer.hr.tvapi.dto.ChannelDto;
import fer.hr.tvapi.dto.ContentDto;
import fer.hr.tvapi.entity.Channel;
import fer.hr.tvapi.entity.Content;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelMapper {

    public static ChannelDto mapToChannelDto (Channel channel) {
        return ChannelDto
                .builder()
                .id(channel.getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .logo(channel.getLogoBase64())
                .contentList(ContentMapper.mapListToContentDto(channel.getContentList()))
                .build();
    }

    public static List<ChannelDto> mapListToChannelDto(List<Channel> channelList) {
        return channelList.stream().map(channel -> mapToChannelDto(channel)).collect(Collectors.toList());
    }

}
