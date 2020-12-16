package meeting.scheduler;

import java.util.ArrayList;


public class Day {
    private int numberOfDay;
    private ArrayList<TimeSlot> timeSlots;

    public Day(int numberOfDay) {
        this.setNumberOfDay(numberOfDay);
        this.setTimeSlots(new ArrayList<>());
        initTimeSlots();
    }

    private void initTimeSlots() {
        for (int i = 0; i < Settings.numTimeSlots; i++) {
            this.getTimeSlots().add(new TimeSlot());
        }
    }

    public int getNumberOfDay() {
        return numberOfDay;
    }

    public void setNumberOfDay(int numberOfDay) {
        this.numberOfDay = numberOfDay;
    }

    public ArrayList<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public String toString() {
   
    //public TimeSlotWrapper getTimeSlotData() {
    	TimeSlotWrapper tsw = new TimeSlotWrapper();
    	String[] slotArray = {"10:00", "11:00", "14:00", "15:00"};
        String s = numberOfDay + ": {  ";
        int i = 0;
        for (TimeSlot t: timeSlots)
        {
        	TimeSlotWrapper.TimeSlotAvailability tsa = new TimeSlotWrapper().new TimeSlotAvailability();
        	tsa.time = slotArray[i];
        	tsa.availability = t.preference;
        	i++;
        	if (t.preference == 0.0) {
        		s += "NA  " ; }
        		else {
        			s += tsa.time + "  ";
        		}
        	tsw.arrayList.add(tsa);
        }
        s += "}";
        return String.valueOf(s);
    }
}
