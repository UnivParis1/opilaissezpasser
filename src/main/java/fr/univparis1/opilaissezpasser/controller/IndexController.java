package fr.univparis1.opilaissezpasser.controller;

import fr.univparis1.opilaissezpasser.main.OpilaissezpasserParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.UploadedFile;

@ManagedBean
@RequestScoped
public class IndexController implements Serializable {

    private UploadedFile file;
        
    private List<Map<String, String>>    results;

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    /**
     * @return the results
     */
    public List<Map<String, String>> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<Map<String, String>> results) {
        this.results = results;
    }

    /**
    * Upload : point d'entrée principal pour le début du process d'intégration des laissez-passer
    * @return String la vue en fonction du résultat du process
    */
    public String upload() throws IOException, Exception {

        Boolean error = false;

        InputStreamReader isr = null;
        try {
            InputStream in = file.getInputstream();
            isr = new InputStreamReader(in);

            OpilaissezpasserParser parser = new OpilaissezpasserParser(isr);
            setResults(parser.parseCsv());

        } catch (IOException ex) {
            error = true;
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            
            
//            throw ex;
        } catch (Exception ex) {
            error = true;
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            error = true;
            throw ex;
        } finally {
            if (isr != null) {
                isr.close();
            }
        }

        if (error == true) {
            return "index";
        } else {
            return "result";
        }
    }

}
