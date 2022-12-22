package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JpaMain {
    public static void main(String[] args) {
//      어플리케이션 실행시점에서 딱 1개 생성되는 entityManagerFactory(공유 하면 안됨)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

//      요청시에 생성
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

//      code!!!
        try{

            Team teamA = new Team();
            teamA.setUsername("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setUsername("팀B");
            em.persist(teamB);

            Member member = new Member();
            member.setAge(10);
            member.setUsername("aa");
            member.setTeam(teamA);
            em.persist(member);

            Member member2 = new Member();
            member2.setAge(10);
            member2.setUsername("ab");
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setAge(10);
            member3.setUsername("ac");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            List<Member> username = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", member2.getUsername()).getResultList();
            System.out.println("username = " + username);

//          FLUSH 자동 호출 commit, query시에 자동 호출 <- 이 이후 쿼리를 위하여
            int resultCount = em.createQuery("update Member m set m.age = m.age+20").executeUpdate();
//          여기이후에 flush 해주는 것이 좋다(db에만 반영)
            em.clear();

            Member findMember = em.find(Member.class,member.getId());
            System.out.println("findMember = " + findMember);
            System.out.println("resultCount = " + resultCount);



//            String query = "select  m From Member m where m.team = :team";
//            Member findMember = em.createQuery(query, Member.class)
//                    .setParameter("team",teamA).getSingleResult();
//            System.out.println("findMember = " + findMember);
//

            tx.commit();


        }catch (Exception e){
           tx.rollback();
           e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }


}
