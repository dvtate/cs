

public class Spinner {

    final int defaultValue = 0;

    // atribute
    private int arrow;

    // constructors
    Spinner(){
        this.arrow = defaultValue;
    }
    Spinner(int value){
        this.arrow = value;
    }

    // methods
    public void spin(){
        this.arrow = (int)(Math.random() * 10);
    }

    // member accessors
    public int getArrow(){
        return this.arrow;
    }
    public void setArrow(int value){
        this.arrow = value;
    }

    // special methods
    public String toString(){
        return "Arrow points to: " + arrow;
    }
    public boolean equals(Spinner s){
        return this.arrow == s.getArrow();
    }

};
