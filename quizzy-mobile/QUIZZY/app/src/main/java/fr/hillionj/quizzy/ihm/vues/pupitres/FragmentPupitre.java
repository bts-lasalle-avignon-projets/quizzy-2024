package fr.hillionj.quizzy.ihm.vues.pupitres;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.hillionj.quizzy.databinding.FragmentDashboardBinding;
import fr.hillionj.quizzy.ihm.vues.QuizzyIHM;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentPupitre extends Fragment implements QuizzyIHM
{
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModelePupitre dashboardViewModel = new ViewModelProvider(this).get(ModelePupitre.class);
        View root = FragmentDashboardBinding.inflate(inflater, container, false).getRoot();
        return root;
    }
}