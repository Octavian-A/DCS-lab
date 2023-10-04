import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client {


    public static void main(String[] args) throws Exception {
        Socket socket = null;
        try {
            //creare obiect address care identifica adresa serverului
            InetAddress server_address = InetAddress.getByName("localhost");
            //se putea utiliza varianta alternativa: InetAddress.getByName("127.0.0.1")

            socket = new Socket(server_address, 1900);

            //construieste fluxul de intrare prin care sunt receptionate datele de la server
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            //construieste fluxul de iesire prin care datele sunt trimise catre server
            // Output is automatically flushed
            // by PrintWriter:
            PrintWriter out =
                    new PrintWriter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream())), true);


            out.println("10");
            out.println("20");
            out.flush();

            String percent =in.readLine();
            System.out.println(percent); //asteapta raspuns

            out.println("END"); //trimite mesaj care determina serverul sa inchida conexiunea

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            socket.close();
        }
    }
}


