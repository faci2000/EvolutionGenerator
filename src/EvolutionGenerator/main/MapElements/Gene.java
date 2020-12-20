package MapElements;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gene {
    private HashMap<Direction,Integer> genome;

    public Gene(int[] genome){
        this.setGenome(new HashMap<>());
        for(int i=0;i<Direction.values().length;i++){
            this.getGenome().put(Direction.values()[i],genome[i]);
        }
    }

    public Direction generateDirection(){
        Random random = new Random();
        int randomValue = random.nextInt(31);
        int i=0;
        while (randomValue>=0){
            randomValue-=(Integer) this.getGenome().values().toArray()[i];
            i+=1;
        }
        return (Direction) this.getGenome().keySet().toArray()[i-1];
    }

    private int[] getGenes(int numberOfNeededGenes){
        int[] partialGenome=new int[8];
        Random random = new Random();
        while(numberOfNeededGenes>0){
            int randomValue = random.nextInt(7);
            if(this.getGenome().get(Direction.values()[randomValue])-partialGenome[randomValue]-1>0){
                partialGenome[randomValue]+=1;
                numberOfNeededGenes-=1;
            }
        }
        return partialGenome;
    }

    static public Gene generateNewGene(Gene mainGene, Gene secondGene){
        int [] tempGenomeA = mainGene.getGenes(16);
        int [] tempGenomeB = secondGene.getGenes(8);
        int[] newGenome = IntStream.range(0,8).map(n->new int[]{1,1,1,1,1,1,1,1}[n]+
                                                    tempGenomeA[n]+
                                                    tempGenomeB[n]).toArray();
        return new Gene(newGenome);
    }

    static public Gene generateRandomGene(){
        int[] partialGenome=new int[8];
        Random random = new Random();
        for(int i=0;i<24;i++){
            partialGenome[random.nextInt(8)]+=1;
        }
        int[] newGenome = IntStream.range(0,8).map(n->new int[]{1,1,1,1,1,1,1,1}[n]+
                partialGenome[n]).toArray();
        return new Gene(newGenome);
    }

    @Override
    public String toString() {
        return getGenome().entrySet().stream().map(n-> n.getKey().toString()+"->"+n.getValue().toString()).collect(Collectors.joining(", "));
    }

    public HashMap<Direction, Integer> getGenome() {
        return genome;
    }

    public void setGenome(HashMap<Direction, Integer> genome) {
        this.genome = genome;
    }

    public int[] getIntGenome(){
        int[] returnGenome = new int[8];
        for(int i=0;i<Direction.values().length;i++){
           returnGenome[i]=this.genome.get(Direction.values()[i]);
        }
        return  returnGenome;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genome.get(Direction.N),
                genome.get(Direction.NE),
                genome.get(Direction.E),
                genome.get(Direction.SE),
                genome.get(Direction.S),
                genome.get(Direction.SW),
                genome.get(Direction.W),
                genome.get(Direction.NW));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Gene){
            for(int i=0;i<8;i++)
                if(!this.genome.get(Direction.values()[i]).equals(((Gene) obj).genome.get(Direction.values()[i])))
                    return false;
            return true;
        }
        return false;
    }
}