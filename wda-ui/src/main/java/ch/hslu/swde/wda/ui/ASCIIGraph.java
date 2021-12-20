package ch.hslu.swde.wda.ui;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

// All credit for this class goes to https://github.com/MitchTalmadge/ASCII-Data
// I have copied it here to allow for making minor adjustments.

import com.mitchtalmadge.asciidata.graph.util.SeriesUtils;
import java.text.DecimalFormat;
import java.util.Arrays;

public class ASCIIGraph {
    private double[] series;
    private double min;
    private double max;
    private double range;
    private int numRows;
    private int numCols;
    private int tickWidth = 8;
    private DecimalFormat tickFormat = new DecimalFormat("###0.00");
    private int axisIndex;
    private int lineIndex;

    private ASCIIGraph(double[] series) {
        this.series = series;
    }

    private void calculateFields() {
        double[] minMax = SeriesUtils.getMinAndMaxValues(this.series);
        this.min = minMax[0];
        this.max = minMax[1];
        this.range = this.max - this.min;
        this.axisIndex = this.tickWidth + 1;
        this.lineIndex = this.axisIndex + 1;
        this.numRows = this.numRows == 0 ? (int)Math.round(this.max - this.min) + 1 : this.numRows;
        this.numCols = this.tickWidth + (this.axisIndex - this.tickWidth) + this.series.length;
    }

    public static ASCIIGraph fromSeries(double[] series) {
        return new ASCIIGraph(series);
    }

    public ASCIIGraph withNumRows(int numRows) {
        this.numRows = numRows;
        return this;
    }

    public ASCIIGraph withTickWidth(int tickWidth) {
        this.tickWidth = tickWidth;
        return this;
    }

    public ASCIIGraph withTickFormat(DecimalFormat tickFormat) {
        this.tickFormat = tickFormat;
        return this;
    }

    public String plot() {
        this.calculateFields();
        // this.numRows = numRows / 2; // compress it a bit

        char[][] graph = new char[this.numRows][this.numCols];

        for(int row = 0; row < this.numRows; ++row) {
            Arrays.fill(graph[row], ' ');
        }

        this.drawTicksAndAxis(graph); // Axis Y
        this.drawXAxis(graph); // My own implementation of Axis X
        this.drawLine(graph);
        // System.out.printf("The number of columns in the graph = %s", numCols);
         System.out.printf("The number of rows in the graph = %s", numRows);
        return this.convertGraphToString(graph);
    }

    public void drawXAxis(char[][] graph) {
        double y = this.determineYValueAtRow(graph.length-1);
        int Xoffset = 10;
        for (int column = Xoffset; column < graph[graph.length-1].length; column++) {
            graph[graph.length-1][column] =(char)(9472);

        }
    }

    private void drawTicksAndAxis(char[][] graph) {
        for(int row = 0; row < graph.length; ++row) {
            double y = this.determineYValueAtRow(row);
            char[] tick = this.formatTick(y).toCharArray();
            System.arraycopy(tick, 0, graph[row], 0, tick.length);
            graph[row][this.axisIndex] = (char)(y == 0.0D ? 9532 : 9508);
        }

    }

    public int getMaxColumn(char[][] graph) {
        return Arrays.stream(graph).mapToInt(row -> row.length).max().getAsInt();
    }


    private void drawLine(char[][] graph) {
        int initialRow = this.determineRowAtYValue(this.series[0]);
        graph[initialRow][this.axisIndex] = 9532;

        for(int x = 0; x < this.series.length - 1; ++x) {
            int startRow = this.determineRowAtYValue(this.series[x]);
            int endRow = this.determineRowAtYValue(this.series[x + 1]);
            if (startRow == endRow) {
                graph[startRow][this.lineIndex + x] = 9472;
            } else {
                graph[startRow][this.lineIndex + x] = (char)(startRow < endRow ? 9582 : 9583);
                graph[endRow][this.lineIndex + x] = (char)(startRow < endRow ? 9584 : 9581);
                int lowerRow = Math.min(startRow, endRow);
                int upperRow = Math.max(startRow, endRow);

                for(int row = lowerRow + 1; row < upperRow; ++row) {
                    graph[row][this.lineIndex + x] = 9474;
                }
            }
        }

    }

    private int determineRowAtYValue(double yValue) {
        return this.numRows - 1 - (int)Math.round((yValue - this.min) / this.range * (double)(this.numRows - 1));
    }

    private double determineYValueAtRow(int row) {
        return this.max - (double)row * (this.range / (double)(this.numRows - 1));
    }

    private String formatTick(double value) {
        StringBuilder paddedTick = new StringBuilder();
        String formattedValue = this.tickFormat.format(value);

        for(int i = 0; i < this.tickWidth - formattedValue.length(); ++i) {
            paddedTick.append(' ');
        }

        return paddedTick.append(formattedValue).toString();

    }

    private String convertGraphToString(char[][] graph) {
        StringBuilder stringGraph = new StringBuilder();
        char[][] var3 = graph;
        int var4 = graph.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            char[] row = var3[var5];
            stringGraph.append(row).append('\n');
        }

        return stringGraph.toString();
    }
}
