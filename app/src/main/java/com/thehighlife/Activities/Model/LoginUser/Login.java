package com.thehighlife.Activities.Model.LoginUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thehighlife.Activities.Model.UserResponseData;

public class Login {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UserResponseData data;

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The data
     */
    public UserResponseData getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(UserResponseData data) {
        this.data = data;
    }
}
