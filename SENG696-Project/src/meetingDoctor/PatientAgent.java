package meetingDoctor;

import jade.core.Agent;

import javax.swing.JOptionPane;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PatientAgent extends Agent {
	private String Symptoms;
	private AID[] patient;
	private AID[] Bestpatient;
	protected void setup() {
		System.out.println(" Patient-agent "+getAID().getName()+" arrival");
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			Symptoms = (String) args[0];
			System.out.println("Symptoms "+Symptoms);

			
			addBehaviour(new TickerBehaviour(this, 15000) {
				protected void onTick() {
					System.out.println("S"
							+ ""+Symptoms);
					
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("appoinment booking");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template); 
						System.out.println("Found the following patients agents:");
						patient = new AID[result.length];
						 Bestpatient = new AID[3];

						for (int i = 0; i < result.length; ++i) {
							patient[i] = result[i].getName();
							System.out.println(patient[i].getName());
						}
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}

					// Perform the request
					myAgent.addBehaviour(new RequestPerformer());
				}
			} );
		}
		else {
			// Make the agent terminate
			System.out.println("No patient booking specified");
			doDelete();
		}
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("patient-agent "+getAID().getName()+"cancelled");
	}

	
	private class RequestPerformer extends Behaviour {
		private AID bestSeller;  
		private int bestPrice; 
		private ACLMessage reply2;
		private int nbestPrice; 
		private int repliesCnt = 0; 
		private MessageTemplate mt;
		private MessageTemplate mt1;// The template to receive replies
		private int step = 0;
		int i=0;
		public void action() {
			switch (step) {
			case 0:
				
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < patient.length; ++i) {
					cfp.addReceiver(patient[i]);
				} 
				cfp.setContent(Symptoms);
				cfp.setConversationId("Appointnment booking");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				System.out.println("Doctor: Hello, how can i help you");
				

				myAgent.send(cfp);
				
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Appointnment booking"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					
					

					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						
						int price = Integer.parseInt(reply.getContent());
						Bestpatient[i]=reply.getSender();
						System.out.println("---------------"+i+"----"+Bestpatient[i]);
						i++;
						if (bestSeller == null || price < bestPrice) {
							
							bestPrice = price;
							bestSeller = reply.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= patient.length) {
						
						step = 2; 
					}
				}
				else {
					block();
				}
				break;
			case 2:			
				
				ACLMessage cfp1 = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < Bestpatient.length; ++i) {
					cfp1.addReceiver(Bestpatient[i]);
					System.out.println("++++++++"+i+"+++++"+Bestpatient[i]);

				} 
				
				cfp1.setContent("sold");
				cfp1.setConversationId("book-trade");
				cfp1.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				System.out.println("Patient: Can i have suggestions on  "+Symptoms+"?");
				bestSeller= null;
				repliesCnt=0;
				myAgent.send(cfp1);
				
				mt1 = MessageTemplate.and(MessageTemplate.MatchConversationId("Appointnment booking"),
						MessageTemplate.MatchInReplyTo(cfp1.getReplyWith()));
				step = 3;
				break;
			case 3:
				
				reply2 = myAgent.receive(mt1);
				
				if (reply2 != null) {
					// Reply received²
				

					if (reply2.getPerformative() == ACLMessage.PROPOSE) {
						 
						int price = Integer.parseInt(reply2.getContent());
						
						
						if (bestSeller == null || price < nbestPrice) {
							
							nbestPrice = price;
							bestSeller = reply2.getSender();
							

						}
					}
					repliesCnt++;
					if (repliesCnt >= patient.length) {
						
						step = 4; 
					}
				}
				else {
					block();
				}
				break;
				
			case 4:
				
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(Symptoms);
				order.setConversationId("Appointnment booking");
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Appointnment booking"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 5;
				break;
			case 5:      
				
				reply = myAgent.receive(mt);
				if (reply != null) {
					
					if (reply.getPerformative() == ACLMessage. INFORM) {
						
						System.out.println(Symptoms+" successfully scheduled from agent "+reply.getSender().getName());
						System.out.println("Price = "+nbestPrice);
						JOptionPane.showMessageDialog(null, "successfully scheduled from agent : "+reply.getSender().getName().split("@")[0]+" with price of "+nbestPrice);
						myAgent.doDelete();
					}
					else {
						System.out.println("Attempt failed: request cannot be booked.");
					}

					step = 6;
				}
				else {
					block();
					
				}
				break;
			}        
		}

		public boolean done() {
			if (step == 2 && bestSeller == null) {
				System.out.println("for the mild symptom "+Symptoms+" try booking again");
			}
			return ((step == 2 && bestSeller == null) || step == 6);
		}
	}  // End of inner class RequestPerformer
}