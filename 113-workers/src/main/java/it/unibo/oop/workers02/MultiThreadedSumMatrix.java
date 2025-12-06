package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{
    
    private int nthread = 0;
    
    public MultiThreadedSumMatrix(final int n) {
        super();
        this.nthread = n;
    }

    @Override
    public double sum(double[][] matrix) {
        final int totalRows = matrix.length;
        final int base = totalRows / nthread;
        final int resto = totalRows % nthread;
                
        /*
         * Build a stream of workers
         */
        final List<Worker> workers = new ArrayList<>(nthread);
        for (int i = 0; i < nthread; i++) {
            final int myRows = base + (i < resto ? 1 : 0);
            
            //If matrix is empty don't do anythig
            if (myRows > 0) {                
                final int startRow = i * base + Math.min(i, resto);
                final Worker w = new Worker(matrix, startRow, myRows);
                workers.add(w);
                w.start();
            }
        }    

        double sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (final InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

    private static class Worker extends Thread {
        private double[][] matrixData;
        private final int startRow;
        private final int nrow;
        private double res;        

        /**
         * Build a new worker.
         *
         * @param list
         *            the list to sum
         * @param startRow
         *            the initial position for this worker
         * @param nrow
         *            the no. of elems to sum up for this worker
         */
        Worker(final double[][] matrix, final int startRow, final int nrow) {
            super();
            this.matrixData = matrix;
            this.startRow = startRow;
            this.nrow = nrow;            
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public synchronized void run() {
            System.out.println("Working from row " + startRow + " to row " + (startRow + nrow - 1));
            for (int row = startRow; row < matrixData.length && row < startRow + nrow; row++ ) {
                for(int num = 0; num < matrixData[row].length; num++) {
                    this.res+=matrixData[row][num];
                }              
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }

        /**
         * Returns the result of summing up the integers within the list.
         *
         * @return the sum of every element in the array
         */
        public synchronized double getResult() {
            return this.res;
        }
    }    
}
