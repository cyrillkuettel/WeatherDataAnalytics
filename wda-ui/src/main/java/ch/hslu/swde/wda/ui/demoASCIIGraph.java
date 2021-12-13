package ch.hslu.swde.wda.ui;

import com.mitchtalmadge.asciidata.graph.ASCIIGraph;

public class demoASCIIGraph {

    private static final double[] sinWaveSeries = new double[240];
    private static final double[] randomWaveSeries = new double[]{
            10, 16, 15, 14, 13, 7, 12, 16, 6, 8, 5, 18, 9, 13, 6, 16, 4, 5, 2, 8, 6, 15, 19, 10, 1,
            17, 2, 9, 19, 6, 10, 19, 13, 4, 10, 10, 14, 10, 10, 9, 8, 16, 14, 12, 14, 11, 3, 13, 18, 15,
            10, 18, 6, 2, 2, 19, 12, 11, 5, 7, 6, 11, 17, 3, 14, 3, 10, 12, 19, 5, 6, 16, 2, 8, 9,
            9, 12, 5, 2, 17, 12, 5, 1, 5, 7, 7, 15, 19, 5, 9}; // 90 Random numbers
    static {
        // Sinuswellen berechnen
        for (int i = 0; i < sinWaveSeries.length; i++)
            sinWaveSeries[i] = 15 * Math.sin(i * ((Math.PI * 4) / sinWaveSeries.length));
    }

    public static void main(String[] args) {

        System.out.println(ASCIIGraph.fromSeries(randomWaveSeries).plot());
        System.out.println(ASCIIGraph.fromSeries(sinWaveSeries).plot());



    }
}
