package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.dto.CreateChannelDto;
import fer.hr.tvapi.entity.Channel;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.BadRequestException;
import fer.hr.tvapi.exception.ForbiddenException;
import fer.hr.tvapi.repository.ChannelRepository;
import fer.hr.tvapi.service.ChannelService;
import fer.hr.tvapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    @Autowired
    public ChannelServiceImpl(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public Channel createChannel(Principal principal, CreateChannelDto createChannelDto) {
        Users user = userService.findByEmail(principal.getName()).get();

        if(!user.getRole().getName().equals("editor")) {
            throw new ForbiddenException("Editor role required for channel creation");
        }

        if(channelRepository.findByName(createChannelDto.getName()).isPresent()) {
            throw new BadRequestException(String.format("Channel with name %s already exists", createChannelDto.getName()));
        }

        Channel channel = Channel.builder()
                .name(createChannelDto.getName())
                .description(createChannelDto.getDescription())
                .logoBase64(createChannelDto.getLogo())
                .user(user)
                .build();

        return channelRepository.save(channel);
    }
}
