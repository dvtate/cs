

public class Spinner {

    final int defaultSections = 10;

    private int sections;

    // atribute
    private int arrow;

    // constructors
    Spinner(){
        this.sections = defaultSections;
    }
    Spinner(int sections){
        this.sections = sections;
    }
    Spinner(int sections, int value) {
        this.sections = sections;
        this.arrow = value;
    }

    // methods
    public void spin(){
        this.arrow = (int)( Math.random() * sections );
    }

    // member accessors
    public int getSections() {
        return sections;
    }
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
        return this.arrow == s.getArrow() && this.sections == s.getSections();
    }

};
