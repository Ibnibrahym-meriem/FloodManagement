package surveillance;

import madkit.kernel.Agent;
import messages.MeteoMessage;

/**
 * Agent Meteo - Groupe : Surveillance - Role : Predicteur
 * Genere des previsions meteorologiques toutes les 10 secondes.
 */
public class AgentMeteo extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Surveillance";
    public static final String ROLE       = "Predicteur";

    private int cycle = 0;

    @Override
    protected void activate() {
        getLogger().info("=== AgentMeteo active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
    }

    @Override
    protected void live() {
        while (isAlive()) {
            MeteoMessage msg = genererPrevision();
            getLogger().info("Prevision : " + msg);

            sendMessage(COMMUNAUTE, "Commandement", "Decideur", msg);

            pause(10000);
        }
    }

    private MeteoMessage genererPrevision() {
        cycle++;
        double pluie, risque;
        String texte;

        if (cycle <= 3) {
            pluie = 5.0;  risque = 0.1;
            texte = "Temps calme, faible risque";
        } else if (cycle <= 6) {
            pluie = 25.0; risque = 0.5;
            texte = "Pluies moderees, surveillance recommandee";
        } else {
            pluie = 60.0; risque = 0.9;
            texte = "Fortes pluies imminentes, risque crue eleve!";
        }

        return new MeteoMessage(pluie, risque, texte);
    }

    @Override
    protected void end() {
        getLogger().info("AgentMeteo arrete.");
    }
}
