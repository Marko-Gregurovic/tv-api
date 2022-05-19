package fer.hr.tvapi.service;

import fer.hr.tvapi.dto.*;
import fer.hr.tvapi.entity.Channel;

import java.security.Principal;
import java.util.List;

public interface ChannelService {

    Channel createChannel(Principal principal, CreateChannelDto channel);

    List<Channel> getAllChannels(Principal principal);

    List<ChannelDto> getAllChannelDtos(Principal principal);

    ChannelDto getChannelDtoById(Long channelId);

    List<Channel> getAllChannelsForEditor(Principal principal, Long editorId);
    List<ChannelDto> getAllChannelDtosForEditor(Principal principal, Long editorId);

    void deleteById(Principal principal, Long channelId);
    List<Channel> getAllChannelsForAuthenticatedUser(Principal principal);
    public List<ChannelDto> getAllChannelDtosForAuthenticatedUser(Principal principal);

    List<ContentDto> createChannelContents(Principal principal, List<ContentCreateDto> contentCreateRequestList, Long channelId);

    ContentDto updateContent(Principal principal, ContentCreateDto contentCreateDto, Long contentId);

    void deleteContentById(Principal principal, Long contentId);

    List<ChannelDto> searchChannels(Principal principal, String channelName);
}
