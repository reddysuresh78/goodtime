/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package goodtime.com.goodtime;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Budget demo pie chart.
 */
public class ClockBuilder {

    public DefaultRenderer getRenderer(int[] colors, String title, int startingAngle) {

        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextSize(20);
        renderer.setDisplayValues(false);
        renderer.setShowLabels(true);
        renderer.setChartTitle(title);
        renderer.setClickEnabled(true);

        renderer.setStartAngle(startingAngle);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(4);
        r.setGradientEnabled(true);
        r.setGradientStart(1, Color.BLUE);
        r.setGradientStop(1, Color.GREEN);
        r.setHighlighted(true);
        r.setShowLegendItem(false);
        r.setHighlighted(true);

        renderer.setShowLegend(false);

        return renderer;

    }

    protected CategorySeries buildCategoryDataset(String title, String[] labels, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            series.add(labels[k], value);
            k++;
        }

        return series;
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            r.setDisplayBoundingPoints(true);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    public GraphicalView buildClock(Context context, String title, int startingAngle, int distance, String[] labels) {

        GraphicalView chartView;

        double[] values = new double[12];
        int[] colors = new int[12];

        String[] planets = new String[12];
        //{ Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };

        for (int i = 0; i < 12; i++) {
            values[i] = distance;
            if (i == 2 || i == 6) {
                colors[i] = Color.RED;
                planets[i] = "Bad";
            } else {
//                colors[i] = Color.BLACK;
                planets[i] = "Good";
            }
            planets[i] = i + ":00 AM - " + (i + 1) + ":00 AM " + planets[i];
            planets[i] = labels[i];

        }

        DefaultRenderer renderer = getRenderer(colors, title, startingAngle);
        CategorySeries series = buildCategoryDataset(title, planets, values);

        chartView = ChartFactory.getPieChartView(context, series, renderer);
        return chartView;

    }

}
