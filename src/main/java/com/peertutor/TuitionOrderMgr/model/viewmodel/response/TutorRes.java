package com.peertutor.TuitionOrderMgr.model.viewmodel.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TutorRes {
    public Long id;
    public String displayName;
    public String introduction;
    public String subjects;
    public String certificates;


    public TutorRes(@JsonProperty("id") Long id,
                    @JsonProperty("displayName") String displayName,
                    @JsonProperty("introduction") String introduction,
                    @JsonProperty("subjects") String subjects,
                    @JsonProperty("certificates") String certificates) {
        this.id = id;
        this.displayName = displayName;
        this.introduction = introduction;
        this.subjects = subjects;
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        return "TutorRes{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
