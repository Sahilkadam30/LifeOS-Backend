package com.life.controller.connect;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.life.dto.connect.CallSignalDTO;
import com.life.dto.connect.TypingDTO;

@Controller
public class ConnectWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public ConnectWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // ── Typing indicator ──────────────────────────────────────────────
    @MessageMapping("/typing")
    public void typing(TypingDTO data) {
        messagingTemplate.convertAndSend(
                "/topic/user/" + data.getReceiverId(),
                data
        );
    }

    // ── WebRTC: incoming call offer ───────────────────────────────────
    @MessageMapping("/call/offer")
    public void callOffer(CallSignalDTO signal) {
        signal.setType("CALL_OFFER");
        messagingTemplate.convertAndSend(
                "/topic/user/" + signal.getReceiverId(),
                signal
        );
    }

    // ── WebRTC: callee answers ────────────────────────────────────────
    @MessageMapping("/call/answer")
    public void callAnswer(CallSignalDTO signal) {
        signal.setType("CALL_ANSWER");
        messagingTemplate.convertAndSend(
                "/topic/user/" + signal.getReceiverId(),
                signal
        );
    }

    // ── WebRTC: callee rejects ────────────────────────────────────────
    @MessageMapping("/call/reject")
    public void callReject(CallSignalDTO signal) {
        signal.setType("CALL_REJECT");
        messagingTemplate.convertAndSend(
                "/topic/user/" + signal.getReceiverId(),
                signal
        );
    }

    // ── WebRTC: ICE candidate exchange ────────────────────────────────
    @MessageMapping("/call/ice")
    public void iceCandidate(CallSignalDTO signal) {
        signal.setType("ICE_CANDIDATE");
        messagingTemplate.convertAndSend(
                "/topic/user/" + signal.getReceiverId(),
                signal
        );
    }

    // ── WebRTC: end / hang-up ─────────────────────────────────────────
    @MessageMapping("/call/end")
    public void callEnd(CallSignalDTO signal) {
        signal.setType("CALL_END");
        messagingTemplate.convertAndSend(
                "/topic/user/" + signal.getReceiverId(),
                signal
        );
    }
}
