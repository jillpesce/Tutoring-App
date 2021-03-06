package datamanagement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public User findUserName(String name) {
        try {
            User user = null;
            String urlString = "http://" + this.host + ":" + port + "/findName?name=" + name;
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

    /**
     * @return allUsersFromTheDatabase
     */
    public List<User> getAllUsers() {
        try {
            User user = null;
            String urlString = "http://" + this.host + ":" + port + "/profile/getAllUsers?";
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
        String picture = (String) data.get("picture");
        if (email == null || email.isEmpty()) {
            return null;
        }
        User user = new User(email, name, school, major, gradYear, bio);
        user.setPicture(picture);
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
     * @param u -- user that we want to add new course to
     * @return true if the save is successful and false otherwise.
     */
    public boolean addCourse(User u) {
        String email = u.getEmail();
        String name = u.getName();
        String school = u.getSchool();
        String major = u.getMajor();
        String bio = u.getBio();
        String gradYear = u.getGradYear();
        String urlString = "http://" + this.host + ":" + this.port + "/addCourse?email=" + email +
                "&updatedCourses=" + u.getCourses();
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
                    if(confirmed) {
                        a.confirmAppointment();
                    }
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
                    if(confirmed) {
                        a.confirmAppointment();
                    }
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

    public List<Timeslot> getFilteredTimeslots(String email, String[] courses) {
        List<Timeslot> timeslots;
        if (courses !=  null) {
            List<String> c = Arrays.asList(courses);
        }
        if (email != null) {
            timeslots = getTutorTimeslots(email);
            if (courses != null && courses.length > 0 && timeslots.size() > 0) {
                int size = timeslots.size();
                int offset = 0;
                for (int i = 0; i < size; i++) {
                    Timeslot t = timeslots.get(i-offset);
                    boolean inCourse = false;

                    for (String s1 : courses) {
                        for (String s2 : t.getCourses()) {
                            if (s1.equals(s2)) {
                                inCourse = true;
                            }
                        }
                    }

                    if (!inCourse) {
                        Log.d("removing: ", t.getTutor() + ",  " +t.getDate());
                        offset ++;
                        timeslots.remove(t);
                    }
                }
            }
        } else if (courses != null){
            timeslots = new ArrayList<Timeslot>();
            for (int i = 0; i < courses.length; i++) {
                String course = courses[i];
                List<Timeslot> temp = getCourseTimeslot(course);
                if (temp != null) {
                    for (int j = 0; j < temp.size(); j++) {
                        Timeslot t = temp.get(j);
                        if (!timeslots.contains(t)) {
                            timeslots.add(t);
                        }
                    }
                }
            }
        } else {
            timeslots = getAllTimeslots();
        }
        Log.d("# OF FILTER TS:", "" + timeslots.size());
        for (Timeslot t : timeslots) {
            Log.d("FILTERED TIMESLOT:", t.getTutor() + ",  " +t.getDate());
        }
        return timeslots;
    }

    public List<Timeslot> getCourseTimeslot(String course) {
        try {
            String urlString = "http://" + this.host + ":" + this.port + "/findFilteredTimeslots?course=" +course;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                JSONParser parser = new JSONParser();

                JSONArray appts = (JSONArray) parser.parse(result);
                List<Timeslot> ts = new ArrayList<Timeslot>();
                Iterator<JSONObject> iter = appts.iterator();

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

    public List<Timeslot> getTutorTimeslots(String email) {
        try {
            String urlString = "http://" + this.host + ":" + this.port + "/getTutorTimeslots?tutorEmail=" +email;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {
                JSONParser parser = new JSONParser();

                JSONArray appts = (JSONArray) parser.parse(result);
                List<Timeslot> ts = new ArrayList<Timeslot>();
                Iterator<JSONObject> iter = appts.iterator();

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

    public boolean deleteTimeslot(String tutorEmail, String date) {
        String urlString = "http://" + this.host + ":" + this.port + "/deleteTimeslot?tutorEmail=" + tutorEmail + "&date=" + date;

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

    public String cancelAppointment(Appointment ap) {
        String status = "failed";
        try {
            String urlString = "http://" + this.host + ":" + this.port +
                    "/cancelAppointment?tutorEmail=" + ap.getTutorEmail()
                    + "&tuteeEmail=" + ap.getTuteeEmail() + "&date=" + ap.getDate();
            Log.d("POINT", "point1");
            HttpSaveRequest saveRequest = new HttpSaveRequest();
            String result = saveRequest.execute(urlString).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;
    }

    public String acceptAppointment(Appointment ap) {
        String status = "failed";
        try {
            String urlString = "http://" + this.host + ":" + this.port +
                    "/acceptAppointment?tutorEmail=" + ap.getTutorEmail()
                    + "&tuteeEmail=" + ap.getTuteeEmail() + "&date=" + ap.getDate();
            Log.d("POINT", "point1");
            HttpSaveRequest saveRequest = new HttpSaveRequest();
            String result = saveRequest.execute(urlString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        getTuteeAppointments(ap.getTutorEmail());
        return status;
    }

    /**
     *
     * @param u -- User we want to save to the database
     * @return true if the save is successful and false otherwise.
     */
    public boolean saveRating(User u, int rating, String review) {
        Log.d("saveRating", "saving rating! in app");
        String email = u.getEmail();
        String urlString = "http://" + this.host + ":" + this.port + "/saveRating?email=" + email
                + "&rating=" + rating + "&review=" + review;
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
     * @return allUsersFromTheDatabase
     */
    public List<List<String>> getUserRatings(User u) {
        try {
            String email = u.getEmail();
            String urlString = "http://" + this.host + ":" + port + "/getRatings?email=" + email;
            HttpFindRequest findRequest = new HttpFindRequest();
            String result = findRequest.execute(urlString).get();
            if (result != null) {

                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(result);
                JSONArray ratingsArray = (JSONArray)data.get("ratings");
                JSONArray reviewsArray = (JSONArray) data.get("reviews");
                ArrayList<String> ratings = new ArrayList<String>();
                ArrayList<String> reviews = new ArrayList<String>();
                Iterator<Integer> it = ratingsArray.iterator();

                while (it.hasNext()) {
                    //Long l = it.next();
                    //String rating = "" + it.next();
                    int i = Integer.parseInt("" + it.next());
                    ratings.add("" + i);
                }

                Iterator<String> it2 = reviewsArray.iterator();
                while (it2.hasNext()) {
                    String review = it2.next();
                    reviews.add(review);
                }
                List<List<String>> returnVal = new ArrayList<List<String>>();
                returnVal.add(ratings);
                returnVal.add(reviews);
                return returnVal;
                //return createRatingReviewMap(reviews, ratings);
                //return ratings;
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new  ArrayList<List<String>>();
    }

    private Map<String, Integer> createRatingReviewMap(List<String> reviews, List<Integer> ratings) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int len = Math.min(reviews.size(), ratings.size());
        for (int i = 0; i < len; i++) {
            map.put(reviews.get(i), ratings.get(i));
        }
        return map;
    }
}
