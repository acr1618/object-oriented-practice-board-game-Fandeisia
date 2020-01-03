package pt.ulusofona.lp2.fandeisiaGame;

import java.io.File;
import java.sql.SQLOutput;
import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
public class FandeisiaGameManager{

    public FandeisiaGameManager(){
    }

    List<Treasure> treasures = new ArrayList<>();
    List<Hole> holes = new ArrayList<>();
    List<Creature> creatures = new ArrayList<>();
    Team teamLdr = new Team (10, "LDR");
    Team teamRes = new Team (20, "RESISTENCIA");
    Team currentTeam = new Team(0,"0");
    List<Element> elements = new ArrayList<>();
    int rowsFgm = 0;
    int columnsFgm = 0;
    int turnsWithoutTreasure = 0;
    int sumTreasuresLeft = 0; //Soma tesouros restantes
    int sumTreasuresTotal =0;
    long logCounter = 0;
    int turnCounter = 0;
    boolean iAactive = false; // Altera para true se quiser IA

    void setRowsFgm(int rows){
        this.rowsFgm = rows;
     }
    void setColumnsFgm(int columns){
        this.columnsFgm = columns;
     }


    // Dado binário (0 ou 1)
    int rollDiceBinary(){ return ThreadLocalRandom.current().nextInt(1 );
    } // Usado para sortear time que começa o jogo

    // Contador para as linhas impressas no console vindas das funções. Apenas para utilizar no desenvolvimento.
    long iterate(long i){
        if (i < 2147483647){
        logCounter++;
        return logCounter;
        }else {
            logCounter = -2000000000;
        return logCounter;
        }
    }

    public void toggleAI(boolean active){
        iAactive = !active;
    }

    public String[][] getCreatureTypes(){
        System.out.println( iterate(logCounter) + " - "+ "IN getCreatureTypes\n -----------------------------------\n");
        return new String[][]{
                {"Anão", "Anao.png", "Descrição do Anão", String.valueOf(1)},
                {"Dragão", "Dragao.png", "Descrição do Dragão", String.valueOf(9)},
                {"Elfo", "Elfo.png", "Descrição do Elfo", String.valueOf(5)},
                {"Gigante", "Gigante.png", "Descrição do Gigante", String.valueOf(5)},
                {"Humano", "Humano.png", "Descrição do Humano", String.valueOf(3)},
        };
    }

    public Map<String, Integer> createComputerArmy(){
        System.out.println( iterate(logCounter) + " - "+"IN createComputerArmy\n -----------------------------------\n");

        int spent =0;
        Map<String, Integer> computerArmy = new HashMap<>();

        /*do {
            computerArmy.put("Anão", new Random().nextInt(4));
            spent = spent + computerArmy.get("Anão");// criar um random entre 0 e 3.
            computerArmy.put("Dragão", new Random().nextInt(4));
            spent = spent + computerArmy.get("Dragão")*9;
            computerArmy.put("Elfo", new Random().nextInt(4));
            spent = spent + computerArmy.get("Elfo")*5;
            computerArmy.put("Gigante", new Random().nextInt(4));
            spent = spent + computerArmy.get("Gigante")*5;
            computerArmy.put("Humano", new Random().nextInt(4));
            spent = spent + computerArmy.get("Humano") *3;

        } while (spent >50 || computerArmy.isEmpty());*/
        computerArmy.put("Dragão", 1);
        return computerArmy;
    }

    public int startGame(String[] content, int rows, int columns){
        System.out.println( iterate(logCounter) + " - "+"IN startGame\n -----------------------------------\n");

        teamLdr = new Team (10,"LDR");
        teamRes = new Team (20, "RESISTENCIA");
        currentTeam = new Team(0,"0");
        setRowsFgm(rows);
        setColumnsFgm(columns);
        List<Element> elements = new ArrayList<>();
        treasures = new ArrayList<>();
        holes = new ArrayList<>();
        creatures = new ArrayList<>();
        turnsWithoutTreasure = 0;
        sumTreasuresLeft = 0;
        sumTreasuresTotal =0;
        turnCounter = 0;
        iAactive = false;

        for(String line: content){
            String[] individual_line = line.split(", ");
            if (individual_line.length >= 6 ) { // É criatura
                String[] detach_colon = individual_line[0].split(": ");
                int id = Integer.parseInt(detach_colon[1]);
                String[] aux_type = individual_line[1].split(": ");
                String type = aux_type[1];
                String[] aux_teamId = individual_line[2].split(": ");
                String aux_teamID = aux_teamId[1];
                int teamId = Integer.parseInt(aux_teamID);
                String[] aux_X = individual_line[3].split(": ");
                String x_string = aux_X[1];
                int x = Integer.parseInt(x_string);
                String[] aux_Y = individual_line[4].split(": ");
                String y_string = aux_Y[1];
                int y = Integer.parseInt(y_string);
                String[] aux_orientation = individual_line[5].split(": ");
                String orientation = aux_orientation[1];
                switch (type) {
                    case ("Anão") : creatures.add(new Dwarf(id, x, y, teamId, 1, orientation));
                        break;
                    case("Dragão") : creatures.add(new Dragon(id, x, y, teamId, 9, orientation));
                        break;
                    case("Elfo") : creatures.add(new Elf(id, x, y, teamId, 5, orientation));
                        break;
                    case("Gigante") : creatures.add(new Giant(id, x, y, teamId, 5, orientation));
                        break;
                    case("Humano") : creatures.add(new Human(id, x, y, teamId, 3, orientation));
                        break;
                }
            }
            if (individual_line[1].contains("hole") || individual_line[1].contains("gold") || individual_line[1].contains("silver") || individual_line[1].contains("bronze")){
                String[] detach_colon = individual_line[0].split(": ");
                int id = Integer.parseInt(detach_colon[1]);
                String[] aux_type = individual_line[1].split(": ");
                String type = aux_type[1];
                String[] aux_X = individual_line[2].split(": ");
                String x_string = aux_X[1];
                int x = Integer.parseInt(x_string);
                String[] aux_Y = individual_line[3].split(": ");
                String y_string = aux_Y[1];
                int y = Integer.parseInt(y_string);
                if (type.equals("hole")){
                    holes.add(new Hole (id, x, y));
                } else {
                    if(type.equals("gold")){
                        treasures.add(new Treasure (id, x, y, 3));
                        sumTreasuresTotal += 3;
                    }
                    if(type.equals("silver")){
                        treasures.add(new Treasure (id, x, y, 2));
                        sumTreasuresTotal += 2;
                    }
                    if(type.equals("bronze")){
                        treasures.add(new Treasure (id, x, y, 1));
                        sumTreasuresTotal += 1;
                    }
                }
            }
            sumTreasuresLeft = sumTreasuresTotal;
        }
        /*imagens*/
        // Set initial orientation and team image
        // Gera imagens diferentes para criaturas da Resistencia.  -- 1 coelho ou 2? - Change filter to a better later. mmaybe sobel filter
        for (Creature creature: creatures){
            if (creature.getTeamId() == 20){
                creature.setOutroTypeName(creature.getOutroTypeName()+"Negate");
                creature.setImage(creature.getOutroTypeName()+"-"+creature.getOrientation()+".png");
            } else {
                creature.setImage(creature.getOutroTypeName()+"-"+creature.getOrientation()+".png");
            }
        }
       /* Prints dos elementos fatiados e separados. */
        //System.out.println(iterate(logCounter) + " - "+"  LISTA DE CRIATURAS IN START GAME: "+ creatures + "\n");
        //System.out.println(iterate(logCounter) + " - "+"  LISTA DE BURACOS IN START GAME: " + holes + "\n");
        //System.out.println(iterate(logCounter) + " - "+"  LISTA DE TESOUROS IN START GAME: " + treasures + "\n");
        //System.out.println(iterate(logCounter) + " - "+"  ID DE RESISTENCIA IN START GAME: " + teamRes.getId() + "\n");
        //System.out.println(iterate(logCounter) + " - "+"  ID DE LDR IN START GAME:" + teamLdr.getId() + "\n");
        //System.out.println(iterate(logCounter) + " - "+"  HASH DE COMPUTER ARMY IN START GAME: " + createComputerArmy() + "\n\n");

        /* Subtrai moedas para cada criatura de cada time*/
        for (Creature c: creatures){
            switch (c.getTeamId()) {
                case 10: teamLdr.removeCoins(c.getCost());
                    break;
                case 20: teamRes.removeCoins(c.getCost());
                    break;
            }
        }
        if (teamLdr.getCoins() < 0 && teamRes.getCoins() < 0){
            return 1;
        } else if(teamLdr.getCoins() < 0){
            return 2;
        } else if(teamRes.getCoins() < 0){
            return 3;
        }
        return 0;

    }

    public int getCurrentScore(int teamId){
        System.out.println(iterate(logCounter) + " - "+"IN getCurrentScore\n -----------------------------------\n");
        System.out.println(iterate(logCounter) + " - "+"  teamLdr.getId() in CUURRENTSCORE: " +teamLdr.getId());
        System.out.println(iterate(logCounter) + " - "+"  teamRes.getId() in CUURRENTSCORE: " +teamRes.getId() + "\n");

        if (teamId == teamLdr.getId()){
            return teamLdr.getPoints();
        }else {
            return teamRes.getPoints();
        }
    } // Está sendo chamada duas vezes seguidas no Visualizador. Depois é chamadas mais 2 vezes.

    public int getCoinTotal(int teamId){
        System.out.println(iterate(logCounter) + " - "+"IN getCoinTotal\n -----------------------------------\n");

        if (teamId == 10){
            //System.out.println(iterate(logCounter) + " - "+"teamLdr.getCoins(): "  + teamLdr.getCoins() + "\n");
            return teamLdr.getCoins();
        } else{
            //System.out.println(iterate(logCounter) + " - "+"  teamRes.getCoins() " + teamRes.getCoins() + "\n");
            return teamRes.getCoins();
        }
    } //OK 29-12 Também se repete depois de escolher exército

    public void setInitialTeam(int teamId){
        System.out.println(iterate(logCounter) + " - "+"Entrou em setInitialTeam\n -----------------------------------\n\n");

        /*System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n############################## BEM VINDO AO FANDEISIA GAME ##############################\n\n");
        System.out.println("ESCOLHA QUAL TIME IRÁ COMEÇAR JOGANDO.");
        System.out.print("DIGITE 1 PARA LDR, 2 PARA RESISTENCIA:");
        Scanner keyboard = new Scanner (System.in);
        int choice = keyboard.nextInt();
        if (choice == 1 ){
            currentTeam = teamLdr;
        } else {
            currentTeam = teamRes;
        }*/
        // Seleção aleatória com dado
         /*int resultDice = rollDiceBinary();
         if (resultDice == 0){
             currentTeam = teamLdr;
         }else {
             currentTeam = teamRes;
         }*/

        if (teamId == teamLdr.getId()){
            currentTeam = teamLdr;
        } else {
            currentTeam = teamRes;
        }
    } //-- TODO o que tem que fazer aqui?

    public int getCurrentTeamId(){
        System.out.println(iterate(logCounter) + " - "+"IN currentTeamId\n -----------------------------------\n");
        System.out.println(iterate(logCounter) + " - "+"  currentTeam.getId(): "+ currentTeam.getId() + "\n");
        return currentTeam.getId();
    }

    public List<Creature> getCreatures(){
        System.out.println(iterate(logCounter) + " - "+"IN getCreatures");
        System.out.println(iterate(logCounter) + " - "+"    Lista de criaturas do mundo: "+ creatures);
        System.out.println(iterate(logCounter) + " - "+"Depois entra em getElementId e percorre tabuleiro. Parece ok. ");
        return creatures;
    }

    public int getElementId(int x, int y){
        //System.out.print(iterate(logCounter) + " - "+"IN getElementId("+x+","+y+")"+ " rowsFgm: "+rowsFgm +" columnsFgm: "+columnsFgm);
        //System.out.println("Dimensões do mundo (rows e columns): " + rowsFgm + " e " + columnsFgm);

        elements = new ArrayList<>();
        elements.addAll(creatures);
        elements.addAll(treasures);
        elements.addAll(holes);

        for(Element e: elements){
            if(e.getX() == x && e.getY() == y){
                return e.getId();
            }
        }
        //System.out.println("Elements in getElementid: " + elements);
        return 0;
    }

    public String[][] getSpellTypes(){
        System.out.println(iterate(logCounter) + " - "+"IN getSpellTypes");
        return new String[][]{
                {"EmpurraParaNorte", "Descrição do EmpurraParaNorte", String.valueOf(1)},
                {"EmpurraParaEste", "Descrição do EmpurraParaEste", String.valueOf(1)},
                {"EmpurraParaSul", "Descrição do EmpurraParaSul", String.valueOf(1)},
                {"EmpurraParaOeste", "Descrição do EmpurraParaOeste", String.valueOf(1)},
                {"ReduzAlcance", "Descrição do ReduzAlcance", String.valueOf(2)},
                {"DuplicaAlcance", "Descrição do DuplicaAlcance", String.valueOf(3)},
                {"Congela", "Descrição do Congela", String.valueOf(3)},
                {"Congela4Ever", "Descrição do Congela4Ever", String.valueOf(10)},
                {"Descongela", "Descrição do Descongela", String.valueOf(8)},
        };
    }

    public String getSpell (int x, int y){
        System.out.println(iterate(logCounter) + " - "+"IN getSpell");
        for(Creature creature: creatures){
            if(creature.getX() == x && creature.getY() == y){
                String spellName = creature.getItSpellName();
                return spellName;
            }
        }
        return null;
    }

    public boolean enchant (int x, int y, String spellName) {
        System.out.println(iterate(logCounter) + " - " + "Entrou em enchant");
        for (Creature c : creatures) {
            if (c.getX() == x && c.getY() == y) {
                assert spellName != null;
                if (c.isFrozen4Ever() && spellName.equals("Descongela")) {
                    if (checkBalanceToSpell(getCurrentTeamId(), 8)) {
                        c.setEnchant(true);
                        c.setItSpellName(spellName);
                        taxSpell(getCurrentTeamId(), 8);
                        return true;
                    } else {
                        return false;
                    }
                }
                if (!c.isFrozen4Ever() && spellName.equals("Congela")) {
                    if (checkBalanceToSpell(getCurrentTeamId(), 3)) {
                        c.setEnchant(true);
                        taxSpell(getCurrentTeamId(), 3);
                        c.setItSpellName(spellName);
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    switch (spellName) {
                        case ("Congela4Ever"): {
                            if (checkBalanceToSpell(getCurrentTeamId(), 10) && !c.isFrozen()) {
                                c.setEnchant(true);
                                //c.Congela4Ever();
                                //c.setItSpellName("Congela4Ever");
                                taxSpell(getCurrentTeamId(), 10);
                                c.setItSpellName(spellName);
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("EmpurraParaNorte"): {
                            c.setNextX(x);
                            c.setNextY(y - 1);
                            if (validateMovement(c) && !c.isFrozen() && !c.isFrozen4Ever()) { // movimento é valido
                                if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                                    c.setEnchant(true);
                                    //c.Congela();
                                    //c.setItSpellName("EmpurraParaNorte");
                                    taxSpell(getCurrentTeamId(), 1);
                                    c.setItSpellName(spellName);
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false; // Se não entrar nesse if tem erro!
                            }
                        }
                        case ("EmpurraParaEste"): {
                            c.setNextX(x + 1);
                            c.setNextY(y);
                            if (validateMovement(c) && !c.isFrozen() && !c.isFrozen4Ever()) { // movimento é valido
                                if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                                    c.setEnchant(true);
                                    //c.setItSpellName("EmpurraParaEste");
                                    //c.EmpurraParaEste();
                                    taxSpell(getCurrentTeamId(), 1);
                                    c.setItSpellName(spellName);
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false; // Se não entrar nesse if tem erro!
                            }
                        }
                        case ("EmpurraParaSul"): {
                            c.setNextX(x);
                            c.setNextY(y + 1);
                            if (validateMovement(c) && !c.isFrozen() && !c.isFrozen4Ever()) { // movimento é valido
                                if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                                    c.setEnchant(true);
                                    //c.Congela();
                                    //c.setItSpellName("EmpurraParaSul");
                                    taxSpell(getCurrentTeamId(), 1);
                                    c.setItSpellName(spellName);
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                        case ("EmpurraParaOeste"): {
                            c.setNextX(x - 1);
                            c.setNextY(y);
                            if (validateMovement(c)) { // movimento é valido
                                if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                                    c.setEnchant(true);
                                    taxSpell(getCurrentTeamId(), 1);
                                    c.setItSpellName(spellName);
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                        case ("ReduzAlcance"): {
                            if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                                c.setEnchant(true);
                                taxSpell(getCurrentTeamId(), 2);
                                c.setItSpellName(spellName);
                                //c.ReduzAlcance();
                                //c.setItSpellName("ReduzAlcance");
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("DuplicaAlcance"): {
                            if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                                c.setEnchant(true);
                                taxSpell(getCurrentTeamId(), 3);
                                c.setItSpellName(spellName);
                                //c.DuplicaAlcance(c.getRange());
                                //c.setItSpellName("DuplicaAlcance");
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void giveCoins(){
        if (!teamLdr.getTreasuresFoundInThisTurn()){
            teamLdr.addCoins(1);
        } else {
            teamLdr.addCoins(2);
        }
        if (!teamRes.getTreasuresFoundInThisTurn()){
            teamRes.addCoins(1);
        } else {
            teamRes.addCoins(2);
        }
    }

    public void processTurn(){ // still todo
        turnCounter ++;
        teamLdr.setTreasuresFoundInThisTurn(false);// Zera em todos turnos e incrementa quando acha tesouro
        teamRes.setTreasuresFoundInThisTurn(false);
        int teste = treasures.size();
        for (Creature creature: creatures){
            // Timer para descongelar
            if (creature.isFrozen()){
                creature.setFrozenTime(1);
            }
            // Se atingir timer descongela e troca figura
            if (creature.isFrozen() && creature.getFrozenTime() == 1){
                creature.setFrozen(false);
                creature.setImage(creature.getOutroTypeName()+"-"+ creature.getOrientation()+".png");
            }
            // Se tem feitiço pra aplicar, aplica-se.
            if (creature.isEnchant()){
                creature.setEnchant(false);
                executeSpell(creature, creature.getItSpellName());
                if(matchTreasure(creature.getX(), creature.getY(), creature.getId(), creature.getTeamId())){
                    // turnsWithoutTreasure = 0;
                    if (creature.getTeamId() ==10){
                        teamLdr.setTreasuresFoundInThisTurn(true);
                    } else {
                        teamRes.setTreasuresFoundInThisTurn(true);
                    }
                }

            }
            /* Movimento normal das criaturas */
            // Se não tá congelada ou congelada4Ever não se movimenta. Se não, bota pra movimentar.
            if (!creature.isFrozen4Ever() && !creature.isFrozen()){
                if (executeStandardMovement(creature)){
                    creature.move();
                    if(matchTreasure(creature.getX(), creature.getY(), creature.getId(), creature.getTeamId())){
                        // turnsWithoutTreasure =0;
                        if (creature.getTeamId() ==10){
                                teamLdr.setTreasuresFoundInThisTurn(true);
                        }
                        if (creature.getTeamId() ==20){
                                teamRes.setTreasuresFoundInThisTurn(true);
                        }
                    }
                } else {
                    creature.spin();
                }
            }
        }
        if(teste == treasures.size()){
            turnsWithoutTreasure ++;
        } else {
            turnsWithoutTreasure = 0;
        }
        //turnsWithoutTreasure ++; // zera toda vez que encontra um tesouro
        switchCurrentTeam();
        giveCoins();
         // 1 se não achou tesouro neste turno e 2 se achou.
    }

    private void executeSpell(Creature creature,String spell) {

        switch(spell){
            case ("Descongela"): {
                creature.descongela();
                creature.setImage(creature.getOutroTypeName()+"-"+ creature.getOrientation()+".png");
                creature.setItSpellName(null);
                break;
            }
            case ("Congela"): {
                creature.congela();
                creature.setImage(creature.getOutroTypeName() + "-Frozen.png");
                creature.setItSpellName(null);
                break;
            }
            case ("Congela4Ever") : {
                creature.congela4Ever();
                creature.setImage(creature.getOutroTypeName() + "-Frozen4Ever.png");
                creature.setItSpellName(null);
                break;
            }
            case ("EmpurraParaNorte"): {
                if (!creature.isFrozen() && !creature.isFrozen4Ever()){
                    if (validateMovement(creature)) {
                        creature.empurraParaNorte();
                        creature.setItSpellName(null);
                        creature.setOrientation("Norte");
                        creature.setImage(creature.getOutroTypeName()+"-Norte.png");
                    }
                }
                break;
            }
            case ("EmpurraParaEste"): {
                if (!creature.isFrozen() && !creature.isFrozen4Ever()){
                    if (validateMovement(creature)) {
                        creature.empurraParaEste();
                        creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                        creature.setOrientation("East");
                        creature.setImage(creature.getOutroTypeName()+"-Este.png");
                    }
                }
                break;
            }
            case ("EmpurraParaSul"): {
                if (!creature.isFrozen() && !creature.isFrozen4Ever()){
                    if (validateMovement(creature)) {
                        creature.empurraParaSul();
                        creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                        creature.setOrientation("Sul");
                        creature.setImage(creature.getOutroTypeName()+"-Sul.png");
                    }
                }
                break;
            }
            case ("EmpurraParaOeste"): {
                if (!creature.isFrozen() && !creature.isFrozen4Ever()){
                    if (validateMovement(creature)) {
                        creature.empurraParaOeste();
                        creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                        creature.setOrientation("Oeste");
                        creature.setImage(creature.getOutroTypeName()+"-Oeste.png");
                    }
                }
                break;
            }
            case ("ReduzAlcance"): {
                creature.reduzAlcance();
                creature.setItSpellName(null);
                break;
            }
            case ("DuplicaAlcance"): {
                creature.duplicaAlcance();
                creature.setItSpellName(null);
                break;
            }
        }
    }

    private boolean executeStandardMovement(Creature creature) {
        System.out.println("Entrou em executeStandardMovement");
            switch (creature.getTypeName()){
                case ("Anão"): {
                    switch (creature.getOrientation()){
                        case ("Norte"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() - creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Sul"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() + creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Este"):{
                            creature.setNextX(creature.getX() + creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Oeste"):{
                            creature.setNextX(creature.getX() - creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }

                case ("Elfo"): {
                    switch (creature.getOrientation()){
                        case ("Norte"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() - creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Sul"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() + creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Este"):{
                            creature.setNextX(creature.getX() + creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Oeste"):{
                            creature.setNextX(creature.getX() - creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                case ("Dragão"): {
                    switch (creature.getOrientation()){
                        case ("Norte"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() - creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Sul"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() + creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Este"):{
                            creature.setNextX(creature.getX() + creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Oeste"):{
                            creature.setNextX(creature.getX() - creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                case ("Humano"): {
                    switch (creature.getOrientation()){
                        case ("Norte"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() - creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Sul"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() + creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Este"):{
                            creature.setNextX(creature.getX() + creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Oeste"):{
                            creature.setNextX(creature.getX() - creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                case ("Gigante"): {
                    switch (creature.getOrientation()){
                        case ("Norte"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() - creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Sul"):{
                            creature.setNextX(creature.getX());
                            creature.setNextY(creature.getY() + creature.getRange());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Este"):{
                            creature.setNextX(creature.getX() + creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                        case ("Oeste"):{
                            creature.setNextX(creature.getX() - creature.getRange());
                            creature.setNextY(creature.getY());
                            if (validateMovement(creature)){
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                // fazer ifs para verificar o time e trocar imagem, devido à restrição nominal dos tipos das craituras! TODO
                //case ("Humano"): TODO
                //case ("Elfo"): TODO
                //case ("Gigante"): TODO
                //case ("Dragão"): TODO
            }


        return false;
    }

    private boolean matchTreasure(int x, int y, int id, int teamId) {
        System.out.println("Entrou em matchTreasure");
        for (Creature creature: creatures){
            if (creature.getId() == id){
                //for (Treasure treasure: treasures){
                for (Iterator<Treasure> i = treasures.iterator(); i.hasNext();){ // Artifício muito louco para passar do ERRO ConcurrentModificationException
                    Treasure treasure = i.next();
                    if (x == treasure.getX() && y == treasure.getY()){ // MATCH
                        creature.addPoints(treasure.getValue()); // Add pontos criatura
                        sumTreasuresLeft -= treasure.getValue();
                        if (treasure.getValue() ==3){
                            creature.addGold();
                        }
                        if (treasure.getValue() ==2){
                            creature.addSilver();
                        }
                        if (treasure.getValue() ==1){
                            creature.addBronze();
                        }
                        if (teamId == 10){
                            teamLdr.addPoints(treasure.getValue()); // Add pontos time
                        } else {
                            teamRes.addPoints(treasure.getValue()); // Add pontos time
                        }
                        i.remove();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean validateMovement(Creature creature) {

        if (creature.getNextX() < 0 || creature.getNextY() < 0){ // fora da tela
            return false;
        }
        if (creature.getNextX() > columnsFgm-1 || creature.getNextY() > rowsFgm-1){ // fora da tela
            return false;
        }

        if (getElementId(creature.getNextX(), creature.getNextX()) <=-500){ // buraco
            return false;
        }

        /*outra criatura */
        return getElementId(creature.getNextX(), creature.getNextY()) <= 0;
    }

    // Checar saldo;
    private boolean checkBalanceToSpell(int teamId, int spellCost) {

        if (teamId == 10){
            return teamLdr.checkBalanceToSpell(spellCost);
        }
        if (teamId == 20){
            return teamRes.checkBalanceToSpell(spellCost);
        }
        System.out.println(iterate(logCounter) + " - "+"O teamId passado não é válido. Impossível consultar saldo ");
        return false;

    }

    // Debita moedas dos feitiços
    private void taxSpell(int teamId, int spellCost) {
        if (teamId == 10){
            teamLdr.removeCoins(spellCost);
            System.out.println(iterate(logCounter) + " - "+"Spell taxed from LORD ELDER");

        }
        if (teamId == 20){
            teamRes.removeCoins(spellCost);
            System.out.println(iterate(logCounter) + " - "+"Spell taxed from RESISTENCIA:");
        }
    }

    private void switchCurrentTeam() {
        if (currentTeam.equals(teamLdr)){
            currentTeam = teamRes;
        } else {
            currentTeam = teamLdr;
        }
    }

    public boolean gameIsOver(){
        System.out.println("Entrou em gameIsOver");

        if (treasures.size() == 0 || turnsWithoutTreasure == 15){
            return true;
        }

        if (teamLdr.getPoints() > teamRes.getPoints() + sumTreasuresLeft || teamRes.getPoints() > teamLdr.getPoints() + sumTreasuresLeft){ //sumTreasuresLeft é zerada em startGame e incrementada em matchTreasure
            return true;
        }
        return false;
    }

    public List<String> getResults(){
        List<String> results = new ArrayList<>();

        if (teamLdr.getPoints() > teamRes.getPoints()){
            results.add("Welcome to FANDEISIA");
            results.add("Resultado: Vitória da equipa " + teamLdr.getName());
            results.add("LDR: "+ teamLdr.getPoints());
            results.add("RESISTENCIA: "+ teamRes.getPoints());
            results.add("Nr. de Turnos jogados: "+ turnCounter);
            results.add("-----");
            for (Creature c: creatures){
                results.add(c.getId() +" : " + c.getTypeName() + " : " + c.getGold() + " : " + c.getSilver() + " : " + c.getBronze() + " : " + c.getPoints());
            }
        } else {
            if (teamRes.getPoints() == teamLdr.getPoints()){
                results.add("Welcome to FANDEISIA");
                results.add("Resultado: EMPATE");
                results.add("LDR: "+ teamLdr.getPoints());
                results.add("RESISTENCIA: "+ teamRes.getPoints());
                results.add("Nr. de Turnos jogados: "+ turnCounter);
                results.add("-----");
                for (Creature c: creatures){
                    results.add(c.getId() +" : " + c.getTypeName() + " : " + c.getGold() + " : " + c.getSilver() + " : " + c.getBronze() + " : " + c.getPoints());
                }
            }else {
                results.add("Welcome to FANDEISIA");
                results.add("Resultado: Vitória da equipa " + teamRes.getName());
                results.add("RESISTENCIA: "+ teamRes.getPoints());
                results.add("LDR: "+ teamLdr.getPoints());
                results.add("Nr. de Turnos jogados: "+ turnCounter);
                results.add("-----");
                for (Creature c: creatures){
                    results.add(c.getId() +" : " + c.getTypeName() + " : " + c.getGold() + " : " + c.getSilver() + " : " + c.getBronze() + " : " + c.getPoints());
                }
            }
        }

        return results;

    }

    public boolean saveGame (File fich){
        System.out.println("Estou em saveGame");
        return true;
    }
    public boolean loadGame (File fich){
        System.out.println("Estou em loadGame");

        return true;
    }

    public String whoIsLordEder(){
        System.out.println("Estou em whoIsLordEder");

        return "Éderzito António Macedo Lopes";
    } //OK 29-12
    public List<String> getAuthors(){
        return Collections.singletonList("Allyson Rodrigues");
    }
}
