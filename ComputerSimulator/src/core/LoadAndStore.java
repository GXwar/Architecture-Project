package core;
import gui.controllers.EngineerConsoleController;

public class LoadAndStore extends ISA{
    private static String stepInformation;
    // record if we should update the memory context in  Monitor
    private static boolean memoryInformation;

    //******************************************************************************************************************************
    public static void LDR(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
        //get the content in memory using address in MAR, and load it to MBR.
        Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
        //Execute the operation move data to IRR
        cpu.getIRR().setContent(cpu.getMBR().getContent());
        stepInformation="Execute:IRR<=MBR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

        //select the register and put the data into register
        switch (R){
            case"00":
                cpu.getR0().setContent(cpu.getIRR().getContent());
                break;
            case "01":
                cpu.getR1().setContent(cpu.getIRR().getContent());
                break;
            case "10":
                cpu.getR2().setContent(cpu.getIRR().getContent());
                break;
            case "11":
                cpu.getR3().setContent(cpu.getIRR().getContent());
        }
        stepInformation="Execute:Register<=IRR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
    }
    
    //LDR without single step halt
    public static void LDRNHLT(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        CPU.cyclePlusOne();
        //get the content in memory using address in MAR, and load it to MBR.
        Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
        CPU.cyclePlusOne();
        //Execute the operation move data to IRR
        cpu.getIRR().setContent(cpu.getMBR().getContent());
        CPU.cyclePlusOne();
        //select the register and put the data into register
        switch (R){
            case"00":
                cpu.getR0().setContent(cpu.getIRR().getContent());
                break;
            case "01":
                cpu.getR1().setContent(cpu.getIRR().getContent());
                break;
            case "10":
                cpu.getR2().setContent(cpu.getIRR().getContent());
                break;
            case "11":
                cpu.getR3().setContent(cpu.getIRR().getContent());
        }
        CPU.cyclePlusOne();
    }

    //LDR for Tomasulo
    public static String LDRTom(String operand1){
    		if(operand1.length() == 12) {
    			 //set MAR register
    	        cpu.getMAR().setContent(operand1);
    	        CPU.cyclePlusOne();
    	        //get the content in memory using address in MAR, and load it to MBR.
    	        Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
    	        CPU.cyclePlusOne();
    	        //Execute the operation move data to IRR
    	        cpu.getIRR().setContent(cpu.getMBR().getContent());
    	        CPU.cyclePlusOne();
    	        //Return the result
    	        return CPU.getInstance().getIRR().getContent();
    		}else if(operand1.length() == 16) {
    			return operand1;
    		} 
    		return "LDR ERROR";
    }
    
  //******************************************************************************************************************************
    public static void STR(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
        
        // select register and put data into IRR which takes one cycle
        switch (R){
            case "00":
                cpu.getIRR().setContent(cpu.getR0().getContent());
                break;
            case "01":
                cpu.getIRR().setContent(cpu.getR1().getContent());
                break;
            case "10":
                cpu.getIRR().setContent(cpu.getR2().getContent());
                break;
            case "11":
                cpu.getIRR().setContent(cpu.getR3().getContent());
                break;
        }
        stepInformation=("Execute:IRR<=Register");
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

        //move data from IRR to MBR which takes one cycle
        cpu.getMBR().setContent(cpu.getIRR().getContent());
        stepInformation="Execute:MBR<=IRR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

        //put data into cache and then write back to memory which takes one cycle
        Cache.getInstance().writeBack(cpu.getMAR().getContent(),cpu.getMBR().getContent());
        memoryInformation=true;
        stepInformation=("Execute:Memory[MAR]<=Cache<=MBR");
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
    }

   //STR without single step halt
    public static void STRNHLT(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        CPU.cyclePlusOne();
        
        // select register and put data into IRR which takes one cycle
        switch (R){
            case "00":
                cpu.getIRR().setContent(cpu.getR0().getContent());
                break;
            case "01":
                cpu.getIRR().setContent(cpu.getR1().getContent());
                break;
            case "10":
                cpu.getIRR().setContent(cpu.getR2().getContent());
                break;
            case "11":
                cpu.getIRR().setContent(cpu.getR3().getContent());
                break;
        }
        CPU.cyclePlusOne();

        //move data from IRR to MBR which takes one cycle
        cpu.getMBR().setContent(cpu.getIRR().getContent());
        CPU.cyclePlusOne();

        //put data into cache and then write back to memory which takes one cycle
        Cache.getInstance().writeBack(cpu.getMAR().getContent(),cpu.getMBR().getContent());
        CPU.cyclePlusOne();
    }
    
  //STR for Tomasulo
    public static String STRTom(String operand){
        if(operand.length() == 2) {
        		switch (operand){
	            case "00":
	                return CPU.getInstance().getR0().getContent();
	            case "01":
	            		return CPU.getInstance().getR1().getContent();
	            case "10":
	            		return CPU.getInstance().getR2().getContent();
	            case "11":
	                return CPU.getInstance().getR3().getContent();
	        }
        }else if(operand.length() == 16) {
        		return operand;
        }
        return "STR Error";
    }

  //******************************************************************************************************************************
    public static void LDA(){
        //put EA into Register from IAR
        switch (R){
            case "00":
                cpu.getR0().setContent(cpu.getIAR().getContent());
                break;
            case "01":
                cpu.getR1().setContent(cpu.getIAR().getContent());
            case "10":
                cpu.getR2().setContent(cpu.getIAR().getContent());
            case "11":
                cpu.getR3().setContent(cpu.getIAR().getContent());
        }
        stepInformation="Execute:Register<=IAR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
    }
    
   //LDA without single step halt
    public static void LDANHLT(){
        //put EA into Register from IAR
        switch (R){
            case "00":
                cpu.getR0().setContent(cpu.getIAR().getContent());
                break;
            case "01":
                cpu.getR1().setContent(cpu.getIAR().getContent());
            case "10":
                cpu.getR2().setContent(cpu.getIAR().getContent());
            case "11":
                cpu.getR3().setContent(cpu.getIAR().getContent());
        }
        CPU.cyclePlusOne();
    }
    
    //LDA for Tomasulo
    public static String LDATom(String operand){
        return operand;
    }
    
  //******************************************************************************************************************************
    public static void LDX(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
        //fetch the data from cache according to MAR
        Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
        //put data into IRR
        cpu.getIRR().setContent(cpu.getMBR().getContent());
        stepInformation="Execute:IRR<=MBR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
        //put the data from IRR into index register
        switch (X){
            case "01":
                cpu.getX1().setContent(cpu.getIRR().getContent());
                break;
            case "10":
                cpu.getX2().setContent(cpu.getIRR().getContent());
                break;
            case "11":
                cpu.getX3().setContent(cpu.getIRR().getContent());
        }
        stepInformation="Execute:Register<=IRR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();
    }
    
   //LDX without single step halt
    public static void LDXNHLT(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        CPU.cyclePlusOne();
        //fetch the data from cache according to MAR
        Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
        CPU.cyclePlusOne();
        //put data into IRR
        cpu.getIRR().setContent(cpu.getMBR().getContent());
        CPU.cyclePlusOne();
        //put the data from IRR into index register
        switch (X){
            case "01":
                cpu.getX1().setContent(cpu.getIRR().getContent());
                break;
            case "10":
                cpu.getX2().setContent(cpu.getIRR().getContent());
                break;
            case "11":
                cpu.getX3().setContent(cpu.getIRR().getContent());
        }
        CPU.cyclePlusOne();
    }

    //LDX for Tomasulo
    public static String LDXTom(String operand){
    		if(operand.length() == 12) {
    			//set MAR register
    	        cpu.getMAR().setContent(operand);
    	        CPU.cyclePlusOne();
    	        //fetch the data from cache according to MAR
    	        Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
    	        CPU.cyclePlusOne();
    	        //return data
    	        cpu.getIRR().setContent(cpu.getMBR().getContent());
    	        CPU.cyclePlusOne();
    	        return CPU.getInstance().getIRR().getContent();
    		}else if (operand.length() == 16) {
    			return operand;
    		}
    		return "LDX Error";
    }
    
  //******************************************************************************************************************************
    public static void STX(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

        //move data from index register to IRR'
        switch (X){
            case "01":
                cpu.getIRR().setContent(cpu.getX1().getContent());
                break;
            case "10":
                cpu.getIRR().setContent(cpu.getX2().getContent());
                break;
            case "11":
                cpu.getIRR().setContent(cpu.getX3().getContent());
                break;
        }
        stepInformation="Execute:IRR<=Register";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

        //move data from IRR to MBR
        cpu.getMBR().setContent(cpu.getIRR().getContent());
        stepInformation="Execute:MBR<=IRR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

        //move data from MBR to memory
        String address = cpu.getMAR().getContent();
        String data = cpu.getMBR().getContent();
        Cache.getInstance().writeBack(address,data);
        memoryInformation=true;
        stepInformation="Execute:Memory[MAR]<=Cache<=MBR";
        sendStepInformation();
        CPU.cyclePlusOne();
        Halt.halt();

    }

    	//STX with out single step halt
    public static void STXNHLT(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        CPU.cyclePlusOne();

        //move data from index register to IRR'
        switch (X){
            case "01":
                cpu.getIRR().setContent(cpu.getX1().getContent());
                break;
            case "10":
                cpu.getIRR().setContent(cpu.getX2().getContent());
                break;
            case "11":
                cpu.getIRR().setContent(cpu.getX3().getContent());
                break;
        }
        CPU.cyclePlusOne();

        //move data from IRR to MBR
        cpu.getMBR().setContent(cpu.getIRR().getContent());
        CPU.cyclePlusOne();

        //move data from MBR to memory
        Cache.getInstance().writeBack(cpu.getMAR().getContent(),cpu.getMBR().getContent());
        CPU.cyclePlusOne();
    }
    
	//STX for Tomasulo
    public static String STXTom(String operand){
    		if(operand.length() == 3) {
    			 switch (operand){
    	            case "x01":
    	                return CPU.getInstance().getX1().getContent();
    	            case "x10":
    	            		return CPU.getInstance().getX2().getContent();
    	            case "x11":
    	            		return CPU.getInstance().getX3().getContent();
    	        }
    			 CPU.cyclePlusOne();
    		}else if (operand.length() == 16) {
    			return operand;
    		}
    		return "STX Error";
    }


    public static void  sendStepInformation(){
        EngineerConsoleController.setStepInformation(stepInformation,memoryInformation);
    }
}
