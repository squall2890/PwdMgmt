package android.christian.passwordmanagement.utility;

import android.christian.passwordmanagement.entity.Password;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

public class CustomParsingXML {

    ArrayList<Password> parsedData = new ArrayList<Password>(); //struttura dati che immagazzinerà i dati letti

    public ArrayList<Password> getParsedData() {  //metodo di accesso alla struttura dati
        return parsedData;
    }

    public CustomParsingXML() {
    }

    public void parseXml(String xmlFile) throws ParserConfigurationException {

        Document doc;
        try {

            String log = "";

            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);

            //Costruiamo il nostro documento a partire dallo stream dati fornito dall'URL
            Element root = doc.getDocumentElement();
            //Elemento(nodo) radice del documento

            log += "Root element :" + root.getNodeName() + "\n";

            //NodeList notes=root.getElementsByTagName("registrazione"); //potremmo direttamente prendere gli elementi note
            NodeList notes = root.getChildNodes();
            //ma prediamo tutti i "figli" diretti di root. Utile se non avessimo solo "note" come figli di root

            for(int i=0; i<notes.getLength(); i++) {
                Node c= notes.item(i);

                if(c.getNodeType() == Node.ELEMENT_NODE) {//controlliamo se questo è un nodo elemento (un tag)
                    //se avessimo usato root.getElementsByTagName("note") questo controllo
                    //non sarebbe stato necessario

                    Password password = new Password("","","","","", ""); //costruiamo un oggetto MyNote dove andremo a salvare i dati

                    Element note = (Element) c; //cast da nodo a Elemento

                    NodeList noteDetails = c.getChildNodes();  //per ogni nota abbiamo i vari dettagli

                    for(int j=0; j<noteDetails.getLength(); j++) {

                        Node c1 = noteDetails.item(j);

                        if(c1.getNodeType()==Node.ELEMENT_NODE) { //anche in questo caso controlliamo se si tratta di tag
                            Element detail = (Element) c1; //cast
                            String nodeName = detail.getNodeName(); //leggo il nome del tag
                            String nodeValue = detail.getFirstChild().getNodeValue();//leggo il testo in esso contenuto
                            log += "Nodo: " + nodeName;
                            log += "Valore: "+nodeValue + "\n";

                            //a dipendenza del nome del nodo (del dettaglio) settiamo il relativo valore nell'oggetto

                            if( nodeName.equals("sito") )
                                password.setNomeSito(nodeValue);

                            if( nodeName.equals("username") )
                                password.setUsername(nodeValue);

                            if( nodeName.equals("password") )
                                password.setPassword(nodeValue);

                            if( nodeName.equals("email") )
                                password.setEmail(nodeValue);

                            if( nodeName.equals("note") )
                                password.setNote(nodeValue);
                        }
                    }
log += "Oggetto " + password.toString();
                    Log.d("PARSER", log);
                    parsedData.add(password);
                }
            }
            //gestione eccezioni
        } catch (SAXException | FactoryConfigurationError | ParserConfigurationException | IOException e) {
            Log.d("PARSER", e.toString());
        }
    }
}