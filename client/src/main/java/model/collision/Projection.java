package model.collision;

class Projection{
    double min;
    double max;

    public Projection(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public boolean overlap(Projection p2) {
        // Check if this projection overlaps with the passed one
        return (!(p2.max < this.min || this.max < p2.min));
    }

    double getOverlap(Projection p2){
        if(this.max<p2.max) return this.max-p2.min;
        else return p2.max-this.min;
    }
}