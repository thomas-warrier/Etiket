package exportkit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exportkit.figma.R;


public class TicketPreviewFragment extends Fragment {

    public TicketPreviewFragment() {
        // Required empty public constructor
    }

    public static TicketPreviewFragment newInstance(String param1, String param2) {
        TicketPreviewFragment fragment = new TicketPreviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_preview, container, false);
    }
}