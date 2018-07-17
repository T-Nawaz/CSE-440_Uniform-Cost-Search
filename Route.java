public class Route {
    public String parent;
    public String child;
    public int distance;

    public Route(String parent, String child , int distance){
        this.parent = parent;
        this.child = child;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return parent + " " + child + ", " + distance + " km";
    }
}
