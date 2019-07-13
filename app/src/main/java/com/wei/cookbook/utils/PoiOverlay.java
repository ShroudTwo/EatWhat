package com.wei.cookbook.utils;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.services.core.PoiItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PoiOverlay {
    private List<PoiItem> a;
    private AMap b;
    private ArrayList<Marker> c = new ArrayList();

    public PoiOverlay(AMap var1, List<PoiItem> var2) {
        this.b = var1;
        this.a = var2;
    }

    public void addToMap() {
        for(int var1 = 0; var1 < this.a.size(); ++var1) {
            Marker var2 = this.b.addMarker(this.a(var1));
            var2.setObject(var1);
            this.c.add(var2);
        }

    }

    public void removeFromMap() {
        Iterator var1 = this.c.iterator();

        while(var1.hasNext()) {
            Marker var2 = (Marker)var1.next();
            var2.remove();
        }

    }

    public void zoomToSpan() {
        if (this.a != null && this.a.size() > 0) {
            if (this.b == null) {
                return;
            }

            if (this.a.size() == 1) {
                this.b.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(((PoiItem)this.a.get(0)).getLatLonPoint().getLatitude(), ((PoiItem)this.a.get(0)).getLatLonPoint().getLongitude()), 18.0F));
            } else {
                LatLngBounds var1 = this.a();
                this.b.moveCamera(CameraUpdateFactory.newLatLngBounds(var1, 5));
            }
        }

    }

    private LatLngBounds a() {
        Builder var1 = LatLngBounds.builder();

        for(int var2 = 0; var2 < this.a.size(); ++var2) {
            var1.include(new LatLng(((PoiItem)this.a.get(var2)).getLatLonPoint().getLatitude(), ((PoiItem)this.a.get(var2)).getLatLonPoint().getLongitude()));
        }

        return var1.build();
    }

    private MarkerOptions a(int var1) {
        return (new MarkerOptions()).position(new LatLng(((PoiItem)this.a.get(var1)).getLatLonPoint().getLatitude(), ((PoiItem)this.a.get(var1)).getLatLonPoint().getLongitude())).title(this.getTitle(var1)).snippet(this.getSnippet(var1)).icon(this.getBitmapDescriptor(var1));
    }

    protected BitmapDescriptor getBitmapDescriptor(int var1) {
        return null;
    }

    protected String getTitle(int var1) {
        return ((PoiItem)this.a.get(var1)).getTitle();
    }

    protected String getSnippet(int var1) {
        return ((PoiItem)this.a.get(var1)).getSnippet();
    }

    public int getPoiIndex(Marker var1) {
        for(int var2 = 0; var2 < this.c.size(); ++var2) {
            if (((Marker)this.c.get(var2)).equals(var1)) {
                return var2;
            }
        }

        return -1;
    }

    public PoiItem getPoiItem(int var1) {
        return var1 >= 0 && var1 < this.a.size() ? (PoiItem)this.a.get(var1) : null;
    }
}

