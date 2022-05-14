package fer.hr.tvapi.controller;

import fer.hr.tvapi.dto.ChannelDto;
import fer.hr.tvapi.dto.CreateChannelDto;
import fer.hr.tvapi.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping
    public ResponseEntity<ChannelDto> createChannel(
            Principal principal,
            @RequestBody(required = true) CreateChannelDto createChannelDto) {
        String userEmail = principal.getName();

        channelService.createChannel(principal, createChannelDto);

        return ResponseEntity.ok().build();
    }

}
