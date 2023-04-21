package it.unibo.truck;

import it.unibo.util.ConnTcp;
import unibo.comm22.utils.CommUtils;
import utils.DomainSystemConfig;

import java.util.Scanner;

public class TruckTUI {




    public static void main(String[] args) {

        int MAXPB = 20;

        int MAXGB = 30;

        int HOME_X = 0 ;
        int HOME_Y = 0;
        int INDOOR_X = 1 ;
        int INDOOR_Y = 4;
        int GLASS_X = 5 ;
        int GLASS_Y = 0;
        int PLASTIC_X = 6;
        int PLASTIC_Y = 3;



        Scanner scanner = new Scanner(System.in);

        // CONFIG

        System.out.println("default");

        System.out.printf("PLASTIC  PESO  %d POSIZIONE (%d,%d) \n", MAXPB,PLASTIC_X,PLASTIC_Y);
        System.out.printf("GLASS  PESO  %d POSIZIONE (%d,%d) \n", MAXGB,GLASS_X,GLASS_Y);
        System.out.printf("INDOOR  POSIZIONE (%d,%d) \n",INDOOR_X,INDOOR_Y);

        System.out.println("vuoi usare le impostazioni di default ? (si no)");


        String answer = scanner.next();

        if (answer.equalsIgnoreCase("no")){

            System.out.println("Definire il peso del container GLASS (default :"+MAXGB+")");

            MAXGB = scanner.nextInt();

            System.out.println("Definire il peso del container PLASTIC (default :"+MAXPB+")");

            MAXPB = scanner.nextInt();

            System.out.println("La mappa solitamente ha dimensione 7(colonne)x5(righe) quindi valori possibili 0-6(colonne e 0-4(righe))");

            System.out.println("POSIZIONE INDOOR default "+ INDOOR_X+"," + INDOOR_Y+ " formato N_N");

            answer = scanner.next("\\d+_\\d+");

            INDOOR_X =  Integer.parseInt(answer.split("_")[0]);
            INDOOR_Y =  Integer.parseInt(answer.split("_")[1]);

            System.out.println("POSIZIONE GLASS default "+ GLASS_X+"," + GLASS_Y+ " formato N_N");

            answer = scanner.next("\\d+_\\d+");

            GLASS_X =  Integer.parseInt(answer.split("_")[0]);
            GLASS_Y =  Integer.parseInt(answer.split("_")[1]);



            System.out.println("POSIZIONE PLASTIC default "+ PLASTIC_X+"," + PLASTIC_Y+ " formato N_N");

            answer = scanner.next("\\d+_\\d+");

            PLASTIC_X =  Integer.parseInt(answer.split("_")[0]);
            PLASTIC_Y =  Integer.parseInt(answer.split("_")[1]);

            System.out.println("recap");

            System.out.printf("PLASTIC  PESO  %d POSIZIONE (%d,%d) \n", MAXPB,PLASTIC_X,PLASTIC_Y);
            System.out.printf("GLASS  PESO  %d POSIZIONE (%d,%d) \n", MAXGB,GLASS_X,GLASS_Y);
            System.out.printf("INDOOR  POSIZIONE (%d,%d) \n",INDOOR_X,INDOOR_Y);
        }





        //waitForApplStarted();
        String payloadWeight = String.format("values(%d,%d)",MAXGB,MAXPB);
        String initContainersStr = CommUtils.buildDispatch("configurer","init_capacity",payloadWeight,"waste_service").toString();
        //Dispatch all_position : coordinates(HOMEX,HOMEY,INDOORX,INDOORY,PLASTICX,PLASTICY,GLASSX,GLASSY)

        String payloadPosition = String.format("coordinates(%d,%d,%d,%d,%d,%d,%d,%d)",HOME_X,HOME_Y,INDOOR_X,INDOOR_Y,PLASTIC_X,PLASTIC_Y,GLASS_X,GLASS_Y);

        String initPositionsStr = CommUtils.buildDispatch("configurer","all_position", payloadPosition,"transporttrolley").toString();

        ConnTcp

                connTcp;
        try {
            connTcp = new ConnTcp(DomainSystemConfig.INSTANCE.getWasteServiceAddress(), DomainSystemConfig.INSTANCE.getWasteServicePort());

            connTcp.forward(initContainersStr);
            connTcp.forward(initPositionsStr);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // EXECUTION

        String[] options = {"1- single request",
                "2- double request",
                "3- reset",
                "4- Exit",
        };


        int choice;

        String wasteRequestStr;
        String reply;
        while (true){

            printMenu(options);
            choice = scanner.nextInt();

            switch (choice){
                case 1 : // single request

                    System.out.println("TYPE: Plastic or Glass ; formato TYPE_PESO");
                    answer = scanner.next("(Plastic|Glass)_\\d+");

                   wasteRequestStr= buildWasteRequest(answer.split("_")[0],Integer.parseInt(answer.split("_")[1]));

                    try {
                        reply = connTcp.request(wasteRequestStr);
                        System.out.println(reply);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2 : // double request
                    System.out.println("TYPE: Plastic or Glass ; formato TYPE_PESO");
                    String answer_1 = scanner.next("(Plastic|Glass)_\\d+");
                    String wasteRequestStr_1= buildWasteRequest(answer_1.split("_")[0],Integer.parseInt(answer_1.split("_")[1]));
                    System.out.println("TYPE: Plastic or Glass ; formato TYPE_PESO");
                    String answer_2 = scanner.next("(Plastic|Glass)_\\d+");
                    String wasteRequestStr_2= buildWasteRequest(answer_2.split("_")[0],Integer.parseInt(answer_2.split("_")[1]));

                    try {
                     String   reply_1 = connTcp.request(wasteRequestStr_1);
                        System.out.println("esito prima richiesta :" + reply_1);
                        String   reply_2 = connTcp.request(wasteRequestStr_2);
                        System.out.println("esito seconda richiesta :" + reply_2);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3 : // reset weights


                    String resetDispatch = CommUtils.buildDispatch("trucktui","reset", "info(reset)", "waste_service").toString();
                    connTcp.forward(resetDispatch);
                    break;
                case 4 : // end

                    System.out.println("=========EXIT=============");
                    System.exit(0);
                    break;

                default:
                    System.err.println("scelta non disponibile");
            }
        }
    }

    protected static String buildWasteRequest(String type, int weight){
       String payloadWaste = String.format("details(%s,%d)",type,weight);
        return CommUtils.buildRequest("trucktui","waste",payloadWaste,"waste_service").toString();
    }

    protected static void printMenu(String[] options){

        for (String option: options)
            System.out.println(option);

        System.out.println("Scegli tramite il numero");


    }
    /*
    protected  static void waitForApplStarted(){
        ActorBasic wasteservice = QakContext.Companion.getActor("waste_service");
        while( wasteservice == null ){
            ColorsOut.outappl("TestCoreRequisiti waits for appl ... " , ColorsOut.GREEN);
            CommUtils.delay(200);
            wasteservice = QakContext.Companion.getActor("waste_service");
        }
    }*/
}
