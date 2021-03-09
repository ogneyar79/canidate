package persictence;

import model.Candidate;

public class MainCRUID {
    public static void main(String[] args) {
        Candidate candidateA = new Candidate("Petrov", "GrandMaster", 50);
        System.out.println("CandidateA :" + candidateA.toString());
        Candidate candidateB = new Candidate("Ivanov", "New", 20);
        Candidate candidateC = new Candidate("Cruglov", " Midle", 40);
        DAO example = DAO.instOf();
        Candidate resultA = example.create(candidateA);
        Candidate resultB = example.create(candidateB);
        Candidate resultC = example.create(candidateC);
        System.out.println(example.allCandidates());

        System.out.println(example.getById(resultA.getId()));
        System.out.println(example.getById(resultB.getId()));
        System.out.println(example.getById(resultC.getId()));

        System.out.println("Petrov :" + example.getByName("Petrov"));
        System.out.println("Ivanov :" + example.getByName("Ivanov"));
        System.out.println("Cruglov :" + example.getByName("Cruglov"));

        example.update(candidateA, resultA.getId());
        example.update(candidateB, resultB.getId());
        example.update(candidateC, resultC.getId());
        example.delete(resultA.getId());
        example.delete(resultB.getId());
        example.delete(resultC.getId());

        System.out.println(example.allCandidates());
    }

}
