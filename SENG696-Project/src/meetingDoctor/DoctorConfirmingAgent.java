
package meetingDoctor;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

public class DoctorConfirmingAgent extends Agent {
	
	private Hashtable catalogue;
	
	private SchedulerGui myGui;
	
	Random rand = new Random();
	private int sold = rand.nextInt(100) + 1;;
	String title;
	Integer price;

	
	// Put agent initializations here
	protected void setup() {
		
		catalogue = new Hashtable();

		// Create and show the GUI 
		myGui = new SchedulerGui(this);
		myGui.showGui();
		
		

		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Schedule/Appointment booking");
		sd.setName("JADE-Appointment_Booking");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		
		addBehaviour(new OfferRequestsServer());

		
		addBehaviour(new PurchaseOrdersServer());
	}

	protected void takeDown() {
		
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		myGui.dispose();
		// Printout a dismissal message
		System.out.println("Doctor-agent "+getAID().getName().split("@")[0]+" terminating.");
	}

	
	public void updateCatalogue(final String title, final int price) {
		
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				catalogue.put(title, new Integer(price));
				System.out.println(getAID().getName().split("@")[0]+"   "+title+" is the registered symptom. No. of days with symptoms (Survey by Ucalgary students due to covid19) = "+price);
			}
		} );
		
     
	}

	
	private class OfferRequestsServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			//System.out.println("getContent::00-"+msg);
			if (msg != null) {
				//System.out.println("getContent::"+msg.getContent());
				if(msg.getContent().equals("sold")){
					int p = (price.intValue()- sold);
					ACLMessage soldreply = msg.createReply();
					System.out.println(getAID().getName().split("@")[0]+": yes, we have a first come first serve for severe injuries :"+title+" Booking cnsiderations "+p);
					soldreply.setPerformative(ACLMessage.PROPOSE);
					soldreply.setContent(String.valueOf(p));	
				
					myAgent.send(soldreply);
					
					
				}
				
				ACLMessage reply = msg.createReply();
				title = msg.getContent();
				price = (Integer) catalogue.get(title);
				
				if (price != null) {
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
					System.out.println(getAID().getName().split("@")[0]+": hello, your scheduled number "+title+" is="+price);
}
				
				else {
					
					
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}

	
	private class PurchaseOrdersServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = (Integer) catalogue.remove(title);
				if (price != null) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title+" booked to agent "+msg.getSender().getName());
				}
				else {
					
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}  
}
