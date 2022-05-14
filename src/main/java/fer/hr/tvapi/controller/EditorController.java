package fer.hr.tvapi.controller;

import fer.hr.tvapi.dto.ChannelDto;
import fer.hr.tvapi.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/editors")
public class EditorController {

    private final ChannelService channelService;

    @Autowired
    public EditorController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/{editorId}/channels")
    public ResponseEntity<List<ChannelDto>> getAllChannelsOfEditor(Principal principal, @PathVariable @NotNull Long editorId){
        return ResponseEntity.ok(channelService.getAllChannelDtosForEditor(principal, editorId));
    }

}
