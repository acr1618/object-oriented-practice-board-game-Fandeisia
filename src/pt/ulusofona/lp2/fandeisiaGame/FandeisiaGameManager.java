package pt.ulusofona.lp2.fandeisiaGame;
import java.io.File;
import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
public class FandeisiaGameManager{

    public FandeisiaGameManager(){
    }

    List<Treasure> treasures = new ArrayList<>(); // Quando for 0 gameIsOver = true.
    List<Hole> holes = new ArrayList<>();
    List<Creature> creatures = new ArrayList<>();

    Team teamLdr = new Team (10, "LDR", 0, 50);
    Team teamRes = new Team (20, "RESISTENCIA", 0, 50);
    Team currentTeam = new Team(0,"0",0,0);

    int ROWS;
    int COLUMNS;
    int currentTurnCounter;
    int turnsWithoutTreasure; // Será usado no gameIsOver. Quando for for >= 15 gameIsOver = true;
    long logCounter = 0;

    public void setRows(int rows){
        this.ROWS = rows;
    }
    public void setColumns(int columns){
        this.COLUMNS = columns;
    }
    public int getRows(){
        return ROWS;
    }
    public int getColumns(){
        return COLUMNS;
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

    /*int setCurrentTeamId(){
        this.dice = rollDiceBinary();
        if (this.dice =0 ){
            currentTeamId = 10;
        }
        return 0;
    }
    int dice =
    if (dice = 0){
        Team CurrentTeam = teamLdr;
    } else {
        Team CurrentTeam = teamRes;
    }*/  // serCurrentTeamId(?)


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
        computerArmy.put("Humano", 1);
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
    } //OK 29-12 Está sendo chamada duas vezes seguidas no Visuliador. Depois é chamadas mais 2 vezes.

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

        //Seleção aleatório usando o dado. Ignora teamId vinda dessa função...
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

    public String getSpell (int x, int y){
        System.out.println(iterate(logCounter) + " - "+"IN getSpell");
        return null;
    }// null 29-12 - FEITIÇO



    // Divisor de águas - Pós criação de exército ok



    public void processTurn(){
        System.out.println("Entrou em  processTurn\n");

        /*
        Deve processar um turno do jogo. Inclui o
        movimento das criaturas.
        */

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





    // Feitiços and afins :

    public String[][] getSpellTypes(){
        System.out.println("Got inside getSpellTypes");
        return new String[][]{
                {"Empurra Para Norte", "Descrição do feitiço", String.valueOf(1)},
                {"Empurra Para Este", "Descrição do feitiço", String.valueOf(1)},
                {"Empurra Para Sul", "Descrição do feitiço", String.valueOf(1)},
                {"Empurra Para Oeste", "Descrição do feitiço", String.valueOf(1)},
                {"Reduz Alcance", "Descrição do feitiço", String.valueOf(2)},
                {"Duplica Alcance", "Descrição do feitiço", String.valueOf(3)},
                {"Congela", "   Descrição do feitiço", String.valueOf(3)},
                {"Congela 4 Ever", "Descrição do feitiço", String.valueOf(10)},
                {"Descongela", "Descrição do feitiço", String.valueOf(8)},
        };
    }
    public boolean enchant (int x, int y, String spellName){
        System.out.println("Estou em enchant");
        return true;
    }
    public boolean pushNorth(int x, int y){
        return true; // Custa 1 Move 1 para Norte
    }
    public boolean pushEast(int x, int y){
        return true; // Custa 1 Move 1 para Leste
    }
    public boolean pushSouth(int x, int y){
        return true; // Custa 1 move para Sul
    }
    public boolean pushWest(int x, int y){
        return true; // Custa 1 Move 1 para Oeste
    }
    public boolean reducesRange(int x, int y){
        return true; // Custa 2 Reduz o alcance para
    }
    public boolean doubleRange(int x, int y){
        return true; // Custa 3 Aumenta alcance para o dobro
    }
    public boolean freezes(int x, int y){
        return true; // Custa 3 Não move neste turno
    }
    public boolean freezes4Ever(int x, int y){
        return true; // Custa 10 Não move até o fim do jogo
    }
    public boolean unfreezes(int x, int y){
        return true; // Custa 8 Inverte aplicação do Freezes4Ever.
    }

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

}
