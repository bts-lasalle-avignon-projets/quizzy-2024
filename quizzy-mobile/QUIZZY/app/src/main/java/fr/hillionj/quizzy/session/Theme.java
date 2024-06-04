package fr.hillionj.quizzy.session;

public class Theme {

    private int idTheme;
    private String nom;

    public Theme(int idTheme, String nom) {
        this.idTheme = idTheme;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public int getIdTheme() {
        return idTheme;
    }
}
