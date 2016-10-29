package es.esy.rafaelsilva.tcc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.esy.rafaelsilva.tcc.R;

/**
 * Criado por Rafael em 29/10/2016, enjoy it.
 */
public class BoasVindasAmizades extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BoasVindasAmizades newInstance(int sectionNumber) {
        BoasVindasAmizades fragment = new BoasVindasAmizades();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boas_vindas_amizades, container, false);
    }
}
