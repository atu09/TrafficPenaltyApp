package com.example.trafficpenaltyapp.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PenaltyInfoVo {

@SerializedName("result")
@Expose
private List<PenaltyResultVo> result = null;

public List<PenaltyResultVo> getResult() {
return result;
}

public void setResult(List<PenaltyResultVo> result) {
this.result = result;
}

}