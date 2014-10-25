package com.ditlabavailability;
 
import java.util.ArrayList;
 
import com.ditlabavailability.R;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
 
public class MainActivityOLD extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         
        ArrayList<SearchResults> searchResults = GetSearchResults();
         
        final ListView lv = (ListView) findViewById(R.id.srListView);
        lv.setAdapter(new MyCustomBaseAdapter(this, searchResults));
         
         
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                SearchResults fullObject = (SearchResults)o;
                Toast.makeText(MainActivityOLD.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
            } 
        });
    }
     
    private ArrayList<SearchResults> GetSearchResults(){
     ArrayList<SearchResults> results = new ArrayList<SearchResults>();
      
     SearchResults sr = new SearchResults();
     sr.setName("Lab AU106");
     sr.setCityState("Available for next 4hrs");
     sr.setPhone("Aungier Street");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Lab AU105");
     sr.setCityState("Available for next 3hrs");
     sr.setPhone("Aungier Street");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Lab KA308");
     sr.setCityState("Available for next 1hr");
     sr.setPhone("Kevin Street");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Lab AU108");
     sr.setCityState("Available for next 1hr");
     sr.setPhone("Aungier Street");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Bill Withers");
     sr.setCityState("Los Angeles, CA");
     sr.setPhone("424-555-8214");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Donald Fagen");
     sr.setCityState("Los Angeles, CA");
     sr.setPhone("424-555-1234");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Steve Rude");
     sr.setCityState("Oakland, CA");
     sr.setPhone("515-555-2222");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Roland Bloom");
     sr.setCityState("Chelmsford, MA");
     sr.setPhone("978-555-1111");
     results.add(sr);
 
     sr = new SearchResults();
     sr.setName("Sandy Baguskas");
     sr.setCityState("Chelmsford, MA");
     sr.setPhone("978-555-2222");
     results.add(sr);
      
     sr = new SearchResults();
     sr.setName("Scott Taylor");
     sr.setCityState("Austin, TX");
     sr.setPhone("512-555-2222");
     results.add(sr);
      
     return results;
    }
}