package com.thehighlife.Activities.Model.PreffredCategoryResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public  class PreffredCategoryPost {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public PreffredCategoryPost(String kit, String kat) {

        categoryId = kit;
        userId = kat;
    }

    /**
     *
     * @return
     * The categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     *
     * @param categoryId
     * The category_id
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
