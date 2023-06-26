package seamcarving;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.washington.cse373.BaseTest;
import seamcarving.energy.DualGradientEnergyFunction;
import seamcarving.energy.EnergyFunction;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

public abstract class BasicSeamFinderTests extends BaseTest {
    protected abstract SeamFinder createSeamFinder();

    protected EnergyFunction createEnergyFunction() {
        return new DualGradientEnergyFunction();
    }

    protected Picture loadPicture(String name) {
        return new Picture(Path.of("data").resolve(name).toFile());
    }

    protected SeamFinderAssert assertThat(SeamFinder seamFinder) {
        return new SeamFinderAssert(seamFinder);
    }

    @Test
    public void findVerticalSeam_returnsCorrectSeam() {
        Picture p = loadPicture("6x5.png");
        EnergyFunction energyFunction = createEnergyFunction();
        double[][] energies = SeamCarver.computeEnergies(p, energyFunction);

        SeamFinder seamFinder = createSeamFinder();
        assertThat(seamFinder).verticalSeam(energies).hasSameEnergyAs(4, 4, 3, 2, 2);
    }

    @Test
    public void findHorizontalSeam_returnsCorrectSeam() {
        Picture p = loadPicture("6x5.png");
        EnergyFunction energyFunction = createEnergyFunction();
        double[][] energies = SeamCarver.computeEnergies(p, energyFunction);

        SeamFinder seamFinder = createSeamFinder();
        assertThat(seamFinder).horizontalSeam(energies).hasSameEnergyAs(2, 2, 1, 2, 1, 1);
    }

    @Test
    public void customer_test() {
        Picture p = loadPicture("chameleon.png");
        EnergyFunction energyFunction = createEnergyFunction();
        double[][] energies = SeamCarver.computeEnergies(p, energyFunction);
        // double[][] shrink = new double[10][10];
        //
        // for (int i = 0; i < 10; i++) {
        //     for (int j = 0; j < 10; j++) {
        //         shrink[i][j] = energies[i + 53][j + 131];
        //     }
        // }

        SeamFinder seamFinder = createSeamFinder();
        List<Integer> t1 = seamFinder.findVerticalSeam(energies);
        System.out.println(t1);

        double seamEnergy = 0.0;
        for (int j = 0; j < energies[0].length; j++) {
            seamEnergy += energies[t1.get(j)][j];
        }
        System.out.println(seamEnergy);

    }

}
