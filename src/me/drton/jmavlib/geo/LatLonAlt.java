package me.drton.jmavlib.geo;

/**
 * User: ton Date: 05.05.14 Time: 8:41
 */
public class LatLonAlt {
    public static double RADIUS_OF_EARTH = 6371000.0;

    public final double lat;
    public final double lon;
    public final double alt;

    public LatLonAlt(double lat, double lon, double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    public LatLonAlt add(double[] vector) {
        return new LatLonAlt(
                lat + vector[0] / RADIUS_OF_EARTH * 180.0 / Math.PI,
                lon + vector[1] / (Math.cos(lat * Math.PI / 180.0) * RADIUS_OF_EARTH) * 180.0 / Math.PI,
                alt - vector[2]);
    }

    public double[] sub(LatLonAlt pos) {
        return new double[]{
                (lat - pos.lat) * Math.PI / 180.0 * RADIUS_OF_EARTH,
                (lon - pos.lon) * Math.PI / 180.0 * Math.cos(lat * Math.PI / 180.0) * RADIUS_OF_EARTH,
                pos.alt - alt
        };
    }

    @Override
    public LatLonAlt clone() {
        return new LatLonAlt(lat, lon, alt);
    }
}
