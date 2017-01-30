
import java.sql.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ReduceWorker {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int Port = 3333;
        System.out.println("Enter Client's IP adress: ");
        String clientip = sc.nextLine();
        sc.close();

        ServerSocket serverSocket = null;
        Socket socket = null;
        RThread[] r = new RThread[3];
        int count = 0;
        arrPack[] a = new arrPack[3];
        try {
            serverSocket = new ServerSocket(Port);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }

            r[count] = new RThread(socket);
            new Thread(r[count]).start();

            count++;

            if (count == 3) {
                while (true) {

                    if (r[0].getValue() != null && r[1].getValue() != null && r[2].getValue() != null) {
                        count = 0;
                        a[0] = r[0].getValue();
                        a[1] = r[1].getValue();
                        a[2] = r[2].getValue();

                        String p1[] = a[0].getStr();
                        String p2[] = a[1].getStr();
                        String p3[] = a[2].getStr();

                        int c1[] = a[0].getI();
                        int c2[] = a[1].getI();
                        int c3[] = a[2].getI();

                        int max;
                        String maxPOI;

                        int tmp1 = 0;
                        int tmp2 = 0;
                        int tmp3 = 0;

                        String[] POIS = new String[p1.length + p2.length + p3.length];
                        int[] counts = new int[c1.length + c2.length + c3.length];

                        for (int q = 0; q < (p1.length + p2.length + p3.length); q++) { /////taksinomhsh

                            if (q < p1.length) {
                                POIS[q] = p1[q];
                                counts[q] = c1[q];
                            } else if (q < p1.length + p2.length) {
                                POIS[q] = p2[q - p1.length];
                                counts[q] = c2[q - c1.length];
                            } else {
                                POIS[q] = p3[q - (p1.length + p2.length)];
                                counts[q] = c3[q - (c1.length + c2.length)];
                            }

                        }

                        String temp2;
                        int temp;
                        for (int i = 0; i < counts.length; i++) { ///taksinomhsh diplh
                            for (int j = 1; j < counts.length - i; j++) {
                                if (counts[j - 1] < counts[j]) {
                                    temp = counts[j - 1];
                                    counts[j - 1] = counts[j];
                                    counts[j] = temp;
                                    ///////
                                    temp2 = POIS[j - 1];
                                    POIS[j - 1] = POIS[j];
                                    POIS[j] = temp2;
                                    //////
                                }
                            }
                        }
                        System.out.println("o arithmos twn pois einai" + POIS.length);
                        String poiname = null;
                        String longi = null;
                        String lati = null;
                        String photo = null;
                        String str = null;
                        int c=0; 
                        if(POIS.length>=10){
                            c = 10 ;
                        }
                        else c = POIS.length;
                        String url = "jdbc:mysql://" + "83.212.117.76" + ":" + "3306" + "/" + "ds_systems_2016";
                        String dbClass = "com.mysql.jdbc.Driver";
                        ArrayList<String> aa = new ArrayList<String>();
                        Statement stm = null;
                        for (int f = 0; f < c; f++) {
                            try {
                                Class.forName(dbClass);
                                Connection con = DriverManager.getConnection(url, "omada49", "omada49db");
                                //
                                String query = "SELECT distinct POI_name FROM checkins where POI='" + POIS[f] + "'";
                                stm = con.createStatement();
                                ResultSet rs = stm.executeQuery(query);
                                while (rs.next()) {
                                    poiname = rs.getString(1);
                                }
                                //
                                String query1 = "SELECT distinct photos FROM checkins where POI='" + POIS[f] + "'";
                                stm = con.createStatement();
                                ResultSet rs1 = stm.executeQuery(query1);
                                while (rs1.next()) {
                                    photo = rs1.getString(1);
                                }
                                //
                                String query2 = "SELECT distinct longitude FROM checkins where POI='" + POIS[f] + "'";
                                stm = con.createStatement();
                                ResultSet rs2 = stm.executeQuery(query2);
                                while (rs2.next()) {
                                    longi = rs2.getString(1);
                                }
                                //
                                String query3 = "SELECT distinct latitude FROM checkins where POI='" + POIS[f] + "'";
                                stm = con.createStatement();
                                ResultSet rs3 = stm.executeQuery(query3);
                                while (rs3.next()) {
                                    lati = rs3.getString(1);
                                }
                                //////
                                str = poiname + "&&" + longi + "&&" + lati + "&&" + photo;
                                //////
                                aa.add(str);
                                System.out.println(str);
                                con.close();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                System.out.println("provlhma sthn vash");
                            }
                        }
                        //////stelnw se client
                        int clientrecieveport = 1111;
                        Socket requestSocket = null;
                        ObjectOutputStream out = null;
                        String message;
                        try {
                            requestSocket = new Socket(InetAddress.getByName(clientip), clientrecieveport);
                            out = new ObjectOutputStream(requestSocket.getOutputStream());
                            out.writeObject(aa);
                            out.flush();
                            System.out.println("YOU HAVE SUCCESFULLY SEND THE DATA TO YOUR CLIENT");

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
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////

                        break;

                    }
                }

            }
        }
    }
}
