package es.esy.rafaelsilva.tcc.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.GenericDAO.LoadPosts;
import es.esy.rafaelsilva.tcc.R;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CorpoHome extends Fragment {

    ImageView addOne;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.corpo_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        addOne  = (ImageView) getActivity().findViewById(R.id.imgAddOne);
//        addOne.setOnClickListener(runAddOne());

        LoadPosts loadPosts = new LoadPosts(getActivity());
        loadPosts.execute("R", "comentario");


    }

    private View.OnClickListener runAddOne() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOne.setImageResource(R.drawable.ic_added);
                Toast.makeText(getActivity(), "add one.", Toast.LENGTH_LONG).show();
            }
        };
    }
}
