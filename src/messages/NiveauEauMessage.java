package messages;

import madkit.kernel.Message;

public class NiveauEauMessage extends Message {

    private double niveauActuel;   // en metres
    private double vitesseMontee;  // en m/h
    private String statut;         // "NORMAL", "ALERTE", "CRITIQUE"
    private long   timestamp;

    public NiveauEauMessage(double niveauActuel, double vitesseMontee) {
        this.niveauActuel  = niveauActuel;
        this.vitesseMontee = vitesseMontee;
        this.timestamp     = System.currentTimeMillis();

        if (vitesseMontee >= 5.0) {
            this.statut = "CRITIQUE";
        } else if (vitesseMontee >= 2.0) {
            this.statut = "ALERTE";
        } else {
            this.statut = "NORMAL";
        }
    }

    public double getNiveauActuel()  { return niveauActuel; }
    public double getVitesseMontee() { return vitesseMontee; }
    public String getStatut()        { return statut; }
    public long   getTimestamp()     { return timestamp; }

    @Override
    public String toString() {
        return "[NiveauEau] niveau=" + niveauActuel + "m | vitesse=" + vitesseMontee
                + "m/h | statut=" + statut;
    }
}
