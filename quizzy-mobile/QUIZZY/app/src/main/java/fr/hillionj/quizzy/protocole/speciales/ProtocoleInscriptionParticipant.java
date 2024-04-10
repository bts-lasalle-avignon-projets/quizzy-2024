package fr.hillionj.quizzy.protocole.speciales;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

public class ProtocoleInscriptionParticipant extends Protocole {
    private String trame;

    public ProtocoleInscriptionParticipant(String trame) {
        this.trame = trame;
    }
    @Override
    public String getFormat() {
        return "$I;%PID%;%NOM%\n";
    }
    @Override
    public String getTrame() {
        return trame;
    }

    @Override
    public TypeProtocole getType() {
        return TypeProtocole.INSCRIPTION_PARTICIPANT;
    }

    public String getNom() {
        return extraireDonnees().get("%NOM%");
    }

    public String getPID() {
        return extraireDonnees().get("%PID%");
    }
}
