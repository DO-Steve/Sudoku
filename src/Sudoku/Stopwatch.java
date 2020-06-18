package Sudoku;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Stopwatch implements ActionListener {

    private int hours, minutes, seconds;
    private Timer timer;

        //** Constructeur **//
    public Stopwatch() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        timer = new Timer(1000, this);
    }

        //** Action a faire chaque seconde **//
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        seconds++;
        if (seconds ==60) {
            minutes++;
            seconds =0;
            if (minutes==60){
                hours++;
                minutes=0;
            }
        }
    }

        //** lancement du chrono
    public void start(){
        timer.start();
    }

        //** Arret du chrono
    public void stop(){
        timer.stop();
    }

        //** remet le chrono a 0 **//
    public void reset(){
        if (timer.isRunning())
            stop();
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

        //** retourne le chrono comme un String **//
    public String getTime() {
        DecimalFormat formatter = new DecimalFormat("00");
        String h = formatter.format(hours);
        String m = formatter.format(minutes);
        String s = formatter.format(seconds);
        return h + ":" + m + ":" + s;
    }
}