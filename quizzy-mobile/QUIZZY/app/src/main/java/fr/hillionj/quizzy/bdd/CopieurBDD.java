package fr.hillionj.quizzy.bdd;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class CopieurBDD {

    private final String cheminDossier;
    private final String nomFichier;
    private final Context context;

    public CopieurBDD(final String nomFichier, @NonNull final Context context) {
        this.nomFichier = nomFichier;
        this.cheminDossier = "/data/data/" + context.getPackageName() + "/databases/";
        this.context  = context;
    }

    private void copierOctets(@NonNull InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[1024];
        int length;
        while((length = input.read(buffer)) > 0)
        {
            output.write(buffer, 0, length);
        }
    }

    private void fermerLesFlux(OutputStream output, InputStream input)
    {
        if(output != null)
        {
            try
            {
                output.close();
            }
            catch(IOException e)
            {
                Log.e("QUIZZY_" + this.getClass().getName(), e.getMessage(), e);
            }
        }
        if(input != null)
        {
            try
            {
                input.close();
            }
            catch(IOException e) {
                Log.e("QUIZZY_" + this.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public void verifier()
    {
        File fichierBaseDeDonnes = new File(cheminDossier + nomFichier);
        try
        {
            if(!fichierBaseDeDonnes.exists())
            {
                copierBaseDeDonnees(context.getAssets().open(nomFichier), fichierBaseDeDonnes);
            }
        }
        catch(Exception e)
        {
            Log.e("QUIZZY_" + this.getClass().getName(), e.getMessage(), e);
        }
    }

    public void copierBaseDeDonnees(InputStream source, File destination) throws IOException
    {
        InputStream  input  = null;
        OutputStream output = null;
        try
        {
            new File(cheminDossier).mkdirs();
            input  = new BufferedInputStream(source);
            output = new BufferedOutputStream(new FileOutputStream(destination));
            copierOctets(input, output);
        }
        finally
        {
            fermerLesFlux(output, input);
        }
    }
}
