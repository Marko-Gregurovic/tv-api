package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.dto.ChannelDto;
import fer.hr.tvapi.dto.CreateChannelDto;
import fer.hr.tvapi.entity.Channel;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.BadRequestException;
import fer.hr.tvapi.exception.ForbiddenException;
import fer.hr.tvapi.exception.NotFoundException;
import fer.hr.tvapi.mapper.ChannelMapper;
import fer.hr.tvapi.repository.ChannelRepository;
import fer.hr.tvapi.service.ChannelService;
import fer.hr.tvapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Channel> getAllChannels(Principal principal) {
        return channelRepository.findAll();
    }

    @Override
    public List<ChannelDto> getAllChannelDtos(Principal principal) {
        return ChannelMapper.mapListToChannelDto(this.getAllChannels(principal));
    }

    @Override
    public ChannelDto getChannelDtoById(Long channelId) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);

        if(channelOptional.isEmpty()) {
            throw new NotFoundException(String.format("No channel with id %d", channelId));
        }

        return ChannelMapper.mapToChannelDto(channelOptional.get());
    }

    @Override
    public List<Channel> getAllChannelsForEditor(Principal principal, Long editorId) {
        Optional<Users> optionalUsers = userService.findById(editorId);

        if (optionalUsers.isEmpty()) {
            throw new NotFoundException(String.format("User %d does not exist", editorId));
        }

        Users user = optionalUsers.get();

        if (!user.getRole().getName().equals("editor")) {
            throw new BadRequestException(String.format("User %d is not an editor", editorId));
        }

        return channelRepository.findByUser(user);
    }

    @Override
    public List<ChannelDto> getAllChannelDtosForEditor(Principal principal, Long editorId) {
        return ChannelMapper.mapListToChannelDto(this.getAllChannelsForEditor(principal, editorId));
    }

    @Override
    public void deleteById(Principal principal, Long channelId) {
        Users user = userService.findByEmail(principal.getName()).get();

        if (!user.getRole().getName().equals("editor")) {
            throw new ForbiddenException("Editor role required for channel deletion");
        }

        Optional<Channel> optionalChannel = channelRepository.findById(channelId);

        if (optionalChannel.isEmpty()) {
            throw new NotFoundException(String.format("Channel with id: %d, does not exist", channelId));
        }

        Channel channel = optionalChannel.get();

        if(!channel.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(String.format("Editor with id %d, is not the owner of channel %d, and therefore cannot delete", user.getId(), channelId));
        }

        channelRepository.deleteById(channelId);
    }

    @Override
    public List<Channel> getAllChannelsForAuthenticatedUser(Principal principal) {
        Users user = userService.findByEmail(principal.getName()).get();

        if (!user.getRole().getName().equals("editor")) {
            throw new BadRequestException(String.format("User %d is not an editor", user.getId()));
        }

        return channelRepository.findByUser(user);
    }

    @Override
    public List<ChannelDto> getAllChannelDtosForAuthenticatedUser(Principal principal) {
        return ChannelMapper.mapListToChannelDto(this.getAllChannelsForAuthenticatedUser(principal));
    }
}
