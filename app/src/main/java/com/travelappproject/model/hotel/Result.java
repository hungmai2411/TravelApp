
package com.travelappproject.model.hotel;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("canonical_ids")
    private Long mCanonicalIds;
    @SerializedName("failure")
    private Long mFailure;
    @SerializedName("message_id")
    private String mMessageId;
    @SerializedName("multicast_id")
    private Long mMulticastId;
    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("success")
    private Long mSuccess;

    public Long getCanonicalIds() {
        return mCanonicalIds;
    }

    public void setCanonicalIds(Long canonicalIds) {
        mCanonicalIds = canonicalIds;
    }

    public Long getFailure() {
        return mFailure;
    }

    public void setFailure(Long failure) {
        mFailure = failure;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String messageId) {
        mMessageId = messageId;
    }

    public Long getMulticastId() {
        return mMulticastId;
    }

    public void setMulticastId(Long multicastId) {
        mMulticastId = multicastId;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }

}
