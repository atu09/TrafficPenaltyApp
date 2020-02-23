package com.traffic.penalty.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PenaltyInfoVo {

@SerializedName("result")
@Expose
private List<PenaltyReasonItem> result = null;

public List<PenaltyReasonItem> getResult() {
return result;
}

public void setResult(List<PenaltyReasonItem> result) {
this.result = result;
}

}