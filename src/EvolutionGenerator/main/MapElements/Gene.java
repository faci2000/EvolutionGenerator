package MapElements;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gene {
    HashMap<Direction,Integer> genome;

    public Gene(int[] genome){
        this.genome = new HashMap<Direction, Integer>();
        for(int i=0;i<Direction.values().length;i++){
            this.genome.put(Direction.values()[i],genome[i]);
        }
    }

    public Direction generateDirection(){
        Random random = new Random();
        int randomValue = random.nextInt(31);
        int i=0;
        while (randomValue>=0){
            randomValue-=(Integer)this.genome.values().toArray()[i];
            i+=1;
        }
        return (Direction)this.genome.keySet().toArray()[i-1];
    }

    private int[] getGenes(int numberOfNeededGenes){
        int[] partialGenome=new int[8];
        Random random = new Random();
        while(numberOfNeededGenes>0){
            int randomValue = random.nextInt(7);
            if(this.genome.get(Direction.values()[randomValue])-partialGenome[randomValue]-1>0){
                partialGenome[randomValue]+=1;
                numberOfNeededGenes-=1;
            }
        }
        return partialGenome;
    }

    static public Gene generateNewGene(Gene mainGene, Gene secondGene){
        int[] newGenome = IntStream.range(0,8).map(n->new int[]{1,1,1,1,1,1,1,1}[n]+
                                                    mainGene.getGenes(16)[n]+
                                                    secondGene.getGenes(8)[n]).toArray();
        return new Gene(newGenome);
    }

    @Override
    public String toString() {
        return genome.entrySet().stream().map(n-> n.getKey().toString()+"->"+n.getValue().toString()).collect(Collectors.joining(", "));
    }
}