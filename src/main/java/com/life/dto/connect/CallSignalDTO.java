package com.life.dto.connect;

/**
 * Generic WebRTC signaling DTO.
 * type: CALL_OFFER | CALL_ANSWER | CALL_REJECT | ICE_CANDIDATE | CALL_END
 * callType: "video" | "audio"
 * payload: SDP offer/answer string or ICE candidate JSON string
 */
public class CallSignalDTO {

    private String type;       // CALL_OFFER, CALL_ANSWER, CALL_REJECT, ICE_CANDIDATE, CALL_END
    private String callType;   // "video" or "audio"
    private Long senderId;
    private Long receiverId;
    private Long conversationId;
    private String senderName;
    private String payload;    // SDP string or ICE JSON string

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCallType() { return callType; }
    public void setCallType(String callType) { this.callType = callType; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
}
