package com.blocksim.presentation.dto.response;

public class FeeComparisonResponseDTO {
    private final String priority;
    private final double fee;
    private final int position;
    private final double estimatedTime;

    public FeeComparisonResponseDTO(String priority, double fee, int position, double estimatedTime) {
        this.priority = priority;
        this.fee = fee;
        this.position = position;
        this.estimatedTime = estimatedTime;
    }

    public String getPriority() {
        return priority;
    }

    public double getFee() {
        return fee;
    }

    public int getPosition() {
        return position;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }
}
