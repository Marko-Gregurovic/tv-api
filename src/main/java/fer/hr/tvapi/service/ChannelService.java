package fer.hr.tvapi.service;

import fer.hr.tvapi.dto.ChannelDto;
import fer.hr.tvapi.dto.CreateChannelDto;
import fer.hr.tvapi.entity.Channel;

import java.security.Principal;
import java.util.List;

public interface ChannelService {

    Channel createChannel(Principal principal, CreateChannelDto channel);

    List<Channel> getAllChannels(Principal principal);

    List<ChannelDto> getAllChannelDtos(Principal principal);

}
