package pt.ulusofona.lp2.fandeisiaGame;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
public class FandeisiaGameManager{

    public FandeisiaGameManager(){
    }

    private List<Treasure> treasures = new ArrayList<>(); // Quando for 0 gameIsOver = true.
    private List<Hole> holes = new ArrayList<>();
    private List<Creature> creatures = new ArrayList<>();

    private Team teamLdr = new Team (10, "LDR", 0, 50);
    private Team teamRes = new Team (20, "RESISTENCIA", 0, 50);
    private Team currentTeam = new Team(0,"0",0,0); // uma espécie de cópia...? mas funciona.

    private int rows;
    private int columns;
    private int currentTurnCounter;
    private int turnsWithoutTreasure; // Será usado no gameIsOver. Quando for for >= 15 gameIsOver = true;
    private long logCounter = 0; // usado como contador do meu log de execução do jogo
    private int nextX; // Tão aqui pra auxiliar por enquanto... talvez já possam ir pra dentro da função que a usa. todo
    private int nextY;

    private void setRows(int rows){
        this.rows = rows;
    }
    private void setColumns(int columns){
        this.columns = columns;
    }
    private int getRows(){
        return rows;
    }
    private int getColumns(){
        return columns;
    }

    // Dado binário (0 ou 1)
    int rollDiceBinary(){ return ThreadLocalRandom.current().nextInt(1 );
    }

    // Contador para as linhas impressas no console vindas das funções - Lembrar que se crashar o long deve-se zerar no limite.
    long iterate(long i){
        if (i < 2147483647){
        logCounter++;
        return logCounter;
        }else {
            logCounter = -2000000000;
        return logCounter;
        }
    }

    public String[][] getCreatureTypes(){
        System.out.println( iterate(logCounter) + " - "+ "IN getCreatureTypes\n -----------------------------------\n");
        return new String[][]{
                {"Anao", "dwarf.png", "add description", String.valueOf(1)},
                {"Dragao", "dragon.png", "add description", String.valueOf(9)},
                {"Elfo", "elf.png", "add description", String.valueOf(5)},
                {"Gigante", "giant.png", "add description", String.valueOf(5)},
                {"Humano", "human.png", "add description", String.valueOf(3)},
        };
    } //OK - 29-12

    public Map<String, Integer> createComputerArmy(){
        System.out.println( iterate(logCounter) + " - "+"IN createComputerArmy\n -----------------------------------\n");

        Map<String, Integer> computerArmy = new HashMap<>();

        // Criando 1 apenas para teste.
        //computerArmy.put("Anao", new Random().nextInt(4)); // criar um random entre 0 e 3.
        //computerArmy.put("Dragao", new Random().nextInt(4));
        //computerArmy.put("Elfo", new Random().nextInt(4));
        //computerArmy.put("Gigante", new Random().nextInt(4));
        //computerArmy.put("Humano", new Random().nextInt(4));
        //computerArmy.put("Humano", new Random().nextInt(4));
        computerArmy.put("Dragao", 1);
        return computerArmy;
        /*
        Deve devolver o exército escolhido pelo
        computador.
        A função deve devolver um ​Map​ cuja chave
        é o tipo da criatura e o valor é o número de
        criaturas desse tipo que o jogador
        automático pretende.
        */
    } //OK 29-12

    public int startGame(String[] content, int rows, int columns){
        System.out.println( iterate(logCounter) + " - "+"IN startGame\n -----------------------------------\n");

        // Pega rowns e columns e 'seta' nossa rows e columns.
        setRows(rows);
        setColumns(columns);

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
                    case ("Anao") : creatures.add(new Dwarf(id, x, y, teamId, 1, orientation));
                        break;
                    case("Dragao") : creatures.add(new Dragon(id, x, y, teamId, 9, orientation));
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
                    }
                    if(type.equals("silver")){
                        treasures.add(new Treasure (id, x, y, 2));
                    }
                    if(type.equals("bronze")){
                        treasures.add(new Treasure (id, x, y, 1));
                    }
                }
            }
        }

        // Gerar imagens diferentes para cada time
        /*for(Creature creature: creaturesLdr){
            if (creature.getType().equals("baker")){
                stringType[0][1] = "bird.png";
            } else if (creature.getType().equals("milker")){
                stringType[0][1] = "simba.png";
            }
        }*/


        // Prints de fluxo dos elementos fatiados e separados.
        System.out.println(iterate(logCounter) + " - "+"  LISTA DE CRIATURAS IN START GAME: "+ creatures + "\n");
        System.out.println(iterate(logCounter) + " - "+"  LISTA DE BURACOS IN START GAME: " + holes + "\n");
        System.out.println(iterate(logCounter) + " - "+"  LISTA DE TESOUROS IN START GAME: " + treasures + "\n");
        System.out.println(iterate(logCounter) + " - "+"  ID DE RESISTENCIA IN START GAME: " + teamRes.getId() + "\n");
        System.out.println(iterate(logCounter) + " - "+"  ID DE LDR IN START GAME:" + teamLdr.getId() + "\n");
        System.out.println(iterate(logCounter) + " - "+"  HASH DE COMPUTER ARMY IN START GAME: " + createComputerArmy() + "\n\n");

        //Subtrai moedas para cada criatura de cada time
        for (Creature c: creatures){
            switch (c.getTeamId()) {
                case 10: teamLdr.removeCoins(c.cost);
                    break;
                case 20: teamRes.removeCoins(c.cost);
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

    } //OK 29-12

    public int getCurrentScore(int teamId){
        System.out.println(iterate(logCounter) + " - "+"IN getCurrentScore\n -----------------------------------\n");
        System.out.println(iterate(logCounter) + " - "+"  teamLdr.getId() in CUURRENTSCORE: " +teamLdr.getId());
        System.out.println(iterate(logCounter) + " - "+"  teamRes.getId() in CUURRENTSCORE: " +teamRes.getId() + "\n");

        if (teamId == teamLdr.getId()){
            return teamLdr.getPoints();
        }else {
            return teamRes.getPoints();
        }
    } //OK 29-12 Está sendo chamada duas vezes seguidas no Visualizador. Depois é chamadas mais 2 vezes.

    public int getCoinTotal(int teamId){
        System.out.println(iterate(logCounter) + " - "+"IN getCoinTotal\n -----------------------------------\n");

        if (teamId == 10){
            System.out.println(iterate(logCounter) + " - "+"teamLdr.getCoins(): "  + teamLdr.getCoins() + "\n");
            return teamLdr.getCoins();
        } else{
            System.out.println(iterate(logCounter) + " - "+"  teamRes.getCoins() " + teamRes.getCoins() + "\n");

            return teamRes.getCoins();
        }
    } //OK 29-12 Também se repete depois de escolher exercito

    public void setInitialTeam(int teamId){
        System.out.println(iterate(logCounter) + " - "+"Entrou em setInitialTeam\n -----------------------------------\n\n");

        //Seleção aleatória usando o dado. Ignora teamId vinda dessa função e sorteia.
        int resultDice = rollDiceBinary();
        if (resultDice == 0){
            currentTeam = teamLdr;
        }else {
            currentTeam = teamRes;
        }
    } //OK 29-12

    public int getCurrentTeamId(){
        System.out.println(iterate(logCounter) + " - "+"IN currentTeamId\n -----------------------------------\n");
        System.out.println(iterate(logCounter) + " - "+"  currentTeam.getId(): "+ currentTeam.getId() + "\n");
        return currentTeam.getId();
    } //OK 29-12

    public List<Creature> getCreatures(){
        System.out.println(iterate(logCounter) + " - "+"IN getCreatures");
        System.out.println(iterate(logCounter) + " - "+"    Lista de criaturas do mundo: "+ creatures);
        return creatures;
    } // OK 29-12

    public int getElementId(int x, int y){
        System.out.println(iterate(logCounter) + " - "+"IN getElementId("+x+","+y+")");

        //System.out.println("    Dimensões do mundo (rows e columns): " + ROWS + " e " + COLUMNS);

        List<Element> elements = new ArrayList<>();
        elements.addAll(creatures);
        elements.addAll(treasures);
        elements.addAll(holes);

        for(Element e: elements){
            if(e.getX() == x && e.getY() == y){
                return e.getId();
            }
        }
        // System.out.println("\n");
        return 0;
    } //OK 29-12 percorre tabuleiro e quando acha uma criatura chama o getSpell();


    public String[][] getSpellTypes(){
        System.out.println(iterate(logCounter) + " - "+"IN getSpellTypes");
        return new String[][]{
                {"pushNorth", "Descrição do feitiço", String.valueOf(1)},
                {"pushEast", "Descrição do feitiço", String.valueOf(1)},
                {"pushSouth", "Descrição do feitiço", String.valueOf(1)},
                {"pushWest", "Descrição do feitiço", String.valueOf(1)},
                {"reduceRange", "Descrição do feitiço", String.valueOf(2)},
                {"doubleRange", "Descrição do feitiço", String.valueOf(3)},
                {"freezes", "Descrição do feitiço", String.valueOf(3)},
                {"freezes4Ever", "Descrição do feitiço", String.valueOf(10)},
                {"unfreezes", "Descrição do feitiço", String.valueOf(8)},
        };
    } //ok 01/01

    public String getSpell (int x, int y){ // Scanner dos spells de todas as criaturas presentes. Ela que marca a varinha na criatura que retornou true para enchant! ok.
        System.out.println(iterate(logCounter) + " - "+"IN getSpell");
        //int creatureId = getCreatureIdByPosition(x, y); // Seria o mesmo que getElementId??????! Provavelmente simm...
        for(Creature creature: creatures){
            if(creature.getX() == x && creature.getY() == y){
                String spellName = creature.getItSpellName();
                if (spellName != null){ // <<------------------ não pode ser null
                    return spellName;
                }
            }
        }
        System.out.println("ERRO - retornou null em GetSpell()");
        return null; // É suposto que o simulador chame apenas quando há criatura na coordenada.
    } // ok 01/01

    public boolean enchant (int x, int y, String spellName){

        System.out.println(iterate(logCounter) + " - "+"Estou em enchant");
        for (Creature c: creatures){
            if (c.getX() == x && c.getY() == y) {
                if (c.isEnchant()){ // Se tá enfeitiçada só recebe outro feitiço se der match congela4Ever/descongela
                    if (c.isFrozen() && spellName.equals("unfreezes")){ // Se está enfeitiçada vê se tá congelada e o feitiço a ser aplicado é descongela. Se for retorna true e descongela.
                        //c.unfreezes();
                        if (checkBalanceToSpell(getCurrentTeamId())){
                            c.setEnchant(true);
                            c.setItSpellName(spellName);
                            taxSpell(getCurrentTeamId(), 8);
                            return true;
                        }
                        // revisar pois pode não precisar já que uma criatura não pode ser enfeitiçada duas vezez no mesmo turno, ao que parece. todo
                    } /*else {
                          return false; //Criatura está enfeitiçada mas não dá match congelada4ever-descongela enchant retorna falso
                      }*/
                }

                else {

                    // A criatura não estava enfeitiçada. Agora ela recebe o feitiço e esta fç enchant retorna true; <---> SE FOR VÁLIDO O QUE FOR O FEITIÇO!!!
                    if (spellName != null){
                        switch (spellName){
                            case ("freezes"): {
                                if (checkBalanceToSpell(getCurrentTeamId())){
                                    c.setEnchant(true);
                                    taxSpell(getCurrentTeamId(), 3);
                                    c.setItSpellName(spellName);
                                    //c.freezes();
                                    //c.setItSpellName("freezes");
                                    return true;
                                } else {
                                    return false;
                                }
                            }

                            case ("freezes4Ever"):{
                                if (checkBalanceToSpell(getCurrentTeamId())){
                                    c.setEnchant(true);
                                    //c.freezes4Ever();
                                    //c.setItSpellName("freezes4Ever");
                                    taxSpell(getCurrentTeamId(), 10);
                                    c.setItSpellName(spellName);
                                    return true;
                                } else {
                                    return false;
                                }
                            }

                            case ("unfreezes"):{
                                if (checkBalanceToSpell(getCurrentTeamId())){
                                    c.setEnchant(true);
                                    //c.freezes();
                                    //c.setItSpellName("unfreezes");
                                    taxSpell(getCurrentTeamId(), 8);
                                    c.setItSpellName(spellName);

                                    return true;
                                } else {
                                    return false; // Neste caso não se faz unfreeze porque se tivesse mesmo frozen4Ever teria unfrozed lá em cima!
                                }
                            }

                            case ("pushNorth"): {
                                nextX = x;
                                nextY = y - 1;
                                if (validateMovement(x, y, nextX, nextY)){ // movimento é valido
                                    if (checkBalanceToSpell(getCurrentTeamId())){
                                        c.setEnchant(true);
                                        //c.freezes();
                                        //c.setItSpellName("pushNorth");
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

                            case ("pushEast"): {
                                nextX = x+ 1;
                                nextY = y;
                                if (validateMovement(x, y, nextX, nextY)){ // movimento é valido
                                    if (checkBalanceToSpell(getCurrentTeamId())){
                                        c.setEnchant(true);
                                        //c.setItSpellName("pushEast");
                                        //c.pushEast();
                                        taxSpell(getCurrentTeamId(),1);
                                        c.setItSpellName(spellName);
                                        // if(getElementId(nextX))matchTreasure() -- TODO --> pegar tesouro. Método de creature ou FGM ou Treasure?
                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false; // Se não entrar nesse if tem erro!
                                }
                            }

                            case ("pushSouth"): {
                                nextX = x;
                                nextY = y + 1;
                                if (validateMovement(x, y, nextX, nextY)){ // movimento é valido
                                    if (checkBalanceToSpell(getCurrentTeamId())){
                                        c.setEnchant(true);
                                        //c.freezes();
                                        //c.setItSpellName("pushSouth");
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

                            case ("pushWest"): {
                                nextX = x -1;
                                nextY = y;
                                if (validateMovement(x, y, nextX, nextY)){ // movimento é valido
                                    if (checkBalanceToSpell(getCurrentTeamId())){
                                        c.setEnchant(true);
                                        //c.freezes();
                                        //c.setItSpellName("pushWest");
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

                            case ("reducesRange"): {

                                if (checkBalanceToSpell(getCurrentTeamId())){
                                    c.setEnchant(true);
                                    taxSpell(getCurrentTeamId(), 2);
                                    c.setItSpellName(spellName);
                                    //c.reducesRange();
                                    //c.setItSpellName("reducesRange");
                                }

                                return true;
                            }

                            case ("doubleRange"): {
                                if (checkBalanceToSpell(getCurrentTeamId())){
                                    c.setEnchant(true);
                                    taxSpell(c.getTeamId(), 3);
                                    c.setItSpellName(spellName);
                                    //c.doubleRange(c.getRange());
                                    //c.setItSpellName("doubleRange");
                                }

                                return true;
                            }
                        }
                    }
                }
                // } else {
                // O feitiço leva para um lugar ocupado ou buraco ou para fora do tabuleiro? --> Retorna falso
                // A criatura está congelada ou congelada4ever? --> Retorna falso
                // Se tá congelada4ever e spellName == descongela return true e c.isFrozen(c.getId()) = false;
                // Outras casos retorna true.
                // c.

                // return true;
            }

        } return false; // Porque nesse x e y não há criatura.

    }


    public void processTurn(){
        System.out.println("Entrou em  processTurn\n");
        for (Creature creature: creatures){
            if (creature.isEnchant()){
                executeSpell(creature.getId(), creature.getItSpellName());
            }
            // executeSpell(creature.getItSpellName());
            /*executeStandartMovement(creature.getX(), creature.getY(), creature.getOrientation(),
                                    creature.isFrozen(),creature.isFrozen4Ever(), creature.getTypeName());
            switch (creature.getTypeName()){
                case ("Dwarf"): {
                    if(validateMovement(creature.getX(),creature.getY(),nextX,nextY)){
                        creature.move();
                    }
                }
            }*/

            //Fazer os outros switchs! TODO

        }

        /*
        Deve processar um turno do jogo. Inclui o
        movimento das criaturas.

        -- Lembrar que a criatura só se movimenta se:
            validar validateMovement() retorna true;
            cada criatura tem seu proprio movimento. apenas chamamos move ?? mas como validar depois do move? pensarr....
        */

    }


    private void executeSpell(int id,String spell) {

        for (Creature creature : creatures){

            if (creature.getId() == id){
                //creature.setItSpellName(spell); Não precisa. Já foi set antes.
                switch(spell){
                    case ("freezes"): {
                        creature.setFrozen(true); // nos movimentos por o if isFrozen
                        creature.setEnchant(false);
                        //creature.setImage("IMAGEM CONGELADA"); //todo
                        creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado (já pode receber outros feitiços) Como vai ficar isso? Por enquanto parece ok.
                    }

                    case ("freezes4Ever") : {
                        creature.setFrozen4Ever(true); // nos movimentos por o if isFrozen4Ever
                        creature.setEnchant(false); //todo
                        //creature.setImage("IMAGEM CONGELADA 4 EVER");
                        creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado (já pode receber outros feitiços - no caso, apenas o descongela!). todo
                    }

                    case ("pushNorth"): {
                        if (validateMovement(creature.getX(), creature.getY(), creature.getX(),creature.getY()-1)) {
                            creature.setY(creature.getY()-1);
                            creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                            creature.setEnchant(false);
                            break;
                        }
                        break;
                    }

                    case ("pushEast"): {
                        if (validateMovement(creature.getX(), creature.getY(), creature.getX()+1,creature.getY())) {
                            creature.setX(creature.getX()+1);
                            creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                            creature.setEnchant(false);
                            break;
                        }
                        break;
                    }

                    case ("pushSouth"): {
                        if (validateMovement(creature.getX(), creature.getY(), creature.getX(),creature.getY()+1)) {
                            creature.setY(creature.getY() + 1);
                            creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                            creature.setEnchant(false);
                            break;
                        }
                        break;
                    }

                    case ("pushWest"): {
                        if (validateMovement(creature.getX(), creature.getY(), creature.getX()-1,creature.getY())) {
                            creature.setX(creature.getX() - 1);
                            creature.setItSpellName(null); // Já foi executado o feitiço, então passa a ficar em estado desencantado.
                            creature.setEnchant(false);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }

    private void executeStandartMovement(int x, int y, String orientation, boolean frozen,
                                         boolean frozen4Ever, String typeName) {
        for (Creature creature: creatures){
            if (!creature.isFrozen() && !creature.isFrozen4Ever()){

                switch (creature.getTypeName()){
                    case ("Dwarf"): {
                        switch (creature.getOrientation()){
                            case ("Norte"):{
                                creature.setNextX(creature.getRange());
                                creature.setNextY(creature.getRange());
                                if (validateMovement(x,y,creature.getNextX(),creature.getNextY())){
                                    creature.move();
                                }

                            }
                        }
                    }
                }

            } else {
                creature.setFrozen(false);
            }



        }

    }
    public boolean gameIsOver(){
        System.out.println("Estou em gameIsOver");

        return false;
        /*Deve devolver ​true​ caso já tenha sido
        alcançada uma das condições de paragem
        do jogo e ​false​ em caso contrário.
        */
    }

    public List<String> getResults(){
        System.out.println("Estou em getResults");

        return new ArrayList<>();
        /*Devolve uma lista de ​Strings​ que
        representem os resultados do jogo,
        conforme descrito na secção dos
        “Resultados da execução ...”.
        Este método não pode devolver ​null​.
        Caso não calculem a informação
        respectiva, devem devolver ​uma lista
        vazia​.
        */
    }

    //-----------------------------------------------

    // Validar movimento:
    private boolean validateMovement(int x, int y, int nextX, int nextY) {

        if (nextX < 0 || nextY < 0){ // fora da tela
            return false;
        }
        if (nextX > columns-1 || nextY > rows-1){ // fora da tela
            return false;
        }

        if (getElementId(nextX, nextY) <=-500){ // buraco
            return false;
        }

        /*outra criatura */
        return getElementId(nextX, nextY) <= 0;
    }

    // Checar saldo;
    private boolean checkBalanceToSpell(int teamId) {

        if (teamId == 10){
            return teamLdr.checkBalanceToSpell(1);
        }
        if (teamId == 20){
            return teamRes.checkBalanceToSpell(1);
        }
        System.out.println(iterate(logCounter) + " - "+"O teamId passado não é válido. Impossível consultar saldo ");
        return false;

    }

    // Debita moedas dos feitiços
    private void taxSpell(int teamId, int cost) {
        if (teamId == 10){
            teamLdr.removeCoins(cost);
            System.out.println(iterate(logCounter) + " - "+"Spell taxed from LORD ELDER");

        }
        if (teamId == 20){
            teamRes.removeCoins(cost);
            System.out.println(iterate(logCounter) + " - "+"Spell taxed from RESISTENCIA:");
        }
    }

    /* int getCreatureIdByPosition(int x, int y){
        for(Creature creature: creatures){
            if (creature.getX() == x && creature.getY() == y ){
                return creature.getId();
            }
        }
        System.out.println("ERRO: getCreatureIdByPosition não encontrou criatura na cordenada ("+x+","+y+") " + "- Pode ser problema em enchant");
        return 0;
    }*/ //Parece o mesmo que getElementId()...

    // Feitiços:

    // Feitiços:

    /*Criatura está sendo enfeitiçada mesmo que o feitiço não tenha tido sucesso.

     * Em enchant os feitiços não estão sendo aplicados, exceto no caso pushEast
     * * Sobreposição de criaturas
     * * Feitiços estão sendo aplicados apenas direto sem o usuário parar a jogada. -- Parece que o propósito é este. Não?
     * As moedas ainda não estão sendo removidas

     * */






    // Save e Load Game:
    public boolean saveGame (File fich){
        System.out.println("Estou em saveGame");
        return true;
    }
    public boolean loadGame (File fich){
        System.out.println("Estou em loadGame");

        return true;
    }

    //Não tem que mexer mais:
    public String whoIsLordEder(){
        System.out.println("Estou em whoIsLordEder");

        return "Éderzito António Macedo Lopes";
    } //OK 29-12
    public List<String> getAuthors(){
        return Collections.singletonList("Allyson Rodrigues");
    }


/* Grupo 373 on leaderboard
------------------------------------------------------------
    JUnit Summary (Teacher Tests)
    Tests run: 24, Failures: 21, Errors: 0, Time elapsed: 0.108 sec
-------------------------------------------------------------------

FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_002_Elfo
java.lang.AssertionError: getElementId() expected:<0> but was:<3>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_002_Elfo(TestTeacherSimuladorP2.java:700)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_004_JogoCompletoEmpate
java.lang.AssertionError: getCurrentTeamId() expected:<20> but was:<10>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_004_JogoCompletoEmpate(TestTeacherSimuladorP2.java:1013)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_010_HumanoBasico
java.lang.AssertionError: expected:<0> but was:<1>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_010_HumanoBasico(TestTeacherSimuladorP2.java:804)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_011_AnaoEHumano
java.lang.AssertionError: getElementId() expected:<3> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_011_AnaoEHumano(TestTeacherSimuladorP2.java:888)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_012_DuplicaAlcanceBasico
java.lang.AssertionError: expected:<DuplicaAlcance> but was:<null>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_012_DuplicaAlcanceBasico(TestTeacherSimuladorP2.java:953)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_012_TooManyTurnsWithoutTreasuresBeingFoundAfterOneCaptureIsDone
java.lang.AssertionError: expected:<3> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_012_TooManyTurnsWithoutTreasuresBeingFoundAfterOneCaptureIsDone(TestTeacherSimuladorP2.java:378)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_014_JogoCompletoSoComAnoesESemFeiticos
java.lang.AssertionError: getElementId() expected:<1> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_014_JogoCompletoSoComAnoesESemFeiticos(TestTeacherSimuladorP2.java:447)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_015_JogoCompleto_2
java.lang.AssertionError: getElementId() expected:<1> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_015_JogoCompleto_2(TestTeacherSimuladorP2.java:580)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_018_enfeiticarComCongelaEDescongela
java.lang.AssertionError: getSpell() expected:<Congela4Ever> but was:<null>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_018_enfeiticarComCongelaEDescongela(TestTeacherSimuladorP2.java:1611)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_019_Gigante
java.lang.AssertionError: expected:<0> but was:<4>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_019_Gigante(TestTeacherSimuladorP2.java:1320)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_01_Leitura_Escrita_Ficheiro1
java.lang.AssertionError: Não criou o ficheiro
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_01_Leitura_Escrita_Ficheiro1(TestTeacherSimuladorP2.java:43)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_020_enfeiticarComEmpurrasHorizontais
java.lang.AssertionError: getCoinTotal() expected:<36> but was:<37>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_020_enfeiticarComEmpurrasHorizontais(TestTeacherSimuladorP2.java:1514)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_021_enfeiticarComEmpurrasVerticais
java.lang.AssertionError: getCoinTotal() expected:<36> but was:<37>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_021_enfeiticarComEmpurrasVerticais(TestTeacherSimuladorP2.java:1438)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_023_ProcessTurnDaMoedas
java.lang.AssertionError: expected:<0> but was:<1>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_023_ProcessTurnDaMoedas(TestTeacherSimuladorP2.java:1369)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_02_Anao_EscapeTheCornerTest
java.lang.AssertionError: A fn getElementId() devolveu o valor errado. expected:<1> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_02_Anao_EscapeTheCornerTest(TestTeacherSimuladorP2.java:279)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_02_GetCreatureTypes
java.lang.AssertionError: A fn getCreatureTypes() nao devolveu o tipo de criatura:Anão
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_02_GetCreatureTypes(TestTeacherSimuladorP2.java:102)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_04_StartGameAndToString
java.lang.AssertionError: A fn getCreatures() devolveu o nr errado de elementos. expected:<5> but was:<3>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_04_StartGameAndToString(TestTeacherSimuladorP2.java:148)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_05_IniciaJogoDuasVezes
java.lang.AssertionError: A fn getCreatures() devolveu o nr errado de criaturas. expected:<2> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_05_IniciaJogoDuasVezes(TestTeacherSimuladorP2.java:185)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_06_GetSpellTypes
java.lang.AssertionError: A fn getSpellTypes() nao devolveu todos os feiticos existentes. expected:<9> but was:<2>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_06_GetSpellTypes(TestTeacherSimuladorP2.java:253)


FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_08_DragaoBasico
java.lang.AssertionError: getElementId() expected:<1> but was:<0>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_08_DragaoBasico(TestTeacherSimuladorP2.java:1131)



FAILURE: pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_09_JogoCompletoComFeiticos
java.lang.AssertionError: getCoinTotal(10) expected:<38> but was:<47>
	at pt.ulusofona.lp2.fandeisiaGame.TestTeacherSimuladorP2.test_09_JogoCompletoComFeiticos(TestTeacherSimuladorP2.java:1196)

*/
}
