package fr.univparis1.opilaissezpasser.main;

import fr.univparis1.opilaissezpasser.utils.PropertySingleton;
import gouv.education.apogee.commun.client.utils.WSUtils;
import gouv.education.apogee.commun.client.ws.laissezpassermetier.LaissezPasserMetierServiceInterface;
import gouv.education.apogee.commun.client.ws.laissezpassermetier.LaissezPasserMetierSoapBindingStub;
import gouv.education.apogee.commun.transverse.exception.WebBaseException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.jumpmind.symmetric.csv.CsvReader;

/**
 *
 * @author ebohm
 */
public class OpilaissezpasserParser {

    private CsvReader opis;
    private List params;
    private final LaissezPasserMetierServiceInterface service;

    public OpilaissezpasserParser(Reader fileReader) throws IOException, Exception {

        params = new ArrayList();

        params.add("typLpa");    // type de laissez-passer (valeur : REINS / IAPRIMO)
        params.add("codAnu");    // année IA du laissez-passer
        params.add("numOPI");    // numéro d'OPI de l'étudiant
        params.add("codEtp");    // code étape
        params.add("codVrsVet"); // code version étape
        params.add("codDip");    // code diplôme
        params.add("codVrsVdi"); // code version diplôme
        params.add("libCmtLpa"); // commentaire du laissez-passer

        String laissezPasserUrl = null;

        PropertySingleton propertySingleton = PropertySingleton.getInstance();
        Properties properties = propertySingleton.getProperties();

        if (properties == null) {
            throw new Exception("Le Fichier de configuration des WebServices n'est pas chargé");
        }

        laissezPasserUrl = properties.getProperty("laissezPasserMetier.urlService");
        if (laissezPasserUrl == null) {
            throw new Exception("Une propriété esssentielle n'est pas trouvé dans le fichier de configuration");
        }

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        try {
            this.service = (LaissezPasserMetierSoapBindingStub) WSUtils.getService(WSUtils.LAISSEZPASSER_SERVICE_NAME, user, password);
        } catch (WebBaseException _ex) {
            System.err.println("Web Exception levée de type " + _ex);
            System.err.println(_ex.getLastErrorMsg());
            throw _ex;
        } catch (Exception _ex) {
            System.err.println("Java Exception levée de type " + _ex);
            throw _ex;
        }

        opis = new CsvReader(fileReader);
        if (opis.readHeaders() == false) {
            String errorMsg = "Erreur de lecture du fichier csv";

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur de traitement", errorMsg);
            FacesContext.getCurrentInstance().addMessage(null, message);

            throw new IOException(errorMsg);
        }

        if (opis.getHeaderCount() != params.size()) {
            String errorMsg = "Le format de fichier est incorrect voir documentation";

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur de traitement", errorMsg);
            FacesContext.getCurrentInstance().addMessage(null, message);

            throw new IOException(errorMsg);
        }
    }

    public List parseCsv() throws IOException, Exception {
        List mapReturn = new ArrayList();

        while (opis.readRecord()) {
            Map map = new HashMap();

            for (Object obj : params) {
                String param = (String) obj;
                map.put(param, opis.get(param));
            }

            boolean error = false;
            String message = null;
            try {
                this.mettreAJourLaissezPasserWS(map);
            } catch (WebBaseException webException) {
                error = true;
                message = webException.getLastErrorMsg();
            } catch (IOException ex) {
                error = true;
                message = ex.getMessage();
            }
            finally {
                map.put("numOPI", (String) map.get("numOPI"));

                if (error == true) {
                    map.put("message", message);
                    map.put("class_css", "opiErrorResult");
                } else {
                    map.put("message", "Laissez-passer inséré avec succès");
                    map.put("class_css", "opiSuccessResult");
                }

                mapReturn.add(map);
            }
        }

        return mapReturn;
    }

    private void mettreAJourLaissezPasserWS(Map map) throws Exception, WebBaseException {
        service.mettreAJourLaissezPasserWS((String) map.get("typLpa"), (String) map.get("codAnu"), (String) (String) map.get("numOPI"), null, (String) map.get("codEtp"), (String) map.get("codVrsVet"), (String) map.get("codDip"), (String) map.get("codVrsVdi"), null, null, (String) map.get("libCmtLpa"));
    }
}
