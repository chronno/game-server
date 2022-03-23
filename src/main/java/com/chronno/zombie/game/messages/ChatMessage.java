package com.chronno.zombie.game.messages;

import com.chronno.zombie.game.model.User;
import lombok.Data;

@Data
public class ChatMessage {

    private User user;
    private String message;

}
