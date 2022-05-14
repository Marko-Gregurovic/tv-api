package fer.hr.tvapi.service;

import fer.hr.tvapi.dto.CreateChannelDto;
import fer.hr.tvapi.entity.Channel;

import java.security.Principal;

public interface ChannelService {

    Channel createChannel(Principal principal, CreateChannelDto channel);

}
