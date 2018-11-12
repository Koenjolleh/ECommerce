import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class CredentialsModel {

    private Map<String, String> credentials;

    //This piece of code make it so that when we instantiate the class
    //it automatically loads the content of the credentials.xml file into the
    //credentials map
    public CredentialsModel(){

        credentials = new TreeMap<>();

        try {
            XStream xstream = new XStream(new DomDriver());

            ObjectInputStream is = xstream.createObjectInputStream

                    (new FileReader("credentials.xml"));

            credentials = (Map<String, String>) is.readObject();

            is.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    //This method returns a map entry if the parameters matches both key and value of an entry
    //if no match is found null is returned. This is to verify that the user does not already exist
    public Map.Entry<String,String> verifyCredentials(String username, String password){
        for (Map.Entry<String,String> entry: credentials.entrySet()){
            if (entry.getKey().equals(username) && entry.getValue().equals(password)){
                return entry;
            }
        }
        return null;
    }

    //This method takes the parameters and puts it into the credentials map
    //as a key/value pair and then immediately saves the map to the credentials.xml
    public void registerUser(String username, String password){

        credentials.put(username,password);

        try {
            XStream xstream = new XStream(new DomDriver());

            ObjectOutputStream out = xstream.createObjectOutputStream

                    (new FileWriter("credentials.xml"));

            out.writeObject(credentials);

            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}
