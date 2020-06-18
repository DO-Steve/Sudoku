package Sudoku;

public class Point {

        private int x;
        private int y;

       //** Constructeur **//
        public Point() {
            x=0; y=0;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        //** @return x **//
        public int getX() {
            return x;
        }

        //** @return y **//
        public int getY() {
            return y;
        }

        //** Methode **//
    public static boolean egal(Point p1, Point p2){
            return ((p1.getX() == p2.getX()) && ( p1.getY() == p2.getY()));
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
