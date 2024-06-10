package Marketplace.business.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private Long chatId;
    private long senderId;
    private String content;

    //Relevant only for the first message request when creating a first chat object
    private long buyerId;
    private long sellerId;
    private long productId;
}

