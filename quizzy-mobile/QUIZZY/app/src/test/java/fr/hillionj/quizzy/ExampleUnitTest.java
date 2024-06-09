package fr.hillionj.quizzy;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Map;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.speciales.application.ReceptionReponse;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ActiverBumpers;

public class ExampleUnitTest {
    @Test
    public void testTrameIDCorrecte() {
        String trameIDCorrect = "$E;1\n";
        assertEquals(ActiverBumpers.class, Protocole.traiterTrame(trameIDCorrect).getClass());
    }

    @Test
    public void testTrameIDIncorrecte() {
        String trameIDIncorrect = "$M;1\n";
        assertNull(Protocole.traiterTrame(trameIDIncorrect));
    }

    @Test
    public void testTrameCorrecte() {
        String trameCorrect = "$E;1\n";
        assertTrue(Protocole.traiterTrame(trameCorrect).estValide());
    }

    @Test
    public void testTrameIncorrecte() {
        String trameIncorrect = "$E;1;5\n";
        assertFalse(Protocole.traiterTrame(trameIncorrect).estValide());
    }

    @Test
    public void testDecodageTrame() {
        String trame = "$R;1;3;4856\n"; //$U;QUESTION;REPONSE;TEMPS\n
        ReceptionReponse protocoleAssocie = (ReceptionReponse) Protocole.traiterTrame(trame);
        assertEquals(1, protocoleAssocie.getNumeroQuestion());
        assertEquals(3, protocoleAssocie.getNumeroReponse());
        assertEquals(4856, protocoleAssocie.getTempsReponse());
    }
}