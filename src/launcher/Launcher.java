package launcher;

import madkit.kernel.Madkit;

import surveillance.*;
import commandement.*;
import intervention.*;

public class Launcher {

    public static void main(String[] args) {

        System.out.println("=== Simulation - Gestion de crue (MadKit 5) ===");

        Madkit.main(new String[] {
            "--launchAgents",
            AgentCapteur.class.getName()      + ",true,1;" +
            AgentMeteo.class.getName()        + ",true,1;" +
            AgentInspecteur.class.getName()   + ",true,1;" +
            AgentCoordinateur.class.getName() + ",true,1;" +
            AgentAlerte.class.getName()       + ",true,1;" +
            AgentVanne.class.getName()        + ",true,1;" +
            AgentEvacuateur.class.getName()   + ",true,1;" +
            AgentSecouriste.class.getName()   + ",true,1"
        });
    }
}
