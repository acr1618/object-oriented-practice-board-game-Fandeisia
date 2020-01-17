package pt.ulusofona.lp2.fandeisiaGame;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/* TODO - BUG central: Quando uma criatura recebe um spell de movimento sendo que outra criatura também recebe spell de movimento pra mesma próxima posição dá erro.
    Acontece que a criatura que já tinha validado não executa o spell e mantem condição de isEnchant. Isso porque a validação é feita denovo no executeSpell.
    Às vezes a criatura fica parada, às vezes fica com condição isENchant e se movimenta, às vezes não dá erro.
    * */

public class FandeisiaGameManager implements Serializable{

    public FandeisiaGameManager(){
    }

    private List<Treasure> treasures = new ArrayList<>();
    private List<Hole> holes = new ArrayList<>();
    private List<Creature> creatures = new ArrayList<>();
    private Team teamLdr = new Team (10, "LDR");
    private Team teamRes = new Team (20, "RESISTENCIA");
    private Team currentTeam; // referencia ao time corrente
    private int rowsFgm = 0; // linhas do mundo
    private int columnsFgm = 0; // colunas do mundo
    private int turnsWithoutTreasure = 0;
    private int sumTreasuresLeft = 0;
    private long logCounter = 0;
    private int turnCounter = 0;
    private boolean iAactive = false; // Altera para true se quiser IA
    private void setRowsFgm(int rows){
        this.rowsFgm = rows;
     }
    private void setColumnsFgm(int columns){
        this.columnsFgm = columns;
     }
    // Contador para as linhas impressas no console vindas das funções. Apenas para utilizar no desenvolvimento.
    private long iterate(long i){
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
        //System.out.println( iterate(logCounter) + " - "+ "IN getCreatureTypes\n -----------------------------------\n");
        return new String[][]{
                {"Humano", "Humano.png", "Descrição do Humano", String.valueOf(3)},
                {"Elfo", "Elfo.png", "Descrição do Elfo", String.valueOf(5)},
                {"Anão", "Anao.png", "Descrição do Anão", String.valueOf(1)},
                {"Dragão", "Dragao.png", "Descrição do Dragão", String.valueOf(9)},
                {"Gigante", "Gigante.png", "Descrição do Gigante", String.valueOf(5)},
        };
    }
    public Map<String, Integer> createComputerArmy(){
        //System.out.println(iterate(logCounter) + " - "+"IN createComputerArmy\n --------------------\n");
        int spent  =0;
        Map<String, Integer> computerArmy = new HashMap<>();
        do {
            //computerArmy.put("Anão", new Random().nextInt(3));
            //spent = spent + computerArmy.get("Anão");
            computerArmy.put("Humano", new Random().nextInt(3));
            spent = spent + computerArmy.get("Humano") *3;
            computerArmy.put("Elfo", new Random().nextInt(3));
            spent = spent + computerArmy.get("Elfo")*5;
            //computerArmy.put("Dragão", new Random().nextInt(1));
            //spent = spent + computerArmy.get("Dragão")*9;
            //computerArmy.put("Gigante", new Random().nextInt(3));
            //spent = spent + computerArmy.get("Gigante")*5;

        } while (spent >50 || computerArmy.isEmpty());
        return computerArmy;
    }
    public Map<String, List<String>> getStatistics(){
        Map<String, List<String>> dictionary = new HashMap<>();

        //As 3 criaturas com mais tesouros encontrados
        List<String> as3MaisCarregadas = creatures.stream()
                .sorted((c1,c2) -> c2.getCollectedTreasures() - c1.getCollectedTreasures())
                .limit(3)
                .sorted((c1,c2) -> c2.getCollectedTreasures() - c1.getCollectedTreasures())
                .map(creature -> creature.getId() + ":" + creature.getCollectedTreasures())
                .collect(Collectors.toList());
        dictionary.put("as3MaisCarregadas", as3MaisCarregadas);

        //As 5 criaturas com mais pontos encontrados
        if (creatures.size() >= 5){
            Comparator<Creature> compareByPointsAndTreasures = Comparator
                    .comparing(Creature::getPoints)
                    .reversed()
                    .thenComparing(Creature::getCollectedTreasures);
            List<String> as5MaisRicas = creatures.stream()
                    .sorted(compareByPointsAndTreasures)
                    .limit(5)
                    .sorted((c1, c2) -> c2.getPoints() - c1.getPoints() )
                    .map(creature -> creature.getId() + ":" + creature.getPoints() + ":" + creature.getCollectedTreasures())
                    .collect(Collectors.toList());
            dictionary.put("as5MaisRicas", as5MaisRicas);
        }else {
            List<String> asExistentes = creatures.stream()
                    .sorted((c1,c2) -> c2.getCollectedTreasures() - c1.getCollectedTreasures())
                    .map(creature -> creature.getId() + ":" + creature.getPoints() + ":" + creature.getCollectedTreasures())
                    .collect(Collectors.toList());
            dictionary.put("as5MaisRicas", asExistentes);
        }

        //As 3 que mais vezes foram alvos de feitiços
        ArrayList<String> osAlvosFavoritos = new ArrayList<>();
        //Stream
        dictionary.put("osAlvosFavoritos", osAlvosFavoritos);

        //As 3 que mais km percorreram? 1 casa 1 km
        ArrayList<String> as3MaisViajadas = new ArrayList<>();
        //Stream
        dictionary.put("as3MaisViajadas", as3MaisViajadas);

        //Total de pontos apanhado por cada tipo de criatura
        ArrayList<String> tiposDeCriaturasESeusTesouros = new ArrayList<>();
        //Stream
        dictionary.put("tiposDeCriaturasESeusTesouros", tiposDeCriaturasESeusTesouros);

        return dictionary;
    } //TODO 3 more statistics
    public void startGame(String[] content, int rows, int columns) throws InsufficientCoinsException{
        //System.out.println( iterate(logCounter) + " - "+"IN startGame\n -----------------------------------\n");

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
        int sumTreasuresTotal = 0;
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
        // Gera imagens diferentes para criaturas da Resistencia.  -- 1 coelho ou 2? - Change filter to a better later. maybe sobel filter
        for (Creature creatureI: creatures){
            if (creatureI.getTeamId() == 20){
                creatureI.setOutroTypeName(creatureI.getOutroTypeName()+"Negate");
            }
            creatureI.setImage(creatureI.getOutroTypeName()+"-"+creatureI.getOrientation()+".png");
        }
       /* Prints dos elementos fatiados e separados. */
        ////System.out.println(iterate(logCounter) + " - "+"  LISTA DE CRIATURAS IN START GAME: "+ creatures + "\n");
        ////System.out.println(iterate(logCounter) + " - "+"  LISTA DE BURACOS IN START GAME: " + holes + "\n");
        ////System.out.println(iterate(logCounter) + " - "+"  LISTA DE TESOUROS IN START GAME: " + treasures + "\n");
        ////System.out.println(iterate(logCounter) + " - "+"  ID DE RESISTENCIA IN START GAME: " + teamRes.getId() + "\n");
        ////System.out.println(iterate(logCounter) + " - "+"  ID DE LDR IN START GAME:" + teamLdr.getId() + "\n");
        ////System.out.println(iterate(logCounter) + " - "+"  HASH DE COMPUTER ARMY IN START GAME: " + createComputerArmy() + "\n\n");

        /* Subtrai moedas para cada criatura de cada time*/
        for (Creature cC: creatures){
            switch (cC.getTeamId()) {
                case 10: teamLdr.removeCoins(cC.getCost());
                    break;
                case 20: teamRes.removeCoins(cC.getCost());
                    break;
            }
        }

        List<Creature> sortedCreatures = creatures;
        creatures = new ArrayList<>();
        sortedCreatures.stream()
                .sorted(Comparator.comparingInt(Element::getId))
                .forEach(c-> creatures.add(c));

        /*if (teamLdr.getCoins() < 0 && teamRes.getCoins() < 0){
            return 1;
        } else if(teamLdr.getCoins() < 0){
            return 2;
        } else if(teamRes.getCoins() < 0){
            return 3;
        }
        return 0;*/ // TODO Exception

    }
    public int getCurrentScore(int teamId){
        //System.out.println(iterate(logCounter) + " - "+"IN getCurrentScore\n -----------------------------------\n");
        //System.out.println(iterate(logCounter) + " - "+"  teamLdr.getId() in CUURRENTSCORE: " +teamLdr.getId());
        //System.out.println(iterate(logCounter) + " - "+"  teamRes.getId() in CUURRENTSCORE: " +teamRes.getId() + "\n");

        if (teamId == teamLdr.getId()){
            return teamLdr.getPoints();
        }else {
            return teamRes.getPoints();
        }
    }
    public int getCoinTotal(int teamId){
        //System.out.println(iterate(logCounter) + " - "+"IN getCoinTotal\n -----------------------------------\n");

        if (teamId == 10){
            ////System.out.println(iterate(logCounter) + " - "+"teamLdr.getCoins(): "  + teamLdr.getCoins() + "\n");
            return teamLdr.getCoins();
        } else{
            ////System.out.println(iterate(logCounter) + " - "+"  teamRes.getCoins() " + teamRes.getCoins() + "\n");
            return teamRes.getCoins();
        }
    }
    public void setInitialTeam(int teamId){
        //System.out.println(iterate(logCounter) + " - "+"Entrou em setInitialTeam\n -----------------------------------\n\n");
        if (teamId == teamLdr.getId()){
            currentTeam = teamLdr;
        } else {
            currentTeam = teamRes;
        }
    }
    public int getCurrentTeamId(){
        //System.out.println(iterate(logCounter) + " - "+"IN currentTeamId\n -----------------------------------\n");
        //System.out.println(iterate(logCounter) + " - "+"  currentTeam.getId(): "+ currentTeam.getId() + "\n");
        return currentTeam.getId();
    } //
    public List<Creature> getCreatures(){
        ////System.out.println(iterate(logCounter) + " - "+"IN getCreatures");
        //System.out.println(iterate(logCounter) + " - "+" IN getCreatures ---  Lista de criaturas do mundo: "+ creatures);
        //System.out.println(iterate(logCounter) + " - "+"Depois entra em getElementId e percorre tabuleiro. Parece ok. ");
        return creatures;
    }
    public int getElementId(int x, int y){
        ////System.out.print(iterate(logCounter) + " - "+"IN getElementId("+x+","+y+")"+ " rowsFgm: "+rowsFgm +" columnsFgm: "+columnsFgm);
        ////System.out.println("Dimensões do mundo (rows e columns): " + rowsFgm + " e " + columnsFgm);

        List<Element> elements = new ArrayList<>();
        elements.addAll(creatures);
        elements.addAll(treasures);
        elements.addAll(holes);

        for(Element e: elements){
            if(e.getX() == x && e.getY() == y){
                return e.getId();
            }
        }
        ////System.out.println("Elements in getElementid: " + elements);
        return 0;
    }
    public String[][] getSpellTypes(){
        //System.out.println(iterate(logCounter) + " - "+"IN getSpellTypes");
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
        //System.out.println(iterate(logCounter) + " - "+"IN getSpell");
        return (getCreature(x, y).getItSpellName());
    }
    public Creature getCreature (int x, int y){
        for (Creature cG : creatures) {
            if (cG.getX() == x && cG.getY() == y){
                return cG;
            }
        }
        return null;
    }
    public boolean enchant (int x, int y, String spellName) {
        if (spellName == null){
            return false;
        }
        //System.out.println(iterate(logCounter) + " - " + "Entrou em enchant");
        Creature c = getCreature(x, y);
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
                    if (validateMovement(c.getX(), c.getY(),c.getNextX(), c.getNextY()) && !c.isFrozen() && !c.isFrozen4Ever()) { // movimento é valido
                        if (checkBalanceToSpell(getCurrentTeamId(), 1)) {
                            c.setEnchant(true);
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
                    if (validateMovement(c.getX(), c.getY(),c.getNextX(), c.getNextY()) && !c.isFrozen() && !c.isFrozen4Ever()) { // movimento é valido
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
                    if (validateMovement(c.getX(), c.getY(),c.getNextX(), c.getNextY()) && !c.isFrozen() && !c.isFrozen4Ever()) { // movimento é valido
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
                    if (validateMovement(c.getX(), c.getY(),c.getNextX(), c.getNextY())) { // movimento é valido
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
                case ("ReduzAlcance"): {// TODO Se reduzir o alcance e o próximo movimento da criatura não for valido.
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
                case ("DuplicaAlcance"): { // TODO Se duplicar o alcance e o próximo movimento da criatura não for valido.
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
        return false;
    }// TODO - Revisar restrições e fluxo de ações. Principalmente implementar as de duplica e reduz alcance.
    public void giveCoins(){
        if (!teamLdr.isTreasuresFoundInThisTurn()){
            teamLdr.addCoins(1);
        } else {
            teamLdr.addCoins(2);
        }
        if (!teamRes.isTreasuresFoundInThisTurn()){
            teamRes.addCoins(1);
        } else {
            teamRes.addCoins(2);
        }
    }
    public void processTurn(){
        turnCounter ++;
        teamLdr.setTreasuresFoundInThisTurn(false);// Zera em todos turnos e incrementa quando acha tesouro
        teamRes.setTreasuresFoundInThisTurn(false);
        int teste = treasures.size();
        for (Creature creature: creatures){

            // Timer para descongelar
            if (creature.isFrozen()){
                creature.setFrozenTime(1);
            }
            // Se atingir o timer então descongela e troca figura
            if (creature.isFrozen() && creature.getFrozenTime() == 1){
                creature.setFrozen(false);
                creature.setImage(creature.getOutroTypeName()+"-"+ creature.getOrientation()+".png");
            }
            // Se tem feitiço pra aplicar, aplica-se.
            if (creature.isEnchant()){
                creature.setEnchant(false);
                executeSpell(creature, creature.getItSpellName());
                if(matchTreasure(creature)){
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
                if (canExecuteStandardMovement(creature)){
                   creature.move();
                    if(matchTreasure(creature)){
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
        switchCurrentTeam();
        giveCoins(); // 1 se não achou tesouro neste turno e 2 se achou.
    }  // still things fix and TODO - Verificar o fluxo, se está correto. Deu erro no teste do número de turnos embora estivesse tudo ok aqui.
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
                    if (validateMovement(creature.getX(), creature.getY(),creature.getNextX(), creature.getNextY())) {
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
                    if (validateMovement(creature.getX(), creature.getY(),creature.getNextX(), creature.getNextY())) {
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
                    if (validateMovement(creature.getX(), creature.getY(),creature.getNextX(), creature.getNextY())) {
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
                    if (validateMovement(creature.getX(), creature.getY(),creature.getNextX(), creature.getNextY())) {
                        creature.empurraParaOeste();
                        creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                        creature.setOrientation("Oeste");
                        creature.setImage(creature.getOutroTypeName()+"-Oeste.png");
                    }
                }
                break;
            }
            case ("ReduzAlcance"): {    // TODO Se reduzir o alcance e o próximo movimento da criatura não for valido.
                creature.reduzAlcance();
                creature.setItSpellName(null);
                break;
            }
            case ("DuplicaAlcance"): { // Tem restrição! TODO Se duplicar o alcance e o próximo movimento da criatura não for válido.
                creature.duplicaAlcance();
                creature.setItSpellName(null);
                break;
            }
        }
    }//TODO - To Check! SOMETHING IS WRONG! nextX nextY creature still spelled. Check getSpell, enchant, processturn, validate e executeSpell.
    private boolean canExecuteStandardMovement(Creature creature) {
        ////System.out.println("Entrou em canExecuteStandardMovement");
        switch (creature.getTypeName()){
            case ("Anão"): case ("Dragão"): case ("Gigante"): {
                switch (creature.getOrientation()){
                    case ("Norte"):{
                        creature.setNextX(creature.getX());
                        creature.setNextY(creature.getY() - creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Sul"):{
                        creature.setNextX(creature.getX());
                        creature.setNextY(creature.getY() + creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Este"):{
                        creature.setNextX(creature.getX() + creature.getRange());
                        creature.setNextY(creature.getY());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Oeste"):{
                        creature.setNextX(creature.getX() - creature.getRange());
                        creature.setNextY(creature.getY());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                }
            }

            case ("Humano"): {
                switch (creature.getOrientation()){
                    case ("Norte"):{
                        creature.setNextX(creature.getX());
                        creature.setNextY(creature.getY() - creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Sul"):{
                        creature.setNextX(creature.getX());
                        creature.setNextY(creature.getY() + creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Este"):{
                        creature.setNextX(creature.getX() + creature.getRange());
                        creature.setNextY(creature.getY());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Oeste"):{
                        creature.setNextX(creature.getX() - creature.getRange());
                        creature.setNextY(creature.getY());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                }
            }

            case ("Elfo"): {
                switch (creature.getOrientation()){
                    case ("Norte"):{
                        creature.setNextX(creature.getX());
                        creature.setNextY(creature.getY() - creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Sul"):{
                        creature.setNextX(creature.getX());
                        creature.setNextY(creature.getY() + creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Este"):{
                        creature.setNextX(creature.getX() + creature.getRange());
                        creature.setNextY(creature.getY());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Oeste"):{
                        creature.setNextX(creature.getX() - creature.getRange());
                        creature.setNextY(creature.getY());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Nordeste"):{
                        creature.setNextY(creature.getY() - creature.getRange());
                        creature.setNextX(creature.getX() + creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Sudeste"):{
                        creature.setNextY(creature.getY() + creature.getRange());
                        creature.setNextX(creature.getX() + creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Sudoeste"):{
                        creature.setNextY(creature.getY() + creature.getRange());
                        creature.setNextX(creature.getX() - creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                    case ("Noroeste"):{
                        creature.setNextY(creature.getY() - creature.getRange());
                        creature.setNextX(creature.getX() - creature.getRange());
                        return validateMovement(creature.getX(), creature.getY(), creature.getNextX(), creature.getNextY());
                    }
                }
            }
        }

    return false;
    }
    private boolean matchTreasure(Creature creatureT) {
        for (Iterator<Treasure> i = treasures.iterator(); i.hasNext();){ // Artifício muito louco para passar do ERRO ConcurrentModificationException
            Treasure treasure = i.next();
            if (creatureT.getX() == treasure.getX() && creatureT.getY() == treasure.getY()){ // MATCH
                sumTreasuresLeft -= treasure.getValue();
                if (treasure.getValue() ==3){ //gold
                    creatureT.addGold();
                }
                if (treasure.getValue() ==2){ //silver
                    creatureT.addSilver();
                }
                if (treasure.getValue() ==1){ //bronze
                    creatureT.addBronze();
                }
                if (creatureT.getTeamId() == 10){
                    teamLdr.addPoints(treasure.getValue()); // Add pontos time
                } else {
                    teamRes.addPoints(treasure.getValue()); // Add pontos time
                }
                creatureT.collectTreasure(treasure.getValue());
                i.remove();
                return true;
            }
        }
return false;
    }
    private boolean validateMovement(int x, int y, int nextX, int nextY) {

        if (nextX < 0 || nextY < 0){ // fora da tela
            return false;
        }
        if (nextX > columnsFgm-1 || nextY > rowsFgm-1){ // fora da tela
            return false;
        }

        if (getElementId(nextX, nextY) <=-500){ // buraco
            return false;
        }

        for (Creature otherCreature: creatures){
            if (!otherCreature.equals(getCreature(x,y))){
                if (nextX == otherCreature.getX() && nextY == otherCreature.getY()){
                    return false;
                }
                if (nextX == otherCreature.getNextX() && nextY == otherCreature.getNextY()){
                    return false;
                }
            }
        }
        /*outra criatura */
        return getElementId(nextX, nextY) <= 0;
    }

    // Checar saldo;
    private boolean checkBalanceToSpell(int teamId, int spellCost) {

        if (teamId == 10){
            return teamLdr.checkBalanceToSpell(spellCost);
        }
        if (teamId == 20){
            return teamRes.checkBalanceToSpell(spellCost);
        }
        //System.out.println(iterate(logCounter) + " - "+"O teamId passado não é válido. Impossível consultar saldo ");
        return false;

    }

    // Debita moedas dos feitiços
    private void taxSpell(int teamId, int spellCost) {
        if (teamId == 10){
            teamLdr.removeCoins(spellCost);
            //System.out.println(iterate(logCounter) + " - "+"Spell taxed from LORD ELDER");
        }
        if (teamId == 20){
            teamRes.removeCoins(spellCost);
            //System.out.println(iterate(logCounter) + " - "+"Spell taxed from RESISTENCIA:");
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
        //System.out.println("Entrou em gameIsOver");

        if (treasures.size() == 0 || turnsWithoutTreasure == 15){
            return true;
        }

        //sumTreasuresLeft é zerada em startGame e incrementada em matchTreasurex
        return teamLdr.getPoints() > teamRes.getPoints() + sumTreasuresLeft || teamRes.getPoints() > teamLdr.getPoints() + sumTreasuresLeft;
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
            for (Creature cR: creatures){
                results.add(cR.getId() +" : " + cR.getTypeName() + " : " + cR.getGold() + " : " + cR.getSilver() + " : " + cR.getBronze() + " : " + cR.getCollectedTreasures());
            }
        } else {
            if (teamRes.getPoints() == teamLdr.getPoints()){
                results.add("Welcome to FANDEISIA");
                results.add("Resultado: EMPATE");
                results.add("LDR: "+ teamLdr.getPoints());
                results.add("RESISTENCIA: "+ teamRes.getPoints());
                results.add("Nr. de Turnos jogados: "+ turnCounter);
                results.add("-----");
                for (Creature cR: creatures){
                    results.add(cR.getId() +" : " + cR.getTypeName() + " : " + cR.getGold() + " : " + cR.getSilver() + " : " + cR.getBronze() + " : " + cR.getCollectedTreasures());
                }
            }else {
                results.add("Welcome to FANDEISIA");
                results.add("Resultado: Vitória da equipa " + teamRes.getName());
                results.add("RESISTENCIA: "+ teamRes.getPoints());
                results.add("LDR: "+ teamLdr.getPoints());
                results.add("Nr. de Turnos jogados: "+ turnCounter);
                results.add("-----");
                for (Creature cR: creatures){
                    results.add(cR.getId() +" : " + cR.getTypeName() + " : " + cR.getGold() + " : " + cR.getSilver() + " : " + cR.getBronze() + " : " + cR.getCollectedTreasures());
                }
            }
        }
        return results;
    }
    public boolean saveGame (File fich){
        FandeisiaGameManager game = new FandeisiaGameManager();

        return true;
    }
    public boolean loadGame (File fich){
        ////System.out.println("Estou em loadGame");

        return true;
    }
    public String whoIsLordEder(){
        ////System.out.println("Estou em whoIsLordEder");

        return "Éderzito António Macedo Lopes";
    } 
    public List<String> getAuthors(){
        return Collections.singletonList("Allyson Rodrigues");
    }
}
