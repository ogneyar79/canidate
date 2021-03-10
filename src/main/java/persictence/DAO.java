package persictence;

import model.Candidate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class DAO implements AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private DAO() {

    }

    /**
     * Create singleton.
     */
    private static final class Lazy {
        private static final DAO INST = new DAO();
    }

    public static DAO instOf() {
        return DAO.Lazy.INST;
    }

    public Candidate create(Candidate candidate) {
        Serializable id = makeTransaction(session -> session.save(candidate));
        return makeTransaction(session -> session.get(Candidate.class, id));
    }

    public Candidate getById(int id) {
        return makeTransaction(session -> session.get(Candidate.class, id));
    }

    public Candidate getByCandidate(Candidate candidate) {
        return (Candidate) this.makeTransaction(session -> session.createQuery("from Candidate c where c.id=" + candidate.getId()).getSingleResult());
    }

    public Candidate getByName(String name) {
        return (Candidate) makeTransaction(session -> session.createQuery("from Candidate c where c.name= :name")
                .setParameter("name", name).list().get(0));
    }

    public void update(Candidate candidate, int id) {
        makeTransaction(session -> session.createQuery("update Candidate c set c.name = :newName, c.experience = :newExperience, c.salary = :newSalary" +
                " where c.id = :fId")
                .setParameter("newName", candidate.getName())
                .setParameter("newExperience", candidate.getExperience())
                .setParameter("newSalary", candidate.getSalary())
                .setParameter("fId", id)
                .executeUpdate());
    }

    public void delete(int id) {
        makeTransaction(session ->
                session.createQuery("delete from Candidate where id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate());
    }

    public Collection<Candidate> allCandidates() {
        return makeTransaction(session ->
                session.createQuery("from Candidate ").list());
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public void deleteAll() {
        makeTransaction(session -> session.createQuery("delete from  Candidate ").executeUpdate());
    }

    private <T> T makeTransaction(final Function<Session, T> operationCRUID) {
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        T result = operationCRUID.apply(session);
        transaction.commit();
        session.close();
        return result;
    }
}
