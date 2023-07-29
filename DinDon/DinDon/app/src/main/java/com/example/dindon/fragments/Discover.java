package com.example.dindon.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.dtofront.Pisos;
import com.example.dindon.dtofront.ItemModel;
import com.example.dindon.helpers.DiscoverHelpers.CardStackAdapter;
import com.example.dindon.helpers.DiscoverHelpers.CardStackCallback;
import com.example.dindon.R;
import com.example.dindon.bbddconection.PisosCallback;
import com.example.dindon.bbddconection.PisosRepository;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Discover extends Fragment {
    private List<Pisos> finales = new ArrayList<>();
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private PisosRepository pisosRepository;
    private MutableLiveData<List<Pisos>> mutablePisosList = new MutableLiveData<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CardStackView cardStackView;
        pisosRepository = new PisosRepository();
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        cardStackView = view.findViewById(R.id.card_stack_view);

        mutablePisosList.observe(getViewLifecycleOwner(), pisos -> {
            finales.addAll(pisos);
            adapter = new CardStackAdapter(addList(), getActivity());
            cardStackView.setLayoutManager(manager);
            cardStackView.setAdapter(adapter);
            cardStackView.setItemAnimator(new DefaultItemAnimator());
        });

        obtenerPisos(new PisosCallback() {
            @Override
            public void onPisosObtenidos(List<Pisos> pisos) {
                mutablePisosList.setValue(pisos);
            }

            @Override
            public void onError(Throwable t) {
                //Manejar error
            }
        });

        setupCardStackView();

        return view;
    }

    private void setupCardStackView() {
        manager = new CardStackLayoutManager(getActivity(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
            }

            @Override
            public void onCardSwiped(Direction direction) {

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                    paginate();
                }
                if (direction == Direction.Left) {
                    //No me gusta y no hago nada
                }
                if (direction == Direction.Right) {
                    //Me gusta
                    BBDDManager bbddManager = new BBDDManager(getContext());
                    PreferencesManager prefe = new PreferencesManager(requireContext());
                    String propietario = adapter.getItems().get(manager.getTopPosition() - 1).getPiso().getPropietario();
                    bbddManager.getUserById(
                            usuarioPropietario -> {
                                AtomicBoolean esAmigo = new AtomicBoolean(false);
                                List<String> friendRequests = usuarioPropietario.getFriendsRequest();
                                if (friendRequests != null) {
                                    bbddManager.getUserData(
                                            yo -> {
                                                if (yo.getFriends() != null) {
                                                    for (String amigo : usuarioPropietario.getFriends()) {
                                                        if (amigo.equals(yo.getId())) {
                                                            esAmigo.set(true);
                                                            break;
                                                        }
                                                    }
                                                    for (String amigo : friendRequests) {
                                                        if (amigo.equals(yo.getId())) {
                                                            esAmigo.set(true);
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (!esAmigo.get()) {
                                                    friendRequests.add(yo.getId());
                                                    bbddManager.updateUserB(usuarioPropietario.getId(),
                                                            usuarioPropietario,
                                                            s -> Log.d("Discover", "Se ha enviado la petición de amistad"),
                                                            t -> Log.d("Discover", "No se ha enviado la petición de amistad")
                                                    );
                                                }
                                            },
                                            error -> Log.e("Discover", "Error al obtener el usuario propietario", error),
                                            prefe.getUserEmail()
                                    );
                                }
                            },
                            error -> Log.e("Discover", "Error al obtener el usuario propietario", error),
                            propietario
                    );

                }
            }


            @Override
            public void onCardRewound() {
                //No se usa
            }

            @Override
            public void onCardCanceled() {
                //No se usa
            }

            @Override
            public void onCardAppeared(View view, int position) {
                //No se usa
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                //No se usa
            }
        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new

                LinearInterpolator());
    }


    private void paginate() {
        List<ItemModel> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    private void obtenerPisos(PisosCallback callback) {
        pisosRepository.obtenerPisos(new Callback<List<Pisos>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pisos>> call, @NonNull Response<List<Pisos>> response) {
                if (response.isSuccessful()) {
                    List<Pisos> pisos = response.body();
                    callback.onPisosObtenidos(pisos);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Pisos>> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        for (Pisos piso : finales) {
            items.add(new ItemModel(piso));
        }
        return items;
    }


}
