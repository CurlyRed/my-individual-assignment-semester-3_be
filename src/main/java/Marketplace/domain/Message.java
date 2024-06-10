package Marketplace.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private long id;
    private Chat chat;
    private User sender;
    private String content;
    private LocalDateTime timeStamp;
}
