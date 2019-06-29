package com.pathos.dev.animals.domain;

import java.util.List;

public class InterventionsQueryResponse {

    public Integer count;
    public List<InterventionRequest> interventions;

    @Override
    public String toString() {
        return "InterventionsQueryResponse{" +
                "count=" + count +
                ", interventions=" + interventions +
                '}';
    }
}
