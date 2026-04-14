package surveillance;

import madkit.kernel.Agent;
import messages.NiveauEauMessage;

/**
 * Agent Capteur - Groupe : Surveillance - Role : Mesureur
 * Mesure le niveau de l'eau toutes les 5 secondes et envoie les donnees.
 */
public class AgentCapteur extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Surveillance";
    public static final String ROLE       = "Mesureur";

    private double niveauEau    = 10.0;
    private double vitesseMontee = 0.5;

    @Override
    protected void activate() {
        getLogger().info("=== AgentCapteur active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
    }

    @Override
    protected void live() {
        while (isAlive()) {
            // Simulation montee progressive de l'eau
            niveauEau    += vitesseMontee * 0.1;
            vitesseMontee = Math.min(vitesseMontee + 0.05, 6.0);

            NiveauEauMessage msg = new NiveauEauMessage(niveauEau, vitesseMontee);
            getLogger().info("Mesure : " + msg);

            sendMessage(COMMUNAUTE, "Commandement", "Decideur", msg);

            pause(5000);
        }
    }

    @Override
    protected void end() {
        getLogger().info("AgentCapteur arrete. Dernier niveau : " + niveauEau + "m");
    }
}
