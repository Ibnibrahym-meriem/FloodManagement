package messages;

import madkit.kernel.Message;

public class MeteoMessage extends Message {

    private double precipitations;  // mm/h prevues
    private double risqueCrue;      // 0.0 a 1.0
    private String prevision;       // texte lisible

    public MeteoMessage(double precipitations, double risqueCrue, String prevision) {
        this.precipitations = precipitations;
        this.risqueCrue     = risqueCrue;
        this.prevision      = prevision;
    }

    public double getPrecipitations() { return precipitations; }
    public double getRisqueCrue()     { return risqueCrue; }
    public String getPrevision()      { return prevision; }

    @Override
    public String toString() {
        return "[Meteo] pluie=" + precipitations + "mm/h | risque="
               + (int)(risqueCrue * 100) + "% | " + prevision;
    }
}
