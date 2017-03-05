package com.jjv.proyectointegradorv1.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjv.proyectointegradorv1.Fragments.Buscar_viaje;
import com.jjv.proyectointegradorv1.Fragments.Chat;
import com.jjv.proyectointegradorv1.Fragments.Mis_viajes;
import com.jjv.proyectointegradorv1.Fragments.Publicar_viaje;
import com.jjv.proyectointegradorv1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String FB_USER = "fireBaseUsuarioLogueado";
    public static final String FB_EMAIL = "fireBaseEmailLogueado";
    public static final String FB_AVATAR = "avatar";
    public static final Uri DEFAULTIMAGEURI = Uri.parse("https://firebasestorage.googleapis.com/v0/b/logginpi.appspot.com/o/Userimage%2Fdefault.png?alt=media&token=3791a8b6-c7d0-45fe-b04b-cd0b90ffb6fd");
    public static final int EDITPROFILE = 22;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static final String TAG = MainActivity.class.getSimpleName();
    private static ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabs;
    private TextView txt1 ;

    //variables usadas para el control de usuario
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String usuarioLogueado, emailLogueado, uidlogueado;
    private Uri userimage;

    // variables para el panel lateral
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navView;
    private View headerView;
    private Drawable iconoMenu;

    // variables para el header del panel lateral
    private TextView tvLatUsuario, tvLatEmail;
    private ImageView avatar;

    // array de iconos para las pestañas
    private final int[] ICONS = {R.drawable.ic_tab_publicar, R.drawable.ic_tab_buscar, R.drawable.ic_tab_mis_viajes, R.drawable.ic_tab_chatear};
    private final int[] ICONS_GRISES = {R.drawable.ic_tab_publicar_gris, R.drawable.ic_tab_buscar_gris, R.drawable.ic_tab_mis_viajes_gris, R.drawable.ic_tab_chatear_gris};
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

    /// variables para fireba storage

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://logginpi.appspot.com").child("Userimage");
    private Uri useruribck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    // muestra en el toolbar un icono y el nombre del usuario registrado
                    toolbar.setTitle(user.getDisplayName().toUpperCase());
                    toolbar.setNavigationIcon(iconoMenu);
                } else {
                    // si el usuario no esta registrado muestra un Toast informandole y lanza la actividad de Login
                    Intent i = new Intent(getBaseContext(), Loggin.class);
                    startActivity(i);
                    finish();
                }

            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // configura el view pager con el section adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        tabs = (TabLayout) findViewById(R.id.tabs);
        //Configuramos el tab layaut con nuestro view pager
        setupViewPager(mViewPager);
        tabs.setupWithViewPager(mViewPager);

        // listener para determinar que pestaña esta activa
        // dependiendo de cual este llama a un metodo u otro para mostrar icono y texto o solo icono
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setText(adapter.getPageTitleCompleto(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setText(adapter.getPageTitle(tab.getPosition()));


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.setText(adapter.getPageTitleCompleto(tab.getPosition()));

            }
        });

        // de esta forma los iconos son mas pequeños y hay mas espacio entre titulo e icono
        /*tabs.getTabAt(0).setIcon(ICONS[0]);
        tabs.getTabAt(1).setIcon(ICONS[1]);
        tabs.getTabAt(2).setIcon(ICONS[2]);
        tabs.getTabAt(3).setIcon(ICONS[3]);*/

        // selecciona una pestaña por defecto cada vez que se llama a onCreate
        // en este caso buscar
        // TODO: dependiendo del perfil del usuario(conductor o usuario normal) seleccionar una pestaña diferente
        mViewPager.setCurrentItem(2);

        /** CONFIGURACION DEL PANEL LATERAL **/

        navView = (NavigationView) findViewById(R.id.nvView);
        headerView = navView.getHeaderView(0);
        iconoMenu = getResources().getDrawable(R.drawable.ic_menu_white);
        tvLatUsuario = (TextView) headerView.findViewById(R.id.usuario);
        tvLatEmail = (TextView) headerView.findViewById(R.id.email);
        avatar = (ImageView) headerView.findViewById(R.id.avatar);

        // si el action bar existe
        if(actionBar != null && mAuth.getCurrentUser() != null){
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu_white));
            actionBar.setDisplayShowHomeEnabled(true);
            FirebaseUser usuario = mAuth.getCurrentUser();

            usuarioLogueado = usuario.getDisplayName();
            emailLogueado = usuario.getEmail();
            uidlogueado = usuario.getUid();
            userimage = usuario.getPhotoUrl();
            storageRef.child(uidlogueado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    loaduserimage(uri);
                    userimage = uri;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    userimage = DEFAULTIMAGEURI;
                }
            });
            tvLatUsuario.setText(mAuth.getCurrentUser().getDisplayName());
            tvLatEmail.setText(mAuth.getCurrentUser().getEmail());
            loaduserimage(userimage);
            //avatar.setImageDrawable();
        }
        // *** esto está por razones de seguridad pero se supone que nunca deberia hacer esto
        else{
            tvLatUsuario.setText("");
            tvLatEmail.setText("");
        }

        // asigna el listener al navigation view
        // configura acciones segun opcion seleccionada
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navView.setCheckedItem(item.getItemId());
                // acciones segun item seleccionado en el panel lateral
                switch(item.getItemId()){
                    case R.id.drawer_perfil:
                        Intent i = new Intent(MainActivity.this, PerfilActivity.class);
                        i.putExtra(FB_AVATAR,userimage.toString());
                        startActivityForResult(i,EDITPROFILE);
                        break;
                    case R.id.drawer_contacto:
                        Intent j = new Intent(MainActivity.this, ContactoActivity.class);
                        j.putExtra(FB_USER, mAuth.getCurrentUser().getDisplayName());
                        j.putExtra(FB_EMAIL, mAuth.getCurrentUser().getEmail());
                        startActivity(j);
                        break;
                    case R.id.drawer_info:
                        // hacer algo cuando el usaurio de en info
                        break;
                    case R.id.drawer_salir:
                        crearDialogo().show();
                        break;

                }

                return false;
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.lat_abierto,
                R.string.lat_cerrado){

            public void OnDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            public void OnDrawerOpened(View v){
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);


        /*************************************/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == EDITPROFILE){
                Uri uri = Uri.parse(data.getStringExtra(FB_AVATAR));
                loaduserimage(uri);
                userimage = uri;
                }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
            storageRef.child(uidlogueado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    loaduserimage(uri);
                    userimage = uri;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    private  void loaduserimage(Uri image) {
        Picasso.with(this).load(image).into(avatar);
    }




    private Dialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage("¿Seguro que quieres salir?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    // se crea el adaptador para nuestro viewpager, se crean los frafmentos necesarios incluyendolos
    // en nuestro adaptador y se le pasa al view pager
    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new Publicar_viaje(), getString(R.string.fragment1_name), ICONS[0]);
        adapter.addFragment(new Buscar_viaje(), getString(R.string.fragment2_name), ICONS[1]);
        adapter.addFragment(new Mis_viajes(), getString(R.string.fragment3_name), ICONS[2]);
        adapter.addFragment(new Chat(), getString(R.string.fragment4_name), ICONS[3]);
        viewPager.setAdapter(adapter);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

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
        }else if(id ==  android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
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
        // devuelve solo el icono
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            // de esta forma los iconos son mas grandes y hay menos espacio entre titulo e icono
            Drawable image = ContextCompat.getDrawable(getBaseContext(), ICONS_GRISES[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" \n");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
        // devuelve icono + titulo (solo cuando la pestaña este seleccionada)
        public CharSequence getPageTitleCompleto(int position){
            Drawable image = ContextCompat.getDrawable(getBaseContext(), ICONS[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" \n" + mFragmentTitleList.get(position));
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
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

    public static void selectPage(int page) {
        mViewPager.setCurrentItem(page);
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

}
