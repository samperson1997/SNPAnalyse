package main.java.serviceImpl.compbio.seqalign;

import main.java.serviceImpl.compbio.Cell;
import main.java.serviceImpl.compbio.DynamicProgramming;

public abstract class SequenceAlignment extends DynamicProgramming {

    protected int match;
    protected int mismatch;
    protected int space;
    private String[] alignments;

    public SequenceAlignment(String sequence1, String sequence2) {
        this(sequence1, sequence2, 1, -1, -1);
    }

    public SequenceAlignment(String sequence1, String sequence2, int match,
                             int mismatch, int gap) {
        super(sequence1, sequence2);

        this.match = match;
        this.mismatch = mismatch;
        this.space = gap;
    }

    protected Object getTraceback() {
        StringBuilder align1Buf = new StringBuilder();
        StringBuilder align2Buf = new StringBuilder();
        Cell currentCell = getTracebackStartingCell();
        while (traceBackIsNotDone(currentCell)) {
            if (currentCell.getRow() - currentCell.getPrevCell().getRow() == 1) {
                align2Buf.insert(0, sequence2.charAt(currentCell.getRow() - 1));
            } else {
                align2Buf.insert(0, '-');
            }
            if (currentCell.getCol() - currentCell.getPrevCell().getCol() == 1) {
                align1Buf.insert(0, sequence1.charAt(currentCell.getCol() - 1));
            } else {
                align1Buf.insert(0, '-');
            }
            currentCell = currentCell.getPrevCell();
        }

        return new String[]{align1Buf.toString(), align2Buf.toString()};
    }

    protected abstract boolean traceBackIsNotDone(Cell currentCell);

    public int getAlignmentScore() {
        if (alignments == null) {
            getAlignment();
        }

        int score = 0;
        for (int i = 0; i < alignments[0].length(); i++) {
            char c1 = alignments[0].charAt(i);
            char c2 = alignments[1].charAt(i);
            if (c1 == '-' || c2 == '-') {
                score += space;
            } else if (c1 == c2) {
                score += match;
            } else {
                score += mismatch;
            }
        }

        return score;
    }

    private String[] getAlignment() {
        ensureTableIsFilledIn();
        alignments = (String[]) getTraceback();
        return alignments;
    }

    protected abstract Cell getTracebackStartingCell();
}