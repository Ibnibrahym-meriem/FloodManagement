package commandement;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import messages.AlerteMessage;

/**
 * Agent Alerte - Groupe Commandement - Role : Notificateur
 * Recoit les alertes du Coordinateur et les diffuse aux citoyens.
 */
public class AgentAlerte extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Commandement";
    public static final String ROLE       = "Notificateur";

    private AlerteMessage.NiveauAlerte dernierNiveau = AlerteMessage.NiveauAlerte.NORMAL;

    @Override
    protected void activate() {
        getLogger().info("=== AgentAlerte active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
    }

    @Override
    protected void live() {
        while (isAlive()) {
            Message msg = waitNextMessage(3000);

            if (msg instanceof AlerteMessage) {
                traiterAlerte((AlerteMessage) msg);
            }
        }
    }

    private void traiterAlerte(AlerteMessage alerte) {
        if (alerte.getNiveau() == dernierNiveau) return;

        dernierNiveau = alerte.getNiveau();

        switch (alerte.getNiveau()) {
            case CRITIQUE:
                getLogger().severe(
                    "ALERTE CRITIQUE DIFFUSEE AUX CITOYENS\n" +
                    "   Niveau eau : " + alerte.getNiveauEau() + "m\n" +
                    "   Message    : " + alerte.getDescription() + "\n" +
                    "   Canal      : SMS + Sirenes + Radio urgence"
                );
                break;
            case ALERTE:
                getLogger().warning(
                    "ALERTE DIFFUSEE AUX CITOYENS\n" +
                    "   Niveau eau : " + alerte.getNiveauEau() + "m\n" +
                    "   Message    : " + alerte.getDescription() + "\n" +
                    "   Canal      : SMS + Application mobile"
                );
                break;
            case ATTENTION:
                getLogger().info("AVIS D'ATTENTION : " + alerte.getDescription());
                break;
            case NORMAL:
                getLogger().info("LEVEE D'ALERTE - situation normalisee : " + alerte.getDescription());
                break;
        }
    }

    @Override
    protected void end() {
        getLogger().info("AgentAlerte arrete.");
    }
}
