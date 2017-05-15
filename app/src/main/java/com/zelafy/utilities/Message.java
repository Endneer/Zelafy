package com.zelafy.utilities;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

/**
 * Created by endneer on 5/15/17.
 */

public class Message implements IMessage {
    private String id;
    private String text;
    private User user;
    private Date createdAt;

    public Message(String id, User user, String text) {
        this(id, user, text, new Date());
    }

    public Message(String id, User user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }


    /**
     * Returns message identifier
     *
     * @return the message id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns message text
     *
     * @return the message text
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * Returns message author. See the {@link IUser} for more details
     *
     * @return the message author
     */
    @Override
    public IUser getUser() {
        return user;
    }

    /**
     * Returns message creation date
     *
     * @return the message creation date
     */
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
