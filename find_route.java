import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class find_route {

    public static LinkedList[] cityGraph;
    public static int cityOrigin, cityDestination;
    public static List<Node> frontier = new LinkedList<Node>();
    public static List<String> frontierNames = new LinkedList<>();
    public static ArrayList<String> cityArrayList = new ArrayList<>();
    public static Map<String, Route> routeList = new HashMap();
    public static String[] cityArray;

    public static void main(String[] args) {
        BufferedReader bufferedReader;
        /**
         * SECTION 1 : INPUT
         * >Read each line from the input file until "END OF INPUT".
         * >Split each line with space and push the city name in a arrayList, if unique
         * >Convert arrayList to array
         */
        try {
            bufferedReader = new BufferedReader(new FileReader(args[0]));
            String newLine = "";
            while (!newLine.equals("END OF INPUT")) {
                newLine = bufferedReader.readLine();
                String[] seperateCity = newLine.split(" ");
                if (!newLine.equals("END OF INPUT")) {
                    for (int i = 0; i < 2; i++) {
                        if (!cityArrayList.contains(seperateCity[i]))
                            cityArrayList.add(seperateCity[i]);
                    }
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cityArray = new String[cityArrayList.size()];
        cityArray = cityArrayList.toArray(cityArray);
        /**
         * SECTION 2: POPULATE and START
         * >Create a placeholder for the LinkedList graph
         * >Populate the LinkedList
         */
        cityGraph = new LinkedList[cityArray.length];
        for (int i = 0; i < cityArray.length; i++) {
            cityGraph[i] = new LinkedList();
        }
        try {
            bufferedReader = new BufferedReader(new FileReader(args[0]));
            String newLine = "";
            while (!newLine.equals("END OF INPUT")) {
                newLine = bufferedReader.readLine();
                String[] seperateWord = newLine.split(" ");
                if (!newLine.equals("END OF INPUT")) {
                    int vertex1 = cityArrayList.indexOf(seperateWord[0]);
                    int vertex2 = cityArrayList.indexOf(seperateWord[1]);
                    int weight = Integer.parseInt(seperateWord[2]);
                    cityGraph[vertex1].add(new Node(vertex2, weight));
                    cityGraph[vertex2].add(new Node(vertex1, weight));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cityOrigin = cityArrayList.indexOf(args[1]);
        cityDestination = cityArrayList.indexOf(args[2]);
        UCS(cityArrayList, cityArray);
    }

    /**
     * SECTION 3: Main Uniformed Cost Search algorithm
     * Start by setting the starting city cost to 0 and adding it to the priority queue
     * */
    public static void UCS(ArrayList cityArraylist, String[] cityArray) {
        //Where explored/expanded nodes are stored
        Set<Integer> explored = new HashSet<>();
        Set<String> exploredString = new HashSet<>();
        //Origin node added to P.Queue
        Node originNode = new Node(cityOrigin, 0);
        originNode.pathcost = 0;
        frontier.add(originNode);

        while (!frontier.isEmpty()) {
            Node cityCurrent = frontier.get(0);
            frontier.remove(0);
            update();
            updateFrontierNames();
            /*if (cityCurrent.cityLabel == cityDestination) {
                explored.add(cityDestination);
                exploredString.add(cityArray[cityDestination]);
                }*/
            explored.add(cityCurrent.cityLabel);
            exploredString.add(cityArray[cityCurrent.cityLabel]);

            ListIterator listIterator = cityGraph[cityCurrent.cityLabel].listIterator(0);
            while (listIterator.hasNext()) {
                Node child = (Node) listIterator.next();
                child.pathcost = child.weight + cityCurrent.pathcost;

                if (!frontierNames.contains(cityArray[child.cityLabel]) && !explored.contains(child.cityLabel)) {
                    routeList.put(cityArray[child.cityLabel],
                            new Route(cityArray[cityCurrent.cityLabel], cityArray[child.cityLabel],child.weight));

                    child.parent = cityCurrent;
                    frontier.add(child);
                    update();
                    updateFrontierNames();

                } else if ((frontier.contains(child))) {
                    if (child.pathcost > cityCurrent.pathcost) {
                        //If there there is already the same node in the frontier but the new route is better,
                        //use the new route by changing the parent of the said node
                        for(Route route : routeList.values()){
                            if (cityArray[child.cityLabel].equals(route.child)){
                                routeList.remove(cityArray[child.cityLabel]);
                                frontier.remove(child);
                                update();
                                updateFrontierNames();
                                routeList.put(cityArray[child.cityLabel],
                                        new Route(cityArray[cityCurrent.cityLabel], cityArray[child.cityLabel], child.weight));
                            }
                        }

                        child.parent = cityCurrent;
                        child.pathcost = child.weight + cityCurrent.pathcost;

                        frontier.remove(cityCurrent);
                        frontier.add(child);
                        update();
                        updateFrontierNames();

                    }
                    update();
                    updateFrontierNames();
                }

            }

        }

        printPath(cityArray[cityOrigin], cityArray[cityDestination]);
    }
    /**
     * SECTION 4: Helping Methods
     * */
    private static void printPath(String start_city, String cityDestination) {
        int distance = 0;
        Stack<Route> paths = new Stack<>();
        while (!cityDestination.equals(start_city)) {
            Route route = routeList.get(cityDestination);
            try {
                paths.add(route);
                distance = distance + route.distance;
                cityDestination = route.parent;
            } catch (Exception e) {
                System.out.println("distance: infinity");
                System.out.println("route: \nnone");
                return;
            }
        }

        System.out.println("distance: " + distance + " km");
        System.out.println("route: ");

        while (!paths.isEmpty()) {
            System.out.println(paths.pop());
        }
    }
    public static void update(){
        Collections.sort(frontier, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.pathcost > o2.pathcost) {
                    return 1;
                } else if (o1.pathcost < o2.pathcost) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

    }
    public static void updateFrontierNames(){
        frontierNames = new ArrayList<>();
        for (Node node : frontier ) {
            frontierNames.add(cityArray[node.cityLabel]);
        }
    }
    /**
     * SECTION 5: Debugging Methods
     * */
    public static void showGraph(int x) {
        for (int i = 0; i < x; i++) {
            System.out.print(i + ": ");
            ListIterator listIterator = cityGraph[i].listIterator(0);
            while (listIterator.hasNext()) {
                Node node = (Node) listIterator.next();
                System.out.print(node.cityLabel + "(" + node.weight + ") ");
            }
            System.out.println();
        }
    }
    public static void printFrontier(String[] cityArray){
        System.out.print("Frontier: [");
        for (Node node: frontier) {
            System.out.print(cityArray[node.cityLabel] + "->" + node.pathcost + " ");
        }
        System.out.print("]");

    }
}
