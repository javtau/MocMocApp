package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjv.proyectointegradorv1.Fragments.Buscar_viaje;
import com.jjv.proyectointegradorv1.Fragments.Chat;
import com.jjv.proyectointegradorv1.Fragments.Mis_viajes;
import com.jjv.proyectointegradorv1.Fragments.Publicar_viaje;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *
     * Branch: Devel
     * Last change: 18/01/2017
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabs;
    private TextView txt1 ;

    //variables usadas para el control de usuario
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final int[] ICONS = {R.drawable.ic_tab_publicar, R.drawable.ic_tab_buscar, R.drawable.ic_tab_mis_viajes, R.drawable.ic_tab_chatear};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // No user is signed in
            Intent i = new Intent(this,Loggin.class);
            startActivity(i);
        }*/
        // recoge la instancia de FireBaseAuth
        mAuth = FirebaseAuth.getInstance();
        // creamos un listener para llevar un control de los cambios en el registro
        // y determinar las acciones correspondientes
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // recupera el usuario registrado actualmente
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // si el usuario esta registrado
                if (user != null) {
                    // User is signed in
                    //Toast.makeText(getBaseContext(), getString(R.string.welcome_msg, user.getDisplayName()), Toast.LENGTH_LONG).show();
                    toolbar.setTitle(user.getDisplayName());
                } else {
                    // User is signed out
                    // si el usuario no esta registrado muestra un Toast informandole y lanza la actividad de Login
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                   Toast.makeText(getBaseContext(), getString(R.string.toast_sin_login), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(), Loggin.class);
                    startActivity(i);
                }

            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager); //Configuramos el tab layaut con nuestro view pager

        tabs.getTabAt(0).setIcon(ICONS[0]);
        tabs.getTabAt(1).setIcon(ICONS[1]);
        tabs.getTabAt(2).setIcon(ICONS[2]);
        tabs.getTabAt(3).setIcon(ICONS[3]);



    }
    // se crea el adaptador para nuestro viewpager, se crean los frafmentos necesarios incluyendolos
    // en nuestro adaptador y se le pasa al view pager
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Publicar_viaje(), getString(R.string.fragment1_name), ICONS[0]);
        adapter.addFragment(new Buscar_viaje(), getString(R.string.fragment2_name), ICONS[1]);
        adapter.addFragment(new Mis_viajes(), getString(R.string.fragment3_name), ICONS[2]);
        adapter.addFragment(new Chat(), getString(R.string.fragment4_name), ICONS[3]);
        viewPager.setAdapter(adapter);
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
        if (id == R.id.action_Closesession) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer>mFragmentIconList = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        // metodo para añadir un nuevo fragmento al adapter
        void addFragment(Fragment fragment, String title, int iconId) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentIconList.add(iconId);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onStart() {
        // cuando se inicia la actividad se añade el listener
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        // cuando finaliza la actividad se elimina el listener
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
