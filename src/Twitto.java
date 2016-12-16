package sci;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.util.Logger;
import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

public class Twitto extends Agent {

	protected int id;

	protected List<Twitto> followers = new ArrayList<Twitto>();
	protected List<Twitto> following = new ArrayList<Twitto>();

	private Logger myLogger = Logger.getMyLogger(getClass().getName());

	private class TwitterBehaviour extends CyclicBehaviour {

		public TwitterBehaviour(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = myAgent.receive();
			if((msg != null) && (msg.getPerformative() == ACLMessage.REQUEST)){

				String sender = msg.getSender().getLocalName();
				int i = 0;
				while((i < sender.length()) && (sender.charAt(i) < '0' || sender.charAt(i) > '9')) i++;
				if (i < sender.length()) {
					myLogger.log(Logger.INFO, sender.substring(i));
					int who = Integer.parseInt(sender.substring(i));

					boolean isIn = false;
					for(Twitto tw : following) isIn = isIn || (who == tw.id);
					if(isIn) {

						StringBuilder sb = new StringBuilder();
						sb.append("Agent "+getLocalName()+" : I have "+followers.size()+" followers (");	
						for(Twitto tw : followers) sb.append(tw.id+" ");
						sb.append(") and "+following.size()+" following me (");
						for(Twitto tw : following) sb.append(tw.id+" ");
						sb.append(")");				
						myLogger.log(Logger.INFO, sb.toString());

						ACLMessage reply = msg.createReply();	
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent("");
						send(reply);

					} else {

						myLogger.log(Logger.INFO, "Agent "+getLocalName()+" : Sorry, I won't answer. You don't follow me...");

					}
				}

			} else if ((msg != null) && (msg.getPerformative() == ACLMessage.INFORM)) {
				myLogger.log(Logger.INFO,"Agent "+getLocalName()+" : Ok, thanks for answering "+msg.getSender().getLocalName()+" !");
			} else {
				block();
			}
		
		}

	}

	public void addFollower(Twitto t){
		this.followers.add(t);
	}

	public void addFollowing(Twitto t){
		this.following.add(t);
	}

	protected void setup() {
		// Registration with the DF 
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();   
		sd.setType("Twitto"); 
		sd.setName(getName());
		sd.setOwnership("TILAB");
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this,dfd);
			TwitterBehaviour TB = new TwitterBehaviour(this);
			addBehaviour(TB);
		} catch (FIPAException e) {
			myLogger.log(Logger.SEVERE, "Agent "+getLocalName()+" - Cannot register with DF", e);
			doDelete();
		}
	}


}
