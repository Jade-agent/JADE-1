package sci;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.Random;


public class TwitterNetwork extends Agent {
  protected int nbAgents = 15;
  protected Twitto[] agents;

  protected void setup() {
	Random dice = new Random();
	double probaFollowing = 0.69;
    agents = new Twitto[nbAgents];

    try {
      ContainerController cc;
      AgentController ac;
      cc = getContainerController();
      for(int i = 0; i < nbAgents; i++) {
        Twitto agent = new Twitto();
        ac = cc.acceptNewAgent("Twitto" + i, agent);
        ac.start();
        agents[i] = agent;
	agent.id = i;
      }

      for(int i = 0; i < nbAgents; i++) {
        for(int j = 0; j < nbAgents; j++) {
		if(i == j) continue;
          if(dice.nextFloat() < probaFollowing) {
            Twitto t1 = agents[i];
            Twitto t2 = agents[j];
            t1.addFollower(t2);
            t2.addFollowing(t1);
          }
        }
      }

    } catch (Exception e) {
      System.out.println(e);
    }

    System.out.println("Twitter begins !");
   	// Make this agent terminate
  	doDelete();
  }
}
