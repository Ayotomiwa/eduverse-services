package dev.captain.chatservice.controller;


import dev.captain.chatservice.model.Channel;
import dev.captain.chatservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/chat-service/")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final RestTemplate restTemplate;


    @GetMapping("modules/{module-id}/channels")
    public ResponseEntity<?> getChannels(@PathVariable("module-id") String moduleId) {
        return ResponseEntity.ok(channelService.getChannels(moduleId));
    }


    @PostMapping("modules/{module-id}/channels")
    public ResponseEntity<?> createChannel(@PathVariable("module-id") String moduleId,
                                           @RequestBody Channel channel,
                                           @RequestParam Long userId) {
        ResponseEntity<String> access = restTemplate.exchange(
                "https://user-service-dgrsoybfsa-ew.a.run.app/api/user-service/modules/" + moduleId + "/authorized?userId=" + userId,
                HttpMethod.GET,
                null,
                String.class
        );

        if (access.getStatusCode().isError()) {
            return ResponseEntity.badRequest().body(access.getBody());
        }
        return ResponseEntity.ok(channelService.createChannel(moduleId, channel));
    }

    @GetMapping("channels/{channel-id}")
    public ResponseEntity<?> getChannel(@PathVariable("channel-id") String channelId) {

        Channel channel = channelService.getChannel(channelId);
        if (channel == null) {
            return ResponseEntity.badRequest().body("Channel does not exist");
        }

        return ResponseEntity.ok(channelService.getChannel(channelId));
    }


    @GetMapping("channels/{channel-id}/messages")
    public ResponseEntity<?> getChannelMessages(@PathVariable("channel-id") String channelId) {
        if(channelId == null){
            return ResponseEntity.badRequest().body("Channel Id cannot be empty");
        }
        return ResponseEntity.ok(channelService.getChannelMessages(channelId));
    }

}
