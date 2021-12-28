package search;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

interface findStrategy {
    public HashSet<Integer> find(Map<String, List<Integer>> mp, String toFind);
}

class ANYstrategy implements findStrategy{

    private findStrategy method;

    @Override
    public HashSet<Integer> find(Map<String, List<Integer>> mp, String toFind) {
        Map<String, List<Integer>> mapCopy = new HashMap<>(mp);
        Set<Integer> arrLst = new HashSet<>();
        for (var entry : mp.entrySet()) {
            for(var it : toFind.split(" ")) {
                if(entry.getKey().toLowerCase().equals(it.toLowerCase())) {
                    entry.getValue().forEach(item -> arrLst.add(item));
                }
            }
        }
        return new HashSet<Integer>(arrLst);
    }

    public void setMethod(findStrategy method) {
        this.method = method;
    }
}

class ALLstrategy implements findStrategy {

    private findStrategy method;

    @Override
    public HashSet<Integer> find(Map<String, List<Integer>> mp, String toFind) {
        Map<String, List<Integer>> mapCopy = new HashMap<>(mp);
        Set<Integer> arrLst = new HashSet<>();
        for (var entry : mp.entrySet()) {
            for(var it : toFind.split(" ")) {
                if(entry.getKey().toLowerCase().equals(it.toLowerCase())) {
                    mapCopy.remove(entry.getKey());
                }
            }
        }
        for (var entry : mapCopy.entrySet()) {
            entry.getValue().forEach(item -> arrLst.add(item));
        }
        return new HashSet<Integer>(arrLst);
    }

    public void setMethod(findStrategy method) {
        this.method = method;
    }
}

class NONEstrategy implements findStrategy {

    private findStrategy method;

    @Override
    public HashSet<Integer> find(Map<String, List<Integer>> mp, String toFind) {
        Map<String, List<Integer>> mapCopy = new HashMap<>(mp);
        Set<Integer> arrLst = new HashSet<>();
        Set<Integer> ban = new HashSet<>();
        for (var entry : mp.entrySet()) {
            for(var it : toFind.split(" ")) {
                if(entry.getKey().toLowerCase().equals(it.toLowerCase())) {
                    entry.getValue().forEach(item -> ban.add(item));
                }
            }
        }
        for (var entry : mp.entrySet()) {
            for (var b : ban) {
                for (var eg : entry.getValue()) {
                    if(eg == b) {
                        mapCopy.remove(entry.getKey());
                    }
                }
            }
        }
        for (var entry : mapCopy.entrySet()) {
            entry.getValue().forEach(item -> arrLst.add(item));
        }
        System.out.println(mapCopy.toString());
        return new HashSet<Integer>(arrLst);
    }

    public void setMethod(findStrategy method) {
        this.method = method;
    }
}

class FindMethod {
    private findStrategy method;

    public HashSet<Integer> find(Map<String, List<Integer>> mp, String toFind) {
        return this.method.find(mp, toFind);
    }

    public void setMethod(findStrategy method) {
        this.method = method;
    }
}

public class Main {
    public static void printAllPeoples(String[] all) {
        for(int i = 0; i < all.length; i++) {
            System.out.println(all[i]);
        }
    }
    public static void printMenu() {
        System.out.println("\n=== Menu ===\n 1. Find a person\n 2. Print all people\n 0. Exit");
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String readFileAsStringArray = Files.readString(Paths.get(args[1]));
//        System.out.println(readFileAsStringArray);
        String[] allStr = new String[readFileAsStringArray.split("\n").length];
        allStr = Arrays.copyOf(readFileAsStringArray.split("\n"), readFileAsStringArray.split("\n").length);
        List<String> lst = new ArrayList<String>(Arrays.asList(allStr));
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < lst.size(); i++) {
            String[] str = lst.get(i).split(" ");
            for (int j = 0; j < str.length; j++) {
                if(map.containsKey(str[j])) {
                    map.get(str[j]).add(i);
                } else {
                    List<Integer> num = new ArrayList<>();
                    num.add(i);
                    map.put(str[j], num);
                }
            }
        }
        printMenu();
        int controller = scanner.nextInt();
        while (controller != 0) {
            switch (controller){
                case 0:
                    controller = 0;
                    break;
                case 1:
                    System.out.print("\nSelect a matching strategy: ALL, ANY, NONE\n");
                    scanner.nextLine();
                    String strat = scanner.nextLine();
                    System.out.print("\nEnter a name or email to search all suitable people.\n");
                    String scan = scanner.nextLine();
                    switch (strat) {
                        case "ALL":
                            FindMethod fs = new FindMethod();
                            fs.setMethod(new ALLstrategy());
                            Set<Integer> set = new HashSet<Integer>(fs.find(map, scan));
                            System.out.println("\n" + Integer.toString(set.size()) + " persons found:");
                            if(set.size() > 0) {
                                for (int it : set) {
                                    System.out.println(allStr[it]);
                                }
                            }
                            break;
                        case "ANY":
                            FindMethod fs1 = new FindMethod();
                            fs1.setMethod(new ANYstrategy());
                            Set<Integer> set1 = new HashSet<Integer>(fs1.find(map, scan));
                            System.out.println("\n" + Integer.toString(set1.size()) + " persons found:");
                            if(set1.size() > 0) {
                                for (int it : set1) {
                                    System.out.println(allStr[it]);
                                }
                            }
                            break;
                        case "NONE":
                            FindMethod fs2 = new FindMethod();
                            fs2.setMethod(new NONEstrategy());
                            Set<Integer> set2 = new HashSet<Integer>(fs2.find(map, scan));
                            System.out.println("\n" + Integer.toString(set2.size()) + " persons found:");
                            if(set2.size() > 0) {
                                for (int it : set2) {
                                    System.out.println(allStr[it]);
                                }
                            }
                            break;
                        default:
                            break;
                    }
//                    if(find(map, scan).size() > 0) {
//                        ArrayList<Integer> arrList = new ArrayList<>(find(map, scan));
//                        for (int it : arrList) {
//                            System.out.println(allStr[it]);
//                        }
//                    }
                    printMenu();
                    controller = scanner.nextInt();
                    break;
                case 2:
                    System.out.println("=== List of people ===");
                    printAllPeoples(allStr);
                    printMenu();
                    controller = scanner.nextInt();
                    break;
                default:
                    System.out.println("\nIncorrect option! Try again.\n");
                    printMenu();
                    controller = scanner.nextInt();
                    break;
            }
        }
        scanner.close();
        System.out.println("\nBye!");
    }
}