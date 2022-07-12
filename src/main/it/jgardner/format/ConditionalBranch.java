package it.jgardner.format;

import java.util.Objects;

public class ConditionalBranch {
    public int conditionalBlockIndex;
    public int conditionalBranchIndex;

    public ConditionalBranch(int conditionalBlockIndex, int conditionalBranchIndex) {
        this.conditionalBlockIndex = conditionalBlockIndex;
        this.conditionalBranchIndex = conditionalBranchIndex;
    }

    public ConditionalBranch(int conditionalBlockIndex) {
        this(conditionalBlockIndex, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionalBranch that = (ConditionalBranch) o;
        return conditionalBlockIndex == that.conditionalBlockIndex && conditionalBranchIndex == that.conditionalBranchIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionalBlockIndex, conditionalBranchIndex);
    }

    @Override
    public String toString() {
        return "ConditionalBranch{" +
                "conditionalBlockIndex=" + conditionalBlockIndex +
                ", conditionalBranchIndex=" + conditionalBranchIndex +
                '}';
    }
}
