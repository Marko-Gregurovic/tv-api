package fer.hr.tvapi.controller;

import fer.hr.tvapi.dto.*;
import fer.hr.tvapi.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/channels")
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

    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAllChannels(Principal principal) {
        return ResponseEntity.ok(channelService.getAllChannelDtos(principal));
    }

    @GetMapping("/search/{channelName}")
    public ResponseEntity<List<ChannelDto>> getAllChannels(Principal principal, @PathVariable String channelName) {
        return ResponseEntity.ok(channelService.searchChannels(principal, channelName));
    }

    @GetMapping("/byId/{channelId}")
    public ResponseEntity<ChannelDto> getChannelById(Principal principal, @PathVariable @NotNull Long channelId) {
        return ResponseEntity.ok(channelService.getChannelDtoById(channelId));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannelById(Principal principal, @PathVariable @NotNull Long channelId) {
        channelService.deleteById(principal, channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owned")
    public ResponseEntity<List<ChannelDto>> getAllOwnedChannels(Principal principal) {
        return ResponseEntity.ok(channelService.getAllChannelDtosForAuthenticatedUser(principal));
    }

    @PostMapping("{channelId}/contents")
    public ResponseEntity<List<ContentDto>> createChannelContents(Principal principal, @PathVariable @NotNull Long channelId, @RequestBody List<ContentCreateDto> createChannelDtoList) {
        List<ContentDto> channelDtoList = channelService.createChannelContents(principal, createChannelDtoList, channelId);
        return ResponseEntity.status(HttpStatus.CREATED).body(channelDtoList);
    }

}
