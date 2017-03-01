package com.jjv.proyectointegradorv1.Utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjv.proyectointegradorv1.Objects.Publicacion;

/**
 * Created by Victor on 26/02/2017.
 */

public class GestionDB {
    private FirebaseDatabase database;
    private FirebaseUser user;
    private Publicacion pub;
    private DatabaseReference dbref;
    private ChildEventListener childEvent;
    private Publicacion publicacion;
    DataSnapshot userkeyShot;


    public GestionDB(FirebaseDatabase database, FirebaseUser user, Publicacion pub) {
        this.database = database;
        this.user=user;
        this.pub=pub;
    }

    public void eliminar(final boolean esPubDeConductor) {

        dbref = database.getReference("user-trips");
        // dbref.orderByChild("keyViaje").equalTo(pub.getKeyViaje()).addValueEventListener(new ValueEventListener() {
        Log.i("pub key eliminar:",pub.getKeyViaje());

        childEvent = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot userkeyShotRecuperada, String s) {
               /* publicacion = dataSnapshot.getValue(Publicacion.class);
                Log.i("publicacion recuperada:",publicacion.getIdConductor());
                Log.i("usuario:",user.getUid());
                Log.i("publicacion a eliminar:",pub.getIdConductor());
                Log.i("KEY VIAJE:","Key del viaje a eliminar: "+dataSnapshot.getKey());
                Log.i("DSN:::::::",dataSnapshot.getChildrenCount()+"");
                Log.i("keys usuarios::::",dataSnapshot.getKey());
                */
                userkeyShot = userkeyShotRecuperada;
                Log.i("userkeyshot recuperada:",userkeyShot.getKey());
                DatabaseReference childref = dbref.child(userkeyShotRecuperada.getKey());
                Query q = childref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
                //devolveria los viajes de cada usuario que coincida con la publicacion a eliminar

                q.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.i("KEY OCA/OCA:",dataSnapshot.getKey());
                        publicacion = dataSnapshot.getValue(Publicacion.class);
                        Log.i("pub OCA/OCA r. uid:",publicacion.getIdConductor());
                        Log.i("publ OCA/OCA r. key:",publicacion.getKeyViaje());

                        if(esPubDeConductor){
                            //si usuario es el conductor borrara todas
                            dataSnapshot.getRef().removeValue();
                        }else{
                            //si no es el conductor comprobara las publicaciones recibidas
                            //solo borrara la que le pertenece
                            if(userkeyShot.getKey().equals(user.getUid())){
                                Log.i("entra en if userkshot:",userkeyShot.getKey());
                                dataSnapshot.getRef().removeValue();
                            }else{
                                //UPDATE en cada publicacion
                                publicacion = dataSnapshot.getValue(Publicacion.class);
                                publicacion.addPlazas(1);
                                dataSnapshot.getRef().setValue(publicacion);
                            }
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        if (esPubDeConductor){
            DatabaseReference ref = database.getReference("Books");
            Query query1 = ref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
            DatabaseReference ref2 = database.getReference("trip");
            Query query2 = ref2.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
            ValueEventListener event = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            query1.addListenerForSingleValueEvent(event);
            query2.addListenerForSingleValueEvent(event);
        }else {

            DatabaseReference ref2 = database.getReference("trip");
            Query query2 = ref2.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
            ValueEventListener event = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        Log.i("datasnppapl",dataSnapshot.getKey());
                        publicacion = appleSnapshot.getValue(Publicacion.class);
                        publicacion.addPlazas(1);
                        appleSnapshot.getRef().setValue(publicacion);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            query2.addListenerForSingleValueEvent(event);
            /*
            database.getReference("trip/"+pub.getKeyViaje())
                    .orderByChild("keyViaje")
                    .equalTo(pub.getKeyViaje())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("datasnpp",dataSnapshot.getKey());
                    publicacion = dataSnapshot.getValue(Publicacion.class);
                    publicacion.addPlazas(1);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/trip/"+publicacion.getKeyViaje(),publicacion.toMap());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*/

        }
        dbref.addChildEventListener(childEvent);
    }



    public void deletePubsEnTrip_UserTrip() {
        DatabaseReference ref = database.getReference("user-trips");
        ValueEventListener eventUT = new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                   Log.i("UID USER RECUPERADO:",appleSnapshot.getKey());
                   DatabaseReference ref = database.getReference("user-trips/"+appleSnapshot.getKey());
                   Query q = ref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
                   ValueEventListener vel = new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                               Log.i("RESERVA RECUPERADA:",appleSnapshot.getKey());
                               appleSnapshot.getRef().removeValue();
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {}
                   };
                   q.addListenerForSingleValueEvent(vel);
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventUT);
        DatabaseReference ref1 = database.getReference("Books");
        Query query1 = ref1.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
        DatabaseReference ref2 = database.getReference("trip");
        Query query2 = ref2.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                   appleSnapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
       query1.addListenerForSingleValueEvent(event);
       query2.addListenerForSingleValueEvent(event);
    }

    public void deletePubDeUserEnUserTrip() {
        //recojemos las publicaciones del usuario
        DatabaseReference ref = database
                .getReference("user-trips/"+user.getUid());
        Query q = ref.orderByChild("keyViaje").equalTo(pub.getKeyViaje()) ;
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    Log.i("Key viaje a eliminar",appleSnapshot.getKey());
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        q.addListenerForSingleValueEvent(vel);

    }

    public void updatePubsEnTrip_UserTrip() {
        updatePlazasTrips();
        updatePlazasUserTrips();
    }

    private void updatePlazasTrips() {
        DatabaseReference ref = database.getReference("trip");
        Query query = ref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    publicacion = appleSnapshot.getValue(Publicacion.class);
                    publicacion.addPlazas(1);
                    appleSnapshot.getRef().setValue(publicacion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query.addListenerForSingleValueEvent(event);
    }

    private void updatePlazasUserTrips() {
        DatabaseReference ref = database.getReference("user-trips");
        ValueEventListener eventUT = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    Log.i("UID USER RECUPERADO:",appleSnapshot.getKey());
                    DatabaseReference ref = database.getReference("user-trips/"+appleSnapshot.getKey());
                    Query q = ref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
                    ValueEventListener vel = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                Log.i("RESERVA RECUPERADA:",appleSnapshot.getKey());
                                publicacion = appleSnapshot.getValue(Publicacion.class);
                                publicacion.addPlazas(1);
                                appleSnapshot.getRef().setValue(publicacion);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    q.addListenerForSingleValueEvent(vel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventUT);
    }
}
