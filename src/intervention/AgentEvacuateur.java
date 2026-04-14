package intervention;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import messages.OrdreMessage;

/**
 * Agent Evacuateur - Groupe Intervention - Role : Organisateur
 * Gere le deplacement des populations des zones a risque.
 */
public class AgentEvacuateur extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Intervention";
    public static final String ROLE       = "Organisateur";

    private boolean evacuationEnCours = false;
    private int     personnesEvacuees = 0;

    @Override
    protected void activate() {
        getLogger().info("=== AgentEvacuateur active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
    }

    @Override
    protected void live() {
        while (isAlive()) {
            Message msg = waitNextMessage(5000);

            if (msg instanceof OrdreMessage) {
                executerOrdre((OrdreMessage) msg);
            }
        }
    }

    private void executerOrdre(OrdreMessage ordre) {
        getLogger().info("Ordre recu : " + ordre);

        switch (ordre.getTypeOrdre()) {
            case EVACUER_ZONE:
                if (!evacuationEnCours) {
                    evacuationEnCours = true;
                    if (ordre.getPriorite() >= 5) {
                        personnesEvacuees = 3500;
                        getLogger().severe(
                            ">>> EVACUATION TOTALE DECLENCHEE !\n" +
                            "    Zones : A1, A2, B1, B2\n" +
                            "    Personnes evacuees : " + personnesEvacuees + "\n" +
                            "    Points de rassemblement : Stade, Ecole centrale\n" +
                            "    Routes : RN7 et D114 activees"
                        );
                    } else {
                        personnesEvacuees = 800;
                        getLogger().warning(
                            ">>> EVACUATION PREVENTIVE zone A1\n" +
                            "    Personnes evacuees : " + personnesEvacuees + "\n" +
                            "    Point de rassemblement : Gymnase municipal"
                        );
                    }
                    envoyerRapport("Evacuation : " + personnesEvacuees + " personnes deplacees");
                } else {
                    getLogger().info("Evacuation deja en cours.");
                }
                break;

            case STANDBY:
                if (evacuationEnCours) {
                    getLogger().info(">>> Retour des populations - evacuation terminee");
                    evacuationEnCours = false;
                    personnesEvacuees = 0;
                }
                break;

            default:
                getLogger().info("Ordre non applicable : " + ordre.getTypeOrdre());
        }
    }

    private void envoyerRapport(String details) {
        sendMessage(COMMUNAUTE, "Commandement", "Decideur",
            new OrdreMessage(OrdreMessage.TypeOrdre.RAPPORT_TERRAIN,
                "AgentEvacuateur : " + details, 3));
    }

    @Override
    protected void end() {
        getLogger().info("AgentEvacuateur arrete. Personnes evacuees : " + personnesEvacuees);
    }
}
