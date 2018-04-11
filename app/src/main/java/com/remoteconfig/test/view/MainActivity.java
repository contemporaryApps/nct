package com.remoteconfig.test.view;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.remoteconfig.test.model.ConfigRepositoryImpl;
import com.remoteconfig.test.model.TabConfig;
import com.remoteconfig.test.presenter.MainContract;
import com.remoteconfig.test.presenter.MainPresenter;
import com.remoteconfig.test.remoteconfig.R;
import com.remoteconfig.test.remoteconfig.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainPresenter presenter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        presenter = new MainPresenter(this, ConfigRepositoryImpl.getInstance(getPreferences(Context.MODE_PRIVATE)));
        presenter.getTabConfiguration();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayTabs(List<TabConfig> tabs) {
        TabPagerAdapter sectionsPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabs);

        binding.container.setAdapter(sectionsPagerAdapter);

        //Loop through the config and add each tab
        for (int i = 0; i < tabs.size(); i++) {
            binding.tabs.addTab(binding.tabs.newTab().setText(tabs.get(i).label));
        }

        binding.container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));
        binding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.container));
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayProgress() {
        binding.postProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.postProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(error)
                .setTitle(R.string.oops)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.getTabConfiguration();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }
}
