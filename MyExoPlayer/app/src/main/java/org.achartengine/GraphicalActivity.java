//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine;

import android.app.Activity;
import android.os.Bundle;
import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;

public class GraphicalActivity extends Activity {
    private GraphicalView mView;
    private AbstractChart mChart;

    public GraphicalActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        this.mChart = (AbstractChart)extras.getSerializable("chart");
        this.mView = new GraphicalView(this, this.mChart);
        String title = extras.getString("title");
        if(title == null) {
            this.requestWindowFeature(1);
        } else if(title.length() > 0) {
            this.setTitle(title);
        }

        this.setContentView(this.mView);
    }
}
