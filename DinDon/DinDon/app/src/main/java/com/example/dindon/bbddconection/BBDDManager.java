package com.example.dindon.bbddconection;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dindon.dtofront.Opinions;
import com.example.dindon.dtofront.Pisos;
import com.example.dindon.dtofront.User;
import com.example.dindon.helpers.CONSTANTS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BBDDManager {
    private final RequestQueue queue;
    String url = CONSTANTS.URL_CONEXION + "/pisos";
    String url2 = CONSTANTS.URL_CONEXION + "/users";
    String url3 = CONSTANTS.URL_CONEXION + "/api/auth";

    public BBDDManager(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void getAllPisos(Response.Listener<ArrayList<Pisos>> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    ArrayList<Pisos> pisosList;
                    Type listType = new TypeToken<List<Pisos>>() {
                    }.getType();
                    pisosList = gson.fromJson(response.toString(), listType);
                    successListener.onResponse(pisosList);
                },
                errorListener
        );
        queue.add(request);
    }

    public void getUserData(Response.Listener<User> successListener, Response.ErrorListener errorListener, String id) {
        Gson gson = new Gson();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2 + "/" + id, null,
                response -> {
                    User user = gson.fromJson(response.toString(), User.class);
                    successListener.onResponse(user);
                },
                errorListener
        );
        queue.add(request);
    }

    public void getUserById(Response.Listener<User> successListener, Response.ErrorListener errorListener, String id) {
        Gson gson = new Gson();
        System.out.println("URL: " + url2 + "/get/" + id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2 + "/get/" + id, null,
                response -> {
                    User user = gson.fromJson(response.toString(), User.class);
                    successListener.onResponse(user);
                },
                errorListener
        );
        queue.add(request);
    }

    public void isUserArrendador(String id, Response.Listener<Boolean> successListener, Response.ErrorListener errorListener) {
        String urlArrendador = url2 + "/busqueda/" + id;

        // Hacer una petición GET y esperar una respuesta de tipo booleano.
        StringRequest request = new StringRequest(Request.Method.GET, urlArrendador,
                response -> {
                    // Convertir la respuesta a booleano.
                    Boolean isArrendador = Boolean.parseBoolean(response);
                    // Llamar a la devolución de llamada con el resultado.
                    successListener.onResponse(isArrendador);
                },
                errorListener
        );

        queue.add(request);
    }

    public void getUserById(String id, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.GET, url + "/propietario/" + id,
                successListener::onResponse,
                errorListener
        );
        queue.add(request);
    }

    public void getPisosbyUser(String id, Response.Listener<List<Pisos>> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + "/propietario/list/" + id, null,
                response -> {
                    ArrayList<Pisos> pisosList;
                    Type listType = new TypeToken<List<Pisos>>() {
                    }.getType();
                    pisosList = gson.fromJson(response.toString(), listType);
                    successListener.onResponse(pisosList);
                },
                errorListener
        );
        queue.add(request);
    }

    public void addPiso(Pisos newPiso, Response.Listener<Pisos> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(newPiso);

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody == null ? null : jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.POST, url, response -> {
            Pisos piso = gson.fromJson(response, Pisos.class);
            successListener.onResponse(piso);
        }, errorListener);

        queue.add(request);
    }

    public void createOpinion(Opinions op, Response.Listener<Opinions> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(op);

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody == null ? null : jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.POST, CONSTANTS.URL_CONEXION + "/opinions", response -> {
            Opinions opinion = gson.fromJson(response, Opinions.class);
            successListener.onResponse(opinion);
        }, errorListener);

        queue.add(request);
    }

    public void updateUserB(String id, User usuario, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(usuario);

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody == null ? null : jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.PUT, url3 + "/B/" + id, response -> {
            // Since response is a string, no need to parse it into a User object.
            successListener.onResponse(response);
        }, errorListener);


        queue.add(request);
    }

    public void updateUser(String id, User usuario, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(usuario);

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody == null ? null : jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                assert response != null;
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.PUT, url3 + "/" + id, successListener::onResponse, errorListener);


        queue.add(request);
    }

    public void editPiso(String id, Pisos piso, Response.Listener<Pisos> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(piso);

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody == null ? null : jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.PUT, url + "/edit/" + id, response -> {
            Pisos piso1 = gson.fromJson(response, Pisos.class);
            successListener.onResponse(piso1);
        }, errorListener);

        queue.add(request);
    }

    public void getPisoById(String id, Response.Listener<Pisos> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return null;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.GET, url + "/" + id, response -> {
            Pisos piso = gson.fromJson(response, Pisos.class);
            successListener.onResponse(piso);
        }, errorListener);

        queue.add(request);
    }

    public void deletePiso(String id, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return null;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.DELETE, url + "/" + id, successListener::onResponse, errorListener);

        queue.add(request);
    }

    public void editUserPreferences(String id, List<String> nuevasPreferencias, Response.Listener<User> successListener, Response.ErrorListener errorListener) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(nuevasPreferencias);

        // Extensión de StringRequest para pasar el contenido como string
        class MipeticionEnString extends StringRequest {
            public MipeticionEnString(int method, String url, Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
                super(method, url, listener, errorListener);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody == null ? null : jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        MipeticionEnString request = new MipeticionEnString(Request.Method.PUT, url2 + "/edit/" + id, response -> {
            User user = gson.fromJson(response, User.class);
            successListener.onResponse(user);
        }, errorListener);

        queue.add(request);
    }

}
