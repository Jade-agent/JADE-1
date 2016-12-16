package sci;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.Random;


public class BoomNetwork extends Agent {
  protected int nbAgents = 10;
  protected BoomAgent[] agents;

  protected void setup() {
    agents = new BoomAgent[nbAgents];

    try {
      ContainerController cc;
      AgentController ac;
      cc = getContainerController();
      for(int i = 0; i < nbAgents; i++) {
        BoomAgent agent = new BoomAgent();
        ac = cc.acceptNewAgent("Candidat" + i, agent);
        ac.start();
        agents[i] = agent;
      }

    } catch (Exception e) {
      System.out.println(e);
    }

    System.out.println("Welcome to the show !");
   	// Make this agent terminate
  	doDelete();
  }
}
