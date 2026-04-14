package messages;

import madkit.kernel.Message;

public class AlerteMessage extends Message {

    public enum NiveauAlerte { NORMAL, ATTENTION, ALERTE, CRITIQUE }

    private NiveauAlerte niveau;
    private String       description;
    private double       niveauEau;

    public AlerteMessage(NiveauAlerte niveau, String description, double niveauEau) {
        this.niveau      = niveau;
        this.description = description;
        this.niveauEau   = niveauEau;
    }

    public NiveauAlerte getNiveau()      { return niveau; }
    public String       getDescription() { return description; }
    public double       getNiveauEau()   { return niveauEau; }

    @Override
    public String toString() {
        return "[Alerte:" + niveau + "] " + description + " (eau=" + niveauEau + "m)";
    }
}
