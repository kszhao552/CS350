import java.util.*;

class Split extends EventGenerator{
    private Double p;

    public Split(Timeline timeline, double _p){
        super(timeline);
        this.p = _p;
    }

    @Override
    void receiveRequest(Event evt){
        double rng = Math.random();
        if (rng < p){
            super.next.receiveRequest(evt);
        }
        else{
            super.next2.receiveRequest(evt);
        }
    }

}
