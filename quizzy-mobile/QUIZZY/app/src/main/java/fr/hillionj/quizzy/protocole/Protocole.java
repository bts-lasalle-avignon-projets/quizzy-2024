package fr.hillionj.quizzy.protocole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Protocole {
    private static final String TAG                = "_Protocole";
    public static Protocole traiterTrame(String trame) {
        if (trame == null || trame.contains(";")) {
            return null;
        }
        String indiceTypeProtocole = trame.split(";")[0];
        TypeProtocole type = TypeProtocole.getType(indiceTypeProtocole);
        if (type == null) {
            return null;
        }
        return type.getProtocole(trame);
    }
    public static Protocole getProtocole(TypeProtocole type) {
        return type.getProtocole(null);
    }

    public abstract String getFormat();
    public abstract String getTrame();
    public abstract TypeProtocole getType();

    public String genererTrame(String... contenu) {
        if (!estValide()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("$" + getType().getIndiceType());
        for (String st : contenu) {
            sb.append(";" + st);
        }
        sb.append("\n");
        return sb.toString();
    }

    public boolean estValide(String... contenu) {
        int nombreArgumentsRequis = getFormat().split(";").length - 1;
        if (nombreArgumentsRequis == 0 && contenu == null) {
            return true;
        }
        return nombreArgumentsRequis == contenu.length;
    }

    protected Map<String, String> extraireDonnees() {
        if (!estValide(getTrame().split(";"))) {
            return null;
        }
        Map<String, String> donnees = new HashMap<>();
        String[] cles = getFormat().replace("$", "").replace("\n", "").split(";");
        String[] arguments = getTrame().replace("$", "").replace("\n", "").split(";");
        for (int i = 0; i < arguments.length; i++) {
            donnees.put(cles[i + 1], arguments[i]);
        }
        return donnees;
    }
}
