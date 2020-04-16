package datamanagement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.util.concurrent.ExecutionException;
import java.util.Iterator;

import database_schema.*;

public class RemoteDataSource {
    private String host;
    private int port;

    public RemoteDataSource() {
        // use Node Express defaults
        host = "10.0.2.2";
        port = 3000;
    }

    public RemoteDataSource(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     *
     * @param email -- email of the user we are trying to find
     * @return User object containing the response from the database.
     * Return null if the user does not exist.
     */
    public User findUser(String email) {
        try {
            User user = null;
            String urlString = "http://" + this.host + ":" + port + "/find?email=" + email;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(result);
                user = createUser(data);
            }
            return user;
        } catch (InterruptedException e) {

        } catch (ExecutionException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *
     * @param name -- name of the user we are trying to find
     * @return ArrayList containing User objects from the database response.
     * Return an empty ArrayList if the user does not exist.
     */
    public ArrayList<User> searchUsers(String name) {
        try {
            User user = null;
            String urlString = "http://" + this.host + ":" + port + "/profile/search?name=" + name;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                ArrayList<User> users = new ArrayList<User>();
                JSONParser parser = new JSONParser();
                JSONArray jsonUsers = (JSONArray) parser.parse(result);
                for(Object objUser : jsonUsers) {
                    JSONObject jsonUser = (JSONObject) objUser;
                    users.add(createUser(jsonUser));
                }
                return users;
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<User>();
    }

    public class HttpFindRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlString = strings[0];
            Log.d("FIND REQUEST:", urlString);
            String result;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.connect();

                int responsecode = conn.getResponseCode();
                if (responsecode != 200) {
                    Log.d("RESPONSE CODE", "Unexpected status code: " + responsecode);
                    System.out.println("Unexpected status code: " + responsecode);
                } else {
                    Scanner in = new Scanner(url.openStream());
                    result = in.nextLine();
                    in.close();
                    return result;
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                Log.d("ERROR", "MalformatedURLException");
            } catch (IOException e) {
                Log.d("ERROR", "IOException");
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    private User createUser(JSONObject data) {
        String email = (String) data.get("email");
        String name = (String) data.get("name");
        String school = (String) data.get("school");
        String major = (String) data.get("major");
        String gradYear = (String) data.get("gradYear");
        String bio = (String) data.get("bio");
        if (email == null || email.isEmpty()) {
            return null;
        }
        User user = new User(email, name, school, major, gradYear, bio);
        return user;
    }

    /**
     *
     * @param u -- User we want to save to the database
     * @return true if the save is successful and false otherwise.
     */
    public boolean saveUser(User u) {
        String email = u.getEmail();
        String name = u.getName();
        String school = u.getSchool();
        String major = u.getMajor();
        String bio = u.getBio();
        String gradYear = u.getGradYear();
        String urlString = "http://" + this.host + ":" + this.port + "/save?email=" + email + "&name=" + name + "&school="
                + school + "&major=" + major + "&bio=" + bio + "&gradYear=" + gradYear;
        HttpSaveRequest saveRequest = new HttpSaveRequest();
        try {
            String result = saveRequest.execute(urlString).get();
            if (result.equals("success")) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param t -- User we want to save to the database
     * @return true if the save is successful and false otherwise.
     */
    public boolean saveNewTimeslot(Timeslot t) {
        Log.d("SAVE NEW", "Timeslot");
        String tutorEmail = t.getTutor();
        String tutorName = t.getTutorName();
        String date = t.getDate();
        String[] courses = t.getCourses();

        if (timeslotExists(tutorEmail,date)) {
            return false;
        } else {
            Log.d("MAKING NEW TIMESLOT"," yeet");
            String urlString = "http://" + this.host + ":" + this.port + "/makeTimeslot?email=" + tutorEmail + "&name=" + tutorName + "&date="
                    + date;

            for (String s : courses) {
                urlString += "&course=" + s;
            }

            HttpSaveRequest saveRequest = new HttpSaveRequest();
            try {
                String result = saveRequest.execute(urlString).get();
                if (result.equals("success")) {
                    Log.d("success:", "yeehaw");
                    return true;
                }
            } catch (InterruptedException e) {
                Log.d("INTERRUPTION ERROR:", "" + e);
                e.printStackTrace();
            } catch (ExecutionException e) {
                Log.d("EXECUTION ERROR:", "" + e);
                e.printStackTrace();
            }
            return false;
        }
    }

    private boolean timeslotExists(String tutorEmail, String date) {
        try {
            String urlString = "http://" + this.host + ":" + port + "/findTimeslot?email=" + tutorEmail+"&date=" +date;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();

            Log.d("STRING RESULT", result);
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(result);

                if (data.get("result").equals("true")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     *
     * @param a -- User we want to save to the database
     * @return true if the save is successful and false otherwise.
     */
    public boolean saveNewAppt(Appointment a) {
        String tutorName = a.getTutor();
        String tuteeName = a.getTutee();
        String tutorEmail = a.getTutorEmail();
        String tuteeEmail = a.getTuteeEmail();
        String date = a.getDate();

        String urlString = "http://" + this.host + ":" + this.port + "/bookAppointment?tutoremail=" + tutorEmail + "&tutorname=" + tutorName + "&date="
                + date + "&tuteename=" + tuteeName + "&tuteeemail=" + tuteeEmail;

        HttpSaveRequest saveRequest = new HttpSaveRequest();
        try {
            String result = saveRequest.execute(urlString).get();
            if (result.equals("success")) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public class HttpSaveRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlString = strings[0];
            String result = "";
            try {
                URL url = new URL(urlString);
                Log.d("url at connect", "" + url);
                Log.d("POINT", "point 2");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.connect();
                Log.d("POINT", "point 3");

                int responsecode = conn.getResponseCode();
                if (responsecode != 200) {
                    Log.d("RESPONSE CODE", "Unexpected status code: " + responsecode);
                } else {
                    Scanner in = new Scanner(url.openStream());
                    String s = in.nextLine();
                    JSONParser parser = new JSONParser();
                    if (in.hasNext()) {
                        JSONObject data = (JSONObject) parser.parse(in.nextLine());
                        result = (String) data.get("result");
                        Log.d("RESULT", result);
                    }
                    in.close();
                }
                conn.disconnect();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public List<Timeslot> getAllTimeslots() {
        try {
            User user = null;
            //String urlString = "http://" + this.host + ":" + port + "/find?email=" + email;
            // we're going to have to change this!
            String urlString = "http://" + this.host + ":" + this.port + "/getAllTimeSlots";
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                JSONParser parser = new JSONParser();
                JSONArray timeslots = (JSONArray) parser.parse(result);
                List<Timeslot> ts = new ArrayList<Timeslot>();
                Iterator<JSONObject> iter = timeslots.iterator();

                while (iter.hasNext()) {

                    JSONObject data = (JSONObject)iter.next();

                    JSONArray temp = (JSONArray)data.get("courses");
                    Iterator<String> it = temp.iterator();
                    List<String> co = new ArrayList<String>();

                    while (it.hasNext()) {
                        String s = (String)it.next();
                        co.add(s);
                    }

                    String[] courses = co.toArray(new String[0]);
                    String tutorName = (String)data.get("tutorName");
                    String tutorEmail = (String)data.get("tutorEmail");
                    String date = (String)data.get("date");

                    Timeslot t = new Timeslot(tutorEmail, date, courses, tutorName);
                    Log.d("NEW TIMESLOT:",t.toString());
                    ts.add(t);
                }

                return ts;
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Appointment> getTuteeAppointments(String email) {
        try {
            String urlString = "http://" + this.host + ":" + this.port + "/findTuteeAppointments?tuteeEmail=" +email;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                JSONParser parser = new JSONParser();

                JSONArray appts = (JSONArray) parser.parse(result);
                List<Appointment> ap = new ArrayList<Appointment>();
                Iterator<JSONObject> iter = appts.iterator();

                while (iter.hasNext()) {

                    JSONObject data = (JSONObject)iter.next();

                    String tuteeName = (String)data.get("tuteeName");
                    String tuteeEmail = (String)data.get("tuteeEmail");
                    String tutorName = (String)data.get("tutorName");
                    String tutorEmail = (String)data.get("tutorEmail");
                    String date = (String)data.get("date");
                    Boolean confirmed = (Boolean)data.get("confirmed");

                    Appointment a = new Appointment(tutorName, tuteeName, date, tutorEmail, tuteeEmail);
                    ap.add(a);
                }
                return ap;
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Appointment> getTutorAppointments(String email) {
        try {
            String urlString = "http://" + this.host + ":" + this.port + "/findTutorAppointments?tutorEmail=" +email;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                JSONParser parser = new JSONParser();

                JSONArray appts = (JSONArray) parser.parse(result);
                List<Appointment> ap = new ArrayList<Appointment>();
                Iterator<JSONObject> iter = appts.iterator();

                while (iter.hasNext()) {

                    JSONObject data = (JSONObject)iter.next();

                    String tuteeName = (String)data.get("tuteeName");
                    String tuteeEmail = (String)data.get("tuteeEmail");
                    String tutorName = (String)data.get("tutorName");
                    String tutorEmail = (String)data.get("tutorEmail");
                    String date = (String)data.get("date");
                    Boolean confirmed = (Boolean)data.get("confirmed");

                    Appointment a = new Appointment(tutorName, tuteeName, date, tutorEmail, tuteeEmail);
                    ap.add(a);
                }

                return ap;
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String cancelAppointment(Appointment ap) {
        String status = "failed";
        try {
            String urlString = "http://" + this.host + ":" + this.port +
                    "/cancelAppointment?tutorEmail=" + ap.getTutorEmail()
                    + "&tuteeEmail=" + ap.getTuteeEmail() + "&date=" + ap.getDate();
            Log.d("POINT", "point1");
            HttpSaveRequest saveRequest = new HttpSaveRequest();
            String result = saveRequest.execute(urlString).get();

//            if (result != null) {
//                JSONParser parser = new JSONParser();
//                JSONObject data = (JSONObject) parser.parse(result);
//                status = (String) data.get("status");
//                if (!status.equals("success")) {
//                    status = "error";
//                }
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;
    }
}
