package core;

import gui.controllers.EngineerConsoleController;

public class TomasuloDecoder {
	private String instruction;
	private String opClass;
	private String opCode;
	private String operand1;
	private String operand2;
	private String destination;
	//Single instance for class Decoder
	private static final TomasuloDecoder instance = new TomasuloDecoder();
	//private constructor
	private TomasuloDecoder() {

	}
	// Method to get instance
	public static TomasuloDecoder getInstance() {
		return instance;
	}

	//get the instruction
	public void setInstruction(String instruction){
		if(instruction.length() == 16) {
			this.instruction=instruction;
		}else {
			//remain for failure process
			this.instruction = "0000000000000000";
		}

	}

	//decode instruction
	public void decode(){

		String op=instruction.substring(0,6);
		switch (op){

			//Miscellaneous Instructions
			case "000000":
				opCode = "HLT";
				opClass = "HLT";
				break;

			//Load and Store Instructions
			case "000001":
				opCode="LDR";
				operand1 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				operand2 = "";
				destination = instruction.substring(6,8);
				break;
			case "000010":
				opCode="STR";
				operand1 = instruction.substring(6,8);
				operand2 = "";
				destination = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				break;
			case "000011":
				opCode="LDA";
				operand1 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				operand2 = "";
				destination = instruction.substring(6,8);
				break;
			case "100001":
				opCode="LDX";
				operand1 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				operand2 = "";
				destination="x"+instruction.substring(8,10);
				break;
			case "100010":
				opCode = "STX";
				operand1 ="x"+instruction.substring(8,10);
				operand2 = "";
				destination = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				break;

			//Transfer Instructions (TI):
			case "001000":
				opCode = "JZ";
				operand1 = instruction.substring(6,8);
				operand2 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));;
				destination = "";
				break;
			case "001001":
				opCode = "JNE";
				operand1 = instruction.substring(6,8);
				operand2 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));;
				destination = "";
				break;
			case "001010":
				//Reserved!
				opCode = "JCC";
				operand1 = "";
				operand2 = "";
				destination = "";
				break;
			case "001011":
				opCode = "JMA";
				operand1 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));;
				operand2 = "";
				destination = "";
				break;
			case "001100":
				//Reserved!
				opCode = "JSR";
				operand1 = "";
				operand2 = "";
				destination = "11";
				break;
			case "001101":
				//Reserved!
				opCode = "RFS";
				operand1 = "";
				operand2 = "";
				destination = "";
				break;
			case "001110":
				opCode = "SOB";
				operand1 = instruction.substring(6,8);
				operand2 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				destination = instruction.substring(6,8);
				break;
			case "001111":
				opCode = "JGE";
				operand1 = instruction.substring(6,8);
				operand2 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				destination = "";
				break;

			//Arithmetic Instructions
			//with Address field
			case "000100":
				opCode = "AMR";
				operand1 = instruction.substring(6,8);
				operand2 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				destination = instruction.substring(6,8);
				break;
			case "000101":
				opCode = "SMR";
				operand1 = instruction.substring(6,8);
				operand2 = Addressing.getEffectiveAddressTom(instruction.substring(8,10),instruction.substring(10,11),instruction.substring(11,16));
				destination = instruction.substring(6,8);
				break;
			//with Immed field
			case "000110":
				opCode = "AIR";
				operand1 = instruction.substring(6,8);
				operand2 = instruction.substring(11,16);
				destination = instruction.substring(6,8);
				break;
			case "000111":
				opCode = "SIR";
				operand1 = instruction.substring(6,8);
				operand2 = instruction.substring(11,16);
				destination = instruction.substring(6,8);
				break;
			//multiple and divide
			case "010000":
				opCode = "MLT";
				operand1 = instruction.substring(6,8);
				operand2 = instruction.substring(8,10);
				destination = instruction.substring(6,8);
				break;
			case "010001":
				opCode = "DVD";
				operand1 = instruction.substring(6,8);
				operand2 = instruction.substring(8,10);
				destination = instruction.substring(6,8);
				break;

			//Logical Instructions
			case "010010":
				//Reserved!
				opCode = "TRR";
				operand1 = "";
				operand2 = "";
				destination = ""; 
				break;
			case "010011":
				opCode = "AND";
				operand1 = instruction.substring(6,8);
				operand2 = instruction.substring(8,10);
				destination = instruction.substring(6,8);
				break;
			case "010100":
				opCode = "ORR";
				operand1 = instruction.substring(6,8);
				operand2 = instruction.substring(8,10);
				destination = instruction.substring(6,8);
				break;
			case "010101":
				opCode = "NOT";
				operand1 = instruction.substring(6,8);
				operand2 = "";
				destination = instruction.substring(6,8);
				break;

			//Shift and Rotate Instruction
			case "011001":
				//Reserved
				opCode = "SRC";
				operand1 = "";
				operand2 = "";
				destination = "";
				break;
			case "011010":
				//Reserved
				opCode = "RRC";
				operand1 = "";
				operand2 = "";
				destination = "";
				break;

			//IO Instruction
			case "110001":
				opCode = "IN";
				operand1 = instruction.substring(11,16);
				operand2 = "";
				destination = instruction.substring(6,8);
				break;
			case "110010":
				opCode = "OUT";
				operand1 = instruction.substring(6,8);
				operand2 = "";
				destination = instruction.substring(11,16);
				break;

			//trap
			case "011110":
				opCode = "TRAP";
				operand1 = "";
				operand2 = "";
				destination = "";
				break;
		}
		
		storeInRS();
		
	}
	
	//Strore decoded instruction into reservation station
	private void storeInRS() {
		String vi, vj;
		Integer qi, qj;
		
		if(operand1 == "") {
			qi = 0;
			vi = "0";
		}else {
			if(RegisterFile.getRegisterName(operand1) == 0 || opCode == "LDA") {
				qi = 0;
				vi = operand1;
			}else {
				qi = RegisterFile.getRegisterName(operand1);
				vi = "";
			}
		}
		
		if(operand2 == "") {
			qj = 0;
			vj = "0";
		}else {
			if(RegisterFile.getRegisterName(operand2) == 0) {
				qj = 0;
				vj = operand1;
			}else {
				qj = RegisterFile.getRegisterName(operand2);
				vj = "";
			}
		}
		
		ReservationStation.newInstruction("", opCode, qi, vi, qj, vj, destination);
	}
	
	//clear decoder
	public void clear() {
		instruction = "0000000000000000";
		decode();
	}
}