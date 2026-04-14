package messages;

import madkit.kernel.Message;

public class OrdreMessage extends Message {

    public enum TypeOrdre {
        OUVRIR_VANNES,
        FERMER_VANNES,
        EVACUER_ZONE,
        DECLENCHER_SECOURS,
        RAPPORT_TERRAIN,
        STANDBY
    }

    private TypeOrdre typeOrdre;
    private String    details;
    private int       priorite; // 1 (basse) a 5 (urgence max)

    public OrdreMessage(TypeOrdre typeOrdre, String details, int priorite) {
        this.typeOrdre = typeOrdre;
        this.details   = details;
        this.priorite  = priorite;
    }

    public TypeOrdre getTypeOrdre() { return typeOrdre; }
    public String    getDetails()   { return details; }
    public int       getPriorite()  { return priorite; }

    @Override
    public String toString() {
        return "[Ordre P" + priorite + "] " + typeOrdre + " - " + details;
    }
}
