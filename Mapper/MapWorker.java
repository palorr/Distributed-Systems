import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import java.util.Date;

public class MapWorker {

    int Port;
    String IP;

    public static void main(String[] args) {
        new MapWorker().run();
    }

    public void run() {
        System.out.println("Enter Reducer IP: ");
        Scanner sc = new Scanner(System.in);
        IP = sc.nextLine();
        Port = 3333;
        System.out.println("Enter Mappers Port: ");
        int Port2 = sc.nextInt();
        ServerSocket providerSocket = null;
        Socket connection = null;
        Object message = null;
        int count = 0;
        String string1=null;
        String string2=null;
        Date date1=null ;
        Date date2=null ;
        String url = "jdbc:mysql://" + "83.212.117.76" + ":" + "3306" + "/" + "ds_systems_2016";
        String dbClass = "com.mysql.jdbc.Driver";
        ArrayList<CheckIn> list = new ArrayList<CheckIn>();
        ArrayList<CheckIn> list2 = new ArrayList<CheckIn>();
        ArrayList<String> list3 = new ArrayList<String>();

        try {
            providerSocket = new ServerSocket(Port2);
            while (true) {
                connection = providerSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());

                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                out.writeObject("Connection Succesful!"+Port2+"<---mapper");
                out.flush();
                do {
                    try {

                        message = in.readObject();
                        count++;
                        System.out.println(count) ;
                        if (count % 4 == 1) {
                            string1 = (String) message;
                        } 
                        else if (count % 4 == 2) {
                            string2 = (String) message;
                        } 
                        else if (count % 4 == 3) {
                            date1 = (Date) message;
                        }
                        else {
                            date2 = (Date) message;
                        }

                    } catch (ClassNotFoundException classnot) {
                        System.err.println("Data received in unknown format");
                    }
                } while (count % 4 != 0);
                square s1 = new square(string1) ;
                square s2 = new square(string2) ;
                doubledate d1 = new doubledate(date1,date2);
                in.close();
                out.close();
                connection.close();
                /////////sql////////
                String query = "SELECT * FROM checkins where latitude<=" + s1.maxla + "and latitude>=" + s1.minla + "and longitude<=" + s1.maxlo + " and longitude>=" + s1.minlo;
                try {
                    Class.forName(dbClass);
                    Connection con = DriverManager.getConnection(url, "omada49", "omada49db");
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        CheckIn tmp = new CheckIn(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getDouble(7), rs.getDouble(8), rs.getTimestamp(9), rs.getString(10));
                        if (tmp.getDate().after(d1.getD1()) && tmp.getDate().before(d1.getD2())) {
                            list.add(tmp);
                        }
                    }
                    list2 = map(list, s2);
                    list.clear();

                    for (CheckIn tmp : list2) {
                        list3.add(tmp.getPOI());
                    }
                    Set<String> POIS = new HashSet<String>(list3);
                    System.out.println("Number of POIS in this mapper: " + POIS.size());
                    String POISarr[] = new String[POIS.size()];

                    int POISarrC[] = new int[POIS.size()];
                    int c = 0;

                    for (String i : POIS) {
                        POISarr[c] = i;
                        c++;
                    }

                    for (int i = 0; i < POISarrC.length; i++) {
                        POISarrC[i] = 0;
                    }

                    for (CheckIn tmp : list2) {
                        for (int i = 0; i < POISarr.length; i++) {
                            if (tmp.getPOI().equals(POISarr[i])) {
                                POISarrC[i]++;
                                break;
                            }
                        }
                    }
                    int temp;
                    String temp2;
                    for (int i = 0; i < POISarrC.length; i++) {
                        for (int j = 1; j < POISarrC.length - i; j++) {
                            if (POISarrC[j - 1] < POISarrC[j]) {
                                temp = POISarrC[j - 1];
                                POISarrC[j - 1] = POISarrC[j];
                                POISarrC[j] = temp;
                                ///////
                                temp2 = POISarr[j - 1];
                                POISarr[j - 1] = POISarr[j];
                                POISarr[j] = temp2;
                                //////
                            }
                        }
                    }

                    sendToReducers(POISarr, POISarrC);

                    list2.clear();
                    list3.clear();
                    POIS.clear();

                    con.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                providerSocket.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    public ArrayList<CheckIn> map(ArrayList<CheckIn> list, square s) {
        ArrayList<CheckIn> list2 = new ArrayList<CheckIn>();
        for (CheckIn tmp : list) {
            if (s.tester(tmp.getX(), tmp.getY())) {
                list2.add(tmp);
            }

        }
        return list2;

    }
    //////////////////////////////////////////////////////////////////////////
    public void sendToReducers(String[] POISarr, int[] POISarrC) {
        Socket requestSocket = null;
        ObjectOutputStream out = null;

        String message;
        try {
            requestSocket = new Socket(InetAddress.getByName(IP), Port);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            arrPack ap = new arrPack(POISarr, POISarrC);
            out.writeObject(ap);
            out.flush();

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
    //////////////////////////////////////////////////////////////////////////
}
