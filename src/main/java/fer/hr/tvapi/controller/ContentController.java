package fer.hr.tvapi.controller;

import fer.hr.tvapi.dto.ContentCreateDto;
import fer.hr.tvapi.dto.ContentDto;
import fer.hr.tvapi.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ChannelService channelService;

    @Autowired
    public ContentController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<ContentDto> putContent(Principal principal, @PathVariable Long contentId, @RequestBody ContentCreateDto contentCreateDto) {
        return ResponseEntity.ok(channelService.updateContent(principal, contentCreateDto, contentId));
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> deleteContent(Principal principal, @PathVariable Long contentId) {
        channelService.deleteContentById(principal, contentId);

        return ResponseEntity.noContent().build();
    }

}
