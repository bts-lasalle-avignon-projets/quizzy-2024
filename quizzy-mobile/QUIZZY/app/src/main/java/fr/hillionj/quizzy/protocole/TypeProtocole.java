package fr.hillionj.quizzy.protocole;

import fr.hillionj.quizzy.protocole.speciales.ProtocoleInscriptionParticipant;
import fr.hillionj.quizzy.protocole.speciales.ProtocoleLancement;

public enum TypeProtocole {
    LANCEMENT("S"), INSCRIPTION_PARTICIPANT("I");

    private String indiceType;

    TypeProtocole(String indiceType) {
        this.indiceType = indiceType;
    }
    public static TypeProtocole getType(String indiceType) {
        for (TypeProtocole type : values()) {
            if (type.getIndiceType().equals(indiceType)) {
                return type;
            }
        }
        return null;
    }

    public String getIndiceType() {
        return indiceType;
    }

    public Protocole getProtocole(String trame) {
        switch (this) {
            case LANCEMENT:
                return new ProtocoleLancement();
            case INSCRIPTION_PARTICIPANT:
                return new ProtocoleInscriptionParticipant(trame);
            default:
                break;
        }
        return null;
    }
}
