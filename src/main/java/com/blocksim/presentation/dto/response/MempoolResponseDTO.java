package com.blocksim.presentation.dto.response;

public class MempoolResponseDTO {
    private final boolean found;
    private final int position;
    private final int total;
    private final double estimatedTime;

    public MempoolResponseDTO(boolean found, int position, int total, double estimatedTime) {
        this.found = found;
        this.position = position;
        this.total = total;
        this.estimatedTime = estimatedTime;
    }

    public boolean isFound() {
        return found;
    }

    public int getPosition() {
        return position;
    }

    public int getTotal() {
        return total;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }
}
