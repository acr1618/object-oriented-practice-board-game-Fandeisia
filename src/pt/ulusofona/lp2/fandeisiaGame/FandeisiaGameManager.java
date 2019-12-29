package pt.ulusofona.lp2.fandeisiaGame;
import java.io.File;
import java.util.*;

public class FandeisiaGameManager{

    public FandeisiaGameManager(){
    }
    public List<Treasure> treasures = new ArrayList<>(); // Quando for 0 gameIsOver = true.
    public List<Hole> holes = new ArrayList<>();
    public List<Creature> creatures = new ArrayList<>();
    public int rows;
    public int columns;
    public Ldr teamLdr = new Ldr (10, "LDR", 0, 50);
    public Resistencia teamRes = new Resistencia (20, "RESISTENCIA", 0, 50);
    public Team currentTeam;

    public int turns;
    public int turnsWithoutTreasure; // Será usado no gameIsOver. Quando for for >= 15 gameIsOver = true;

    public String[][] getCreatureTypes(){
        System.out.println("Entrou em getCreatureTypes");
        return new String[][]{
                {"Anao", "dwarf.png", "add description", String.valueOf(1)},
                {"Dragao", "dragon.png", "add description", String.valueOf(9)},
                {"Elfo", "elf.png", "add description", String.valueOf(5)},
                {"Gigante", "giant.png", "add description", String.valueOf(5)},
                {"Humano", "human.png", "add description", String.valueOf(3)},
        };
    }
    public String[][] getSpellTypes(){
        System.out.println("Got inside getSpellTypes");
        return new String[][]{
                {"Empurra Para Norte", "Descrição do feitiço", String.valueOf(1)},
                {"Empurra Para Este", "Descrição do feitiço", String.valueOf(1)},
                {"Empurra Para Sul", "Descrição do feitiço", String.valueOf(1)},
                {"Empurra Para Oeste", "Descrição do feitiço", String.valueOf(1)},
                {"Reduz Alcance", "Descrição do feitiço", String.valueOf(2)},
                {"Duplica Alcance", "Descrição do feitiço", String.valueOf(3)},
                {"Congela", "Descrição do feitiço", String.valueOf(3)},
                {"Congela 4 Ever", "Descrição do feitiço", String.valueOf(10)},
                {"Descongela", "Descrição do feitiço", String.valueOf(8)},
        };
    }

    public Map<String, Integer> createComputerArmy(){
        System.out.println("Entrou em createComputerArmy");

        Map<String, Integer> computerArmy = new HashMap<>();

        // Criando 1 apenas para teste.
        computerArmy.put("Anao", 0); // criar um random entre 0 e 3.
        computerArmy.put("Dragao", 0);
        computerArmy.put("Elfo", 0);
        computerArmy.put("Gigante", 0);
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
    }

    public int getRows(){
        return rows;
    }
    public void setRows(int rows){
        this.rows = rows;
    }
    public int getColumns(){
        return columns;
    }
    public void setColumns(int columns){
        this.columns = columns;
    }
    public int startGame(String[] content, int rows, int columns){
        System.out.println("Entrou em startGame");
        setRows(rows);
        setColumns(columns);
        /*
        Deve inicializar as estruturas de dados
        relevantes para processar um jogo.
        O ​array content​ irá descrever o
        conteúdo inicial do mundo (criaturas e
        tesouros, buracos), tendo para isso várias
        Strings. Cada String vai representar um
        objecto do mundo. As Strings vão ter um
        dos seguintes formatos:
        Para criaturas:
        “id: <id>, type: <type>,
        teamId: <teamId>, x: <x>, y:
        <y>, orientation:
        <orientation>”
        Para tesouros:
        “id: <id>, type: treasure, x:
        <x>, y: <y>”
        Para buracos:
        “id: <id>, type: hole, x: <x>, y: <y>”
        As posições onde não exista nem uma
        criatura, nem um tesouro, nem um buraco,
        são posições vazias.
        Esta função tem de validar os 2 exércitos
        de forma a ver se respeitam o ​plafond​ de
        pontos inicial.
        Se ambos os exércitos respeitarem o
        plafond, ​ deve ser devolvido o valor
        numérico zero.
        Se nenhum dos exércitos respeitar o
        plafond respectivo, deve ser devolvido o
        valor numérico um.
        Se o exército da equipa LDR (Humano)
        não respeitar o seu ​plafond​, deve ser
        devolvido o valor dois.
        Se o exército da equipa RESISTENCIA não
        respeitar o seu ​plafond, ​ deve ser devolvido
        o valor numérico três.
        Os argumentos ​rows e columns
        vão-nos indicar as dimensões do
        tabuleiro.
        */
        for(String line: content){

            String[] individual_line = line.split(", ");

            if (individual_line.length >= 5 ) { // É criatura

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
                        treasures.add(new Gold (id, x, y, 3));
                    }
                    if(type.equals("silver")){
                        treasures.add(new Silver (id, x, y, 3));
                    }
                    if(type.equals("bronze")){
                        treasures.add(new Bronze (id, x, y, 3));
                    }
                }
            }
        }

        /* if creatures are from Resistência Army give them different images! Like negative filtered ones
        for(Creature creature: creaturesLdr){
            if (creature.getType().equals("baker")){
                stringType[0][1] = "bird.png";
            } else if (creature.getType().equals("milker")){
                stringType[0][1] = "simba.png";
            }
        }*/

        System.out.println(creatures);
        System.out.println(holes);
        System.out.println(treasures);
        System.out.println(teamRes.getId());
        System.out.println(teamLdr.getId());
        System.out.println(createComputerArmy());

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

    }
    public void setInitialTeam(int teamId){
        if (teamId == teamLdr.getId()){
            currentTeam = teamLdr;
        }else {
            currentTeam = teamRes;
        }
    }

    public void processTurn(){
        System.out.println("Entrou em  processTurn");

        /*
        Deve processar um turno do jogo. Inclui o
        movimento das criaturas.
        */

    }
    public List<Creature> getCreatures(){
        System.out.println("Estou em  getCreatures");
        return creatures;
    }

    public boolean gameIsOver(){
        System.out.println("Estou em gameIsOver");

        return false;
        /*Deve devolver ​true​ caso já tenha sido
        alcançada uma das condições de paragem
        do jogo e ​false​ em caso contrário.
        */
    }

    public List<String> getAuthors(){
        return Collections.singletonList("Allyson Rodrigues");
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

    public List<Element> joinElements(){
        List<Element> elements = new ArrayList<>();
        elements.addAll(creatures);
        elements.addAll(treasures);
        elements.addAll(holes);

        return elements;
    }
    public int getElementId(int x, int y){
        System.out.println("Estou em getElementId");

        List<Element> elements = joinElements();
        for(Element e: elements){
            if(e.getX() == x && e.getY() == y){
                return e.getId();
            }
        }
        return 0;

    }

    public int getCurrentTeamId(){
        System.out.println("Estou em currentTeamId");
        return currentTeam.getId();
    }

    public int getCurrentScore(int teamId){
        System.out.println("Estou em getCurrentTeamScore");
        return currentTeam.getPoints();
    }

    public boolean enchant (int x, int y, String spellName){
        System.out.println("Estou em enchant");
        return true;
    }

    public String getSpell (int x, int y){
        System.out.println("Estou em getSpell");
        return null;
    }

    public int getCoinTotal(int teamId){
        System.out.println("Estou em getCoinTotal");

        if (teamId == 10){
            return teamLdr.getCoins();
        } else{
            return teamRes.getCoins();
        }
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

        return "um jogador de futebol";
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
}
