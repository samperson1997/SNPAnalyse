package main.java.serviceImpl;

import main.java.service.SequenceFileCheck;

public class SequenceFileCheckImpl implements SequenceFileCheck{
    private DataAc dataAc;

    public SequenceFileCheckImpl(String path) {
        dataAc = new DataAc(path);
    }

    @Override
    public boolean checkGeneFileIsNormal() {

        return false;
    }
}
