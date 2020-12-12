package MapElementsTests;

import MapElements.Direction;
import MapElements.Gene;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneTest {
    Gene testGene;
    @Test
    public void generateDirectionTest(){
        testGene = new Gene(new int[]{4,4,4,4,4,4,4,4});
        for(int i=0;i<100;i++){
            assertNotNull(testGene.generateDirection() );
            System.out.println(testGene.generateDirection());
        }
        testGene=new Gene(new int[]{25,1,1,1,1,1,1,1});
        for(int i=0;i<100;i++){
            assertNotNull(testGene.generateDirection() );
            System.out.println(testGene.generateDirection());
        }
    }

    @Test
    public void getGenerateNewGeneTest(){
        testGene = new Gene(new int[]{4,4,4,4,4,4,4,4});
        Gene secondGene = new Gene(new int[]{4,4,4,4,4,4,4,4});
        for(int i=0;i<10;i++){
            assertNotNull(Gene.generateNewGene(testGene,secondGene));
            System.out.println(Gene.generateNewGene(testGene,secondGene));
        }
        testGene=new Gene(new int[]{25,1,1,1,1,1,1,1});
        for(int i=0;i<10;i++){
            assertNotNull(Gene.generateNewGene(testGene,secondGene));
            System.out.println(Gene.generateNewGene(testGene,secondGene));
        }
    }
}
