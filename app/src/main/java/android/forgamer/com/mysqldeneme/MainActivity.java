package android.forgamer.com.mysqldeneme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.forgamer.com.mysqldeneme.Classes.Constants;
import android.forgamer.com.mysqldeneme.Classes.JSONFunctions;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    ListView listDuyuru;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listDuyuru = (ListView) findViewById(R.id.listView);
        new getJSON().execute();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            new getJSON().execute();
            // Handle the camera action
        } else if (id == R.id.nav_gallery)
        {

        } else if (id == R.id.nav_slideshow)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class getJSON extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>
    {
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params)
        {
            ArrayList<HashMap<String, String>> arrayDuyurular = new ArrayList<>();
            JSONObject json = JSONFunctions.getJSONfromURL(Constants.JSON_URL_ALL);
            try
            {
                if (json.getInt("basarili") == 1)
                {
                    JSONArray duyurular = json.getJSONArray("duyurular");

                    for (int i = 0; i < duyurular.length(); i++)
                    {
                        JSONObject duyuruJSON = duyurular.getJSONObject(i);
                        HashMap<String, String> duyuru = new HashMap<>();
                        duyuru.put("id", duyuruJSON.getString("id"));
                        duyuru.put("mesaj", duyuruJSON.getString("mesaj"));
                        duyuru.put("tarih", duyuruJSON.getString("tarih"));
                        duyuru.put("yazar", duyuruJSON.getString("yazar"));
                        arrayDuyurular.add(duyuru);
                    }
                }
            } catch (Exception e)
            {
                Log.d("ForGamer", "doInBackground: " + e.getMessage());
            }
            return arrayDuyurular;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> duyurular)
        {
            adapterDuyuru adapter = new adapterDuyuru(MainActivity.this, R.layout.list_item_duyuru, duyurular);
            listDuyuru.setAdapter(adapter);
            super.onPostExecute(duyurular);
        }
    }

    private class adapterDuyuru extends ArrayAdapter<HashMap<String, String>>
    {
        ArrayList<HashMap<String, String>> duyurular;
        int resId;
        Context context;

        public adapterDuyuru(Context context, int resource, ArrayList<HashMap<String, String>> objects)
        {
            super(context, resource, objects);
            duyurular = objects;
            resId = resource;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            DuyuruHolder holder = null;

            if (row == null)
            {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(resId, parent, false);

                holder = new DuyuruHolder();
                holder.tvMesaj = (TextView) row.findViewById(R.id.tvMesaj);
                holder.tvTarih = (TextView) row.findViewById(R.id.tvTarih);
                holder.tvYazar = (TextView) row.findViewById(R.id.tvYazar);

                row.setTag(holder);
            } else
            {
                holder = (DuyuruHolder) row.getTag();
            }
            holder.tvMesaj.setText(duyurular.get(position).get("mesaj"));
            holder.tvYazar.setText(duyurular.get(position).get("yazar"));
            holder.tvTarih.setText(duyurular.get(position).get("tarih"));

            return row;
        }

        class DuyuruHolder
        {
            TextView tvMesaj;
            TextView tvTarih;
            TextView tvYazar;
        }
    }
}
