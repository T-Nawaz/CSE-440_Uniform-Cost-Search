public class Node
{
    public int cityLabel;
    public int weight;
    public int pathcost;
    public Node parent;

    public Node(int cityLabel, int weight)
    {
        this.cityLabel = cityLabel;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Node{" +
                "cityLabel=" + cityLabel +
                ", weight=" + weight +
                ", pathcost=" + pathcost +
                ", parent=" + parent +
                '}';
    }

    public int getCityLabel() {
        return cityLabel;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
