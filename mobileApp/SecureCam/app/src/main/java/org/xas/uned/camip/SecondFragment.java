package org.xas.uned.camip;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xas.uned.camip.R;
import org.xas.uned.camip.adapter.AppListAdapter;
import org.xas.uned.camip.dao.FollowedAppsDAO;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment implements AppListAdapter.ItemClickListener {

    private AppListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getActivity().findViewById(R.id.rvApps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        List<String> appNames = new ArrayList<>();
        for (ApplicationInfo packageInfo : packages) {
            appNames.add(packageInfo.packageName);
        }

        adapter = new AppListAdapter(getActivity(), appNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        FollowedAppsDAO.getInstance().addApp(adapter.getItem(position));
        view.setBackgroundColor(Color.GREEN);
    }
}