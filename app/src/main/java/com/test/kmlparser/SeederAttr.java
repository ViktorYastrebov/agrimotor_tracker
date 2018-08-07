package com.test.kmlparser;

public class SeederAttr {

    private int m_sowerCount;
    private int m_sowerLength; //in meters

    SeederAttr(int sowerCount, int sowerLength)
    {
        m_sowerCount = sowerCount;
        m_sowerLength = sowerLength;
    }

    int getSowerCount() {
        return m_sowerCount;
    }

    int getSowerLength() {
        return m_sowerLength;
    }

    int getTotalLength() {
        return m_sowerLength * m_sowerCount;
    }
}
