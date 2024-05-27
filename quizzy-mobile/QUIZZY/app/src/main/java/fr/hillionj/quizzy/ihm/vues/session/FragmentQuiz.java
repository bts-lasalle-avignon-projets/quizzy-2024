package fr.hillionj.quizzy.ihm.vues.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import fr.hillionj.quizzy.databinding.FragmentHomeBinding;
import fr.hillionj.quizzy.ihm.vues.IHM;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentQuiz extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModeleQuiz homeViewModel = new ViewModelProvider(this).get(ModeleQuiz.class);
        View root = FragmentHomeBinding.inflate(inflater, container, false).getRoot();

        IHM.getIHM().ajouterIHM(this);

        return root;
    }

    public void mettreAjourDeroulement() {
    }
}