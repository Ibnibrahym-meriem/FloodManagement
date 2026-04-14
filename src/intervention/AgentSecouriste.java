package intervention;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import messages.OrdreMessage;

/**
 * Agent Secouriste - Groupe Intervention - Role : Sauveteur
 * Intervient aupres des victimes en situation de crue.
 */
public class AgentSecouriste extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Intervention";
    public static final String ROLE       = "Sauveteur";

    private boolean interventionEnCours = false;
    private int     victimesSecourues   = 0;

    @Override
    protected void activate() {
        getLogger().info("=== AgentSecouriste active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
        getLogger().info("Equipes de secours en attente d'ordre.");
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
            case DECLENCHER_SECOURS:
                if (!interventionEnCours) {
                    interventionEnCours = true;
                    victimesSecourues   = simulerIntervention(ordre.getPriorite());
                    getLogger().severe(
                        ">>> EQUIPES DE SECOURS DEPLOYEES !\n" +
                        "    Priorite           : " + ordre.getPriorite() + "/5\n" +
                        "    Zone               : " + ordre.getDetails() + "\n" +
                        "    Victimes secourues : " + victimesSecourues + "\n" +
                        "    Moyens engages     : " + getMoyensEngages(ordre.getPriorite())
                    );
                    envoyerRapport(victimesSecourues + " victimes secourues");
                } else {
                    getLogger().info("Intervention deja en cours - renfort demande.");
                    victimesSecourues += simulerIntervention(ordre.getPriorite());
                    envoyerRapport("Renfort : total " + victimesSecourues + " victimes");
                }
                break;

            case STANDBY:
                if (interventionEnCours) {
                    getLogger().info(">>> Fin d'intervention. Retour a la base.");
                    interventionEnCours = false;
                }
                break;

            default:
                getLogger().info("Ordre non applicable : " + ordre.getTypeOrdre());
        }
    }

    private int simulerIntervention(int priorite) {
        return priorite * 12;
    }

    private String getMoyensEngages(int priorite) {
        if (priorite >= 5) {
            return "4 equipes GRIMP + 2 helicopteres + 3 zodiacs";
        } else if (priorite >= 3) {
            return "2 equipes GRIMP + 1 zodiac";
        } else {
            return "1 equipe de premiere intervention";
        }
    }

    private void envoyerRapport(String details) {
        sendMessage(COMMUNAUTE, "Commandement", "Decideur",
            new OrdreMessage(OrdreMessage.TypeOrdre.RAPPORT_TERRAIN,
                "AgentSecouriste : " + details, 4));
    }

    @Override
    protected void end() {
        getLogger().info("AgentSecouriste arrete. Total victimes secourues : " + victimesSecourues);
    }
}
